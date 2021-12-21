package com.white.mvp.fragment_apointment_mvp;

import com.white.models.ApointmentModel;

public interface ApointmentFragmentView {
    void onSuccess(ApointmentModel apointmentModel);
    void onFailed(String msg);
    void showProgressBar();
    void hideProgressBar();

}
