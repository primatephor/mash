package org.mash.harness;

/**
 * The list run response assumes there are lists of data to verify, and you use the element number to determine
 * which RunResponse to validate.
 *
 * A common example is validating some database data.  You may run 'select * from table' and get back many rows, so you
 * validate each row by setting the element number here.  Your result set should know how to traverse this array. 
 *
 * @author
 * @since Jun 9, 2010 5:49:51 PM
 */
public interface ListRunResponse extends RunResponse
{
    /**
     * Specify which element in the list response to verify
     *
     * @param elementNumber to validate
     */
    public void setElementNumber(int elementNumber);

    public int getSize();
}
