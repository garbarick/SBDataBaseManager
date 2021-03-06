package ru.net.serbis.dbmanager.widget;

import android.appwidget.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.task.*;
import ru.net.serbis.dbmanager.util.*;

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
        ConfigAdapter adapter = new ConfigAdapter(this, queries, R.drawable.sql);
        ListView main = Utils.findView(this ,R.id.queries);
        main.setAdapter(adapter);
        main.setOnItemClickListener(this);
        initOk(adapter);
        initCancel();
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {   
        ConfigAdapter adapter = Utils.getAdapter(parent);
        adapter.setChecked(position);
        adapter.notifyDataSetChanged();
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
        Button button = Utils.findView(this, R.id.ok);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    int checked = adapter.getChecked();
                    if (checked <= -1)
                    {
                        return;
                    }
                    AppDbQuery query = queries.get(checked);
                    helper.addWidget(widgetId, query.getQuery().getId());

                    WidgetBase widget = getWidget();
                    widget.init(Config.this, widgetId);
                        
                    Intent intent = new Intent();
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                    setResult(RESULT_OK, intent);
                        
                    finish();
                }
            }
        );
    }

    private void initCancel()
    {
        Button button = Utils.findView(this, R.id.cancel);
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
    
    public WidgetBase getWidget()
    {
        try
        {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
            Class clazz = Class.forName(widgetManager.getAppWidgetInfo(widgetId).provider.getClassName());
            return (WidgetBase) clazz.newInstance();
        }
        catch (Throwable e)
        {
            Log.info(this, "error on initWidget", e);
            return null;
        }
    }
}
