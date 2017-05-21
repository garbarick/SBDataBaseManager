package ru.net.serbis.dbmanager.widget;

import android.appwidget.*;
import android.content.*;
import android.widget.*;

public class Service extends RemoteViewsService 
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return (new Provider(getApplicationContext(), intent));
    }
}
