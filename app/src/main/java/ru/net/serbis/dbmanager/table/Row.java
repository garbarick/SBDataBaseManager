package ru.net.serbis.dbmanager.table;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;

public class Row extends LinearLayout
{
    private List<String> cells;
    private int column = 1;
    
    private int color = R.color.row;
    private int colorCell1 = R.color.rowCell1;
    private int colorCell2 = R.color.rowCell2;
    private int colorCell3 = R.color.rowCell3;
    
    private float weightCell2;
    private float weightCell3;
    
    public Row(Context context)
    {
        super(context);
        init(context);
    }
    
    public Row(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public Row(Context context, AttributeSet attrs, int style)
    {
        super(context, attrs, style);
        init(context);
    }

    public void setCells(List<String> cells)
    {
        this.cells = cells;
    }
    
    public void setColumn(int column)
    {
        this.column = column;
    }
    
    private void init(Context context)
    {
        inflate(context, R.layout.content_row, this);
        weightCell2 = getWeight(R.id.cell2);
        weightCell3 = getWeight(R.id.cell3);
        setColors();
    }
    
    private void setColors()
    {
        setBackgroundResource(color);
        setColor(R.id.cell1, colorCell1);
        setColor(R.id.cell2, colorCell2);
        setColor(R.id.cell3, colorCell3);
    }
    
    public void setColors(int color, int colorCell1, int colorCell2, int colorCell3)
    {
        this.color = color;
        this.colorCell1 = colorCell1;
        this.colorCell2 = colorCell2;
        this.colorCell3 = colorCell3;
        setColors();
    }
    
    protected <T extends View> T findView(int id)
    {
        return (T) findViewById(id);
    }
    
    public void update()
    {
        setText(0, R.id.cell1);
        boolean first = setText(column, R.id.cell2);
        boolean second = setText(column + 1, R.id.cell3);
        if (first && second)
        {
            setWeight(R.id.cell2, weightCell2);
            setWeight(R.id.cell3, weightCell3);
        }
        else if (first && !second)
        {
            setWeight(R.id.cell2, 0);
            setWeight(R.id.cell3, 100);
        }
    }

    private boolean setText(int position, int id)
    {
        TextView text = findView(id);
        if (cells.size() > position)
        {
            text.setText(cells.get(position));
            return true;
        }
        else
        {
            text.setText(null);
            return false;
        }
    }
    
    private float getWeight(int id)
    {
        TextView text = findView(id);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) text.getLayoutParams();
        return params.weight;
    }
    
    private void setWeight(int id, float weight)
    {
        TextView text = findView(id);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) text.getLayoutParams();
        params.weight = weight;
        text.setLayoutParams(params);
    }
    
    private void setColor(int id, int color)
    {
        TextView text = findView(id);
        text.setBackgroundResource(color);
    }
}
