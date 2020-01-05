package ru.net.serbis.dbmanager.query.db.table;

import android.database.sqlite.*;

public class Queries extends Table
{
    @Override
    protected String getScript()
    {
        return "create table queries(" +
            "    id integer primary key autoincrement," +
            "    package text," +
            "    db text," +
            "    name text," +
            "    query text" +
            ")";
    }
}
