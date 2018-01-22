package chessclub.com.icc.jb.icc;

import java.text.DateFormat;

import org.springframework.context.MessageSource;

import chessclub.com.icc.jb.entity.GameLog;
import chessclub.com.icc.jb.ifac.IDatabaseService;
import chessclub.com.icc.l2.PersonalTellEcho;
import david.logan.chess.support.Color;

public class UserGameInfo extends BasicUserICCHandler {

    String opponent = null;
    
    public UserGameInfo(IDatabaseService databaseService, MessageSource messageSource, String whoasked, String aboutwho) {
        super(databaseService, messageSource, whoasked);
        opponent = aboutwho.replaceAll("[^a-zA-Z0-9\\-]","");
    }
    
    @Override
    protected void continuehandler() {
        String key = "winning.";
        GameLog gl = databaseService.getLastGame(this.getpx().getUserid(), opponent);
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, getLocale());
        boolean disconnector = false;
        boolean ismate = false;
        double score;

        if(gl == null) {
            xtell(getpx().getUserid(), messageSource.getMessage("no.adjudicated.game", new Object[]{opponent}, getLocale()));//mf.format(new Object[]{opponent}));
            return;
        }
        // On {date}, you {won/lost} your game against {opponent} due to {you/your opponent} disconnecting (with a score of {score}|facing a mate in {mate})
        // winning.score = On {date}, you won your game against {opponent} due to your opponent disconnecting with a score of {score}
        // winning.mate = On {date}, you won your game against {opponent} due to your opponent disconnecting facing a mate in {mate}
        // losing.score = On {date}, you lost your game against {opponent} due to you disconnecting with a score of {score}
        // losing.mate = On {date}, you lost your game against {opponent} due to you disconnecting facing a mate in {mate}
        
        String adjDate = formatter.format(gl.getAdjdate());
        ismate = gl.isIsmate();
        score = gl.getScore();
        if(getpx().getUserid().equalsIgnoreCase(gl.getWhite())) {
            opponent = gl.getBlack();
            if(gl.getWhodisconnected() == Color.WHITE) {
                disconnector = true;
            }
        } else {
            opponent = gl.getWhite();
            if(gl.getWhodisconnected() == Color.BLACK) {
                disconnector = true;
            }
        }
        
        if((disconnector && score < 0) || (!disconnector && score > 0)) {
            key = "losing.";
        }
        
        if(ismate) {
            key += "mate";
            score = Math.abs(score);
        } else {
            key += "score";
            score = score / 100.0;
        }
        xtell(getpx().getUserid(), messageSource.getMessage(key, new Object[]{adjDate,opponent,score}, getLocale()));
    }
    
    public void PersonalTellEcho(PersonalTellEcho p)
    {
        removeHandler(this);
    }

    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
    }
}
