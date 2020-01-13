package ru.net.serbis.dbmanager.widget;

import android.app.*;
import android.appwidget.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.db.*;

public class QueryStartActivity extends Activity
{
    @Override
    protected void onResume()
    {
        super.onResume();
        
        int id = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Helper helper = new Helper(this);
        AppDbQuery query = helper.getQuery(id);
        new QueryExecutor(this, query, true);
    }
}
