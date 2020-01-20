package ru.net.serbis.dbmanager.dialog;

import android.content.*;
import ru.net.serbis.dbmanager.*;

public class InfoMessage extends MessageDialog
{
    public InfoMessage(Context context, String title, String message)
    {
        super(
            context,
            title,
            message,
            android.R.drawable.ic_dialog_info);
    }
    
    public InfoMessage(Context context, int title, int message)
    {
        super(
            context,
            title,
            message,
            android.R.drawable.ic_dialog_info);
    }
}
