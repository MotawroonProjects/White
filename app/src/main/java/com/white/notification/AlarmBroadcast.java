package com.white.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;


import com.white.local_database.AlertModel;
import com.white.local_database.DataBaseActions;
import com.white.local_database.DatabaseInteraction;
import com.white.local_database.DeletedAlerts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.VIBRATOR_SERVICE;

public class AlarmBroadcast extends BroadcastReceiver implements DatabaseInteraction {
    private DataBaseActions dataBaseActions;
    private Context context;
    private LocalNotification localNotification;
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        dataBaseActions = new DataBaseActions(context.getApplicationContext());
        dataBaseActions.setInteraction(this);
        String time = intent.getStringExtra("time");
        Log.e("llll",time);
        dataBaseActions.displayAlertByTime(time);



    }

    @Override
    public void insertedSuccess() {

    }

    @Override
    public void displayData(List<AlertModel> alertModelList) {

    }

    @Override
    public void displayByTime(AlertModel alertModel)
    {

        if (alertModel!=null)
        {
            Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
            }else
            {
                vibrator.vibrate(3000);

            }

                localNotification = new LocalNotification(context,alertModel.getTitle());
                localNotification.manageNotification();




           // alertModel.setAlert_state(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(sdf.format(alertModel.getDate())));
            calendar.add(Calendar.MINUTE, 24/alertModel.getTakenum());
            alertModel.setDate(sdf.format(calendar.getTime()));
            dataBaseActions.update(alertModel);


        }




    }

    @Override
    public void displayAlertsByState(List<AlertModel> alertModelList) {

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
