package ru.net.serbis.dbmanager.dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.adapter.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class ParamsDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    protected ListView list;

    public ParamsDialog(Context context, String name, List<String> names, List<String> values, boolean readOnly)
    {
        super(context);
        setTitle(name);
        setIcon(android.R.drawable.ic_menu_revert);
        
        list = new ListView(context);  
        ParamsAdapter adapter = new ParamsAdapter(context, names, values);
        list.setAdapter(adapter);
        setView(list);

        if (!readOnly)
        {
            setPositiveButton(android.R.string.ok, this);
        }
        setNegativeButton(android.R.string.cancel, this);

        show();
    }
    
    @Override
    public AlertDialog show()
    {
        AlertDialog dialog = super.show();
        dialog.getWindow().clearFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
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
        List<String> values = new ArrayList<String>();
        ParamsAdapter adapter = Utils.getAdapter(list);
        for(int i = 0; i < adapter.getCount(); i++)
        {
            values.add(adapter.getItem(i).getValue());
        }
        ready(values);
    }

    protected void onClickCancel()
    {
    }

    protected abstract void ready(List<String> values);
}
