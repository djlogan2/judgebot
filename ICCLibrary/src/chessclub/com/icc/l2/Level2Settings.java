package chessclub.com.icc.l2;

import chessclub.com.enums.L2;

public class Level2Settings {
    private byte[] settings = new byte[L2.values().length];

    public Level2Settings() {
        for (int x = 0; x < settings.length; x++) {
            settings[x] = '0';
        }
//        settings[L2.GAMELIST_BEGIN.ordinal()] = '1';
//        settings[L2.GAMELIST_ITEM.ordinal()] = '1';
//        settings[L2.LIST.ordinal()] = '1';
        settings[L2.LOGIN_FAILED.ordinal()] = '1';
//        settings[L2.MESSAGELIST_BEGIN.ordinal()] = '1';
//        settings[L2.MESSAGELIST_ITEM.ordinal()] = '1';
        settings[L2.MOVE_ALGEBRAIC.ordinal()] = '1';
        settings[L2.MOVE_CLOCK.ordinal()] = '1';
        settings[L2.MOVE_LIST.ordinal()] = '1';
        settings[L2.MOVE_SMITH.ordinal()] = '1';
        settings[L2.MOVE_TIME.ordinal()] = '1';
//        settings[L2.OFFERS_IN_MY_GAME.ordinal()] = '1';
//        settings[L2.MSEC.ordinal()] = '1';
//        settings[L2.MY_GAME_ENDED.ordinal()] = '1';
//        settings[L2.MY_GAME_RESULT.ordinal()] = '1';
//        settings[L2.MY_GAME_STARTED.ordinal()] = '1';
//        settings[L2.MY_NOTIFY_LIST.ordinal()] = '1';
//        settings[L2.NOTIFY_ARRIVED.ordinal()] = '1';
//        settings[L2.NOTIFY_LEFT.ordinal()] = '1';
//        settings[L2.PERSONAL_QTELL.ordinal()] = '1';
//        settings[L2.PERSONAL_TELL_ECHO.ordinal()] = '1';
//        settings[L2.PERSONAL_TELL.ordinal()] = '1';
//        settings[L2.RATING_TYPE_KEY.ordinal()] = '1';
//        settings[L2.SEND_MOVES.ordinal()] = '1';
        settings[L2.SET2.ordinal()] = '1';
//        settings[L2.TAKEBACK.ordinal()] = '1';
        settings[L2.WHO_AM_I.ordinal()] = '1';
        settings[L2.ERROR.ordinal()] = '1';
    }

    @Override
    public String toString() {
        return new String(settings);
    }

