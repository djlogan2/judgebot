package chessclub.com.icc.l2;

import chessclub.com.enums.TellType;

/**
 * The instance sent in response to a personal tell of ours we issued to somebody else.
 * @author David Logan
 *
 */
public class PersonalTellEcho extends Level2Packet {
    // 62 DG_PERSONAL_TELL_ECHO
    // Form: (receivername type ^Y{string^Y})
    /**
     * The person we sent the tell to.
     * @return The person we sent the tell to.
     */
    public String toname() {
        return getParm(1);
    }

    /**
     * The {@link TellType} of the tell.
     * @return  The {@link TellType} of the tell.
     */
    public TellType telltype() {
        return TellType.getTellType(Integer.parseInt(getParm(2)));
    }

    /**
     * The text we sent.
     * @return  The text we sent.
     */
    public String text() {
        return getParm(3);
    }
}
