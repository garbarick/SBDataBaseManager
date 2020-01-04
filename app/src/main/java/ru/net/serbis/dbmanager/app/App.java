package ru.net.serbis.dbmanager.app;

import android.graphics.drawable.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.dbmanager.sh.*;

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
	
    public File getDataBaseDir(String sub)
    {
        return new File(getDataBaseDir(), sub);
    }
	
    public File getDBFile(String db)
    {
        return new File(getDataBaseDir(), db);
    }
    
	public File getDBFile(String sub, String db)
    {
        return new File(getDataBaseDir(sub), db);
    }
	
    public File getJournalFile(String db)
    {
        return getDBFile(db + "-journal");
    }
    
	public File getJournalFile(String sub, String db)
    {
        return getDBFile(sub, db + "-journal");
    }
    
    @Override
    public int compareTo(Object that)
    {
        return label.compareTo(((App) that).label);
    }
    
    public List<String> getDBFiles()
    {
        List<String> result = new ArrayList<String>();
        for (String name : new Shell().command(
            "cd " + getDataBaseDir().getAbsolutePath(),
            "ls"))
        {
            if (checkDBName(name))
            {
                result.add(name);
            }
        }
        return result;
    }
    
    protected boolean checkDBName(String name)
    {
        return !name.endsWith("-journal");
    }
}
