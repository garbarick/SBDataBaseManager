package ru.net.serbis.dbmanager.json;

import android.content.*;
import android.database.sqlite.*;
import android.util.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.app.db.*;
import ru.net.serbis.dbmanager.dialog.*;
import ru.net.serbis.dbmanager.query.*;
import ru.net.serbis.dbmanager.task.*;
import ru.net.serbis.dbmanager.util.*;

public class ImportJSON extends ProgressTool
{
    private String table;
    private List<String> columns;
    private DB db;
    
    private File file;
    private JsonReader reader;
    private boolean readHeader;
    private List<String> header;
    private Map<String, Integer> names = new LinkedHashMap<String, Integer>();

    public ImportJSON(Context context, DB db, String table, List<String> columns)
    {
        super(context);
        this.db = db;
        this.table = table;
        this.columns = columns;
    }

    public void executeDialog()
    {
        File appDir = Utils.getToolFolder();

        new FileDialog(context, appDir, ".json")
        {
            @Override
            protected void onSelect(File file)
            {
                setFile(file);
                readHeader = true;
                if (progress())
                {
                    readHeader = false;
                    new MapingDialog(context, table, header, columns)
                    {
                        protected void ready(List<String> values)
                        {
                            if (initNameMap(values))
                            {
                                baseExecuteDialog();
                            }
                        }
                    };
                }
                else
                {
                    new AlertMessage(context, result);
                }
            }
        };
    }

    private void setFile(File file)
    {
        this.file = file;
    }
    
    private boolean initNameMap(List<String> values)
    {
        boolean result = false;
        int i = 0;
        for(String value : values)
        {
            if (Utils.isNotEmpty(value))
            {
                result = true;
                names.put(value, i);
            }
            i++;
        }
        return result;
    }
    
    private void baseExecuteDialog()
    {
        super.executeDialog(R.string.importJSON);
    }

    @Override
    public void execute() throws Exception
    {
        if (readHeader)
        {
            readData(null);
        }
        else
        {
            db.run(
                new DbCall<Void>()
                {
                    public Void call(SQLiteDatabase db)
                    {
                        db.beginTransaction();
                        readData(db);
                        db.setTransactionSuccessful();
                        db.endTransaction();
                        return null;
                    }
                }, false
            );
        }
    }
    
    private void readData(SQLiteDatabase db)
    {
        try
        {
            reader = new JsonReader(new FileReader(file));
            read(db);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
        finally
        {
            Utils.close(reader);
        }
    }

    private void read(SQLiteDatabase db) throws Exception
    {
        reader.beginArray();
        header = readRow();
        if (readHeader)
        {
            return;
        }
        while(reader.hasNext())
        {
            List<String> row = readRow();
            insert(db, row);
        }
        reader.endArray();
    }

    private List<String> readRow() throws Exception
    {
        List<String> result = new ArrayList<String>();
        reader.beginArray();
        while(reader.hasNext())
        {
            result.add(reader.nextString());
        }
        reader.endArray();
        return result;
    }
    
    private void insert(SQLiteDatabase db, List<String> row)
    {
        List<String> columns = new ArrayList<String>(names.keySet());
        List<String> values = new ArrayList<String>();
        for(int i : names.values())
        {
            values.add(row.get(i));
        }
        Query query = new QueryGenerator().generateInsert(table, columns, values);
        db.execSQL(query.getQuery(), query.getBindArray());
    }
}
