/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chessclub.com.icc.l1;

import david.logan.chess.support.Fen;


/**
 * Instance returned from the framework when a "sfen" is issued.
 * @author David Logan
 */
public class Sfen extends Level1Packet {
    /**
     * The returned FEN string.
     * @return The returned FEN string.
     * @throws Exception 
     */
    public Fen fen() throws Exception {
        return new Fen(getParm(1));
    }
}
