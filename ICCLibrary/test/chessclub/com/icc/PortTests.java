/**
 * 
 */
package chessclub.com.icc;
// CHECKSTYLE:OFF
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

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
public class PortTests implements IAcceptGenericPackets {
    private String test = "";
    /**
         * 
         */
    public PortTests() {
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
    public final void badPort1() throws Exception {
        test = "badPort1";
        ICCInstance icc = new ICCInstance();
        ICCException ex = icc.setInstance("chessclub.com", -1, "", "", this, 16 * 1024);

        assertNotNull(ex);
        assertTrue("Expected a type of IllegalArgumentException, got " + ex.getOriginalException().getClass().getName(), ex.getOriginalException() instanceof IllegalArgumentException);
        assertTrue("Expected PORT, got " + ex.getError(), ICCError.PORT == ex.getError());
        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void badPort2() throws Exception {
        test = "badPort2";
        ICCInstance icc = new ICCInstance();
        ICCException ex = icc.setInstance("chessclub.com", 0, "", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void badPort3() throws Exception {
        test = "badPort3";
        ICCInstance icc = new ICCInstance();
        ICCException ex = icc.setInstance("chessclub.com", 1, "", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    public void initHandler() {
        fail("Method should not have been called");
    }

    public void setInstance(final ICCInstance icc) {
    }

    public void shuttingDown() {
        if ("badPort2".equals(test)) {
            return;
        }
        if ("badPort3".equals(test)) {
            return;
        }
        fail("Method should not have been called: test=" + test);
    }

    public void iccException(final ICCException ex) {
        assertNotNull(ex);
        assertNotNull(ex.getOriginalException());
        if ("badPort2".equals(test)) {
            assertTrue("Expected a type of ConnectException, got " + ex.getOriginalException().getClass().getName(), ex.getOriginalException() instanceof ConnectException);
            return;
        }
        if ("badPort3".equals(test)) {
            assertTrue("Expected a type of SocketTimeoutException, got " + ex.getOriginalException().getClass().getName(), ex.getOriginalException() instanceof SocketTimeoutException);
            return;
        }
        fail("Method should not have been called: test=" + test + ",exception=" + ex.getOriginalException().getClass().getName());
    }

    public void connectionClosed() {
    }

    public void Error(chessclub.com.icc.l2.Error error) {
        fail("Method should not have been called");
    }

    public void badCommand1(BadCommand p) {
        fail("Method should not have been called");
    }

    public void badCommand2(L1ErrorOnly p) {
        fail("Method should not have been called");
    }

}
