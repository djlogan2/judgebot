package chessclub.com.icc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chessclub.com.enums.CN;
import chessclub.com.enums.ICCError;
import chessclub.com.enums.L2;
import chessclub.com.enums.Rating;
import chessclub.com.icc.handler.interfaces.IAbstractICCHandler;
import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.handler.interfaces.IAdminOnOffDuty;
import chessclub.com.icc.handler.interfaces.IChannelQTell;
import chessclub.com.icc.handler.interfaces.IChannelTell;
import chessclub.com.icc.handler.interfaces.IChannelsShared;
import chessclub.com.icc.handler.interfaces.ICommand;
import chessclub.com.icc.handler.interfaces.IDBMachines;
import chessclub.com.icc.handler.interfaces.IDBUsers;
import chessclub.com.icc.handler.interfaces.IFinger;
import chessclub.com.icc.handler.interfaces.IGameAdjudicated;
import chessclub.com.icc.handler.interfaces.IGameList;
import chessclub.com.icc.handler.interfaces.IGameStartedResult;
import chessclub.com.icc.handler.interfaces.IGetps;
import chessclub.com.icc.handler.interfaces.IGetpx;
import chessclub.com.icc.handler.interfaces.IHelperQuestions;
import chessclub.com.icc.handler.interfaces.IKeyVersion;
import chessclub.com.icc.handler.interfaces.IListChanged;
import chessclub.com.icc.handler.interfaces.ILocation;
import chessclub.com.icc.handler.interfaces.ILoggedOut;
import chessclub.com.icc.handler.interfaces.ILoginFailed;
import chessclub.com.icc.handler.interfaces.ILogons;
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
import chessclub.com.icc.l1.DBMachines;
import chessclub.com.icc.l1.DBUsers;
import chessclub.com.icc.l1.FingerNotes;
import chessclub.com.icc.l1.GameAdjudicated;
import chessclub.com.icc.l1.Getps;
import chessclub.com.icc.l1.Getpx;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.Level1Packet;
import chessclub.com.icc.l1.ListChanged;
import chessclub.com.icc.l1.Location;
import chessclub.com.icc.l1.LoggedOut;
import chessclub.com.icc.l1.Logons;
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
import chessclub.com.icc.l1.Unrelayed;
import chessclub.com.icc.l1.Unseeking;
import chessclub.com.icc.l1.XTellResult;
import chessclub.com.icc.l1l2.GameList;
import chessclub.com.icc.l1l2.HelpQuestion;
import chessclub.com.icc.l1l2.HelpQuestionModified;
import chessclub.com.icc.l2.ChannelQTell;
import chessclub.com.icc.l2.ChannelTell;
import chessclub.com.icc.l2.ChannelsShared;
import chessclub.com.icc.l2.Command;
import chessclub.com.icc.l2.GameListItem;
import chessclub.com.icc.l2.GameNumber;
import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;
import chessclub.com.icc.l2.GamelistBegin;
import chessclub.com.icc.l2.Level2Packet;
import chessclub.com.icc.l2.Level2Settings;
import chessclub.com.icc.l2.LoginFailed;
import chessclub.com.icc.l2.MessageListItem;
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
import chessclub.com.icc.l2.RatingTypeKey;
import chessclub.com.icc.l2.Seek;
import chessclub.com.icc.l2.SeekRemoved;
import chessclub.com.icc.l2.SendMoves;
import chessclub.com.icc.l2.Set2;
import chessclub.com.icc.l2.Shout;
import chessclub.com.icc.l2.TakeBackOccurred;
import chessclub.com.icc.l2.TitleChange;
import chessclub.com.icc.l2.Tourney;
import chessclub.com.icc.l2.TourneyGameEnded;
import chessclub.com.icc.l2.TourneyGameStarted;
import chessclub.com.icc.l2.UnknownPacket;
import chessclub.com.icc.l2.WebAuth;
import chessclub.com.icc.l2.WhoAmI;
import chessclub.com.icc.l2.WildKey;

/**
 * The primary class used in the ICC framework. An instance of this is run which is one logon to the server.
 * @author David Logan
 *
 */
public class ICCInstance extends Thread {

    private static final int EXITCODE = -16;
    private static final int LEVEL1EMPTY = -1; // This is the default value when there is no level 1 active
    private static final int LEVEL1ENDED = -2; // You get a spurious extra ending bracket when logging out of ICC

    private Logger LOG = LogManager.getLogger(ICCInstance.class+" (not logged in)");
    private Logger LOGRAW = LogManager.getLogger(ICCInstance.class.getName() + ".rawdata (not logged in)");
    private Logger LOGPACKETS = LogManager.getLogger(ICCInstance.class.getName() + ".packets (not logged in)");
    private Socket socket = null;
    private InputStream is = null;
    private OutputStream os = null;
    private InetSocketAddress addr = null;
    private byte[] bytebuffer = null;
    private boolean handlesInitialized = false;
    private boolean adminOnDuty = false;
    private int iSocketBufferSize = 4096;
    private String userid;
    private String password;
    private boolean bSetUp = false;
    private Level2Settings l2 = new Level2Settings();
    private HashMap<Integer, String> ratingKey = new HashMap<Integer, String>();
    private HashMap<String, IAbstractICCHandler> handlerMap = new HashMap<String, IAbstractICCHandler>();
    private HashMap<IAbstractICCHandler, GameList> gamelist = new HashMap<IAbstractICCHandler, GameList>();
    private HashMap<Integer, MessageListItem> messages = new HashMap<Integer, MessageListItem>();

    /**
     * This is primarily for Unit testing purposes, so we can test whether or not level 2 settings have changed on the server
     * @param l2 - A new level 2 settings class.
     */
    public void setLevel2Settings(Level2Settings l2) {
        this.l2 = l2;
    }
    public Level2Settings getLevel2Settings() {
        return l2;
    }
    @Override
    public String toString() {
        return "ICCInstance userid " + userid + " password " + password + " host " + addr.getHostName();
    }

    /**
     * 
     */
    public ICCInstance() { }
    /**
     * Creates an instance of a connection to ICC.
     *
     * @param host
     *            The host name of the server (default "chessclub.com")
     * @param port
     *            The port to connect to (default 5000)
     * @param pUserid
     *            The userid to connect with (default "g")
     * @param pPassword
     *            The password to connect with (default none)
     * @param handler
     *            The handler that will handle all incoming ICC data
     * @param socketBufferSize
     *            The number of bytes for the raw socket byte buffer
     * @return  {@link  ICCException} if there was any problem. <b>null</b> is returned if there was no error.
     * @throws Exception 
     */
    public ICCException setInstance(final String host, final int port, final String pUserid, final String pPassword, final IAbstractICCHandler handler, final int socketBufferSize) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("host=" + host + ",port=" + port + ",userid=" + pUserid + ",password=" + pPassword + ",handler=" + handler + ",socketBufferSize=" + socketBufferSize);
        }

        if (handler != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding first handler " + handler.hashCode());
            }
            handler.setInstance(this);
            addHandler(handler);
        }
        try {
            addr = new InetSocketAddress(InetAddress.getByName(host), port);
        } catch (IllegalArgumentException e) {
            LOG.error("IllegalArgumentException: " + e);
            return new ICCException(ICCError.PORT, e);
        } catch (UnknownHostException e) {
            LOG.error("UnknownHostException: " + e);
            return new ICCException(ICCError.HOSTNAME, e);
        }
        this.userid = pUserid;
        this.password = pPassword;

        /* Do this rather than return an error to keep the same methodology of returning a
         * LoginFailed packet rather than this error here and the rest of the errors be
         * LoginFailed.
         */
        if (this.userid == null || this.userid.length() == 0) {
            this.userid = "_";
        }
        
        this.iSocketBufferSize = socketBufferSize;
