package ru.net.serbis.dbmanager.query;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.dialog.*;
import ru.net.serbis.dbmanager.folder.*;

public class Queries extends Folder
{
    private List<Query> queries = new ArrayList<Query>();
    private Helper helper;

    @Override
    protected void initCreate()
    {
        super.initCreate();
        helper = new Helper(this);
    }

    @Override
    protected void initMain()
    {
        QueryAdapter adapter = new QueryAdapter(this, queries, R.drawable.sql);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(this);
        registerForContextMenu(main);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {               
        Query query = (Query) parent.getItemAtPosition(position);
        new QueryExecutor(Queries.this, new AppDbQuery(appDb, query));
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
        super.onCreateOptionsMenu(menu);
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
                newQuery();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newQuery()
    {
        Intent intent = new Intent(this, Edit.class);
        intent.putExtra(Constants.APP, appDb.getApp());
        intent.putExtra(Constants.DB, appDb.getDb());
        startActivityForResult(intent, Constants.NEW_REQUEST);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.editQuery:
            case R.id.deleteQuery:
                onMainListContext(item);
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
                editQuery(query);
                break;

            case R.id.deleteQuery:
                deleteQuery(query);
                break;
        }
    }
    
    private void editQuery(Query query)
    {
        Intent intent = new Intent(Queries.this, Edit.class);
        intent.putExtra(Constants.QUERY, query);
        intent.putExtra(Constants.APP, appDb.getApp());
        intent.putExtra(Constants.DB, appDb.getDb());
        startActivityForResult(intent, Constants.EDIT_REQUEST);
    }

    private void deleteQuery(final Query query)
    {
        new QuestionDialog(this, R.string.areYouSure)
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                helper.deleteQuery(query);
                startTask();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.NEW_REQUEST:
                    {
                        Query query = (Query) data.getSerializableExtra(Constants.QUERY);
                        helper.addQuery(query, appDb);
                    }
                    break;

                case Constants.EDIT_REQUEST:
                    {
                        Query query = (Query) data.getSerializableExtra(Constants.QUERY);
                        helper.updateQuery(query);
                    }
                    break;
            }
            startTask();
        }
    }
}
