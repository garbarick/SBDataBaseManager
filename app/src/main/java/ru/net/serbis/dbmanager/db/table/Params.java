package ru.net.serbis.dbmanager.db.table;

import android.database.sqlite.*;

public class Params extends Table
{
    @Override
    public void make(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        make(db,
             "create table if not exists params(" +
             "    db_id integer references databases(id)," +
             "    name text," +
             "    value text" +
             ")");
    }
}
