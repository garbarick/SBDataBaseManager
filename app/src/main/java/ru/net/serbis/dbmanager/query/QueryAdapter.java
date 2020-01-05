package ru.net.serbis.dbmanager.query;

import android.content.*;
import java.util.*;
import ru.net.serbis.dbmanager.adapter.*;

public class QueryAdapter extends IconAdapter<Query>
{
    public QueryAdapter(Context context, List<Query> objects, int iconId)
    {
        super(context, objects, iconId);
    }
    
    @Override
    protected String getLabel(int position)
    {
        return getItem(position).getName();
    }
}
