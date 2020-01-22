package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public class FieldsAdapter
{
    protected Context context;
    protected View view;
    protected LinearLayout fields;
    protected boolean readOnly;

    public FieldsAdapter(Context context, List<String> names, List<String> values, boolean readOnly)
    {
        this.context = context;
        this.readOnly = readOnly;
        initView();
        initParams(names, values);
    }

    public View getView()
    {
        return view;
    }

    protected void initView()
    {
        view = LayoutInflater.from(context).inflate(R.layout.fields, null);
        fields = Utils.findView(view, R.id.fields);
    }

    protected void initParams(List<String> names, List<String> values)
    {
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            String value = "";
            if (values != null && values.size() > i)
            {
                value = values.get(i);
            }
            fields.addView(createFieldView(name, value));
        }
    }
    
    protected View createFieldView(String name, String value)
    {
        View field = LayoutInflater.from(context).inflate(R.layout.field, null);
        
        TextView text = Utils.findView(field, R.id.name);
        text.setText(name);
        
        EditText edit = Utils.findView(field, R.id.value);
        edit.setText(value);
        
        if (readOnly)
        {
            edit.setKeyListener(null);
            edit.setTextIsSelectable(true);
        }
        
        return field;
    }

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
