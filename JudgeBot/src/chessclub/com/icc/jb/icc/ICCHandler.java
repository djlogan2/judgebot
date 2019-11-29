package chessclub.com.icc.jb.icc;

import java.io.StringReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import chessclub.com.enums.CN;
import chessclub.com.icc.DefaultICCAdminCommands;
import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.handler.interfaces.IChannelsShared;
import chessclub.com.icc.handler.interfaces.IGameStartedResult;
import chessclub.com.icc.handler.interfaces.ILoggedOut;
import chessclub.com.icc.handler.interfaces.ILoginFailed;
import chessclub.com.icc.handler.interfaces.INewMessage;
import chessclub.com.icc.handler.interfaces.INotifyArrivedLeft;
import chessclub.com.icc.handler.interfaces.INotifyList;
import chessclub.com.icc.handler.interfaces.IPersonInMyChannel;
import chessclub.com.icc.handler.interfaces.IPersonalTell;
import chessclub.com.icc.handler.interfaces.IPlayerState;
import chessclub.com.icc.handler.interfaces.IWhoAmI;
import chessclub.com.icc.jb.UCIThreadManager;
import chessclub.com.icc.jb.timeDelay;
import chessclub.com.icc.jb.entity.DisconnectStats;
import chessclub.com.icc.jb.entity.GameLog;
import chessclub.com.icc.jb.ifac.IDatabaseService;
import chessclub.com.icc.jb.ifac.JudgeBotCommandInterface;
import chessclub.com.icc.jb.ifac.timeDelayHandlerInterface;
import chessclub.com.icc.jb.parser.JudgeBotCommandParser;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.LoggedOut;
import chessclub.com.icc.l2.ChannelsShared;
import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;
import chessclub.com.icc.l2.LoginFailed;
import chessclub.com.icc.l2.MessageListItem;
import chessclub.com.icc.l2.MyNotifyList;
import chessclub.com.icc.l2.PersonInMyChannel;
import chessclub.com.icc.l2.PersonalTell;
import chessclub.com.icc.l2.PlayerArrived;
import chessclub.com.icc.l2.PlayerStatePacket;
import chessclub.com.icc.l2.WhoAmI;
import david.logan.chess.support.Color;
import david.logan.chess.support.GameScore;
import david.logan.custom.collections.LimitedSet;

