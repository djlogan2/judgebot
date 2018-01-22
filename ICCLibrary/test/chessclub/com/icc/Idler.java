package chessclub.com.icc;

import chessclub.com.icc.handler.interfaces.IAbstractICCHandler;
//CHECKSTYLE:OFF
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;

public class Idler extends ICCInstance  implements IAbstractICCHandler {

    public void initHandler() {
    }

    public void iccException(ICCException ex) {
    }

    public void setInstance(ICCInstance icc) {
    }

    public void connectionClosed() {
    }

    public void shuttingDown() {
    }

    public void Error(chessclub.com.icc.l2.Error error) {
    }

    public void badCommand1(BadCommand p) {
    }

    public void badCommand2(L1ErrorOnly p) {
    }

}
