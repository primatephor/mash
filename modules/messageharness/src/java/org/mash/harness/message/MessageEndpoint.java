package org.mash.harness.message;

/**
 * A message endpoint does the work of reading and sending messages.  These may be emails, queues, IM, whatever.
 *
 * @author
 * @since Feb 5, 2010 9:07:06 AM
 *
 */
public interface MessageEndpoint
{
    /**
     * Read a message from the endpoint.  Generally it's up to the implementation to figure out how this is done,
     * but for now we're talking next on the queue.
     *
     * @return message
     * @throws SendException when unable to read
     */
    Message read() throws SendException;

    /**
     * Send a message to the endpoint
     *
     * @param message to send
     * @throws SendException when unable to send
     */
    void send(Message message) throws SendException;
}