public class ICCHandler extends DefaultICCAdminCommands
    implements timeDelayHandlerInterface, IPersonalTell, IPlayerState,
                IAcceptGenericPackets, ILoginFailed, INewMessage, INotifyList, INotifyArrivedLeft,
                IChannelsShared, IPersonInMyChannel, IGameStartedResult, ILoggedOut, IWhoAmI,
                JudgeBotCommandInterface {

    private boolean testOnly = true;
    private static final Logger log = LogManager.getLogger(ICCHandler.class);
    private HashMap<Integer,String[]> gamenumberToID = new HashMap<Integer,String[]>();
    private ICCInstance icc = null;
    private MessageSource messageSource;
    private IDatabaseService databaseService;
    private ICCBean iccBean;
    private LimitedSet<PersonalTell> last100tells = new LimitedSet<PersonalTell>(100);

    public ICCHandler(MessageSource messageSource, IDatabaseService databaseService, ICCBean iccBean, boolean testOnly, String adminPassword) {
        this.messageSource = messageSource;
        this.databaseService = databaseService;
        this.iccBean = iccBean;
        this.testOnly = testOnly;
        this.setAdminPassword(adminPassword);
    }
    
    @Override
    public void initHandler() {
    }

    public void whoAmI(WhoAmI p) {
        setNoAutologout(true);
        onDuty(true);
        addChannel(100);
    }

    @Override
    public void personalTell(PersonalTell p) {
        if (log.isDebugEnabled()) {
            log.debug("JBICCHandler PersonalTell " + p.name() + " " + p.text());
        }
        
        last100tells.add(p);
        JudgeBotCommandParser parser = new JudgeBotCommandParser(new StringReader(p.text()));
        
        boolean isHelper = false;
        if(!p.titles().isH() && inchannel100.contains(p.name())) {
            log.debug("Faking H for " + p.name() + " because they are in channel 100");
            isHelper = true;
        }
        
        try {
            parser.parse(p, isHelper, this);
        } catch (Error e) {
            log.error("Command parse exception: " + e.getMessage());
            log.info("Personal tell from " +p.name()+": "+p.text());
            if(!p.titles().isC())
                spoof(p.name(),"help judgebot");
            return;
        } catch (Exception e) {
            log.error("Command parse exception: " + e.getMessage());
            log.info("Personal tell from " +p.name()+": "+p.text());
            if(!p.titles().isC())
                spoof(p.name(),"help judgebot");
            return;
        }
    }


    public void badCommand1(BadCommand p) {
        log.error("ICCHandler BadCommand: " + p.command());
    }

    @Override
    public void gameStarted(GameStarted p)
    {
    	synchronized(this)
    	{
    		if(!expiredDelay.isEmpty())
    		{
    			String[] fromDelay = expiredDelay.pop();
    			log.info("(From time delay) Processing adjourned game between "+fromDelay[0]+" and "+fromDelay[1]);
    			icc.addHandler(new AdjudicateBase(icc, messageSource, databaseService, iccBean, new String[]{fromDelay[0],fromDelay[1]}, testOnly));
    		}
    	}
		
		if(p.level1Type(0) == CN.RESUME)
		{
    		log.info("Removing adjourned game between "+p.whitename()+" and "+p.blackname()+" from the delay queue");
			timeDelay.getInstance().removeWait(p.whitename(), p.blackname());
		}
    	gamenumberToID.put(p.gamenumber(), new String[]{p.whitename(),p.blackname()});
    }
    
    @Override
    public void gameResult(GameResultPacket p) {
        log.debug("GameResult: " + p.gameresult() + "/" + p.gamescore());
        String[] names = gamenumberToID.remove(p.gamenumber());
        if(names != null && p.gamescore() == GameScore.ADJOURNED)
        {
        	log.info("Putting adjourned game between "+names[0]+" and "+names[1]+" on the delay queue");
        	timeDelay.getInstance().addWait(names[0], names[1]);
        	return;
        }
        return;
    }

    @SuppressWarnings("unused")
    private void locale_xtell(String user, String key, String[] args)
    {
    	icc.addHandler(new LocaleXtell(databaseService, messageSource, user, key, args));
    }
    private void locale_xtell(String user, Object[] args)
    {
    	icc.addHandler(new LocaleXtell(databaseService, messageSource, user, args));
    }
    /**
     * We should never get a PlayerStatePacket unless somebody has forced us to 
     */
    @Override
    public void playerState(PlayerStatePacket p) {
    	if(p.level1Type(0) != CN.MEXAMINE)
    	{
    		log.error("We are in PlayerState but we don't know why. Ignoring. p="+p);
    		return;
    	}
    }

    Stack<String[]> expiredDelay = new Stack<String[]>();

    @Override
	synchronized public void timeDelayExpired(String white, String black) {
		expiredDelay.push(new String[]{white,black});
	}

    @Override
    public void newMessage(MessageListItem item) {
        log.info("Message from "+item.sender()+": "+item.message());
        forwardMessage(item.index(), "");
        clearmessage(item.index());
    }

    @Override
    public void iccException(ICCException ex) {
    }

    @Override
    public void setInstance(ICCInstance icc) {
        super.setInstance(icc, this);
        this.icc = icc; 
    }

    @Override
    public void loginFailed(LoginFailed p) {
        log.error("Login failed. Ending");
        this.iccBean.end();
    }

    private static ArrayList<String> notifyList = new ArrayList<String>();
    public static ArrayList<String> getNotifyList() { return notifyList; }
    
    @Override
    public void myNotifyList(MyNotifyList p) {
        if(p.added()) {
            if(!notifyList.contains(p.user()))
                    notifyList.add(p.user());
        } else {
            notifyList.remove(p.user());
        }
    }

    @Override
    public void notifyArrived(PlayerArrived p) {
        if(timeDelay.getInstance().queueSize() < 5 && AdjudicateBase.getCurrentExamineQueue() < 5)
            xtell(p.getUser(), "Hi! I appear to be running OK");
        else {
            xtell(p.getUser(), "Hi! One of my queues seems to be getting big. Am I OK?");
        }
    }

    private ArrayList<String> inchannel100 = new ArrayList<String>();
    
    @Override
    public void channelsShared(ChannelsShared p) {
        for(int channel : p.channels()) {
            if(channel != 100)
                leaveChannel(channel);
            else if(!inchannel100.contains(p.name())) {
                log.debug("Adding "+p.name()+" to inchannel100");
                inchannel100.add(p.name());
            }
        }
    }

    @Override
    public void personInMyChannel(PersonInMyChannel p) {
        if(p.channel() != 100) {
            leaveChannel(p.channel());
            return;
        }
        if(p.entered()) {
            if(!inchannel100.contains(p.name())) {
                log.debug("Adding "+p.name()+" to inchannel100");
                inchannel100.add(p.name());
            }
        } else {
            log.debug("Removing "+p.name()+" from inchannel100");
            inchannel100.remove(p.name());
        }
    }

    @Override
    public void notifyLeft(String name) {
    }

    @Override
    public void connectionClosed() {
    }

    @Override
    public void badCommand2(L1ErrorOnly p) {
        if(p.getType() == CN.ADMIN) {
            log.error("Admin authenticate failed. Ending");
            this.iccBean.end();
            System.exit(-4);
        }
    }

    @Override
    public void shuttingDown() {
    }

    @Override
    public void loggedOut(LoggedOut l) {
        int x = 0;
        String msg = null;
        do {
            msg = l.getLogoutMessage(x++);
            if(msg != null) {
                log.error(msg);
            }
        } while(msg != null);
    }

    @Override
    public void help(PersonalTell user) {
        log.debug("help("+user.name()+")");

        ArrayList<String> helplines = new ArrayList<String>();
      helplines.add("User commands:");
      helplines.add("   nowin - Instructs JudgeBot to stop awarding you wins, even if your opponent disconnects");
      helplines.add("   win - Instructs JudgeBot to start awarding you wins again (if you have previously requested 'nowin'.)");
      helplines.add("   info {opponent} - Opponent can omitted or it can be a full or partial id.");
      helplines.add("       Lists up to 20 games between you and opponent(s) and the result.");
      helplines.add("       MANUAL means the game must be manually adjudicated.");
      helplines.add("       LOSE means the disconnector lost the game.");
      helplines.add("       (other results are self-explanatory.");
      helplines.add("       The score is from the disconnectors point of view. A -5 means the disconnector is down a rook.");
      if(user.titles().isOnDutyAdministrator() || user.titles().isH() || inchannel100.contains(user.name())) {
          helplines.add("Admin/helper commands:");
          helplines.add("   summary {user1} {user2} -- Partial IDs are ok");
          helplines.add("     Identical to the 'info' command, except internally, 'info' implies the issuing user as the first argument.");
          helplines.add("     In other words, if user 'xyz' issues 'info abc', you can enter 'summary xyz abc' for the same result.");
          helplines.add("   status - Shows the status of the various queues and other variables");
          helplines.add("   lasttells - Show the last 100 personal tells sent to JudgeBot");
          helplines.add("   resetmsg - Don't use this unless you are instructed. For use by those on JudgeBots notify list.");
          helplines.add("      Will tell JudgeBot to send the notify list messages when the examine queue gets too high again");
          helplines.add("   killgame - Don't use this unless you are instructed.");
          helplines.add("      Unexamines and skips the game currently being examined. It will also auto-reinstate 'resetmsg' once the examine queue falls below 5 again.");
      }
      if(user.titles().isOnDutyAdministrator()) {
          helplines.add("Administrator commands:");
          helplines.add("   nolose <handle> - Tells JudgeBot that this person cannot lose a game by disconnecting.");
          helplines.add("   lose <handle> - Tells JudgeBot that this person can lose by disconnecting (if a previous 'nolose' was issued.)");
      };
      if("djlogan".equals(user.name()) || "FREEBIRD".equals(user.name()))
          helplines.add("System status commands: memory, os, runtime");
      qtell(user.name(), helplines);
    }


    @Override
    public void lasttells(PersonalTell user, boolean isHelper) {
        log.debug("status("+user.name()+")");
        if(!user.titles().isOnDutyAdministrator() && !isHelper)
            return;
        ArrayList<String> cmds = new ArrayList<String>(last100tells.size());
        for(Iterator<PersonalTell> pti = last100tells.iterator() ; pti.hasNext() ;) {
            PersonalTell pt = pti.next();
            cmds.add(String.format("%-15s %s", pt.name(), pt.text()));
        }
        qtell(user.name(), cmds);
    }

    @Override
    public void status(PersonalTell user, boolean isHelper) {
        log.debug("status("+user.name()+")");
        try {
        if(!user.titles().isOnDutyAdministrator() && !isHelper) {
            return;
        }
      locale_xtell(user.name(),new Object[]{
          "icc.test.only", new Object[]{testOnly},
          "engine.count", new Object[]{UCIThreadManager.activeEngines(),UCIThreadManager.maxEngines()},
          "engine.queue", new Object[]{UCIThreadManager.getCurrentQueueSize(),UCIThreadManager.getMaxQueueSize()},
          "timedelay.queue", new Object[]{timeDelay.getInstance().queueSize(), timeDelay.getInstance().maxQueueSize()},
          "examine.queue", new Object[]{AdjudicateBase.getCurrentExamineQueue(), AdjudicateBase.getMaxExamineQueue()},
          "adjudicate.delay.seconds", new Object[]{timeDelay.getInstance().getTimeDelay()}
      });
        } catch(Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    @Override
    public void summary(PersonalTell admin, boolean isHelper, String user1, String user2) {
        boolean exact = false;
        
        log.debug("summary("+admin.name()+","+user1+","+user2+")");

        if(!admin.titles().isOnDutyAdministrator() && !isHelper) {
            if(!admin.name().equals(user1)) {
                return;
            } else {
                exact = true;
            }
        };
        
        ArrayList<String> qtells = new ArrayList<String>();
        
        if(admin.titles().isOnDutyAdministrator() || isHelper)
        {
	        if(user1 != null)
	        {
	        	DisconnectStats ds = databaseService.getDisconnectStats(user1);
	        	if(ds != null)
	        	{
	        		qtells.add(String.format("%s Disconnected %d times resulting in %d losses, %d manual adjudications, %d draws, %d wins, and %d aborts", user1,
	        				ds.disconnected.aborted + ds.disconnected.drawn + ds.disconnected.lost + ds.disconnected.manual + ds.disconnected.won,
	        				ds.disconnected.lost, ds.disconnected.manual, ds.disconnected.drawn, ds.disconnected.won, ds.disconnected.aborted));
	        		qtells.add(String.format("%s was a victim %d times resulting in %d losses, %d manual adjudications, %d draws, %d wins, and %d aborts", user1,
	        				ds.victim.aborted + ds.victim.drawn + ds.victim.lost + ds.victim.manual + ds.victim.won,
	        				ds.victim.lost, ds.victim.manual, ds.victim.drawn, ds.victim.won, ds.victim.aborted));
	        	}
	        }
	        
	        if(user2 != null)
	        {
	        	DisconnectStats ds = databaseService.getDisconnectStats(user2);
	        	if(ds != null)
	        	{
	        		qtells.add(String.format("%s Disconnected %d times resulting in %d losses, %d manual adjudications, %d draws, %d wins, and %d aborts", user2,
	        				ds.disconnected.aborted + ds.disconnected.drawn + ds.disconnected.lost + ds.disconnected.manual + ds.disconnected.won,
	        				ds.disconnected.lost, ds.disconnected.manual, ds.disconnected.drawn, ds.disconnected.won, ds.disconnected.aborted));
	        		qtells.add(String.format("%s was a victim %d times resulting in %d losses, %d manual adjudications, %d draws, %d wins, and %d aborts", user2,
	        				ds.victim.aborted + ds.victim.drawn + ds.victim.lost + ds.victim.manual + ds.victim.won,
	        				ds.victim.lost, ds.victim.manual, ds.victim.drawn, ds.victim.won, ds.victim.aborted));
	        	}
	        }
        }
        
        Page<GameLog> page = databaseService.listGames(PageRequest.of(0, 25), user1, exact, user2, true);
        if(page.getContent().size() == 0) {
            qtell(admin.name(), "JudgeBot: No games found");
            return;
        }
        for(GameLog gl : page.getContent()) {
            String wd = "";
            String bd = "";
    
            String strdate;
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            strdate = df.format(gl.getAdjdate());
    
            if(gl.getWhodisconnected() == Color.WHITE)
                wd = "[D]";
            else if(gl.getWhodisconnected() == Color.BLACK)
                bd = "[D]";
    
            String score;
            if(gl.isAdjudicationSkipped()) {
                score = "N/A";
            } else if(gl.isIsmate()) {
                score = String.format("M/%2d", Math.abs(gl.getScore()));
            } else {
                score = String.format("%5.2f", gl.getScoredouble());
            }
            qtells.add(
                String.format("%20s %15s%3s %15s%3s  %6s %5s",
                        strdate, gl.getWhite(), wd, gl.getBlack(), bd, gl.getAction().toString(), score)
            );
        }
        qtell(admin.name(), qtells);
    }

    @Override
    public void win(PersonalTell p, boolean add) {
        log.debug("win("+p.name()+","+add+")");
        this.addHandler(new WinExempt(icc, databaseService, messageSource, p.name(), add));
    }

    @Override
    public void info(PersonalTell p, String otheruser) {
        log.debug("info("+p.name()+","+otheruser+")");
        this.summary(p, false, p.name(), otheruser);
    }

    @Override
    public void lose(PersonalTell p, String user, boolean add) {
        log.debug("lose("+p.name()+","+add+")");
        this.addHandler(new LoseExempt(databaseService, messageSource, p.name(), user, add));
    }

    @Override
    public void runtime(PersonalTell p) {
        log.debug("win("+p.name()+")");
        if(!p.titles().isOnDutyAdministrator()) {
            return;
        };
        ArrayList<String> messages = new ArrayList<String>();
      RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
      messages.add("----- JudgeBot runtime information -----");
      messages.add("ClassPath               : "+rt.getClassPath());
      messages.add("LibraryPath             : "+rt.getLibraryPath()); 
      messages.add("ManagementSpecVersion   : "+rt.getManagementSpecVersion());
      messages.add("Name                    : "+rt.getName()); 
      messages.add("SpecName                : "+rt.getSpecName()); 
      messages.add("SpecVendor              : "+rt.getSpecVendor()); 
      messages.add("SpecVersion             : "+rt.getSpecVersion()); 
      messages.add("StartTime               : "+rt.getStartTime()); 
      messages.add("Uptime                  : "+rt.getUptime()); 
      messages.add("VmName                  : "+rt.getVmName()); 
      messages.add("VmVendor                : "+rt.getVmVendor()); 
      messages.add("VmVersion               : "+rt.getVmVersion()); 
      messages.add("isBootClassPathSupported: "+rt.isBootClassPathSupported());
      messages.add("-----           Input Arguments    -----");
      messages.addAll(rt.getInputArguments()); // rt.getInputArguments().toArray(qt));
      messages.add("-----        System Properties     -----");
      int maxlen = 0;
      String dots = "";
      for(String prop : rt.getSystemProperties().keySet())
          maxlen = (maxlen<prop.length()?prop.length():maxlen);
      maxlen += 3;
      for(int i=0;i<maxlen;i++) dots += '.';
      for(String prop : rt.getSystemProperties().keySet())
          messages.add(prop+dots.substring(0, maxlen-prop.length())+rt.getSystemProperties().get(prop));
      messages.add("-----              (end)           -----");
      String[] q = new String[messages.size()];
      qtell(p.name(), messages.toArray(q));
    }

    @Override
    public void memory(PersonalTell p) {
        log.debug("memory("+p.name()+")");
        if(!p.titles().isOnDutyAdministrator()) {
            return;
        };
      MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
      MemoryUsage heap = memory.getHeapMemoryUsage();
      MemoryUsage nonheap = memory.getNonHeapMemoryUsage();
      qtell(p.name(), new String[] {
          "Memory usage: Heap   : (Committed: "+heap.getCommitted()+" init:"+heap.getInit()+" max:"+heap.getMax()+" used:"+heap.getUsed()+")",
          "Memory usage: NonHeap: (Committed: "+nonheap.getCommitted()+" init:"+nonheap.getInit()+" max:"+nonheap.getMax()+" used:"+nonheap.getUsed()+")"});
    }

    @Override
    public void os(PersonalTell p) {
        log.debug("os("+p.name()+")");
        if(!p.titles().isOnDutyAdministrator()) {
            return;
        };
      OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
      qtell(p.name(), "Operating System: (arch: "+os.getArch()+" #proc:"+os.getAvailableProcessors()+" name:"+os.getName()+" avgload: "+os.getSystemLoadAverage()+" version: "+os.getVersion()+")");
    }

    @Override
    public void reset(PersonalTell p) {
        AdjudicateBase.resetMessaged();
    }

    @Override
    public void killgame(PersonalTell p, boolean isHelper) {
        AdjudicateBase.kill();
    }

    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
    }

}
