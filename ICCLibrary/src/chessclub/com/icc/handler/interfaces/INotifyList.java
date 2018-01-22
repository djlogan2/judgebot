package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.MyNotifyList;

public interface INotifyList extends IAbstractICCHandler {
    /**
     * Called when a user is added or removed from the notify list. When first logging on,
     * all users are "added" via this call.
     * @param p The user/add/remove data in a {@link MyNotifyList} instance.
     */
    void myNotifyList(MyNotifyList p);
}
