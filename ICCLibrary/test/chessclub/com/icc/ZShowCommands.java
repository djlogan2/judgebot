/**
 * 
 */
package chessclub.com.icc;
// CHECKSTYLE:OFF

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.handler.interfaces.IWhoAmI;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.Level1Packet;
import chessclub.com.icc.l2.WhoAmI;

/**
 * 
 * @author David Logan
 * 
 *         Test class
 * 
 */
public class ZShowCommands extends DefaultICCAdminCommands implements UnitTestHandler, IAcceptGenericPackets, IWhoAmI {
    private ICCInstance icc = null;

    /**
         * 
         */
    public ZShowCommands() {
    }

    /**
     * *
     * 
     * @throws Exception
     *             If an Exception occurs
     */
    @Before
    public final void setUp() throws Exception {
        icc = new ICCInstance();
    }

    /**
     * 
     * @throws Exception
     *             If an Exception occurs
     */
    @After
    public final void tearDown() throws Exception {
    }

    /**
     * @throws Exception 
         * 
         */
    @Test
    public final void doZCommands() throws Exception {
        ICCException ex = icc.setInstance("queen.chessclub.com", 5010, "", "", this, 16 * 1024);

        assertNull(ex);
        icc.run();
    }

    public void initHandler() {
        return;
    }

    public void iccException(final ICCException ex) {
        fail("Method should not have been called");
    }

    public void setInstance(final ICCInstance picc) {
        super.setInstance(picc, this);
    }

    public void whoAmI(final WhoAmI p) {
        setAdminPassword("");
        onDuty(true);
        icc.send("z-show-commands");
    }

    public void connectionClosed() {
        fail("Method should not have been called");
    }

    public void shuttingDown() {
    }

