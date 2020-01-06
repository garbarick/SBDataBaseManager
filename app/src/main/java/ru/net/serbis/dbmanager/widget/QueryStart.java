package ru.net.serbis.dbmanager.widget;

import android.appwidget.*;
import android.content.*;
import android.widget.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.query.db.*;

public class QueryStart extends WidgetBase
{
    private static final String START = QueryStart.class.getName() + ".Start";
    
    @Override
    public void init(Context context, int id)
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        RemoteViews views = getRemoteView(context, manager, id);
        
        AppDbQuery query = getQuery(context, id);
        views.setTextViewText(R.id.name, query.getQuery().getName());
        
        views.setOnClickPendingIntent(R.id.start, getPendingIntent(context, START, id));
        
        manager.updateAppWidget(id, views);
    }

    private AppDbQuery getQuery(Context context, int id)
    {
        Helper helper = new Helper(context);
        return helper.getQuery(id);
    }

    @Override
    protected void onReceive(Context context, Intent intent, int id)
    {
        if (START.equals(intent.getAction()))
        {
            Intent start = getActionIntent(context, QueryStartActivity.class, START, id);
            start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            start.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

            context.startActivity(start);
        }
    }
}
