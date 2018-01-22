/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chessclub.com.icc.l2;

public class TakeBackOccurred extends Level2Packet {
//    22 DG_TAKEBACK                                                                                                                                                                                                                                                     
//    Form: (gamenumber takeback-count)                                                                                                                                                                                                                                
//    A takeback occurred in the game that I'm observing or playing.
    public int gamenumber() {
        return Integer.parseInt(getParm(1));
    }
    public int takebackCount() {
        return Integer.parseInt(getParm(2));
    }
}
