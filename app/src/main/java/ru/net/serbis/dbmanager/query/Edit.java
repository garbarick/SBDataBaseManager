package ru.net.serbis.dbmanager.query;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.table.*;
import ru.net.serbis.dbmanager.util.*;

public class Edit extends Activity
{
    private EditText editName;
    private EditText editQuery;
    private AppDb appDp;
    private Query query;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query);
        setResult(RESULT_CANCELED);
        
        editName = Utils.findView(this, R.id.name);
        editQuery = Utils.findView(this, R.id.query);
        
        Intent intent = getIntent();
        appDp = Utils.getAppDb(intent);
        if (intent.hasExtra(Table.QUERY))
        {
            query = (Query) intent.getSerializableExtra(Table.QUERY);
            editName.setText(query.getName());
            editQuery.setText(query.getQuery());
        }
        
        initSave();
        initCancel();
        initExecute();
    }
    
    private Query getQuery(Query query)
    {
        return new Query(
            query != null ? query.getId() : 0,
            editName.getText().toString(),
            editQuery.getText().toString());
    }
    
    private void initSave()
    {
        Button button = Utils.findView(this, R.id.save);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    Intent intent = new Intent(getIntent());
                    intent.putExtra(Table.QUERY, getQuery(query));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        );
    }
    
    private void initCancel()
    {
        Button button = Utils.findView(this, R.id.cancel);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    finish();
                }
            }
        );
    }
    
    private void initExecute()
    {
        Button button = Utils.findView(this, R.id.execute);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    new QueryExecutor(Edit.this, new AppDbQuery(appDp, getQuery(query)));
                }
            }
        );
    }
}
