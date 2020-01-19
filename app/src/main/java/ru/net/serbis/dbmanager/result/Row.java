package ru.net.serbis.dbmanager.result;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.dbmanager.*;
import ru.net.serbis.dbmanager.util.*;

public class Row extends LinearLayout
{
    public static final int MARGIN = 2;
    public static final int PADDING = 5;

    private static final int SHIFT = 100;
    
    private List<String> cells;
    private Width width;

    private int color = R.color.row;
    private int colorFirstCell = R.color.rowFirstCell;
    private int colorOtherCell = R.color.rowOtherCell;

    private boolean columnsInit = false;

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

    public void setWidth(Width width)
    {
        this.width = width;
    }

    private void init(Context context)
    {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setColors(int color, int colorFirstCell, int colorOtherCell)
    {
        this.color = color;
        this.colorFirstCell = colorFirstCell;
        this.colorOtherCell = colorOtherCell;
    }

    public void update()
    {
        if (!columnsInit)
        {
            createContent();
            columnsInit = true;
        }
        updateWidth(cells);
        setWidths();
    }
    
    public void updateWidth(List<String> cells)
    {
        int i = 0;
        int[] widths = width.getNewWidths();
        for (String cell : cells)
        {
            widths[i] = getTextWidth(cell) + MARGIN + PADDING + PADDING;
            i ++;
        }
        width.setWidths(widths);
    }
    
    private void setWidths()
    {
        int i = 0;
        for (String cell : cells)
        {
            TextView text = Utils.findView(this, i + SHIFT);
            text.setText(cell);
            setWidth(text, width.getWidth(i), MARGIN, PADDING);
            i ++;
        }
    }

    private void createContent()
    {
        setBackgroundResource(color);
        for (int i = 0; i < cells.size(); i ++)
        {
            TextView text = new TextView(getContext());
            text.setBackgroundResource(i == 0 ? colorFirstCell : colorOtherCell);
            text.setId(i + SHIFT);
            addView(text);
        }
    }

    public void setWidth(View view, int width)
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }

    public void setWidth(View view, int width, int margin, int padding)
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.setMargins(0, 0, margin, 0);
        view.setPadding(padding, 0, padding, 0);
        view.setLayoutParams(params);
    }

    private int getTextWidth(String text)
    {
        TextView view = new TextView(getContext());
        view.setText(text);
        view.measure(0, 0);
        return view.getMeasuredWidth();
    }
}
