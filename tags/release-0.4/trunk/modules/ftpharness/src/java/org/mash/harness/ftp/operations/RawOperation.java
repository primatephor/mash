package org.mash.harness.ftp.operations;

import org.apache.commons.net.ftp.FTPClient;
import org.mash.harness.RunResponse;
import org.mash.harness.ftp.FTPOperation;
import org.mash.harness.ftp.RawResponse;

/**
 *
 * @author teastlack
 * @since Sep 30, 2009 7:48:33 PM
 *
 */
public class RawOperation implements FTPOperation
{
    public RunResponse operate(FTPClient client, String ftpParams) throws Exception
    {
        RunResponse result = null;
        if (ftpParams != null)
        {
            String command = ftpParams;
            String args = null;
            if (command.indexOf(" ") > 0)
            {
                command = command.substring(0, command.indexOf(" "));
                args = ftpParams.substring(ftpParams.indexOf(" ") + 1);
            }
            client.sendCommand(command, args);
            result = new RawResponse(client.getReplyStrings());
        }
        return result;
    }
}
