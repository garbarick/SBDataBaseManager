package ru.net.serbis.dbmanager.app.db;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.io.*;
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

    public DBResult select(final String query, final boolean withColumns, final boolean withRowNum, final String... args)
    {
        boolean read = query.toLowerCase().startsWith("select ");
        return run(
            new DbCall<DBResult>()
            {
                public DBResult call(SQLiteDatabase db)
                {
                    return selectInDB(db, query, withColumns, withRowNum, args);
                }
            }, read
        );
    }
    
    public DBResult selectTable(final String table, final boolean withColumns, final boolean withTypes, final boolean withRowNum)
    {
        return run(
            new DbCall<DBResult>()
            {
                public DBResult call(SQLiteDatabase db)
                {
                    DBResult result = selectInDB(db, "select * from " + table, withColumns, withRowNum);
                    initTypes(db, result, table);
                    return result;
                }
            }, true
        );
    }

    private DBResult selectInDB(SQLiteDatabase db, String query, boolean withColumns, boolean withRowNum, String... args)
    {
        Cursor cursor = db.rawQuery(query, args);
        DBResult result = new DBResult();
        result.init(context, params, cursor, withColumns, withRowNum);
        return result;
    }
    
    private void initTypes(SQLiteDatabase db, DBResult result, String table)
    {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + table + ")", null);
        result.initTypes(cursor);
    }

    public void execute(final String query, final String... args)
    {
        run(
            new DbCall<Void>()
            {
                public Void call(SQLiteDatabase db)
                {
                    db.execSQL(query, new Bind().convertData(params, args));
                    return null;
                }
            }, false
        );
    }
}
