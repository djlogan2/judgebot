package chessclub.com.icc.l1;

import java.util.HashMap;

import chessclub.com.enums.Rating;

/**
 * The Administrator level getps data return.
 * @author David Logan
 *
 */
public class Getps extends Level1Packet {
    //*getps 
    //*getps uid    trial ttl_wild ttl_blitz ttl_std ttl_bullet ttl_bug ttl_losers ttl_crazy ttl_5 ttl_1 ttl_corr ttl_15 ttl_3 ttl_45 ttl_960
    //idjit is not logged in.
    //"fuckme" does not match any player's name exactly.
    private String user;
    private Boolean loggedoff;
    private boolean baduser = false;
    private Boolean trial;
    private boolean parsed = false;
    private HashMap<Rating, Integer> ratings = new HashMap<Rating, Integer>();
    private static Rating[] order = new Rating[]{Rating.WILD, Rating.BLITZ, Rating.STANDARD, Rating.BULLET, Rating.BUGHOUSE, Rating.LOSERS, Rating.CRAZYHOUSE, Rating.FIVEMINUTE, Rating.ONEMINUTE, Rating.CORRESPONDENCE_RATING, Rating.FIFTEENMINUTE, Rating.THREEMINUTE, Rating.FORTYFIVEMINUTE, Rating.CHESS960};
    public String getUser() {
        if(!parsed) parse();
        return user;
    }
    public Boolean isLoggedOff() {
        if(!parsed) parse();
        return loggedoff;
    }
    public boolean isBadUser() {
        if(!parsed) parse();
        return baduser;
    }
    public Boolean isTrial() {
        if(!parsed) parse();
        return trial;
    }
    public Integer getRating(Rating type) {
        if(!parsed) parse();
        return ratings.get(type); // Obviously will return null unless getps was valid with a logged on user
    }
    private void parse() {
        if(getParm(1).endsWith("is not logged in.")) {
            loggedoff = true;
            user = (getParm(1).split("\\s+"))[0];
        } else if(getParm(1).endsWith("does not match any player's name exactly.")) {
            baduser = true;
            user = (getParm(1).split("\""))[1];
        } else if(getParm(1).startsWith("*getps")) {
            loggedoff = false;
            String[] items = getParm(1).split("\\s+");
            user = items[1];
            trial = items[2].equals("1");
            for(int x=3;x<items.length;x++)
                ratings.put(order[x-3], Integer.parseInt(items[x]));
        } else {
            baduser = true;
        }
    }
    
}
