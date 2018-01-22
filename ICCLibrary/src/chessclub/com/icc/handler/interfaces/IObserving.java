package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.GameNumber;
import chessclub.com.icc.l2.GameStarted;

public interface IObserving extends IAbstractICCHandler {
    void startedObserving(GameStarted p);
    void stopObserving(GameNumber p);
}
