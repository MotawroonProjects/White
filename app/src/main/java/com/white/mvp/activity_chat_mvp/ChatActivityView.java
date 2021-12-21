package com.white.mvp.activity_chat_mvp;

import com.white.models.MessageDataModel;
import com.white.models.MessageModel;

public interface ChatActivityView {
    void onFinished();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);


    void ondataload(MessageModel body);

    void ondataloadmore(MessageModel body);

    void onremove();
    void onLoad();
    void onFinishload();

    void ondataload(MessageDataModel body);

    void onfinishloadmore();

    void onloadmore();
}
