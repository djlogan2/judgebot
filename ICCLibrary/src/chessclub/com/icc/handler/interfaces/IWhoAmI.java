package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.WhoAmI;

public interface IWhoAmI extends IAbstractICCHandler {
    /**
     * Called when a user successfully logs on.
     * @param p The {@link WhoAmI} data in sent packet
     */
    void whoAmI(WhoAmI p);
}
