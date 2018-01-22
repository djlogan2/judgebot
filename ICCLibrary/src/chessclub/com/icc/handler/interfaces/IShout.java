package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.Shout;

public interface IShout extends IAbstractICCHandler {
    void incomingShout(Shout p);
}
