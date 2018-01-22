package chessclub.com.icc.jb.icc;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import chessclub.com.icc.handler.interfaces.IPersonalTellEcho;
import chessclub.com.icc.jb.ifac.IDatabaseService;
import chessclub.com.icc.l2.PersonalTellEcho;

public class LoseExempt extends BasicUserICCHandler implements IPersonalTellEcho {

    private static final Logger log = LogManager.getLogger(LoseExempt.class);
    private boolean putOn = false;
    private String loser = null;
    private int loserid = -1;

    public LoseExempt(IDatabaseService databaseService, MessageSource messageSource,
                        String whoasked, String handle, boolean on) {
        super(databaseService, messageSource, whoasked);
        putOn = on;
        loser = handle;
        if (log.isDebugEnabled()) {
            log.debug(this.hashCode() + "LoseExempt created for handle " + handle);
        }
    }

    @Override
    public void getpx(chessclub.com.icc.l1.Getpx p)
    {
        if("loseexempt".equals(p.getKey())) {
            loserid = p.getUniqueId();
            if (putOn) {
                add();
            } else {
                remove();
            }
        }
        else
            super.getpx(p);
    }
    
    @Override
    protected void continuehandler() {
        getpx(loser,"loseexempt");
    }

    private void xtell(String who, String key, String theuser)
    {
        super.xtell(who, messageSource.getMessage(key, new Object[]{theuser}, getLocale())); //mf.format(new Object[]{theuser}));
    }
    
    private void add() {
        switch (databaseService.addLoseExempt(loserid, loser, handle())) {
            case OK:
                xtell(handle(), "added.to.nolose.list", loser);
                break;
            case DUPLICATE:
                xtell(handle(), "nolose.already.on", loser);
                break;
            default:
                xtell(handle(), "nolose.list.error", loser);
                break;
        }
    }

    private void remove() {
        switch (databaseService.removeLoseExempt(loserid)) {
            case OK:
                xtell(handle(), "removed.from.nolose.list", loser);
                break;
            default:
                xtell(handle(), "lose.list.error", loser);
                break;
        }
    }

    @Override
    public void personalTellEcho(PersonalTellEcho p) {
        if (log.isDebugEnabled()) {
            log.debug("Removing myself " + hashCode());
        }
        removeHandler(this);
    }

    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
    }
}
