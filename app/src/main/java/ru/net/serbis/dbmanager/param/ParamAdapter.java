package ru.net.serbis.dbmanager.param;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;

import ru.net.serbis.dbmanager.adapter.Adapter;
import ru.net.serbis.dbmanager.util.*;

public class ParamAdapter extends Adapter<Param>
{
    public ParamAdapter(Context context, List<? extends Param> params)
    {
        super(context, R.layout.param, params);
    }

    @Override
    protected ImageView setIcon(View view, int position)
    {
        return null;
    }

    @Override
    protected TextView setLabel(View view, int position)
    {
        Param param = getItem(position);
        TextView nameView = Utils.findView(view, R.id.name);
        nameView.setText(param.getName());
        TextView valueView = Utils.findView(view, R.id.value);
        String value = param.getValue();
        if (value == null)
        {
            valueView.setText(R.string.defaultValue);
        }
        else
        {
            valueView.setText(value);
        }
        return null;
    }
}
