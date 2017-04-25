package ru.net.serbis.dbmanager.table;

import android.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public class TableAdapter extends ArrayAdapter<List<String>>
{
    private int column = 1;
    
    public TableAdapter(Activity context, List<List<String>> objects)
    {
        super(context, R.layout.content, objects);
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        Row row;
        if (view == null)
        {
             row = new Row(getContext());
        }
        else
        {
            row = (Row) view;
        }
        row.setCells(getItem(position));
        row.setColumn(column);
        row.update();
        return row;
    }
}
