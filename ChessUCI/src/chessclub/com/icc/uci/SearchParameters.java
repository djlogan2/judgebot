package chessclub.com.icc.uci;

import david.logan.chess.support.MoveList;

/**
 * The class that houses the variety of UCI chess engine search parameters.
 *
 * @author davidlogan
 *
 */
public final class SearchParameters {
    /**
     * Get the list of moves this search is restricted to.
     *
     * @return {@link UCIMoves} - The list of moves this search will consider.
     */
    public final MoveList getSearchMoves() {
        return searchMoves;
    }

    /**
     * Set the list of moves the UCI Engine will consider.
     * @param psearchMoves {@link MoveList} - The list of moves this search will consider.
     */
    public final void setSearchMoves(final MoveList psearchMoves) {
        this.searchMoves = psearchMoves;
    }

    /**
     * Get the setting of "ponder" mode.
     * @return The setting of "ponder" mode.
     */
    public final boolean isPonderMode() {
        return ponderMode;
    }

    /**
     * Set whether "ponder" mode is on (true) or off(false).
     * @param pponderMode true/false for ponder mode on/off.
     */
    public final void setPonderMode(final boolean pponderMode) {
        this.ponderMode = pponderMode;
    }

    /**
     * Get the number of milliseconds white has left on the clock at the time of this move.
     * @return The number of milliseconds white has left on the clock at the time of this move.
     */
    public final int getWmsec() {
        return wmsec;
    }

    /**
     * Set the number of milliseconds white has left on the clock at the time of this move.
     * @param pwmsec The number of milliseconds white has left on the clock at the time of this move.
     */
    public final void setWmsec(final int pwmsec) {
        this.wmsec = pwmsec;
    }

    /**
     * Get the number of milliseconds black has left on the clock at the time of this move.
     * @return The number of milliseconds black has left on the clock at the time of this move.
     */
    public final int getBmsec() {
        return bmsec;
    }

    /**
     * Set the number of milliseconds black has left on the clock at the time of this move.
     * @param pbmsec The number of milliseconds black has left on the clock at the time of this move.
     */
    public final void setBmsec(final int pbmsec) {
        this.bmsec = pbmsec;
    }

    /**
     * The increment value in milliseconds.
     * @return The increment value in milliseconds.
     */
    public final int getWinc() {
        return winc;
    }

    /**
     * Set the increment value in milliseconds.
     * @param pwinc The increment value in milliseconds.
     */
    public final void setWinc(final int pwinc) {
        this.winc = pwinc;
    }

    /**
     * The increment value in milliseconds.
     * @return The increment value in milliseconds.
     */
    public final int getBinc() {
        return binc;
    }

    /**
     * Set the increment value in milliseconds.
     * @param pbinc The increment value in milliseconds.
     */
    public final void setBinc(final int pbinc) {
        this.binc = pbinc;
    }

    public final int getMovesToGo() {
        return movesToGo;
    }

    public final void setMovesToGo(int pmovesToGo) {
        this.movesToGo = pmovesToGo;
    }

    public final int getDepth() {
        return depth;
    }

    public final void setDepth(int pdepth) {
        this.depth = pdepth;
    }

    public final int getNodes() {
        return nodes;
    }

    public final void setNodes(int pnodes) {
        this.nodes = pnodes;
    }

    public final int getMate() {
        return mate;
    }

    public final void setMate(int pmate) {
        this.mate = pmate;
    }

    public final int getMovetime() {
        return movetime;
    }

    public final void setMovetime(int pmovetime) {
        this.movetime = pmovetime;
    }

    public final boolean isInfinite() {
        return infinite;
    }

    public final void setInfinite(boolean pinfinite) {
        this.infinite = pinfinite;
    }

    private MoveList searchMoves = null;
    private boolean ponderMode = false;
    private int wmsec = -1;
    private int bmsec = -1;
    private int winc = -1;
    private int binc = -1;
    private int movesToGo = 0;
    private int depth = -1;
    private int nodes = -1;
    private int mate = -1;
    private int movetime = -1;
    private boolean infinite = false;
}
