package ru.net.serbis.dbmanager.dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class BindsDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    private ListView list;
    private Context context;
    private boolean closeParent;

    public BindsDialog(Context context, String name, List<String> names, boolean closeParent)
    {
        super(context);
        this.context = context;
        this.closeParent = closeParent;
        setTitle(name);
        
        list = new ListView(context);  
        BindsAdapter adapter = new BindsAdapter(context, names);
        list.setAdapter(adapter);
        setView(list);

        setPositiveButton(android.R.string.ok, this);
        setNegativeButton(android.R.string.cancel, this);

        Dialog dialog = show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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

    private void onClickOk()
    {
        List<String> values = new ArrayList<String>();
        for(int i = 0; i < list.getChildCount(); i++)
        {
            View view = list.getChildAt(i);
            EditText value = Utils.findView(view, R.id.value);
            values.add(value.getText().toString());
        }
        ready(values);
    }
    
    private void onClickCancel()
    {
        Utils.closeActivity(context, closeParent);
    }
    
    protected abstract void ready(List<String> values);
}
