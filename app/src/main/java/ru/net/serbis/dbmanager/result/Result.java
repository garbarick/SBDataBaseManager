package ru.net.serbis.dbmanager.result;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.dialog.*;
import ru.net.serbis.dbmanager.json.*;
import ru.net.serbis.dbmanager.param.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.task.*;
import ru.net.serbis.dbmanager.util.*;

public class Result extends AsyncActivity implements Width.Listener
{
    private AppDb appDb;
    private String name;
    private Query query;
    private DBResult result;
    private Width width;

    private Row header;
    private ListView list;
    private String error;
    private ParamMap params;
    private boolean edit;
    private boolean switchEdit;
    
    private Menu optionsMenu;
    private boolean optionsInit;

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.content);

        Intent intent = getIntent();
        appDb = Utils.getAppDb(intent);
        params = new Helper(this).getParams(appDb);
        if (intent.hasExtra(Constants.TABLE))
        {
            name = intent.getStringExtra(Constants.TABLE);
            edit = true;
        }
        else if (intent.hasExtra(Constants.QUERY))
        {
            query = (Query) intent.getSerializableExtra(Constants.QUERY);
            name = query.getName();
        }
        setTitle(name);
    }

    @Override
    protected void initMain()
    {
        if (result == null)
        {
            showError();
            return;
        }
        if (switchEdit)
        {
            setTitle(name + " (" + getResources().getString(R.string.readOnly) + ")");
        }
        if (!optionsInit)
        {
            initOptionMenu();
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
        List<String> headerCells = result.getColumns();
        width = new Width(headerCells.size());

        ResultAdapter adapter = new ResultAdapter(this, result.getRows(), width);
        list = Utils.findView(this, R.id.table);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        registerForContextMenu(list);

        header = Utils.findView(this, R.id.header);
        header.setColors(R.color.header, R.color.headerFirstCell, R.color.headerOtherCell);
        header.setCells(headerCells);
        header.setWidth(width);
        header.update();
        
        for (int i = 0; i <= 100 && i < result.getRows().size(); i++)
        {
            header.updateWidth(result.getRows().get(i));
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
            DB db = new DB(this, appDb, params);
            if (query == null)
            {
                result = db.selectTable(name, true, edit, true);
            }
            else
            {
                result = db.select(query.getQuery(), true, true, query.getBindArray());
            }
            if (edit && db.isReadOnly())
            {
                edit = false;
                switchEdit = true;
            }
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
                hideMenuItem(menu, R.id.editRow);
                hideMenuItem(menu, R.id.deleteRow);
            }
        }
    }

    private String getRowName(int position)
    {
        return getResources().getString(R.string.row) + " " + (position + 1);
    }
    
    private void hideMenuItem(Menu menu, int id)
    {
        if (menu == null)
        {
            return;
        }
        MenuItem item = menu.findItem(id);
        if (item != null)
        {
            item.setVisible(false);
        }
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
        optionsMenu = menu;
        if (!progress)
        {
            initOptionMenu();
        }
        return true;
    }
    
    private void initOptionMenu()
    {
        if (optionsMenu == null)
        {
            return;
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result, optionsMenu);
        optionsInit = true;
        if (edit)
        {
            return;
        }
        hideMenuItem(optionsMenu, R.id.addRow);
        hideMenuItem(optionsMenu, R.id.importJSON);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.addRow:
                addRow();
                return true;
            case R.id.importJSON:
                importJSON();
                return true;
            case R.id.exportJSON:
                exportJSON();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editRow(int position, boolean readOnly)
    {
        final List<String> names = header.getEditCells();
        final List<String> oldValues = Row.getEditCells(result.getRows().get(position));
        new FieldsDialog(
            this,
            getRowName(position),
            names,
            result.getTypes(),
            oldValues,
            readOnly)
        {
            @Override
            protected void ready(List<String> newValues)
            {
                Query query = new QueryGenerator().generateUpdate(name, names, oldValues, newValues);
                executeQuery(query);
            }
        };
    }
    
    private void deleteRow(final int position)
    {
        new QuestionDialog(this, R.string.areYouSure)
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                List<String> names = header.getEditCells();
                List<String> values = Row.getEditCells(result.getRows().get(position));
                Query query = new QueryGenerator().generateDelete(name, names, values);
                executeQuery(query);
            }
        };
    }
    
    private void addRow()
    {
        final List<String> names = header.getEditCells();
        new FieldsDialog(
            this,
            getResources().getString(R.string.addRow),
            names,
            result.getTypes(),
            null,
            false)
        {
            @Override
            protected void ready(List<String> values)
            {
                Query query = new QueryGenerator().generateInsert(name, names, values);
                executeQuery(query);
            }
        };
    }

    private void executeQuery(Query query)
    {
        if (query == null)
        {
            new AlertMessage(Result.this, R.string.pleaseDefineColumns);
            return;
        }
        try
        {
            new DB(this, appDb, params).execute(query.getQuery(), query.getBindArray());
            startTask();
        }
        catch (Exception e)
        {
            Log.info(this, e);
            new AlertMessage(this, e.getMessage());
        }
    }

    private void importJSON()
    {
        new ImportJSON(
            this,
            new DB(this, appDb, params),
            name,
            header.getEditCells())
        {
            @Override
            protected void onFinish()
            {
                startTask();
            }  
        }.executeDialog();
    }

    private void exportJSON()
    {
        new ExportJSON(
            this,
            name,
            header.getCells(),
            result.getRows()).executeDialog();
    }
}
