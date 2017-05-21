package ru.net.serbis.dbmanager.table;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.task.*;

public class Table extends AsyncActivity implements Width.Listener
{
    public static final String TABLE = "table";
    public static final String QUERY = "query";

    private App app;
    private String db;
    private Query query;
    private List<List<String>> rows;
    private Width width;

    private Row header;
    private ListView list;
    private String error;

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.content);

        Intent intent = getIntent();
        app = (App) intent.getSerializableExtra(DataBases.APP);
        db = intent.getStringExtra(DataBases.DB);
        if (intent.hasExtra(TABLE))
        {
            String table = intent.getStringExtra(TABLE);
            setTitle(table);
            query = new Query(0, null, "select * from " + table);
        }
        else if (intent.hasExtra(QUERY))
        {
            query = (Query) intent.getSerializableExtra(QUERY);
            setTitle(query.getName());
        }
    }

    @Override
    protected void initMain()
    {
        if (rows == null)
        {
            showError();
        }
        else
        {
            initMainContent();
        }
    }

    private void showError()
    {
        View main = getMain();
        main.setVisibility(View.GONE);

        new AlertDialog.Builder(this)
            .setTitle(getResources().getString(R.string.error))
            .setMessage(error)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
            .setPositiveButton(
            android.R.string.ok,
            new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    Table.this.finish();
                }
            })
            .show();
    }

    private void initMainContent()
    {
        List<String> headerCells = rows.remove(0);
        width = new Width(headerCells.size());

        TableAdapter adapter = new TableAdapter(this, rows, width);
        list = findView(R.id.table);
        list.setAdapter(adapter);

        header = findView(R.id.header);
        header.setColors(R.color.header, R.color.headerFirstCell, R.color.headerOtherCell);
        header.setCells(headerCells);
        header.setWidth(width);
        header.update();
        
        for (int i = 0; i <= 100 && i < rows.size(); i++)
        {
            header.updateWidth(rows.get(i));
        }
        
        update();
        width.addListener(this);
    }

    @Override
    public void inBackground()
    {
        try
        {
            rows = new DB(this, app, db).select(query.getQuery(), true, true);
        }
        catch (Exception e)
        {
            Log.info(this, e);
            error = e.getMessage();
        }
    }

    @Override
    public void update()
    {
        list.invalidateViews();
        header.update();

        header.setWidth(header, width.getSum());
        header.setWidth(list, width.getSum());
    }
}
