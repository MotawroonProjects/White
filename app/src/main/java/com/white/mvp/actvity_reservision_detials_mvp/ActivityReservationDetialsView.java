package com.white.mvp.actvity_reservision_detials_mvp;

import com.white.models.ApointmentModel;
import com.white.models.ReasonModel;

public interface ActivityReservationDetialsView {
    void onLoad();
    void onFinishload();
    void onFailed(String msg);

    void onsucsess();

    void onServer();

    void onnotlogin();

    void onnotconnect(String toLowerCase);

    void onFailed();

    void onProgressShow();

    void onProgressHide();

    void onSuccess(ReasonModel body);

    void onSuccess(ApointmentModel.Data data);

    void oncloseSuccess();
}
