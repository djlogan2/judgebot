package chessclub.com.icc.l2;

import chessclub.com.enums.PlayerState;

/**
 * The instance sent for a DG_STATE packet.
 * @author David Logan
 *
 */
public class PlayerStatePacket extends Level2Packet {
    // 11 DG_STATE
    // form: (playername P 23)
    // State information (including game number)
    // P=playing
    // E=examining
    // S=playing simul
    // X=none of the above
    /**
     * The name of the user this state information is for.
     * @return The name of the user this state information is for.
     */
    public String name() {
        return getParm(1);
    }

    /**
     * The current {@link PlayerState}.
     * @return The current {@link PlayerState}
     */
    public PlayerState state() {
        return PlayerState.getState(getParm(2).charAt(0));
    }

    /**
     * True if the user is currently playing a game.
     * @return True if the user is currently playing a game.
     */
    public boolean isPlaying() {
        return state() == PlayerState.PLAYING;
    }

    /**
     * true if the user is examining a game.
     * @return true if the user is examining a game.
     */
    public boolean isExamining() {
        return state() == PlayerState.EXAMINING;
    }

    /**
     * true if the user is playing in a simul.
     * @return true if the user is playing in a simul.
     */
    public boolean isPlayingSimul() {
        return state() == PlayerState.PLAYINGSIMUL;
    }

    /**
     * The user is not playing or examining any game.
     * @return The user is not playing or examining any game.
     */
    public boolean isNone() {
        return state() == PlayerState.NONE;
    }
}
