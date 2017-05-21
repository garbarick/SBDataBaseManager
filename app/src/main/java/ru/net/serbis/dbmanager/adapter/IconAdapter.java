package ru.net.serbis.dbmanager.adapter;

import android.app.*;
import android.graphics.drawable.*;
import java.util.*;

public abstract class IconAdapter<T> extends Adapter<T>
{
    private Drawable icon;
    
    public IconAdapter(Activity context, int layout, int rowLayout, List<T> objects, int iconId)
    {
        super(context, layout, rowLayout, objects);
        initIcon(context, iconId);
    }
    
    public IconAdapter(Activity context, int rowLayout, List<T> objects, int iconId)
    {
        super(context, rowLayout, objects);
        initIcon(context, iconId);
    }
    
    public IconAdapter(Activity context, List<String> objects, int iconId)
    {
        super(context, objects);
        initIcon(context, iconId);
    }
    
    protected void initIcon(Activity context, int iconId)
    {
        icon = context.getResources().getDrawable(iconId);
    }
    
    @Override
    protected Drawable getIcon(int position)
    {
        return icon;
    }
}
