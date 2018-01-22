/**
 * 
 */
package chessclub.com.icc;
// CHECKSTYLE:OFF
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chessclub.com.icc.l1.SEvent;
import chessclub.com.icc.l2.WhoAmI;

/**
 * 
 * @author David Logan
 * 
 *         Test class
 * 
 */
public class ShoutTests extends FailEverything {
    private ICCInstance icc = null;
    private String test = null;

    /**
         * 
         */
    public ShoutTests() {
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
        icc = null;
    }

    /**
     * @throws Exception 
         * 
         */
    @Test
    public final void testShout1() throws Exception {
        test = "testShout1";
        icc = new ICCInstance();
        ICCException ex = icc.setInstance("queen.chessclub.com", 5010, "", "", this, 16 * 1024);

        assertNull(ex);

        icc.run();
    }

    /**
     * @throws Exception 
     * 
     */
    @Test
    public final void testShout2() throws Exception {
        test = "testShout2";
        icc = new ICCInstance();
        ICCException ex = icc.setInstance("queen.chessclub.com", 5010, "", "", this, 16 * 1024);

        assertNull(ex);

        icc.run();
    }

    @Override
    public void whoAmI(final WhoAmI p) {
        if ("testShout1".equals(test)) {
            shout("Hello world");
        }
        if ("testShout1".equals(test)) {
            iShout("Hello world");
        }
    }

    @Override
    public void shuttingDown() {
    }

    @Override
    public void sEvent(final SEvent p) {
    }

}
