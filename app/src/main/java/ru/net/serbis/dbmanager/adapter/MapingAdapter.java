package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public class MapingAdapter extends ParamsAdapter
{
    protected String table;
    protected List<String> columns = new ArrayList<String>();
    
    public MapingAdapter(Context context, String table, List<String> jsonColmns, List<String> columns)
    {
        super(context, R.layout.mapping, R.layout.map, jsonColmns);
        this.table = table;
        this.columns.add("");
        this.columns.addAll(columns);
    }

    @Override
    protected void initView()
    {
        super.initView();
        TextView text = Utils.findView(view, R.id.table);
        text.setText(table);
    }
    
    @Override
    protected View createChild(String name, int position)
    {
        View field = super.createChild(name, position);
        
        TextView text = Utils.findView(field, R.id.columnFrom);
        text.setText(name);
        
        Spinner value = Utils.findView(field, R.id.columnTo);
        value.setAdapter(new ValuesAdapter(context, columns));
        int current = columns.indexOf(name);
        if (current > 0)
        {
            value.setSelection(current);
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
            Spinner value = Utils.findView(field, R.id.columnTo);
            result.add(value.getSelectedItem().toString());
        }
        return result;
    }
}
