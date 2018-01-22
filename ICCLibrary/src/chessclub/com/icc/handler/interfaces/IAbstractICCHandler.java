package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l2.Error;

/**
 *
 * @author David Logan
 *  <p>
 *         The AbstractICCHandler interface contains all of the methods for all of the packets handled by the ICC runtime thread. Every incoming packet is parsed and put into a packet, and then the subsequent handler method is invoked.
 *         If you want to handle ICC packets, implement this interface, or extend from the DefaultICCHandler class.
 * 
 */
public interface IAbstractICCHandler {

    /**
     * This method gets called by the framework after the ICC instance connections have been established.
     * At this point, the handler is able to make all necessary ICC setup calls.
     */
    void initHandler();

    /**
     * This method gets called by the framework if we have any errors to report.
     * @param   ex   The {@link ICCException}. 
     */
    void iccException(ICCException ex);
    
    /**
     * This method is called by the framework to set the ICC instance this handler is tied to.
     * @param icc   The {@link ICCInstance} this handler is tied to.
     */
    void setInstance(ICCInstance icc);

    void connectionClosed();

    void badCommand1(BadCommand p);

    void badCommand2(L1ErrorOnly p);
    
    void Error(Error error);

    void shuttingDown();
}
