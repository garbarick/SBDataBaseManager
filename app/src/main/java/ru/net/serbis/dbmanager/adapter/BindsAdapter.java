package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public class BindsAdapter extends ArrayAdapter<String>
{
    public BindsAdapter(Context context, List<String> names)
    {
        super(context, android.R.layout.simple_list_item_1, names);
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.bind, null);
        }

        TextView name = Utils.findView(view, R.id.name);
        name.setText(getItem(position));

        return view;
	}
}
