package ru.net.serbis.dbmanager.widget;

import android.appwidget.*;
import android.content.*;
import android.widget.*;
import ru.net.serbis.dbmanager.*;

public class Widget extends WidgetBase
{
    private static final String UPDATE = Widget.class.getName() + ".Update";
    
    @Override
    public void init(Context context, int id)
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        RemoteViews views = getRemoteView(context, manager, id);
        if (views == null)
        {
            return;
        }

        views.setRemoteAdapter(id, R.id.table, getActionIntent(context, Service.class, UPDATE, id));
        views.setOnClickPendingIntent(R.id.update, getPendingIntent(context, UPDATE, id));
        
        manager.updateAppWidget(id, views);
    }

    @Override
    protected void onReceive(Context context, Intent intent, int id)
    {
        if (UPDATE.equals(intent.getAction()))
        {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.notifyAppWidgetViewDataChanged(id, R.id.table);
        }
    }
}
