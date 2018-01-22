package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.Command;

public interface ICommand extends IAbstractICCHandler {
    void command(Command p);
}
