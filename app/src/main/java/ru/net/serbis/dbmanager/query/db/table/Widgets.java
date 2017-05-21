package ru.net.serbis.dbmanager.query.db.table;

public class Widgets extends Table
{
    @Override
    protected String getScript()
    {
        return "create table widgets(" +
            "    id integer," +
            "    query_id integer," +
            "    foreign key(query_id) references queries(id)" +
            ")";
    }
}
