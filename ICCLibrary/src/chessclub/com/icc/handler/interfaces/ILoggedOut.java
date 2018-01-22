package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l1.LoggedOut;

public interface ILoggedOut extends IAbstractICCHandler {
    /**
     * Called when the user is logged out of ICC. It contains logout messages as well as any
     * reason for the logout.
     * @param l The packet.
     */
    void loggedOut(LoggedOut l);
}
