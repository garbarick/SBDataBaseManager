package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class IconAdapter<T> extends Adapter<T>
{
    private Drawable icon;

    public IconAdapter(Context context, int layout, int rowLayout, Collection<T> objects)
    {
        super(context, layout, rowLayout, objects);
    }

    public IconAdapter(Context context, int layout, int rowLayout, Collection<T> objects, int iconId)
    {
        super(context, layout, rowLayout, objects);
        initIcon(context, iconId);
    }

    public IconAdapter(Context context, int rowLayout, Collection<T> objects)
    {
        this(context, R.layout.main, rowLayout, objects);
    }

    public IconAdapter(Context context, int rowLayout, Collection<T> objects, int iconId)
    {
        this(context, R.layout.main, rowLayout, objects, iconId);
    }

    public IconAdapter(Context context, Collection<String> objects)
    {
        this(context, R.layout.row, objects);
    }

    public IconAdapter(Context context, Collection<String> objects, int iconId)
    {
        this(context, R.layout.row, objects, iconId);
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        view = super.getView(position, view, parent);
        setIcon(view, position);
        setLabel(view, position);
        return view;
    }

    protected Drawable getIcon(int position)
    {
        return icon;
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
    
    protected void initIcon(Context context, int iconId)
    {
        icon = context.getResources().getDrawable(iconId);
    }
}
