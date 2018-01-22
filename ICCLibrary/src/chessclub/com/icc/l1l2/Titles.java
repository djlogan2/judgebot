package chessclub.com.icc.l1l2;

import java.util.ArrayList;

/**
 * The instance that returns the various types of titles an ICC user can have.
 * @author David Logan
 *
 */
public class Titles {

    private ArrayList<String> titles = null;

    /**
     * The constructor.
     * @param pTitles   The manual string data from incoming ICC data.
     */
    public Titles(final String pTitles) {
        if (pTitles == null || pTitles.length() == 0) {
            return;
        }
        titles = new ArrayList<String>();
        String noparens = pTitles.replace('(', ' ').replace(')', ' ');
        for (String t : noparens.split("\\s+")) {
            if(t != null && t.length() != 0)
                titles.add(t);
        }
    }

    /**
     * true if the user has the 'GM' title.
     * @return true if the user has the 'GM' title.
     */
    public boolean isGM() {
        return (titles != null && titles.contains("GM"));
    }

    /**
     * true if the user has the 'IM' title.
     * @return true if the user has the 'IM' title.
     */
    public boolean isIM() {
        return (titles != null && titles.contains("IM"));
    }

    /**
     * true if the user has the 'FM' title.
     * @return true if the user has the 'FM' title.
     */
    public boolean isFM() {
        return (titles != null && titles.contains("FM"));
    }

    /**
     * true if the user has the 'WGM' title.
     * @return true if the user has the 'WGM' title.
     */
    public boolean isWGM() {
        return (titles != null && titles.contains("WGM"));
    }

    /**
     * true if the user has the 'WIM' title.
     * @return true if the user has the 'WIM' title.
     */
    public boolean isWIM() {
        return (titles != null && titles.contains("WIM"));
    }

    /**
     * true if the user has the 'WFM' title.
     * @return true if the user has the 'WFM' title.
     */
    public boolean isWFM() {
        return (titles != null && titles.contains("WFM"));
    }

    /**
     * true if the user has any of the FIDE titles.
     * @return true if the user has any of the FIDE titles.
     */
    public boolean isTitled() {
        return (titles != null && (isGM() || isIM() || isFM() || isWGM() || isWIM() || isWFM()));
    }

    /**
     * true if the user has the 'TD' (Tournament Director) title.
     * @return true if the user has the 'TD' (Tournament Director) title
     */
    public boolean isTD() {
        return (titles != null && titles.contains("TD"));
    }

    /**
     * true if the user has the 'C' (Computer account) label.
     * @return true if the user has the 'C' (Computer account) label
     */
    public boolean isC() {
        return (titles != null && titles.contains("C"));
    }

    /**
     * true if the user has the 'C' (Computer account) label.
     * @return true if the user has the 'C' (Computer account) label
     */
    public boolean isComputer() {
        return isC();
    }

    /**
     * true if the username is not a registered user.
     * ICC generally does not allow usernames other than "guest####"
     * to be logged on to ICC. However, "guest####" should have this title.
     * @return true if the username is not registered.
     */
    public boolean isUnregistered() {
        return (titles != null && titles.contains("U"));
    }

    /**
     * If the username is a registered username.
     * @return If the username is a registered username.
     */
    public boolean isRegistered() {
        return !isUnregistered();
    }

    /**
     * true if the user is an on-duty ICC administrator. Going on and off duty turns on and off
     * the administrators (*) title.
     * @return true if the user is an on-duty ICC administrator.
     */
    public boolean isOnDutyAdministrator() {
        return (titles != null && titles.contains("*"));
    }

    /**
     * true if the user as the 'DM' (Display Master) title.
     * @return true if the user as the 'DM' (Display Master) title.
     */
    public boolean isDM() {
        return (titles != null && titles.contains("DM"));
    }

    /**
     * true if the user is an ICC helper.
     * @return true if the user is an ICC helper.
     */
    public boolean isH() {
        return (titles != null && titles.contains("H"));
    }

    /**
     * true if the user is an ICC helper.
     * @return true if the user is an ICC helper.
     */
    public boolean isHelper() {
        return isH();
    }

    /**
     * For sorting. It assigned a numeric value to each FIDE title so that we can determine
     * title-wise who should have a general upper hand in a game.
     * @return The numerical collation value of a user based on their highest FIDE title.
     */
    private int titleValue() {
        if(titles == null)
            return 0;
        
        if (isGM()) {
            return 6;
        } else if (isIM()) {
            return 5;
        } else if (isFM()) {
            return 4;
        } else if (isWGM()) {
            return 4;
        } else if (isWIM()) {
            return 3;
        } else if (isWFM()) {
            return 2;
        }
        return 0;
    }

    /**
     * Comparison function that compares one set of titles to another set of titles.
     * <p> We follow the standard return. -1 if this < other, 1 if this > other, else 0. 
     * @param other The other {@link Titles} object.
     * @return -1 if this < other, 1 if this > other, else 0.
     */
    public int compareFideTitle(final Titles other) {
        int us = titleValue();
        int them = other.titleValue();
        if (us < them) {
            return -1;
        }
        if (us == them) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public String toString() {
        if(titles == null || titles.size() == 0)
            return "";
        String ret = "(";
        String space = "";
        for(String t : titles) {
            ret += space + t;
            space = " ";
        }
        ret += ")";
        return ret;
    }
    
    public void addTitle(String title) {
        if(titles == null)
            titles = new ArrayList<String>();
        if(!titles.contains(title))
            titles.add(title);
    }
    public void removeTitle(String title) {
        if(titles != null)
            titles.remove(title);
    }
}
