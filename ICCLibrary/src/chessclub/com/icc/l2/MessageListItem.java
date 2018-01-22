package chessclub.com.icc.l2;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * The class instance that gets created when a new message comes in.
 * <p>This could also be existing messages being read in for the first time as the framework
 * issues an ICC "messages" command after logon.
 * <p>Form: (index sender time date message)
 * @author David Logan
 *
 */
public class MessageListItem extends Level2Packet {
    /**
     * The index number of the message.
     * @return  The index number of the message.
     */
    public int index() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The person the message came from.
     * @return  The person the message came from.
     */
    public String sender() {
        return getParm(2);
    }

    /**
     * The date and time the message was sent.
     * @return  The date and time the message was sent.
     */
    public DateTime dateSent() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm dd-MMM-YY").withZone(DateTimeZone.forID("America/New_York"));
        return (formatter.parseDateTime(getParm(3) + " " + getParm(4))).withZone(DateTime.now().getZone());
    }

    /**
     * The text of the message.
     * @return  The text of the message.
     */
    public String message() {
        return getParm(5);
    }
}
