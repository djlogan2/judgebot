package chessclub.com.icc;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import chessclub.com.enums.CN;
import chessclub.com.icc.handler.interfaces.IListChanged;
import chessclub.com.icc.handler.interfaces.ITitlesChanged;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.ListChanged;
import chessclub.com.icc.l1l2.Titles;
import chessclub.com.icc.l2.TitleChange;

/**
 * 
 * @author David Logan
 * 
 * Test class
 *
 */
public class ListTests extends BaseUserTest implements ITitlesChanged, IListChanged {
    private enum alllists { 
        alias,
        censor("listCensor"),
        dm,
        force("listForce"),
        im,
        wim,
        fm,
        muzzle("listMuzzle"),
        norated("listNorate"),
        playboth("listPlayboth"),
        sshout("listSShout"),   
        banned("listBan"),
        channel,
        exempt("listExempt"),
        gm,
        ipnotify,
        nobest("listNobest"),
        notify("listNotify"),
        pmuzzle("listPmuzzle"),
        wfm,      
        buster("listBuster"),
        computer("listComputer"),
//        filter,
        gnotify("listGnotify"),
        kmuzzle("listKmuzzle"),
//        nopass,
        nounrated("listNounrated"),
//        simul,
        wgm;
        private Method m;
        private alllists() {
            
        }
        private alllists(String methodname) {
            try {
                m = DefaultICCAdminCommands.class.getMethod(methodname, String.class, boolean.class);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
                fail();
            } catch (SecurityException e1) {
                e1.printStackTrace();
                fail();
            }
        }
        public void invoke(DefaultICCAdminCommands commands, String user, boolean doit) {
            if(m == null)
                fail(this + " does not have an invoke method");
            try {
                m.invoke(commands,  user, doit);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        public alllists next() {
            boolean found = false;
            for(alllists al : alllists.values()) {
                if(found)
                    return al;
                else if(al == this)
                    found = true;
            }
            return null;
        }
    };
    
    private enum allsteps {
        remove1, add1, add2, remove2;
        public allsteps next() {
            switch(this) {
            case remove1: return add1;
            case add1: return add2;
            case add2: return remove2;
            case remove2: return null;
            default: return null;
            }
        }
    };
    
    private alllists whichlist = alllists.alias;
    private allsteps whichstep = allsteps.remove1;
    
    @Override
    protected void issueCommand() {
        System.out.println(currentUserType + " " + currentListType + " " + whichlist + " " + whichstep);

        switch(whichlist) {
        case channel:
            switch(whichstep) {
            case remove1:
            case remove2:
                commands.leaveChannel(283);
                break;
            case add1:
            case add2:
                commands.addChannel(283);
            }
            break;
        case alias:
            switch(whichstep) {
            case remove1:
            case remove2:
                commands.listRemoveAlias("unittest");
                break;
            case add1:
            case add2:
                commands.listAddAlias("unittest",  "xt djlogan hi");
            }
            break;
        case dm:
            switch(whichstep) {
            case remove1:
            case remove2:
                commands.changeTitles("roncosbay",  new Titles("DM"), new Titles(""));
                break;
            case add1:
            case add2:
                commands.changeTitles("roncosbay",  new Titles(""), new Titles("DM"));
            }
            break;
        case im:
            switch(whichstep) {
            case remove1:
            case remove2:
                commands.changeTitles("roncosbay",  new Titles("IM"), new Titles(""));
                break;
            case add1:
            case add2:
                commands.changeTitles("roncosbay",  new Titles(""), new Titles("IM"));
            }
            break;
        case fm:
            switch(whichstep) {
            case remove1:
            case remove2:
                commands.changeTitles("roncosbay",  new Titles("FM"), new Titles(""));
                break;
            case add1:
            case add2:
                commands.changeTitles("roncosbay",  new Titles(""), new Titles("FM"));
            }
            break;
        case gm:
            switch(whichstep) {
            case remove1:
            case remove2:
                commands.changeTitles("roncosbay",  new Titles("GM"), new Titles(""));
                break;
            case add1:
            case add2:
                commands.changeTitles("roncosbay",  new Titles(""), new Titles("GM"));
            }
            break;
        case wim:
            switch(whichstep) {
            case remove1:
            case remove2:
                commands.changeTitles("roncosbay",  new Titles("WIM"), new Titles(""));
                break;
            case add1:
            case add2:
                commands.changeTitles("roncosbay",  new Titles(""), new Titles("WIM"));
            }
            break;
        case wfm:
            switch(whichstep) {
            case remove1:
            case remove2:
                commands.changeTitles("roncosbay",  new Titles("WFM"), new Titles(""));
                break;
            case add1:
            case add2:
                commands.changeTitles("roncosbay",  new Titles(""), new Titles("WFM"));
            }
            break;
        case wgm:
            switch(whichstep) {
            case remove1:
            case remove2:
                commands.changeTitles("roncosbay",  new Titles("WGM"), new Titles(""));
                break;
            case add1:
            case add2:
                commands.changeTitles("roncosbay",  new Titles(""), new Titles("WGM"));
            }
            break;
        case ipnotify:
            switch(whichstep) {
            case remove1:
            case remove2:
                try {
                    commands.listRemoveIPNotify(InetAddress.getByName("64.69.35.190"));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
                break;
            case add1:
            case add2:
                try {
                    commands.listAddIPNotify(InetAddress.getByName("64.69.35.190"));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
            }
            break;
       default:
            switch(whichstep) {
            case remove1:
            case remove2:
                whichlist.invoke(commands,  "roncosbay",  false);
                break;
            case add1:
            case add2:
                whichlist.invoke(commands,  "roncosbay",  true);
            }
            break;
//        default:
//            fail(whichlist + " is not being handled");
        }
    }
    
    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
        switch(currentUserType) {
        case GUEST:
            switch(whichstep) {
            case remove1:
            case remove2:
                assertEquals("E1 Your '" + whoami + "' username does not have enough privileges to use the command 'minus'.  Upgrade to a full membership and start enjoying unlimited game play and MUCH MORE! http://www.chessclub.com/upgrade\r\n", error.text());
                break;
            case add1:
            case add2:
                assertEquals("E1 Your '" + whoami + "' username does not have enough privileges to use the command 'plus'.  Upgrade to a full membership and start enjoying unlimited game play and MUCH MORE! http://www.chessclub.com/upgrade\r\n", error.text());
                break;
            }
            break;
        case ANON:
            switch(whichlist) {
            default:
                fail(currentUserType + " " + currentListType + " " + whichlist + " " + whichstep + " " + error.text());
//              case filter:
//              case nopass:
//              case simul:
            }
            break;
        case REGISTERED:
            switch(whichlist) {
            default:
                fail(currentUserType + " " + currentListType + " " + whichlist + " " + whichstep + " " + error.text());
//              case filter:
//              case nopass:
//              case simul:
            }
            break;
        case ADMIN:
            switch(whichlist) {
            default:
                fail(currentUserType + " " + currentListType + " " + whichlist + " " + whichstep + " " + error.text());
//              case filter:
//              case nopass:
//              case simul:
            }
            break;
        }
        nextTest();
    }
    
    protected void nextTest() {
        whichstep = whichstep.next();
        if(whichstep != null) {
            issueCommand();
            return;
        }
        whichstep = allsteps.remove1;
        whichlist = whichlist.next();
        
        if(whichlist != null) {
            issueCommand();
            return;
        }
        whichlist = alllists.alias;
        icc.quit();
    }

    public void badCommand1(BadCommand p) {
        fail("badCommand1: " + p.command());
    }

    public void badCommand2(L1ErrorOnly p) {
        switch(currentUserType) {
        case ANON:
            switch(whichlist) {
            case alias:
            case censor:
                switch(whichstep) {
                case remove1:
                case remove2:
                    assertTrue(p.hasError());
                    assertEquals("The list is empty.", p.errortext());
                    assertEquals(CN.MINUS, p.getType());
                    nextTest();
                    return;
                case add1:
                case add2:
                    assertTrue(p.hasError());
                    assertEquals("You must be registered to create an alias list.", p.errortext());
                    assertEquals(CN.PLUS, p.getType());
                    nextTest();
                    return;
                default:
                    break;
                }
            case computer:
                switch(whichlist) {
                case alias:
                    switch(whichstep) {
                    case remove1:
                    case remove2:
                        assertTrue(p.hasError());
                        assertEquals("\"" + whichlist + "\" does not match any appropriate list name. Possible choices are:", p.errortext());
                        assertEquals(CN.MINUS, p.getType());
                        nextTest();
                        return;
                    case add1:
                    case add2:
                        assertTrue(p.hasError());
                        assertEquals("You must be registered to create an alias list.", p.errortext());
                        assertEquals(CN.PLUS, p.getType());
                        nextTest();
                        return;
                    default:
                        fail(currentUserType + " " + currentListType + " " + whichlist + " " + whichstep + " " + p.getType() + " : " + p.errortext());
                    }
                default:
                    break;
                }
            default:
                fail(currentUserType + " " + currentListType + " " + whichlist + " " + whichstep + " " + p.getType() + " : " + p.errortext());
            }
            break;
        case REGISTERED:
            switch(whichlist) {
            default:
                fail(currentUserType + " " + currentListType + " " + whichlist + " " + whichstep + " " + p.getType() + " : " + p.errortext());
            }
            break;
        case ADMIN:
            switch(whichlist) {
            default:
                fail(currentUserType + " " + currentListType + " " + whichlist + " " + whichstep + " " + p.getType() + " : " + p.errortext());
            }
            break;
        default:
            fail(currentUserType + " : " + currentListType  + " : badCommand2: " + p.getType() + " : " + p.errortext());
        }
        nextTest();
        fail(currentUserType + " : " + currentListType  + " : badCommand2: " + p.getType() + " : " + p.errortext());
    }

    public void titlesChanged(TitleChange p) {
        fail(currentUserType + " : " + currentListType  + " : titlesChanged");
    }

    public void listChanged(ListChanged lp) {
        switch(currentUserType) {
        case ANON:
            switch(whichlist) {
            case alias:
                switch(whichstep) {
                case add1:
                    break;
                default:
                    //fail(currentUserType + " : " + currentListType  + " : listChanged: " + lp.getList() + " : " + lp.getCommand());
                }
                default:
                    //fail(currentUserType + " : " + currentListType  + " : listChanged: " + lp.getList() + " : " + lp.getCommand());
            }
            default:
                //fail(currentUserType + " : " + currentListType  + " : listChanged: " + lp.getList() + " : " + lp.getCommand());
        }
    }
}
