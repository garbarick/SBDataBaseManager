package ru.net.serbis.dbmanager.param;

import java.util.*;

public class ParamMap extends HashMap<String, String>
{
    public boolean put(String key, Boolean value)
    {
        put(key, value.toString());
        return value;
    }
    
    public boolean is(String key)
    {
        return containsKey(key) && Boolean.valueOf(get(key));
    }
}
