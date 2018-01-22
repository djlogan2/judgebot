/**
 * 
 */
package chessclub.com.icc;

import static org.junit.Assert.fail;
import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;          
import chessclub.com.icc.handler.interfaces.IAdminOnOffDuty;                
import chessclub.com.icc.handler.interfaces.IChannelQTell;                  
import chessclub.com.icc.handler.interfaces.IChannelTell;                   
import chessclub.com.icc.handler.interfaces.IChannelsShared;                
import chessclub.com.icc.handler.interfaces.ICommand;                       
import chessclub.com.icc.handler.interfaces.IFinger;                        
import chessclub.com.icc.handler.interfaces.IGameAdjudicated;               
import chessclub.com.icc.handler.interfaces.IGameList;                      
import chessclub.com.icc.handler.interfaces.IGameStartedResult;             
import chessclub.com.icc.handler.interfaces.IGetps;                         
import chessclub.com.icc.handler.interfaces.IGetpx;                         
import chessclub.com.icc.handler.interfaces.IHelperQuestions;               
import chessclub.com.icc.handler.interfaces.IKeyVersion;                    
import chessclub.com.icc.handler.interfaces.ILoggedOut;                     
import chessclub.com.icc.handler.interfaces.ILoginFailed;                   
import chessclub.com.icc.handler.interfaces.IManualAccept;                  
import chessclub.com.icc.handler.interfaces.IMoveList;                      
import chessclub.com.icc.handler.interfaces.IMyGame;           
import chessclub.com.icc.handler.interfaces.IMySeek;                        
import chessclub.com.icc.handler.interfaces.IMyTurn;                        
import chessclub.com.icc.handler.interfaces.IMyVariable;                    
import chessclub.com.icc.handler.interfaces.INewMessage;                    
import chessclub.com.icc.handler.interfaces.INewRegistration;               
import chessclub.com.icc.handler.interfaces.INotifyArrivedLeft;             
import chessclub.com.icc.handler.interfaces.INotifyList;                    
import chessclub.com.icc.handler.interfaces.INotifyPlayerState;             
import chessclub.com.icc.handler.interfaces.IObserving;                     
import chessclub.com.icc.handler.interfaces.IPartnership;                   
import chessclub.com.icc.handler.interfaces.IPersonInMyChannel;             
import chessclub.com.icc.handler.interfaces.IPersonalQTell;                 
import chessclub.com.icc.handler.interfaces.IPersonalTell;                  
import chessclub.com.icc.handler.interfaces.IPersonalTellEcho;              
import chessclub.com.icc.handler.interfaces.IPlayerArrived;                 
import chessclub.com.icc.handler.interfaces.IPlayerArrivedSimple;           
import chessclub.com.icc.handler.interfaces.IPlayerState;                   
import chessclub.com.icc.handler.interfaces.IRatingInfo;                    
import chessclub.com.icc.handler.interfaces.IRatingReset;                   
import chessclub.com.icc.handler.interfaces.IRefresh;                       
import chessclub.com.icc.handler.interfaces.ISEvent;                        
import chessclub.com.icc.handler.interfaces.ISFen;                          
import chessclub.com.icc.handler.interfaces.ISeek;                          
import chessclub.com.icc.handler.interfaces.IShout;                         
import chessclub.com.icc.handler.interfaces.IShoutResponse;                 
import chessclub.com.icc.handler.interfaces.IShowPassword;                  
import chessclub.com.icc.handler.interfaces.ISpgn;                          
import chessclub.com.icc.handler.interfaces.ISpoof;                         
import chessclub.com.icc.handler.interfaces.ITitlesChanged;                 
import chessclub.com.icc.handler.interfaces.ITourney;                       
import chessclub.com.icc.handler.interfaces.IUserComplained;                
import chessclub.com.icc.handler.interfaces.IWebAuth;                       
import chessclub.com.icc.handler.interfaces.IWhoAmI;                        
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.Complain;
import chessclub.com.icc.l1.FingerNotes;
import chessclub.com.icc.l1.GameAdjudicated;
import chessclub.com.icc.l1.Getps;
import chessclub.com.icc.l1.Getpx;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.LoggedOut;
import chessclub.com.icc.l1.Play;
import chessclub.com.icc.l1.RatingChanged;
import chessclub.com.icc.l1.Register;
import chessclub.com.icc.l1.SEvent;
import chessclub.com.icc.l1.SPgn;
import chessclub.com.icc.l1.Seeking;
import chessclub.com.icc.l1.Sfen;
import chessclub.com.icc.l1.ShoutResponse;
import chessclub.com.icc.l1.ShowPassword;
import chessclub.com.icc.l1.Spoof;
import chessclub.com.icc.l1.Unseeking;
import chessclub.com.icc.l1l2.GameList;
import chessclub.com.icc.l1l2.HelpQuestion;
import chessclub.com.icc.l1l2.HelpQuestionModified;
import chessclub.com.icc.l2.ChannelQTell;
import chessclub.com.icc.l2.ChannelTell;
import chessclub.com.icc.l2.ChannelsShared;
import chessclub.com.icc.l2.Command;
import chessclub.com.icc.l2.GameNumber;
import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;
import chessclub.com.icc.l2.LoginFailed;
import chessclub.com.icc.l2.MessageListItem;
import chessclub.com.icc.l2.L2MoveList;
import chessclub.com.icc.l2.Msec;
import chessclub.com.icc.l2.MyNotifyList;
import chessclub.com.icc.l2.MyRating;
import chessclub.com.icc.l2.MyVariable;
import chessclub.com.icc.l2.OffersInMyGame;
import chessclub.com.icc.l2.Partnership;
import chessclub.com.icc.l2.PersonInMyChannel;
import chessclub.com.icc.l2.PersonalQTell;
import chessclub.com.icc.l2.PersonalTell;
import chessclub.com.icc.l2.PersonalTellEcho;
import chessclub.com.icc.l2.PlayerArrived;
import chessclub.com.icc.l2.PlayerStatePacket;
import chessclub.com.icc.l2.Seek;
import chessclub.com.icc.l2.SeekRemoved;
import chessclub.com.icc.l2.SendMoves;
import chessclub.com.icc.l2.Shout;
import chessclub.com.icc.l2.TakeBackOccurred;
import chessclub.com.icc.l2.TitleChange;
import chessclub.com.icc.l2.Tourney;
import chessclub.com.icc.l2.TourneyGameEnded;
import chessclub.com.icc.l2.TourneyGameStarted;
import chessclub.com.icc.l2.WebAuth;
import chessclub.com.icc.l2.WhoAmI;
import chessclub.com.icc.l2.WildKey;

