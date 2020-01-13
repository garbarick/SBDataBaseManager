package ru.net.serbis.dbmanager.app;

import java.io.*;
import ru.net.serbis.dbmanager.app.db.*;

public class ThisApp extends App
{
    public ThisApp(String packageName)
    {
        super(packageName);
    }
    
    public File getDataBaseDir(AppDb appDb)
    {
        return new File(getDataBaseDir(), appDb.getApp().getPackage());
    }
    
    public File getDBFile(AppDb appDb)
    {
        return new File(getDataBaseDir(appDb), appDb.getDb());
    }
    
    public File getJournalFile(AppDb appDb)
    {
        return new File(getDataBaseDir(appDb), appDb.getDb() + "-journal");
    }
}
