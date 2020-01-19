package ru.net.serbis.dbmanager.dialog;

import android.app.*;
import android.content.*;
import ru.net.serbis.dbmanager.*;

public class MessageDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    public MessageDialog(Context context, int title, String message)
    {
        super(context);
        setTitle(context.getResources().getString(title))
            .setMessage(message)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, this)
            .show();
    }
    
    public MessageDialog(Context context, int title, int message)
    {
        this(
            context,
            title,
            context.getResources().getString(message));
    }
    
    @Override
    public void onClick(DialogInterface dialog, int which)
    {
    }
}
