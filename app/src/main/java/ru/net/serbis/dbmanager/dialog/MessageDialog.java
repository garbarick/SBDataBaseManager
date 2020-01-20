package ru.net.serbis.dbmanager.dialog;

import android.app.*;
import android.content.*;
import ru.net.serbis.dbmanager.*;

public class MessageDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    public MessageDialog(Context context, String title, String message, int icon)
    {
        super(context);
        setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, this)
            .show();
    }
    
    public MessageDialog(Context context, int title, String message, int icon)
    {
        this(
            context,
            context.getResources().getString(title),
            message,
            icon);
    }
    
    public MessageDialog(Context context, int title, int message, int icon)
    {
        this(
            context,
            title,
            context.getResources().getString(message),
            icon);
    }
    
    @Override
    public void onClick(DialogInterface dialog, int which)
    {
    }
}
