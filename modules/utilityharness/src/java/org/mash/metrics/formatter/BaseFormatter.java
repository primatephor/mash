package org.mash.metrics.formatter;

import org.mash.metrics.LoggerLine;
import org.mash.metrics.Formatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @since Mar 18, 2011 12:34:15 PM
 */
public class BaseFormatter implements Formatter
{
    protected List<String> columnNames;
    private List<LoggerLine> lines;
    protected String separator = ",";

    public BaseFormatter()
    {
        this.columnNames = new ArrayList<String>();
        this.columnNames.add("Entity");
        this.columnNames.add("Start Time");
        this.columnNames.add("# of Calls");
        this.columnNames.add("Average");
        this.columnNames.add("Total");
        this.columnNames.add("% of CPU");
        this.columnNames.add("Max Time");
        this.columnNames.add("Min Time");
    }

    public void addLine(LoggerLine line)
    {
        if (lines == null)
        {
            lines = new ArrayList<LoggerLine>();
        }
        lines.add(line);
    }

    public String format()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append(formatColumnNames());
        buffer.append("\n");
        buffer.append(formatData());
        buffer.append("\n");
        return buffer.toString();
    }

    public List<LoggerLine> getLines()
    {
        if (lines != null)
        {
            Collections.sort(lines, new LineCompare());
        }
        return lines;
    }

    public String formatData()
    {
        StringBuilder buffer = new StringBuilder();
        Iterator<LoggerLine> lineIter = getLines().iterator();
        while (lineIter.hasNext())
        {
            LoggerLine line = lineIter.next();
            buffer.append(formatData(line));
            if (lineIter.hasNext())
            {
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }

    public String formatData(LoggerLine line)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append(line.getEntity()).append(separator);
        buffer.append(line.getStart()).append(separator);
        buffer.append(line.getCount()).append(separator);
        buffer.append(toMinutesAndSeconds(line.getAverage())).append(separator);
        buffer.append(toMinutesAndSeconds(line.getTotal())).append(separator);
        buffer.append(line.getPercent()).append(separator);
        buffer.append(toMinutesAndSeconds(line.getMax())).append(separator);
        buffer.append(toMinutesAndSeconds(line.getMin()));
        return buffer.toString();
    }

    public String formatColumnNames()
    {
        StringBuilder buffer = new StringBuilder();
        Iterator<String> columnIter = columnNames.iterator();
        while (columnIter.hasNext())
        {
            String columnName = columnIter.next();
            buffer.append(formatColumnNames(columnName));
            if (columnIter.hasNext())
            {
                buffer.append(separator);
            }
        }
        return buffer.toString();
    }

    public String formatColumnNames(String columnName)
    {
        return columnName;
    }

    protected String toMinutesAndSeconds(Long value)
    {
        String result = "0m 0s";
        if (value != null)
        {
            //for padding in case the value is very small
            String decimal = "000"+ String.valueOf(value);
            if (decimal.length() > 3)
            {
                decimal = decimal.substring(decimal.length() - 3);
            }
            Long minutes = TimeUnit.MILLISECONDS.toMinutes(value);
            Long seconds = TimeUnit.MILLISECONDS.toSeconds(value);
            result = minutes + "m " + (seconds - TimeUnit.MINUTES.toSeconds(minutes)) + "." + decimal + "s";
        }
        return result;
    }

    public int length()
    {
        int result = 0;
        if (getLines() != null)
        {
            result = getLines().size();
        }
        return result;
    }

    private class LineCompare implements Comparator<LoggerLine>
    {
        public int compare(LoggerLine o1,
                           LoggerLine o2)
        {
            return o2.getTotal().compareTo(o1.getTotal());
        }
    }
}
