package chessclub.com.icc.l1;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chessclub.com.enums.Rating;
import chessclub.com.icc.DefaultICCAdminCommands;
import chessclub.com.icc.l1l2.Titles;

/**
 * The Administrator level getpx data return.
 * @author David Logan
 *
 */
public class Getpx extends Level1Packet {
    public final static int CURRENTRATING=0;
    public final static int BESTRATING=1;
    public final static int NUMBERGAMES=2;
    /**
     * Returns true if the getpx failed to execute.
     * @return True if getpx failed to execute.
     */
    public boolean getHasFailed() {
        return this.getpxerror;
    }
    /**
     * The userid the getpx was issued for.
     * @see DefaultICCAdminCommands#getpx(String, String)
     * @return  The userid the getpx was issued for.
     */
    public String getUserid() {
        if (!parsed) {
            parse();
        }
        return userid;
    }

    /**
     * The key value that was passed into the getpx command.
     * @see DefaultICCAdminCommands#getpx(String, String)
     * @return  The key value that was passed into the getpx command.
     */
    public String getKey() {
        if (!parsed) {
            parse();
        }
        return key;
    }

    /**
     * True if the userid is using a 7 day trial account. False if not.
     * @return  True if the userid is using a 7 day trial account. False if not.
     */
    public boolean isIs7dayTrial() {
        if (!parsed) {
            parse();
        }
        return is7dayTrial;
    }

    /**
     * I don't know what this is.
     * @return  I don't know what this is.
     */
    public int getState() {
        if (!parsed) {
            parse();
        }
        return state;
    }

    /**
     * True if the user is an ICC administrator. False if not.
     * @return  True if the user is an ICC administrator. False if not.
     */
    public boolean isAdmin() {
        if (!parsed) {
            parse();
        }
        return isAdmin;
    }

    /**
     * True if the user is expired. False if not.
     * @return  True if the user is expired. False if not.
     */

    public boolean isExpired() {
        if (!parsed) {
            parse();
        }
        return isExpired;
    }

    /**
     * True if the user is a computer account. False if not.
     * @return  True if the user is a computer account. False if not.
     */
    public boolean isComputer() {
        if (!parsed) {
            parse();
        }
        return isComputer;
    }

    /**
     * Number of seconds online if online.
     * @return  Number of seconds online if online
     */
    public int getTimeOn() {
        if (!parsed) {
            parse();
        }
        return timeOn;
    }

    /**
     * Number of seconds idle if online.
     * @return  Number of seconds idle if online
     */
    public int getTimeIdle() {
        if (!parsed) {
            parse();
        }
        return timeIdle;
    }

    /**
     * The number of pings that {@link #getPingAverage()} and {@link #getPingStd()} are based on.
     * @return  The number of pings.
     */
    public int getPingCount() {
        if (!parsed) {
            parse();
        }
        return pingCount;
    }

    /**
     * The average ping over {@link #getPingCount()} number of pings. Nonsense if the count is zero
     * @return  The average ping
     */
    public int getPingAverage() {
        if (!parsed) {
            parse();
        }
        return pingAverage;
    }

    /**
     * The standard deviations in ping values over {@link #getPingCount()} number of pings. Nonsense if the count is zero
     * @return  The standard deviations in ping values
     */
    public int getPingStd() {
        if (!parsed) {
            parse();
        }
        return pingStd;
    }

    /**
     * The titles this player holds.
     * @return  A {@link Titles} instance
     */
    public Titles getTitles() {
        if (!parsed) {
            parse();
        }
        return titles;
    }

    /**
     * The value of the users "tourney" variable. This is typically set by tournament bots when
     * the user is in a tournament.
     * @return  The 'tourney' variable value
     */
    public String getTourney() {
        if (!parsed) {
            parse();
        }
        return tourney;
    }

    /**
     * A nested array of ratings in the order below and in the form of {<b>Current rating, Best Rating, Total number of games</b>}:.
     * <ul>
     * <li>Wild</li><li>Blitz</li><li>Standard</li><li>Bullet</li><li>Bughouse</li><li>Loser's
     * </li><li>Crazyhouse</li><li>5-minute</li><li>1-minute</li><li>Correspondence</li><li>15-minute
     * </li><li>3-minute</li><li>45-Minute</li><li>Chess960</li>
     * </ul>
     * @return  The nested array of ratings: {<b>Current rating, Best Rating, Total number of games</b>} 
     */
    public ArrayList<Integer[]> getRatings() {
        if (!parsed) {
            parse();
        }
        return ratings;
    }

