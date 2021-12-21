package com.white.mvp.activity_notification_mvp;

import com.white.models.NotificationModel;

public interface NotificationActivityView {
    void onFinished();

    void onProgressShow();

    void onProgressHide();

    void onFailed(String msg);

    void onSuccess(NotificationModel notificationModel);
    void onLoad();

    void onFinishload();

    void onSuccessDelete();
}
