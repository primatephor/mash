package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.WebClient;

public class WebConversationHolder
{
    private static WebClient ourInstance = new WebClient();

    public static WebClient getInstance()
    {
        return ourInstance;
    }

    private WebConversationHolder()
    {
    }

    public static synchronized void reset()
    {
        ourInstance = new WebClient();
    }
}
