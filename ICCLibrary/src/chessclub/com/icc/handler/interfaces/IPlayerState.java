package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.PlayerStatePacket;

public interface IPlayerState extends IAbstractICCHandler {
    void playerState(PlayerStatePacket p);
}
