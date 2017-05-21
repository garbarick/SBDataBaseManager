package ru.net.serbis.dbmanager.query;

import ru.net.serbis.dbmanager.app.*;

public class AppDbQuery
{
    private App app;
    private String db;
    private Query query;
    
    public AppDbQuery(App app, String db, Query query)
    {
        this.app = app;
        this.db = db;
        this.query = query;
    }

    public App getApp()
    {
        return app;
    }

    public String getDb()
    {
        return db;
    }
    
    public Query getQuery()
    {
        return query;
    }
}
