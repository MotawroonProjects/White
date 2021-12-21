package com.white.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {


    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public static class User implements Serializable {
        private int id;
        private String code;
        private String user_type;
        private String name;
        private String email = null;
        private String phone_code;
        private String phone;
        private String gender;
        private String address = null;
        private double latitude;
        private double longitude;
        private String specialization_id = null;
        private String job_degree_id = null;
        private String city_id = null;
        private String license_img = null;
        private String logo = null;
        private String banner = null;
        private String birth_day;
        private String blood_type;
        private String details = null;
        private double rates;
        private double app_cost;
        private String fireBaseToken;

        private double detection_price;
        private String appointment_time = null;
        private String is_emergency;
        private String email_verified_at = null;
        private String is_blocked;
        private String is_login;
        private String logout_time = null;
        private String is_confirmed;
        private String confirmation_code = null;
        private String forget_password_code = null;
        private String software_type;
        private String deleted_at = null;
        private String created_at;
        private String updated_at;
        private double distance;
        private String token;
        private String weight;
        private String length;
        private String fat_percentage;
        private List<DiseaseModel> users_with_diseases;

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getUser_type() {
            return user_type;
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

        public String getGender() {
            return gender;
        }

        public String getAddress() {
            return address;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getSpecialization_id() {
            return specialization_id;
        }

        public String getJob_degree_id() {
            return job_degree_id;
        }

        public String getCity_id() {
            return city_id;
        }

        public String getLicense_img() {
            return license_img;
        }

        public String getLogo() {
            return logo;
        }

        public String getBanner() {
            return banner;
        }

        public String getBirth_day() {
            return birth_day;
        }

        public String getBlood_type() {
            return blood_type;
        }

        public String getDetails() {
            return details;
        }

        public double getRates() {
            return rates;
        }

        public double getApp_cost() {
            return app_cost;
        }

        public double getDetection_price() {
            return detection_price;
        }

        public String getAppointment_time() {
            return appointment_time;
        }

        public String getIs_emergency() {
            return is_emergency;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public String getIs_blocked() {
            return is_blocked;
        }

        public String getIs_login() {
            return is_login;
        }

        public String getLogout_time() {
            return logout_time;
        }

        public String getIs_confirmed() {
            return is_confirmed;
        }

        public String getConfirmation_code() {
            return confirmation_code;
        }

        public String getForget_password_code() {
            return forget_password_code;
        }

        public String getSoftware_type() {
            return software_type;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public double getDistance() {
            return distance;
        }

        public String getToken() {
            return token;
        }

        public List<DiseaseModel> getUsers_with_diseases() {
            return users_with_diseases;
        }

        public String getWeight() {
            return weight;
        }

        public String getLength() {
            return length;
        }

        public String getFat_percentage() {
            return fat_percentage;
        }

        public String getFireBaseToken() {
            return fireBaseToken;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }
    }
}
