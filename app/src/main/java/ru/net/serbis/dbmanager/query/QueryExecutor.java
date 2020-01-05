package ru.net.serbis.dbmanager.query;

import android.app.*;
import android.content.*;
import java.util.*;
import java.util.regex.*;
import ru.net.serbis.dbmanager.dialog.*;
import ru.net.serbis.dbmanager.table.*;

public class QueryExecutor
{
    private static final Pattern BINDS = Pattern.compile("#(\\S*?)#");
    
    private Activity context;
    private Query query;
      
    public QueryExecutor(Activity context, Query query)
    {
        this.context = context;
        this.query = query;
        initBinds();
    }

    private void initBinds()
    {
        final StringBuffer result = new StringBuffer();
        final Matcher matcher = BINDS.matcher(query.getQuery());
        List<String> names = new ArrayList<String>();
        while (matcher.find())
        {
            names.add(matcher.group(1));
            matcher.appendReplacement(result, "?");
        }
        if (names.isEmpty())
        {
            readyQuery(query);
            return;
        }
        new BindsDialog(context, names)
        {
            @Override
            protected void ready(List<String> values)
            {
                Query query = new Query();
                query.setBinds(values);
                
                matcher.appendTail(result);
                query.setQuery(result.toString());
                
                readyQuery(query);
            }
        };
    }
    
    private void readyQuery(Query query)
    {
        Intent intent = new Intent(context.getIntent());
        intent.setClass(context, Table.class);
        intent.putExtra(Table.QUERY, query);
        context.startActivity(intent);
    }
}
