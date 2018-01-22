package chessclub.com.enums;

import chessclub.com.icc.l2.PlayerArrived;

/**
 * An enumeration that helps with the {@link PlayerArrived} packet.
 * @author David Logan
 *
 */
public enum GameState {
    /**
     * User is not playing a game.
     */
    NONE('X'),
    /**
     * User is playing a game.
     */
    PLAYING('P'),
    /**
     * User is examining a game.
     */
    EXAMINING('E'),
    /**
     * User is playing a simul.
     */
    SIMUL('S');
    private char c;

    GameState(final char pC) {
        this.c = pC;
    }

    /**
     * Returns the character associated with the enum.
     * <table>
     *     <tr><td>NONE</td><td><b>X</b></td></tr>
     *     <tr><td>PLAYING</td><td><b>P</b></td></tr>
     *     <tr><td>EXAMINING</td><td><b>E</b></td></tr>
     *     <tr><td>SIMUL</td><td><b>S</b></td></tr>
     *     </table>
     * @return  The character
     */
    public char getChar() {
        return c;
    }

    /**
     * Returns the enum from the given character.
     * <table>
     *     <tr><td>NONE</td><td><b>X</b></td></tr>
     *     <tr><td>PLAYING</td><td><b>P</b></td></tr>
     *     <tr><td>EXAMINING</td><td><b>E</b></td></tr>
     *     <tr><td>SIMUL</td><td><b>S</b></td></tr>
     *     </table>
     * @param c The character value
     * @return  The enumerated value
     */
    public static GameState getState(final char c) {
        for (GameState g : GameState.values()) {
            if (g.getChar() == c) {
                return g;
            }
        }
        return null;
    }
}
