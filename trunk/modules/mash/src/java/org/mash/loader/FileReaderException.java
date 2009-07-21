package org.mash.loader;

/**
 * User: teastlack Date: Jul 1, 2009 Time: 11:00:54 AM
 */
public class FileReaderException extends Exception {
    public FileReaderException(String message, Exception cause) {
        super(message, cause);
    }

    public FileReaderException(String message) {
        super(message);
    }
}
