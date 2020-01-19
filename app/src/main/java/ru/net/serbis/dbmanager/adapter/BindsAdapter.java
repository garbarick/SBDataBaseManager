package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.param.*;
import ru.net.serbis.dbmanager.util.*;

public class BindsAdapter extends ArrayAdapter<Param> implements View.OnFocusChangeListener
{
    public BindsAdapter(Context context, List<String> names)
    {
        super(context, android.R.layout.simple_list_item_1);
        for (String name : names)
        {
            add(new Param(name, ""));
        }
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.bind, null);
        }

        Param item = getItem(position);
        TextView name = Utils.findView(view, R.id.name);
        name.setText(item.getName());
        
        EditText value = Utils.findView(view, R.id.value);
        value.setText(item.getValue());
        value.setOnFocusChangeListener(this);

        view.setTag(position);
        return view;
	}

    @Override
    public void onFocusChange(View view, boolean focused)
    {
        if (focused)
        {
            return;
        }
        EditText value = (EditText) view;
        ViewGroup group = (ViewGroup) view.getParent();
        int position = group.getTag();
        getItem(position).setValue(value.getText().toString());
    }
}
