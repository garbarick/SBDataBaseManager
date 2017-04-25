package ru.net.serbis.dbmanager.query;

import java.io.*;

public class Query implements Serializable
{
    private long id;
    private String name;
    private String query;

    public Query(long id, String name, String query)
    {
        this(name, query);
        this.id = id;
    }
    
    public Query(String name, String query)
    {
        this.name = name;
        this.query = query;
    }
    
    public Query()
    {
    }

    public void setId(long id)
    {
        this.id = id;
    }
    
    public long getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public String getQuery()
    {
        return query;
    }
}
