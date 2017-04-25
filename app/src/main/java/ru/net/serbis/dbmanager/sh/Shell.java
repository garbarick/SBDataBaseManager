package ru.net.serbis.dbmanager.sh;

import java.io.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public class Shell
{
    public List<String> command(String... commands)
    {
        OutputStreamWriter output = null;
        BufferedReader input = null;
        BufferedReader error = null;
        try
        {
            Process process = Runtime.getRuntime().exec("su");
            output = new OutputStreamWriter(process.getOutputStream());
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            for (String command : commands)
            {
                Log.info(this, "command:" + command);
                output.write(command + "\n");
                output.flush();
            }

            output.write("exit\n");
            output.flush();
            process.waitFor();

            Log.info(this, error, "error");
            return getResult(input);
        }
        catch (Throwable e)
        {
            Log.info(this, "Error on command", e);
            return Collections.<String>emptyList();
        }
        finally
        {
            close(input);
            close(error);
            close(output);
        }
    }
    
    private void close(Closeable o)
    {
        try
        {
            if (o != null)
            {
                o.close();
            }
        }
        catch (Throwable ignored)
        {
        }
    }
    
    private List<String> getResult(BufferedReader input) throws Exception
    {
        List<String> result = new ArrayList<String>();
        String line;
        while (input.ready() && (line = input.readLine()) != null)
        {
            result.add(line);
        }
        return result;
    }
}
