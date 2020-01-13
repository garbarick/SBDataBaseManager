package ru.net.serbis.dbmanager.db.table.migrate;

import ru.net.serbis.dbmanager.db.table.*;
import android.database.sqlite.*;

public abstract class Migrate extends Table
{
    @Override
    public void make(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (need(oldVersion, newVersion))
        {
            update(db);
        }
    }
    
    protected abstract boolean need(int oldVersion, int newVersion)
    
    protected abstract void update(SQLiteDatabase db)
}
