package chessclub.com.icc;

import java.net.InetAddress;
import java.util.Collection;

import chessclub.com.enums.AdjudicateAction;
import chessclub.com.enums.Rating;
import chessclub.com.icc.l1l2.Titles;

/**
 * This class provides the default ICC administrative commands.
 * @author David Logan
 *
 */
public class DefaultICCAdminCommands extends DefaultICCCommands {

    private String adminPassword = null;

    /**
     * Adds a user to the list of banned userids.
     * @param pUserid    The userid to ban
     */
    public void ban(final String pUserid) {
        send("+ban " + pUserid);
    }
    
    /**
     * Forwards an ICC message to another user.
     * @param whichOne  Which message number to forward
     * @param toWhom    The user to forward it to.
     */
    public void forwardMessage(final int whichOne, final String toWhom) {
        send("fmessage " + toWhom + "! " + whichOne);
    }

    /**
     * Issues the Admin-only ICC "getpx" command.
     * @param handle    The user to issue it for
     * @param key       The required getpx key parameter. It is returned in the packet.
     */
    public void getpx(final String handle, final String key) {
        send("getpx " + handle + "! " + key);
    }

    /**
     * Sends the ICC admin only command to return the users location.
     * @param handle    The user for which we wish to know the location.
     * @see IDontThinkWeAreHandlingThisYet
     */
    public void location(final String handle) {
        send("location " + handle + "!");
    }

    /**
     * Sets the admin password within the framework.
     * @see #onDuty(boolean)
     * @param password  The ICC administrator password for this account.
     */
    public void setAdminPassword(final String password) {
        this.adminPassword = password;
    }

    /**
     * Sends a command to the ICC that will set the administrator on duty or off duty.
     * On duty means that many admin-only commands can be used. 
     * On duty can only be set if {@link #setAdminPassword(String)} has been called.
     * @param on    True if we are to go on duty, false if not.
     */
    public void onDuty(final boolean on) {
        if (adminPassword == null || adminPassword.isEmpty()) {
            return;
        }
        if (!on) {
            send("ad");
        } else {
            send("ad " + adminPassword);
        }
    }

    /**
     * Sends a command to the ICC to issue a "qtell" to a user or a channel.
     * @param name  The name of the user or the channel number
     * @param what  What to say
     */
    public void qtell(final String name, final String what) {
        qtell(name, new String[] {what});
    }

    /**
     * Sends a command to the ICC to issue a "qtell" to a user or a channel.
     * @param name  The name of the user or the channel number
     * @param what  What to say
     */
    public void qtell(final String name, final String[] what) {
        String bigone = "";
        String nl = "";
        for (String s : what) {
            String es = s.replaceAll("\\\\", "\\\\\\\\");
            if (bigone.length() + es.length() > 260 ) { // 900) {
                this.send("qtell " + name + "! " + bigone);
                nl = "";
                bigone = "";
            }
            bigone += nl + es;
            nl = "\\n";
        }
        this.send("qtell " + name + "! " + bigone);
    }

    public void qtell(String name, Collection<String> what) {
        String bigone = "";
        String nl = "";
        for (String s : what) {
            String es = s.replaceAll("\\\\", "\\\\\\\\");
            if (bigone.length() + es.length() > 260 ) { // 900) {
                this.send("qtell " + name + "! " + bigone);
                nl = "";
                bigone = "";
            }
            bigone += nl + es;
            nl = "\\n";
        }
        this.send("qtell " + name + "! " + bigone);
    }
    /**
     * This is an <b>ADMIN</b> that sends a spoof ICC command in order to spoof a command to another logged on account.
     * @param who   The logged on user to receive the spoof.
     * @param what  The command you are going to force the user to execute.
     */
    public void spoof(final String who, final String what) {
        send("spoof " + who + "! " + what);
    }

    /**
     * Sends the <b>ADMIN</b> ICC "adjudicate" command in order to adjudicate a game.
     * @param p1    One player
     * @param p2    The other player
     * @param action    How to adjudicate the command.
     *                  <ul>
     *                  <li><b>WHITE</b>-Adjudicate in favor of the white player</li>
     *                  <li><b>BLACK</b>-Adjudicate in favor of the black player</li>
     *                  <li><b>DRAW</b>-Adjudicate as a draw</li>
     *                  <li><b>ABORT</b>-Adjudicate as an abort, not changing any rating points</li>
     *                  <li>FIRST-Unknown what this is. It is a valid adjudicate parameter.</li>
     *                  </ul>
     */
    public void adjudicate(final String p1, final String p2, final AdjudicateAction action) {
        send("adjudicate " + p1 + "! " + p2 + "! " + action.toString().toLowerCase());
    }

