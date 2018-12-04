package org.mash.harness.fileupload;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.file.FileLoader;
import org.mash.file.FileReaderException;
import org.mash.harness.*;
import org.mash.harness.http.HttpRunHarness;
import org.mash.harness.http.StandardRequestFactory;
import org.mash.loader.HarnessName;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Harness that enables uploading of files using the HTTP multipart/form-data.
 *
 * Because the gargoylesoftware htmlunit libraries don't seem to support file
 * upload, we build the mime multipart content manually here using the Apache
 * HTTP libraries.
 *
 * @author ctanno
 * @since Dec 4, 2018
 */
@HarnessName(name = "fileUpload")
public class FileUploadHarness extends HttpRunHarness implements RunHarness
{
    private static final Logger LOG = LogManager.getLogger(FileUploadHarness.class.getName());
    public static final String CONTEXT_HEADER = "header";

    private String contentType;

    @Override
    protected Map<String, String> getContents()
    {
        Map<String,String> result = new HashMap<String,String>();

        File basePath = null;
        if( this.getDefinition() != null && this.getDefinition().getScriptDefinition() != null )
        {
            basePath = this.getDefinition().getScriptDefinition().getPath();
        }
        LOG.debug( "basePath=" + basePath.getAbsolutePath());

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(Charset.forName("UTF-8"));

        FileLoader fileLoader = new FileLoader();
        int fileCount = 1;

        for( org.mash.config.File fileConfig : getFiles())
        {
            String filename = fileConfig.getValue();

            ContentType contentType = ContentType.APPLICATION_OCTET_STREAM;
            if( fileConfig.getContentType() != null && ! fileConfig.getContentType().isEmpty())
            {
                contentType = ContentType.getByMimeType( fileConfig.getContentType() ) != null ?
                        ContentType.getByMimeType( fileConfig.getContentType() ) :
                        ContentType.create( fileConfig.getContentType() );
            }
            LOG.debug( "Using content type " + contentType + " for files " + filename );

            try
            {
                File file = fileLoader.findFile(filename, basePath);
                if( file.canRead())
                {
                    LOG.debug( "Adding upload files " + file.getAbsolutePath());
                    builder.addPart("files" + (fileCount++), new FileBody(file, contentType));
                }
                else
                {
                    LOG.error( "Ignoring unreadable files " + filename);
                }
            }
            catch( FileReaderException fre )
            {
                LOG.error( "Cannot read files " + filename, fre );
            }

        }

        for( Parameter param: super.getParameters(null))
        {
            LOG.debug( "Adding text parameter " + param.getName() + " with value " + param.getValue());
            builder.addTextBody(param.getName(), param.getValue());
        }

        HttpEntity httpEntity = builder.build();
        this.contentType = httpEntity.getContentType().getValue();
        LOG.debug( "Content-Type is " + httpEntity.getContentType().toString());

        try
        {
            String content = null;

            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            httpEntity.writeTo(outstream);
            outstream.flush();
            content = outstream.toString();

            LOG.trace( "Body content:\n" + content);

            result.put( StandardRequestFactory.BODY, content );
        }
        catch( IOException ioe )
        {
            LOG.error( "Error while building content", ioe );
        }

        return result;
    }

    /* This manually adds a Content-Type parameter.  We need to do this since
       the content type is dynamically generated above as it includes the Mime
       multipart boundary, for example:

       multipart/form-data; boundary=w06fYDKESVxaQtAQSsddtFOkSkr6toaVahb; charset=UTF-8
     */
    @Override
    public List<Parameter> getParameters()
    {
        List<Parameter> results = super.getParameters();

        if( contentType != null && ! contentType.isEmpty())
        {
            Parameter param = new Parameter( "Content-Type", contentType);
            param.setContext(CONTEXT_HEADER);
            results.add(param);
        }

        return results;
    }

}
