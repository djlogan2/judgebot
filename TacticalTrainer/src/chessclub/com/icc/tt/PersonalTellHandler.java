package chessclub.com.icc.tt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import org.springframework.context.MessageSource;

import chessclub.com.enums.Rating;
import chessclub.com.icc.DefaultICCAdminCommands;
import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IGetpx;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.Getpx;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l2.PersonalTell;
import chessclub.com.icc.tt.entity.TacticUserStat;
import chessclub.com.icc.tt.entity.TacticsTable;
import chessclub.com.icc.tt.entity.TacticsUser;
import chessclub.com.icc.tt.ifac.IDatabaseService;
import david.logan.chess.support.MoveList.moveIterator;

public class PersonalTellHandler extends DefaultICCAdminCommands implements IGetpx {

    private PersonalTell ptPacket;
    private ICCInstance icc;
    private Locale locale;
    
    private MessageSource messageSource;
    private IDatabaseService databaseService;

    public PersonalTellHandler(MessageSource messageSource, IDatabaseService databaseService, ICCInstance icc, PersonalTell p) {
        this.messageSource = messageSource;
        this.databaseService = databaseService;
        ptPacket = p;
        this.icc = icc;
        this.setInstance(icc,  this);
    }
    
    @Override
    public void initHandler() {
        getpx(ptPacket.name(), "forlanguage");
    }

    @Override
    public void iccException(ICCException ex) {


    }

