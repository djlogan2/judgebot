package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;

public interface IGameStartedResult extends IAbstractICCHandler {
    void gameStarted(GameStarted p);
    void gameResult(GameResultPacket p);
}
