package com.white.mvp.activity_doctor_detials_mvp;

import com.white.models.SingleDataDoctorModel;

public interface DoctorDetialsActivityView {
    void onFinished();
    void onLoad();
    void onFinishload();
    void onFailed(String msg);


    void ondoctorsucess(SingleDataDoctorModel body);

    void onlikesucess();
}
