package ru.net.serbis.dbmanager.app.db;

import android.content.*;
import android.database.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.param.*;

public class DBResult
{
    private List<String> columns;
    private Map<String, String> types;
    private List<List<String>> rows = new ArrayList<List<String>>();

    public List<String> getColumns()
    {
        return columns;
    }

    public Map<String, String> getTypes()
    {
        return types;
    }

    public List<List<String>> getRows()
    {
        return rows;
    }

    public void init(
        Context context,
        ParamMap params,
        Cursor cursor,
        boolean withColumns,
        boolean withRowNums)
    {
        int size = cursor.getColumnCount();
        if (withColumns)
        {
            initColumns(context, cursor, withRowNums, size);
        }
        initRows(params, cursor, withRowNums, size);
    }
    
    public void initTypes(Cursor cursor)
    {
        types = new HashMap<String, String>(columns.size());
        if (cursor.moveToFirst())
        {
            do
            {
                int name = cursor.getColumnIndex("name");
                int type = cursor.getColumnIndex("type");
                types.put(cursor.getString(name), cursor.getString(type));
            }
            while(cursor.moveToNext());
        }
    }
    
    private List<String> getList(boolean withRowNums, int size)
    {
        return new ArrayList<String>(withRowNums? size + 1: size);
    }
    
    private void initColumns(Context context, Cursor cursor, boolean withRowNums, int size)
    {
        columns = getList(withRowNums, size);
        if (withRowNums)
        {
            columns.add(context.getResources().getString(R.string.num));
        }
        for (int i = 0; i < size; i++)
        {
            columns.add(cursor.getColumnName(i));
        }
    }
    
    private void initRows(ParamMap params, Cursor cursor, boolean withRowNums, int size)
    {
        rows = new ArrayList<List<String>>();
        if (cursor.moveToFirst())
        {
            int y = 1;
            do
            {
                List<String> row = getList(withRowNums, size);
                if (withRowNums)
                {
                    row.add(String.valueOf(y++));
                }
                for (int i = 0; i < size; i++)
                {
                    row.add(getString(params, cursor, i));
                }
                rows.add(row);
            }
            while(cursor.moveToNext());
        }
    }

    private String getString(ParamMap params, Cursor cursor, int i)
    {
        try
        {
            if (cursor.isNull(i))
            {
                return null;
            }
            if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING ||
                cursor.getType(i) == Cursor.FIELD_TYPE_BLOB)
            {
                if (params.containsKey(Constants.CHARSET))
                {
                    String charset = params.get(Constants.CHARSET);
                    return new String(cursor.getBlob(i), charset);
                }
            }
            return cursor.getString(i);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
