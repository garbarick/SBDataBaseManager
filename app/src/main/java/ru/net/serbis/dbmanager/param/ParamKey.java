package ru.net.serbis.dbmanager.param;

import android.content.*;

public class ParamKey extends Param
{
    private String key;

    public ParamKey(String key, Context context, int id)
    {
        super(context, id);
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
}
