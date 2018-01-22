package chessclub.com.icc.l2;

import java.util.ArrayList;
import java.util.Collections;

import chessclub.com.enums.GameState;
import chessclub.com.enums.L2;
import chessclub.com.enums.Rating;

/**
 * The instance created when a player first logs on to the ICC.
 * @author David Logan
 *
 */
public class PlayerArrived extends Level2Packet {

    private ArrayList<L2> ratingOrder = new ArrayList<L2>();

    /**
     * The method that allows us to connect the current level 2 settings.
     * We must do this before we ask for any ratings, or none of the ratings will be valid.
     * @param l2s   The current level 2 settings.
     */
    public void level2(final Level2Settings l2s) {
        for (Rating r : Rating.values()) {
            if (l2s.isSet(r.getL2())) {
                ratingOrder.add(r.getL2());
            }
        }
        Collections.sort(ratingOrder);
    }

    /**
     * The bullet rating for the user or zero.
     * @return  The bullet rating for the user or zero.
     */
    public int getBullet() {
        return getRating(Rating.BULLET);
    }

    /**
     * The blitz rating for the user or zero.
     * @return  The blitz rating for the user or zero.
     */
    public int getBlitz() {
        return getRating(Rating.BLITZ);
    }

    /**
     * The standard rating for the user or zero.
     * @return  The standard rating for the user or zero.
     */
    public int getStandard() {
        return getRating(Rating.STANDARD);
    }

    /**
     * The wild rating for the user or zero.
     * @return  The wild rating for the user or zero.
     */
    public int getWild() {
        return getRating(Rating.WILD);
    }

    /**
     * The bughouse rating for the user or zero.
     * @return  The bughouse rating for the user or zero.
     */
    public int getBughouse() {
        return getRating(Rating.BUGHOUSE);
    }

    /**
     * The losers rating for the user or zero.
     * @return  The losers rating for the user or zero.
     */
    public int getLosers() {
        return getRating(Rating.LOSERS);
    }

    /**
     * The crazyhouse rating for the user or zero.
     * @return  The crazyhouse rating for the user or zero.
     */
    public int getCrazyhouse() {
        return getRating(Rating.CRAZYHOUSE);
    }

    /**
     * The 5-minute rating for the user or zero.
     * @return  The 5-minute rating for the user or zero.
     */
    public int getFiveMinute() {
        return getRating(Rating.FIVEMINUTE);
    }

    /**
     * The 1-minute rating for the user or zero.
     * @return  The 1-minute rating for the user or zero.
     */
    public int getOneMinute() {
        return getRating(Rating.ONEMINUTE);
    }

    /**
     * The correspondence rating for the user or zero.
     * @return  The correspondence rating for the user or zero.
     */
    public int getCorrespondence() {
        return getRating(Rating.CORRESPONDENCE_RATING);
    }

    /**
     * The 15-minute rating for the user or zero.
     * @return  The 15-minute rating for the user or zero.
     */
    public int getFifteenMinute() {
        return getRating(Rating.FIFTEENMINUTE);
    }

    /**
     * The 3-minute rating for the user or zero.
     * @return  The 3-minute rating for the user or zero.
     */
    public int getThreeMinute() {
        return getRating(Rating.THREEMINUTE);
    }

    /**
     * The 45-minute rating for the user or zero.
     * @return  The 45-minute rating for the user or zero.
     */
    public int getFortyFiveMinute() {
        return getRating(Rating.FORTYFIVEMINUTE);
    }

    /**
     * The Chess960 rating for the user or zero.
     * @return  The Chess960 rating for the user or zero.
     */
    public int getChess960() {
        return getRating(Rating.CHESS960);
    }

    /**
     * The user that just logged in
     * @return The user that just logged in
     */
    public String getUser() {
        return getParm(1);
    }
    /**
     * The rating for the user or zero for the request {@link Rating}.
     * @param r The {@link Rating} we are requesting.
     * @return  The rating for the user or zero for the requested {@link Rating}
     */
    public int getRating(final Rating r) {
        int i = ratingOrder.indexOf(r.getL2());
        if (i == -1) {
            return 0;
        } else {
            return Integer.parseInt(getParm(2 + i));
        }
    }

    /**
     * true if the user is open for matches. false if not.
     * @return  true if the user is open for matches. false if not.
     */
    public boolean isOpen() {
        return "1".equals(getParm(2 + ratingOrder.size()));
    }

    /**
     * The current {@link GameState} of the user.
     * @return The current {@link GameState} of the user.
     */
    public GameState gameState() {
        return GameState.getState((getParm(3 + ratingOrder.size()).charAt(0)));
    }

    /**
     * The game number the user is observing, examining, playing, etc.
     * @return  The game number the user is observing, examining, playing, etc.
     */
    public int gameNumber() {
        return Integer.parseInt(getParm(4 + ratingOrder.size()));
    }
}
