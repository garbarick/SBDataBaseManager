package ru.net.serbis.dbmanager.dialog;

import android.content.*;
import ru.net.serbis.dbmanager.*;

public class AlertMessage extends MessageDialog
{
    public AlertMessage(Context context, String message)
    {
        super(
            context,
            R.string.error,
            message,
            android.R.drawable.ic_dialog_alert);
    }

    public AlertMessage(Context context, int message)
    {
        super(
            context,
            R.string.error,
            message,
            android.R.drawable.ic_dialog_alert);
    }
}
