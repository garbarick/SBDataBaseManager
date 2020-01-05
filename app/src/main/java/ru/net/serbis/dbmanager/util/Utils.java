package ru.net.serbis.dbmanager.util;

import android.app.*;
import android.view.*;

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
}
