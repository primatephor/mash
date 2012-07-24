package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.WebClient;

public class WebConversationHolder
{
    private static final ThreadLocal<WebClient> ourInstance = new ThreadLocal<WebClient>()
    {
        @Override
        protected WebClient initialValue()
        {
            return new WebClient();
        }
    };

    public static WebClient getInstance()
    {
        return ourInstance.get();
    }

    private WebConversationHolder()
    {
    }

    public static void reset()
    {
        ourInstance.remove();
    }
}
