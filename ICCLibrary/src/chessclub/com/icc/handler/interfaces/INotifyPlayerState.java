package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.PlayerStatePacket;

public interface INotifyPlayerState extends IAbstractICCHandler {
    void notifyPlayerState(PlayerStatePacket p);
}
