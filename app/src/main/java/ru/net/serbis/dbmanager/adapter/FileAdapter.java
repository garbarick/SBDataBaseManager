package ru.net.serbis.dbmanager.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public class FileAdapter extends Adapter<File> implements AdapterView.OnItemClickListener
{
    private File dir;
    private String ext;
    private int selected;
    
    public FileAdapter(Context context, File dir, String ext)
    {
        super(context, 0, R.layout.file);
        this.ext = ext;
        initFiles(dir);
    }
    
    private void initFiles(File dir)
    {
        clear();
        selected = -1;
        this.dir = dir;
        File parent = dir.getParentFile();
        if (parent != null)
        {
            add(parent);
        }
        addDirs(dir);
        addFiles(dir);
    }

    private void addDirs(File dir)
    {
        final Set<File> files = new TreeSet<File>();
        dir.listFiles(
            new FileFilter()
            {
                public boolean accept(File file)
                {
                    if (file.isDirectory())
                    {
                        files.add(file);
                    }
                    return false;
                }
            }
        );
        addAll(files);
    }

    private void addFiles(File dir)
    {
        final Set<File> files = new TreeSet<File>();
        dir.listFiles(
            new FileFilter()
            {
                public boolean accept(File file)
                {
                    if (file.isFile() && file.getName().endsWith(ext))
                    {
                        files.add(file);
                    }
                    return false;
                }
            }
        );
        addAll(files);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        view = super.getView(position, view, parent);
        TextView text = Utils.findView(view, R.id.file);
        text.setText(getName(position));
        text.setBackgroundResource(position == selected ? R.color.darkGray: 0);
        return view;
    }
    
    private String getName(int position)
    {
        File file = getItem(position);
        if (position == 0)
        {
            return new File(dir, "..").getAbsolutePath();
        }
        else if (file.isDirectory())
        {
            return file.getName() + "/";
        }
        return file.getName();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        File file = getItem(position);
        if (file.isDirectory())
        {
            initFiles(file);
        }
        else
        {
            selected = position;
        }
        notifyDataSetChanged();
    }
    
    public int getSelected()
    {
        return selected;
    }
}
