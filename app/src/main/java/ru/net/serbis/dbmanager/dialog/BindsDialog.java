package ru.net.serbis.dbmanager.dialog;

import android.content.*;
import java.util.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class BindsDialog extends ParamsDialog
{
    private Context context;
    private boolean closeParent;

    public BindsDialog(Context context, String name, List<String> names, boolean closeParent)
    {
        super(context, name, names, null, false);
        this.context = context;
        this.closeParent = closeParent;
    }
   
    @Override
    protected void onClickCancel()
    {
        Utils.closeActivity(context, closeParent);
    }
}
