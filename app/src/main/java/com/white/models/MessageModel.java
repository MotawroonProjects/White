package com.white.models;

import java.io.Serializable;
import java.util.List;

public class MessageModel implements Serializable {
    private List<MessageModel> data;
    private int current_page;

    public MessageModel(String room_id) {
        this.room_id=room_id;
    }


    public List<MessageModel> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    private int medical_consulting_id;
    private int id;
    private String to_user_id;
    private String from_user_id;
    private String type;
    private String message;
    private String image;
    private String voice;
    private String room_id;
    private String is_read;
    private long date;
    private User from_user_fk;



    public int getId() {
        return id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }

    public String getVoice() {
        return voice;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getIs_read() {
        return is_read;
    }

    public long getDate() {
        return date;
    }

    public User getFrom() {
        return from_user_fk;
    }

    public int getMedical_consulting_id() {
        return medical_consulting_id;
    }

    public static class User implements Serializable {
        private int id;
        private String name;
        private String email;
        private String phone_code;
        private String phone;
        private String image;
        private String logo;
        private String latitude;
        private String longitude;
        private String address;
        private String user_type;
        private String details;

        private String fireBaseToken;


        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }


        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getImage() {
            return image;
        }

        public String getLogo() {
            return logo;
        }


        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }

        public String getType() {
            return user_type;
        }

        public String getFireBaseToken() {
            return fireBaseToken;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }


        public String getDetails() {
            return details;
        }


    }

}