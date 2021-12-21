package com.white.mvp.activity_favourite_mvp;

import com.white.models.FavouriteDoctorModel;

public interface FavouriteActivityView {
    void onFinished();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);


    void ondoctorsucess(FavouriteDoctorModel body);
}
