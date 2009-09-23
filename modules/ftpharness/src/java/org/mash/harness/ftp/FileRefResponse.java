package org.mash.harness.ftp;

import org.mash.harness.OGNLResponse;

import java.io.File;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 3:43:00 PM
 *
 */
public class FileRefResponse extends OGNLResponse
{
    public FileRefResponse(File file)
    {
        super(file);
    }
}
