package com.white.models;

import java.io.Serializable;
import java.util.List;

public class SingleReservisionTimeModel implements Serializable{
    private String hour;
    private String hour_type;
    private List<Detials> details;

    public String getHour() {
        return hour;
    }

    public String getHour_type() {
        return hour_type;
    }

    public List<Detials> getDetials() {
        return details;
    }

    public class Detials implements Serializable{
        private String from;
        private String to;
        private String from_hour_type;
        private String to_hour_type;
        private String type;

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getFrom_hour_type() {
            return from_hour_type;
        }

        public String getTo_hour_type() {
            return to_hour_type;
        }

        public String getType() {
            return type;
        }
    }
}
