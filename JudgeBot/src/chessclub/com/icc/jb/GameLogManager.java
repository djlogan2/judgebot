package chessclub.com.icc.jb;

import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;

import chessclub.com.icc.jb.entity.AdjudicateRule;
import chessclub.com.icc.jb.entity.EngineRule;
import chessclub.com.icc.jb.entity.GameLog;
import chessclub.com.icc.jb.enums.JBAdjudicateAction;
import chessclub.com.icc.jb.ifac.IDatabaseService;
import chessclub.com.icc.jb.ifac.parserInterface;
import chessclub.com.icc.l2.GameStarted;
import chessclub.com.icc.parser.ICCParser;
import chessclub.com.icc.parser.ParseException;
import david.logan.chess.support.Color;

public class GameLogManager {

	private static final Logger log = LogManager.getLogger(GameLogManager.class);

	private IDatabaseService databaseService;
	
	@SuppressWarnings("unused")
	private GameLogManager() {
	}

	private int stored_game_id = -1;
	
	public GameLogManager(IDatabaseService databaseService, GameStarted p, Color disconnector, String fen, boolean testonly) {
	    log.debug("p="+p+",fen="+fen+",testonly="+testonly+",disconnector="+disconnector);
        this.databaseService = databaseService;
        GameLog gl = new GameLog();
        gl.setFen(fen.replaceAll("[^\\x00-\\x7f]", ""));
        gl.setDisconnector(disconnector);
        gl.setTestOnly(testonly);
        gl.setWhiteName(p.whitename());
        gl.setBlackName(p.blackname());
        databaseService.addGame(gl);
        stored_game_id = gl.getId();
	}

	public EngineRule getMatchingEngineRule(parserInterface ifac) {
	    log.debug("getMatchingEngineRule");
		Collection<EngineRule> engrules = databaseService.getActiveEngineRules(false);
		Iterator<EngineRule> i = engrules.iterator();
		while(i.hasNext()) {
			EngineRule er = i.next();
			boolean matched = false;
			
			try {
				matched = (new ICCParser(new StringReader(er.getRule()))).booleanRule(ifac);
			} catch (ParseException e) {
				e.printStackTrace();
				log.error("Should not have failed the parser: "+e.getMessage());
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Should not have failed the parser: "+e.getMessage());
				return null;
			}

			if(log.isDebugEnabled()) log.debug("Checking EngineRule "+er.getId()+", returned "+matched);
			
            databaseService.addMatchedMissedEngineRule(stored_game_id, er.getId(), matched);
			if (matched) {
	            databaseService.engineRuleHit(stored_game_id, true);
				return er;
			}
		}
        databaseService.engineRuleHit(stored_game_id, false);
		return null;
	}

	public JBAdjudicateAction getMatchingAdjudicationRule(parserInterface ifac) {
        log.debug("getMatchingAdjudicateRule");
		Collection<AdjudicateRule> adjrules = databaseService.getActiveAdjudicationRules(false);
		Iterator<AdjudicateRule> i = adjrules.iterator();
		while(i.hasNext()) {
			AdjudicateRule ar = i.next();
			boolean matched = false;
			
			try {
				matched = (new ICCParser(new StringReader(ar.getRule()))).booleanRule(ifac);
			} catch (ParseException e) {
				e.printStackTrace();
				log.error("Should not have failed the parser: "+e.getMessage());
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Should not have failed the parser: "+e.getMessage());
				return null;
			}

			if(log.isDebugEnabled()) log.debug("Checking AdjudicateRule "+ar.getId()+", returned "+matched);
			
            databaseService.addMatchedMissedAdjudicationRule(stored_game_id, ar.getId(), matched);
			if (matched) {
	            databaseService.adjudicationRuleHit(stored_game_id, true, ar.getAction());
				return ar.getAction();
			} else {
			    
			}
		}
        databaseService.adjudicationRuleHit(stored_game_id, false, null);
		return JBAdjudicateAction.MANUAL;
	}

	public void setScore(boolean ismate, int score) {
        log.debug("setScore("+ismate+","+score+")");
		databaseService.setScore(stored_game_id, ismate, score);
	}

	public void setTimes(int whitemsec, int blackmsec) {
        log.debug("setTimes("+whitemsec+","+blackmsec+")");
		databaseService.setTimes(stored_game_id, whitemsec, blackmsec);
	}

	public void storeLogVariable(String variable, String value) {
        log.debug("storeLogVariable("+variable+","+value+")");
		databaseService.setVariable(stored_game_id, variable, value);
	}

    public void setMoves(String lasttwomoves) {
        log.debug("setMoves("+lasttwomoves+")");
        try
        {
        	String ltm = lasttwomoves.replaceAll("[^\\x00-\\x7f]", "");
        databaseService.setMoves(stored_game_id, ltm);
        } catch(DataIntegrityViolationException e)
        {
        	e.printStackTrace();
        	log.error("setMoves exception mess: " + e.getMessage());
        	if(e.getCause() != null)
        		log.error("setMoves exception cause: " + e.getCause().getMessage());
        	if(e.getMostSpecificCause() != null)
        		log.error("setMoves exception most : " + e.getMostSpecificCause().getMessage());
        	if(e.getRootCause() != null)
        		log.error("setMoves exception root : " + e.getRootCause().getMessage());
        }
    }
}