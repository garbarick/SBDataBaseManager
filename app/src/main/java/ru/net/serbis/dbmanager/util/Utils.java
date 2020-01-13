package ru.net.serbis.dbmanager.util;

import android.app.*;
import android.content.*;
import android.view.*;
import ru.net.serbis.dbmanager.app.*;
import ru.net.serbis.dbmanager.app.db.*;

public class Utils
{
    public static <T extends View> T findView(View view, int id)
    {
        return (T) view.findViewById(id);
    }
    
    public static <T extends View> T findView(Activity view, int id)
    {
        return (T) view.findViewById(id);
    }
    
    public static AppDb getAppDb(Intent intent)
    {
        App app = (App) intent.getSerializableExtra(DataBases.APP);
        String db = intent.getStringExtra(DataBases.DB);
        return new AppDb(app, db);
    }
    
    public static void closeActivity(Context context, boolean close)
    {
        if (close && context instanceof Activity)
        {
            ((Activity) context).finish();
        }
    }
}
