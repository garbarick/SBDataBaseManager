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
import ru.net.serbis.dbmanager.swipe.*;
import ru.net.serbis.dbmanager.task.*;

public class Table extends AsyncActivity
{
    public static final String TABLE = "table";
    public static final String QUERY = "query";

    private App app;
    private String db;
    private Query query;
    private List<List<String>> rows;
    private int column = 1;
    private int maxColumn;

    private Row header;
    private TableAdapter adapter;
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
        maxColumn = headerCells.size() - 1;

        adapter = new TableAdapter(this, rows);
        ListView list = findView(R.id.table);
        list.setAdapter(adapter);
        initSwipeListener(list);

        header = findView(R.id.header);
        header.setColors(R.color.header, R.color.headerCell1, R.color.headerCell2, R.color.headerCell3);
        header.setCells(headerCells);
        header.update();
    }

    @Override
    public void inBackground()
    {
        try
        {
            rows = new DB(this, app, db).select(query.getQuery(), true);
        }
        catch (Exception e)
        {
            Log.info(this, e);
            error = e.getMessage();
        }
    }

    private void initSwipeListener(ListView list)
    {
        list.setOnTouchListener(
            new SwipeListener(this, false)
            {
                @Override
                public void onSwipeLeft()
                {
                    if (column < maxColumn)
                    {
                        column ++;
                        updateCells();
                    }
                }

                @Override
                public void onSwipeRight()
                {
                    if (column > 1)
                    {
                        column --;
                        updateCells();
                    }
                }
            }
        );
    }

    private void updateCells()
    {
        header.setColumn(column);
        header.update();

        adapter.setColumn(column);
        adapter.notifyDataSetChanged();
    }
}