    public boolean haveAdminPassword() {
        return (adminPassword != null && adminPassword.length() != 0);
    }

    public void setOther(String who, String variable, String newValue) {
        send("set-other "+who+"! " + variable + " " + newValue);
    }

    public void changeBestRating(String who, Rating rating, int newRating) {
        send("rating "+who+"! best-"+rating.toString()+" "+newRating);
    }

    public void changeRating(String who, Rating rating, int newRating) {
        send("rating "+who+"! "+rating.toString()+" "+newRating);
    }
    @Override
    public void finger(String who) {
        send("finger "+who+"! -a 1-999999");
    }

    public void comment(String user, String comment) {
        send("comment "+user+" "+comment);
    }

    public void comment(String user, String[] commentLines) {
        String str = "";
        String slash = "";
        for(int x = 0 ; x < commentLines.length ; x++) {
            if(str.length() + slash.length() + commentLines[x].length() > 300) {
                comment(user, str);
                str = "";
                slash = "";
            }
            str += slash + commentLines[x];
            slash = "/";
        }
        if(str.length() > 0)
            comment(user, str);
    }

    private void _changeTitles(String user, boolean oldt, boolean newt, String list) {
        String operation;
        if(oldt != newt) {
            if(oldt) {
                operation = "minus";
            } else {
                operation = "plus";
            }
            send(operation + " " + list + " " + user);
        }
    }
    public void changeTitles(String user, Titles oldTitles, Titles newtitles) {
        _changeTitles(user, oldTitles.isC(), newtitles.isC(), "computer");
        _changeTitles(user, oldTitles.isDM(), newtitles.isDM(), "dm");
        _changeTitles(user, oldTitles.isFM(), newtitles.isFM(), "fm");
        _changeTitles(user, oldTitles.isGM(), newtitles.isGM(), "gm");
        _changeTitles(user, oldTitles.isIM(), newtitles.isIM(), "im");
        _changeTitles(user, oldTitles.isH(), newtitles.isH(), "helper");
        _changeTitles(user, oldTitles.isWFM(), newtitles.isWFM(), "wfm");
        _changeTitles(user, oldTitles.isWGM(), newtitles.isWGM(), "wgm");
        _changeTitles(user, oldTitles.isWIM(), newtitles.isWIM(), "wim");
    }
    
    public void listForce(String user, boolean doit) {
        send((doit ? "plus" : "minus") + " force " + user);
    }

    public void listMuzzle(String user, boolean doit) {
        send((doit ? "plus" : "minus") + " muzzle " + user);
    }
    
    public void listNorate(String user, boolean doit) {
        send((doit ? "plus" : "minus") + " norate " + user);
    }

    public void listNounrated(String user, boolean doit) {
        send((doit ? "plus" : "minus") + " nounrated " + user);
    }

    public void listPlayboth(String user, boolean doit) {
        send((doit ? "plus" : "minus") + " playboth " + user);
    }

    public void listSShout(String user, boolean prevent) {
        send((prevent ? "plus" : "minus") + " sshout " + user);
    }
    
    public void listBan(String user, boolean doit) {
        send((doit ? "plus" : "minus") + " ban " + user);
    }

    public void listExempt(String user, boolean exempt) {
        send((exempt ? "plus" : "minus") + " exempt " + user);
    }

    public void listAddIPNotify(InetAddress addr) {
        send("plus ipnot " + addr.getHostAddress());
    }
    public void listRemoveIPNotify(InetAddress addr) {
        send("minus ipnot " + addr.getHostAddress());
    }

    public void listNobest(String user, boolean nobest) {
        send((nobest ? "plus" : "minus") + " nobest " + user);
    }
    
    public void listPmuzzle(String user, boolean muzzle) {
        send((muzzle ? "plus" : "minus") + " pmuzzle " + user);
    }
    
    public void listKmuzzle(String user, boolean muzzle) {
        send((muzzle ? "plus" : "minus") + " kmuzzle " + user);
    }
    
    public void listComputer(String user, boolean computer) {
        send((computer ? "plus" : "minus") + " computer " + user);
    }
    
    public void listBuster(String user, boolean buster) {
        send((buster ? "plus" : "minus") + " buster " + user);
    }
}
