package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.PlayerArrived;

public interface INotifyArrivedLeft extends IAbstractICCHandler {
    void notifyArrived(PlayerArrived p);
    void notifyLeft(String name);
}
