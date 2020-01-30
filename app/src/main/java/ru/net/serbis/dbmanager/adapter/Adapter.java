package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public abstract class Adapter<T> extends ArrayAdapter<T>
{
    protected int rowLayout;
    
    public Adapter(Context context, int layout, int rowLayout)
    {
        super(context, layout);
        this.rowLayout = rowLayout;
    }

    public Adapter(Context context, int layout, int rowLayout, Collection<T> objects)
    {
        this(context, layout, rowLayout);
        addAll(objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(rowLayout, null);
        }
        return view;
    }
}
