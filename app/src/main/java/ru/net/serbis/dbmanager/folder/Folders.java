package ru.net.serbis.dbmanager.folder;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.task.*;

public class Folders extends AsyncActivity
{
    private String tables;
    private String queries;
    private List<String> folders = new ArrayList<String>();

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.main);
        setTitle(getIntent().getStringExtra(DataBases.DB));
    }

    @Override
    protected void initMain()
    {
        StringAdapter adapter = new StringAdapter(this, folders, R.drawable.folder);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {               
                    Intent intent = new Intent(getIntent());
                    String folder = (String) parent.getItemAtPosition(position);
                    if (tables.equals(folder))
                    {
                        intent.setClass(Folders.this, Tables.class);
                    }
                    else if (queries.equals(folder))
                    {
                        intent.setClass(Folders.this, Queries.class);
                    }
                    else
                    {
                        return;
                    }
                    intent.putExtra(Folder.FOLDER, folder);
                    startActivity(intent);
                }
            }
        );
    }

    @Override
    public void inBackground()
    {
        tables = getResources().getString(R.string.tables);
        queries = getResources().getString(R.string.queries);
        folders.add(tables);
        folders.add(queries);
    }
}
