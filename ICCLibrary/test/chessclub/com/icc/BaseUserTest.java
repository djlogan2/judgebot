package chessclub.com.icc;

import static org.junit.Assert.*;

import java.util.concurrent.Semaphore;

import org.junit.Test;

import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.handler.interfaces.IWhoAmI;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l2.WhoAmI;

/**
 * 
 * @author David Logan
 * 
 * Test class
 *
 */
public abstract class BaseUserTest extends DefaultICCAdminCommands implements IAcceptGenericPackets, IWhoAmI {

    protected enum usertype {
        GUEST, ANON, REGISTERED, ADMIN
    }
    protected usertype currentUserType;
    
    protected enum listtype {
        NONE, ABUSER, C1MUZZLE, NOPLAY, NOVIEW, SMUZZLE, CENSOR, FORCE,
        MUZZLE, NORATED, PLAYBOTH, SSHOUT, BANNED, EXEMPT, PMUZZLE, COMPUTER,
        KMUZZLE, NOPASS, NOUNRATED
    };
    protected listtype currentListType = null;
    protected ICCInstance icc;
    protected String whoami;
    protected DefaultICCAdminCommands commands;
    protected DefaultICCAdminCommands testAdminCommands;
    protected Semaphore semaphore = new Semaphore(1);
    
    protected ICCInstance adminicc;
    protected DefaultICCAdminCommands adminCommands;
    protected adminHandler adminHandler;

    @SuppressWarnings("incomplete-switch")
    @Test
    public void testAllSituations() {
        
        adminicc = new ICCInstance();
        adminHandler = new adminHandler();
        adminicc.setInstance("queen.chessclub.com",  5000,  "",  "",  adminHandler,  4096);
        try {
            semaphore.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        adminicc.start();
        try {
            semaphore.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        
        for(listtype l : listtype.values()) {
            currentListType = l;
            for(usertype u : usertype.values()) {
                currentUserType = u;
                icc = new ICCInstance();
                switch(currentUserType) {
                case GUEST:
                    icc.setInstance("queen.chessclub.com", 5000, "g", "", this, 4096);
                    break;
                case ANON:
                    icc.setInstance("queen.chessclub.com", 5000, "!", "", this, 4096);
                    break;
                case REGISTERED:
                    icc.setInstance("queen.chessclub.com", 5000, "", "", this, 4096);
                    break;
                }
                icc.run();
            }
        }
        
        icc = adminicc;
        icc.addHandler(this);
        commands = adminCommands;
        
        issueCommand();
        adminCommands.onDuty(false);
        issueCommand();

        adminicc.quit();
        
        try {
            adminicc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected void removeFromList(listtype lt) {
        if(lt != listtype.NONE)
            adminicc.send("-" + lt + " " + whoami);
    }
    
    protected void addToList(listtype lt) {
        if(lt != listtype.NONE)
            adminicc.send("+" + lt + " " + whoami);
    }
    
    protected abstract void issueCommand();

    public void initHandler() {
    }

    public void iccException(ICCException ex) {
        fail();
    }

    public void setInstance(ICCInstance icc) {
        if(this.currentUserType == usertype.ADMIN) {
            commands = adminCommands;
        } else {
            commands = new DefaultICCAdminCommands();
            commands.setInstance(icc,  this);
        }
    }

    public void connectionClosed() {
        fail();
    }

    public void Error(chessclub.com.icc.l2.Error error) {
        fail(error.text());
    }

    public void shuttingDown() {
    }

    public void whoAmI(WhoAmI p) {
        whoami = p.userid();
        issueCommand();
    }
    
    private class adminHandler implements IAcceptGenericPackets, IWhoAmI {

        public void initHandler() {
        }

        public void iccException(ICCException ex) {
            fail();
        }

        public void setInstance(ICCInstance icc) {
        }

        public void connectionClosed() {
            fail();
        }

        public void Error(chessclub.com.icc.l2.Error error) {
            fail();
        }

        public void shuttingDown() {
            fail();
        }

        public void whoAmI(WhoAmI p) {
            adminCommands = new DefaultICCAdminCommands();
            adminCommands.setInstance(adminicc,  adminHandler);
            adminCommands.setAdminPassword("7MoPlOcK55");
            adminCommands.onDuty(true);
            semaphore.release();
        }

        public void badCommand1(BadCommand p) {
            // TODO Auto-generated method stub
            
        }

        public void badCommand2(L1ErrorOnly p) {
            // TODO Auto-generated method stub
            
        }
        
    }
}
