package ru.net.serbis.dbmanager.query;

import java.util.*;
import ru.net.serbis.dbmanager.util.*;

public class QueryGenerator
{
    public Query generateInsert(String table, List<String> columns, List<String> values)
    {
        StringBuilder query = new StringBuilder("insert into " + table + "(");
        StringBuilder data = new StringBuilder("values(");
        List<String> binds = new ArrayList<String>();
        boolean first = true;
        for(int i = 0; i < columns.size(); i++)
        {
            String column = columns.get(i);
            String value = values.get(i);
            if (Utils.isEmpty(value))
            {
                continue;
            }
            if (!first)
            {
                query.append(" ,");
                data.append(" ,");
            }
            first = false;
            query.append(column);
            data.append("?");
            binds.add(value);
        }
        if (first)
        {
            return null;
        }
        query.append(") ").append(data).append(")");
        Query result = new Query();
        result.setQuery(query.toString());
        result.setBinds(binds);
        return result;
    }

    public Query generateDelete(String table, List<String> columns, List<String> values)
    {
        StringBuilder query = new StringBuilder("delete from " + table + " where ");
        List<String> binds = new ArrayList<String>();
        boolean first = true;
        for(int i = 0; i < columns.size(); i++)
        {
            String column = columns.get(i);
            String value = values.get(i);
            if (!first)
            {
                query.append(" and ");
            }
            first = false;
            if (Utils.isEmpty(value))
            {
                query.append(column).append(" is null");
            }
            else
            {
                query.append(column).append(" = ?");
                binds.add(value);
            }
        }
        if (first)
        {
            return null;
        }
        Query result = new Query();
        result.setQuery(query.toString());
        result.setBinds(binds);
        return result;
    }

    public Query generateUpdate(String table, List<String> columns, List<String> oldValues, List<String> newValues)
    {
        StringBuilder query = new StringBuilder("update " + table + " set ");
        StringBuilder where = new StringBuilder(" where ");
        List<String> binds = new ArrayList<String>();
        List<String> whereBinds = new ArrayList<String>();
        boolean first = true;
        for(int i = 0; i < columns.size(); i++)
        {
            String column = columns.get(i);
            String oldValue = oldValues.get(i);
            String newValue = newValues.get(i);
            if (!first)
            {
                query.append(", ");
                where.append(" and ");
            }
            first = false;
            if (Utils.isEmpty(newValue))
            {
                query.append(column).append(" = null");
            }
            else
            {
                query.append(column).append(" = ?");
                binds.add(newValue);
            }
            if (Utils.isEmpty(oldValue))
            {
                where.append(column).append(" is null");
            }
            else
            {
                where.append(column).append(" = ?");
                whereBinds.add(oldValue);
            }
        }
        if (first)
        {
            return null;
        }
        query.append(" ").append(where);
        binds.addAll(whereBinds);
        Query result = new Query();
        result.setQuery(query.toString());
        result.setBinds(binds);
        return result;
    }
}
