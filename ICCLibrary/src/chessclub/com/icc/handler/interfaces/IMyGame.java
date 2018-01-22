package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.GameNumber;
import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;
import chessclub.com.icc.l2.Msec;
import chessclub.com.icc.l2.OffersInMyGame;
import chessclub.com.icc.l2.SendMoves;
import chessclub.com.icc.l2.TakeBackOccurred;

public interface IMyGame extends IAbstractICCHandler {
    void myGameResult(GameResultPacket p);
    void myGameStarted(GameStarted p);
    void myGameEnded(GameNumber p);
    void examinedGameIsGone(GameNumber p);
    void takeBackOccurred(TakeBackOccurred p);
    void msec(Msec p);
    void sendMoves(SendMoves p);
    void newOffers(OffersInMyGame p);
//    wdraw bdraw wadjourn badjourn wabort babort wtakeback btakeback
}
