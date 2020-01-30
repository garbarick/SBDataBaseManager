package ru.net.serbis.dbmanager.app;

import android.content.*;
import android.graphics.drawable.*;
import java.util.*;
import ru.net.serbis.dbmanager.adapter.*;

public class AppAdapter extends IconAdapter<App>
{
    public AppAdapter(Context context, List<App> objects)
    {
        super(context, objects);
    }

    @Override
    protected Drawable getIcon(int position)
    {
        return getItem(position).getIcon();
    }

    @Override
    protected String getLabel(int position)
    {
        return getItem(position).getLabel();
    }
}
