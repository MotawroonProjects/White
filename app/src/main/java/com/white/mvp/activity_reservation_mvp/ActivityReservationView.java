package com.white.mvp.activity_reservation_mvp;

import com.white.models.DayModel;
import com.white.models.ReservisionTimeModel;
import com.white.models.RoomIdModel;

public interface ActivityReservationView {
    void onDateSelected(String date, String dayname);
    void onLoad();
    void onFinishload();
    void onFailed(String msg);

    void onreservtimesucess(ReservisionTimeModel body);

    void onsucess(RoomIdModel body);

    void ondata(DayModel body);
}
