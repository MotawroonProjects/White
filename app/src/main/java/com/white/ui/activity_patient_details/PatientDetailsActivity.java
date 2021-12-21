package com.white.ui.activity_patient_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.white.R;
import com.white.adapters.ChildDrugsAdapter;
import com.white.databinding.ActivityPatientDetailsBinding;
import com.white.language.Language;
import com.white.local_database.AlertModel;
import com.white.local_database.DataBaseActions;
import com.white.local_database.DatabaseInteraction;
import com.white.local_database.DeletedAlerts;
import com.white.models.DrugModel;
import com.white.models.UserModel;
import com.white.mvp.activity_patient_details_mvp.ActivityPatientDetailsPresenter;
import com.white.mvp.activity_patient_details_mvp.ActivityPatientDetailsView;
import com.white.notification.AlertManager;
import com.white.preferences.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class PatientDetailsActivity extends AppCompatActivity implements ActivityPatientDetailsView, DatabaseInteraction {
    private ActivityPatientDetailsBinding binding;
    private String lang;
    private DrugModel drugModel;
    private ActivityPatientDetailsPresenter presenter;
    private List<DrugModel.Drugs> drugModelList;
    private ChildDrugsAdapter adapter;
    private Date date;
    private Preferences preferences;
    private UserModel userModel;
    private DataBaseActions dataBaseActions;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_details);
//        String alarm = Context.ALARM_SERVICE;
//        AlarmManager am = (AlarmManager) getSystemService(alarm);
//
//        Intent intent = new Intent("REFRESH_THIS");
//        PendingIntent pi = PendingIntent.getBroadcast(this, 123456789, intent, 0);
//
//        int type = AlarmManager.RTC_WAKEUP;
//        long interval = 1000 * 50;
//
//        am.setInexactRepeating(type, System.currentTimeMillis(), interval, pi);

        getDataFromIntent();
        initView();

    }
//    private boolean isMyServiceRunning() {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (LocalNotification.class.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("data") != null) {
            drugModel = (DrugModel) intent.getSerializableExtra("data");
        }

    }

    private void initView() {
        dataBaseActions = new DataBaseActions(this);
        dataBaseActions.setInteraction(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.view.setVisibility(View.GONE);
        drugModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(drugModel.getDoctor_fk());
        binding.setTitle(drugModel.getDoctor_fk().getSpecialization_fk().getTitle());
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChildDrugsAdapter(drugModelList, this);
        binding.recView.setAdapter(adapter);
        presenter = new ActivityPatientDetailsPresenter(this, this);
        drugModelList.addAll(drugModel.getReservations_drugs());
        adapter.notifyDataSetChanged();
        if (drugModelList.size() == 0) {
            binding.tvNoData.setVisibility(View.VISIBLE);
            ;
            binding.btstart.setVisibility(View.GONE);
        }
        binding.btstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
                // DrugTimeModel drugTimeModel = preferences.getTimeData(PatientDetailsActivity.this);
                List<AlertModel> dataList = new ArrayList<>();
//                if (drugTimeModel == null) {
//                    drugTimeModel = new DrugTimeModel();
//                    dataList = new ArrayList<>();
//                } else {
//                    dataList = drugTimeModel.getData();
//                }
                long time = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                try {
                    date = sdf.parse(sdf.format(time));
                    // Log.e(";s;;s",date.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < drugModelList.size(); i++) {
                    calendar.setTime(date);
                    AlertModel data = new AlertModel();
                    data.setTitle(getString(R.string.your_dose) + drugModelList.get(i).getDrag_name() +" "+ getString(R.string.Now));
                    data.setTakenum(drugModelList.get(i).getTake_num());
                    calendar.add(Calendar.MINUTE, 24 / data.getTakenum());

                    data.setDate(sdf.format(calendar.getTime()));

                    dataList.add(data);

                    dataBaseActions.insert(data);
                    AlertManager manager = new AlertManager(PatientDetailsActivity.this);
                    manager.startNewAlarm(data);
                }


                //  drugTimeModel.setData(dataList);
                //preferences.create_update_time(PatientDetailsActivity.this, drugTimeModel);
            }
        });
        binding.imageBack.setOnClickListener(view -> finish());

        binding.progBar.setVisibility(View.GONE);
    }


    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        binding.view.setVisibility(View.VISIBLE);
        binding.progBar.setVisibility(View.VISIBLE);
        drugModelList.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        // binding.btstart.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.progBar.setVisibility(View.GONE);

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