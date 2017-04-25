package ru.net.serbis.dbmanager.query;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public class Helper extends SQLiteOpenHelper
{
    private interface Call<T>
    {
        T call(SQLiteDatabase db);
    }

    public Helper(Context context)
    {
        super(context, "db", null, 1);
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
        try
        {
            createaQueriesTable(db);
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
    }

    private void createaQueriesTable(SQLiteDatabase db)
    {
        db.execSQL(
            "create table queries(" +
            "    id integer primary key autoincrement," +
            "    package text," +
            "    db text," +
            "    name text," +
            "    query text" +
            ")");
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
}
