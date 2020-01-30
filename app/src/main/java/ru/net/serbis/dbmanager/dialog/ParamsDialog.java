package ru.net.serbis.dbmanager.dialog;

import android.app.*;
import android.content.*;
import java.util.*;
import ru.net.serbis.dbmanager.adapter.*;

public abstract class ParamsDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    protected Context context;
    protected ParamsAdapter adapter;
    
    public ParamsDialog(Context context, String name, int iconId)
    {
        super(context);
        this.context = context;
        setTitle(name);
        setIcon(iconId);
        setCancelable(false);
    }
    
    protected void initView()
    {
        adapter = getAdapter();
        setView(adapter.getView());
    }
    
    protected ParamsAdapter getAdapter()
    {
        return adapter;
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
