package ru.net.serbis.dbmanager.dialog;

import android.app.*;
import android.content.*;
import java.util.*;
import ru.net.serbis.dbmanager.adapter.*;

public abstract class FieldsDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    protected FieldsAdapter adapter;
    
    public FieldsDialog(Context context, String name, List<String> names, List<String> values, boolean readOnly)
    {
        super(context);
        setTitle(name);
        setIcon(android.R.drawable.ic_menu_revert);
        
        adapter = new FieldsAdapter(context, names, values, readOnly);
        setView(adapter.getView());
        
        if (!readOnly)
        {
            setPositiveButton(android.R.string.ok, this);
        }
        setNegativeButton(android.R.string.cancel, this);

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

            case Dialog.BUTTON_NEGATIVE:
                onClickCancel();
                break;
        }
    }

    protected void onClickOk()
    {
        ready(adapter.getValues());
    }

    protected void onClickCancel()
    {
    }

    protected abstract void ready(List<String> values);
}
