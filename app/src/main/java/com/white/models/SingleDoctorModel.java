package com.white.models;

import java.io.Serializable;
import java.util.List;

public class SingleDoctorModel implements Serializable {
    private int id;
    private String code;
    private String user_type;
    private String name;
    private String email;
    private String phone_code;
    private String phone;
    private String gender;
    private String address;
    private double latitude;
    private double longitude;
    private int specialization_id;
    private int job_degree_id;
    private int city_id;
    private String license_img;
    private String logo;
    private String banner;
    private String birth_day;
    private String blood_type;
    private String details;
    private double rates;
    private int app_cost;
    private String title_job_degree;
    private double detection_price;
    private String appointment_time;
    private String is_emergency;
    private String email_verified_at;
    private String is_blocked;
    private String is_login;
    private String logout_time;
    private int is_confirmed;
    private String confirmation_code;
    private String forget_password_code;
    private String software_type;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private double distance;
       private String online_time_by_min;
    private String online_time;
    private String online_time_status;
    private Job job_degree_fk;
    private SpecializationModel specialization_fk;
    private CityModel city_fk;
    private List<Rates> rates_fk;
    private Favourite fav_fk;
    private String detection_time;

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

    public int getSpecialization_id() {
        return specialization_id;
    }

    public int getJob_degree_id() {
        return job_degree_id;
    }

    public int getCity_id() {
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

    public int getApp_cost() {
        return app_cost;
    }

    public String getTitle_job_degree() {
        return title_job_degree;
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

    public int getIs_confirmed() {
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

    public String getOnline_time_by_min() {
        return online_time_by_min;
    }

    public String getOnline_time() {
        return online_time;
    }

    public String getOnline_time_status() {
        return online_time_status;
    }

    public Job getJob_degree_fk() {
        return job_degree_fk;
    }

    public SpecializationModel getSpecialization_fk() {
        return specialization_fk;
    }

    public CityModel getCity_fk() {
        return city_fk;
    }

    public List<Rates> getRates_fk() {
        return rates_fk;
    }

    public Favourite getFav_fk() {
        return fav_fk;
    }

    public String getDetection_time() {
        return detection_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public class Job implements Serializable {
        private int id;
        private String title;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }

    public class Rates implements Serializable {
        private int id;
        private int user_id;
        private int doctor_id;
        private String comment;
        private double rate;
        private String created_at;
        private String updated_at;
        private User patient_fk;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getDoctor_id() {
            return doctor_id;
        }

        public String getComment() {
            return comment;
        }

        public double getRate() {
            return rate;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public User getPatient_fk() {
            return patient_fk;
        }

        public class User implements Serializable {
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
        }

    }

    public class Favourite implements Serializable {
        private int id;
        private int user_id;
        private int doctor_id;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getDoctor_id() {
            return doctor_id;
        }
    }

}
