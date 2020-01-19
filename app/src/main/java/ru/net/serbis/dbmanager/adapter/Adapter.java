package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class Adapter<T> extends ArrayAdapter<T>
{
    protected int rowLayout;
    
    public Adapter(Context context, int layout, int rowLayout, Collection<T> objects)
    {
        super(context, layout);
        addAll(objects);
        this.rowLayout = rowLayout;
    }
    
    public Adapter(Context context, int rowLayout, Collection<T> objects)
    {
        this(context, R.layout.main, rowLayout, objects);
    }
    
    public Adapter(Context context, Collection<T> objects)
    {
        this(context, R.layout.row, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(rowLayout, null);
        }
        setIcon(view, position);
        setLabel(view, position);
        return view;
    }
    
    protected Drawable getIcon(int position)
    {
        return null;
    }
    
    protected String getLabel(int position)
    {
        return null;
    }
    
    protected ImageView setIcon(View view, int position)
    {
        ImageView icon = Utils.findView(view, R.id.icon);
        icon.setImageDrawable(getIcon(position));
        return icon;
    }
    
    protected TextView setLabel(View view, int position)
    {
        TextView label = Utils.findView(view, R.id.label);
        label.setText(getLabel(position));
        return label;
    }
}
