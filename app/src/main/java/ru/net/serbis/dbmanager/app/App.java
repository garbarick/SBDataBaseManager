package ru.net.serbis.dbmanager.app;
import android.graphics.drawable.*;
import java.io.*;

public class App implements Comparable, Serializable
{
    private String label;
    private String packageName;
    private transient Drawable icon;

    public App(String packageName)
    {
        this.packageName = packageName;
    }
    
    public String getLabel()
    {
        return label;
    }
    
    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getPackage()
    {
        return packageName;
    }

    public Drawable getIcon()
    {
        return icon;
    }
    
    public void setIcon(Drawable icon)
    {
        this.icon = icon;
    }
    
    public boolean hasDataBase()
    {
        File dir = getDataBaseDir();
        return dir.exists();
    }

    public File getDataBaseDir()
    {
        return new File("/data/data/" + packageName + "/databases");
    }
    
    public File getDBFile(String db)
    {
        return new File(getDataBaseDir(), db);
    }
    
    public File getJournalFile(String db)
    {
        return new File(getDataBaseDir(), db + "-journal");
    }
    
    @Override
    public int compareTo(Object that)
    {
        return label.compareTo(((App) that).label);
    }
}
