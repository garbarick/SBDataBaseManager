package ru.net.serbis.dbmanager.folder;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.param.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.task.*;
import ru.net.serbis.dbmanager.util.*;

public class Folders extends AsyncActivity
{
    private Map<String, Class<? extends Folder>> folders = new LinkedHashMap<String, Class<? extends Folder>>();

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.main);
        setTitle(getIntent().getStringExtra(Constants.DB));
    }

    @Override
    protected void initMain()
    {
        StringAdapter adapter = new StringAdapter(this, folders.keySet(), R.drawable.folder);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {               
        Intent intent = new Intent(getIntent());
        String folder = (String) parent.getItemAtPosition(position);
        intent.setClass(Folders.this, folders.get(folder));
        intent.putExtra(Constants.FOLDER, folder);
        startActivity(intent);
    }

    @Override
    public void inBackground()
    {
        addFolder(R.string.tables, Tables.class);
        addFolder(R.string.queries, Queries.class);
        addFolder(R.string.params, Params.class);
    }

    private void addFolder(int id, Class<? extends Folder> clazz)
    {
        folders.put(getResources().getString(id), clazz);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.to_app_list, menu);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
