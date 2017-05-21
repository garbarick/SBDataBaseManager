package ru.net.serbis.dbmanager.widget;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.query.db.*;
import ru.net.serbis.dbmanager.task.*;
import android.appwidget.*;
import android.content.*;

public class Config extends AsyncActivity
{
    private List<AppDbQuery> queries = new ArrayList<AppDbQuery>();
    private Helper helper;
    private int widgetId;

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.config);
        setResult(RESULT_CANCELED);

        widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        helper = new Helper(this);
    }

    @Override
    protected void initMain()
    {
        final ConfigAdapter adapter = new ConfigAdapter(this, queries, R.drawable.sql);
        ListView main = findView(R.id.queries);
        main.setAdapter(adapter);
        main.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {               
                    adapter.setChecked(position);
                    adapter.notifyDataSetChanged();
                }
            }
        );
        initOk(adapter);
        initCancel();
    }

    @Override
    public void inBackground()
    {
        try
        {
            queries.addAll(helper.getQueries());
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
    }

    private void initOk(final ConfigAdapter adapter)
    {
        Button button = findView(R.id.ok);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    int checked = adapter.getChecked();
                    if (checked > -1)
                    {
                        AppDbQuery query = queries.get(checked);
                        helper.addWidget(widgetId, query.getQuery().getId());

                        new Widget().init(Config.this, widgetId);
                        
                        Intent intent = new Intent();
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                        setResult(RESULT_OK, intent);
                        
                        finish();
                    }
                }
            }
        );
    }

    private void initCancel()
    {
        Button button = findView(R.id.cancel);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    finish();
                }
            }
        );
    }
}
