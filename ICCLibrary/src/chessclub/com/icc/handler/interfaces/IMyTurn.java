package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.GameNumber;

public interface IMyTurn extends IAbstractICCHandler {
    void myTurn(GameNumber p);
}
