package com.white.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;


import com.white.local_database.AlertModel;
import com.white.local_database.DataBaseActions;
import com.white.local_database.DatabaseInteraction;
import com.white.local_database.DeletedAlerts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AlertManager implements DatabaseInteraction {

    private Context context;
    private DataBaseActions dataBaseActions;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);

    public AlertManager(Context context) {
        this.context = context;
    }

    private void startAlarm(String time)
    {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"wake_lock");
        wakeLock.acquire();

        String t = time;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context.getApplicationContext(), AlarmBroadcast.class);
        try {
            Log.e("dlddkk",t+" "+sdf.parse(time).getTime());
        } catch (ParseException e) {
           Log.e("sllsls",e.toString());
        }
        intent.putExtra("time",t);
        int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, sdf.parse(time).getTime(), pendingIntent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                alarmManager.set(AlarmManager.RTC_WAKEUP, sdf.parse(time).getTime(), pendingIntent);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        wakeLock.release();
    }

    public void reStartAlarm()
    {
        dataBaseActions = new DataBaseActions(context);
        dataBaseActions.setInteraction(this);
        dataBaseActions.displayAllAlert();
//        dataBaseActions.displayAlertByState(0);
    }

    public void startNewAlarm(AlertModel alertModel)
    {

//        Calendar calendarTime = Calendar.getInstance();
//        calendarTime.setTimeInMillis(System.currentTimeMillis());
//        calendarTime.clear();
//
//        Calendar calendarDate = Calendar.getInstance();
//        calendarDate.setTimeInMillis(System.currentTimeMillis());
//        calendarDate.clear();
//        //calendarTime.setTimeInMillis(alertModel.getTime());
//        try {
//            calendarTime.setTimeInMillis(sdf.parse(alertModel.getDate()).getTime());
//
//            calendarDate.setTimeInMillis(sdf.parse(alertModel.getDate()).getTime());
//        } catch (ParseException e) {
//          //  e.printStackTrace();
//        }
//
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.clear();
//        calendar.set(Calendar.DAY_OF_MONTH,calendarDate.get(Calendar.DAY_OF_MONTH));
//        calendar.set(Calendar.MONTH,calendarDate.get(Calendar.MONTH));
//        calendar.set(Calendar.YEAR,calendarDate.get(Calendar.YEAR));
//        calendar.set(Calendar.HOUR_OF_DAY,calendarTime.get(Calendar.HOUR_OF_DAY));
//        calendar.set(Calendar.MINUTE,calendarTime.get(Calendar.MINUTE));


        startAlarm(alertModel.getDate());
    }

    @Override
    public void insertedSuccess() {

    }

    @Override
    public void displayData(List<AlertModel> alertModelList) {

    }

    @Override
    public void displayByTime(AlertModel alertModel) {

    }

    @Override
    public void displayAlertsByState(List<AlertModel> alertModelList) {

        try {
//            Log.e("size_state",alertModelList.size()+"_");
//            Log.e("data1",alertModelList.get(0).getDetails()+"_"+alertModelList.get(0).getAlert_state());
//            Log.e("data2",alertModelList.get(1).getDetails()+"_"+alertModelList.get(1).getAlert_state());

        }catch (Exception e){}

        if (alertModelList.size()>0)
        {


            for (AlertModel alertModel:alertModelList)
            {
                Calendar calendarTime = Calendar.getInstance();
                calendarTime.setTimeInMillis(System.currentTimeMillis());
                calendarTime.clear();

                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTimeInMillis(System.currentTimeMillis());
                calendarDate.clear();
               // calendarTime.setTimeInMillis(alertModel.getTime());
                try {
                    calendarTime.setTimeInMillis(sdf.parse(alertModel.getDate()).getTime());
                    calendarDate.setTimeInMillis(sdf.parse(alertModel.getDate()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.clear();
                calendar.set(Calendar.DAY_OF_MONTH,calendarDate.get(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.MONTH,calendarDate.get(Calendar.MONTH));
                calendar.set(Calendar.YEAR,calendarDate.get(Calendar.YEAR));
                calendar.set(Calendar.HOUR_OF_DAY,calendarTime.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE,calendarTime.get(Calendar.MINUTE));


                startAlarm(alertModel.getDate());


            }
        }

    }

    @Override
    public void displayAlertsByOnline(List<AlertModel> alertModelList) {

    }

    @Override
    public void displayAllAlerts(List<AlertModel> alertModelList) {

    }

    @Override
    public void displayAllDeletedAlerts(List<DeletedAlerts> deletedAlertsList) {

    }


    @Override
    public void onDeleteSuccess() {

    }
}
