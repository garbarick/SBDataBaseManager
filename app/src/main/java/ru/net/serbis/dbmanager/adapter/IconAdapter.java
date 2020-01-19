package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.graphics.drawable.*;
import java.util.*;

public abstract class IconAdapter<T> extends Adapter<T>
{
    private Drawable icon;
    
    public IconAdapter(Context context, int layout, int rowLayout, Collection<T> objects, int iconId)
    {
        super(context, layout, rowLayout, objects);
        initIcon(context, iconId);
    }
    
    public IconAdapter(Context context, int rowLayout, Collection<T> objects, int iconId)
    {
        super(context, rowLayout, objects);
        initIcon(context, iconId);
    }
    
    public IconAdapter(Context context, Collection<String> objects, int iconId)
    {
        super(context, objects);
        initIcon(context, iconId);
    }
    
    protected void initIcon(Context context, int iconId)
    {
        icon = context.getResources().getDrawable(iconId);
    }
    
    @Override
    protected Drawable getIcon(int position)
    {
        return icon;
    }
}
