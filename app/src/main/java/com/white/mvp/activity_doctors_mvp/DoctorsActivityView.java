package com.white.mvp.activity_doctors_mvp;

import com.white.models.AllCityModel;
import com.white.models.AllSpiclixationModel;
import com.white.models.DoctorModel;

public interface DoctorsActivityView {
    void onFinished();
    void onProgressShow(int type);
    void onProgressHide(int type);
    void onFailed(String msg);
    void onSuccess(AllSpiclixationModel allSpiclixationModel);
    void onSuccesscitie(AllCityModel allCityModel);

    void ondoctorsucess(DoctorModel body);
}
