package ru.net.serbis.dbmanager.db;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;
import ru.net.serbis.dbmanager.app.*;
import ru.net.serbis.dbmanager.folder.*;
import ru.net.serbis.dbmanager.task.*;

public class DataBases extends AsyncActivity
{
    public static final String APP = "app";
    public static final String DB = "db";
    
    private List<String> dbs = new ArrayList<String>();
    private App app;

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.main);
        app = (App) getIntent().getSerializableExtra(APP);
        setTitle(app.getLabel());
    }

    @Override
    protected void initMain()
    {
        StringAdapter adapter = new StringAdapter(this, dbs, R.drawable.app_icon);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {               
                    Intent intent = new Intent(getIntent());
                    intent.setClass(DataBases.this, Folders.class);
                    intent.putExtra(DB, (String) parent.getItemAtPosition(position));
                    startActivity(intent);
                }
            }
        );
    }

    @Override
    public void inBackground()
    {
        dbs.addAll(app.getDBFiles());
        Collections.sort(dbs);
    }
}
