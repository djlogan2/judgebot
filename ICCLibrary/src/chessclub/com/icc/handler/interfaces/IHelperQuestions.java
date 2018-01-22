package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l1l2.HelpQuestion;
import chessclub.com.icc.l1l2.HelpQuestionModified;

public interface IHelperQuestions extends IAbstractICCHandler {
    void newHelperQuestion(HelpQuestion q);
    void handlingHelperQuestion(HelpQuestionModified p);
    void removingHelperQuestion(HelpQuestionModified p);
}
