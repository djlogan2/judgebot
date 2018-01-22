package chessclub.com.icc.l2;


/**
 * The web authorization key value sent from the framework.
 * <p>(key url-prefix www-servers)
 * <p>Key is some 50-character random ID, like qIqJRSyyAXRXmFmsPhyyX7nFpiwksooR6MAXE7tc4laqYwQNLn
 * <p>url-prefix will probably be something like {ia/0a}, where the 0a part indicates version and chess server. (100 char max)
 * <p>www-servers will probably be either {*.chessclub.com|chessclub.com} or just {wwwc.chessclub.com}, and will be 100 characters max.
 * <p>Conceivably it will be a fancier pattern to allow for ports, e.g. maybe
 * {wwwc.chessclub.com|wwwc.chessclub.com:\d+} but that's not decided yet.
 * The idea is that when the user clicks on (or otherwise opens) a URL that looks
 * like https://wwwc.chessclub.com/update-email.pl
 * the client should probably open
 * https://wwwc.chessclub.com/ia/0a/qIqJRSyyAXRXmFmsPhyyX7nFpiwksooR6MAXE7tc4laqYwQNLn/update-email.pl
 * <p>You could state the rule as:
 * <p>If the URL is of the form https://<matching-server>/<whatever>
 * and <whatever> doesn't already begin /<url-prefix>, open instead
 * https://<matching-server>/<url-prefix>/<chess-server>/<session-key>/<whatever>
 * @author David Logan
 */
public class WebAuth extends Level2Packet {
    /**
     * The key value.
     * @return The key value.
     */
    public String key() {
        return getParm(1);
    }

    /**
     * The URL prefix.
     * @return The URL prefix
     */
    public String urlprefix() {
        return getParm(2);
    }

    /**
     * The list of servers this web key is valid for.
     * @return The list of servers this web key is valid for.
     */
    public String[] servers() {
        return getParm(3).split("|");
    }
}
