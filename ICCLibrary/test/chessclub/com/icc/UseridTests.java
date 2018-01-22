/**
 * 
 */
package chessclub.com.icc;
// CHECKSTYLE:OFF
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.handler.interfaces.ILoggedOut;
import chessclub.com.icc.handler.interfaces.ILoginFailed;
import chessclub.com.icc.handler.interfaces.ISEvent;
import chessclub.com.icc.handler.interfaces.IWhoAmI;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.LoggedOut;
import chessclub.com.icc.l1.SEvent;
import chessclub.com.icc.l2.LoginFailed;
import chessclub.com.icc.l2.WhoAmI;

/**
 * 
 * @author David Logan
 * 
 *         Test class
 * 
 */
public class UseridTests extends DefaultICCAdminCommands implements IAcceptGenericPackets, ILoginFailed, IWhoAmI, ISEvent, ILoggedOut {
    private String test = "";
    @SuppressWarnings("unused")
    private String where = "";
    private ICCInstance icc = null;
    private Idler idler = null; 
    /**
         * 
         */
    public UseridTests() {
    }

    /**
     * *
     * 
     * @throws Exception
     *             If an Exception occurs
     */
    @Before
    public final void setUp() throws Exception {
        where = "unknown";
        test = "unknown";
        icc = new ICCInstance();
    }

    /**
     * 
     * @throws Exception
     *             If an Exception occurs
     */
    @After
    public final void tearDown() throws Exception {
        icc = null;
    }

