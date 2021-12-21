package com.white.models;

import java.io.Serializable;
import java.util.List;

public class FavouriteDoctorModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable {
        private SingleDoctorModel fav_fk;

        public SingleDoctorModel getFav_fk() {
            return fav_fk;
        }
    }
}
