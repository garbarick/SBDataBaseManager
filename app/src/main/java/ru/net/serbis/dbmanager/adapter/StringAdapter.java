package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import java.util.*;

public class StringAdapter extends IconAdapter<String>
{
    public StringAdapter(Context context, Collection<String> objects, int iconId)
    {
        super(context, objects, iconId);
    }

    @Override
    protected String getLabel(int position)
    {
        return getItem(position);
    }
}
