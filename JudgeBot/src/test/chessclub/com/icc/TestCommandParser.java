package test.chessclub.com.icc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;

import org.junit.Test;

import chessclub.com.icc.jb.ifac.JudgeBotCommandInterface;
import chessclub.com.icc.jb.parser.JudgeBotCommandParser;
import chessclub.com.icc.l1l2.Titles;
import chessclub.com.icc.l2.PersonalTell;

public class TestCommandParser implements JudgeBotCommandInterface {

    private String function = null;
	@Test
	public void testStart() throws Exception {
	    Titles titles = mock(Titles.class);
	    when(titles.isOnDutyAdministrator()).thenReturn(true);
	    PersonalTell p = mock(PersonalTell.class);
	    JudgeBotCommandParser parser = new JudgeBotCommandParser(new StringReader("info os"));
	    when(p.name()).thenReturn("testStart");
	    when(p.titles()).thenReturn(titles);
	    parser.parse(p, false, this);
	    assertEquals("testStart.info.os", function);
	    
        parser = new JudgeBotCommandParser(new StringReader("info o"));
        when(p.name()).thenReturn("testStart");
        parser.parse(p, false, this);
        assertEquals("testStart.info.o", function);
        
        parser = new JudgeBotCommandParser(new StringReader("info "));
        when(p.name()).thenReturn("testStart");
        parser.parse(p, false, this);
        assertEquals("testStart.info.", function);
        
        parser = new JudgeBotCommandParser(new StringReader("info"));
        when(p.name()).thenReturn("testStart");
        parser.parse(p, false, this);
        assertEquals("testStart.info.", function);
        
        parser = new JudgeBotCommandParser(new StringReader("summary"));
        when(p.name()).thenReturn("testStart");
        parser.parse(p, false, this);
        assertEquals("testStart.summary.", function);
        
        parser = new JudgeBotCommandParser(new StringReader("summary fuck"));
        when(p.name()).thenReturn("testStart");
        parser.parse(p, false, this);
        assertEquals("testStart.summary.fuck", function);
        
        parser = new JudgeBotCommandParser(new StringReader("summary fuck metoo"));
        when(p.name()).thenReturn("testStart");
        parser.parse(p, false, this);
        assertEquals("testStart.summary.fuck.metoo", function);
        
        parser = new JudgeBotCommandParser(new StringReader("status"));
        when(p.name()).thenReturn("testStart");
        parser.parse(p, false, this);
        assertEquals("testStart.status", function);
	}

    @Override
    public void help(PersonalTell p) {
        function = p.name() + ".help";
    }

    @Override
    public void status(PersonalTell p, boolean isHelper) {
        function = p.name() + ".status";
    }

    @Override
    public void summary(PersonalTell p, boolean isHelper, String user1, String user2) {
        function = p.name() + ".summary.";
        if(user1 != null) function += user1;
        if(user2 != null) function += "." + user2;
    }

    @Override
    public void win(PersonalTell p, boolean add) {
        function = p.name() + ".win." + Boolean.toString(add);
    }

    @Override
    public void info(PersonalTell p, String otheruser) {
        function = p.name() + ".info.";
        if(otheruser != null) function += otheruser;
    }

    @Override
    public void lose(PersonalTell p, String user, boolean add) {
        function = p.name() + ".lose.";
        if(user != null) function += user;
        function += "." + Boolean.toString(add);
    }

    @Override
    public void runtime(PersonalTell p) {
        function = p.name() + ".runtime";
    }

    @Override
    public void memory(PersonalTell p) {
        function = p.name() + ".memory";
    }

    @Override
    public void os(PersonalTell p) {
        function = p.name() + ".os";
    }

    @Override
    public void lasttells(PersonalTell p, boolean isHelper) {
    }

    @Override
    public void reset(PersonalTell p) {
    }

    @Override
    public void killgame(PersonalTell p, boolean isHelper) {
    }

}
