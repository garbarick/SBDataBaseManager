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
    
    public BindsDialog(Context context, List<String> names)
    {
        super(context);
        
        setTitle(R.string.binds);
        
        list = new ListView(context);  
        BindsAdapter adapter = new BindsAdapter(context, names);
        list.setAdapter(adapter);
        setView(list);

        setPositiveButton(android.R.string.ok, this);
        setNegativeButton(android.R.string.cancel, null);

        Dialog dialog = show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(DialogInterface dialog, int id)
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
    
    protected abstract void ready(List<String> values);
}
