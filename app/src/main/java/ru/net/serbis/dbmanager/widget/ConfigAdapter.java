package ru.net.serbis.dbmanager.widget;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;
import ru.net.serbis.dbmanager.query.*;

public class ConfigAdapter extends IconAdapter<AppDbQuery>
{
    private int checked = -1;
    
    public ConfigAdapter(Context context, List<AppDbQuery> objects, int iconId)
    {
        super(context, R.layout.config, R.layout.check_row, objects, iconId);
    }

    public void setChecked(int checked)
    {
        this.checked = checked;
    }

    public int getChecked()
    {
        return checked;
    }

    @Override
    protected String getLabel(int position)
    {
        return getItem(position).getQuery().getName();
    }

    @Override
    protected TextView setLabel(View view, int position)
    {
        TextView label = super.setLabel(view, position);
        CheckedTextView checkedLabel = (CheckedTextView) label;
        checkedLabel.setChecked(position == checked);
        return label;
    }
}
