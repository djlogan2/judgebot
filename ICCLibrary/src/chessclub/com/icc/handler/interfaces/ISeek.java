package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.Seek;
import chessclub.com.icc.l2.SeekRemoved;

public interface ISeek extends IAbstractICCHandler {
    void seek(Seek p);
    void seekRemoved(SeekRemoved p);
}
