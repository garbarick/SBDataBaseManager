package ru.net.serbis.dbmanager.query;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.util.*;
import ru.net.serbis.dbmanager.dialog.*;

public class Edit extends Activity implements View.OnClickListener
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
        if (intent.hasExtra(Constants.QUERY))
        {
            query = (Query) intent.getSerializableExtra(Constants.QUERY);
            editName.setText(query.getName());
            editQuery.setText(query.getQuery());
        }
        
        initButton(R.id.save);
        initButton(R.id.execute);
        initButton(R.id.info);
        initButton(R.id.cancel);
    }
    
    private Query getQuery(Query query)
    {
        return new Query(
            query != null ? query.getId() : 0,
            editName.getText().toString(),
            editQuery.getText().toString());
    }
    
    private void initButton(int id)
    {
        Button button = Utils.findView(this, id);
        button.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch(id)
        {
            case R.id.save:
                saveAction();
                break;
            case R.id.execute:
                executeAction();
                break;
            case R.id.info:
                infoAction();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    private void saveAction()
    {
        Intent intent = new Intent(getIntent());
        intent.putExtra(Constants.QUERY, getQuery(query));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void executeAction()
    {
        new QueryExecutor(this, new AppDbQuery(appDp, getQuery(query)));
    }
    
    private void infoAction()
    {
        new MessageDialog(this, R.string.info, R.string.queryInfo);
    }
}
