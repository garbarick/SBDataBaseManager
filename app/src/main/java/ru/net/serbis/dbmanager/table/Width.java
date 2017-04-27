package ru.net.serbis.dbmanager.table;

import java.util.*;

public class Width
{
    public interface Listener
    {
        void update();
    }
    
    private int count;
    private int[] widths;
    private int sum;
    private List<Listener> listeners = new ArrayList<Listener>();
    
    public Width(int count)
    {
        this.count = count;
        widths = getNewWidths();
    }
    
    public void addListener(Listener listener)
    {
        listeners.add(listener);
    }
    
    public boolean setWidth(int i, int width)
    {
        int old = widths[i];
        if (width > old)
        {
            widths[i] = width;
            sum = sum - old + width;
            return true;
        }
        return false;
    }
    
    public synchronized void setWidths(int[] widths)
    {
        boolean update = false;
        for (int i = 0; i < count; i ++)
        {
            update = setWidth(i, widths[i]) || update;
        }
        if (update)
        {
            update();
        }
    }
    
    private void update()
    {
        for (Listener listener : listeners)
        {
            listener.update();
        }
    }
    
    public int getSum()
    {
        return sum;
    }
    
    public int getWidth(int i)
    {
        return widths[i];
    }
    
    public int[] getNewWidths()
    {
        int[] widths = new int[count];
        Arrays.fill(widths, 0);
        return widths;
    }
}
