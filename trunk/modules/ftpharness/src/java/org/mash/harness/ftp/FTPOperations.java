package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;
import org.mash.harness.ftp.operations.ListFTPOperation;

/**
 *
 * @author teastlack
 * @since Sep 17, 2009 6:20:04 PM
 *
 */
public enum FTPOperations
{
    PUT(new NoOp()),
    GET(new NoOp()),
    LS(new ListFTPOperation());

    private FTPOperation operation;

    FTPOperations(FTPOperation operation)
    {
        this.operation = operation;
    }

    public FTPOperation getOperation()
    {
        return operation;
    }

    private static class NoOp implements FTPOperation
    {
        private static final Logger log = Logger.getLogger("FTPNoOp");

        public RunResponse operate(FTPClient client, String ftpParams) throws Exception
        {
            log.warn("Calling an invalid operation!");
            return null;
        }
    }
}
