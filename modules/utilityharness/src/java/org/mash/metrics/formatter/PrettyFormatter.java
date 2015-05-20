package org.mash.metrics.formatter;

import org.mash.metrics.LoggerLine;
import org.mash.tool.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @since Mar 18, 2011 1:19:46 PM
 */
public class PrettyFormatter extends BaseFormatter
{
    private Map<String, Integer> columnSizes;

    public PrettyFormatter()
    {
        separator = ", ";
    }

    @Override
    public void addLine(LoggerLine line)
    {
        super.addLine(line);
        adjustSize("Entity", line.getEntity());
        adjustSize("Start Time", line.getStart());
        adjustSize("# of Calls", line.getCount());
        adjustSize("Average", toMinutesAndSeconds(line.getAverage()));
        adjustSize("Total", toMinutesAndSeconds(line.getTotal()));
        adjustSize("% of CPU", line.getPercent());
        adjustSize("Max Time", toMinutesAndSeconds(line.getMax()));
        adjustSize("Min Time", toMinutesAndSeconds(line.getMin()));
    }

    @Override
    public String formatColumnNames(String columnName)
    {
        return pad(columnName, columnName);
    }

    @Override
    public String formatData(LoggerLine line)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append(pad("Entity", line.getEntity())).append(separator);
        buffer.append(pad("Start Time", line.getStart())).append(separator);
        buffer.append(pad("# of Calls", line.getCount())).append(separator);
        buffer.append(pad("Average", toMinutesAndSeconds(line.getAverage()))).append(separator);
        buffer.append(pad("Total", toMinutesAndSeconds(line.getTotal()))).append(separator);
        buffer.append(pad("% of CPU", line.getPercent())).append(separator);
        buffer.append(pad("Max Time", toMinutesAndSeconds(line.getMax()))).append(separator);
        buffer.append(pad("Min Time", toMinutesAndSeconds(line.getMin())));
        return buffer.toString();
    }

    public Map<String, Integer> getColumnSizes()
    {
        if (columnSizes == null)
        {
            columnSizes = new HashMap<String, Integer>();
            for (String columnName : columnNames)
            {
                columnSizes.put(columnName, columnName.length());
            }
        }
        return columnSizes;
    }

    protected String pad(String columnName,
                         Object value)
    {
        String result = "";
        if (getColumnSizes().get(columnName) != null && value != null)
        {
            Integer currentSize = getColumnSizes().get(columnName);
            String sValue = String.valueOf(value);
            result = StringUtil.rightPad(sValue, currentSize, ' ');
        }
        return result;
    }

    protected void adjustSize(String columnName,
                              Object value)
    {
        if (getColumnSizes().get(columnName) != null && value != null)
        {
            Integer currentSize = getColumnSizes().get(columnName);
            Integer max = max(currentSize, String.valueOf(value).length());
            getColumnSizes().put(columnName, max);
        }
    }

    private Integer max(Integer first,
                        Integer second)
    {
        Integer result = first;
        if (second != null && result != null && second > result)
        {
            result = second;
        }
        return result;
    }
}
