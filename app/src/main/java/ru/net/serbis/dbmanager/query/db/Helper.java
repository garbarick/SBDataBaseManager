package ru.net.serbis.dbmanager.query.db;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.query.db.table.*;

import ru.net.serbis.dbmanager.query.db.table.Queries;

public class Helper extends SQLiteOpenHelper
{
    public Helper(Context context)
    {
        super(context, "db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        createTables(db);
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

    private void createTables(SQLiteDatabase db)
    {
        new Queries().call(db);
        new Widgets().call(db);
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
        Cursor cursor  = db.query("queries", new String[]{"id", "name", "query"}, "package = ? and db = ?", new String[]{packageName, dbName}, null, null, "name");
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

    public boolean addQuery(final Query query, final String packageName, final String dbName)
    {
        return runInDB(
            new Call<Boolean>()
            {
                public Boolean call(SQLiteDatabase db)
                {
                    return addQuery(db, query, packageName, dbName);
                }
            },
            true
        );
    }

    private boolean addQuery(SQLiteDatabase db, Query query, String packageName, String dbName)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("package", packageName);
            values.put("db", dbName);
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
        Cursor cursor  = db.query("queries", new String[]{"id", "package", "db", "name", "query"}, null, null, null, null, "name");
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
        Cursor cursor  = db.query("queries", new String[]{"id", "package", "db", "name", "query"}, "id = (select query_id from widgets where id = ?)", new String[]{widgetId.toString()}, null, null, null);
        if (cursor.moveToFirst())
        {
            return getQuery(cursor);
        }
        return null;
    }

    private AppDbQuery getQuery(Cursor cursor)
    {
        return new AppDbQuery(
            new App(cursor.getString(1)),
            cursor.getString(2),
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
}
