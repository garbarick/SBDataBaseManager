package ru.net.serbis.dbmanager.db;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;
import ru.net.serbis.dbmanager.folder.*;
import ru.net.serbis.dbmanager.table.*;

public class Tables extends Folder
{
    private List<String> tables = new ArrayList<String>();

    @Override
    protected void initMain()
    {
        StringAdapter adapter = new StringAdapter(this, tables, R.drawable.table);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {               
                    Intent intent = new Intent(getIntent());
                    intent.setClass(Tables.this, Table.class);
                    intent.putExtra(Table.TABLE, (String) parent.getItemAtPosition(position));
                    startActivity(intent);
                }
            }
        );
    }

    @Override
    public void inBackground()
    {
        try
        {
            for (List<String> row : new DB(this, app, db).select(
                "select name" +
                "  from sqlite_master" +
                " where type = 'table'" +
                " order by name",
                false))
            {
                tables.add(row.get(1));
            }
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
    }
}
