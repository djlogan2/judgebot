package chessclub.com.enums;

/**
 * This is the enumeration that details the type of error the framework is sending to an
 * ICC handler.
 * @author David Logan
 *
 */
public enum ICCError {
    /**
     * An error with the hostname.
     */
    HOSTNAME,
    /**
     * An error with the port.
     */
    PORT,
    /**
     * We don't know why we got this error, but we got it.
     */
    UNKNOWN
}
