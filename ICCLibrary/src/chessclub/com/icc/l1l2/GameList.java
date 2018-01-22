package chessclub.com.icc.l1l2;

import java.util.ArrayList;

import chessclub.com.icc.l2.GameListItem;
import chessclub.com.icc.l2.GamelistBegin;

/**
 * The class that has game lists when a user issues an ICC command that returns a game list.
 * @author David Logan
 *
 */
public class GameList {
    private boolean nohits = false;
    private GamelistBegin begin = null;
    private ArrayList<GameListItem> items = new ArrayList<GameListItem>();

    public GameList()
    {
        nohits = true;
    }
    public String level1Key() {
        return begin.level1Key();
    }
    /**
     * The command (one word) that initiated this game list.
     * @return  The command that initiated this game list.
     */
    public String command() {
        return begin.command();
    }

    /**
     * The parameters (everything but the command word) that initiated this game list.
     * @return  The parameters that initiated this game list.
     */
    public String[] parameters() {
        return begin.parameters();
    }

    /**
     * Number of games in this list.
     * @return  Number of games in this list.
     */
    public int nhits() {
        if(nohits)
            return 0;
        else
            return begin.nhits();
    }

    /**
     * The index of of the first game.
     * @return  The index of of the first game.
     */
    public int first() {
        return begin.first();
    }

    /**
     * The index of of the last game.
     * @return  The index of of the last game.
     */
    public int last() {
        return begin.last();
    }

    /**
     * The summary field of the GAMELIST_BEGIN packet. (I'm not sure what this is exactly.)
     * TODO Find out what this is and modify the javadoc.
     * @return  The summary field of the GAMELIST_BEGIN packet. 
     */
    public String summary() {
        return begin.summary();
    }

    /**
     * Not for public use. The constructor, and it takes the associated {@link GamelistBegin} instance
     * @param pBegin    The {@link GamelistBegin} instance associated with this game list.
     */
    public GameList(final GamelistBegin pBegin) {
        this.begin = pBegin;
    }

    /**
     * Not for public use. adds games to this list.
     * @param item The {@link GameListItem} to be added to this list.
     * @return  true if we have added the last game. Returns false if there are more games that need to be added.
     */
    public boolean addgame(final GameListItem item) {
        this.items.add(item);
        return (items.size() == begin.nhits());
    }

    /**
     * The list of games in this list.
     * @return  The list of games. See {@link GameListItem} for details.
     */
    public GameListItem[] games() {
        GameListItem[] ret = new GameListItem[0];
        return items.toArray(ret);
    }
}
