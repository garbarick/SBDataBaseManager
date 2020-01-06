package ru.net.serbis.dbmanager.widget;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.net.*;
import android.widget.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.query.db.*;

public abstract class WidgetBase extends AppWidgetProvider
{
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
    
    public abstract void init(Context context, int id);
    
    protected void delete(Context context, int id)
    {
        new Helper(context).deleteWidget(id);
    }
    
    protected RemoteViews getRemoteView(Context context, AppWidgetManager manager, int id)
    {
        int layoutId = manager.getAppWidgetInfo(id).initialLayout;
        return new RemoteViews(context.getPackageName(), layoutId);
    }
    
    protected Intent getActionIntent(Context context, Class clazz, String action, int id)
    {
        Intent intent = new Intent(context, clazz);
        intent.setAction(action);
        intent.setData(getUri(action, id));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        return intent;
    }
    
    protected PendingIntent getPendingIntent(Context context, String action, int id)
    {
        Intent intent = getActionIntent(context, getClass(), action, id);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    
    protected Uri getUri(String action, int id)
    {
        return Uri.fromParts(action, getClass().getName(), String.valueOf(id));
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        
        int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (AppWidgetManager.INVALID_APPWIDGET_ID == id)
        {
            return;
        }
        onReceive(context, intent, id);
    }
    
    protected void onReceive(Context context, Intent intent, int id)
    {
    }
}
