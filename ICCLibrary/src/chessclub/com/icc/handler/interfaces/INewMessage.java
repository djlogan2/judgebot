package chessclub.com.icc.handler.interfaces;

import chessclub.com.icc.l2.MessageListItem;

public interface INewMessage extends IAbstractICCHandler {
    void newMessage(MessageListItem p);
}
