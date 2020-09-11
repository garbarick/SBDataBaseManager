package ru.net.serbis.dbmanager;

import android.content.pm.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.app.*;
import ru.net.serbis.dbmanager.task.*;
import ru.net.serbis.dbmanager.util.*;

public class Main extends AsyncActivity
{
    private List<App> appList = new ArrayList<App>();

    @Override
    protected void initCreate()
    {
        setContentView(R.layout.main);
    }
    
    @Override
    protected void initMain()
    {
        AppAdapter adapter = new AppAdapter(this, appList);
        ListView main = getMain();
        main.setAdapter(adapter);
        main.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id)
    {
        Utils.toDbList(this, (App) parent.getItemAtPosition(position));
    }

    @Override
    public void inBackground()
    {
        PackageManager manager = getPackageManager();
        for (ApplicationInfo info : manager.getInstalledApplications(PackageManager.GET_META_DATA))
        {
            App app = new App(info.packageName);
            if (app.hasDataBase())
            {
                app.setLabel(info.loadLabel(manager).toString());
                app.setIcon(info.loadIcon(manager));
                appList.add(app);
            }
        }
        Collections.sort(appList);
        Storage storage = new Storage(this);
        if (storage.hasDataBase())
        {
            appList.add(0, storage);
        }
    }
}