    @Override
    public void setInstance(ICCInstance icc) {


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
    public void getpx(Getpx p) {
        locale = new Locale(p.getLanguage());
        if(ptPacket == null || ptPacket.text() == null) return;
        
        String[] parms = ptPacket.text().split("\\s+");
        parms[0] = parms[0].toLowerCase();
        // "status"
        icc.removeHandler(this);
        if("check".equals(parms[0])) {
        	for(int x = 0 ; x < parms.length ; x++) {
        		System.out.println("parm " + x + " is " + parms[x]);
        	}
        	//Fen fen = new Fen("");
        	
        } else if(parms[0].startsWith("s")) {
            TacticsUser user = databaseService.getUser(p.getUserid(), p.getUniqueId(), p.getRating(Rating.STANDARD));
            if(user == null) {
                _xtell("notinsystem");
                return;
            }
            String rating = Integer.toString(user.getRating());
            
            if(user.getGamecount() < 20)
                rating += "/P" + Integer.toString(user.getGamecount());
            
            _xtell("yourrating", rating);
            
            if(user.getGamecount() >= 20)
                _xtell("solved1", user.getGamecount(), databaseService.getRanking(user));
            else
                _xtell("solved2", user.getGamecount());
            return;
            // "dbstats"
        } else if(parms[0].startsWith("da")) { // database
            DatabaseStatistics ds = databaseService.getStatistics();
            int total = 0;
            for(int i : ds.getTacticalCounts())  {
                total += i;
            }
            //xtell(ptPacket.name(), "(test) Total tactics         : " + total);
            //xtell(ptPacket.name(), "(test) Average tactic rating : " + ds.getAvgTacticRating());
            _xtell("database1", total);
            _xtell("database2", ds.getAvgTacticRating());
            total = 0;
            for(int i : ds.getNumberPlayers())  {
                total += i;
            }
            _xtell("database3", total);
            _xtell("database4", ds.getAvgPlayerRating());
            _xtell("database5", ds.getHighestRated(), ds.getHighRating());
            _xtell("database6", ds.getMostTacticsPlayed(), ds.getGamesPlayed());
            //xtell(ptPacket.name(), "(test) Total players         : " + total);
            //xtell(ptPacket.name(), "(test) Average player rating : " + ds.getAvgPlayerRating());
            //xtell(ptPacket.name(), "(test) Highest rated player  : " + ds.getHighestRated() + " with a rating of " + ds.getHighRating());
            //xtell(ptPacket.name(), "(test) Player with most games: "+ ds.getMostTacticsPlayed() + " with " + ds.getGamesPlayed() + " games played");
            return;
        } else if(parms[0].toLowerCase().startsWith("b")) { // best
        	int games = 20;
        	if(parms.length > 2) {
        		_xtell("invalidbest");
        		//xtell(ptPacket.name(), "(test) Invalid command. Format is 'best' or 'best ##'");
        		return;
        	} else if(parms.length == 2) {
        		try {
        			games = Integer.parseInt(parms[1]);
        		} catch(NumberFormatException e) {
        			_xtell("invalidbest");
	        		//xtell(ptPacket.name(), "(test) Invalid command. Format is 'best' or 'best ##'");
	        		return;
        		}
        	}
        	ArrayList<TacticUserStat> s = databaseService.rankByRating(games, null);
        	Iterator<TacticUserStat> itus = s.iterator();
        	ArrayList<String> top20 = new ArrayList<String>();
    		//top20.add("(test)");
    		//top20.add("Rank     Rtng Username                       Corr  Wrong Best Last Played");
        	top20.add(databaseService.getMessage(locale, "rankheader"));
    		top20.add("------   ---- ------------------------------ ----- ----- ---- -----------------------");
        	for(int i = 0 ; i < 20 && itus.hasNext() ; i++) {
        		TacticUserStat tus = itus.next();
        		top20.add(String.format("%-6d   %4d %-30s %5d %5d %4d %-23s",
        				tus.rank, tus.rating, tus.username, tus.correct, (tus.played - tus.correct), tus.highestrating, tus.lastplayed.toString()));
        	}
        	this.qtell(ptPacket.name(),  top20);
        	return;
        } else if(parms[0].toLowerCase().startsWith("de")) { // detail
        	String who;
        	Integer[][] rd;
        	if(parms.length > 2) {
        		_xtell("invaliddetail");
        		return;
        	} else if(parms.length == 2) {
        		rd = databaseService.ratingDetail(parms[1]);
        		who = parms[1];
        	} else {
        		rd = databaseService.ratingDetail(ptPacket.name());
        		who = ptPacket.name();
        	}
        	
        	if(rd == null) {
        		_xtell("ranknotfound", who);
        		return;
        	}
        	
        	ArrayList<String> detail = new ArrayList<String>();
        	
    		detail.add("  Range     Corr  Wrong  % Corr  % Wrong");
    		detail.add("---------   ----- ----- -------- --------");
        	for(Integer[] line : rd)
        	{
        		int rating = line[0].intValue();
        		int right = line[1].intValue();
        		int wrong = line[2].intValue();
        		detail.add(String.format("%-4d-%-4d   %5d %5d %7.3f%% %7.3f%%",
        				rating, rating+199, right, wrong,
        				100.0 * (double)right / ((double)right+(double)wrong),
        				100.0 * (double)wrong / ((double)right+(double)wrong)
        				));
        	}
        	this.qtell(ptPacket.name(),  detail);
        	return;
        } else if(parms[0].toLowerCase().startsWith("r")) { // rank
        	ArrayList<TacticUserStat> s;
        	String who;
        	if(parms.length > 2) {
        		_xtell("invalidrank");
        		//xtell(ptPacket.name(), "(test) Invalid command. Format is 'rank' or 'rank <username>'");
        		return;
        	} else if(parms.length == 2) {
        		s = databaseService.rankByRating(20,  parms[1]);
        		who = parms[1];
        	} else {
        		s = databaseService.rankByRating(20,  ptPacket.name());
        		who = ptPacket.name();
        	}
        	if(s == null || s.size() == 0) {
        		_xtell("ranknotfound", who);
        		//xtell(ptPacket.name(), "(test) No user by the name of '" + who + "' was found, or user has not played 20 games");
        		return;
        	}
        	Iterator<TacticUserStat> itus = s.iterator();
        	ArrayList<String> top20 = new ArrayList<String>();
    		//top20.add("(test)");
    		//top20.add("Rank     Rtng Username                       Corr  Wrong Best Last Played");
        	top20.add(databaseService.getMessage(locale, "rankheader"));
    		top20.add("------   ---- ------------------------------ ----- ----- ---- -----------------------");
        	for(int i = 0 ; i < 20 && itus.hasNext() ; i++) {
        		TacticUserStat tus = itus.next();
        		if(who.equalsIgnoreCase(tus.username))
        			top20.add("=====================================================================================");
        		top20.add(String.format("%-6d   %4d %-30s %5d %5d %4d %-23s",
        				tus.rank, tus.rating, tus.username, tus.correct, (tus.played - tus.correct), tus.highestrating, tus.lastplayed.toString()));
        		if(who.equalsIgnoreCase(tus.username))
        			top20.add("=====================================================================================");
        	}
        	this.qtell(ptPacket.name(),  top20);
        	return;
        } else if(ptPacket.text().toLowerCase().startsWith("massmessage ") && ptPacket.name().equals("djlogan")) {
        	// It'll add itself to the list of handlers, do its thing, then remove itself from the list of handlers
        	@SuppressWarnings("unused")
			MassMessage mm = new MassMessage(messageSource, databaseService, icc, ptPacket.text().substring(12));
        	return;
        } else if("tactic".equals(parms[0]) &&
        		(ptPacket.titles().isHelper() || ptPacket.titles().isOnDutyAdministrator())) {
        	if(parms.length != 2) {
        		_xtell("invalidtactic");
        		//xtell(ptPacket.name(), "(test) Invalid command. Please use 'tactic ####', where #### is a tactic id");
        		return;
        	}
        	int id;
        	try {
        		id = Integer.parseInt(parms[1]);
        	} catch(Exception e) {
        		_xtell("invalidtactic");
        		//xtell(ptPacket.name(), "(test) Invalid command. Please use 'tactic ####', where #### is a tactic id");
        		return;
        	}
        	TacticsTable tt = databaseService.getTactic(id);
        	if(tt == null) {
        		_xtell("tacticnotexist", parms[1]);
        		//xtell(ptPacket.name(), "(test) Tactic " + parms[1] + " does not exist");
        		return;
        	}
        	String moves = "";
        	String fen;
        	moveIterator mi = tt.getMoves().iterator();
        	try {
        		fen = tt.getFen().getFEN();
        	} catch(Exception e) {
        		fen = "INVALID FEN IN DATABASE";
        	}
        	try {
        		while(mi.hasNext()) moves += mi.next().getShortSmithMove() + " ";
        	} catch(Exception e) {
        		moves = "INVALID MOVES IN DATABASE";
        	}
        	String[] _qtell = new String[] {
        	"fen          | " + fen,
        	"moves        | " + moves,
        	"rating       | " + tt.getRating(),
        	"id           | " + tt.getId(),
        	"played       | " + tt.getPlayed(),
        	"tomove       | " + tt.getTomove(),
        	"good         | " + tt.getgood(),
        	"badreason    | " + tt.getBadreason(),
        	"sourcepgn_id | ",
        	"winning      | " + tt.getWinning()
        	};
        	qtell(ptPacket.name(), _qtell);
        	return;
        }
        
    	xtell(ptPacket.name(), "help, database, detail {user}, stats, best {##}, rank {user}");
    	if(ptPacket.titles().isOnDutyAdministrator())
    		xtell(ptPacket.name(), "tactic ####");
    }

    protected void _xtell(String code, Object ... parameters) {
        String msg = messageSource.getMessage(code, parameters, locale);
        super.xtell(ptPacket.name(),  msg);
    }
    
    @Override
    public void shuttingDown() {


    }

    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
        // TODO Auto-generated method stub
        
    }
}