    private int getRatingIndex(final Rating r) {
        switch(r) {
        case WILD: return 0;
        case BLITZ: return 1;
        case STANDARD: return 2;
        case BULLET: return 3;
        case BUGHOUSE: return 4;
        case LOSERS: return 5;
        case CRAZYHOUSE: return 6;
        case FIVEMINUTE: return 7;
        case ONEMINUTE: return 8;
        case CORRESPONDENCE_RATING: return 9;
        case FIFTEENMINUTE: return 10;
        case THREEMINUTE: return 11;
        case FORTYFIVEMINUTE: return 12;
        case CHESS960: return 13;
        default: return 1;
        }
    }

    /**
     * Returns the current rating for the requested {@link Rating} type.
     * @param r The {@link Rating} type
     * @return  The current rating.
     */
    public int getCurrentRating(final Rating r) {
        return getRatings().get(getRatingIndex(r))[0];
    }

    /**
     * Returns the best rating for the requested {@link Rating} type.
     * @param r The {@link Rating} type
     * @return  The best rating.
     */
    public int getHighRating(final Rating r) {
        return getRatings().get(getRatingIndex(r))[1];
    }
    
    /**
     * Returns the number of games played for the requested {@link Rating} type.
     * @param r The {@link Rating} type
     * @return  The number of games played
     */
    public int getGameCount(final Rating r) {
        return getRatings().get(getRatingIndex(r))[2];
    }
    
    /**
     * The unique identifier. This number is guaranteed to be unique to every new user, even
     * when a new user registers the same userid as a previous user who let it expire.
     * @return  The unique identifier.
     */
    public int getUniqueId() {
        if (!parsed) {
            parse();
        }
        return uniqueId;
    }

    /**
     * Date the user was created.
     * @return  Date the user was created.
     */
    public Date getCreationDate() {
        if (!parsed) {
            parse();
        }
        return creationDate;
    }

    /**
     * true if the user is on the norated list. false otherwise.
     * @return  true if the user is on the norated list. false otherwise.
     */
    public boolean isNoRated() {
        if (!parsed) {
            parse();
        }
        return noRated;
    }

    /**
     * exempt: 0=no, 1=really exempt, 2 or higher might mean something like is-on-auto-renewal-plan.
     * @return  exempt: 0=no, 1=really exempt, 2 or higher might mean something like is-on-auto-renewal-plan.
     */
    public int getExempt() {
        if (!parsed) {
            parse();
        }
        return exempt;
    }

    /**
     * The number of days until the account expires. Possibly negative.
     * @return  The number of days until the account expires. Possibly negative.
     */
    public int getDaysToExpire() {
        if (!parsed) {
            parse();
        }
        return daysToExpire;
    }

    /**
     * The language code in the user record.
     * @return  The language code in the user record.
     */
    public String getLanguage() {
        if (!parsed) {
            parse();
        }
        return language;
    }
    
    /**
     * Whether or not this is a valid getpx, or if the getpx returned *getpxf, which is user not found.
     * @return True if the user wasn't found
     */
    public boolean isValidUser() {
        return !usernotfound;
    }

