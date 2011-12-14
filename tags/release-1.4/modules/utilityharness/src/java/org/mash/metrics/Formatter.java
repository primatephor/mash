package org.mash.metrics;


/**
 * Format all the lines gathered during running.
 *
 * Lines are added after they're pulled from the manager and added to the formatter to deal with.
 * Column names and the lines are formatted separately.
 *
 * @author: teastlack
 * @since: Mar 21, 2011
 */
public interface Formatter
{
    /**
     * Add a line to the formatter.
     * @param line to format
     */
    void addLine(LoggerLine line);

    /**
     * format everything and return the formatted text
     * @return formatted lines
     */
    String format();

    /**
     * Format just the logger lines
     * @return line formats
     */
    String formatData();

    /**
     * Format just the column names
     * @return return the column names
     */
    String formatColumnNames();

    /**
     * @return number of lines of output format
     */
    public int length();
}
