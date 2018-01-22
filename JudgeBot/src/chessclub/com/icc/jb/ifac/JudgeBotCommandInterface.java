package chessclub.com.icc.jb.ifac;

import chessclub.com.icc.l2.PersonalTell;

public interface JudgeBotCommandInterface {
    public void help(PersonalTell p);
    public void status(PersonalTell p, boolean isHelper);
    public void summary(PersonalTell p, boolean isHelper, String user1, String user2);
    public void win(PersonalTell p, boolean add);
    public void info(PersonalTell p, String otheruser);
    public void lose(PersonalTell p, String user, boolean add);
    public void runtime(PersonalTell p);
    public void memory(PersonalTell p);
    public void os(PersonalTell p);
    public void lasttells(PersonalTell p, boolean isHelper);
    public void reset(PersonalTell p);
    public void killgame(PersonalTell p, boolean isHelper);
}
