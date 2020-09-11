package ru.net.serbis.dbmanager.folder;

import android.content.*;
import android.view.*;
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.to_app_list, menu);
        inflater.inflate(R.menu.to_db_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.toAppList:
                Utils.toAppList(this);
                return true;
            case R.id.toDbList:
                Utils.toDbList(this, appDb.getApp());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
