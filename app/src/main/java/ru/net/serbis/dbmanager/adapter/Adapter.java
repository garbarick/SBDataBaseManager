package ru.net.serbis.dbmanager.adapter;

import android.app.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public abstract class Adapter <T> extends BaseAdapter<T>
{
    protected int rowLayout;
    
    public Adapter(Activity context, int layout, int rowLayout, List<T> objects)
    {
        super(context, layout, objects);
        this.rowLayout = rowLayout;
    }
    
    public Adapter(Activity context, int rowLayout, List<T> objects)
    {
        this(context, R.layout.main, rowLayout, objects);
    }
    
    public Adapter(Activity context, List<T> objects)
    {
        this(context, R.layout.row, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = getContext().getLayoutInflater().inflate(rowLayout, null);
        }
        setIcon(view, position);
        setLabel(view, position);
        return view;
    }
    
    protected abstract Drawable getIcon(int position);
    protected abstract String getLabel(int position);
    
    protected ImageView setIcon(View view, int position)
    {
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageDrawable(getIcon(position));
        return icon;
    }
    
    protected TextView setLabel(View view, int position)
    {
        TextView label = (TextView) view.findViewById(R.id.label);
        label.setText(getLabel(position));
        return label;
    }
}
