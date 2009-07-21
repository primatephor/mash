package org.mash.harness.http;

/**
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public enum Method
{
    POST(new ClientCreator()
    {
        public HttpClient createClient()
        {
            return new PostClient();
        }
    }),
    GET(new ClientCreator()
    {
        public HttpClient createClient()
        {
            return new GetClient();
        }
    }),
    CUSTOM(new ClientCreator()
    {
        public HttpClient createClient()
        {
            return customClient;
        }
    });

    private ClientCreator creator;
    private static HttpClient customClient;

    Method(ClientCreator creator)
    {
        this.creator = creator;
    }

    public HttpClient getClient()
    {
        return creator.createClient();
    }

    private interface ClientCreator
    {
        HttpClient createClient();
    }

    public static void setCustomClient(HttpClient customClient)
    {
        Method.customClient = customClient;
    }
}
