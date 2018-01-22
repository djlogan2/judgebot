package chessclub.com.icc.l2;


/**
 * Handles the GAMELIST_BEGIN Level 2 packet.
 * @author David Logan
 *
 */
public class GamelistBegin extends Level2Packet {
    // 72 DG_GAMELIST_BEGIN
    // (command {parameters} nhits first last {summary})
    /**
     * The command that caused this gamelist to be sent.
     * @return  The command that caused this gamelist to be sent.
     */
    public String command() {
        return getParm(1);
    }

    /**
     * The parameters associated with the command.
     * @return  The parameters associated with the command.
     */
    public String[] parameters() {
        return getParm(2).split("\\s+");
    }

    /**
     * The number of games.
     * @return  The number of games.
     */
    public int nhits() {
        return Integer.parseInt(getParm(3));
    }

    /**
     * The index number of the first game.
     * @return  The index number of the first game.
     */
    public int first() {
        return Integer.parseInt(getParm(4));
    }

    /**
     * The index number of the last game.
     * @return  The index number of the last game.
     */
    public int last() {
        return Integer.parseInt(getParm(5));
    }

    /**
     * A "summary" field sent back with the gamelist begin packet.
     * @return  A "summary" field sent back with the gamelist begin packet.
     */
    public String summary() {
        return getParm(6);
    }
}
