package chessclub.com.icc;

import chessclub.com.enums.ICCError;

/**
 * The class that is returned to a handler from the framework that contains ICC errors.
 * @author David Logan
 *
 */
public class ICCException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7068579526811199021L;

    private ICCError error;
    private Exception originalException = null;
    
    /**
     * ICCException(final {@link ICCError} pError).
     * @param pError    The type of error we are sending to the handlers.
     */
    public ICCException(final ICCError pError) {
        error = pError;
    }
    
    /**
     * ICCException(final {@link ICCError} pError, final Exception ex).
     * @param pError    The type of error we are sending to the handlers.
     * @param ex        The original exception we caught.
     */
    public ICCException(final ICCError pError, final Exception ex) {
        error = pError;
        originalException = ex;
    }
    
    /**
     * Returns the type of error this instance is reporting.
     * @return  The {@link  ICCError}
     */
    public ICCError getError() {
        return error;
    }
    
    /**
     * Returns the original exception that caused this error to be returned.
     * @return  The original exception instance
     */
    public Exception getOriginalException() {
        return originalException;
    }
    
    @Override
    public String getMessage() {
        if (originalException != null) {
            return originalException.getMessage();
        } else {
            return null;
        }
    }
}
