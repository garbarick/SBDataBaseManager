package ru.net.serbis.dbmanager.task;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public abstract class AsyncActivity extends Activity implements Async, AdapterView.OnItemClickListener
{
    protected boolean progress = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initCreate();
        startTask();
    }
    
    protected void startTask()
    {
        new Task(this).execute();
    }
    
    protected abstract void initCreate();
    
    protected <T extends View> T getMain()
    {
        return Utils.findView(this, R.id.main);
    }
    
    @Override
    public void preExecute()
    {
        View main = getMain();
        main.setVisibility(View.GONE);
    }
   
    @Override
    public void postExecute()
    {
        progress = false;
        findViewById(R.id.progress).setVisibility(View.GONE);
        View main = getMain();
        main.setVisibility(View.VISIBLE);
        initMain();
    }
    
    protected abstract void initMain();
}
