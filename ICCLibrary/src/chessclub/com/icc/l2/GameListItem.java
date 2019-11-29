package chessclub.com.icc.l2;

import david.logan.chess.support.Color;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import chessclub.com.enums.GamelistStatus;

/**
 * The class that manages one of the game list items (Level 2 GAMELIST_ITEM packet).
 * @author David Logan
 *
 */
public class GameListItem extends Level2Packet {
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(GameListItem.class);

    // 73 DG_GAMELIST_ITEM
    // (index id event date time white-name white-rating black-name black-rating rated rating-type
    // wild init-time-W inc-W init-time-B inc-B eco status color mode {note} here)
    /**
     * The index of the game.
     * @return  The index of the game.
     */
    public int index() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The ICC game ID. ICC maintains internal game numbers that differ from board numbers that differ from indexes.
     * This is the internal game ID (that you use when you use the #12345678 format.)
     * @return  The ICC game ID.
     */
    public int id() {
        return Integer.parseInt(getParm(2));
    }

    /**
     * The ICC event this game was played in. If a player plays in a tournament (or any other reason the 'tourney' variable is set),
     * that string will be here.
     * @return  The event string.
     */
    public String event() {
        return getParm(3);
    }

    /**
     * The date and time this game ended.
     * @return  The date and time this game ended.
     */
    public DateTime datetime() {
        return (DateTimeFormat.forPattern("yyyy.MM.dd HH:mm:ss").parseDateTime(getParm(4) + " " + getParm(5)).withZone(DateTimeZone.forID("America/New_York"))).withZone(DateTime.now().getZone());
    }

    /**
     * The userid of the white player.
     * @return  The userid of the white player.
     */
    public String whitename() {
        return getParm(6);
    }

    /**
     * The rating of the white player.
     * @return  The rating of the white player.
     */
    public Integer whiterating() {
        try {
            return Integer.parseInt(getParm(7));
        } catch(NumberFormatException e) {
            return null;
        }
    }

    /**
     * The userid of the black player.
     * @return  The userid of the black player.
     */
    public String blackname() {
        return getParm(8);
    }

    /**
     * The rating of the black player.
     * @return  The rating of the black player.
     */
    public Integer blackrating() {
        try {
            return Integer.parseInt(getParm(9));
        } catch(NumberFormatException e) {
            return null;
        }
    }

    /**
     * True if the game was a rated game.
     * @return  True if the game was a rated game.
     */
    public boolean rated() {
        return "1".equals(getParm(10));
    }

    /**
     * The rating type.
     * @see RatingTypeKey
     * @return  The rating type. See {@link RatingTypeKey}
     */
    public int ratingtype() {
        return Integer.parseInt(getParm(11));
    }

    /**
     * The wild type.
     * @return  The wild type.
     */
    public int wild() {
        return Integer.parseInt(getParm(12));
    }

    /**
     * Whites initial time for the game.
     * @return  Whites initial time for the game.
     */
    public int whiteinitial() {
        return Integer.parseInt(getParm(13));
    }

    /**
     * Whites increment value.
     * @return  Whites increment value.
     */
    public int whiteinc() {
        return Integer.parseInt(getParm(14));
    }

    /**
     * Black initial time for the game.
     * @return  Black initial time for the game.
     */
    public int blackinitial() {
        return Integer.parseInt(getParm(15));
    }

    /**
     * Black sincrement value.
     * @return  Blacks increment value.
     */
    public int blackinc() {
        return Integer.parseInt(getParm(16));
    }

    /**
     * The ECO string for the opening portion of this game.
     * @return  The ECO string for the opening portion of this game.
     */
    public String eco() {
        return getParm(17);
    }

    /**
     * The ending status of this game.
     * @see GamelistStatus
     * @return See {@link GamelistStatus}
     */
    public GamelistStatus status() {
        return GamelistStatus.getStatus(Integer.parseInt(getParm(18)));
    }

    /**
     * According to the ICC documentation, this is "Color is 0=black 1=white, and is the side that lost (for status 0) or maybe acted".
     * @return  The color.
     */
    public Color color() {
        switch (Integer.parseInt(getParm(19))) {
        case 0:
            return Color.BLACK;
        case 1:
            return Color.WHITE;
        default:
            return Color.NONE;
        }
    }
    /**
     * The mode, coupled with the status, returns how this game ended:
     * <ul>
     *  <li>status <b>WIN</b>:</li>
     *  <table><tr><th>mode</th><th>code</th><th>Description</th></tr>
 *      <tr><td>0</td><td>(Res)</td><td>Black resigns</td></tr>
 *      <tr><td>1</td><td>(Mat)</td><td>Black checkmated</td></tr>
 *      <tr><td>2</td><td>(Fla)</td><td>Black forfeits on time.</td></tr>
 *      <tr><td>3</td><td>(Adj)</td><td>White declared the winner by adjudication</td></tr>
 *      <tr><td>4</td><td>(BQ)</td><td>Black disconnected and forfeits</td></tr>
 *      <tr><td>5</td><td>(BQ)</td><td>Black got disconnected and forfeits</td></tr>
 *      <tr><td>6</td><td>(BQ)</td><td>Unregistered player Black disconnected and forfeits</td></tr>
 *      <tr><td>7</td><td>(Res)</td><td>Black's partner resigns</td></tr>
 *      <tr><td>8</td><td>(Mat)</td><td>Black's partner checkmated</td></tr>
 *      <tr><td>9</td><td>(Fla)</td><td>Black's partner forfeits on time</td></tr>
 *      <tr><td>10</td><td>(BQ)</td><td>Black's partner disconnected and forfeits</td></tr>
 *      <tr><td>11</td><td>(BQ)</td><td>Black disconnected and forfeits [obsolete?]</td></tr>
 *      <tr><td>12</td><td>(1-0)</td><td>White wins [specific reason unknown]</td></tr>
 *      </table>
 * <li>status <b>DRAW</b>:</li>
     *  <table><tr><th>mode</th><th>code</th><th>Description</th></tr>
 *      <tr><td>0</td><td>(Agr)</td><td>Game drawn by mutual agreement</td></tr>
 *      <tr><td>1</td><td>(Sta)</td><td>Black stalemated</td></tr>
 *      <tr><td>2</td><td>(Rep)</td><td>Game drawn by repetition</td></tr>
 *      <tr><td>3</td><td>(50)</td><td>Game drawn by the 50 move rule</td></tr>
 *      <tr><td>4</td><td>(TM)</td><td>Black ran out of time and White has no material to mate</td></tr>
 *      <tr><td>5</td><td>(NM)</td><td>Game drawn because neither player has mating material</td></tr>
 *      <tr><td>6</td><td>(NT)</td><td>Game drawn because both players ran out of time</td></tr>
 *      <tr><td>7</td><td>(Adj)</td><td>Game drawn by adjudication</td></tr>
 *      <tr><td>8</td><td>(Agr)</td><td>Partner's game drawn by mutual agreement</td></tr>
 *      <tr><td>9</td><td>(NT)</td><td>Partner's game drawn because both players ran out of time</td></tr>
 *      <tr><td>10</td><td>(1/2)</td><td>Game drawn [specific reason unknown]</td></tr>
 *      </table>
 * <li>status <b>ADJOURNED</b>:</li>
     *  <table><tr><th>mode</th><th>code</th><th>Description</th></tr>
 *      <tr><td>0</td><td>(?)</td><td>Game adjourned by mutual agreement</td></tr>
 *      <tr><td>1</td><td>(?)</td><td>Game adjourned when Black disconnected</td></tr>
 *      <tr><td>2</td><td>(?)</td><td>Game adjourned by system shutdown</td></tr>
 *      <tr><td>3</td><td>(?)</td><td>Game courtesyadjourned by Black</td></tr>
 *      <tr><td>4</td><td>(?)</td><td>Game adjourned by an administrator</td></tr>
 *      <tr><td>5</td><td>(?)</td><td>Game adjourned when Black got disconnected</td></tr>
 *      </table>
 * <li>status <b>ABORT</b>:</li>
     *  <table><tr><th>mode</th><th>code</th><th>Description</th></tr>
 *      <tr><td>0</td><td>(Agr)</td><td>Game aborted by mutual agreement</td></tr>
 *      <tr><td>1</td><td>(BQ)</td><td>Game aborted when Black disconnected</td></tr>
 *      <tr><td>2</td><td>(SD)</td><td>Game aborted by system shutdown</td></tr>
 *      <tr><td>3</td><td>(BA)</td><td>Game courtesyaborted by Black</td></tr>
 *      <tr><td>4</td><td>(Adj)</td><td>Game aborted by an administrator</td></tr>
 *      <tr><td>5</td><td>(Sho)</td><td>Game aborted because it's too short to adjourn</td></tr>
 *      <tr><td>6</td><td>(BQ)</td><td>Game aborted when Black's partner disconnected</td></tr>
 *      <tr><td>7</td><td>(Sho)</td><td>Game aborted by Black at move 1</td></tr>
 *      <tr><td>8</td><td>(Sho)</td><td>Game aborted by Black's partner at move 1</td></tr>
 *      <tr><td>9</td><td>(Sho)</td><td>Game aborted because it's too short</td></tr>
 *      <tr><td>10</td><td>(Adj)</td><td>Game aborted because Black's account expired</td></tr>
 *      <tr><td>11</td><td>(BQ)</td><td>Game aborted when Black got disconnected</td></tr>
 *      <tr><td>12</td><td>(?)</td><td>No result [specific reason unknown]</td></tr>
 *      </table>
 *      </ul>
     * @return  The mode value
     */
    public int mode() {
        return Integer.parseInt(getParm(20));
    }

    /**
     * The libannotate string that sometimes accompanies a library game.
     * @return  The libannotate string that sometimes accompanies a library game.
     */
    public String note() {
        return getParm(21);
    }

    /**
     * true if it's a stored list and opponent is present, otherwise false.
     * @return  true if it's a stored list and opponent is present, otherwise false.
     */
    public boolean here() {
        return "1".equals(getParm(22));
    }

    /**
     * This function returns the color that disconnected. If the game wasn't adjourned or another mode/status indicates we cannot figure out who
     * disconnected, it returns <b>NONE</b>.
     * @return  The disconnecting color or <b>NONE</b>
     */
    public Color whoDisconnected() {
        if (mode() != 1 && mode() != 5) {
            return Color.NONE;
        } else {
            return color();
        }
    }

}
/*
 * ANYONE IMPLEMENTING THIS CAN USE THE TABLE BELOW TO CONVERT THE STATUS AND MODE INTO TEXT STRINGS, SHORT OR LONG, AND USE LOCALE OR WHATEVER A CALLER WISHES TO DO
 *
 * status 0:
 *      mode 0</td><td>(Res) Black resigns
 *      mode 1</td><td>(Mat) Black checkmated
 *      mode 2</td><td>(Fla) Black forfeits on time.
 *      mode 3</td><td>(Adj) White declared the winner by adjudication
 *      mode 4</td><td>(BQ) Black disconnected and forfeits
 *      mode 5</td><td>(BQ) Black got disconnected and forfeits
 *      mode 6</td><td>(BQ) Unregistered player Black disconnected and forfeits
 *      mode 7</td><td>(Res) Black's partner resigns
 *      mode 8</td><td>(Mat) Black's partner checkmated
 *      mode 9</td><td>(Fla) Black's partner forfeits on time
 *      mode 10</td><td>(BQ) Black's partner disconnected and forfeits
 *      mode 11</td><td>(BQ) Black disconnected and forfeits [obsolete?]
 *      mode 12</td><td>(1-0) White wins [specific reason unknown]
 * status 1:
 *      mode 0</td><td>(Agr) Game drawn by mutual agreement
 *      mode 1</td><td>(Sta) Black stalemated
 *      mode 2</td><td>(Rep) Game drawn by repetition
 *      mode 3</td><td>(50) Game drawn by the 50 move rule
 *      mode 4</td><td>(TM) Black ran out of time and White has no material to mate
 *      mode 5</td><td>(NM) Game drawn because neither player has mating material
 *      mode 6</td><td>(NT) Game drawn because both players ran out of time
 *      mode 7</td><td>(Adj) Game drawn by adjudication
 *      mode 8</td><td>(Agr) Partner's game drawn by mutual agreement
 *      mode 9</td><td>(NT) Partner's game drawn because both players ran out of time
 *      mode 10</td><td>(1/2) Game drawn [specific reason unknown]
 * status 2:
 *      mode 0</td><td>(?) Game adjourned by mutual agreement
 *      mode 1</td><td>(?) Game adjourned when Black disconnected
 *      mode 2</td><td>(?) Game adjourned by system shutdown
 *      mode 3</td><td>(?) Game courtesyadjourned by Black
 *      mode 4</td><td>(?) Game adjourned by an administrator
 *      mode 5</td><td>(?) Game adjourned when Black got disconnected
 * status 3:
 *      mode 0</td><td>(Agr) Game aborted by mutual agreement
 *      mode 1</td><td>(BQ) Game aborted when Black disconnected
 *      mode 2</td><td>(SD) Game aborted by system shutdown
 *      mode 3</td><td>(BA) Game courtesyaborted by Black
 *      mode 4</td><td>(Adj) Game aborted by an administrator
 *      mode 5</td><td>(Sho) Game aborted because it's too short to adjourn
 *      mode 6</td><td>(BQ) Game aborted when Black's partner disconnected
 *      mode 7</td><td>(Sho) Game aborted by Black at move 1
 *      mode 8</td><td>(Sho) Game aborted by Black's partner at move 1
 *      mode 9</td><td>(Sho) Game aborted because it's too short
 *      mode 10</td><td>(Adj) Game aborted because Black's account expired
 *      mode 11</td><td>(BQ) Game aborted when Black got disconnected
 *      mode 12</td><td>(?) No result [specific reason unknown]
 */
