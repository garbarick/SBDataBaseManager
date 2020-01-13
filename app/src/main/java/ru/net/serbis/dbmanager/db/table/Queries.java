package ru.net.serbis.dbmanager.db.table;

import android.database.sqlite.*;

public class Queries extends Table
{
    @Override
    public void make(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        make(db,
             "create table if not exists queries(" +
             "    id integer primary key autoincrement," +
             "    db_id integer references databases(id)," +
             "    name text," +
             "    query text" +
             ")");
    }
}
