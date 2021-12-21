package com.white.local_database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlertDao {

    @Insert
    long insert(AlertModel alertModel);

    @Delete
    void delete(AlertModel alertModel);

    @Update
    void update(AlertModel alertModel);


    @Query("SELECT * FROM alerts_table WHERE date LIKE :time LIMIT 1")
    AlertModel getAlertByTime(String time);



    @Insert
    void insertAllData(List<AlertModel> alertModel);

    @Query("SELECT * FROM alerts_table")
    List<AlertModel> getAllAlertsData();

}
