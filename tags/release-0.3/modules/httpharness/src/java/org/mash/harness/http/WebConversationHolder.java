package org.mash.harness.http;

import com.meterware.httpunit.WebConversation;

public class WebConversationHolder
{
    private static WebConversation ourInstance = new WebConversation();

    public static WebConversation getInstance()
    {
        return ourInstance;
    }

    private WebConversationHolder()
    {
    }

    public static synchronized void reset()
    {
        ourInstance = new WebConversation();
    }
}
