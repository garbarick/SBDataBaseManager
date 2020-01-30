package ru.net.serbis.dbmanager.dialog;

import android.content.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.adapter.*;

public abstract class MapingDialog extends ParamsDialog
{
    public MapingDialog(Context context, String table, List<String> jsonColmns, List<String> columns)
    {
        super(
            context,
            context.getResources().getString(R.string.mappingColumns),
            android.R.drawable.ic_menu_mapmode);
        
        adapter = new MapingAdapter(context, table, jsonColmns, columns);
        initView();

        setPositiveButton(android.R.string.ok, this);
        setNegativeButton(android.R.string.cancel, this);
        show();
    }
}
