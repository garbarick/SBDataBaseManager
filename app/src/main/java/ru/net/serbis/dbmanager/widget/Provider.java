package ru.net.serbis.dbmanager.widget;

import android.appwidget.*;
import android.content.*;
import android.widget.*;
import android.widget.RemoteViewsService.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.param.*;
import ru.net.serbis.dbmanager.query.*;

public class Provider implements RemoteViewsFactory
{
    private List<Param> rows = new ArrayList<Param>();
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
            DBResult table = 
                new DB(context, query.getAppDb(), helper.getParams(query.getAppDb()))
                    .select(query.getQuery().getQuery(), true, false);

            List<String> keys = table.getColumns();
            List<String> values = table.getRows().isEmpty() ? null : table.getRows().get(0);

            int i = 0;
            for (String key : keys)
            {
                String value = values != null ? values.get(i++) : "";
                rows.add(new Param(key, value));
            }
        }
        catch (Exception e)
        {
            Log.info(this, e);
            rows.add(new Param(context, R.string.error, e.getMessage()));
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
        Param row = rows.get(position);

        views.setTextViewText(R.id.key, row.getName());
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
