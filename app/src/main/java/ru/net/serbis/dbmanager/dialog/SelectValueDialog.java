package ru.net.serbis.dbmanager.dialog;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public abstract class SelectValueDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    private NumberPicker view;
    
    public SelectValueDialog(Context context, String name, String current, Collection<String> values)
    {
        super(context);
        setTitle(name);
        
        view = new NumberPicker(context);
        view.setMinValue(0);
        view.setMaxValue(values.size());
        
        List<String> selectedValues = new ArrayList<String>();
        selectedValues.add(context.getResources().getString(R.string.defaultValue));
        selectedValues.addAll(values);
        view.setDisplayedValues(selectedValues.toArray(new String[selectedValues.size()]));
        
        if (current == null)
        {
            view.setValue(0);
        }
        else
        {
            view.setValue(selectedValues.indexOf(current));
        }
        
        setView(view);
        setPositiveButton(android.R.string.ok, this);
        setNeutralButton(R.string.defaultValue, this);
        setNegativeButton(android.R.string.cancel, null);
        show();
    }

    @Override
    public void onClick(DialogInterface dialog, int id)
    {
        switch(id)
        {
            case Dialog.BUTTON_POSITIVE:
                onClickOk();
                break;

            case Dialog.BUTTON_NEUTRAL:
                onClickOk(null);
                break;
        }
    }
    
    protected void onClickOk()
    {
        if (view.getValue() == 0)
        {
            onClickOk(null);
        }
        else
        {
            onClickOk(view.getDisplayedValues()[view.getValue()]);
        }
    }
    
    protected abstract void onClickOk(String value);
}
