package chessclub.com.icc.l1;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * The instance created when the user first logs on. This is just a bunch of initial
 * logon "event" information designed to be dumped in text format to the user.
 * <p>It will be all in english.
 * @author David Logan
 *
 */
public class SEvent extends Level1Packet {
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(SEvent.class);

    /**
     * The number of lines of text.
     * @return The number of lines of text.
     */
    public int numberLines() {
        return numberParms() - 1;
    }

    /**
     * Returns the requested line.
     * @param index The requested line by zero-based index.
     * @return The line of data.
     */
    public String getLine(final int index) {
        return getParm(index + 1);
    }
}
