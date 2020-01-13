package ru.net.serbis.dbmanager.widget;

import android.appwidget.*;
import android.content.*;
import android.widget.*;
import android.widget.RemoteViewsService.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.db.*;

public class Provider implements RemoteViewsFactory
{
    private List<KeyValue> rows = new ArrayList<KeyValue>();
    private Context context;
    private int id;

    public Provider(Context context, Intent intent)
    {
        this.context = context;
        id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    private void initTable()
    {
        rows.clear();
        try
        {
            Helper helper = new Helper(context);
            AppDbQuery query = helper.getQuery(id);
            List<List<String>> table = new DB(context, query.getAppDb()).select(query.getQuery().getQuery(), true, false);

            List<String> keys = table.get(0);
            List<String> values = table.size() > 1 ? table.get(1) : null;

            int i = 0;
            for (String key : keys)
            {
                String value = values != null ? values.get(i++) : "";
                rows.add(new KeyValue(key, value));
            }
        }
        catch (Exception e)
        {
            Log.info(this, e);
            rows.add(new KeyValue(context.getResources().getString(R.string.error), e.getMessage()));
        }
    }

    @Override
    public int getCount()
    {
        return rows.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.table_row);
        KeyValue row = rows.get(position);

        views.setTextViewText(R.id.key, row.getKey());
        views.setTextViewText(R.id.value, row.getValue());

        return views;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public void onCreate()
    {
    }

    @Override
    public void onDataSetChanged()
    {
        initTable();
    }

    @Override
    public void onDestroy()
    {
	}
}
