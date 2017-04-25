package ru.net.serbis.dbmanager.adapter;

import android.app.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public abstract class Adapter <T> extends BaseAdapter<T>
{
    public Adapter(Activity context, List<T> objects)
    {
        super(context, R.layout.main, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = getContext().getLayoutInflater().inflate(R.layout.row, null);
        }

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageDrawable(getIcon(position));

        TextView label = (TextView) view.findViewById(R.id.label);
        label.setText(getLabel(position));

        return view;
    }
    
    protected abstract Drawable getIcon(int position);
    protected abstract String getLabel(int position);
}
