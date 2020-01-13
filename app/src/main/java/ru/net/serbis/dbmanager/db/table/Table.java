package ru.net.serbis.dbmanager.db.table;

import android.database.sqlite.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.db.*;

public abstract class Table
{
    public abstract void make(SQLiteDatabase db, int oldVersion, int newVersion)
    
    protected void make(SQLiteDatabase db, String query)
    {
        try
        {
            db.execSQL(query);
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
    }
}