//        internalInitialize();
        bSetUp = true;
        return null;
    }

    private synchronized IAbstractICCHandler[] getHandlers() {
        IAbstractICCHandler[] h = new IAbstractICCHandler[0];
        h = handlerMap.values().toArray(h);
        return h;
    }

    /**
     * Removes an added ICC handler from the ICC instance.
     * @param handler   The {@link AbstractICCHandler} to remove.
     */
    public synchronized void removeHandler(final IAbstractICCHandler handler) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Removing handler " + handler.hashCode());
        }
        L2[] endl2 = endL2(handler);
        for(L2 l2 : endl2) {
            LOG.debug("Ending level 2 " + l2);
            setLevel2(l2,  false);
        }
        handlerMap.remove(Integer.toString(handler.hashCode()));
//        if (examineHandler != null && examineHandler.hashCode() == handler.hashCode()) {
//            send("unexamine");
//            nextexamine(handler);
//        }
    }

    /**
     * Adds a new handler instance to this ICC instance. When a handler issues a command, the framework
     * will make sure the command response goes to this handler.
     * Generic packets will go to all handlers that return true to the call {@link AbstractICCHandler#acceptGenericPackets()}
     * @param handler   The {@link AbstractICCHandler} to add
     */
    private L2[] newL2;
    public synchronized void addHandler(final IAbstractICCHandler handler) {
        if (handler != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding handler " + handler.hashCode());
            }
            newL2 = startL2(handler);
            handler.setInstance(this);
            handlerMap.put(Integer.toString(handler.hashCode()), handler);
            if (os != null) {
                doStartL2();
                handler.initHandler();
            }
        }
    }

    private void doStartL2() {
        for(L2 l2 : newL2) {
            LOG.debug("Starting level 2 " + l2);
            this.l2.set(l2, true);
            this.setLevel2(l2,  true);
        }
        newL2 = null;
    }
    
    @Override
    public void run() {
        if (!bSetUp) {
            LOG.error("run() called without being set up correctly");
            return;
        }
        
        bytebuffer = new byte[iSocketBufferSize];
        try {
            socket = new Socket();
            socket.setKeepAlive(true);
            socket.bind(null);
            socket.connect(addr, 10000);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            if (doLogin()) {
                doMain();
            }
        } catch (IllegalArgumentException e3) {
            LOG.error("ICCInstance::run IllegalArgumentException: " + e3);
            handleError(ICCError.UNKNOWN, e3);
            //e2.printStackTrace();
        } catch (SocketTimeoutException e2) {
            LOG.error("ICCInstance::run SocketTimeoutException: " + e2);
            handleError(ICCError.UNKNOWN, e2);
            //e2.printStackTrace();
        } catch (IOException e1) {
            LOG.error("ICCInstance::run IOException: " + e1);
            handleError(ICCError.UNKNOWN, e1);
            //e1.printStackTrace();
        }
        for (IAbstractICCHandler h : getHandlers()) {
            h.shuttingDown();
        }
    }

    private boolean doLogin() {
        LOG.debug("doLogin");
        LOG.debug("   level2settings=" + l2);
        
        l2.command(true); // We need to internally create a hack to tell callers when we've left a channel

        String buffer = "";

        while (socket.isConnected()) {
            try {
                if (is.read(bytebuffer) == -1) {
                    return false;
                }
                buffer += new String(bytebuffer);
                if (buffer.contains("login:")) {
                    buffer = "";
                    os.write(("level1=15" + (char) 13 + (char) 10).getBytes());
                    os.write(("level2settings=" + l2 + (char) 13 + (char) 10).getBytes());
                    os.write((userid + (char) 13 + (char) 10).getBytes());
                    os.flush();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error("IOException: " + e);
                return false;
            }
        }
        return false;
    }

    private void doMain() {
        boolean ctrl = false;
        int level1 = LEVEL1EMPTY;
        boolean level2 = false;
        String currentLevel1 = "";
        String currentLevel2 = "";
        ArrayList<String> level1Array = new ArrayList<String>();
        ArrayList<L2PacketHolder> level2Array = new ArrayList<L2PacketHolder>();

        while (socket.isConnected()) {
            LOGRAW.trace("read loop: level1="+level1);
            if(LOGRAW.isTraceEnabled()) {
                for(int x=0;x<level1Array.size();x++)
                    LOGRAW.trace("level1["+x+"]="+level1Array.get(x));
            }
            int nRead;
            try {
                LOGRAW.trace("Before is.read");
                nRead = is.read(bytebuffer);
                LOGRAW.trace("After is.read, nRead="+nRead);
                if (nRead == -1) {
                    handlePackets(level1Array, level2Array);
                    LOGRAW.debug("nRead is -1. Handling final set of packets, then returning");
                    return;
                }
                LOGRAW.debug("raw input:" + new String(bytebuffer, 0, nRead) + ":");
                for (int bx = 0; bx < nRead; bx++) {
                    byte by = bytebuffer[bx];
                    switch (by) {
                    case 25: // ^Y
                        ctrl = true;
                        break;
                    case '[':
                        if (ctrl) {
                            ctrl = false;
                            if (level1 > LEVEL1EMPTY) {
                                if (level1Array.size() > level1) {
                                    LOGRAW.trace("Replacing level1[" + level1 + " with '" + currentLevel1 + "'");
                                    level1Array.set(level1, currentLevel1);
                                } else {
                                    LOGRAW.trace("Adding new level1='" + currentLevel1 + "'");
                                    level1Array.add(currentLevel1);
                                }
                            }
                            level1++;
                            if (level1Array.size() > (level1 + 1)) {
                                currentLevel1 = level1Array.get(level1 + 1);
                                LOGRAW.trace("retreiving currentLevel1=" + currentLevel1);
                            } else {
                                LOGRAW.trace("new currentLevel1");
                                currentLevel1 = "";
                            }
                        } else if (level2) {
                            currentLevel2 += '[';
                        } else {
                            currentLevel1 += '[';
                        }
                        break;
                        // I think ^Z is when we "overflow ICC's buffer", which is a very bad thing. Using this hack will get you through
                        // the hole (and processing continues), but it LOSES DATA!!
                    case 26:
                        // ^Z Means ICC and their dumb decsions have hit us again. They had some sort of "buffer overrun",
                        // and can't send us the rest of our data, even though there should be no earthly reason that they have
                        // this problem. Nevertheless, we have to RESET and RECOVER.
                        ctrl = true;
                        level1 = LEVEL1EMPTY + 1;
                    case ']':
                        if (ctrl) {
                            ctrl = false;
                            if (level1 == LEVEL1ENDED) {
                                return;
                            }
                            if (level1Array.size() > (level1 + 1)) {
                                LOGRAW.trace("Replacing level1[" + (level1 + 1) + " with '" + currentLevel1 + "'");
                                level1Array.set((level1 + 1), currentLevel1);
                            } else {
                                LOGRAW.trace("Adding new level1='" + currentLevel1 + "'");
                                level1Array.add(currentLevel1);
                            }
                            if (--level1 == LEVEL1EMPTY) {
                                handlePackets(level1Array, level2Array);
                                level1Array.clear();
                                level2Array.clear();
                            }
                        } else if (level2) {
                            currentLevel2 += ']';
                        } else {
                            currentLevel1 += ']';
                        }
                        break;
                    case '(':
                        if (ctrl) {
                            ctrl = false;
                            level2 = true;
                            currentLevel2 = "";
                        } else if (level2) {
                            currentLevel2 += '(';
                        } else {
                            currentLevel1 += '(';
                        }
                        break;
                    case ')':
                        if (ctrl) {
                            ctrl = false;
                            level2 = false;
                            if (LOGRAW.isTraceEnabled()) {
                                LOGRAW.trace("Adding level2 packet='" + currentLevel2 + "'");
                                LOGRAW.trace("   currentLevel1='" + currentLevel1 + "'");
                            }
                            String hdrstr = (level1Array.size() > 0 ? level1Array.get(0) : currentLevel1);
                            hdrstr = hdrstr.substring(0, hdrstr.indexOf("\r\n"));
                            String[] cl1 = hdrstr.split("\\s+");
                            level2Array.add(new L2PacketHolder(level1, (cl1.length == 3 ? cl1[2] : null), currentLevel2));
                        } else if (level2) {
                            currentLevel2 += ')';
                        } else {
                            currentLevel1 += ')';
                        }
                        break;
                    default:
                        if (ctrl) {
                            if (level2) {
                                currentLevel2 += (char) 25;
                            } else {
                                currentLevel1 += (char) 25;
                            }
                            ctrl = false;
                        }
                        if (level2) {
                            currentLevel2 += (char) by;
                        } else {
                            currentLevel1 += (char) by;
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                if (e instanceof SocketException && "socket closed".equals(e.getMessage())) {
                    LOG.info("doMain socket closed: " + e.getMessage());
                    return;
                }
                LOG.error("doMain Exception: " + e.getMessage());
                LOG.error(e);
                e.printStackTrace();
                return;
            }
        }
    }

    private void handlePackets(final ArrayList<String> level1Array, final ArrayList<L2PacketHolder> level2Array)
            throws Exception {
        for (String ss : level1Array) {
            if (!ignoreLevel1(ss)) {
                Level1Packet l1 = null;
                try {
                    l1 = Level1Packet.getPacketInstance(ss);
                } catch (ClassNotFoundException e1) {
                    if (ss.startsWith("310")) {
                        Level1Packet l1test = Level1Packet.guaranteePacketInstance(ss);
                        handle(l1test);
                        return;
                    }
                    LOG.error("Unknown packet " + ss);
//                    System.exit(EXITCODE);
                }
                if(l1 != null)
                    handle(l1);
            }
        }
        for (L2PacketHolder ph : level2Array) {
            // Level2Packet l2 = new Level2Packet(s);
            Level2Packet myl2 = null;
            myl2 = Level2Packet.getPacketInstance(ph.getPacket(), ph.getL1key(), level1Array, ph.getL1index() + 1);
            if(myl2 == null) {
            	LOGPACKETS.debug("ERROR IN LEVEL 2 FOR PACKET");
            	continue;
            }
            if (LOGPACKETS.isDebugEnabled()) {
                LOGPACKETS.debug("level2 packet='" + myl2 + "'");
            }
            if(!(myl2 instanceof UnknownPacket)) {
                switch (myl2.type()) {
                case LOGIN_FAILED:
                    if (((LoginFailed) myl2).reason() == 5) {
                        this.os.write((password + (char) 13 + (char) 10).getBytes());
                        this.os.flush();
                    } else {
                        this.handle(myl2);
                        terminate();
                    }
                    break;
                case RATING_TYPE_KEY:
                    this.ratingKey.put(((RatingTypeKey) myl2).index(), ((RatingTypeKey) myl2).text());
                    break;
                default:
                    handle(myl2);
                    break;
                }
            }
        }
    }

    /**
     * Sends a command to ICC.
     * @param prefix    A generic set of text that uniquely identifies any response to this command
     * @param command   The command itself
     */
    public void send(final String prefix, final String command) {
        send("`" + prefix + "`" + command);
    }

    /**
     * Sends a command to ICC. This is protected because calls should be made to {@link #send(String, String)}
     * @param command   The command to be sent to ICC
     */
    protected synchronized void send(final String command) {
        int tries = 3;
        LOG.debug("Sending='" + command);
        byte[] cmdbytes = (command + (char) 13 + (char) 10).getBytes();
        while (tries != 0) {
            tries--;
            try {
                os.write(cmdbytes);
                os.flush();
                return;
            } catch (SocketException e) {
                LOG.error("send(" + command + ") socket error " + e);
                e.printStackTrace();
            } catch (IOException e) {
                LOG.error("send(" + command + ") error " + e);
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                LOG.error("send(" + command + ") sleep error " + e1);
                e1.printStackTrace();
            }
        }
    }

    private boolean ignoreLevel1(final String packet) {
        return CN.isUnknownPacket(Integer.parseInt(packet.split("\\s+")[0]));
    }

    private void handleError(final ICCError error, final Exception ex) {
        ICCException ie = new ICCException(error, ex);
        
        for (IAbstractICCHandler h : getHandlers()) {
            if (h instanceof IAcceptGenericPackets) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("sending exception " + error + " '" + ex.getMessage() + "' to " + h.hashCode());
                }
                h.iccException(ie);
            }
        }
    }

    public void setOverrideHandler(ICCPacketHandler overrideHandler) {
        this.overrideHandler = overrideHandler;
    }
    
    private ICCPacketHandler overrideHandler = null;
    protected void handle(final Level1Packet l1) {
        switch(l1.getType()) {
        case ADMIN:
            adminOnDuty = true;
            break;
        case ADMIN_UN:
            adminOnDuty = false;
            break;
        case FMESSAGE:
        case MESSAGE:
            this.send("messages");
            break;
        case XTELL:
            if(((XTellResult)l1).nothing())
                return; // Most XTell results are empty
        default:
            break;
        }

        if(overrideHandler != null && overrideHandler.handleLevel1(l1))
            return;

        IAbstractICCHandler h1;
        synchronized (this) {
            h1 = handlerMap.get(l1.getPrefix());
        }
        if (h1 != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("sending=" + l1 + " to " + h1.hashCode());
            }
            handle2(h1, l1);
            return;
        }

        if (l1.getPrefix() != null && !l1.getPrefix().isEmpty()) {
            LOG.error("Unable to find a handler for a level 1 message with key " + l1.getPrefix());
            return;
        }

        IAbstractICCHandler[] harray = getHandlers();

        for (IAbstractICCHandler h : harray) {
            if (h instanceof IAcceptGenericPackets) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("sending=" + l1 + " to " + h.hashCode());
                }
                handle2(h, l1);
            }
        }
    }

    private String whoami;
    protected void handle(final Level2Packet pl2) throws Exception {
        switch (pl2.type()) {
        case COMMAND:
            //
            // THE HACK TO SEND IN A FAKE DG 27 WHEN WE LEAVE A CHANNEL!
            //
            if(pl2.getParm(2).toLowerCase().startsWith("ch")) {
                if("-".equals(pl2.getParm(1)) || pl2.getParm(1).toLowerCase().startsWith("mi")) {
                    String channel = pl2.getParm(2).split("\\s+")[1];
                    String fakepacket = "27 "+channel+" "+whoami+" 0";
                    @SuppressWarnings({ "rawtypes", "unchecked" })
                    Level2Packet fp = Level2Packet.getPacketInstance(fakepacket, pl2.level1Key(), new ArrayList(Arrays.asList(pl2.getLevel1RawPacketData())), pl2.getLevel1Count());
                    handle(fp);
                    return;
                }
            }
            break;
        case WHO_AM_I:
            whoami = ((WhoAmI)pl2).userid();
            LOG = LogManager.getLogger(ICCInstance.class.getName() + " ("+whoami+")");
            LOGRAW = LogManager.getLogger(ICCInstance.class.getName() + ".rawdata ("+whoami+")");
            LOGPACKETS = LogManager.getLogger(ICCInstance.class.getName() + ".packets ("+whoami+")");
            send("ICCInstance", "set style 13");
            send("ICCInstance", "set width 255"); // Make sure we have maximum width
            send("ICCInstance", "messages"); // Get the first set of messages
            if(((WhoAmI)pl2).titles().isH())
                send("ICCInstance", "unrelayed");
            break;
        case PLAYER_ARRIVED:
            ((PlayerArrived) pl2).level2(this.l2);
            break;
        case NOTIFY_ARRIVED:
            ((PlayerArrived) pl2).level2(this.l2);
            break;
        case NEW_MY_RATING:
            ((MyRating) pl2).level2(l2); 
            break;
        case CHANNEL_TELL:
            if(((ChannelTell)pl2).channel() == 100 || ((ChannelTell)pl2).channel() == 1) {
                if(doChannelTell((ChannelTell) pl2))
                    return;
            }
            break;
        default:
            break;
        }
        
        if(overrideHandler != null && overrideHandler.handleLevel2(pl2))
            return;
        
        switch (pl2.type()) {
        case WHO_AM_I:
            if (!handlesInitialized) {
                IAbstractICCHandler[] harray = getHandlers();
                for (IAbstractICCHandler ha : harray) {
                    ha.initHandler();
                    doStartL2();
                }
                handlesInitialized = true;
            }
            if (!handlesInitialized) {
                IAbstractICCHandler[] harray = getHandlers();
                for (IAbstractICCHandler ha : harray) {
                    ha.initHandler();
                    doStartL2();
                }
                handlesInitialized = true;
            }
            for (IAbstractICCHandler ha : getHandlers()) {
                if(ha instanceof IWhoAmI)
                    ((IWhoAmI) ha).whoAmI((WhoAmI)pl2);
            }
            break;
            default:
                break;
        }

        IAbstractICCHandler h1;
        synchronized (this) {
            h1 = handlerMap.get(pl2.level1Key());
        }
        if (h1 != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("sending=" + pl2 + " to " + h1.hashCode());
            }
            handle2(h1, pl2);
            return;
        }

        if (pl2.level1Key() != null && !pl2.level1Key().isEmpty()) {
            LOG.error("Unable to find a handler for a level 2 message with key " + pl2.level1Key());
            return;
        }

        IAbstractICCHandler[] harray = getHandlers();
        for (IAbstractICCHandler h : harray) {
            if (h instanceof IAcceptGenericPackets) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("sending=" + pl2 + " to " + h.hashCode());
                }
                handle2(h, pl2);
            }
        }
    }

    private void handle2(final IAbstractICCHandler h, final Level1Packet l1) {
        if((l1.getType() == CN.ADMIN || l1.getType() == CN.ADMIN_UN) && !((L1ErrorOnly) l1).hasError()) {
            //pass through to the handler
        } else if(l1.getType() == CN.LIBLIST && l1.numberParms() == 2 && l1.getParm(1).contains("has no games")) {
            GameList gl = new GameList();
            if(h instanceof IGameList)
                ((IGameList)h).gameList(gl);
            return;
        } else if (l1 instanceof chessclub.com.icc.l1.L1ErrorOnly) {
            if (((L1ErrorOnly) l1).hasError()) {
                LOG.debug("Calling badCommand2 for an L1ErrorOnly packet of " + l1);
                h.badCommand2((L1ErrorOnly) l1);
            }
            return;
        } else if (l1.getType() == CN.SHOW_COMMANDS) {
            if (h instanceof chessclub.com.icc.UnitTestHandler) {
                ((UnitTestHandler) h).do310(l1);
            }
            return;
        }
        switch (l1.getType()) {
//        case TAKEBACK:
//        	if(h instanceof IMyGame)
//                ((IMyGame)h).takeBackRequested((TakeBackRequested)l1);
//        	break;
        case FINGER:
            if(h instanceof IFinger)
                ((IFinger)h).finger((FingerNotes)l1);
            break;
        case DB_MACHINES:
        	((IDBMachines)h).dbmachines((DBMachines)l1);
        	break;
        case DB_USERS:
        	((IDBUsers)h).dbusers((DBUsers)l1);
        	break;
        case LOCATION:
        	((ILocation)h).location((Location)l1);
        	break;
        case LOGONS:
        case LOGONS0:
        	((ILogons)h).logons((Logons)l1);
        	break;
        case S_EVENTS:
            if(h instanceof ISEvent)
                ((ISEvent)h).sEvent((SEvent) l1);
            break;
        case SPOOF:
            if(h instanceof ISpoof)
                ((ISpoof)h).spoof((Spoof) l1);
            break;
        case S_LOGOUT:
            if(h instanceof ILoggedOut)
                ((ILoggedOut)h).loggedOut((LoggedOut) l1);
            break;
        case BAD:
        case S_BAD:
            if(!"ICCInstance".equals(l1.getPrefix())) {
                //
                // At the moment, there are a lot of possible returns with null command
                // because we aren't handling everything correctly. But as we work through
                // our unit tests, we assume that a null command means we have the level 2
                // datagram.
                //
                if(((BadCommand)l1).command() != null)
                    h.badCommand1((BadCommand) l1);
            }
            break;
        case UNRELAYED:
            processUnrelayed(h, (Unrelayed)l1);
            break;
        case ADMIN:
            if(h instanceof IAdminOnOffDuty)
                ((IAdminOnOffDuty)h).adminOnDuty();
            break;
        case ADMIN_UN:
            if(h instanceof IAdminOnOffDuty)
                ((IAdminOnOffDuty)h).adminOffDuty();
            break;
        case GETPX:
            if(h instanceof IGetpx)
                ((IGetpx)h).getpx((Getpx) l1);
            break;
        case GETPS:
            if(h instanceof IGetps)
                ((IGetps)h).getps((Getps) l1);
            break;
        case SEEKING:
            if(!((Seeking)l1).isEmpty() && h instanceof IMySeek)
                ((IMySeek)h).mySeek((Seeking) l1);
            break;
        case UNSEEKING:
            if(h instanceof IMySeek)
                ((IMySeek)h).mySeekRemoved((Unseeking) l1);
            break;
        case PLAY:
        case MATCH:
        case REMATCH:
            if(((Play)l1).waitingForAccept() && h instanceof IManualAccept)
                ((IManualAccept)h).manualAccept((Play)l1);
            break;
        case SFEN:
            if(h instanceof ISFen)
                ((ISFen)h).inSfen((Sfen) l1);
            break;
        case SPGN:
            if(h instanceof ISpgn)
                ((ISpgn)h).inSPgn((SPgn) l1);
            break;
        case COMPLAIN:
            if(h instanceof IUserComplained)
                ((IUserComplained)h).UserComplained((Complain) l1);
            break;
        case ADJUDICATE:
            if(h instanceof IGameAdjudicated)
                ((IGameAdjudicated)h).gameAdjudicated((GameAdjudicated) l1);
            break;
        case SHOUT:
        case I:
            if(h instanceof IShoutResponse)
                ((IShoutResponse)h).shoutResponse((ShoutResponse) l1);
            break;
        case REGISTER:
            if(h instanceof INewRegistration)
                ((INewRegistration)h).newRegistration((Register) l1);
            break;
        case MAILPASSWORD:
            if(h instanceof IShowPassword)
                ((IShowPassword)h).showPassword((ShowPassword) l1);
            break;
        case RATING:
            if(h instanceof IRatingReset)
                ((IRatingReset)h).ratingReset((RatingChanged) l1);
            break;
        case PLUS:
        case MINUS:
            if(h instanceof IListChanged)
                ((IListChanged)h).listChanged((ListChanged)l1);
            break;
        default:
            break;
        }
    }

    private void processUnrelayed(IAbstractICCHandler handler, Unrelayed l1) {
        for(HelpQuestion q : l1.getQuestionList()) {
            if(handler instanceof IHelperQuestions)
                ((IHelperQuestions)handler).newHelperQuestion(q);
        }
    }

    private void handle2(final IAbstractICCHandler h, final Level2Packet pl2) throws Exception { // SUPPRESS CHECKSTYLE too many lines
        switch (pl2.type()) {
        case WHO_AM_I:
            if(h instanceof IWhoAmI)
                ((IWhoAmI)h).whoAmI((WhoAmI) pl2);
            break;
        case LOGIN_FAILED:
            if(h instanceof ILoginFailed)
                ((ILoginFailed)h).loginFailed((LoginFailed) pl2);
            break;
        case PLAYER_ARRIVED_SIMPLE:
            if(h instanceof IPlayerArrivedSimple)
                ((IPlayerArrivedSimple)h).playerArrivedSimple(pl2.getParm(1));
            break;
        case MY_NOTIFY_LIST:
            if(h instanceof INotifyList)
            ((INotifyList)h).myNotifyList((MyNotifyList) pl2);
            break;
        case TOURNEY_GAME_ENDED:
            if(h instanceof ITourney)
                ((ITourney)h).tourneyGameEnded((TourneyGameEnded) pl2);
            break;
        case MY_STRING_VARIABLE:
        case MY_VARIABLE:
            if(h instanceof IMyVariable)
                ((IMyVariable)h).myVariable((MyVariable) pl2);
            break;
        case PLAYER_ARRIVED:
            if(h instanceof IPlayerArrived)
                ((IPlayerArrived)h).playerArrived((PlayerArrived) pl2);
            break;
        case NOTIFY_ARRIVED:
            if(h instanceof INotifyArrivedLeft)
            ((INotifyArrivedLeft)h).notifyArrived((PlayerArrived) pl2);
            break;
        case NEW_MY_RATING:
            if(h instanceof IRatingInfo)
                ((IRatingInfo)h).myRating((MyRating) pl2);
            break;
        case KEY_VERSION:
            if(h instanceof IKeyVersion)
                ((IKeyVersion)h).keyVersion(Integer.parseInt(pl2.getParm(1)));
            break;
        case WILD_KEY:
            if(h instanceof IRatingInfo)
                ((IRatingInfo)h).wildKey((WildKey) pl2);
            break;
        case WEB_AUTH:
            if(h instanceof IWebAuth)
                ((IWebAuth)h).webAuth((WebAuth) pl2);
            break;
        case CHANNELS_SHARED:
            if(h instanceof IChannelsShared)
                ((IChannelsShared)h).channelsShared((ChannelsShared) pl2);
            break;
        case PLAYER_LEFT:
            if(h instanceof IPlayerArrived)
                ((IPlayerArrived)h).playerLeft(pl2.getParm(1));
            else if(h instanceof IPlayerArrivedSimple)
                ((IPlayerArrivedSimple)h).playerLeft(pl2.getParm(1));
            break;
        case PEOPLE_IN_MY_CHANNEL:
            if(h instanceof IPersonInMyChannel)
                ((IPersonInMyChannel)h).personInMyChannel((PersonInMyChannel) pl2);
            break;
        case SEEK:
            if(h instanceof ISeek)
                ((ISeek)h).seek((Seek) pl2);
            break;
        case SEEK_REMOVED:
            if(h instanceof ISeek)
                ((ISeek)h).seekRemoved((SeekRemoved) pl2);
            break;
        case TOURNEY:
            if(h instanceof ITourney)
                ((ITourney)h).tourney((Tourney) pl2);
            break;
        case CHANNEL_TELL:
            if(h instanceof IChannelTell)
                ((IChannelTell)h).channelTell((ChannelTell)pl2);
            break;
        case TOURNEY_GAME_STARTED:
            if(h instanceof ITourney)
                ((ITourney)h).tourneyGameStarted((TourneyGameStarted) pl2);
            break;
        case REMOVE_TOURNEY:
            if(h instanceof ITourney)
                ((ITourney)h).removeTourney(Integer.parseInt(pl2.getParm(1)));
            break;
        case SHOUT:
            if(h instanceof IShout)
                ((IShout)h).incomingShout((Shout) pl2);
            break;
        case PERSONAL_TELL:
            if(h instanceof IPersonalTell)
                ((IPersonalTell)h).personalTell((PersonalTell) pl2);
            break;
        case PERSONAL_QTELL:
            if(h instanceof IPersonalQTell)
                ((IPersonalQTell)h).personalQTell((PersonalQTell) pl2);
            break;
        case NOTIFY_LEFT:
            if(h instanceof INotifyArrivedLeft)
            ((INotifyArrivedLeft)h).notifyLeft(pl2.getParm(1));
            break;
        case STATE:
            if(h instanceof IPlayerState)
                ((IPlayerState)h).playerState((PlayerStatePacket) pl2);
            break;
        case PARTNERSHIP:
            if(h instanceof IPartnership)
                ((IPartnership)h).partnership((Partnership) pl2);
            break;
        case COMMAND:
            if(h instanceof ICommand)
                ((ICommand)h).command((Command) pl2);
            break;
        case NOTIFY_STATE:
            if(h instanceof INotifyPlayerState)
                ((INotifyPlayerState)h).notifyPlayerState((PlayerStatePacket) pl2);
            break;
        case CHANNEL_QTELL:
            if(h instanceof IChannelQTell)
                ((IChannelQTell)h).channelQTell((ChannelQTell) pl2);
            break;
        case PERSONAL_TELL_ECHO:
            if(h instanceof IPersonalTellEcho)
                ((IPersonalTellEcho)h).personalTellEcho((PersonalTellEcho) pl2);
            break;
        case SET2:
            internalSet2((Set2) pl2);
            break;
        case GAME_RESULT:
            if(h instanceof IGameStartedResult)
                ((IGameStartedResult)h).gameResult((GameResultPacket) pl2);
            break;
        case MY_GAME_RESULT:
            if(h instanceof IMyGame)
            ((IMyGame)h).myGameResult((GameResultPacket) pl2);
            break;
        case GAME_STARTED:
            if(h instanceof IGameStartedResult)
                ((IGameStartedResult)h).gameStarted((GameStarted) pl2);
            break;
        case MY_GAME_STARTED:
            if(h instanceof IMyGame)
                ((IMyGame)h).myGameStarted((GameStarted) pl2);
            break;
        case MSEC:
            if(h instanceof IMyGame)
                ((IMyGame)h).msec((Msec) pl2);
            break;
        case SEND_MOVES:
            if(h instanceof IMyGame)
                ((IMyGame)h).sendMoves((SendMoves) pl2);
            break;
        case EXAMINED_GAME_IS_GONE:
            if(h instanceof IMyGame)
                ((IMyGame)h).examinedGameIsGone((GameNumber) pl2);
            break;
        case STOP_OBSERVING:
            if(h instanceof IObserving)
                ((IObserving)h).stopObserving((GameNumber) pl2);
            break;
        case REFRESH:
            if(h instanceof IRefresh)
                ((IRefresh)h).refresh((GameNumber) pl2);
            break;
        case MY_TURN:
            if(h instanceof IMyTurn)
                ((IMyTurn)h).myTurn((GameNumber) pl2);
            break;
        case MY_GAME_ENDED:
            if(h instanceof IMyGame)
                ((IMyGame)h).myGameEnded((GameNumber) pl2);
            break;
        case GAMELIST_BEGIN:
            processGamelist((GamelistBegin) pl2, h);
            break;
        case GAMELIST_ITEM:
            processGamelist((GameListItem) pl2, h);
            break;
        case STARTED_OBSERVING:
            if(h instanceof IObserving)
                ((IObserving)h).startedObserving((GameStarted) pl2);
            break;
        case MESSAGELIST_BEGIN:
            // processMessageList((MessageListBegin) pl2, h);
            break;
        case MESSAGELIST_ITEM:
            processMessageList((MessageListItem) pl2, h);
            break;
        case TITLES:
            if(h instanceof ITitlesChanged)
                ((ITitlesChanged)h).titlesChanged((TitleChange)pl2);
            break;
        case TAKEBACK:
            if(h instanceof IMyGame)
            ((IMyGame)h).takeBackOccurred((TakeBackOccurred)pl2);
            break;
        case OFFERS_IN_MY_GAME:
        	if(h instanceof IMyGame)
        		((IMyGame)h).newOffers((OffersInMyGame)pl2);
        	break;
        case ERROR:
            h.Error((chessclub.com.icc.l2.Error)pl2);
//        case MOVE_LIST:
//            if(h instanceof IMoveList)
//                ((IMoveList)h).moveList((MoveList)pl2);
        default:
            break;
        }
    }

    private void process_newHelperQuestion(HelpQuestion q) {
        if(overrideHandler != null && overrideHandler.handleOther(q))
            return;
        
        IAbstractICCHandler[] harray = getHandlers();
        for (IAbstractICCHandler h : harray) {
            if (h instanceof IHelperQuestions) {
                ((IHelperQuestions)h).newHelperQuestion(q);
            }
        }
    }
    private void process_handlingHelperQuestion(String name, int qid) {
        HelpQuestionModified q = new HelpQuestionModified();
        q.setAction(HelpQuestionModified.HANDLING);
        q.setAdmin(name);
        q.setQuestionID(qid);

        if(overrideHandler != null && overrideHandler.handleOther(q))
            return;

        IAbstractICCHandler[] harray = getHandlers();
        for (IAbstractICCHandler h : harray) {
            if (h instanceof IHelperQuestions) {
                ((IHelperQuestions)h).handlingHelperQuestion(q);
            }
        }
    }
    private void process_removingHelperQuestion(String name, int qid) {
        HelpQuestionModified q = new HelpQuestionModified();
        q.setAction(HelpQuestionModified.REMOVED);
        q.setAdmin(name);
        q.setQuestionID(qid);

        if(overrideHandler != null && overrideHandler.handleOther(q))
            return;

        IAbstractICCHandler[] harray = getHandlers();
        for (IAbstractICCHandler h : harray) {
            if (h instanceof IHelperQuestions) {
                ((IHelperQuestions)h).removingHelperQuestion(q);
            }
        }
    }
    private HashMap<String, ArrayList<Integer>> q_inprogressList = new HashMap<String, ArrayList<Integer>>();
    private boolean doChannelTell(ChannelTell pl2) {
        if(pl2.channel() == 100) {
            Pattern p = Pattern.compile("\\[(\\d+)\\]\\s+\\((.*)\\)\\s+(.*)");
            Matcher m = p.matcher(pl2.text());
            if (m.matches()) {
                HelpQuestion q = new HelpQuestion();
                q.question = m.group(3);
                q.interfac = m.group(2);
                q.questionId = Integer.parseInt(m.group(1));
                q.asked      = new Date();
                q.gone       = false;
                q.user = pl2.name();
                process_newHelperQuestion(q);
                return true;
            }
            p = Pattern.compile("\\[handling\\s+(\\d+)\\]");
            m = p.matcher(pl2.text());
            if (m.matches()) {
                ArrayList<Integer> qlist = q_inprogressList.get(pl2.name());
                if(qlist == null) {
                    qlist = new ArrayList<Integer>();
                    q_inprogressList.put(pl2.name(), qlist);
                }
                qlist.add(Integer.parseInt(m.group(1)));
                process_handlingHelperQuestion(pl2.name(), Integer.parseInt(m.group(1)));
                return true;
            }
            p = Pattern.compile("\\[removing\\s+(\\d+)\\]");
            m = p.matcher(pl2.text());
            if (m.matches()) {
                ArrayList<Integer> qlist = q_inprogressList.get(pl2.name());
                if(qlist != null) {
                    if(qlist.size() == 1) {
                        q_inprogressList.remove(pl2.name());
                    } else {
                        qlist.remove((Object)Integer.parseInt(m.group(1))); // Specify (Object) to ensure we get the right method
                    }
                }
                process_removingHelperQuestion(pl2.name(), Integer.parseInt(m.group(1)));
                return true;
            }
        } else if(pl2.channel() == 1) {
            if(pl2.titles().isH() || pl2.titles().isOnDutyAdministrator()) {
                ArrayList<Integer> qlist = q_inprogressList.get(pl2.name());
                if(qlist != null) {
                    for(Integer qid : qlist)
                        process_removingHelperQuestion(pl2.name(), qid);
                }
                q_inprogressList.remove(pl2.name());
            }
        };
        return false;
    }

    private void processMessageList(final MessageListItem pl2, final IAbstractICCHandler h) {
        if (messages.containsKey(pl2.index())) {
            return;
        }
        messages.put(pl2.index(), pl2);
        if(h instanceof INewMessage)
            ((INewMessage)h).newMessage(pl2);
    }

    /**
     * The current list of ICC messages are kept within the framework. This method will return the
     * current list of the logged on users ICC messages.
     * @return  An array of ICC messages
     */
    public MessageListItem[] allMessages() {
        MessageListItem[] ret = new MessageListItem[messages.size()];
        ret = messages.values().toArray(ret);
        return ret;
    }

    // private void processMessageList(MessageListBegin pl2, AbstractICCHandler h) {
    // messageList = new MessageList(pl2);
    // }

    private void processGamelist(final GamelistBegin l22, final IAbstractICCHandler h) {
        GameList gl = new GameList(l22);
        if (gl.nhits() == 0) {
            if(h instanceof IGameList)
                ((IGameList)h).gameList(gl);
        } else {
            gamelist.put(h, new GameList(l22));
        }
    }

    private void processGamelist(final GameListItem l22, final IAbstractICCHandler h) {
        GameList g = gamelist.get(h);
        if (g == null) {
            LOG.error("GameListItem request for a handler but no GameList is in the HashMap");
            return;
        }
        if (g.addgame(l22)) {
            gamelist.remove(h);
            if(h instanceof IGameList)
                ((IGameList)h).gameList(g);
        }
    }

    private void setLevel2(final L2 pl2, final boolean set) {
        this.send("set-2 " + pl2.ordinal() + " " + (set ? "1" : "0"));
    }

    private void internalSet2(final Set2 set2) {
        if (this.l2.isSet(set2.code()) != set2.on()) {
            LOG.error("ICCInstance has to reset level 2 " + set2.code() + " to " + this.l2.isSet(set2.code()) + " because we received a set-2 change we didn't authorize");
            setLevel2(set2.code(), l2.isSet(set2.code()));
        }
    }

    /**
     * Ask ICC to start sending packets for every game that begins on the server. This will be a lot of packets.
     * @param accept    True if we want to start getting packets for every game that starts and ends, and examined games that go away.
     */
