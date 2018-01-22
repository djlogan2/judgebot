package chessclub.com.icc.l2;

import chessclub.com.icc.l1l2.Titles;

/**
 * An instance sent from the framework when a game is started. It contains a lot of game information.
 * @author David Logan
 *
 */
public class GameStarted extends Level2Packet {
    // 12 DG_GAME_STARTED

    // Form: (gamenumber whitename blackname wild-number rating-type rated
    // white-initial white-increment black-initial black-increment
    // played-game {ex-string} white-rating black-rating game-id
    // white-titles black-titles irregular-legality irregular-semantics
    // uses-plunkers fancy-timecontrol promote-to-king)
    // 15 768 laxman A-1 0 5-minute 1 5 0 5 0 0 {Ex: laxman -1} 2108 2154 1523373293 {} {} 0 0 0 {} 0
    // protected void level2(Level2Settings l2s)
    // {
    // }
    /**
     * The ICC game number.
     * @return The ICC game number.
     */
    public int gamenumber() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The white players name. 
     * @return  The white players name.
     */
    public String whitename() {
        return getParm(2);
    }

    /**
     * The black players name.
     * @return  The black players name.
     */
    public String blackname() {
        return getParm(3);
    }

    /**
     * The wild type.
     * @return  The wild type.
     */
    public int wildnumber() {
        return Integer.parseInt(getParm(4));
    }

    /**
     * An english description of the rating type. 
     * @return An english description of the rating type.
     */
    public String ratingtype() {
        return getParm(5);
    }

    /**
     * True if this game is rated. False if unrated.
     * @return  True if this game is rated. False if unrated.
     */
    public boolean rated() {
        return "1".equals(getParm(6));
    }

    /**
     * Whites initial time.
     * @return  Whites initial time.
     */
    public int whiteinitial() {
        return Integer.parseInt(getParm(7));
    }

    /**
     * Whites increment value.
     * @return  Whites increment value.
     */
    public int whiteincrement() {
        return Integer.parseInt(getParm(8));
    }

    /**
     * Blacks initial time.
     * @return  Blacks initial time.
     */
    public int blackinitial() {
        return Integer.parseInt(getParm(9));
    }

    /**
     * Blacks increment value.
     * @return  Blacks increment value.
     */
    public int blackincrement() {
        return Integer.parseInt(getParm(10));
    }

    /**
     * true if this game is being played. false if this game is being examined.
     * @return  true if this game is being played. false if this game is being examined.
     */
    public boolean played() {
        return "1".equals(getParm(11));
    }

    /**
     * The english text that describes an examined games reason for being examined.
     * @return  The english text that describes an examined games reason for being examined.
     */
    public String exstring() {
        return getParm(12);
    }

    /**
     * Returns true if this is an examined game. false if this game is being played.
     * @return  true if this is an examined game. false if this game is being played.
     */
    public boolean examined() {
        return !played();
    }

    /**
     * Whites rating at the start of the game. 0 for unrated players.
     * @return  Whites rating at the start of the game. 0 for unrated players.
     */
    public int whiterating() {
        return Integer.parseInt(getParm(13));
    }

    /**
     * Blacks rating at the start of the game. 0 for unrated players.
     * @return  Blacks rating at the start of the game. 0 for unrated players.
     */
    public int blackrating() {
        return Integer.parseInt(getParm(14));
    }

    /**
     * game-id is a number that can be used to specify a stored game, e.g. with "sposition #902425349".
     * @return  The ICC game id.
     */
    public int gameid() {
        return Integer.parseInt(getParm(15));
    }

    /**
     * The titles the white player holds.
     * @see Titles
     * @return  The {@link Titles} instance
     */
    public Titles whitetitles() {
        return new Titles(getParm(16));
    }

    /**
     * The titles the black player holds.
     * @see Titles
     * @return  The {@link Titles} instance
     */
    public Titles blacktitles() {
        return new Titles(getParm(17));
    }

    /**
     * irregular-legality is 0 normally, 1 if there are legal moves that wouldn't
        be legal in chess (i.e., turn off client-side legal move checking unless
        the client is familiar with this wild type).
     * @return  true if irregular moves are allowed false if not.
     */
    public boolean irregularLegality() {
        return "1".equals(getParm(18));
    }

    /**
     * irregular-semantics is 0 normally, 1 for atomic chess and other variants
        where you can't update the board based on the last move by usual rules.
        (see also DG_SET_BOARD)
     * @return  true if irregular semantics are allowed, false if not.
     */
    public boolean irregularSemantics() {
        return "1".equals(getParm(19));
    }

    /**
     * uses-plunkers is 0 normally, 1 for bughouse and similar variants.
     * @return  true or false
     */
    public boolean usesPlunkers() {
        return "1".equals(getParm(20));
    }

    /**
     *  if non-null specifies a time control that supercedes the
        white-initial white-increment black-initial black-increment fields.
        <p>The format is as in PGN, although we might extend that to allow Bronstein delays,
        time odds, and other strange situations.
        and to improve readability.  
        <p>Examples you might actually see:<ul>
          <li>40/7200:20/3600       (2 hours, plus an hour after 40th, 60th move, 80th move...)</li>
          <li>40/5400+30:900+30     (90 minutes, plus 15 minutes after 40th move, plus 30 second increment throughout)</li>
          <li>?                     (unknown -- best not to display clocks)</li> 
          <li>-                     (untimed -- best not to display clocks)</li></ul>
        <p>Ideas for extending it:
        <ul>
          <li>40/2h:20/1h   (h for hour, m for minute, s for second (default))</li>
          <li>G/25m         (a G/ or SD/ means for the rest of the game)</li>
          <li>5m+d3         (Bronstein delay)  </li>
          <li>30+d0.2       (1/5th second delay)</li>
          <li>0+d10         (simply you have 10 seconds for each move)</li>
          <li>*60s          (hourglass)</li>
          <li>?             (unknown)</li>
          <li>untimed</li>
          <li>5m+10 vs 6m+10</li>
          <li>correspondence 10/30days  (clocks in 60ths of a day and tick upward)</li>
          <li>G/2h:25/10m byo-yomi   (for the game of Go ... okay never mind)</li></ul>
     * @return  The fancy time control string or null
     */
    public String fancytimecontrol() {
        return getParm(21);
    }

    /**
     * false normally, true if it may sometimes be legal to promote a pawn to a king.
     * @return  false normally, true if it may sometimes be legal to promote a pawn to a king.
     */
    public boolean promotetoking() {
        return "1".equals(getParm(22));
    }
}
