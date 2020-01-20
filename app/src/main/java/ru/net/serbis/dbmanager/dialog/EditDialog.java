package ru.net.serbis.dbmanager.dialog;

import android.app.*;
import android.content.*;
import android.widget.*;
import android.text.*;

public class EditDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    private EditText edit;
    
    public EditDialog(Context context, String title, String message, boolean readOnly)
    {
        super(context);
        setTitle(title);
        setIcon(android.R.drawable.ic_menu_edit);

        edit = new EditText(context);
        edit.setText(message);
        if (readOnly)
        {
            edit.setKeyListener(null);
            edit.setTextIsSelectable(true);
        }
        setView(edit);

        setPositiveButton(android.R.string.ok, this);
        if (!readOnly)
        {
            setNegativeButton(android.R.string.cancel, null);
        }
        show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        onClick(edit.getText().toString());
    }
    
    protected void onClick(String text)
    {
    }
}
