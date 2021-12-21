package com.white.local_database;



import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.esotericsoftware.kryo.NotNull;

import java.io.Serializable;

@Entity(tableName = "alerts_table")
public class AlertModel implements Serializable {
    @NotNull
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String date;
    private int takenum;


    public AlertModel() {
    }

    public AlertModel(String title, String date, int takenum) {
        this.title = title;
        this.date = date;
        this.takenum = takenum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
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
