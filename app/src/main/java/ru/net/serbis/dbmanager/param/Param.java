package ru.net.serbis.dbmanager.param;

import android.content.*;

public class Param
{
    private String name;
    private String value;

    public Param(String name)
    {
        this.name = name;
    }
    
    public Param(Context context, int name)
    {
        this(context.getResources().getString(name));
    }
    
    public Param(Context context, int name, String value)
    {
        this(context, name);
        this.value = value;
    }

    public Param(String name, String value)
    {
        this(name);
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
