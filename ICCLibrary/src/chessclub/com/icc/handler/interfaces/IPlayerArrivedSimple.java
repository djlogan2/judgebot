package chessclub.com.icc.handler.interfaces;

public interface IPlayerArrivedSimple extends IAbstractICCHandler {
    /**
     * Called when a player arrives to ICC.
     * @param name  The name of the userid of the person arriving
     */
    void playerArrivedSimple(String name);
    void playerLeft(String name);
}
