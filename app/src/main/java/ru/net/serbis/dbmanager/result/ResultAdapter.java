package ru.net.serbis.dbmanager.result;

import android.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public class ResultAdapter extends ArrayAdapter<List<String>>
{
    private Width width;
    
    public ResultAdapter(Activity context, List<List<String>> objects, Width width)
    {
        super(context, R.layout.content, objects);
        this.width = width;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        Row row;
        if (view == null)
        {
             row = new Row(getContext());
             row.setWidth(width);
        }
        else
        {
            row = (Row) view;
        }
        row.setCells(getItem(position));
        row.update();
        return row;
    }
}
