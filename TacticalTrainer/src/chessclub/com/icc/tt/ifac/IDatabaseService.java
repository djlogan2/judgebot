package chessclub.com.icc.tt.ifac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import chessclub.com.icc.tt.DatabaseStatistics;
import chessclub.com.icc.tt.entity.CompletedTactic;
import chessclub.com.icc.tt.entity.GameToCheck;
import chessclub.com.icc.tt.entity.TacticUserStat;
import chessclub.com.icc.tt.entity.TacticsTable;
import chessclub.com.icc.tt.entity.TacticsUser;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;

public interface IDatabaseService {
    int saveTactic(Fen fen, String moves, Color tacticWinner, boolean winning /*, int winnerRating, int loserRating*/);
    TacticsUser getUser(String username, int uniqueid, int rating);
    TacticsTable getRandomTactic(Collection<Integer> ineligible_ids, TacticsUser user, Color tomove, int rating);
//    void addCompletedTactic(int uniqueid, int tacticid);
    void solvedTactic(CompletedTactic ct);
    DatabaseStatistics getStatistics();
    Collection<TacticsTable> getAllTactics();
    void good(TacticsTable incoming_tt);
    String getMessage(Locale locale, String code);
    int getRanking(int uid);
    int getRanking(TacticsUser user);
    Collection<TacticsTable> getNullTactics();
    GameToCheck getNextGameToCheck();
    void saveGameToCheck(String who, int gameid);
    void saveGametoCheck(String who, Fen fen);
    boolean hasBeenBothered(String who);
	Collection<String> getAllUsers();
	Collection<TacticsTable> getNullWinningTactics();
	void winning(TacticsTable incoming_tt);
	void updateFen(int id, Fen fen);
	ArrayList<TacticUserStat>rankByRating(int mingames, String who);
	TacticsTable getTactic(int id);
	void updateGoodAndComment(int id, boolean good, String comment);
	Integer[][] ratingDetail(String name);
}
