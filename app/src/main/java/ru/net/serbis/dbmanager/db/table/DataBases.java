package ru.net.serbis.dbmanager.db.table;

import android.database.sqlite.*;

public class DataBases extends Table
{
    @Override
    public void make(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        make(db,
             "create table if not exists databases(" +
             "    id integer primary key autoincrement," +
             "    package text," +
             "    name text" +
             ")");
    }
}