    public void do310(final Level1Packet p) { 
        String[] expectedZ = {
                "  S...1 !",
                "  S.... +",
                "  S.... ,",
                "  S.... -",
                "  S.... .",
                "  S.... /",
                "  N.... /",
                "  N0..1 1-minute",
                "  N0..1 15-minute",
                "  N0..1 3-minute",
                "  N0..1 45-minute",
                "  N0..1 5-minute",
                "  N0..1 960",
                "  S...1 :",
                "  s.... =",
                "  N1...X abort",
                "  s0..1X abort",
                "  s..1. abortadjourned",
                "  P.... accept",
                "  N.... accept",
                "  s..1. account",
                "  S..1.X addplayer",
                "  S..1. address",
                "  N1..1 adjourn",
                "  S..1. adjudicate",
                "  N..1. admin",
                "  S..1. admin",
                "  S..01 admin",
                "  G.... allobservers",
                "  N.... allobservers",
                "  S.1.1 annotate",
                "  S01.. arrow",
                "  N..1. aset",
                "  S..1.X aset",
                "  N1... assess",
                "  S.... assess",
                "  S..1. atell",
                "  S.... away",
                "  N.... away",
                "  s..1. awho",
                "  N..1. awho",
                "  N.1.. backward",
                "  s.1.. backward",
                "  N.... bell",
                "  N.... best",
                "  S.... best",
                "  N.... blanking",
                "  S01.. boardinfo",
                "  N.... bugwho",
                "  s..1. capitalize",
                "  S...1 cc-ask-director",
                "  s...1 cc-delete",
                "  N...1 cc-list",
                "  s...1 cc-list",
                "  S0..1 cc-move",
                "  S...1 cc-qadjudicate",
                "  S...1 cc-qdelete",
                "  S...1 cc-qlabel",
                "  N...1 cc-qlist",
                "  s...1 cc-qlist",
                "  S.1.1 cc-qmodify",
                "  S...1 cc-qstart",
                "  s...1 cc-start-as-black",
                "  s...1 cc-start-as-white",
                "  N.... censor",
                "  s.... censor",
                "  S..1. change-password",
                "  N0..1 chess960",
                "  s1... chessmove",
                "  s.1.. chessmove",
                "  S01.. circle",
                "  N.1.. clearboard",
                "  N...1 clearmessages",
                "  s...1 clearmessages",
                "  N.... commands",
                "  S..1. comment",
                "  S011. commentgame",
                "  S....X complain",
                "  N..1. consistency-check",
                "  N..1. cookie",
                "  S..1. cookie",
                "  N.1.. copygame",
                "  G0... copygame",
                "  S...1 ctell",
                "  N1... d",
                "g N.... date",
                "  s..1. db-by-disk",
                "  P..1. db-machine",
                "  s..1. db-machines",
                "  s..1. db-users",
                "  N..1. debug-anon",
                "  s..1. debug-anon",
                "  G..1. debug-game",
                "  N.... debug-l2",
                "  s1... decline",
                "  N1... decline",
                "  P0... decline",
                "  N0... decline",
                "  N1... draw",
                "  s0..1 draw",
                "  N..1. dump-accounts",
                "  N..1. dumptiming",
                "  S.... eco",
                "  N.... eco",
                "  N.... events",
                "  S0... examine",
                "  N.1.. examine",
                "  N0... examine",
                "g N0...X exit",
                "g N1...X exit",
                "  S.... expunge",
                "  N00.1X extend",
                "  N1... fen",
                "  G.... fen",
                "  N.1.. fen",
                "  N0..1 fifteen-minute",
                "  s..1. findaddress",
                "  s..1. findip",
                "  S..1. findname",
                "g N.... finger",
                "g S.... finger",
                "  N0..1 five-minute",
                "  N.... flag",
                "  G.... flip",
                "  N.... flip",
                "  s..1. flush",
                "  N..1. flush",
                "  N..1. flush-players",
                "  N..1. fm0pool",
                "  S..1. fmessage",
                "  N..1. fmpawnpool",
                "  N.... follow",
                "  s.... follow",
                "  s..1. forfeitadjourned",
                "  N0..1 forty-five-minute",
                "  N.1.. forward",
                "  s.1.. forward",
                "  s...1 g-best",
                "  s.... g-describe",
                "  S..1. g-free",
                "  S...1 g-invite",
                "  s...1 g-join",
                "  S...1 g-kick",
                "  N.... g-list-groups",
                "  s...1 g-list-invited",
                "  s...1 g-list-joining",
                "  s.... g-list-members",
                "  S...1 g-message",
                "  S...1 gameid",
                "  G.... games",
                "  *.... games",
                "  N.... games",
                "  P...1 getpi",
                "  P...1 getps",
                "  S...1 getpx",
                "  S...1 giveticket",
                "  N.... goto",
                "  G.... goto",
                "  s..1. guestip",
                "g N.... help",
                "g s.... help",
                "  N.... history",
                "  s.... history",
                "  S...1 i",
                "  S.... inchannel",
                "  N.... inchannel",
                "g N.... info",
                "  s.... isregname",
                "  S..1. keepgame",
                "  S... kibitz",
                "  S.... kibitzto",
                "  N.... lagstats",
                "  s.... lagstats",
                "  s...1 lasttells",
                "  N.... lhistory",
                "  S...1 libannotate",
                "  S...1 libappend",
                "  s...1 libdelete",
                "  S...1 libkeepexam",
                "  N.1.1 libkeepexam",
                "  s.... liblist",
                "  N...1 liblist",
                "  S...1 libsave",
                "  N.... limits",
                "  s.... list",
                "  S..1. list-other",
                "  N.... llogons",
                "  S1... loadfen",
                "  S.1.. loadfen",
                "  S1... loadgame",
                "  S.1.. loadgame",
                "  S..1. location",
                "  s.... logons",
                "  N.... logons",
                "g N1...X logout",
                "g N0...X logout",
                "  S.... logpgn",
                "  S..1. loudshout",
                "  S.... mailhelp",
                "  S..1. mailmessage",
                "  P...1 mailoldmoves",
                "  N...1 mailoldmoves",
                "  s..1. mailpassword",
                "  S...1 mailstored",
                "  S.... match",
                "  N0... match",
                "  S...1 messages",
                "  N...1 messages",
                "  s.1.. mexamine",
                "  S.... minus",
                "  S..1. moderate",
                "  N..1. moderate",
                "g N.... more",
                "  s1..1 moretime",
                "  N1... moves",
                "  G.... moves",
                "  N.1.. moves",
                "g S.... news",
                "g N.... news",
                "  S.... norelay",
                "  p..1. nuke",
                "  *.... observe",
                "  N.... observe",
                "g G.... observe",
                "  P.... oldmoves",
                "  N.... oldmoves",
                "  N0..1 one-minute",
                "  N.... open",
                "  N..1. p15pool",
                "  N..1. p1pool",
                "  N..1. p3pool",
                "  N..1. p45pool",
                "  N..1. p5pool",
                "  N..1. p960pool",
                "  P.... partner",
                "  N.... partner",
                "  S...1 password",
                "  S..1. payfailure",
                "  S..1. payment",
                "  N0... pending",
                "  N1... pending",
                "  P.... pending",
                "  N...1 personal-info",
                "  s...1 personal-info",
                "  G.... pgn",
                "  N1... pgn",
                "  N.1.. pgn",
                "  S.... phraselist",
                "  S...1X phraseptell",
                "  S...1X phrasesay",
                "  S...1X phrasetell",
                "g s.... ping",
                "g N.... ping",
                "  s0... play",
                "  S..1.X player-load",
                "  N.... players",
                "  S.... plus",
                "  N.... prefresh",
                "  G.... primary",
                "  S..1. probationextend",
                "  S..1. processresult",
                "  s.... promote",
                "  S...1 pstat",
                "  S.... ptell",
                "  S..1. ptunepool",
                "  S...1 qaddevent",
                "  S...1 qchanminus",
                "  S...1 qchanplus",
                "  S...1 qclear",
                "  S...1 qfollow",
                "  S...1 qimpart",
                "  S...1 qmatch",
                "  S...1 qpartner",
                "  s...1 qremoveevent",
                "  S...1 qretract",
                "  S...1 qset",
                "  p...1 qsimulize",
                "  S...1 qsuggest",
                "  S...1 qtell",
                "  S..1. query-ui",
                "g N0...X quit",
                "g N1...X quit",
                "  P...1 qunfollow",
                "  P..1. quota",
                "  N.... quota",
                "  S..1. r-rating",
                "  S.... rank",
                "  N.... rank",
                "  N...1 rated",
                "  S..1. rating",
                "  S..1. ratingrecord",
                "  S...1 refer",
                "  G.... refresh",
                "  N.... refresh",
                "  S0... reg-dialog-response",
                "g S0... reg-entry",
                "g N0... reg-submit",
                "g N00..X register",
                "  s..1. rehash",
                "  N..1. rehash",
                "  N..1. rehashmore",
                "  S.... relay",
                "  S...1 relay-now",
                "  N0... rematch",
                "  G..1. remgame",
                "  s..1.X remplayer",
                "  S...1 request-abort",
                "  S...1 request-draw",
                "  S...1 request-win",
                "  s...1 requireticket",
                "  N...1 requireticket",
                "  s0... res",
                "  S..1. reserve-game",
                "  S..1. reset-record",
                "  N1...X resign",
                "  s0..1X resign",
                "  S..1. restrict",
                "  S.1.. result",
                "  N0..1 resume",
                "  N.1.. reverse",
                "  N.1.. revert",
                "  S.... say",
                "  S...1 search",
                "  N...1 search",
                "  S.... seeking",
                "  N.... seeking",
                "  S0... server",
                "g S.... set",
                "g S.... set-2",
                "  N..1. set-motd",
                "  S..1. set-motd",
                "  S..1. set-other",
                "g S.... set-quietly",
                "  s.1.. setblackclock",
                "  s1... setblackclock",
                "  s.1.. setblackclockquietly",
                "  S.1.. setblackname",
                "  S.1.. settimecontrol",
                "  s1... setwhiteclock",
                "  s.1.. setwhiteclock",
                "  s.1.. setwhiteclockquietly",
                "  S.1.. setwhitename",
                "  S.... sfen",
                "  N...1 shout",
                "  S...1 shout",
                "  S..1. show-password",
                "  N.... showadmin",
                "  N..1. shutdown",
                "  N..1.X shutdown-slow",
                "  N..1.X shutdown-staydown",
                "  N1..1 simulize",
                "  S..1. sji-ad",
                "  S.... smoves",
                "  S..1. snubbing",
                "  N.... sought",
                "  S.... sought",
                "  S.... spgn",
                "  S..1. spoof",
                "  S.... sposition",
                "  N..1. sql-connect",
                "  S...1 sshout",
                "  N0..1 startsimul",
                "  N.... statistics",
                "  N.... stored",
                "  s.... stored",
                "g N.... style",
                "g s.... style",
                "g S.... t",
                "g N.... t",
                "  S.1.. tag",
                "  s1... takeback",
                "  N1... takeback",
                "  S...1 taketicket",
                "g S.... tell",
                "  S..1. tellunreg",
                "  p..1. test",
                "  S..1. testdg",
                "  s..1. testlog",
                "  N0..1 three-minute",
                "  N1... time",
                "  G.... time",
                "  G..1. title-needed",
                "  S.1.. tomove",
                "  S01.. unarrow",
                "  s.... uncensor",
                "  N.... uncensor",
                "  S01.. uncircle",
                "  S..1. uncomment",
                "  N.1.. unexamine",
                "  S.... unfollow",
                "  N.... unfollow",
                "  S..1. unmoderate",
                "  N.... unobserve",
                "  G.... unobserve",
                "  s...1 unrelayed",
                "  N.... unrelayed",
                "  s.... unseeking",
                "  N.... unseeking",
                "  N.... upstatistics",
                "  s.... vars",
                "g N.... vars",
                "  s.... view",
                "  S... whisper",
                "  S.... whisperto",
                "g N.... who",
                "g S.... who",
                "  N.... whois",
                "  S.... whois",
                "  s.... wildrank",
                "  N.... wildrank",
                "  S..1. wsuggest",
                "  S.1.. xkibitz",
                "  S1... xkibitz",
                "  N.... xobserve",
                "  G.... xobserve",
                "  P.... xpartner",
                "  N.... xpartner",
                "g S.... xtell",
                "  S1... xwhisper",
                "  S.1.. xwhisper",
                "g s.... yfinger",
                "g N.... yfinger",
                "  S.... z-check-speak",
                "  S..1. z-clear-clientid",
                "g NX...X z-register",
                "  S..1. z-set-clientid",
                "  S..1. z-set-speak",
                "  N..1. z-show-commands",
                "  N..1. z-show-complaint-status",
                "  N..1. z-show-guest-commands",
                "  P..1. z-show-speak",
                "  N.... znotl"
                };
        assertEquals(expectedZ.length + 1, p.numberParms());
        for (int x = 0; x < expectedZ.length; x++) {
            assertEquals("Expected '" + expectedZ[x] + "' but got '" + p.getParm(x + 1) + "'", expectedZ[x], p.getParm(x + 1));
        }
        icc.quit();
    }

    public void Error(chessclub.com.icc.l2.Error error) {
        fail("Method should not have been called");
    }

    public void badCommand1(BadCommand p) {
        fail("Method should not have been called");
    }

    public void badCommand2(L1ErrorOnly p) {
        fail("Method should not have been called");
    }

}
