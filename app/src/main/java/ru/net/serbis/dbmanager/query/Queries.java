package ru.net.serbis.dbmanager.query;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.folder.*;
import ru.net.serbis.dbmanager.query.db.*;
import ru.net.serbis.dbmanager.table.*;

public class Queries extends Folder
{
    private final static int NEW_QUERY_REQUEST = 0;
    private final static int EDIT_QUERY_REQUEST = 1;

    private List<Query> queries = new ArrayList<Query>();
    private Helper helper;
    private boolean init = false;

    @Override
    protected void initCreate()
    {
        super.initCreate();
        helper = new Helper(this);
    }

    @Override
    protected void initMain()
    {
        if (init)
        {
            refreshContent();
        }
        else
        {
            initMainContent();
        }
    }

    private void refreshContent()
    {
        ListView main = getMain();
        QueryAdapter adapter = (QueryAdapter) main.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void initMainContent()
    {
        QueryAdapter adapter = new QueryAdapter(this, queries, R.drawable.sql);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {               
                    Query query = (Query) parent.getItemAtPosition(position);
                    new QueryExecutor(Queries.this, new AppDbQuery(appDb, query));
                }
            }
        );
        registerForContextMenu(main);
        init = true;
    }

    @Override
    public void inBackground()
    {
        try
        {
            queries.clear();
            queries.addAll(helper.getQueries(appDb.getApp().getPackage(), appDb.getDb()));
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.queries, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.main)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(queries.get(info.position).getName());
            menu.setHeaderIcon(R.drawable.sql);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.query, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.newQuery:
                {
                    Intent intent = new Intent(Queries.this, Edit.class);
                    intent.putExtra(DataBases.APP, appDb.getApp());
                    intent.putExtra(DataBases.DB, appDb.getDb());
                    startActivityForResult(intent, NEW_QUERY_REQUEST);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.editQuery:
            case R.id.deleteQuery:
                {
                    onMainListContext(item);
                }
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void onMainListContext(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Query query = queries.get(info.position);
        switch (item.getItemId())
        {
            case R.id.editQuery:
                {
                    Intent intent = new Intent(Queries.this, Edit.class);
                    intent.putExtra(Table.QUERY, query);
                    intent.putExtra(DataBases.APP, appDb.getApp());
                    intent.putExtra(DataBases.DB, appDb.getDb());
                    startActivityForResult(intent, EDIT_QUERY_REQUEST);
                }
                break;

            case R.id.deleteQuery:
                helper.deleteQuery(query);
                startTask();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case NEW_QUERY_REQUEST:
                    {
                        Query query = (Query) data.getSerializableExtra(Table.QUERY);
                        helper.addQuery(query, appDb.getApp().getPackage(), appDb.getDb());
                    }
                    break;

                case EDIT_QUERY_REQUEST:
                    {
                        Query query = (Query) data.getSerializableExtra(Table.QUERY);
                        helper.updateQuery(query);
                    }
                    break;
            }
            startTask();
        }
    }
}
