package ru.net.serbis.dbmanager.db.table.migrate;

import ru.net.serbis.dbmanager.db.table.*;
import android.database.sqlite.*;

public class From2 extends Migrate
{
    @Override
    protected boolean need(int oldVersion, int newVersion)
    {
        return oldVersion == 2 && newVersion >= 3;
    }

    @Override
    protected void update(SQLiteDatabase db)
    {
        make(db,
             "insert into databases(package, name)" +
             "select distinct package, db" +
             "  from queries");
        make(db,
             "alter table queries rename to queries_old");
        new Queries().make(db, 0, 0);
        make(db,
             "insert into queries(id, db_id, name, query)" +
             "select q.id, d.id, q.name, q.query" +
             "  from queries_old q," +
             "       databases d" +
             " where d.package = q.package" +
             "   and d.name = q.db");
        make(db,
             "alter table widgets rename to widgets_old");
        new Widgets().make(db, 0, 0);
        make(db,
             "insert into widgets(id, query_id)" +
             "select id, query_id from widgets_old");
        make(db,
             "drop table widgets_old");
        make(db,
             "drop table queries_old");
    }
}
