package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l1.Seeking;
import chessclub.com.icc.l1.Unseeking;

public interface IMySeek extends IAbstractICCHandler {
    void mySeek(Seeking p);
    void mySeekRemoved(Unseeking p);
}
