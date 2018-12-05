package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.config.Attachment;
import org.mash.config.Parameter;
import org.mash.file.FileLoader;
import org.mash.file.FileReaderException;
import org.mash.harness.*;
import org.mash.harness.rest.RestResponse;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessName;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Configurations:
 * <ul>
 * <li> 'clean' will create a new web conversation </li>
 * <li> 'url' is the url to submit to </li>
 * <li> 'type' is the type of web request ('POST' or 'GET')</li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters are applied to the request type, and the request is invoked.  There are special parameters:
 * <ul>
 * <li> 'body' will be the streamed input.  If present, the request won't add this as a part of the parameter list, but
 * will instead submit this as an input stream </li>
 * </ul>
 *
 * @author
 * @since Jul 5, 2009
 */
@HarnessName(name = "http")
public class HttpRunHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = LogManager.getLogger(HttpRunHarness.class.getName());
    public static final String CONTEXT_HEADER="header";
    private String url;
    protected String type;
    private String clean;
    private String username;
    private String password;
    protected HttpClient client;
    protected RunResponse response;

    public void run(HarnessContext context)
    {
        log.debug("Running Http Invocation");
        if (clean != null && Boolean.valueOf(clean))
        {
            WebConversationHolder.reset();
        }

        client = getClient(type, username, password);

        if (client != null)
        {
            try
            {
                Map<String, String> contents = buildContents();

                client.submit(url, contents, getParameters(CONTEXT_HEADER));
                if (log.isTraceEnabled())
                {
                    RunResponse response = getResponse();
                    if (response != null)
                    {
                        log.trace("Response:" + response.getString());
                    }
                }
            }
            catch (Exception e)
            {
                log.error("Unexpected error sending to " + url, e);
                this.getErrors().add(new HarnessError(this.getName(), "Unexpected error sending to " + url, e));
            }
        }
    }

    protected Map<String, String> buildContents()
    {
        if( getAttachments() != null && getAttachments().size() > 0 )
        {
            return buildMultipartContents();
        }
        else
        {
            Map<String, String> params = new HashMap<String, String>();
            //only get parameters with no context
            for (Parameter parameter : getParameters(null))
            {
                params.put(parameter.getName(), parameter.getValue());
            }
            return params;
        }
    }

    protected Map<String, String> buildMultipartContents()
    {
        Map<String,String> result = new HashMap<String,String>();

        File basePath = null;
        if( this.getDefinition() != null && this.getDefinition().getScriptDefinition() != null )
        {
            basePath = this.getDefinition().getScriptDefinition().getPath();
        }
        log.debug( "basePath=" + basePath.getAbsolutePath());

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(Charset.forName("UTF-8"));

        FileLoader fileLoader = new FileLoader();
        int fileCount = 1;

        for( Attachment attachment : getAttachments())
        {
            String filename = attachment.getValue();

            ContentType contentType = ContentType.APPLICATION_OCTET_STREAM;
            if( attachment.getContentType() != null && ! attachment.getContentType().isEmpty())
            {
                contentType = ContentType.getByMimeType( attachment.getContentType() ) != null ?
                        ContentType.getByMimeType( attachment.getContentType() ) :
                        ContentType.create( attachment.getContentType() );
            }
            log.debug( "Using content type " + contentType + " for attachment " + filename );

            try
            {
                File file = fileLoader.findFile(filename, basePath);
                if( file.canRead())
                {
                    log.debug( "Adding attachment " + file.getAbsolutePath());
                    builder.addPart("attachment" + (fileCount++), new FileBody(file, contentType));
                }
                else
                {
                    log.error( "Ignoring unreadable attachment " + filename);
                }
            }
            catch( FileReaderException fre )
            {
                log.error( "Cannot read attachment " + filename, fre );
            }

        }

        for( Parameter param: super.getParameters(null))
        {
            log.debug( "Adding text parameter " + param.getName() + " with value " + param.getValue());
            builder.addTextBody(param.getName(), param.getValue());
        }

        HttpEntity httpEntity = builder.build();
        String contentType = httpEntity.getContentType().getValue();
        log.debug( "Content-Type is " + httpEntity.getContentType().toString());
        if( contentType != null && ! contentType.isEmpty())
        {
            Parameter param = new Parameter( "Content-Type", contentType);
            param.setContext(CONTEXT_HEADER);
            getParameters().add(param);
        }

        try
        {
            String content = null;

            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            httpEntity.writeTo(outstream);
            outstream.flush();
            content = outstream.toString();

            log.trace( "Body content:\n" + content);

            result.put( StandardRequestFactory.BODY, content );
        }
        catch( IOException ioe )
        {
            log.error( "Error while building content", ioe );
        }

        return result;
    }

    protected Page getPage()
    {
        Page result = null;
        if (client != null)
        {
            result = client.getWebResponse();
        }
        else
        {
            log.warn("Client is null!");
        }
        return result;
    }

    protected Set<Cookie> getCookies()
    {
        Set<Cookie> cookies = null;
        if (client != null)
        {
            WebClient webClient = client.getClient();
            if (null != webClient)
            {
                CookieManager cookieManager = webClient.getCookieManager();
                if (null != cookieManager)
                {
                    cookies = cookieManager.getCookies();
                }
            }
        }
        return cookies;
    }

    public RunResponse getResponse()
    {
        Page result = getPage();
        if (result != null)
        {
            if (result instanceof XmlPage)
            {
                log.debug("XML result, using rest response");
                response = new RestResponse(result);
            }
            else
            {
                log.debug("HTML result, using http response");
                response = new HttpResponse(result, getCookies());
            }
        }
        else
        {
            log.warn("No response!");
        }
        return response;
    }

    /**
     * Override this to create your on harness to extract the appropriate client type
     *
     * @param clientType (POST, PUT, DELETE, GET)
     * @return HttpClient
     */
    protected HttpClient getClient(String clientType, String username, String password)
    {
        return new HttpClient(new StandardRequestFactory(), clientType, username, password);
    }

    @HarnessConfiguration(name = "url")
    public void setUrl(String url)
    {
        this.url = url;
    }

    @HarnessConfiguration(name = "type")
    public void setType(String type)
    {
        this.type = type;
    }

    @HarnessConfiguration(name = "clean")
    public void setClean(String clean)
    {
        this.clean = clean;
    }

    @HarnessConfiguration(name = "username")
    public void setUsername(String username)
    {
        this.username = username;
    }

    @HarnessConfiguration(name = "password")
    public void setPassword(String password)
    {
        this.password = password;
    }
}
