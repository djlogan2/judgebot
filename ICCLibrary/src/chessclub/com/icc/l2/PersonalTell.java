package chessclub.com.icc.l2;

import chessclub.com.enums.TellType;
import chessclub.com.icc.l1l2.Titles;

/**
 * The instance sent from the framework when this user gets a personal tell.
 * @author David Logan
 *
 */
public class PersonalTell extends Level2Packet {
    // 31 DG_PERSONAL_TELL
    // The level 2 version of a personal tell. If this is on, the
    // normal form is not generated.
    //
    // Form: (playername titles ^Y{tell string^Y} type)
    /**
     * The name of the user that we got the tell from.
     * @return  The name of the user that we got the tell from.
     */
    public String name() {
        return getParm(1);
    }

    /**
     * The title data of the person that sent you the tell.
     * 
     * @return An instance of a {@link Titles} class
     */
    public Titles titles() {
        return new Titles(getParm(2));
    }

    /**
     * The text of the tell.
     * 
     * @return The text of the tell
     */
    public String text() {
        return getParm(3);
    }

    /**
     * The {@link TellType}.
     * @return The {@link TellType}
     */
    public TellType telltype() {
        return TellType.getTellType(Integer.parseInt(getParm(4)));
    }

    /**
     * A "say" from the current (or last played) game.
     * 
     * @return true or false
     */
    public boolean say() {
        return telltype() == TellType.SAY;
    }

    /**
     * Regular Personal Tell.
     * 
     * @return true or false
     */
    public boolean tell() {
        return telltype() == TellType.TELL;
    }

    /**
     * Personal Tell from a bughouse partner.
     * 
     * @return true or false
     */
    public boolean ptell() {
        return telltype() == TellType.PTELL;
    }

    /**
     * A tell from a helper.
     * 
     * @return true or false
     */
    public boolean atell() {
        return telltype() == TellType.ATELL;
    }
    
    @Override
    public String toString() {
        return "PERSONAL_TELL["+telltype().toString()+"]" + name() + titles().toString() + ": "+text();
    }
}
