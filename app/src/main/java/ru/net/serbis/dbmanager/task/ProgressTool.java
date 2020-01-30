package ru.net.serbis.dbmanager.task;

import android.content.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.dialog.*;

public abstract class ProgressTool
{
    protected Context context;
    protected String result;

    public ProgressTool(Context context)
    {
        this.context = context;
        result = context.getResources().getString(R.string.successfully);
    }

    public void executeDialog(int title)
    {
        new ProgressDialog(context, title)
        {
            @Override
            public void inBackground()
            {
                progress();
            }

            @Override
            protected String getResult()
            {
                return result;
            }

            @Override
            protected void onPositive()
            {
                onFinish();
            }
        };
    }
    
    protected boolean progress()
    {
        try
        {
            execute();
            return true;
        }
        catch(Exception e)
        {
            onError(e);
            return false;
        }
    }

    public abstract void execute() throws Exception;

    protected void onError(Exception e)
    {
        Log.info(this, e);
        result = e.getMessage();
    }

    protected void onFinish()
    {
    }
}
