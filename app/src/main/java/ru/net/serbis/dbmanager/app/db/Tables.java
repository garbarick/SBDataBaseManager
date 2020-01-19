package ru.net.serbis.dbmanager.app.db;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;
import ru.net.serbis.dbmanager.folder.*;
import ru.net.serbis.dbmanager.result.*;

public class Tables extends Folder
{
    private List<String> tables = new ArrayList<String>();

    @Override
    protected void initMain()
    {
        StringAdapter adapter = new StringAdapter(this, tables, R.drawable.table);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {               
        Intent intent = new Intent(getIntent());
        intent.setClass(Tables.this, Result.class);
        intent.putExtra(Constants.TABLE, (String) parent.getItemAtPosition(position));
        startActivity(intent);
    }

    @Override
    public void inBackground()
    {
        try
        {
            for (List<String> row : new DB(this, appDb).select(
                "select name" +
                "  from sqlite_master" +
                " where type = 'table'" +
                " order by name",
                false,
                false))
            {
                tables.add(row.get(0));
            }
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
    }
}
