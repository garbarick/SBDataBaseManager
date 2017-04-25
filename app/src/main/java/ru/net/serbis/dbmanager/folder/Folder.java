package ru.net.serbis.dbmanager.folder;

import android.content.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.task.*;

public abstract class Folder extends AsyncActivity
{
    public static final String FOLDER = "folder";
    protected App app;
    protected String db;

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.main);
        
        Intent intent = getIntent();
        app = (App) intent.getSerializableExtra(DataBases.APP);
        db = intent.getStringExtra(DataBases.DB);
        String folder = intent.getStringExtra(FOLDER);
        setTitle(db + " (" + folder + ")");
    }
}