    /**
     * This is sent right after I login. It tells the client what my name is. This is needed because level 2 DGs do not use a "*" for my name (to avoid having to generate different messages for everybody) and because if I login with "g" the client needs to
     * be told what my name is.
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean whoAmI() {
        return settings[L2.WHO_AM_I.ordinal()] == '1';
    }

    /**
     * This is sent right after I login. It tells the client what my name is. This is needed because level 2 DGs do not use a "*" for my name (to avoid having to generate different messages for everybody) and because if I login with "g" the client needs to
     * be told what my name is.
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void whoAmI(final boolean b) {
        settings[L2.WHO_AM_I.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * This indicates that the client wants to maintain the set of logged in players. Whenever a player connects a DG_PLAYER_ARRIVED DG is generated of the following form: (playername <ratings> <open> <state>)
     * 
     * The <ratings> info depends on the setting of these level-2 variables described below: DG_BULLET, DG_BLITZ, DG_STANDARD, DG_WILD, DG_BUGHOUSE, DG_LOSERS, DG_CRAZYHOUSE, DG_FIVEMINUTE, DG_ONEMINUTE, DG_CORRESPONDENCE_RATING, DG_FIFTEENMINUTE,
     * DG_THREEMINUTE, DG_FORTYFIVEMINUTE, DG_CHESS960. A rating will be sent for each rating datagram that has been turned on. Ratings are sent in pairs of numbers, first the rating and then the annotation, as described in DG_BULLET. The ratings will be
     * sent in DG order, as listed above. Turn on modifier variables (e.g. DG_BULLET) before turning on this one.
     * 
     * The <open> info is a single number, as described in DG_OPEN.
     * 
     * The <state> info is a pair of fields (state plus game number), as described in DG_STATE.
     * 
     * When this DG is turned on (with set-2 or during login), a raw list of DGs of this type is sent to me for all the current players. Note that when the server is crowded this can be a lot of data, so it is not recommended that this happen without the
     * user expressly requesting it.
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean playerArrived() {
        return settings[L2.PLAYER_ARRIVED.ordinal()] == '1';
    }

    /**
     * This indicates that the client wants to maintain the set of logged in players. Whenever a player connects a DG_PLAYER_ARRIVED DG is generated of the following form: (playername <ratings> <open> <state>)
     * 
     * The <ratings> info depends on the setting of these level-2 variables described below: DG_BULLET, DG_BLITZ, DG_STANDARD, DG_WILD, DG_BUGHOUSE, DG_LOSERS, DG_CRAZYHOUSE, DG_FIVEMINUTE, DG_ONEMINUTE, DG_CORRESPONDENCE_RATING, DG_FIFTEENMINUTE,
     * DG_THREEMINUTE, DG_FORTYFIVEMINUTE, DG_CHESS960. A rating will be sent for each rating datagram that has been turned on. Ratings are sent in pairs of numbers, first the rating and then the annotation, as described in DG_BULLET. The ratings will be
     * sent in DG order, as listed above. Turn on modifier variables (e.g. DG_BULLET) before turning on this one.
     * 
     * The <open> info is a single number, as described in DG_OPEN.
     * 
     * The <state> info is a pair of fields (state plus game number), as described in DG_STATE.
     * 
     * When this DG is turned on (with set-2 or during login), a raw list of DGs of this type is sent to me for all the current players. Note that when the server is crowded this can be a lot of data, so it is not recommended that this happen without the
     * user expressly requesting it.
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void playerArrived(final boolean b) {
        settings[L2.PLAYER_ARRIVED.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     *  A player disconnected.
     *  Form: (playername)
     * @return  true if we are to get level 2 packets, false if not
     */
    public boolean playerLeft() {
        return settings[L2.PLAYER_LEFT.ordinal()] == '1';
    }

    /**
     *  A player disconnected.
     *  Form: (playername)
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void playerLeft(final boolean b) {
        settings[L2.PLAYER_LEFT.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * "bullet" rating.
     * 
     * The form of every rating is the following:
     * 
     * (playername rating annotation)
     * 
     * The rating is a decimal number. The annotation is 0 if the player has no rating in that category, 1 for a provisional rating, 2 for an established rating.
     * 
     * Shouldn't get rating DGs for unregs. However this information will be included in the DG_PLAYER_ARRIVED type DG (just as a "0 0" for each rating selected).
     * 
     * When level2setting[DG_BULLET]=1 and level2setting[DG_PLAYER_ARRIVED]=1, I get a rating DG any time someone's rating changes (i.e. when their game ends), and a dg_player_arrived message instead when I or he connect. If
     * level2setting[DG_PLAYER_ARRIVED]=1, and I do a set-2 to enable DG_BULLET ratings, I get a list of bullet rating messages. No rating DG messages are sent unless DG_PLAYER_ARRIVED is also enabled.
     * 
     * @return   true if we are to get the rating in level 2 packet data, false if not
     */
    public boolean bullet() {
        return settings[L2.BULLET.ordinal()] == '1';
    }

