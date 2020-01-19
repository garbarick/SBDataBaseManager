package ru.net.serbis.dbmanager.db;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.db.table.*;
import ru.net.serbis.dbmanager.db.table.migrate.*;
import ru.net.serbis.dbmanager.param.*;
import ru.net.serbis.dbmanager.query.*;

import ru.net.serbis.dbmanager.db.table.DataBases;
import ru.net.serbis.dbmanager.db.table.Params;
import ru.net.serbis.dbmanager.db.table.Queries;

public class Helper extends SQLiteOpenHelper
{
    public Helper(Context context)
    {
        super(context, "db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createTables(db, 0, 0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        createTables(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void createTables(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        new DataBases().make(db, oldVersion, newVersion);
        new Queries().make(db, oldVersion, newVersion);
        new Widgets().make(db, oldVersion, newVersion);
        new Params().make(db, oldVersion, newVersion);
        
        //migrate
        new From2().make(db, oldVersion, newVersion);
    }

    private <T> T runInDB(Call<T> call, boolean write)
    {
        SQLiteDatabase db = null;
        try
        {
            db = write ? getWritableDatabase() : getReadableDatabase();
            return call.call(db);
        }
        finally
        {
            close(db);
        }
    }

    private void close(SQLiteDatabase db)
    {
        if (db != null)
        {
            db.close();
        }
    }

    public List<Query> getQueries(final String packageName, final String dbName)
    {
        return runInDB(
            new Call<List<Query>>()
            {
                public List<Query> call(SQLiteDatabase db)
                {
                    return getQueries(db, packageName, dbName);
                }
            },
            false
        );
    }

    private List<Query> getQueries(SQLiteDatabase db, String packageName, String dbName)
    {
        List<Query> result = new ArrayList<Query>();
        Cursor cursor  = db.query("queries q, databases d", new String[]{"q.id", "q.name", "q.query"}, "d.id = q.db_id and d.package = ? and d.name = ?", new String[]{packageName, dbName}, null, null, "q.name");
        if (cursor.moveToFirst())
        {
            do
            {
                Query query = new Query(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2));
                result.add(query);
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    public boolean addQuery(final Query query, final AppDb appDb)
    {
        return runInDB(
            new Call<Boolean>()
            {
                public Boolean call(SQLiteDatabase db)
                {
                    return addQuery(db, query, appDb);
                }
            },
            true
        );
    }
    
    private long getDbId(SQLiteDatabase db, AppDb appDb, boolean create)
    {
        Cursor cursor  = db.query("databases", new String[]{"id"}, "package = ? and name = ?", new String[]{appDb.getApp().getPackage(), appDb.getDb()}, null, null, null);
        if (cursor.moveToFirst())
        {
            return cursor.getLong(0);
        }
        
        if (!create)
        {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put("package", appDb.getApp().getPackage());
        values.put("name", appDb.getDb());
        return db.insert("databases", null, values);
    }

    private boolean addQuery(SQLiteDatabase db, Query query, AppDb appDb)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("db_id", getDbId(db, appDb, true));
            values.put("name", query.getName());
            values.put("query", query.getQuery());

            long id = db.insert("queries", null, values);
            query.setId(id);
            return true;
        }
        catch (Exception e)
        {
            Log.info(this, e);
            return false;
        }
    }

    public boolean updateQuery(final Query query)
    {
        return runInDB(
            new Call<Boolean>()
            {
                public Boolean call(SQLiteDatabase db)
                {
                    return updateQuery(db, query);
                }
            },
            true
        );
    }

    private boolean updateQuery(SQLiteDatabase db, Query query)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("name", query.getName());
            values.put("query", query.getQuery());

            int count = db.update("queries", values, "id = ?", new String[]{String.valueOf(query.getId())});
            return count == 1;
        }
        catch (Exception e)
        {
            Log.info(this, e);
            return false;
        }
    }

    public boolean deleteQuery(final Query query)
    {
        return runInDB(
            new Call<Boolean>()
            {
                public Boolean call(SQLiteDatabase db)
                {
                    return deleteQuery(db, query);
                }
            },
            true
        );
    }

    private boolean deleteQuery(SQLiteDatabase db, Query query)
    {
        try
        {
            int count = db.delete("queries", "id = ?", new String[]{String.valueOf(query.getId())});
            return count == 1;
        }
        catch (Exception e)
        {
            Log.info(this, e);
            return false;
        }
    }

    public List<AppDbQuery> getQueries()
    {
        return runInDB(
            new Call<List<AppDbQuery>>()
            {
                public List<AppDbQuery> call(SQLiteDatabase db)
                {
                    return getQueries(db);
                }
            },
            false
        );
    }

