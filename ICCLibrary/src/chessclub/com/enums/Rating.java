package chessclub.com.enums;

import java.util.HashMap;

/**
 * The various ICC rating types.
 * @author David Logan
 *
 */
public enum Rating {
    /**
     * Bullet rating.
     */
    BULLET(L2.BULLET),
    /**
     * Blitz rating.
     */
    BLITZ(L2.BLITZ),
    /**
     * Standard rating.
     */
    STANDARD(L2.STANDARD),
    /**
     * Wild rating.
     */
    WILD(L2.WILD),
    /**
     * Bughouse rating.
     */
    BUGHOUSE(L2.BUGHOUSE),
    /**
     * Losers rating.
     */
    LOSERS(L2.LOSERS),
    /**
     * Crazyhouse rating.
     */
    CRAZYHOUSE(L2.CRAZYHOUSE),
    /**
     * 5-minute rating.
     */
    FIVEMINUTE(L2.FIVEMINUTE),
    /**
     * 1-minute rating.
     */
    ONEMINUTE(L2.ONEMINUTE),
    /**
     * Correspondence rating.
     */
    CORRESPONDENCE_RATING(L2.CORRESPONDENCE_RATING),
    /**
     * 15-minute rating.
     */
    FIFTEENMINUTE(L2.FIFTEENMINUTE),
    /**
     * 3-minute rating.
     */
    THREEMINUTE(L2.THREEMINUTE),
    /**
     * 45-minute rating.
     */
    FORTYFIVEMINUTE(L2.FORTYFIVEMINUTE),
    /**
     * Chess960 rating.
     */
    CHESS960(L2.CHESS960);

    private L2 l2;

    Rating(final L2 pl2) {
        this.l2 = pl2;
    }

    /**
     * Returns the {@link L2} enumeration value for this Rating value.
     * @return  The {@link L2} instance.
     */
    public L2 getL2() {
        return l2;
    }

    /**
     * Return the enumeration based on the integer DG level 2 packet value.
     * @param l2    The DG Level 2 packet values. ({@link L2})
     * @return  The enumerated value.
     */
    static Rating getRating(final L2 l2) {
        for (Rating r : Rating.values()) {
            if (r.l2 == l2) {
                return r;
            }
        }
        return null;
    }

    public static Rating fromString(String from) {
        return conversion.get(from.toLowerCase());
    }
    @Override
    public String toString() {
        return Rating.bconversion.get(this);
    }
    private final static HashMap<String,Rating> conversion = new HashMap<String,Rating>();
    private final static HashMap<Rating,String> bconversion = new HashMap<Rating,String>();
    static {
        conversion.put("bullet", BULLET);
        conversion.put("blitz", BLITZ);
        conversion.put("standard", STANDARD);
        conversion.put("wild", WILD);
        conversion.put("bughouse", BUGHOUSE);
        conversion.put("loser's", LOSERS);
        conversion.put("crazyhouse", CRAZYHOUSE);
        conversion.put("5-minute", FIVEMINUTE);
        conversion.put("1-minute", ONEMINUTE);
        conversion.put("correspondence", CORRESPONDENCE_RATING);
        conversion.put("15-minute", FIFTEENMINUTE);
        conversion.put("3-minute", THREEMINUTE);
        conversion.put("45-minute", FORTYFIVEMINUTE);
        conversion.put("chess960", CHESS960);
        bconversion.put(BULLET, "Bullet");
        bconversion.put(BLITZ, "Blitz");
        bconversion.put(STANDARD, "Standard");
        bconversion.put(WILD, "Wild");
        bconversion.put(BUGHOUSE, "Bughouse");
        bconversion.put(LOSERS, "Loser's");
        bconversion.put(CRAZYHOUSE, "Crazyhouse");
        bconversion.put(FIVEMINUTE, "5-minute");
        bconversion.put(ONEMINUTE, "1-minute");
        bconversion.put(CORRESPONDENCE_RATING, "Correspondence");
        bconversion.put(FIFTEENMINUTE, "15-minute");
        bconversion.put(THREEMINUTE, "3-minute");
        bconversion.put(FORTYFIVEMINUTE, "45-minute");
        bconversion.put(CHESS960, "Chess960");
    }
}