    /**
     * "bullet" rating.
     * 
     * The form of every rating is the following:
     * 
     * (playername rating annotation)
     * 
     * The rating is a decimal number. The annotation is 0 if the player has no rating in that category, 1 for a provisional rating, 2 for an established rating.
     * 
     * Shouldn't get rating DGs for unregs. However this information will be included in the DG_PLAYER_ARRIVED type DG (just as a "0 0" for each rating selected).
     * 
     * When level2setting[DG_BULLET]=1 and level2setting[DG_PLAYER_ARRIVED]=1, I get a rating DG any time someone's rating changes (i.e. when their game ends), and a dg_player_arrived message instead when I or he connect. If
     * level2setting[DG_PLAYER_ARRIVED]=1, and I do a set-2 to enable DG_BULLET ratings, I get a list of bullet rating messages. No rating DG messages are sent unless DG_PLAYER_ARRIVED is also enabled.
     * 
     * @param b   true if we are to get the rating in level 2 packet data, false if not
     */
    public void bullet(final boolean b) {
        settings[L2.BULLET.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * The "blitz" rating.
     * @see #bullet()
     * @return  true if we are to get the rating in level 2 packet data, false if not
     */
    public boolean blitz() {
        return settings[L2.BLITZ.ordinal()] == '1';
    }

    /**
     * The "blitz" rating.
     * @see #bullet()
     * @param b   true if we are to get the rating in level 2 packet data, false if not
     */
    public void blitz(final boolean b) {
        settings[L2.BLITZ.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * The "standard" rating.
     * @see #bullet()
     * @return  true if we are to get the rating in level 2 packet data, false if not
     */
    public boolean standard() {
        return settings[L2.STANDARD.ordinal()] == '1';
    }

    /**
     * The "standard" rating.
     * @see #bullet()
     * @param b   true if we are to get the rating in level 2 packet data, false if not
     */
    public void standard(final boolean b) {
        settings[L2.STANDARD.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * The "wild" rating.
     * @see #bullet()
     * @return  true if we are to get the rating in level 2 packet data, false if not
     */
    public boolean wild() {
        return settings[L2.WILD.ordinal()] == '1';
    }

    /**
     * The "wild" rating.
     * @see #bullet()
     * @param b   true if we are to get the rating in level 2 packet data, false if not
     */
    public void wild(final boolean b) {
        settings[L2.WILD.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * The "bughouse" rating.
     * @see #bullet()
     * @return  true if we are to get the rating in level 2 packet data, false if not
     */
    public boolean bughouse() {
        return settings[L2.BUGHOUSE.ordinal()] == '1';
    }

    /**
     * The "bughouse" rating.
     * @see #bullet()
     * @param b true if we are to get the rating in level 2 packet data, false if not
     */
    public void bughouse(final boolean b) {
        settings[L2.BUGHOUSE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     *  form: (playername timestamp-client-number)
     *  Timestamp Client information of this player: Actually these are never sent; they only appear
     *  as part of a player-arrived DG.
     * @return  true if we are getting timestamp in playerArrived packets, false if not
     */
    public boolean timeStamp() {
        return settings[L2.TIMESTAMP.ordinal()] == '1';
    }

    /**
     *  form: (playername timestamp-client-number)
     *  Timestamp Client information of this player: Actually these are never sent; they only appear
     *  as part of a player-arrived DG.
     * @param b true if we are want timestamp in playerArrived packets, false if not
     */
    public void timeStamp(final boolean b) {
        settings[L2.TIMESTAMP.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     *  form: (playername titles)
     *  Title DGs are rarely sent (only when somebody's titles
     *  change; most commonly, when an admin turns their "*" on or off).
     *  Again, these player-info DGs are only sent when
     *  DG_PLAYER_ARRIVED messages are also enabled.
     * @return  true if we want title changes sent to us, false if we do not
     */
    public boolean titles() {
        return settings[L2.TITLES.ordinal()] == '1';
    }

    /**
     *  form: (playername titles)
     *  Title DGs are rarely sent (only when somebody's titles
     *  change; most commonly, when an admin turns their "*" on or off).
     *  Again, these player-info DGs are only sent when
     *  DG_PLAYER_ARRIVED messages are also enabled.
     * @param b true if we want title changes sent to us, false if we do not
     */
    public void titles(final boolean b) {
        settings[L2.TITLES.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * No documentation available in iccformat.txt.
     * @return  true if we want DG_OPEN packets, false if we do not
     */
    public boolean open() {
        return settings[L2.OPEN.ordinal()] == '1';
    }

    /**
     * No documentation available in iccformat.txt.
     * @param b true if we want DG_OPEN packets, false if we do not
     */
    public void open(final boolean b) {
        settings[L2.OPEN.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * Return player state information.
     *  form: (playername P 23)
     *  State information (including game number)
     *  P=playing
     *  E=examining
     *  S=playing simul
     *  X=none of the above
     *  This information is sent to you via #figureitout packets
     * @return  true if we want player state information, false if we do not
     */
    public boolean state() {
        return settings[L2.STATE.ordinal()] == '1';
    }

    /**
     * Return player state information.
     *  form: (playername P 23)
     *  State information (including game number)
     *  P=playing
     *  E=examining
     *  S=playing simul
     *  X=none of the above
     *  This information is sent to you via #figureitout packets
     * @param b true if we want player state information, false if we do not
     */
    public void state(final boolean b) {
        settings[L2.STATE.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean gameStarted() {
        return settings[L2.GAME_STARTED.ordinal()] == '1';
    }

    public void gameStarted(final boolean b) {
        settings[L2.GAME_STARTED.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean gameResult() {
        return settings[L2.GAME_RESULT.ordinal()] == '1';
    }

    public void gameResult(final boolean b) {
        settings[L2.GAME_RESULT.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean examinedGameIsGone() {
        return settings[L2.EXAMINED_GAME_IS_GONE.ordinal()] == '1';
    }

    public void examinedGameIsGone(final boolean b) {
        settings[L2.EXAMINED_GAME_IS_GONE.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean myGameStarted() {
        return settings[L2.MY_GAME_STARTED.ordinal()] == '1';
    }

    public void myGameStarted(final boolean b) {
        settings[L2.MY_GAME_STARTED.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean myGameResult() {
        return settings[L2.MY_GAME_RESULT.ordinal()] == '1';
    }

    public void myGameResult(final boolean b) {
        settings[L2.MY_GAME_RESULT.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean myGameEnded() {
        return settings[L2.MY_GAME_ENDED.ordinal()] == '1';
    }

    public void myGameEnded(final boolean b) {
        settings[L2.MY_GAME_ENDED.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean startedObserving() {
        return settings[L2.STARTED_OBSERVING.ordinal()] == '1';
    }

    public void startedObserving(final boolean b) {
        settings[L2.STARTED_OBSERVING.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean stopObserving() {
        return settings[L2.STOP_OBSERVING.ordinal()] == '1';
    }

    public void stopObserving(final boolean b) {
        settings[L2.STOP_OBSERVING.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean playersInMyGame() {
        return settings[L2.PLAYERS_IN_MY_GAME.ordinal()] == '1';
    }

    public void playersInMyGame(final boolean b) {
        settings[L2.PLAYERS_IN_MY_GAME.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean offersInMyGame() {
        return settings[L2.OFFERS_IN_MY_GAME.ordinal()] == '1';
    }

    public void offersInMyGame(final boolean b) {
        settings[L2.OFFERS_IN_MY_GAME.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean takeback() {
        return settings[L2.TAKEBACK.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void takeback(final boolean b) {
        settings[L2.TAKEBACK.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean backward() {
        return settings[L2.BACKWARD.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void backward(final boolean b) {
        settings[L2.BACKWARD.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean sendMoves() {
        return settings[L2.SEND_MOVES.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void sendMoves(final boolean b) {
        settings[L2.SEND_MOVES.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean moveList() {
        return settings[L2.MOVE_LIST.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void moveList(final boolean b) {
        settings[L2.MOVE_LIST.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean kibitz() {
        return settings[L2.KIBITZ.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void kibitz(final boolean b) {
        settings[L2.KIBITZ.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean peopleInMyChannel() {
        return settings[L2.PEOPLE_IN_MY_CHANNEL.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void peopleInMyChannel(final boolean b) {
        settings[L2.PEOPLE_IN_MY_CHANNEL.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean channelTell() {
        return settings[L2.CHANNEL_TELL.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void channelTell(final boolean b) {
        settings[L2.CHANNEL_TELL.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean match() {
        return settings[L2.MATCH.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void match(final boolean b) {
        settings[L2.MATCH.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean matchRemoved() {
        return settings[L2.MATCH_REMOVED.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void matchRemoved(final boolean b) {
        settings[L2.MATCH_REMOVED.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean personalTell() {
        return settings[L2.PERSONAL_TELL.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void personalTell(final boolean b) {
        settings[L2.PERSONAL_TELL.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean shout() {
        return settings[L2.SHOUT.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void shout(final boolean b) {
        settings[L2.SHOUT.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean moveAlgebraic() {
        return settings[L2.MOVE_ALGEBRAIC.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void moveAlgebraic(final boolean b) {
        settings[L2.MOVE_ALGEBRAIC.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean moveSmith() {
        return settings[L2.MOVE_SMITH.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void moveSmith(final boolean b) {
        settings[L2.MOVE_SMITH.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean moveTime() {
        return settings[L2.MOVE_TIME.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void moveTime(final boolean b) {
        settings[L2.MOVE_TIME.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean moveClock() {
        return settings[L2.MOVE_CLOCK.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void moveClock(final boolean b) {
        settings[L2.MOVE_CLOCK.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean bughouseHoldings() {
        return settings[L2.BUGHOUSE_HOLDINGS.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void bughouseHoldings(final boolean b) {
        settings[L2.BUGHOUSE_HOLDINGS.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean setClock() {
        return settings[L2.SET_CLOCK.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void setClock(final boolean b) {
        settings[L2.SET_CLOCK.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean flip() {
        return settings[L2.FLIP.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void flip(final boolean b) {
        settings[L2.FLIP.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean ackPing() {
        return settings[L2.ACK_PING.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void ackPing(final boolean b) {
        settings[L2.ACK_PING.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean arrow() {
        return settings[L2.ARROW.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void arrow(final boolean b) {
        settings[L2.ARROW.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean boardInfo() {
        return settings[L2.BOARDINFO.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void boardInfo(final boolean b) {
        settings[L2.BOARDINFO.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean bughousePass() {
        return settings[L2.BUGHOUSE_PASS.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void bughousePass(final boolean b) {
        settings[L2.BUGHOUSE_PASS.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean channelsShared() {
        return settings[L2.CHANNELS_SHARED.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void channelsShared(final boolean b) {
        settings[L2.CHANNELS_SHARED.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean channelQtell() {
        return settings[L2.CHANNEL_QTELL.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void channelQtell(final boolean b) {
        settings[L2.CHANNEL_QTELL.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean chess960() {
        return settings[L2.CHESS960.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void chess960(final boolean b) {
        settings[L2.CHESS960.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean circle() {
        return settings[L2.CIRCLE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void circle(final boolean b) {
        settings[L2.CIRCLE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean command() {
        return settings[L2.COMMAND.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void command(final boolean b) {
        settings[L2.COMMAND.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean correspondenceRating() {
        return settings[L2.CORRESPONDENCE_RATING.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void correspondenceRating(final boolean b) {
        settings[L2.CORRESPONDENCE_RATING.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean crazyhouse() {
        return settings[L2.CRAZYHOUSE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void crazyhouse(final boolean b) {
        settings[L2.CRAZYHOUSE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean dialogData() {
        return settings[L2.DIALOG_DATA.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void dialogData(final boolean b) {
        settings[L2.DIALOG_DATA.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean dialogDefault() {
        return settings[L2.DIALOG_DEFAULT.ordinal()] == '1';
    }

    // public boolean dialogEnd() { return settings[L2.DIALOG_END.ordinal()]=='1'; }
    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void dialogDefault(final boolean b) {
        settings[L2.DIALOG_DEFAULT.ordinal()] = (byte) (b ? '1' : '0');
    }

    // public boolean dialogEnd() { return settings[L2.DIALOG_END.ordinal()]=='1'; }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean dialogRelease() {
        return settings[L2.DIALOG_RELEASE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void dialogRelease(final boolean b) {
        settings[L2.DIALOG_RELEASE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean dialogStart() {
        return settings[L2.DIALOG_START.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void dialogStart(final boolean b) {
        settings[L2.DIALOG_START.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean disablePremove() {
        return settings[L2.DISABLE_PREMOVE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void disablePremove(final boolean b) {
        settings[L2.DISABLE_PREMOVE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean dummyResponse() {
        return settings[L2.DUMMY_RESPONSE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void dummyResponse(final boolean b) {
        settings[L2.DUMMY_RESPONSE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean fen() {
        return settings[L2.FEN.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void fen(final boolean b) {
        settings[L2.FEN.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean fifteenMinute() {
        return settings[L2.FIFTEENMINUTE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void fifteenMinute(final boolean b) {
        settings[L2.FIFTEENMINUTE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean fiveMinute() {
        return settings[L2.FIVEMINUTE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void fiveMinute(final boolean b) {
        settings[L2.FIVEMINUTE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean fortyFiveMinute() {
        return settings[L2.FORTYFIVEMINUTE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void fortyFiveMinute(final boolean b) {
        settings[L2.FORTYFIVEMINUTE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean gamelistBegin() {
        return settings[L2.GAMELIST_BEGIN.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void gamelistBegin(final boolean b) {
        settings[L2.GAMELIST_BEGIN.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean gamelistItem() {
        return settings[L2.GAMELIST_ITEM.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void gamelistItem(final boolean b) {
        settings[L2.GAMELIST_ITEM.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean gameMessage() {
        return settings[L2.GAME_MESSAGE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void gameMessage(final boolean b) {
        settings[L2.GAME_MESSAGE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean idle() {
        return settings[L2.IDLE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void idle(final boolean b) {
        settings[L2.IDLE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean illegalMove() {
        return settings[L2.ILLEGAL_MOVE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void illegalMove(final boolean b) {
        settings[L2.ILLEGAL_MOVE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean isolatedBoard() {
        return settings[L2.ISOLATED_BOARD.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void isolatedBoard(final boolean b) {
        settings[L2.ISOLATED_BOARD.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean isVariation() {
        return settings[L2.IS_VARIATION.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void isVariation(final boolean b) {
        settings[L2.IS_VARIATION.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean jBoard() {
        return settings[L2.JBOARD.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void jBoard(final boolean b) {
        settings[L2.JBOARD.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean knowsFischerRandom() {
        return settings[L2.KNOWS_FISCHER_RANDOM.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void knowsFischerRandom(final boolean b) {
        settings[L2.KNOWS_FISCHER_RANDOM.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean list() {
        return settings[L2.LIST.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void list(final boolean b) {
        settings[L2.LIST.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean loginFailed() {
        return settings[L2.LOGIN_FAILED.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void loginFailed(final boolean b) {
        settings[L2.LOGIN_FAILED.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean logPGN() {
        return settings[L2.LOG_PGN.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void logPGN(final boolean b) {
        settings[L2.LOG_PGN.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean losers() {
        return settings[L2.LOSERS.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void losers(final boolean b) {
        settings[L2.LOSERS.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean matchAssessment() {
        return settings[L2.MATCH_ASSESSMENT.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void matchAssessment(final boolean b) {
        settings[L2.MATCH_ASSESSMENT.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean messageListBegin() {
        return settings[L2.MESSAGELIST_BEGIN.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void messageListBegin(final boolean b) {
        settings[L2.MESSAGELIST_BEGIN.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean messageListItem() {
        return settings[L2.MESSAGELIST_ITEM.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void messageListItem(final boolean b) {
        settings[L2.MESSAGELIST_ITEM.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean moreTime() {
        return settings[L2.MORETIME.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void moreTime(final boolean b) {
        settings[L2.MORETIME.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean moveLag() {
        return settings[L2.MOVE_LAG.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void moveLag(final boolean b) {
        settings[L2.MOVE_LAG.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean msec() {
        return settings[L2.MSEC.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void msec(final boolean b) {
        settings[L2.MSEC.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean mugshot() {
        return settings[L2.MUGSHOT.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void mugshot(final boolean b) {
        settings[L2.MUGSHOT.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean myGameChange() {
        return settings[L2.MY_GAME_CHANGE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void myGameChange(final boolean b) {
        settings[L2.MY_GAME_CHANGE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean myNotifyList() {
        return settings[L2.MY_NOTIFY_LIST.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void myNotifyList(final boolean b) {
        settings[L2.MY_NOTIFY_LIST.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean myRelationToGame() {
        return settings[L2.MY_RELATION_TO_GAME.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void myRelationToGame(final boolean b) {
        settings[L2.MY_RELATION_TO_GAME.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean myStringVariable() {
        return settings[L2.MY_STRING_VARIABLE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void myStringVariable(final boolean b) {
        settings[L2.MY_STRING_VARIABLE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean myTurn() {
        return settings[L2.MY_TURN.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void myTurn(final boolean b) {
        settings[L2.MY_TURN.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean myVariable() {
        return settings[L2.MY_VARIABLE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void myVariable(final boolean b) {
        settings[L2.MY_VARIABLE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean newMyRating() {
        return settings[L2.MY_RATING.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void newMyRating(final boolean b) {
        settings[L2.MY_RATING.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean notifyArrived() {
        return settings[L2.NOTIFY_ARRIVED.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void notifyArrived(final boolean b) {
        settings[L2.NOTIFY_ARRIVED.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean notifyLeft() {
        return settings[L2.NOTIFY_LEFT.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void notifyLeft(final boolean b) {
        settings[L2.NOTIFY_LEFT.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean notifyOpen() {
        return settings[L2.NOTIFY_OPEN.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void notifyOpen(final boolean b) {
        settings[L2.NOTIFY_OPEN.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean notifyState() {
        return settings[L2.NOTIFY_STATE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void notifyState(final boolean b) {
        settings[L2.NOTIFY_STATE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean oneMinute() {
        return settings[L2.ONEMINUTE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void oneMinute(final boolean b) {
        settings[L2.ONEMINUTE.ordinal()] = (byte) (b ? '1' : '0');
    }

    /**
     * 
     * @return true if we are to get level 2 packets, false if not
     */
    public boolean partnerShip() {
        return settings[L2.PARTNERSHIP.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void partnerShip(final boolean b) {
        settings[L2.PARTNERSHIP.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean password() {
        return settings[L2.PASSWORD.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void password(final boolean b) {
        settings[L2.PASSWORD.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean pastMove() {
        return settings[L2.PAST_MOVE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void pastMove(final boolean b) {
        settings[L2.PAST_MOVE.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean personalQtell() {
        return settings[L2.PERSONAL_QTELL.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void personalQtell(final boolean b) {
        settings[L2.PERSONAL_QTELL.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean personalTellEcho() {
        return settings[L2.PERSONAL_TELL_ECHO.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void personalTellEcho(final boolean b) {
        settings[L2.PERSONAL_TELL_ECHO.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean pgnTag() {
        return settings[L2.PGN_TAG.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void pgnTag(final boolean b) {
        settings[L2.PGN_TAG.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean playerArrivedSimple() {
        return settings[L2.PLAYER_ARRIVED_SIMPLE.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void playerArrivedSimple(final boolean b) {
        settings[L2.PLAYER_ARRIVED_SIMPLE.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean positionBegin() {
        return settings[L2.POSITION_BEGIN.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void positionBegin(final boolean b) {
        settings[L2.POSITION_BEGIN.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean positionBegin2() {
        return settings[L2.POSITION_BEGIN2.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void positionBegin2(final boolean b) {
        settings[L2.POSITION_BEGIN2.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean pstat() {
        return settings[L2.PSTAT.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void pstat(final boolean b) {
        settings[L2.PSTAT.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean qretract() {
        return settings[L2.QRETRACT.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void qretract(final boolean b) {
        settings[L2.QRETRACT.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean ratingTypeKey() {
        return settings[L2.RATING_TYPE_KEY.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void ratingTypeKey(final boolean b) {
        settings[L2.RATING_TYPE_KEY.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean refresh() {
        return settings[L2.REFRESH.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void refresh(final boolean b) {
        settings[L2.REFRESH.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean removeTourney() {
        return settings[L2.REMOVE_TOURNEY.ordinal()] == '1';
    }

    /**
     * 
     * @param b
     *            true if we are to get level 2 packets, false if not
     */
    public void removeTourney(final boolean b) {
        settings[L2.REMOVE_TOURNEY.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean seek() {
        return settings[L2.SEEK.ordinal()] == '1';
    }

    public void seek(final boolean b) {
        settings[L2.SEEK.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean seekRemoved() {
        return settings[L2.SEEK_REMOVED.ordinal()] == '1';
    }

    public void seekRemoved(final boolean b) {
        settings[L2.SEEK_REMOVED.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean seesShouts() {
        return settings[L2.SEES_SHOUTS.ordinal()] == '1';
    }

    public void seesShouts(final boolean b) {
        settings[L2.SEES_SHOUTS.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean set2() {
        return settings[L2.SET2.ordinal()] == '1';
    }

    public void set2(final boolean b) {
        settings[L2.SET2.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean setBoard() {
        return settings[L2.SET_BOARD.ordinal()] == '1';
    }

    public void setBoard(final boolean b) {
        settings[L2.SET_BOARD.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean sjiAd() {
        return settings[L2.SJI_AD.ordinal()] == '1';
    }

    public void sjiAd(final boolean b) {
        settings[L2.SJI_AD.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean sound() {
        return settings[L2.SOUND.ordinal()] == '1';
    }

    public void sound(final boolean b) {
        settings[L2.SOUND.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean stringlistBegin() {
        return settings[L2.STRINGLIST_BEGIN.ordinal()] == '1';
    }

    public void stringlistBegin(final boolean b) {
        settings[L2.STRINGLIST_BEGIN.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean stringlistItem() {
        return settings[L2.STRINGLIST_ITEM.ordinal()] == '1';
    }

    public void stringlistItem(final boolean b) {
        settings[L2.STRINGLIST_ITEM.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean suggestion() {
        return settings[L2.SUGGESTION.ordinal()] == '1';
    }

    public void suggestion(final boolean b) {
        settings[L2.SUGGESTION.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean switchServers() {
        return settings[L2.SWITCH_SERVERS.ordinal()] == '1';
    }

    public void switchServers(final boolean b) {
        settings[L2.SWITCH_SERVERS.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean threeMinute() {
        return settings[L2.THREEMINUTE.ordinal()] == '1';
    }

    public void threeMinute(final boolean b) {
        settings[L2.THREEMINUTE.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean tourney() {
        return settings[L2.TOURNEY.ordinal()] == '1';
    }

    public void tourney(final boolean b) {
        settings[L2.TOURNEY.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean tourneyGameEnded() {
        return settings[L2.TOURNEY_GAME_ENDED.ordinal()] == '1';
    }

    public void tourneyGameEnded(final boolean b) {
        settings[L2.TOURNEY_GAME_ENDED.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean tourneyGameStarted() {
        return settings[L2.TOURNEY_GAME_STARTED.ordinal()] == '1';
    }

    public void tourneyGameStarted(final boolean b) {
        settings[L2.TOURNEY_GAME_STARTED.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean translationOkay() {
        return settings[L2.TRANSLATION_OKAY.ordinal()] == '1';
    }

    public void translationOkay(final boolean b) {
        settings[L2.TRANSLATION_OKAY.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean uid() {
        return settings[L2.UID.ordinal()] == '1';
    }

    public void uid(final boolean b) {
        settings[L2.UID.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean unaccented() {
        return settings[L2.UNACCENTED.ordinal()] == '1';
    }

    public void unaccented(final boolean b) {
        settings[L2.UNACCENTED.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean unarrow() {
        return settings[L2.UNARROW.ordinal()] == '1';
    }

    public void unarrow(final boolean b) {
        settings[L2.UNARROW.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean uncircle() {
        return settings[L2.UNCIRCLE.ordinal()] == '1';
    }

    public void uncircle(final boolean b) {
        settings[L2.UNCIRCLE.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean wildKey() {
        return settings[L2.WILD_KEY.ordinal()] == '1';
    }

    public void wildKey(final boolean b) {
        settings[L2.WILD_KEY.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean wsuggest() {
        return settings[L2.WSUGGEST.ordinal()] == '1';
    }

    public void wsuggest(final boolean b) {
        settings[L2.WSUGGEST.ordinal()] = (byte) (b ? '1' : '0');
    }

    public boolean isSet(final L2 l2) {
        return settings[l2.ordinal()] == '1';
    }

    public void set(final L2 l2, final boolean b) {
        settings[l2.ordinal()] = (byte) (b ? '1' : '0');
    }
}
