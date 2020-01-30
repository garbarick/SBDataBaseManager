package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class ParamsAdapter
{
    protected Context context;
    protected int layoutId;
    protected int rowLayoutId;
    protected List<String> names;
    protected View view;
    protected LinearLayout fields;

    public ParamsAdapter(Context context, int layoutId, int rowLayoutId, List<String> names)
    {
        this.context = context;
        this.layoutId = layoutId;
        this.rowLayoutId = rowLayoutId;
        this.names = names;
    }
    
    public View getView()
    {
        if (view == null)
        {
            initView();
            createChildren(names);
        }
        return view;
    }

    protected void initView()
    {
        view = LayoutInflater.from(context).inflate(layoutId, null);
        fields = Utils.findView(view, R.id.fields);
    }
    
    protected void createChildren(List<String> names)
    {
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            fields.addView(createChild(name, i));
        }
    }
    
    protected View createChild(String name, int position)
    {
        return LayoutInflater.from(context).inflate(rowLayoutId, null);
    }
    
    public abstract List<String> getValues();
}
