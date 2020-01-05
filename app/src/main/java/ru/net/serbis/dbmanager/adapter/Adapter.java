package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class Adapter <T> extends ArrayAdapter<T>
{
    protected int rowLayout;
    
    public Adapter(Context context, int layout, int rowLayout, List<T> objects)
    {
        super(context, layout, objects);
        this.rowLayout = rowLayout;
    }
    
    public Adapter(Context context, int rowLayout, List<T> objects)
    {
        this(context, R.layout.main, rowLayout, objects);
    }
    
    public Adapter(Context context, List<T> objects)
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
    
    protected abstract Drawable getIcon(int position);
    protected abstract String getLabel(int position);
    
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