//    public void enableGameStartInfo(final boolean accept) {
//        if (this.l2.gameStarted() != accept) {
//            this.l2.gameStarted(accept);
//            setLevel2(L2.GAME_STARTED, accept);
//        }
//        if (this.l2.gameResult() != accept) {
//            this.l2.gameResult(accept);
//            setLevel2(L2.GAME_RESULT, accept);
//        }
//        if (this.l2.examinedGameIsGone() != accept) {
//            this.l2.examinedGameIsGone(accept);
//            setLevel2(L2.EXAMINED_GAME_IS_GONE, accept);
//        }
//    }

//    public void enableVariables(final boolean enable) {
//        if(this.l2.myVariable() != enable) {
//            this.l2.myVariable(enable);
//            setLevel2(L2.MY_VARIABLE, enable);
//        }
//        if(this.l2.myStringVariable() != enable) {
//            this.l2.myStringVariable(enable);
//            setLevel2(L2.MY_STRING_VARIABLE, enable);
//        }
//    }
    
//    public void enableChannelTells(final boolean enable) {
//        if(this.l2.channelTell() != enable) {
//            this.l2.channelTell(enable);
//            setLevel2(L2.CHANNEL_TELL, enable);
//        }
//        if(this.l2.channelQtell() != enable) {
//            this.l2.channelQtell(enable);
//            setLevel2(L2.CHANNEL_QTELL, enable);
//        }
//    }

