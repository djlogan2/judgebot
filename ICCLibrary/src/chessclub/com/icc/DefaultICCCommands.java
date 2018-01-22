package chessclub.com.icc;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chessclub.com.icc.handler.interfaces.IAbstractICCHandler;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;
import david.logan.chess.support.GameScore;
import david.logan.chess.support.Move;

/**
 * The various ICC commands.
 * @author David Logan
 *
 */
public class DefaultICCCommands {

    // We may put these in at some point.
    // throw new UnsupportedOperationException("Not supported yet.");
    private static final Logger LOG = LogManager.getLogger(DefaultICCCommands.class);
    private ICCInstance icc = null;
    private IAbstractICCHandler handler;
    private String prefix;

    /**
     * This sets the ICC logon framework instance. When we send commands, we will send commands to
     * this ICC instance.
     * @param pIcc  The ICCInstance.
     * @param handler The AbstractICCHandler instance that gets the results of these commands
     */
    public void setInstance(final ICCInstance pIcc, IAbstractICCHandler handler) {
        icc = pIcc;
        this.handler = handler;
    }
    public void setInstance(ICCInstance pIcc, String prefix) {
        icc = pIcc;
        this.prefix = prefix;
    }
    
    public void setHandler(IAbstractICCHandler handler) {
        this.handler = handler;
    }

    /**
     * The ICC Instance we are using.
     * @return  The ICC Instance we are using.
     */
    public ICCInstance getInstance() {
        return icc;
    }
    
    /**
     * The ICC handler that gets the responses from the commands we issue from this class instance
     * @return  The ICC handler that gets command results.
     */
    public IAbstractICCHandler getHandler() {
        return handler;
    }
    /**
     * Sends data to ICC. It will prefix the command with the hash code of the caller so that the
     * framework can send the response back to the right handler instance. The format of the sent
     * command will be `12345678`command.
     * The Level 1 packet coming back will have '12345678' in it. All registered handlers in the framework
     * are registered by their hash code. Responses to commands will only go to the handler that issued
     * the command. 
     * @param cmd   The command to be sent.
     */
    protected void send(final String cmd) {
        if(prefix != null) {
            icc.send(prefix, cmd);
        } else if(handler != null)
            icc.send(Integer.toString(handler.hashCode()), cmd);
        else
            icc.send(cmd);
    }
    
    /**
     * Sends a command to ICC in order to get a FEN string for a particular game number.
     * @param gamestring    An ICC game number (number or #number)
     * @see AbstractICCHandler#inSfen(Sfen)
     */
    public void sfen(final String gamestring) {
        send("sfen " + gamestring);
    }

    /**
     * Sends a command to ICC in order to get a FEN string for a particular game number.
     * @param gamestring    An ICC game number (number or #number)
     * @see AbstractICCHandler#inSfen(Sfen)
     */
    public void sfen(final int gamenumber) {
        send("sfen #" + gamenumber);
    }

    /**
     * Sends a comment to get the list of stored (adjourned) games for the listed user.
     * @param userid    The user for which to get the list of stored games for
     * @see AbstractICCHandler#gameList(GameList)
     */
    public void stored(final String userid) {
        send("stored " + userid);
    }
    public void stored() { // Obviously we need to be able to get ours as well
    	send("stored");
    }

    /**
     * Examines a game by game number or game id.
     * @param examineId A numeric game number or a game id
     */
    public void examine(final String examineId) {
        send("examine " + examineId);
    }
    /**
     * Sends a command to the ICC to stop examining whatever game is currently being examined.
     */
    public void unexamine() {
        send("unexamine");
    }

    /**
     * Sends a command to the ICC to move the currently examined game forward a certain number of moves.
     * @param movecount The number of (half) moves to move the examined game forward.
     */
    public void forward(final int movecount) {
        send("forward " + movecount);
    }

    /**
     * Sends a command to the ICC to stop observing the currently observed default game.
     * Since you can be observing more than one game, this unobserves the "default" observed game.
     */
    public void unobserve() {
        send("unobserve");
    }

    /**
     * Sends a command to the ICC to stop observing the observed game being played the indicated user.
     * @param userid    The user we wish to stop observing.
     */
    public void unobserve(final String userid) {
        send("unobserve " + userid);
    }

    /**
     * Sends a command to the ICC to stop observing the observed game by a game number.
     * @param gamenumber    The the game number we wish to stop observing.
     */
    public void unobserve(final int gamenumber) {
        send("unobserve " + gamenumber);
    }

    /**
     * Sends a command to the ICC to send a kibitz to a game being played (by this player or an observed game).
     * @param text  The text to kibitz. This will be sent to the default game if there is more than one observed game.
     */
    public void kibitz(final String text) {
        send("kibitz " + text);
    }

