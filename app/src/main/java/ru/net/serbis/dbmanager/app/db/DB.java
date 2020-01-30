package ru.net.serbis.dbmanager.app.db;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.*;
import ru.net.serbis.dbmanager.param.*;
import ru.net.serbis.dbmanager.sh.*;

public class DB
{
    private Shell sh = new Shell();
    private AppDb appDb;
    private Context context;
    private ThisApp thisApp;
    private ParamMap params = new ParamMap();
    private boolean readOnly;

    public DB(Context context, AppDb appDb)
    {
        this.appDb = appDb;
        this.context = context;
        this.thisApp = new ThisApp(context.getPackageName());
    }
    
    public DB(Context context, AppDb appDb, ParamMap params)
    {
        this(context, appDb);
        this.params = params;
    }

    public boolean isReadOnly()
    {
        return readOnly;
    }

    public <T> T run(DbCall<T> call, boolean read)
    {
        try
        {
            return runInDb(call, read);
        }
        catch (SQLiteCantOpenDatabaseException openError)
        {
            try
            {
                return runByChmod(call, read);
            }
            catch (SQLiteDatabaseLockedException lockError)
            {
                readOnly = true;
                return runByCopy(call, read);
            }
        }
    }
    
    private <T> T runInDb(DbCall<T> call, boolean read)
    {
        File file = appDb.getDBFile();
        return runInDB(file, call, read);
    }

    private <T> T runByChmod(DbCall<T> call, boolean read)
    {
        File file = appDb.getDBFile();
        File journal = appDb.getJournalFile();
        try
        {
            sh.command(
                "chmod 666 " + file.getAbsolutePath(),
                "chmod 666 " + journal.getAbsolutePath());

            return runInDB(file, call, read);
        }
        finally
        {
            sh.command(
                "chmod 660 " + file.getAbsolutePath(),
                "chmod 600 " + journal.getAbsolutePath());
        }
    }

    private <T> T runByCopy(DbCall<T> call, boolean read)
    {
        File file = appDb.getDBFile();
        File journal = appDb.getJournalFile();
        File contextFile = thisApp.getDBFile(appDb);
        File contextJournal = thisApp.getJournalFile(appDb);
        thisApp.getDataBaseDir(appDb).mkdirs();
        try
        {
            sh.command(
                "cp " + file.getAbsolutePath() + " " + contextFile.getAbsolutePath(),
                "cp " + journal.getAbsolutePath() + " " + contextJournal.getAbsolutePath(),
                "chmod 666 " + contextFile.getAbsolutePath(),
                "chmod 666 " + contextJournal.getAbsolutePath());

            return runInDB(contextFile, call, read);
        }
        finally
        {
            contextFile.delete();
            contextJournal.delete();
            thisApp.getDataBaseDir(appDb).delete();
        }
    }
    
    private <T> T runInDB(File file, DbCall<T> call, boolean read)
    {
        SQLiteDatabase db = null;
        try
        {
            db = SQLiteDatabase.openDatabase(
                file.getAbsolutePath(),
                null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS |
                (read ? SQLiteDatabase.OPEN_READONLY : SQLiteDatabase.OPEN_READWRITE));
                
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

    public List<List<String>> select(final String query, final boolean withColumnName, final boolean withRowNum, final String... args)
    {
        boolean read = query.toLowerCase().startsWith("select ");
        return run(
            new DbCall<List<List<String>>>()
            {
                public List<List<String>> call(SQLiteDatabase db)
                {
                    return selectInDB(db, query, withColumnName, withRowNum, args);
                }
            }, read
        );
    }

    private List<List<String>> selectInDB(SQLiteDatabase db, String query, boolean withColumnName, boolean withRowNum, String... args)
    {
        List<List<String>> result = new ArrayList<List<String>>();
        Cursor cursor = db.rawQuery(query, args);
        if (withColumnName)
        {
            result.add(getHeader(cursor, withRowNum));
        }
        result.addAll(getRows(cursor, withRowNum));
        return result;
    }

    private List<String> getHeader(Cursor cursor, boolean withRowNum)
    {
        List<String> result = new ArrayList<String>();
        if (withRowNum)
        {
            result.add(context.getResources().getString(R.string.num));
        }
        int count = cursor.getColumnCount();
        for (int i = 0; i < count; i++)
        {
            result.add(cursor.getColumnName(i));
        }
        return result;
    }
    
    private List<List<String>> getRows(Cursor cursor, boolean withRowNum)
    {
        List<List<String>> result = new ArrayList<List<String>>();
        if (cursor.moveToFirst())
        {
            int y = 1;
            int count = cursor.getColumnCount();
            do
            {
                List<String> row = new ArrayList<String>();
                if (withRowNum)
                {
                    row.add(String.valueOf(y++));
                }
                for (int i = 0; i < count; i++)
                {
                    row.add(getString(cursor, i));
                }
                result.add(row);
            }
            while(cursor.moveToNext());
        }
        return result;
    }
    
    private String getString(Cursor cursor, int i)
    {
        try
        {
            if (params.containsKey(Constants.CHARSET))
            {
                String charset = params.get(Constants.CHARSET);
                return new String(cursor.getBlob(i), charset);
            }
            return cursor.getString(i);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    public void execute(final String query, final String... args)
    {
        run(
            new DbCall<Void>()
            {
                public Void call(SQLiteDatabase db)
                {
                    db.execSQL(query, args);
                    return null;
                }
            }, false
        );
    }
}
