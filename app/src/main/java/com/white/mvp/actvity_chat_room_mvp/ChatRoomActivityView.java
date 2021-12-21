package com.white.mvp.actvity_chat_room_mvp;

import com.white.models.UserRoomModelData;

public interface ChatRoomActivityView {
    void onFinished();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);

    void ondata(UserRoomModelData body);
}
