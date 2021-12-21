package com.white.mvp.activity_patient_details_mvp;


public interface ActivityPatientDetailsView {
    void onFailed(String msg);
    void showProgressBar();
    void hideProgressBar();

}
