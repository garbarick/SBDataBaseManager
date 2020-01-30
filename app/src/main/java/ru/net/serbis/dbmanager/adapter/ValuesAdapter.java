package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.widget.*;
import java.util.*;

public class ValuesAdapter extends ArrayAdapter<String>
{
    public ValuesAdapter(Context context, List<String> values)
    {
        super(context, android.R.layout.simple_list_item_1, values);
    }
}
