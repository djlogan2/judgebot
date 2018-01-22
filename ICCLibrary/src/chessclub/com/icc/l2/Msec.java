/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chessclub.com.icc.l2;

import david.logan.chess.support.Color;

/**
 * Created to return a DG_MSEC Level 2 packet. 
 * <p>56 DG_MSEC
 * <p>(gamenumber color msec running)
 * <p>Number of milliseconds on the clock (in range -2^31 to 2^31-1).
 * Color is "W" or "B".  Running is 0 or 1, and is intended to tell
 * the client whether to make that clock tick down.
 * <p>These DGs are sent in the following situations:
 * <p>Played-game cases:<ol>
 * <li>when someone makes a move in a played game, to give their
 * new clock value, including increment earned.</li> 
 * <li>when someone's clock starts running
 * <ul><li>no-timestamp case, when it becomes his/your move</li>
 * <li>timestamp case, not you, ack-board received</li>
 * <li>timestamp case, your move now (inside ts signals)</li></ul>
 * </li>
 * <li>when a played game starts</li>
 * <li>when a played game ends</li>
 * <li>on a takeback</li>
 * <li>setwhiteclock/setblackclock (but setting a moving clock is
 * unpredictable)</li></ol>
 * <p><b>It is NOT sent in response to a moretime command.</b>
 * <p>Examine-mode cases:
 * <p>[unclear what behavior we want here, but for now running=0 for these]
 * <ol start=8>
 * <li>move in examined game</li>
 * <li>start of examined game</li>
 * <li>backward or revert</li>
 * <li>forward</li>
 * <li>setwhiteclock/setblackclock</li>
 * </ol>
 * <p>Misc:
 * <ol start=13><li>spos</li>
 * <li>refresh of game not being observed</li></ol>
     * @author David Logan
 */
public class Msec extends Level2Packet {
    // 56 DG_MSEC
    // (gamenumber color msec running)
    /**
     * The game number this packet is connected to.
     * @return  The game number this packet is connected to.
     */
    public int gamenumber() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The {@link Color} this packet is reporting on.
     * @return  The {@link Color} this packet is reporting on.
     */
    public Color color() {
        switch (getParm(2).charAt(0)) {
        case 'w':
        case 'W':
            return Color.WHITE;
        case 'b':
        case 'B':
            return Color.BLACK;
        default:
            break;
        }
        return null;
    }

    /**
     * The number of milliseconds left for this player.
     * @return  The number of milliseconds left for this player.
     */
    public int msec() {
        return Integer.parseInt(getParm(3));
    }

    /**
     * If true, the interface should set the clock in motion from this time.
     * @return  true if the interface should run down the clock. false if the clock should be static.
     */
    public boolean running() {
        return "1".equals(getParm(4));
    }
}
