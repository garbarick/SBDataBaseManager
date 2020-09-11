package ru.net.serbis.dbmanager.app.db;

import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.param.*;
import ru.net.serbis.dbmanager.util.*;

public class Bind
{
    public Object[] convertData(ParamMap params, String[] data)
    {
        if (data == null || data.length == 0)
        {
            return data;
        }
        if (params.containsKey(Constants.CHARSET))
        {
            String charset = params.get(Constants.CHARSET);
            Object[] result = new Object[data.length];
            for(int i = 0; i < data.length; i++)
            {
                result[i] = convert(data[i], charset);
            }
            return result;
        }
        return data;
    }

    private Object convert(String str, String charset)
    {
        if (Utils.isEmpty(str))
        {
            return str;
        }
        try
        {
            return str.getBytes(charset);
        }
        catch (Exception e)
        {
            return str;
        }
    }
}