    /**
     * @throws Exception 
         * 
         */
    @Test
    public final void unknownUserid() throws Exception {
        test = "unknownUserid";
        ICCException ex = icc.setInstance("chessclub.com", 5010, "", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void shortUserid() throws Exception {
        test = "shortUserid";
        ICCException ex = icc.setInstance("chessclub.com", 5010, "a", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    /**
 * 
 */
//    @Test
//    public final void longUserid() {
//        test = "longUserid";
//        ICCException ex = icc.setInstance("chessclub.com", 5010, "abcdefghijklmnopqrstuvwxyza", "", this, 16 * 1024);
//
//        assertNull(ex);
//
//        try {
//            where = "start";
//            icc.start();
//        } catch (Exception e) {
//            System.out.println(e);
//            fail("Bad exception");
//        }
//
//        try {
//            where = "join";
//            icc.join();
//        } catch (Exception e) {
//            System.out.println(e);
//            fail("Bad exception");
//        }
//        assert (false);
//    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void invalidUserid() throws Exception {
        test = "invalidPassword";
        ICCException ex = icc.setInstance("chessclub.com", 5010, "0bad", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void nullPassword1() throws Exception {
        test = "nullPassword1";
        ICCException ex = icc.setInstance("chessclub.com", 5010, "", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void nullPassword2() throws Exception {
        test = "nullPassword2";
        ICCException ex = icc.setInstance("chessclub.com", 5010, "", null, this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void nullUserid1() throws Exception {
        test = "nullUserid1";
        ICCException ex = icc.setInstance("chessclub.com", 5010, "", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void nullUserid2() throws Exception {
        test = "nullUserid2";
        ICCException ex = icc.setInstance("chessclub.com", 5010, null, "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void bannedUserid() throws Exception {
        test = "bannedUserid";
        
        where = "ban";
        ICCException ex = icc.setInstance("queen.chessclub.com", 5010, "", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();

        icc = new ICCInstance();
        ex = icc.setInstance("queen.chessclub.com", 5010, "", "", this, 16 * 1024);
        assertNull(ex);
        icc.run();

        where = "unban";
        icc = new ICCInstance();
        ex = icc.setInstance("queen.chessclub.com", 5010, "", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void alreadyLoggedIn() throws Exception {
        test = "alreadyLoggedIn";

        ICCException ex = icc.setInstance("queen.chessclub.com", 5010, "", "", this, 16 * 1024);
        assertNull(ex);
        icc.run();

        try {
            idler.quit();
            idler.join();
        } catch (Exception e) {
            System.out.println(e);
            fail("Bad exception");
        }
        assert (false);
    }

    public void initHandler() {
        if ("bannedUserid".equals(test)) {
            return;
        }
    }

    public void loginFailed(final LoginFailed p) {
        if ("unknownUserid".equals(test)) {
            assertEquals(LoginFailed.UNKNOWN_USERID, p.reason());
            assertEquals("No such username", p.textReason());
            return;
        }
        if ("shortUserid".equals(test)) {
            assertEquals(LoginFailed.BAD_USERID, p.reason());
            assertEquals("Invalid username", p.textReason());
            return;
        }
        if ("invalidUserid".equals(test)) {
            assertEquals(LoginFailed.BAD_USERID, p.reason());
            assertEquals("Invalid username", p.textReason());
            return;
        }
        if ("invalidPassword".equals(test)) {
            assertEquals(LoginFailed.BAD_USERID, p.reason());
            assertEquals("Invalid username", p.textReason());
            return;
        }
        if ("nullPassword1".equals(test)) {
            assertEquals(LoginFailed.INVALID_PASSWORD, p.reason());
            assertEquals("Invalid password.", p.textReason());
            return;
        }
        if ("nullPassword2".equals(test)) {
            assertEquals(LoginFailed.INVALID_PASSWORD, p.reason());
            assertEquals("Invalid password.", p.textReason());
            return;
        }
        if ("nullUserid1".equals(test)) {
            assertEquals(LoginFailed.BAD_USERID, p.reason());
            assertEquals("Invalid username", p.textReason());
            return;
        }
        if ("nullUserid2".equals(test)) {
            assertEquals(LoginFailed.BAD_USERID, p.reason());
            assertEquals("Invalid username", p.textReason());
            return;
        }
        if ("bannedUserid".equals(test)) {
            assertEquals(LoginFailed.USER_BANNED, p.reason());
            assertEquals("The ICC administrators have blocked your login.  "
                            + "If you have questions about this, please contact us by email at icc@chessclub.com, "
                            + "or login as a guest and ask in the help channel, or see www.chessclub.com/contact.",
                            p.textReason());
            return;
        }
        fail("Method should not have been called: test=" + test);
    }

    public void whoAmI(final WhoAmI p) {
        if ("bannedUserid".equals(test)) {
            setAdminPassword("7MoPlOcK55");
            this.onDuty(true);
            return;
        }
        if ("alreadyLoggedIn".equals(test)) {
            idler = new Idler();        
            ICCException ex;
            try {
                ex = idler.setInstance("queen.chessclub.com", 5010, "", "", this, 16 * 1024);
                assertNull(ex);
            } catch (Exception e1) {
                fail(e1.getMessage());
            }
            try {
                idler.start();
            } catch (Exception e) {
                System.out.println(e);
                fail("Bad exception");
            }
            return;
        }
        fail("Method should not have been called: test=" + test);

    }

    public void sEvent(final SEvent p) {
        if ("bannedUserid".equals(test)) {
            return;
        }
        if ("alreadyLoggedIn".equals(test)) {
            return;
        }
        fail("Method should not have been called: test=" + test);

    }

    public void connectionClosed() {
        fail("Method should not have been called: test=" + test);

    }

    public void shuttingDown() {
        return;
    }

    public void iccException(final ICCException ex) {
        // if ("unknownUserid".equals(test) && "join".equals(where)) {
        // assertTrue("Expected a type of ConnectException, got " + ex.getOriginalException().getClass().getName(), ex.getOriginalException() instanceof ConnectException);
        // return;
        // }
        fail("Method should not have been called: test=" + test + ",exception=" + ex.getOriginalException().getClass().getName());
    }

    public void loggedOut(final LoggedOut l) {
        if ("bannedUserid".equals(test)) {
            assert (!l.duplicateLogin());
            return;
        }
        if ("alreadyLoggedIn".equals(test)) {
            assert (l.duplicateLogin());
            return;
        }
        fail("Method should not have been called: test=" + test);
    }

    public void setInstance(ICCInstance icc) {
//        fail("Method should not have been called: test=" + test);
    }

    public void Error(chessclub.com.icc.l2.Error error) {
      fail("Method should not have been called: test=" + test);
    }

    public void badCommand1(BadCommand p) {
        fail("Method should not have been called: test=" + test);
    }

    public void badCommand2(L1ErrorOnly p) {
        fail("Method should not have been called: test=" + test);
    }

}
