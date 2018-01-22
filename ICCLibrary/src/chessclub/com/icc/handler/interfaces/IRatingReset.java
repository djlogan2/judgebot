package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l1.RatingChanged;

public interface IRatingReset extends IAbstractICCHandler {
    void ratingReset(RatingChanged p);
}
