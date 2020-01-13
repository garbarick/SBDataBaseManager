package ru.net.serbis.dbmanager.app.db;

import java.io.*;
import ru.net.serbis.dbmanager.app.*;

public class AppDb
{
    private App app;
    private String db;

    public AppDb(App app, String db)
    {
        this.app = app;
        this.db = db;
    }

    public App getApp()
    {
        return app;
    }

    public String getDb()
    {
        return db;
    }
    
    public File getDBFile()
    {
        return app.getDBFile(db);
    }
    
    public File getJournalFile()
    {
        return app.getJournalFile(db);
    }
}
