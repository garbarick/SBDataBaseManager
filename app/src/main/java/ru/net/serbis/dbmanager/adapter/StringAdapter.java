package ru.net.serbis.dbmanager.adapter;

import android.app.*;
import java.util.*;

public class StringAdapter extends IconAdapter<String>
{
    public StringAdapter(Activity context, List<String> objects, int iconId)
    {
        super(context, objects, iconId);
    }

    @Override
    protected String getLabel(int position)
    {
        return getItem(position);
    }
}
