package com.white.models;

import java.io.Serializable;
import java.util.List;

public class DrugTimeModel implements Serializable {
    private List<Data> data;

    public void setData(List<Data> data) {

        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable {
        private String title;
        private String date;
        private int takenum;

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getTakenum() {
            return takenum;
        }

        public void setTakenum(int takenum) {
            this.takenum = takenum;
        }
    }
}
