package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;
import java.text.*;
import android.app.*;
import android.widget.CalendarView.*;

public class FieldsAdapter extends ParamsAdapter
{
    private Map<String, String> types;
    private List<String> values;
    private boolean readOnly;
    private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FieldsAdapter(Context context, List<String> names, Map<String, String> types, List<String> values, boolean readOnly)
    {
        super(context, R.layout.fields, R.layout.field, names);
        this.types = types;
        this.values = values;
        this.readOnly = readOnly;
    }

    @Override
    protected View createChild(String name, int position)
    {
        switch (getType(name))
        {
            case Constants.TYPE_BOOLEAN:
                return createChildBoolean(name, position);
            case Constants.TYPE_TIMESTAMP:
                return createChildDateTime(name, position);
            default:
                return createChildString(name, position);
        }
    }

    private String getType(String name)
    {
        if (types == null || !types.containsKey(name))
        {
            return Constants.TYPE_STRING;
        }
        return types.get(name);
    }

    protected View createChildString(String name, int position)
    {
        View field = super.createChild(name, position);
        return createChildString(field, name, position);
    }

    protected View createChildString(View field, String name, int position)
    {
        initName(field, name);

        String value = getValue(position);
        EditText edit = Utils.findView(field, R.id.value);
        edit.setText(value);

        if (readOnly)
        {
            edit.setKeyListener(null);
            edit.setTextIsSelectable(true);
        }

        return field;
    }

    private void initName(View field, String name)
    {
        TextView text = Utils.findView(field, R.id.name);
        text.setText(name);
    }

    private String getValue(int position)
    {
        String value = "";
        if (values != null && values.size() > position)
        {
            value = values.get(position);
        }
        return value;
    }

    protected View createChildBoolean(String name, int position)
    {
        View field = createView(R.layout.field_boolean);
        initName(field, name);

        String value = getValue(position);
        CheckBox check = Utils.findView(field, R.id.value);
        check.setChecked(Utils.toInt(value) > 0);
        check.setEnabled(!readOnly);

        return field;
    }

    protected View createChildDateTime(String name, int position)
    {
        if (readOnly)
        {
            return createChildString(name, position);
        }
        View field = createView(R.layout.field_timestamp);
        createChildString(field, name, position);

        final EditText edit = Utils.findView(field, R.id.value);
        ImageButton current = Utils.findView(field, R.id.current);
        current.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    edit.setText(FORMAT.format(new Date()));
                }
            }
        );
        ImageButton date = Utils.findView(field, R.id.date);
        date.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    final Date date = getDate(edit.getText().toString());
                    new DatePickerDialog(
                        context,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day)
                            {
                                date.setYear(year - 1900);
                                date.setMonth(month);
                                date.setDate(day);
                                edit.setText(FORMAT.format(date));
                            }
                        },
                        date.getYear() + 1900,
                        date.getMonth(),
                        date.getDate()
                    ).show();
                }
            }
        );
        ImageButton time = Utils.findView(field, R.id.time);
        time.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    final Date date = getDate(edit.getText().toString());
                    new TimePickerDialog(
                        context,
                        new TimePickerDialog.OnTimeSetListener()
                        {
                            @Override
                            public void onTimeSet(TimePicker view, int hours, int minutes)
                            {
                                date.setHours(hours);
                                date.setMinutes(minutes);
                                edit.setText(FORMAT.format(date));
                            }
                        },
                        date.getHours(),
                        date.getMinutes(),
                        true
                    ).show();
                }
            }
        );
        return field;
    }

    private Date getDate(String text)
    {
        try
        {
            return FORMAT.parse(text);
        }
        catch (Exception e)
        {
            return new Date();
        }
    }

    @Override
    public List<String> getValues()
    {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < fields.getChildCount(); i++)
        {
            View field = fields.getChildAt(i);

            View view = Utils.findView(field, R.id.value);
            String value = null;
            if (view instanceof EditText)
            {
                EditText edit = (EditText) view;
                value = edit.getText().toString();
            }
            else if (view instanceof CheckBox)
            {
                CheckBox check = (CheckBox) view;
                value = check.isChecked() ? "1" : "0";
            }
            result.add(value);
        }
        return result;
	}
}
