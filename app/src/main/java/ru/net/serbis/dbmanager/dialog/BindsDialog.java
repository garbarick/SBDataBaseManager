package ru.net.serbis.dbmanager.dialog;

import android.content.*;
import java.util.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class BindsDialog extends FieldsDialog
{
    private boolean closeParent;

    public BindsDialog(Context context, String name, List<String> names, boolean closeParent)
    {
        super(context, name, names, null, null, false);
        this.closeParent = closeParent;
    }
   
    @Override
    protected void onClickCancel()
    {
        Utils.closeActivity(context, closeParent);
    }
}
