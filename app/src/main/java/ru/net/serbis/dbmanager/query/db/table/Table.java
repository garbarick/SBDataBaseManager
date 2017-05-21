package ru.net.serbis.dbmanager.query.db.table;

import android.database.sqlite.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.query.db.*;

public abstract class Table implements Call
{
    @Override
    public Object call(SQLiteDatabase db)
    {
        try
        {
            db.execSQL(getScript());
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
        return null;
    }

    protected abstract String getScript();
}
