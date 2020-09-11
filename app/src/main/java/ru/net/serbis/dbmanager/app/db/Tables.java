package ru.net.serbis.dbmanager.app.db;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;
import ru.net.serbis.dbmanager.dialog.*;
import ru.net.serbis.dbmanager.folder.*;
import ru.net.serbis.dbmanager.result.*;

public class Tables extends Folder
{
    private List<String> tables = new ArrayList<String>();
    private List<String> sqls = new ArrayList<String>();

    @Override
    protected void initMain()
    {
        StringAdapter adapter = new StringAdapter(this, tables, R.drawable.table);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(this);
        registerForContextMenu(main);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {  
        viewTable((String) parent.getItemAtPosition(position));
    }
    
    private void viewTable(String table)
    {
        Intent intent = new Intent(getIntent());
        intent.setClass(Tables.this, Result.class);
        intent.putExtra(Constants.TABLE, table);
        startActivity(intent);
    }

    @Override
    public void inBackground()
    {
        try
        {
            for (List<String> row : new DB(this, appDb).select(
                "select name, sql" +
                "  from sqlite_master" +
                " where type = 'table'" +
                " order by name",
                false,
                false).getRows())
            {
                tables.add(row.get(0));
                sqls.add(row.get(1));
            }
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.main)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(tables.get(info.position));
            menu.setHeaderIcon(R.drawable.table);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.table, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.view:
            case R.id.structure:
                onMainListContext(item);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void onMainListContext(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.view:
                viewTable(tables.get(info.position));
                break;

            case R.id.structure:
                new EditDialog(
                    this,
                    tables.get(info.position) + " (" + getResources().getString(R.string.structure) + ")",
                    sqls.get(info.position),
                    true);
                break;
        }
    }
}
