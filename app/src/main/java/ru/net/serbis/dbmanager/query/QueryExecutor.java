package ru.net.serbis.dbmanager.query;

import android.content.*;
import java.util.*;
import java.util.regex.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.dialog.*;
import ru.net.serbis.dbmanager.table.*;
import ru.net.serbis.dbmanager.util.*;

public class QueryExecutor
{
    private static final Pattern BINDS = Pattern.compile("#(\\S*?)#");
    
    private Context context;
    private AppDbQuery appDbQuery;
    private boolean closeParent;
      
    public QueryExecutor(Context context, AppDbQuery appDbQuery)
    {
        this(context, appDbQuery, false);
    }
    
    public QueryExecutor(Context context, AppDbQuery appDbQuery, boolean closeParent)
    {
        this.context = context;
        this.appDbQuery = appDbQuery;
        this.closeParent = closeParent;
        initBinds();
    }

    private void initBinds()
    {
        final Query baseQuery = appDbQuery.getQuery();
        final StringBuffer result = new StringBuffer();
        final Matcher matcher = BINDS.matcher(baseQuery.getQuery());
        List<String> names = new ArrayList<String>();
        while (matcher.find())
        {
            names.add(matcher.group(1));
            matcher.appendReplacement(result, "?");
        }
        if (names.isEmpty())
        {
            readyQuery(baseQuery);
            return;
        }
        new BindsDialog(context, baseQuery.getName(), names, closeParent)
        {
            @Override
            protected void ready(List<String> values)
            {
                Query query = new Query();
                query.setName(baseQuery.getName());
                query.setBinds(values);
                
                matcher.appendTail(result);
                query.setQuery(result.toString());
                
                readyQuery(query);
            }
        };
    }
    
    private void readyQuery(Query query)
    {
        Intent intent = new Intent(context, Table.class);
        AppDb appDb = appDbQuery.getAppDb();
        intent.putExtra(DataBases.APP, appDb.getApp());
        intent.putExtra(DataBases.DB, appDb.getDb());
        intent.putExtra(Table.QUERY, query);
        
        if (closeParent)
        {
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }
        
        context.startActivity(intent);
        
        Utils.closeActivity(context, closeParent);
    }
}
