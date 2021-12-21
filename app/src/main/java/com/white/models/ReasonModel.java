package com.white.models;

import java.io.Serializable;
import java.util.List;

public class ReasonModel implements Serializable {

private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable {
       private int id;
       private String reason;

        public int getId() {
            return id;
        }

        public String getReason() {
            return reason;
        }
    }
}
