package ru.net.serbis.dbmanager.db.table;

import android.database.sqlite.*;

public class Widgets extends Table
{
    @Override
    public void make(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        make(db,
             "create table if not exists widgets(" +
             "    id integer," +
             "    query_id integer references queries(id)" +
             ")");
    }
}
