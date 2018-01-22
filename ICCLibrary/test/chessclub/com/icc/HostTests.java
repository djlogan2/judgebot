/**
 * 
 */
package chessclub.com.icc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chessclub.com.enums.ICCError;
import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;

/**
 * 
 * @author David Logan
 * 
 *         Test class
 * 
 */
public class HostTests implements IAcceptGenericPackets {
    private String test = "";
    private String where = "";
    /**
     * 
     */
    public HostTests() {
    }

    /**
     * *
     * 
     * @throws Exception
     *             If an Exception occurs
     */
    @Before
    public final void setUp() throws Exception {
        test = "unknown";
        where = "entry";
    }

    /**
     * 
     * @throws Exception
     *             If an Exception occurs
     */
    @After
    public final void tearDown() throws Exception {
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void badHost() throws Exception {
        test = "badHost";
        ICCInstance icc = new ICCInstance();
        ICCException ex = icc.setInstance("badbadbad.loganshouse.com", 5010, "", "", this, 16 * 1024);
        assertNotNull(ex);
        assertTrue("Expected a type of UnknownHostException, got " + ex.getOriginalException().getClass().getName(), ex.getOriginalException() instanceof UnknownHostException);
        assertTrue("Expected HOSTNAME, got " + ex.getError(), ICCError.HOSTNAME == ex.getError());

        try {
            where = "start";
            icc.start();
        } catch (Exception e) {
            System.out.println(e);
            fail("Bad exception");
        }

        try {
            where = "join";
            icc.join();
        } catch (Exception e) {
            System.out.println(e);
            fail("Bad exception");
        }
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void badIP1() throws Exception {
        test = "badIP1";
        ICCInstance icc = new ICCInstance();
        ICCException ex = icc.setInstance("256.1.200.10", 5010, "", "", this, 16 * 1024);
        assertNotNull(ex);
        assertTrue("Expected a type of UnknownHostException, got " + ex.getOriginalException().getClass().getName(), ex.getOriginalException() instanceof UnknownHostException);
        assertTrue("Expected HOSTNAME, got " + ex.getError(), ICCError.HOSTNAME == ex.getError());
        try {
            where = "start";
            icc.start();
        } catch (Exception e) {
            System.out.println(e);
            fail("Bad exception");
        }

        try {
            where = "join";
            icc.join();
        } catch (Exception e) {
            System.out.println(e);
            fail("Bad exception");
        }
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void badHost2() throws Exception {
        test = "badHost2";
        ICCInstance icc = new ICCInstance();
        ICCException ex = icc.setInstance("196.168.1.69", 5010, "", "", this, 16 * 1024);
        assertNull(ex);
        
        try {
            where = "start";
            icc.start();
        } catch (Exception e) {
            System.out.println(e);
            fail("Bad exception");
        }
        try {
            where = "join";
            icc.join();
        } catch (Exception e) {
            System.out.println(e);
            fail("Bad exception");
        }
    }

    public void initHandler() {
        fail("Method should not have been called");

    }

    public void setInstance(final ICCInstance icc) {
    }

    public void connectionClosed() {
        fail("Method should not have been called");

    }

    public void shuttingDown() {
    }

    public void iccException(final ICCException ex) {
        assertNotNull(ex);
        assertNotNull(ex.getOriginalException());
        if ("badHost2".equals(test) && "join".equals(where)) {
            assertTrue("Expected a type of SocketTimeoutException, got " + ex.getOriginalException().getClass().getName(), ex.getOriginalException() instanceof SocketTimeoutException);
            return;
        }
        fail("Method should not have been called: test=" + test + ",where=" + where + ",exception=" + ex.getOriginalException().getClass().getName());
    }

    public void Error(chessclub.com.icc.l2.Error error) {
        fail("Method should not have been called");
    }

    public void badCommand1(BadCommand p) {
    }

    public void badCommand2(L1ErrorOnly p) {
    }

}
