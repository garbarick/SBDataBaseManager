package ru.net.serbis.dbmanager.param;

import java.util.*;

public class ParamMap
{
    protected Map<String, String> data = new HashMap<String, String>();

    public boolean put(String key, Object value)
    {
        data.put(key, String.valueOf(value));
        return value;
    }

    public boolean is(String key)
    {
        return containsKey(key) && Boolean.valueOf(get(key));
    }

    public boolean containsKey(String key)
    {
        return data.containsKey(key);
    }

    public String get(String key)
    {
        return data.get(key);
    }
}
