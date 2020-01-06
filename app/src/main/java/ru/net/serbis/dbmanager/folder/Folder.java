package ru.net.serbis.dbmanager.folder;

import android.content.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.task.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class Folder extends AsyncActivity
{
    public static final String FOLDER = "folder";
    protected AppDb appDb;

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.main);
        
        Intent intent = getIntent();
        appDb = Utils.getAppDb(intent);
        String folder = intent.getStringExtra(FOLDER);
        setTitle(appDb.getDb() + " (" + folder + ")");
    }
}
