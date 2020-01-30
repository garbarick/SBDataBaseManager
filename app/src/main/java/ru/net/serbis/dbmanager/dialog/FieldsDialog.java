package ru.net.serbis.dbmanager.dialog;

import android.content.*;
import java.util.*;
import ru.net.serbis.dbmanager.adapter.*;

public abstract class FieldsDialog extends ParamsDialog
{
    public FieldsDialog(Context context, String name, List<String> names, List<String> values, boolean readOnly)
    {
        super(context, name, android.R.drawable.ic_menu_revert);
        
        adapter = new FieldsAdapter(context, names, values, readOnly);
        initView();
        
        if (!readOnly)
        {
            setPositiveButton(android.R.string.ok, this);
        }
        setNegativeButton(android.R.string.cancel, this);

        show();
    }
}
