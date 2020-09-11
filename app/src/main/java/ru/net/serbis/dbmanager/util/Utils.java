package ru.net.serbis.dbmanager.util;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import ru.net.serbis.dbmanager.*;
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

    public static <T extends ArrayAdapter> T getAdapter(AdapterView view)
    {
        return (T) view.getAdapter();
    }

    public static AppDb getAppDb(Intent intent)
    {
        App app = (App) intent.getSerializableExtra(Constants.APP);
        String db = intent.getStringExtra(Constants.DB);
        return new AppDb(app, db);
    }

    public static void closeActivity(Context context, boolean close)
    {
        if (close && context instanceof Activity)
        {
            ((Activity) context).finish();
        }
    }

    public static boolean isEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str)
    {
        return str != null && str.length() > 0;
    }

    public static void close(Closeable o)
    {
        try
        {
            if (o != null)
            {
                o.close();
            }
        }
        catch (Exception e)
        {
        }
    }

    public static File getToolFolder()
    {
        File dir = Environment.getExternalStorageDirectory();
        File appDir = new File(dir, "SBDataBaseManager");
        appDir.mkdirs();
        return appDir;
    }

    public static <T> T getService(Context context, String name)
    {
        return (T) context.getSystemService(name);
    }

    public static void toAppList(Activity context)
    {
        Intent intent = new Intent(context, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void toDbList(Activity context, App app)
    {
        Intent intent = new Intent(context, DataBases.class);
        intent.putExtra(Constants.APP, app);
        context.startActivity(intent);
    }
    
    public static int toInt(String str)
    {
        try
        {
            return Integer.valueOf(str);
        }
        catch(Exception e)
        {
            return 0;
        }
    }
}
