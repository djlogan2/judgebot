package chessclub.com.icc;

import chessclub.com.icc.handler.interfaces.IAbstractICCHandler;
import chessclub.com.icc.l1.Level1Packet;

// CHECKSTYLE:OFF
/**
 * An extended interface that allows the unit tests to test things without having to
 * make them available to the general framework.
 * @author David Logan
 *
 */
public interface UnitTestHandler extends IAbstractICCHandler {
    void do310(final Level1Packet p);
}
