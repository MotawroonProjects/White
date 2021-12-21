package com.white.models;

import java.io.Serializable;
import java.util.List;

public class DayModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class  Data implements Serializable{
        private int id;
        private int doctor_id;
        private String day_name;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public int getDoctor_id() {
            return doctor_id;
        }

        public String getDay_name() {
            return day_name;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
