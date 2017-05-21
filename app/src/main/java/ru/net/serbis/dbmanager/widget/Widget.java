package ru.net.serbis.dbmanager.widget;
import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.widget.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.query.db.*;

public class Widget extends AppWidgetProvider
{
    public static final String UPDATE = Widget.class.getName() + ".Update";
    
    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids)
    {
        super.onUpdate(context, manager, ids);
        for (int id : ids)
        {
            init(context, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] ids)
    {
        super.onDeleted(context, ids);
        for (int id : ids)
        {
            delete(context, id);
        }
    }

    public void init(Context context, int id)
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), manager.getAppWidgetInfo(id).initialLayout);

        Intent intent = new Intent(context, Service.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        views.setRemoteAdapter(id, R.id.table, intent);
        views.setOnClickPendingIntent(R.id.update, getPendingIntent(context));
        
        manager.updateAppWidget(id, views);
    }

    private void delete(Context context, int id)
    {
        new Helper(context).deleteWidget(id);
    }

    private PendingIntent getPendingIntent(Context context)
    {
        Intent intent = new Intent(context, Widget.class);
        intent.setAction(Widget.UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        
        Log.info(this, "action=" + intent.getAction());
        if (UPDATE.equals(intent.getAction()))
        {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName name = new ComponentName(context, Widget.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(name), R.id.table);
        }
    }
}
