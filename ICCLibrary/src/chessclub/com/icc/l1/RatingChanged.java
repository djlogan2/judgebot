/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chessclub.com.enums.Rating;


/**
 * Instance returned from the framework when a "rating" command is issued.
 * @author David Logan
 */
public class RatingChanged extends Level1Packet {
    private boolean best = false;
    private Rating type = null;
    private int oldrating;
    private int newrating;
    private String user;
    private boolean error = false;
    public boolean isError() {
        return error;
    }
    public boolean isBest() {
        if(type == null)
            parse();
        return best;
    }
    public Rating getRatingType() {
        if(type == null)
            parse();
        return type;
    }
    public String user() {
        if(type == null)
            parse();
        return user;
    }
    public int oldRating() {
        if(type == null)
            parse();
        return oldrating;
    }
    public int newRating() {
        if(type == null)
            parse();
        return newrating;
    }
    private void parse() {
        if(numberParms() != 3) {
            error = true;
            return;
        }
        //Comment for idjit: djlogan (16:57 03-Jan-13 EST): Changed best 5-minute rating from 1119 to 1118^M
        //best 5-minute rating of idjit changed from 1119 to 1118.^M
        Pattern p = Pattern.compile("(best\\s+)?(\\S+)\\s+rating of\\s+(\\S+)\\s+changed from\\s+(\\d+)\\s+to\\s+(\\d+)\\.");
        Matcher m = p.matcher(getParm(2));
        if(!m.matches()) {
            error = true;
            return;
        }
        if(m.group(1) != null)
            best = true;
        type = Rating.fromString(m.group(2));
        user = m.group(3);
        oldrating = Integer.parseInt(m.group(4));
        newrating = Integer.parseInt(m.group(5));
        //Comment for idjit: djlogan (12:18 03-Jan-13 EST): Changed blitz rating from 1112 to 1113^M
        //blitz rating of idjit changed from 1112 to 1113.
    }
}
