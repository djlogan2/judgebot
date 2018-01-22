package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.Tourney;
import chessclub.com.icc.l2.TourneyGameEnded;
import chessclub.com.icc.l2.TourneyGameStarted;

public interface ITourney extends IAbstractICCHandler {
    void tourney(Tourney p);
    void tourneyGameStarted(TourneyGameStarted p);
    void tourneyGameEnded(TourneyGameEnded p);
    void removeTourney(int index);
}
