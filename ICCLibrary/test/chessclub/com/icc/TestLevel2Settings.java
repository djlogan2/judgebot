package chessclub.com.icc;
import static org.junit.Assert.*;

import org.junit.Test;

import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l2.Level2Settings;


public class TestLevel2Settings implements IAcceptGenericPackets {
    private class testL2 extends Level2Settings {
        private String ourl2 = "";
        public testL2() {
            int l= super.toString().length();
            for(int x=0;x<=l+900;x++)
                ourl2 += "1";
        }
        @Override
        public String toString() {
            System.out.println("level2settings="+ourl2);
            return ourl2;
        }
    }
    
    @Test
    public void testl2() throws Exception {
        ICCInstance icc = new ICCInstance();
        icc.setLevel2Settings(new testL2());
        icc.setInstance("chessclub.com", 5010, "", "", this, 4096);
        icc.start();
        icc.join();
    }

    public void initHandler() {
    }

    public void iccException(ICCException ex) {
        fail();
    }

    public void setInstance(ICCInstance icc) {
    }

    public void connectionClosed() {
        fail();
    }

    public void shuttingDown() {
    }

    public void Error(chessclub.com.icc.l2.Error error) {
        fail();
    }

    public void badCommand1(BadCommand p) {
        fail();
    }

    public void badCommand2(L1ErrorOnly p) {
        fail();
    }

}
