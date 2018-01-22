package chessclub.com.icc;

import chessclub.com.icc.l1.Level1Packet;
import chessclub.com.icc.l2.Level2Packet;

public interface ICCPacketHandler {
    public boolean handleLevel1(Level1Packet p);
    public boolean handleLevel2(Level2Packet p);
    public boolean handleOther(Object p);
}
