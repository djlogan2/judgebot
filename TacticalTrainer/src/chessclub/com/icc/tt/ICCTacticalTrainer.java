package chessclub.com.icc.tt;

import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import chessclub.com.icc.DefaultICCCommands;
import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.handler.interfaces.IGameStartedResult;
import chessclub.com.icc.handler.interfaces.IGetpx;
import chessclub.com.icc.handler.interfaces.IManualAccept;
import chessclub.com.icc.handler.interfaces.IMySeek;
import chessclub.com.icc.handler.interfaces.IPersonalTell;
import chessclub.com.icc.handler.interfaces.IWhoAmI;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.Getpx;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.Play;
import chessclub.com.icc.l1.Seeking;
import chessclub.com.icc.l1.Unseeking;
import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;
//import chessclub.com.icc.l2.MoveList;
import chessclub.com.icc.l2.PersonalTell;
import chessclub.com.icc.l2.WhoAmI;
import chessclub.com.icc.tt.ifac.IDatabaseService;
import david.logan.chess.support.Color;
import david.logan.custom.collections.AgingList;

public class ICCTacticalTrainer extends DefaultICCCommands
    implements IAcceptGenericPackets, IWhoAmI, IPersonalTell, IMySeek, IManualAccept, IGameStartedResult, IGetpx {
    private static final Logger log = LogManager.getLogger(ICCTacticalTrainer.class);
    private ICCInstance icc;
    private String whoami;
    private AgingList<String> abusers = new AgingList<String>(10);
    private MessageSource messageSource;
    private IDatabaseService databaseService;
    @SuppressWarnings("unused")
    private ICCBean iccBean;

    @SuppressWarnings("unused")
    private ICCTacticalTrainer() {
    }
    
    public ICCTacticalTrainer(MessageSource messageSource, IDatabaseService databaseService, ICCBean iccBean) {
        this.messageSource = messageSource;
        this.databaseService = databaseService;
        this.iccBean = iccBean;
    }
    
    public void run() {
    	while(true) {
	        icc = new ICCInstance();
	        try {
	            icc.setInstance("chessclub.com", 5000, "", "", this, 4096);
	        } catch (Exception e) {
	            // We have handled the "are we accepting generic packets" issue
	            e.printStackTrace();
	        }
	        icc.start();
	        try {
	            icc.join();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
    	}
    }

    @Override
    public void initHandler() {

    }

    @Override
    public void iccException(ICCException ex) {

    }

    @Override
    public void setInstance(ICCInstance icc) {
        setInstance(icc, this);
        this.icc =icc;
    }

    @Override
    public void whoAmI(WhoAmI p) {
    	Thread.currentThread().setName("Tactical Trainer");
        whoami = p.userid();
        setNoAutologout(true);
        try {
            seek(20, 20, false, 20, Color.NONE, false, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void personalTell(PersonalTell p) {
    	if(abusers.add(p.name()) == 10) {
    		censor(p.name());
    		message( "djlogan", "I censored " + p.name() + " due to too many tells");
    		return;
    	}
        icc.addHandler(new PersonalTellHandler(messageSource, databaseService, icc, p));
    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void badCommand1(BadCommand p) {

    }

    @Override
    public void badCommand2(L1ErrorOnly p) {

    }

    @Override
    public void shuttingDown() {

    }

    @Override
    public void mySeek(Seeking p) {
        log.debug("We are now seeking, our index is " + p.index());
//        unseek(p.index());
    }

    @Override
    public void mySeekRemoved(Unseeking p) {
        log.debug("Our seek with index " + p.index() + " has been removed.");
    }
    
    @Override
    public void manualAccept(Play p) {
        TacticalTrainerGame game = new TacticalTrainerGame(messageSource, databaseService, whoami, p.opponent());
        icc.addHandler(game);

        log.debug(p.opponent() + " has requested a game with me");
    }

    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
    }

	@Override
	public void gameStarted(GameStarted p) {
		String checkName;
		if(this.whoami.equals("TacticalTrainer")) {
			if(p.whitename().equals("TrainingBot")) {
				checkName = p.blackname();
			} else if(p.blackname().equals("TrainingBot")) {
				checkName = p.whitename();
			} else
				return;
			if(!databaseService.hasBeenBothered(checkName)) {
				getpx(checkName, "bother");
			};
		}
	}

	@Override
	public void gameResult(GameResultPacket p) {
	}

	@Override
	public void getpx(Getpx p) {
		if(p == null || p.getLanguage() == null)
			return;
		if("bother".equals(p.getKey())) {
	        Locale locale = new Locale(p.getLanguage());
	        String msg = messageSource.getMessage("pleaseplay", null, locale);
	        if("TacticalTrainer".equals(whoami))
	        	message(p.getUserid(), msg);
	        else
	        	System.out.println("Skipping message to " + p.getUserid() + " because this is not the TacticalTrainer account");
		}
	}
}
