package chessclub.com.icc.l2;


/**
 * This is the packet that gets sent from the framework when we have a login failure.
 * @author David Logan
 *
 */
public class LoginFailed extends Level2Packet {

    /**
     * reason() == SHORT_USERID when a userid is not valid for ICC.
     */
    public static final int BAD_USERID = 4;
    /**
     * reason() == UNKNOWN_USERID when an invalid userid is used.
     */
    public static final int UNKNOWN_USERID = 6;
    /**
     * reason() == USER_BANNED when the users password is invalid.
     */
    public static final int USER_BANNED = 10;
    /**
     * reason() == INVALID_PASSWORD when the users password is invalid.
     */
    public static final int INVALID_PASSWORD = 11;
    /**
     * 1 To register, please connect to the main server (chessclub.com).
     * 2 Sorry, names may be at most 15 characters long. Try again.
     * 3 A name should be at least two characters long! Try again.
     * 4 Sorry, a name must begin with a letter and consist of letters and digits. Try again.
     * 5 <name> is a registered name. If it is yours, type the password.
     * 6 <name> is not a registered name. [and you gave a password]
     * 7 <name> is not a registered name. [n filtered]
     * 8 <name> is not a registered name. [but okay, hit return to enter]
     * 9 Try again. [user typed null password]
     * 10 Something is wrong.
     * 11 Invalid password.
     * 12 The administrators have banned you from using this chess server.
     * 13 Something is wrong.
     * 14 <name>, whose name matches yours, is already logged in. Sorry.
     * 15 StratLabs client trial expired.
     * 16 Sorry, but this server cannot process account renewals or new accounts. Please connect to the main ICC server, chessclub.com, in order to activate your membership
     * 17 The chess server is currently only admitting registered players.
     * 18 The chess server is currently only admitting registered players. [full, no unregs allowed on queue]
     * 19 The chess server is full. [quota]
     * 20 You have entered the queue. Your current position is %d.
     * 21 To register, please use our web page www.chessclub.com/register.
     * 22 The <name> account is restricted to using Blitzin and only on one particular computer.
     * 
     * @return The reason for the login failed message
     */
    public int reason() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The text reason that has been sent to the client from the server as part of the packet data.
     * 
     * @return The text reason
     */
    public String textReason() {
        return getParm(2).trim();
    }
}
