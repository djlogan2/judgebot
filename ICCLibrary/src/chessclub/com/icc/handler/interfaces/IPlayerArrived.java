package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.PlayerArrived;

public interface IPlayerArrived extends IAbstractICCHandler {
    void playerArrived(PlayerArrived p);
    void playerLeft(String name);
}
