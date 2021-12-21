package com.white.mvp.activity_confirm_code_mvp;

import com.white.models.UserModel;

public interface ActivityConfirmCodeView {
    void onCounterStarted(String time);
    void onCounterFinished();
    void onCodeFailed(String msg);
    void onUserFound(UserModel userModel);
    void onUserNoFound();
    void onFailed();
    void onServer();
    void onLoad();
    void onFinishload();
    void onnotconnect(String msg);
}
