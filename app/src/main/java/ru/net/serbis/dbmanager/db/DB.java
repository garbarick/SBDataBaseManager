package ru.net.serbis.dbmanager.db;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.*;
import ru.net.serbis.dbmanager.sh.*;

public class DB
{
    private interface Call<T>
    {
        T call(SQLiteDatabase db);
    }

    private Shell sh = new Shell();
    private App app;
    private String db;
    private Context context;
    private App thisApp;

    public DB(Context context, App app, String db)
    {
        this.app = app;
        this.db = db;
        this.context = context;
        this.thisApp = new App(context.getPackageName());
    }
    
    private <T> T run(Call<T> call)
    {
        try
        {
            return runByChmod(call);
        }
        catch (SQLiteDatabaseLockedException e)
        {
            return runByCopy(call);
        }
    }

    private <T> T runByChmod(Call<T> call)
    {
        File file = app.getDBFile(db);
        File journal = app.getJournalFile(db);
        try
        {
            sh.command(
                "chmod 666 " + file.getAbsolutePath(),
                "chmod 666 " + journal.getAbsolutePath());

            return runInDB(file, call);
        }
        finally
        {
            sh.command(
                "chmod 660 " + file.getAbsolutePath(),
                "chmod 600 " + journal.getAbsolutePath());
        }
    }

    private <T> T runByCopy(Call<T> call)
    {
        File file = app.getDBFile(db);
        File journal = app.getJournalFile(db);
        File contextFile = thisApp.getDBFile(db);
        File contextJournal = thisApp.getJournalFile(db);
        thisApp.getDataBaseDir().mkdir();
        try
        {
            sh.command(
                "cp " + file.getAbsolutePath() + " " + contextFile.getAbsolutePath(),
                "cp " + journal.getAbsolutePath() + " " + contextJournal.getAbsolutePath(),
                "chmod 666 " + contextFile.getAbsolutePath(),
                "chmod 666 " + contextJournal.getAbsolutePath());

            return runInDB(contextFile, call);
        }
        finally
        {
            contextFile.delete();
            contextJournal.delete();
            thisApp.getDataBaseDir().delete();
        }
    }
    
    private <T> T runInDB(File file, Call<T> call)
    {
        SQLiteDatabase db = null;
        try
        {
            db = SQLiteDatabase.openDatabase(
                file.getAbsolutePath(),
                null,
                SQLiteDatabase.OPEN_READWRITE);
                
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

    public List<List<String>> select(final String query, final boolean withColumnName)
    {
        return run(
            new Call<List<List<String>>>()
            {
                public List<List<String>> call(SQLiteDatabase db)
                {
                    return selectInDB(db, query, withColumnName);
                }
            }
        );
    }

    private List<List<String>> selectInDB(SQLiteDatabase db, String query, boolean withColumnName)
    {
        List<List<String>> result = new ArrayList<List<String>>();
        Cursor cursor = db.rawQuery(query, null);
        if (withColumnName)
        {
            result.add(getHeader(cursor));
        }
        result.addAll(getRows(cursor));
        return result;
    }

    private List<String> getHeader(Cursor cursor)
    {
        List<String> result = new ArrayList<String>();
        result.add(context.getResources().getString(R.string.num));
        int count = cursor.getColumnCount();
        for (int i = 0; i < count; i++)
        {
            result.add(cursor.getColumnName(i));
        }
        return result;
    }
    
    private List<List<String>> getRows(Cursor cursor)
    {
        List<List<String>> result = new ArrayList<List<String>>();
        if (cursor.moveToFirst())
        {
            int y = 1;
            int count = cursor.getColumnCount();
            do
            {
                List<String> row = new ArrayList<String>();
                row.add(String.valueOf(y++));
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
            return cursor.getString(i);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
