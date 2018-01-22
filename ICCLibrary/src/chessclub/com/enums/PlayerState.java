package chessclub.com.enums;

import chessclub.com.icc.l2.PlayerStatePacket;

/**
 * The current player state in terms of playing or examining games.
 * @author David Logan
 * @see PlayerStatePacket
 *
 */
public enum PlayerState {
    /**
     * The user is playing a game.
     */
    PLAYING('P'),
    /**
     * The user is examining a game.
     */
    EXAMINING('E'),
    /**
     * The user is playing in a simul.
     */
    PLAYINGSIMUL('S'),
    /**
     * The user is not playing or examining any game.
     */
    NONE('X');
    
    private char iccstate;

    private PlayerState(final char c) {
        iccstate = c;
    }

    /**
     * Takes the character from the incoming packet data and returns the associated enumeration.
     * @param c The raw character
     * @return  The enumeration.
     */
    public static PlayerState getState(final char c) {
        for (PlayerState p : PlayerState.values()) {
            if (p.iccstate == c) {
                return p;
            }
        }
        return null;
    }
}
