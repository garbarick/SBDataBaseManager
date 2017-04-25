package ru.net.serbis.dbmanager.task;

import android.os.*;
import android.view.*;

public class Task extends AsyncTask<Object, Object, Object>
{
    private Async async;

    public Task(Async async)
    {
        this.async = async;
    }
    
    @Override
    protected void onPreExecute()
    {
        async.preExecute();
    }

    @Override
    protected void onPostExecute(Object o)
    {
        async.postExecute();
    }

    @Override
    protected Object doInBackground(Object... o)
    {
        async.inBackground();
        return null;
    }
}
