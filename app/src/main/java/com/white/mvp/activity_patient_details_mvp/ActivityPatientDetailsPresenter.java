package com.white.mvp.activity_patient_details_mvp;

import android.content.Context;

public class ActivityPatientDetailsPresenter {
    private ActivityPatientDetailsView view;
    private Context context;


    public ActivityPatientDetailsPresenter(ActivityPatientDetailsView view, Context context) {
        this.view = view;
        this.context = context;

    }

}