package ru.net.serbis.dbmanager.folder;

import android.content.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.task.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class Folder extends AsyncActivity
{
    protected AppDb appDb;

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.main);
        
        Intent intent = getIntent();
        appDb = Utils.getAppDb(intent);
        String folder = intent.getStringExtra(Constants.FOLDER);
        setTitle(appDb.getDb() + " (" + folder + ")");
    }
}
