package chessclub.com.icc.jb.ifac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import chessclub.com.icc.jb.entity.AdjudicateRule;
import chessclub.com.icc.jb.entity.DisconnectStats;
import chessclub.com.icc.jb.entity.EngineRule;
import chessclub.com.icc.jb.entity.GameLog;
import chessclub.com.icc.jb.entity.LoseExempt;
import chessclub.com.icc.jb.entity.WinExempt;
import chessclub.com.icc.jb.enums.JBAdjudicateAction;
import chessclub.com.icc.jb.enums.SQLReturn;

public interface IDatabaseService {

	public Page<GameLog> listGames(Pageable page, String white, boolean exact, String black, boolean either);
	public GameLog getGameLog(int id);
	public GameLog getGameLogWithJoins(int id);
	public void addGame(GameLog gl);
	public Collection<AdjudicateRule> getActiveAdjudicationRules(boolean deleted);
    public Collection<EngineRule> getActiveEngineRules(boolean deleted);
	public void setScore(int gameid, boolean ismate, int score);
	public void setTimes(int gameid, int whitemsec, int blackmsec);
	public void setVariable(int gameid, String variable, String value);
	public GameLog getLastGame(String player1, String player2);
	public String getWinExempt(int id);
	public String getNoLose(int id);
	public SQLReturn addLoseExempt(int loserid, String loser, String handle);
	public SQLReturn removeLoseExempt(int loserid);
	public SQLReturn addWinExempt(int uniqueId, String winner, String handle);
	public SQLReturn removeWinExempt(int uniqueId);
	public void updateAdjudicateRules(ArrayList<HashMap<String,Object>> updateArray) throws Exception;
    public void updateEngineRules(ArrayList<HashMap<String, Object>> updateArray) throws Exception;
    public void addMatchedMissedAdjudicationRule(int gameid, int adjrule_id, boolean matched);
    public void addMatchedMissedEngineRule(int gameid, int engrule_id, boolean matched);
    public void adjudicationRuleHit(int gameid, boolean matched, JBAdjudicateAction jbAdjudicateAction);
    public void engineRuleHit(int gameid, boolean matched);
    public void setMoves(int gameid, String lasttwomoves);
    public Page<WinExempt> getWinExemptList(String searchString, PageRequest pageRequest);
    public Page<LoseExempt> getLoseExemptList(String searchString, PageRequest pageRequest);
	public DisconnectStats getDisconnectStats(String who);
}
