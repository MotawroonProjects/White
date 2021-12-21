package com.white.models;

import java.io.Serializable;

public class RoomIdModel implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public class  Data implements Serializable {
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
