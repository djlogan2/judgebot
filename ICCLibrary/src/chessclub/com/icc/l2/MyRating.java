package chessclub.com.icc.l2;

import java.util.ArrayList;
import java.util.Collections;

import chessclub.com.enums.L2;
import chessclub.com.enums.Rating;

/**
 * 87 DG_NEW_MY_RATING
 * <p>(rating-0 rating-1 rating-2 ...)
 * <p>Your ratings, in the order specified by DG_RATING_TYPE_KEY.  Rating of
 * -1 if unregistered, 0 if registered but played no games.  (Yes, I know,
 * both are possible actual ratings, but big deal.)
 * Sent when turned on, and when rating changes.
 * @author David Logan
 *
 */
public class MyRating extends Level2Packet {

    private ArrayList<L2> ratingOrder = new ArrayList<L2>();

    /**
     * Required to be called before we can get any ratings back. Because the level 2 settings
     * specify which ratings are returned, we've got to know this so that we know which numbers
     * belong to which ratings.
     * <p>The framework calls this before the packet is passed to the handler.
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
     * Return the bullet rating or 0.
     * @return  Return the bullet rating or 0
     */
    public int getBullet() {
        return getRating(Rating.BULLET);
    }

    /**
     * Return the blitz rating or 0.
     * @return  Return the blitz rating or 0
     */
    public int getBlitz() {
        return getRating(Rating.BLITZ);
    }

    /**
     * Return the standard rating or 0.
     * @return  Return the standard rating or 0
     */
    public int getStandard() {
        return getRating(Rating.STANDARD);
    }

    /**
     * Return the wild rating or 0.
     * @return  Return the wild rating or 0
     */
    public int getWild() {
        return getRating(Rating.WILD);
    }

    /**
     * Return the bughouse rating or 0.
     * @return  Return the bughouse rating or 0
     */
    public int getBughouse() {
        return getRating(Rating.BUGHOUSE);
    }

    /**
     * Return the bullet losers or 0.
     * @return  Return the losers rating or 0
     */
    public int getLosers() {
        return getRating(Rating.LOSERS);
    }

    /**
     * Return the crazyhouse rating or 0.
     * @return  Return the crazyhouse rating or 0
     */
    public int getCrazyhouse() {
        return getRating(Rating.CRAZYHOUSE);
    }

    /**
     * Return the 5-minute rating or 0.
     * @return  Return the 5-minute rating or 0
     */
    public int getFiveMinute() {
        return getRating(Rating.FIVEMINUTE);
    }

    /**
     * Return the 1-minute rating or 0.
     * @return  Return the 1-minute rating or 0
     */
    public int getOneMinute() {
        return getRating(Rating.ONEMINUTE);
    }

    /**
     * Return the correspondence rating or 0.
     * @return  Return the correspondence rating or 0
     */
    public int getCorrespondence() {
        return getRating(Rating.CORRESPONDENCE_RATING);
    }

    /**
     * Return the 15-minute rating or 0.
     * @return  Return the 15-minute rating or 0
     */
    public int getFifteenMinute() {
        return getRating(Rating.FIFTEENMINUTE);
    }

    /**
     * Return the 3-minute rating or 0.
     * @return  Return the 3-minute rating or 0
     */
    public int getThreeMinute() {
        return getRating(Rating.THREEMINUTE);
    }

    /**
     * Return the 45-minute rating or 0.
     * @return  Return the 45-minute rating or 0
     */
    public int getFortyFiveMinute() {
        return getRating(Rating.FORTYFIVEMINUTE);
    }

    /**
     * Return the Chess960 rating or 0.
     * @return  Return the Chess960 rating or 0
     */
    public int getChess960() {
        return getRating(Rating.CHESS960);
    }

    /**
     * Gets the rating requested by the {@link Rating} enumeration.
     * If we do not have this rating, we will return zero
     * @param r The requested {@link Rating}
     * @return  The rating value or zero
     */
    public int getRating(final Rating r) {
        int i = ratingOrder.indexOf(r.getL2());
        if (i == -1) {
            return 0;
        } else {
            return Integer.parseInt(getParm(1 + i));
        }
    }
}
