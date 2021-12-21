package com.white.mvp.activity_Emrgency_mvp;

import com.white.models.DoctorModel;

public interface EmergencyActivityView {
    void onFinished();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);


    void ondoctorsucess(DoctorModel body);
}
