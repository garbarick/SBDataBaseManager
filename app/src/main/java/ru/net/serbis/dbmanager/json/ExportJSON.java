package ru.net.serbis.dbmanager.json;

import android.content.*;
import android.util.*;
import java.io.*;
import java.text.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.task.*;
import ru.net.serbis.dbmanager.util.*;

public class ExportJSON extends ProgressTool
{
    private String table;
    private List<String> header;
    private List<List<String>> rows;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private JsonWriter writer;

    public ExportJSON(Context context, String table, List<String> header, List<List<String>> rows)
    {
        super(context);
        this.table = table;
        this.header = header;
        this.rows = rows;
    }

    public void executeDialog()
    {
        super.executeDialog(R.string.exportJSON);
    }

    @Override
    public void execute() throws Exception
    {
        try
        {
            File appDir = Utils.getToolFolder();
            String name = table + "-" + format.format(new Date()) + ".json";
            File file = new File(appDir, name);

            writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            write();

            result = file.getAbsolutePath();
        }
        finally
        {
            Utils.close(writer);
        }
    }
    
    private void write() throws Exception
    {
        writer.beginArray();
        write(header);
        for(List<String> row : rows)
        {
            write(row);
        }
        writer.endArray();
    }

    private void write(List<String> row) throws Exception
    {
        writer.beginArray();
        Iterator<String> iter = row.iterator();
        if (iter.hasNext())
        {
            iter.next();
        }
        while(iter.hasNext())
        {
            writer.value(iter.next());
        }
        writer.endArray();
    }
}
