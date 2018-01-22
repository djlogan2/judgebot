package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.LoginFailed;

public interface ILoginFailed extends IAbstractICCHandler {
    /**
     * Called when a users login fails.
     * @param p The {@link LoginFailed} packet data with the failed login information.
     */
    void loginFailed(LoginFailed p);


}
