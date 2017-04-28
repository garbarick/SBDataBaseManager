package ru.net.serbis.dbmanager.adapter;

import android.app.*;
import android.graphics.drawable.*;
import java.util.*;

public abstract class IconAdapter<T> extends Adapter<T>
{
    private Drawable icon;
    
    public IconAdapter(Activity context, List<String> objects, int iconId)
    {
        super(context, objects);
        icon = context.getResources().getDrawable(iconId);
    }
    
    @Override
    protected Drawable getIcon(int position)
    {
        return icon;
    }
}
