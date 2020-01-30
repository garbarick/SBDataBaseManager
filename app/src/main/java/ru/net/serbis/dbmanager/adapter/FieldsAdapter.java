package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public class FieldsAdapter extends ParamsAdapter
{
    protected List<String> values;
    protected boolean readOnly;

    public FieldsAdapter(Context context, List<String> names, List<String> values, boolean readOnly)
    {
        super(context, R.layout.fields, R.layout.field, names);
        this.values = values;
        this.readOnly = readOnly;
    }

    @Override
    protected View createChild(String name, int position)
    {
        View field = super.createChild(name, position);
        
        TextView text = Utils.findView(field, R.id.name);
        text.setText(name);
        
        String value = "";
        if (values != null && values.size() > position)
        {
            value = values.get(position);
        }
        EditText edit = Utils.findView(field, R.id.value);
        edit.setText(value);

        if (readOnly)
        {
            edit.setKeyListener(null);
            edit.setTextIsSelectable(true);
        }

        return field;
    }
    
    @Override
    public List<String> getValues()
    {
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < fields.getChildCount(); i++)
        {
            View field = fields.getChildAt(i);
            EditText value = Utils.findView(field, R.id.value);
            result.add(value.getText().toString());
        }
        return result;
	}
}