    /**
     * Sends a command to the ICC to send a kibitz to a game being played (by this player or an observed game).
     * @param whichgame  Which game number to send the kibitz to
     * @param text  The text to kibitz. This will be sent to the default game if there is more than one observed game.
     */
    public void kibitz(final int whichgame, final String text) {
        send("kibitzto " + whichgame + " " + text);
    }

    /**
     * Sends a command to the ICC that sets whether this account should be logged out after 60 minutes of inactivity or not.
     * @param noa   If TRUE, this logged on user will never be logged out. If FALSE, this user will be logged out after 60 minutes of inactivity.
     */
    public void setNoAutologout(final boolean noa) {
        this.send("set noautologout " + (noa ? "1" : "0"));
    }

    /**
     * Sends a command to the ICC to tell another user or tell a channel something.
     * @param name  The name of the user or the channel number
     * @param what  What to say
     */
    public void xtell(final String name, final String what) {
        this.send("xtell " + name + "! " + what);
    }

    /**
     * Sends a command to the ICC to send a message to a user.
     * @param name  The name of the user
     * @param what  The message to leave
     */
    public void message(final String name, final String what) {
        this.send("message " + name + "! " + what);
    }

    /**
     * Observes a user playing a game.
     * @param user  The user.
     */
    public void observe(final String user) {
        send("observe " + user + "!");
    }

    /**
     * Observes a game by game number.
     * @param gamenumber    The game number.
     */
    public void observe(final int gamenumber) {
        send("observe " + gamenumber);
    }

    // observehighest() { send("observe *");
    //
    // "observe *" -- observes the "highest rated game" being played.
    // "observe *B-C" -- observes the highest-rated bullet game with
    // no computer. Uses codes from help games2.
    // protected void finalize() throws Throwable
    // {
    // if(log.isDebugEnabled())
    // log.debug("finalize removing myself: "+this.hashCode());
    // icc.removeHandler(this);
    // super.finalize();
    // }
    
    /**
     * Add a handler to the framework.
     * @param   handler The handler to add.
     */
    public void addHandler(final IAbstractICCHandler handler) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding handler " + handler.hashCode());
        }
        icc.addHandler(handler);
    }

    /**
     * Removes a handler from the ICC framework.
     * @param handler   The handler to remove.
     */
    public void removeHandler(final IAbstractICCHandler handler) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Removing handler " + handler.hashCode());
        }
        icc.removeHandler(handler);
    }

