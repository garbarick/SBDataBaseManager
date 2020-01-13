package ru.net.serbis.dbmanager.query;

import ru.net.serbis.dbmanager.app.db.*;

public class AppDbQuery
{
    private AppDb appDb;
    private Query query;
    
    public AppDbQuery(AppDb appDb, Query query)
    {
        this.appDb = appDb;
        this.query = query;
    }

    public AppDb getAppDb()
    {
        return appDb;
    }
    
    public Query getQuery()
    {
        return query;
    }
}
