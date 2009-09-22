package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.mash.harness.RunResponse;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 11:57:03 AM
 *
 */
public interface FTPOperation
{
    RunResponse operate(FTPClient client, String ftpParams) throws Exception;
}
