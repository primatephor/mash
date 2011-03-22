package org.mash.metrics;


/**
 * @author: teastlack
 * @since: Mar 21, 2011
 */
public interface Formatter
{
    void addLine(LoggerLine line);

    String format();

    String formatData();

    String formatColumnNames();

    public int length();
}