/**
 * 
 * @author David Logan
 * 
 *         Test class
 * 
 */
public class FailEverything extends DefaultICCCommands implements 
IAcceptGenericPackets
,IAdminOnOffDuty
,IChannelQTell
,IChannelTell
,IChannelsShared
,ICommand
,IFinger
,IGameAdjudicated
,IGameList
,IGameStartedResult
,IGetps
,IGetpx
,IHelperQuestions
,IKeyVersion
,ILoggedOut
,ILoginFailed
,IManualAccept
,IMoveList
,IMyGame
,IMySeek
,IMyTurn
,IMyVariable
,INewMessage
,INewRegistration
,INotifyArrivedLeft
,INotifyList
,INotifyPlayerState
,IObserving
,IPartnership
,IPersonInMyChannel
,IPersonalQTell
,IPersonalTell
,IPersonalTellEcho
,IPlayerArrived
,IPlayerArrivedSimple
,IPlayerState
,IRatingInfo
,IRatingReset
,IRefresh
,ISEvent
,ISFen
,ISeek
,IShout
,IShoutResponse
,IShowPassword
,ISpgn
,ISpoof
,ITitlesChanged
,ITourney
,IUserComplained
,IWebAuth
,IWhoAmI
 {
    public void initHandler() {
    }

    public void iccException(final ICCException ex) {
        fail("Method should not have been called");
    }

    public void loginFailed(final LoginFailed p) {
        fail("Method should not have been called");
    }

    public void whoAmI(final WhoAmI p) {
        fail("Method should not have been called");
    }

    public void playerArrivedSimple(final String name) {
        fail("Method should not have been called");
    }

    public void myNotifyList(final MyNotifyList p) {
        fail("Method should not have been called");
    }

    public void tourneyGameEnded(final TourneyGameEnded p) {
        fail("Method should not have been called");
    }

    public void myVariable(final MyVariable p) {
        fail("Method should not have been called");
    }

    public void playerArrived(final PlayerArrived p) {
        fail("Method should not have been called");
    }

    public void notifyArrived(final PlayerArrived p) {
        fail("Method should not have been called");
    }

    public void myRating(final MyRating p) {
        fail("Method should not have been called");
    }

    public void keyVersion(final int version) {
        fail("Method should not have been called");
    }

    public void wildKey(final WildKey p) {
        fail("Method should not have been called");
    }

    public void webAuth(final WebAuth p) {
        fail("Method should not have been called");
    }

    public void channelsShared(final ChannelsShared p) {
        fail("Method should not have been called");
    }

    public void playerLeft(final String name) {
        fail("Method should not have been called");
    }

    public void personInMyChannel(final PersonInMyChannel p) {
        fail("Method should not have been called");
    }

    public void seek(final Seek p) {
        fail("Method should not have been called");
    }

    public void seekRemoved(final SeekRemoved p) {
        fail("Method should not have been called");
    }

    public void tourney(final Tourney p) {
        fail("Method should not have been called");
    }

    public void channelTell(final ChannelTell p) {
        fail("Method should not have been called");
    }

    public void tourneyGameStarted(final TourneyGameStarted p) {
        fail("Method should not have been called");
    }

    public void removeTourney(final int index) {
        fail("Method should not have been called");
    }

    public void incomingShout(final Shout p) {
        fail("Method should not have been called");
    }

    public void personalTell(final PersonalTell p) {
        fail("Method should not have been called");
    }

    public void notifyLeft(final String name) {
        fail("Method should not have been called");
    }

    public void sEvent(final SEvent p) {
        fail("Method should not have been called");
    }

    public void playerState(final PlayerStatePacket p) {
        fail("Method should not have been called");
    }

    public void partnership(final Partnership p) {
        fail("Method should not have been called");
    }

    public void command(final Command p) {
        fail("Method should not have been called");
    }

    public void spoof(final Spoof p) {
        fail("Method should not have been called");
    }

    public void notifyPlayerState(final PlayerStatePacket p) {
        fail("Method should not have been called");
    }

    public void channelQTell(final ChannelQTell p) {
        fail("Method should not have been called");
    }

    public void personalTellEcho(final PersonalTellEcho p) {
        fail("Method should not have been called");
    }

    public void connectionClosed() {
        fail("Method should not have been called");
    }

    public void newMessage(final MessageListItem p) {
        fail("Method should not have been called");
    }

    public void adminOnDuty() {
        fail("Method should not have been called");
    }

    public void adminOffDuty() {
        fail("Method should not have been called");
    }

    public void getpx(final Getpx p) {
        fail("Method should not have been called");
    }

    public void gameResult(final GameResultPacket p) {
        fail("Method should not have been called");
    }

    public void myGameResult(final GameResultPacket p) {
        fail("Method should not have been called");
    }

    public void gameStarted(final GameStarted p) {
        fail("Method should not have been called");
    }

    public void myGameStarted(final GameStarted p) {
        fail("Method should not have been called");
    }

    public void moveList(final L2MoveList p) {
        fail("Method should not have been called");
    }

    public void msec(final Msec p) {
        fail("Method should not have been called");
    }

    public void sendMoves(final SendMoves p) {
        fail("Method should not have been called");
    }

    public void inSfen(final Sfen p) {
        fail("Method should not have been called");
    }

    public void examinedGameIsGone(final GameNumber p) {
        fail("Method should not have been called");
    }

    public void stopObserving(final GameNumber p) {
        fail("Method should not have been called");
    }

    public void refresh(final GameNumber p) {
        fail("Method should not have been called");
    }

    public void myTurn(final GameNumber p) {
        fail("Method should not have been called");
    }

    public void myGameEnded(final GameNumber p) {
        fail("Method should not have been called");
    }

    public void gameList(final GameList p) {
        fail("Method should not have been called");
    }

    public void startedObserving(final GameStarted p) {
        fail("Method should not have been called");
    }

    public void shuttingDown() {
        fail("Method should not have been called");
    }

    public void gameAdjudicated(final GameAdjudicated p) {
        fail("Method should not have been called");
    }

    public void loggedOut(final LoggedOut l) {
        fail("Method should not have been called");
    }

    public void shoutResponse(final ShoutResponse r) {
        fail("Method should not have been called");
    }

    public void newRegistration(Register l1) {
        fail("Method should not have been called");
    }

    public void setInstance(ICCInstance icc) {
        fail("Method should not have been called");
    }

    public void newHelperQuestion(HelpQuestion q) {
        fail("Method should not have been called");
    }

    public void personalQTell(PersonalQTell p) {
        fail("Method should not have been called");
    }

    public void UserComplained(Complain p) {
        fail("Method should not have been called");
    }

    public void titlesChanged(TitleChange p) {
        fail("Method should not have been called");
    }

    public void showPassword(ShowPassword l1) {
        fail("Method should not have been called");
    }

    public void finger(FingerNotes f) {
        fail("Method should not have been called");
    }

    public void handlingHelperQuestion(HelpQuestionModified p) {
        fail("Method should not have been called");
    }

    public void removingHelperQuestion(HelpQuestionModified p) {
        fail("Method should not have been called");
    }

    public void ratingReset(RatingChanged p) {
        fail("Method should not have been called");
    }

    public void getps(Getps p) {
        fail("Method should not have been called");
    }

    public void inSPgn(SPgn p) {
        fail("Method should not have been called");
    }

    public void mySeek(Seeking p) {
        fail("Method should not have been called");
    }

    public void mySeekRemoved(Unseeking p) {
        fail("Method should not have been called");
    }

    public void manualAccept(Play p) {
    }

    public void Error(chessclub.com.icc.l2.Error error) {
        fail("Method should not have been called");
    }

    public void badCommand1(BadCommand p) {
        fail("Method should not have been called");
    }

    public void badCommand2(L1ErrorOnly p) {
        fail("Method should not have been called");
    }

	@Override
	public void takeBackOccurred(TakeBackOccurred p) {
        fail("Method should not have been called");
	}

	@Override
	public void newOffers(OffersInMyGame p) {
        fail("Method should not have been called");
	}
}
