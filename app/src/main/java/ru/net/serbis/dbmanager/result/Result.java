package ru.net.serbis.dbmanager.result;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.dialog.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.task.*;
import ru.net.serbis.dbmanager.util.*;
import ru.net.serbis.dbmanager.db.*;

public class Result extends AsyncActivity implements Width.Listener
{
    private AppDb appDb;
    private Query query;
    private List<List<String>> rows;
    private Width width;

    private Row header;
    private ListView list;
    private String error;
    private Map<String, String> params;
    private boolean edit;

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.content);

        Intent intent = getIntent();
        appDb = Utils.getAppDb(intent);
        params = new Helper(this).getParams(appDb);
        if (intent.hasExtra(Constants.TABLE))
        {
            String table = intent.getStringExtra(Constants.TABLE);
            setTitle(table);
            query = new Query(0, null, "select * from " + table);
            edit = true;
        }
        else if (intent.hasExtra(Constants.QUERY))
        {
            query = (Query) intent.getSerializableExtra(Constants.QUERY);
            setTitle(query.getName());
        }
    }

    @Override
    protected void initMain()
    {
        if (rows == null)
        {
            showError();
            return;
        }
        initMainContent();
    }

    private void showError()
    {
        View main = getMain();
        main.setVisibility(View.GONE);

        new AlertMessage(this, error)
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Result.this.finish();
            }
        };
    }

    private void initMainContent()
    {
        List<String> headerCells = rows.remove(0);
        width = new Width(headerCells.size());

        ResultAdapter adapter = new ResultAdapter(this, rows, width);
        list = Utils.findView(this, R.id.table);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        registerForContextMenu(list);

        header = Utils.findView(this, R.id.header);
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
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {
        ResultAdapter adapter = Utils.getAdapter(parent);
        adapter.select(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void inBackground()
    {
        try
        {
            rows = new DB(this, appDb, params).select(query.getQuery(), true, true, query.getBindArray());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.table)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(getRowName(info.position));
            menu.setHeaderIcon(android.R.drawable.ic_menu_revert);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.row, menu);
            if (!edit)
            {
                menu.findItem(R.id.editRow).setVisible(false);
                menu.findItem(R.id.deleteRow).setVisible(false);
            }
        }
    }

    private String getRowName(int position)
    {
        return getResources().getString(R.string.row) + " " + (position + 1);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.viewRow:
                editRow(info.position, true);
                return true;
            case R.id.editRow:
                editRow(info.position, false);
                return true;
            case R.id.deleteRow:
                deleteRow(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!edit)
        {
            return false;
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.addRow:
                addRow();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editRow(int position, boolean readOnly)
    {
        List<String> names = header.getEditCells();
        List<String> values = Row.getEditCells(rows.get(position));
        new ParamsDialog(
            this,
            getRowName(position),
            names,
            values,
            readOnly)
        {
            @Override
            protected void ready(List<String> values)
            {
                new AlertMessage(Result.this, R.string.notImplementedYet);
            }
        };
    }
    
    private void deleteRow(int position)
    {
        new QuestionDialog(this, R.string.areYouSure)
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                new AlertMessage(Result.this, R.string.notImplementedYet);
            }
        };
    }
    
    private void addRow()
    {
        List<String> names = header.getEditCells();
        new ParamsDialog(
            this,
            getResources().getString(R.string.addRow),
            names,
            null,
            false)
        {
            @Override
            protected void ready(List<String> values)
            {
                new AlertMessage(Result.this, R.string.notImplementedYet);
            }
        };
    }
}