    // username: username with case corrected by the system
    // token: as sent to the system
    // is7daytrial: 0=normal, 1=seven-day-wonder
    // state: 0 = not logged on, otherwise p-"state
    // isadmin, isexpired, iscomputer are each 1 or 0
    // time-on and time-idle are in seconds (nonsense if not logged on)
    // if ping-count is 0 then ping-average and ping-std are nonsense
    // ping-average and ping-std are in ms
    // titles is a string in braces such as {* FM} (the * means admin, whether empowered or not)
    // tourney is a string in braces
    // each of wild blitz... are comma separated numbers with rating,best,count
    // We might add more ratings, and insert data before the ampersand.
    // unique_id is some integer that won't be re-used if someone else registers this name years later.
    // creation is a unix date (i.e. seconds since 1-Jan-1970, in decimal), but may be 0 for some old players.
    // norated means on the norated list
    // exempt: 0=no, 1=really exempt, 2 or higher might mean something like is-on-auto-renewal-plan
    // daystoexpiration number of days until the account expires (possibly negative), although if it's exempt ignore this.
    // *getpx djlogan x 0 1 1 0 0 116437 0 10265 82 17 {*} {0} 1400,0,0 1691,1740,2035 1784,1896,802 1400,0,0 1400,0,0 1400,0,0 1400,0,0 1211,1340,107 1600,0,0 1600,0,0 1600,0,0 1600,0,0 1600,0,0 1600,0,0 & 1112900 950116355 0 1 -2905 en
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Getpx.class);
    private boolean parsed = false;
    private String userid;
    private String key;
    private boolean is7dayTrial;
    private int state;
    private boolean isAdmin;
    private boolean isExpired;
    private boolean isComputer;
    private int timeOn;
    private int timeIdle;
    private int pingCount;
    private int pingAverage;
    private int pingStd;
    private Titles titles;
    private String tourney;
    private ArrayList<Integer[]> ratings = new ArrayList<Integer[]>();
    private int uniqueId;
    private Date creationDate;
    private boolean noRated;
    private int exempt;
    private int daysToExpire;
    private String language;
    private boolean usernotfound = false;
    private boolean getpxerror = false;

    private void parse() {
        ArrayList<String> tempParms = new ArrayList<String>();
        String temppx = getParm(1);
        if("getpx is for tournament directors and administrators.".equals(temppx)) {
            usernotfound = true;
            getpxerror = true;
            return;
        }
        while (!temppx.isEmpty()) {
            if (temppx.startsWith("{")) {
                tempParms.add(temppx.substring(1, temppx.indexOf('}')));
                temppx = temppx.substring(temppx.indexOf('}') + 2);
            } else {
                int i = temppx.indexOf(' ');
                if (i == -1) {
                    tempParms.add(temppx);
                    temppx = "";
                } else {
                    tempParms.add(temppx.substring(0, temppx.indexOf(' ')));
                    temppx = temppx.substring(temppx.indexOf(' ') + 1);
                }
            }
        }
        String[] getpxParms = new String[0];
        getpxParms = tempParms.toArray(getpxParms);
        userid = getpxParms[1];
        key = getpxParms[2];
        if("*getpxf".equals(getpxParms[0])) {
            usernotfound = true;
            return;
        }
        is7dayTrial = ("1".equals(getpxParms[3]));
        state = Integer.parseInt(getpxParms[4]);
        isAdmin = ("1".equals(getpxParms[5]));
        isExpired = ("1".equals(getpxParms[6]));
        isComputer = ("1".equals(getpxParms[7]));
        timeOn = Integer.parseInt(getpxParms[8]);
        timeIdle = Integer.parseInt(getpxParms[9]);
        pingCount = Integer.parseInt(getpxParms[10]);
        pingAverage = Integer.parseInt(getpxParms[11]);
        pingStd = Integer.parseInt(getpxParms[12]);
        titles = new Titles(getpxParms[13]);
        tourney = getpxParms[14];
        int x = 15;
        while (!"&".equals(getpxParms[x])) {
            String[] rs = getpxParms[x++].split(",");
            ratings.add(new Integer[] {Integer.parseInt(rs[0]), Integer.parseInt(rs[1]), Integer.parseInt(rs[2])});
        }
        x++;
        uniqueId = Integer.parseInt(getpxParms[x++]);
        creationDate = new Date(Integer.parseInt(getpxParms[x++]));
        noRated = ("1".equals(getpxParms[x++]));
        exempt = Integer.parseInt(getpxParms[x++]);
        daysToExpire = Integer.parseInt(getpxParms[x++]);
        language = getpxParms[x];
        parsed = true;
    }

    public Integer getRating(Rating ratingType) {
        return new Integer(ratings.get(getRatingIndex(ratingType))[CURRENTRATING]);
    }

    public Integer getBestRating(Rating ratingType) {
        return new Integer(ratings.get(getRatingIndex(ratingType))[BESTRATING]);
    }
}
