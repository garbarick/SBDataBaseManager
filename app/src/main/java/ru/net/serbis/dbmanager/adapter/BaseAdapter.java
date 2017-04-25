package ru.net.serbis.dbmanager.adapter;

import android.app.*;
import android.widget.*;
import java.util.*;

public class BaseAdapter <T> extends ArrayAdapter<T>
{
    public BaseAdapter(Activity context, int layout, List<T> objects)
    {
        super(context, layout, objects);
    }

    public Activity getContext()
    {
        return (Activity) super.getContext();
    }
}
