package chessclub.com.icc;

import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.GameAdjudicated;
import chessclub.com.icc.l1.Getpx;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.LoggedOut;
import chessclub.com.icc.l1.SEvent;
import chessclub.com.icc.l1.Sfen;
import chessclub.com.icc.l1.ShoutResponse;
import chessclub.com.icc.l1.Spoof;
import chessclub.com.icc.l1l2.GameList;
import chessclub.com.icc.l2.ChannelQTell;
import chessclub.com.icc.l2.ChannelTell;
import chessclub.com.icc.l2.ChannelsShared;
import chessclub.com.icc.l2.Command;
import chessclub.com.icc.l2.GameNumber;
import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;
import chessclub.com.icc.l2.L2MoveList;
import chessclub.com.icc.l2.LoginFailed;
import chessclub.com.icc.l2.MessageListItem;
import chessclub.com.icc.l2.Msec;
import chessclub.com.icc.l2.MyNotifyList;
import chessclub.com.icc.l2.MyRating;
import chessclub.com.icc.l2.MyVariable;
import chessclub.com.icc.l2.Partnership;
import chessclub.com.icc.l2.PersonInMyChannel;
import chessclub.com.icc.l2.PersonalTell;
import chessclub.com.icc.l2.PersonalTellEcho;
import chessclub.com.icc.l2.PlayerArrived;
import chessclub.com.icc.l2.PlayerStatePacket;
import chessclub.com.icc.l2.Seek;
import chessclub.com.icc.l2.SeekRemoved;
import chessclub.com.icc.l2.SendMoves;
import chessclub.com.icc.l2.Shout;
import chessclub.com.icc.l2.Tourney;
import chessclub.com.icc.l2.TourneyGameEnded;
import chessclub.com.icc.l2.TourneyGameStarted;
import chessclub.com.icc.l2.WebAuth;
import chessclub.com.icc.l2.WhoAmI;
import chessclub.com.icc.l2.WildKey;

/**
 *
 * @author David Logan
 *  <p>
 *         The AbstractICCHandler interface contains all of the methods for all of the packets handled by the ICC runtime thread. Every incoming packet is parsed and put into a packet, and then the subsequent handler method is invoked.
 *         If you want to handle ICC packets, implement this interface, or extend from the DefaultICCHandler class.
 * 
 */
public interface AbstractICCHandler {

    /**
     * This method gets called by the framework after the ICC instance connections have been established.
     * At this point, the handler is able to make all necessary ICC setup calls.
     */
    void initHandler();

    /**
     * This method gets called by the framework if we have any errors to report.
     * @param   ex   The {@link ICCException}. 
     */
    void iccException(ICCException ex);
    
    /**
     * Return true if you want your handler to send you generic level 1 and level 2 packets.
     * If the handler does not return true, you will not get generic packet data.
     * Generic packets are the spurious packets sent to you from the server.
     * This includes any packet that is NOT in response to a command (and would not have a command prefix associated with it.)
     * @return  true if you want generic packets, false if you don't.
     */
    boolean acceptGenericPackets();

    /**
     * This method is called by the framework to set the ICC instance this handler is tied to.
     * @param icc   The {@link ICCInstance} this handler is tied to.
     */
    void setInstance(ICCInstance icc);

    /**
     * Called when a users login fails.
     * @param p The {@link LoginFailed} packet data with the failed login information.
     */
    void loginFailed(LoginFailed p);

    /**
     * Called when a user successfully logs on.
     * @param p The {@link WhoAmI} data in sent packet
     */
    void whoAmI(WhoAmI p);

    /**
     * Called when a player arrives to ICC.
     * @param name  The name of the userid of the person arriving
     */
    void playerArrivedSimple(String name);

    /**
     * Called when a user is added or removed from the notify list. When first logging on,
     * all users are "added" via this call.
     * @param p The user/add/remove data in a {@link MyNotifyList} instance.
     */
    void myNotifyList(MyNotifyList p);

    void tourneyGameEnded(TourneyGameEnded p);

    void myVariable(MyVariable p);

    void playerArrived(PlayerArrived p);

    void notifyArrived(PlayerArrived p);

    void myRating(MyRating p);

    void keyVersion(int version);

    void wildKey(WildKey p);

    void webAuth(WebAuth p);

    void channelsShared(ChannelsShared p);

    void playerLeft(String name);

    void personInMyChannel(PersonInMyChannel p);

    void seek(Seek p);

    void seekRemoved(SeekRemoved p);

    void tourney(Tourney p);

    void channelTell(ChannelTell p);

    void tourneyGameStarted(TourneyGameStarted p);

    void removeTourney(int index);

    void incomingShout(Shout p);

    void personalTell(PersonalTell p);

    void notifyLeft(String name);

    void sEvent(SEvent p);

    void playerState(PlayerStatePacket p);

    void partnership(Partnership p);

    void command(Command p);

    void spoof(Spoof p);

    void notifyPlayerState(PlayerStatePacket p);

    void channelQTell(ChannelQTell p);

    void personalTellEcho(PersonalTellEcho p);

    void connectionClosed();

    //void message(Message p);
    void newMessage(MessageListItem p);
    
    void badCommand1(BadCommand p);

    void badCommand2(L1ErrorOnly p);

    void adminOnDuty();

    void adminOffDuty();

    void getpx(Getpx p);

    void gameResult(GameResultPacket p);

    void myGameResult(GameResultPacket p);

    void gameStarted(GameStarted p);

    void myGameStarted(GameStarted p);

    void moveList(L2MoveList p);

    void msec(Msec p);

    void sendMoves(SendMoves p);

    void inSfen(Sfen p);

    void examinedGameIsGone(GameNumber p);

    void stopObserving(GameNumber p);

    void refresh(GameNumber p);

    void myTurn(GameNumber p);

    void myGameEnded(GameNumber p);

    void gameList(GameList p);

    void startedObserving(GameStarted p);

    void shuttingDown();

    void gameAdjudicated(GameAdjudicated p);
    
    /**
     * Called when the user is logged out of ICC. It contains logout messages as well as any
     * reason for the logout.
     * @param l The packet.
     */
    void loggedOut(LoggedOut l);
    
    /**
     * Called when the user sends a shout. This packet contains the result of that command.
     * @param r The {@link  ShoutResponse}packet.
     */
    void shoutResponse(ShoutResponse r);
}
