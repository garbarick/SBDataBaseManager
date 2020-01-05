package ru.net.serbis.dbmanager.dialog;

import android.app.*;
import android.content.*;
import ru.net.serbis.dbmanager.*;

public abstract class MessageDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    public MessageDialog(Context context, String error)
    {
        super(context);
        setTitle(context.getResources().getString(R.string.error))
            .setMessage(error)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, this)
            .show();
    }
}
