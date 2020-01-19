package ru.net.serbis.dbmanager.param;

import android.view.*;
import android.widget.*;
import java.nio.charset.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.db.*;
import ru.net.serbis.dbmanager.dialog.*;
import ru.net.serbis.dbmanager.folder.*;
import ru.net.serbis.dbmanager.util.*;

public class Params extends Folder
{
    private Helper helper;
    private List<ParamKey> params = new ArrayList<ParamKey>();

    @Override
    protected void initCreate()
    {
        super.initCreate();
        helper = new Helper(this);
        params.add(new ParamKey(Constants.CHARSET, this, R.string.charset));
    }

    @Override
    protected void initMain()
    {
        ParamAdapter adapter = new ParamAdapter(this, params);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {               
        final ParamKey param = (ParamKey) parent.getItemAtPosition(position);
        if (!Constants.CHARSET.equals(param.getKey()))
        {
            return;
        }
        final ParamAdapter adapter = Utils.getAdapter(parent);
        new SelectValueDialog(
            Params.this,
            param.getName(),
            param.getValue(),
            Charset.availableCharsets().keySet())
        {
            @Override
            protected void onClickOk(String value)
            {
                param.setValue(value);
                if (helper.setParam(appDb, param))
                {
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public void inBackground()
    {
        helper.initParams(appDb, params);
    }
}
