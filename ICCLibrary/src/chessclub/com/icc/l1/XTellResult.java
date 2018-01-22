package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XTellResult extends Level1Packet {
    private String targetPerson;
    public static final int NORMAL = 0;
    public static final int YOUCENSORING = 1;
    public static final int BEINGCENSORED = 2;
    public static final int TRYINGTOCENSOR = 3;
    public static final int LOGGEDOFF = 4;
    public static final int MUZZLED = 5;
    public static final int QUIETPLAY = 6;
    
    private int status = NORMAL;
    private String awayMessage;
    private boolean parsed = false;
    
    public boolean nothing() {
        return (numberParms() == 1);
    }
    public String targetPerson() {
        if(!parsed) parse();
        return targetPerson;
    }
    public String awayMessage() {
        if(!parsed) parse();
        return awayMessage;
    }
    public int getStatus() {
        if(!parsed) parse();
        return status;
    }
    public boolean failed() {
        if(!parsed) parse();
        return (status == YOUCENSORING || status == BEINGCENSORED || status == LOGGEDOFF || status == MUZZLED || status == QUIETPLAY);
    }
    //    (told idjit)
    //    Not sent -- idjit is censoring you.
    //    Not sent -- You are censoring idjit.
    //    (told idjit, who is doing)
    //    Roncosbay is trying to censor you.
    //    (told Roncosbay, who has been idle for 2 minutes)
    //    (told Roncosbay, who is doing unspeakable things (idle 2 minutes))
    //    Roncosbay is not logged in.
    //    Sorry, you are not allowed to chat.
    //    Not sent -- chatt does not receive tells while playing.
    //---------------------------------------------------------------------------------------------
    // Because there is no way for me to differentiate between a server generated (idle ## minutes)
    // and a user entered one [like: "/away doing unspeakable things (idle 129383098 minutes)"]
    // I have no choice but to not parse out the idle time into a usable value.
    // I can only report on it as an away message.
    //---------------------------------------------------------------------------------------------
    //
    // Channels:
    // (told 4 people)
    // Your privilege to write in channels has been temporarily revoked.
    // 183 added.                                                     (xt to a channel we added due to the xt)
    // Submitted to 29 volunteer administrators and helpers.          (xt 1)
    // Submitted.                                                     (xt to another moderated channel)
    //
    private final static Pattern p1 = Pattern.compile("\\(told (\\S+)(, who (.*))?\\)");
    private final static Pattern p2 = Pattern.compile("Not sent -- (\\S+) is censoring you\\.");
    private final static Pattern p3 = Pattern.compile("Not sent -- You are censoring (\\S+)\\.");
    private final static Pattern p4 = Pattern.compile("(\\S+) is trying to censor you.");
    private final static Pattern p5 = Pattern.compile("(\\S+) is not logged in.");
    private final static Pattern p6 = Pattern.compile("Not sent -- (\\S+) does not receive tells while playing.");

    private void parse() {
        Matcher m;
        for( int x = 1 ; x < this.numberParms() ; x++) {
            m = p1.matcher(getParm(x));
            if(m.matches()) {
                targetPerson = m.group(1);
                awayMessage = m.group(3);
                continue;
            }
            m = p2.matcher(getParm(x));
            if(m.matches()) {
                targetPerson = m.group(1);
                status = BEINGCENSORED;
                continue;
            }
            m = p3.matcher(getParm(x));
            if(m.matches()) {
                targetPerson = m.group(1);
                status = YOUCENSORING;
                continue;
            }
            m = p4.matcher(getParm(x));
            if(m.matches()) {
                targetPerson = m.group(1);
                status = TRYINGTOCENSOR;
                continue;
            }
            m = p5.matcher(getParm(x));
            if(m.matches()) {
                targetPerson = m.group(1);
                status = LOGGEDOFF;
                continue;
            }
            m = p6.matcher(getParm(x));
            if(m.matches()) {
                targetPerson = m.group(1);
                status = QUIETPLAY;
                continue;
            }
            if("Sorry, you are not allowed to chat.".equals(getParm(x)))
                status = MUZZLED;
            // targetPerson is UNKNOWN of course
            else if("Your privilege to write in channels has been temporarily revoked.".equals(getParm(x)))
                status = MUZZLED;
        }
        parsed = true;
    }
}