    private List<AppDbQuery> getQueries(SQLiteDatabase db)
    {
        List<AppDbQuery> result = new ArrayList<AppDbQuery>();
        Cursor cursor  = db.query("queries q, databases d", new String[]{"q.id", "d.package", "d.name", "q.name", "q.query"}, "d.id = q.db_id", null, null, null, "q.name");
        if (cursor.moveToFirst())
        {
            do
            {
                result.add(getQuery(cursor));
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    public AppDbQuery getQuery(final int widgetId)
    {
        return runInDB(
            new Call<AppDbQuery>()
            {
                public AppDbQuery call(SQLiteDatabase db)
                {
                    return getQuery(db, widgetId);
                }
            },
            false
        );
    }

    private AppDbQuery getQuery(SQLiteDatabase db, Integer widgetId)
    {
        Cursor cursor  = db.query("queries q, databases d, widgets w", new String[]{"q.id", "d.package", "d.name", "q.name", "q.query"}, "d.id = q.db_id and w.query_id = q.id and w.id = ?", new String[]{widgetId.toString()}, null, null, null);
        if (cursor.moveToFirst())
        {
            return getQuery(cursor);
        }
        return null;
    }

    private AppDbQuery getQuery(Cursor cursor)
    {
        String packageName = cursor.getString(1);
        App app = Constants.STORAGE.equals(packageName) ? new Storage() : new App(packageName);
        return new AppDbQuery(
            new AppDb(app, cursor.getString(2)),
            new Query(
                cursor.getLong(0),
                cursor.getString(3),
                cursor.getString(4)));
    }

    public boolean addWidget(final int widgetId, final long queryId)
    {
        return runInDB(
            new Call<Boolean>()
            {
                public Boolean call(SQLiteDatabase db)
                {
                    return addWidget(db, widgetId, queryId);
                }
            },
            true
        );
    }

    private boolean addWidget(SQLiteDatabase db, int widgetId, long queryId)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("id", widgetId);
            values.put("query_id", queryId);

            db.insert("widgets", null, values);
            return true;
        }
        catch (Exception e)
        {
            Log.info(this, e);
            return false;
        }
    }

    public boolean deleteWidget(final int widgetId)
    {
        return runInDB(
            new Call<Boolean>()
            {
                public Boolean call(SQLiteDatabase db)
                {
                    return deleteWidget(db, widgetId);
                }
            },
            true
        );
    }

    private boolean deleteWidget(SQLiteDatabase db, Integer widgetId)
    {
        try
        {
            int count = db.delete("widgets", "id = ?", new String[]{widgetId.toString()});
            return count == 1;
        }
        catch (Exception e)
        {
            Log.info(this, e);
            return false;
        }
    }

    public Map<String, String> getParams(final AppDb appDb)
    {
        return runInDB(
            new Call<Map<String, String>>()
            {
                public Map<String, String> call(SQLiteDatabase db)
                {
                    return getParams(db, appDb);
                }
            },
            false
        );
    }

    public Map<String, String> getParams(SQLiteDatabase db, AppDb appDb)
    {
        Map<String, String> result = new HashMap<String, String>();
        try
        {
            long dbId = getDbId(db, appDb, false);
            if (dbId > -1)
            {
                Cursor cursor  = db.query("params", new String[]{"name", "value"}, "db_id = ?", new String[]{String.valueOf(dbId)}, null, null, null);
                if (cursor.moveToFirst())
                {
                    do
                    {
                        result.put(cursor.getString(0), cursor.getString(1));
                    }
                    while(cursor.moveToNext());
                }
            }
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
        return result;
    }

    public void initParams(final AppDb appDb, final List<ParamKey> params)
    {
        runInDB(
            new Call<Void>()
            {
                public Void call(SQLiteDatabase db)
                {
                    initParams(db, appDb, params);
                    return null;
                }
            },
            false
        );
    }
    
    private void initParams(SQLiteDatabase db, AppDb appDb, List<ParamKey> params)
    {
        try
        {
            long dbId = getDbId(db, appDb, false);
            if (dbId == -1)
            {
                return;
            }
            for (ParamKey param : params)
            {
                Cursor cursor  = db.query("params", new String[]{"value"}, "db_id = ? and name = ?", new String[]{String.valueOf(dbId), param.getKey()}, null, null, null);
                if (cursor.moveToFirst())
                {
                    param.setValue(cursor.getString(0));
                }
            }
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
    }
    
    public boolean setParam(final AppDb appDb, final ParamKey param)
    {
        return runInDB(
            new Call<Boolean>()
            {
                public Boolean call(SQLiteDatabase db)
                {
                    return setParam(db, appDb, param);
                }
            },
            true
        );
    }
    
    private boolean setParam(SQLiteDatabase db, AppDb appDb, ParamKey param)
    {
        try
        {
            long dbId = getDbId(db, appDb, true);
            db.delete("params", "db_id = ? and name = ?", new String[]{String.valueOf(dbId), param.getKey()});
            if (param.getValue() == null)
            {
                return true;
            }
            ContentValues values = new ContentValues();
            values.put("db_id", dbId);
            values.put("name", param.getKey());
            values.put("value", param.getValue());
            db.insert("params", null, values);
            return true;
        }
        catch (Exception e)
        {
            Log.info(this, e);
            return false;
        }
    }
}