//    public void enablePeopleInMyChannel(boolean enable) {
//        // Worthless packet. It send less data when somebody connects, but overall WORTHLESS
//        // And yet, while trying to logon as an admin (djlogan), I run out of buffer space
//        // So...we shall try adding it.
//        if(this.l2.channelsShared() != enable) {
//            this.l2.channelsShared(enable);
//            setLevel2(L2.CHANNELS_SHARED, enable);
//        }
//        if (this.l2.peopleInMyChannel() != enable) {
//            this.l2.peopleInMyChannel(enable);
//            setLevel2(L2.PEOPLE_IN_MY_CHANNEL, enable);
//        }
//    }

//    public void enableTitleChanges(boolean enable) {
//        if(this.l2.titles() != enable) {
//            this.l2.titles(enable);
//            setLevel2(L2.TITLES, enable);
//        }
//        if(this.l2.playerArrived() != enable) {
//            this.l2.playerArrived(enable);
//            setLevel2(L2.PLAYER_ARRIVED, enable);
//        }
//    }
//    public void enablePlayersArrivalsAndLeaves(boolean enable) {
//        if(this.l2.playerLeft() != enable) {
//            this.l2.playerLeft(enable);
//            setLevel2(L2.PLAYER_LEFT, enable);
//        }
//        if(this.l2.playerArrived() != enable) {
//            this.l2.playerArrived(enable);
//            setLevel2(L2.PLAYER_ARRIVED, enable);
//        }
//    }
    
