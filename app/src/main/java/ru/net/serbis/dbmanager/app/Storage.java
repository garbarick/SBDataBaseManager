package ru.net.serbis.dbmanager.app;

import android.content.*;
import android.os.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public class Storage extends App
{
    public Storage(Context context)
    {
        super("storage");
        setLabel("Storage");
        setIcon(context.getResources().getDrawable(android.R.drawable.ic_menu_directions));
    }

    @Override
    public File getDataBaseDir()
    {
        return Environment.getExternalStoragePublicDirectory("databases");
    }

    @Override
    public List<String> getDBFiles()
    {
        String [] names = getDataBaseDir().list();
        if (names == null)
        {
            return Collections.EMPTY_LIST;
        }
        List<String> result = new ArrayList<String>(names.length);
        for (String name : names)
        {
            if (checkDBName(name))
            {
                result.add(name);
            }
        }
        return result;
    }
}
