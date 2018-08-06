package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessContext;
import org.mash.harness.HarnessError;
import org.mash.harness.RawResponse;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

import java.io.IOException;

/**
 * Perform generic FTP operations on a server, based on whatever parameters are passed via the ftp_params param.
 * <p>
 * Configurations:
 * <ul>
 * <li> 'url' is the url to submit to </li>
 * <li> 'user' is the user to connect with</li>
 * <li> 'password' is the user's password </li>
 * <li> 'passive_mode' is the FTP client local passive mode (this is on my default)</li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters are applied to the request, and the request is invoked.  There are special parameters:
 * <ul>
 * <li> 'ftp_params' parameters to apply to the operation </li>
 * </ul>
 *
 * @since Sep 17, 2009 10:20:40 AM
 */
@HarnessName(name = "ftp")
public class FTPRunHarness extends BaseHarness implements RunHarness {
    private static final Logger log = LogManager.getLogger(FTPRunHarness.class.getName());
    //configs
    private String url;
    private String user;
    private String password;
    private Boolean passiveMode = Boolean.TRUE;

    //params
    private String ftpParams;

    private RunResponse response;

    public void run(HarnessContext context) {
        FTPClient client = buildClient();
        if(url == null){
            log.error("Unable to connect, no url configured");
            this.getErrors().add(new HarnessError(this, "Connection", "No URL configured for FTP"));
        }
        else {
            log.info("Attempting connect to " + url);
            try {
                //5 minutes
                client.setConnectTimeout(5 * 60 * 1000);
                int port = 22;
                String urlWithoutPort = url;
                if (url.contains(":")) {
                    int portIdx = url.indexOf(":");
                    port = Integer.valueOf(url.substring(portIdx + 1));
                    urlWithoutPort = url.substring(0, portIdx);
                }
                client.connect(urlWithoutPort, port);
                int reply = client.getReplyCode();
                log.info("Connection status: " + reply);

                // After connection attempt, you should check the reply code to verify success.
                if (!FTPReply.isPositiveCompletion(reply)) {
                    client.disconnect();
                    log.error("FTP connection refused");
                    this.getErrors().add(new HarnessError(this, "Connection", "FTP connection refused"));
                }

                //default is TRUE (initialized above)
                if(passiveMode){
                    client.enterLocalPassiveMode();
                }
                else{
                    client.enterLocalActiveMode();
                }

                if (!hasErrors()) {
                    if (!client.login(user, password)) {
                        this.getErrors().add(new HarnessError(this, "Authentication", "Unable to login with user " + user));
                    } else {
                        response = runOperation(client);
                    }
                    if (response != null && log.isTraceEnabled()) {
                        log.trace("Response:" + response.getString());
                    }
                }
            } catch (Exception e) {
                log.error("Unexpected error ftping server", e);
                this.getErrors().add(new HarnessError(this.getClass().getName(),
                        "Unexpected error ftping server", e.getMessage()));
            } finally {

                try {
                    client.logout();
                } catch (IOException e) {
                    log.error("Unexpected error logging out", e);
                }

                if (client.isConnected()) {
                    try {
                        client.disconnect();
                    } catch (Exception ioe) {
                        log.error("Unexpected error closing connection to ftp server", ioe);
                    }
                }
            }
        }
    }

    protected RunResponse runOperation(FTPClient client) throws OperationException {
        RunResponse result = null;
        try {
            if (ftpParams != null) {
                String command = ftpParams;
                String args = null;
                if (command.indexOf(" ") > 0) {
                    command = command.substring(0, command.indexOf(" "));
                    args = ftpParams.substring(ftpParams.indexOf(" ") + 1);
                }
                client.sendCommand(command, args);
                result = new RawResponse(client.getReplyStrings());
            }
        } catch (Exception e) {
            throw new OperationException("Problem running command remotely", e);
        }
        return result;
    }

    protected FTPClient buildClient() {
        return new FTPClient();
    }

    public RunResponse getResponse() {
        return response;
    }

    @HarnessConfiguration(name = "url")
    public void setUrl(String url) {
        this.url = url;
    }

    @HarnessConfiguration(name = "user")
    public void setUser(String user) {
        this.user = user;
    }

    @HarnessConfiguration(name = "password")
    public void setPassword(String password) {
        this.password = password;
    }

    @HarnessConfiguration(name = "passive_mode")
    public void setPassiveMode(String passiveMode) {
        this.passiveMode= Boolean.valueOf(passiveMode);
    }

    @HarnessParameter(name = "ftp_params")
    public void setFtpParams(String ftpParams) {
        this.ftpParams = ftpParams;
    }

}
