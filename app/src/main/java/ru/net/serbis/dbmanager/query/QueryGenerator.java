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

    private String generateWhere(List<String> columns, List<String> pkColumns, List<String> values, List<String> binds)
    {
        StringBuilder where = new StringBuilder(" where ");
        boolean first = true;
        for(int i = 0; i < columns.size(); i++)
        {
            String column = columns.get(i);
            if (!pkColumns.isEmpty() && !pkColumns.contains(column))
            {
                continue;
            }
            String value = values.get(i);
            if (!first)
            {
                where.append(" and ");
            }
            first = false;
            if (Utils.isEmpty(value))
            {
                where.append(column).append(" is null");
            }
            else
            {
                where.append(column).append(" = ?");
                binds.add(value);
            }
        }
        return where.toString();
    }

    public Query generateDelete(String table, List<String> columns, List<String> pkColumns, List<String> values)
    {
        StringBuilder query = new StringBuilder("delete from " + table);
        List<String> binds = new ArrayList<String>();
        query.append(generateWhere(columns, pkColumns, values, binds));
        if (binds.isEmpty())
        {
            return null;
        }
        Query result = new Query();
        result.setQuery(query.toString());
        result.setBinds(binds);
        return result;
    }

    private String generateSet(List<String> columns, List<String> values, List<String> binds)
    {
        StringBuilder set = new StringBuilder(" set ");
        boolean first = true;
        for(int i = 0; i < columns.size(); i++)
        {
            String column = columns.get(i);
            String value = values.get(i);
            if (!first)
            {
                set.append(", ");
            }
            first = false;
            if (Utils.isEmpty(value))
            {
                set.append(column).append(" = null");
            }
            else
            {
                set.append(column).append(" = ?");
                binds.add(value);
            }
        }
        return set.toString();
    }

    public Query generateUpdate(String table, List<String> columns, List<String> pkColumns, List<String> oldValues, List<String> newValues)
    {
        StringBuilder query = new StringBuilder("update " + table);
        List<String> binds = new ArrayList<String>();
        query.append(generateSet(columns, newValues, binds));
        if (binds.isEmpty())
        {
            return null;
        }
        List<String> whereBinds = new ArrayList<String>();
        query.append(generateWhere(columns, pkColumns, oldValues, whereBinds));
        if (whereBinds.isEmpty())
        {
            return null;
        }
        binds.addAll(whereBinds);
        Query result = new Query();
        result.setQuery(query.toString());
        result.setBinds(binds);
        return result;
    }
}
