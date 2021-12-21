package com.white.models;

import java.io.Serializable;
import java.util.List;

public class ReservisionTimeModel implements Serializable {
    private List<SingleReservisionTimeModel> data;

    public List<SingleReservisionTimeModel> getData() {
        return data;
    }
}
