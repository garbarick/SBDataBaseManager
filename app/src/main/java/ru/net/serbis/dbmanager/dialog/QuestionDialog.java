package ru.net.serbis.dbmanager.dialog;

import android.content.*;
import ru.net.serbis.dbmanager.*;
import android.app.*;

public class QuestionDialog extends MessageDialog
{
    public QuestionDialog(Context context, int message)
    {
        super(
            context,
            message,
            "",
            android.R.drawable.ic_dialog_alert);
    }

    @Override
    public AlertDialog show()
    {
        setNegativeButton(android.R.string.cancel, null);
        return super.show();
    }
}