//    /**
//     * Ask ICC to start sending packets for every game that begins on the server. This will be a lot of packets.
//     * @param enable    True if we want to start getting packets for every game that starts and ends, including examined games.
//     */
//    protected void enableGameInfo(final boolean enable) {
//        icc.enableGameStartInfo(enable);
//    }

    /*
     * We have to call the framework because messages are maintained in an array in the framework.
     */
    /**
     * Clears a message on the ICC server.
     * @param index The message number to clear.
     */
    public void clearmessage(final int index) {
        icc.clearmessage(index);
    }

    /**
     * Send an ICC shout (regular shout).
     * @param message   The message. It will appear as "xxx shouted: message"
     */
    public void shout(final String message) {
        send("shout " + message);
    }

    /**
     * Send an ICC shout ("I" type shout).
     * @param message   The message. It will appear as "xxx (modified verb) (rest of message)"
     */
    public void iShout(final String message) {
        send("i " + message);
    }

    /**
     * Send an ICC "finger" command
     * @param who   The person for whom the finger data has been requested
     */
    public void finger(String who) {
        send("finger -a " + who);
    }

    public void getps(String user) {
        send("getps " + user + "!");
    }

    /**
     * Issues the TD-Only ICC "getpx" command.
     * @param handle    The user to issue it for
     * @param key       The required getpx key parameter. It is returned in the packet.
     */
    public void getpx(final String handle, final String key) {
        send("getpx " + handle + "! " + key);
    }

    public void leaveChannel(int channel) {
        send("minus channel "+channel);
    }

    public void addChannel(int channel) {
        send("plus channel "+channel);
    }
    public void history(String user) {
        send("history "+user+"!");
    }
    public void personalinfo() {
        personalinfo(null);
    }
    public void personalinfo(String user) {
        if(user == null)
            send("personal-info");
        else
            send("personal-info "+user+"!");
    }
    public void liblist() {
        send("liblist");
    }
    public void liblist(String user) {
        send("liblist "+user);
    }

    public void libkeepexam()
    {
        send("libkeepexam");
    }
    public void libkeepexam(String whitename, String blackname, GameScore outcome, int libnumber)
    {
        String command = "libkeepexam " + whitename + " " + blackname + " ";
        switch(outcome) {
            case WHITE_WINS:
                command += "+ ";
                break;
            case BLACK_WINS:
                command += "- ";
                break;
            case DRAW:
            case ABORTED:
                command += "= ";
                break;
            case ADJOURNED:
                command += "* ";
                break;
        }
        command += "%";
        command += Integer.toString(libnumber);
        send(command);
    }
    
    public void settag(String tag, String value)
    {
        send("tag " + tag + " " + value);
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public void spgn(int id) {
        send("spgn #"+id);
    }
    
    public void seek(int time, int inc) throws Exception {
        seek(time, inc, true, 0, Color.NONE, true, 0, 0);
    }
    
    public void seek(int time, int inc, boolean rated, int wild, Color color, boolean auto, int minRating, int maxRating) throws Exception {
        String seek = "seek ";
        
        if(time <= 0)
            throw new Exception("Time cannot be zero or negative in a seek: time="+time);
        seek += Integer.toString(time);
        
        if(inc < 0)
            throw new Exception("Increment cannot be negative in a seek: inc="+inc);
        seek += " " + Integer.toString(inc);
        
        seek += " " + (rated ? 'r' : 'u');
        
        if(wild < 0 || wild > 29)
            throw new Exception("Wild must be 0 <= wild <= 29, but was "+wild);
        
        seek += " w"+ Integer.toString(wild);
        
        switch(color) {
        case WHITE: seek += " white"; break;
        case BLACK: seek += " black"; break;
        case NONE: break;
        }
        
        seek += " " + (auto ? "auto" : "manual");
        
        if(minRating < 0 || minRating > maxRating) throw new Exception("Minimum rating must be >= 0 and <= maxRating");
        // I'll do this for completeness, but the previous if should handle all problems with maxRating already
        if(maxRating < 0 || maxRating < minRating) throw new Exception("Maximum rating must be >= 0 and >= minRating");
        if(minRating != 0 || maxRating != 0) {
            seek += " "+Integer.toString(minRating)+"-"+Integer.toString(maxRating);
        }
        
        send(seek);
    }
    
    public void unseek(int index) {
        send("unseek "+index);
    }
    
    public void loadfen(String fen) {
        send("loadfen "+fen);
    }
    
    public void simulloadfen(int gamenumber, String fen) {
        send(";goto " + gamenumber + "; loadfen "+fen);
    }
    
    public void loadfen(Fen fen) {
        loadfen(fen.getFEN());
    }
    
    public void simulloadfen(int gamenumber, Fen fen) {
        simulloadfen(gamenumber, fen.getFEN());
    }
    
    public void simulize() {
        send("simulize");
    }
    
    public void simulabort(int gamenumber) {
        send(";goto " + gamenumber +"; abort");
    }
    
    public void simulresign(int gamenumber) {
        send(";goto " + gamenumber +"; resign");
    }
    public void resign(String username) { // Resign a stored game
    	send("resign " + username);
    }
    
    public void simulmove(int gamenumber, Move move) throws Exception {
        String moveStr = move.getFromSquare().toString() + move.getToSquare().toString();
        switch(move.getQueeningPiece()) {
        case QUEEN: moveStr += "=q"; break;
        case BISHOP: moveStr += "=b"; break;
        case ROOK: moveStr += "=r"; break;
        case KNIGHT: moveStr += "=n"; break;
        default:
            break;
        }
        send(";goto " + gamenumber +"; " + moveStr);
    }
    
    public void makemove(Move move) throws Exception {
        String moveStr = move.getFromSquare().toString() + move.getToSquare().toString();
        switch(move.getQueeningPiece()) {
        case QUEEN: moveStr += "=q"; break;
        case BISHOP: moveStr += "=b"; break;
        case ROOK: moveStr += "=r"; break;
        case KNIGHT: moveStr += "=n"; break;
        default:
            break;
        }
        send(moveStr);
    }
    
    public void accept(String opponent) {
        send("accept " + opponent);
    }
    
    public void takeback(int howmany) {
        send("takeback " + howmany);
    }
    
    public void simultakeback(int gamenumber, int howmany) {
        send(";goto " + gamenumber + "; takeback " + howmany);
    }
    
    public void arrow(Move move) {
        send("arrow " + move.getFromSquare().toString() + " " + move.getToSquare().toString());
    }
    
    public void simularrow(int gamenumber, Move move) {
        send(";goto " + gamenumber + ";arrow " + move.getFromSquare().toString() + " " + move.getToSquare().toString());
    }
    
    public void listRemoveAlias(String string) {
        send("minus alias " + string + " *");
    }
    
    public void listAddAlias(String string, String command) {
        send("plus alias " + string + " " + command);
    }
    public void listCensor(String user, boolean doit) {
        send((doit ? "plus" : "minus") + " censor " + user);
    }
    public void listNotify(String user, boolean notify) {
        send((notify ? "plus" : "minus") + " notify " + user);
    }
    public void listGnotify(String user, boolean gnotify) {
        send((gnotify ? "plus" : "minus") + " gnotify " + user);
    }
    public void censor(String user) {
        send("+censor " + user);
    }
}