//    public void enableSeekMessages(boolean enable) {
//        if(this.l2.seek() != enable) {
//            this.l2.seek(enable);
//            setLevel2(L2.SEEK, enable);
//        }
//        if(this.l2.seekRemoved() != enable) {
//            this.l2.seekRemoved(enable);
//            setLevel2(L2.SEEK_REMOVED, enable);
//        }
//    }
    
    /**
     * Logs off of ICC.
     */
    public void quit() {
        send("quit");
    }

    /**
     * Clears a message on the ICC server.
     * @param index The message number to clear.
     */
    public void clearmessage(final int index) {
//        if (messages.containsKey(index)) {
            send("clearmessage " + index);
            messages.remove(index);
//        }
    }

    /**
     * This function can be called to simply close the socket, forcibly disconnecting from the ICC.
     */
    private void terminate() {
        if (socket == null) {
            return;
        }
        
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("IOException: "+e);
            System.exit(EXITCODE);
        }
    }
    public boolean isOnDuty() {
        return adminOnDuty;
    }
    
    public Rating getRatingByKey(int ratingKey) {
       String ratingString = this.ratingKey.get(ratingKey);
       return Rating.fromString(ratingString);
    }

    private static class xxx {
        public Class<?> clazz;
        public L2 l2;
        public xxx(Class<?> clazz, L2 l2) {
            this.clazz = clazz;
            this.l2 = l2;
        }
    }
    
    private static xxx[] assignments_array = new xxx[] {
        new xxx(IChannelQTell.class, L2.CHANNEL_QTELL),
        new xxx(IChannelTell.class, L2.CHANNEL_TELL),
        new xxx(IHelperQuestions.class, L2.CHANNEL_TELL),
        new xxx(IChannelsShared.class, L2.CHANNELS_SHARED),
        new xxx(ICommand.class, L2.COMMAND),
        new xxx(IGameList.class, L2.GAMELIST_BEGIN),
        new xxx(IGameList.class, L2.GAMELIST_ITEM),
        new xxx(IGameStartedResult.class, L2.GAME_RESULT),
        new xxx(IGameStartedResult.class, L2.GAME_STARTED),
        new xxx(IKeyVersion.class, L2.KEY_VERSION),
        new xxx(IMyGame.class, L2.EXAMINED_GAME_IS_GONE),
        new xxx(IMyGame.class, L2.MY_GAME_ENDED),
        new xxx(IMyGame.class, L2.MY_GAME_RESULT),
        new xxx(IMyGame.class, L2.MY_GAME_STARTED),
        new xxx(IMyTurn.class, L2.MY_TURN),
        new xxx(IMyVariable.class, L2.MY_STRING_VARIABLE),
        new xxx(IMyVariable.class, L2.MY_VARIABLE),
        new xxx(INotifyArrivedLeft.class, L2.NOTIFY_ARRIVED),
        new xxx(INotifyArrivedLeft.class, L2.NOTIFY_LEFT),
        new xxx(INotifyList.class, L2.MY_NOTIFY_LIST),
        new xxx(INotifyPlayerState.class, L2.NOTIFY_STATE),
        new xxx(IObserving.class, L2.STARTED_OBSERVING),
        new xxx(IObserving.class, L2.STOP_OBSERVING),
        new xxx(IPartnership.class, L2.PARTNERSHIP),
        new xxx(IPersonInMyChannel.class, L2.PEOPLE_IN_MY_CHANNEL),
        new xxx(IPersonInMyChannel.class, L2.COMMAND),
        new xxx(IPersonalQTell.class, L2.PERSONAL_QTELL),
        new xxx(IPersonalTell.class, L2.PERSONAL_TELL),
        new xxx(IPersonalTellEcho.class, L2.PERSONAL_TELL_ECHO),
        new xxx(IPlayerArrived.class, L2.PLAYER_ARRIVED),
        new xxx(IPlayerArrived.class, L2.PLAYER_LEFT),
        new xxx(IPlayerArrivedSimple.class, L2.PLAYER_ARRIVED_SIMPLE),
        new xxx(IPlayerArrivedSimple.class, L2.PLAYER_LEFT),
        new xxx(IPlayerState.class, L2.STATE),
        new xxx(IRatingInfo.class, L2.NEW_MY_RATING),
        new xxx(IRatingInfo.class, L2.WILD_KEY),
        new xxx(IRefresh.class, L2.REFRESH),
        new xxx(ISeek.class, L2.SEEK),
        new xxx(ISeek.class, L2.SEEK_REMOVED),
        new xxx(IMyGame.class, L2.MSEC),
        new xxx(IMyGame.class, L2.SEND_MOVES),
        new xxx(IShout.class, L2.SHOUT),
        new xxx(IMyGame.class, L2.TAKEBACK),
        new xxx(IMyGame.class, L2.OFFERS_IN_MY_GAME),
        new xxx(ITitlesChanged.class, L2.TITLES),
        new xxx(ITourney.class, L2.REMOVE_TOURNEY),
        new xxx(ITourney.class, L2.TOURNEY),
        new xxx(ITourney.class, L2.TOURNEY_GAME_ENDED),
        new xxx(ITourney.class, L2.TOURNEY_GAME_STARTED),
        new xxx(IWebAuth.class, L2.WEB_AUTH),
        new xxx(IMoveList.class, L2.MOVE_LIST)
    };

    private HashMap<L2, Integer> l2counts = new HashMap<L2, Integer>();

    private L2[] startL2(IAbstractICCHandler h) {
        LOG.trace("startL2 " + h.getClass().getCanonicalName());
        ArrayList<L2> newl2 = new ArrayList<L2>();
        for(xxx x : assignments_array) {
            for(Class<?> cl : h.getClass().getInterfaces()) {
                if(cl.equals(x.clazz)) {
                    LOG.trace("startL2 matches " + x.clazz.getCanonicalName() + ", count is " + l2counts.get(x.l2));
                    if(!l2counts.containsKey(x.l2)) {
                        newl2.add(x.l2);
                        l2counts.put(x.l2,  1);
                    } else {
                        l2counts.put(x.l2,  l2counts.get(x.l2) + 1);
                    }
                }
            }
        }
        LOG.trace("startL2 end");
        L2[] a = new L2[newl2.size()];
        return newl2.toArray(a);
    }
    
    private L2[] endL2(IAbstractICCHandler h) {
        LOG.trace("endL2 " + h.getClass().getCanonicalName());
        ArrayList<L2> newl2 = new ArrayList<L2>();
        for(xxx x : assignments_array) {
            for(Class<?> cl : h.getClass().getInterfaces()) {
                if(cl.equals(x.clazz)) {
                    if(l2counts.containsKey(x.l2)) {
                        Integer count = l2counts.get(x.l2) - 1;
                        LOG.trace("startL2 matches " + x.clazz.getCanonicalName() + ", count is " + count);
                        if(count == 0) {
                            l2counts.remove(x.l2);
                            newl2.add(x.l2);
                        } else {
                            l2counts.put(x.l2,  count);
                        }
                    } else {
                        LOG.error("We should not be removing a handler where there is no level 2 in the count map to remove! " + x.clazz.getCanonicalName() + "," + x.l2);
                    }
                }
            }
        }
        LOG.trace("endL2 end");
        L2[] a = new L2[newl2.size()];
        return newl2.toArray(a);
    }
}
