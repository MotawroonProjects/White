package com.white.ui.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import com.white.R;
import com.white.databinding.ActivityHomeBinding;
import com.white.language.Language;
import com.white.models.SingleDoctorModel;
import com.white.models.UserModel;
import com.white.mvp.activity_home_mvp.ActivityHomePresenter;
import com.white.mvp.activity_home_mvp.HomeActivityView;
import com.white.preferences.Preferences;
import com.white.ui.activity_doctor.DoctorActivity;
import com.white.ui.activity_doctor_details.DoctorDetailsActivity;
import com.white.ui.activity_login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.white.ui.notification_activity.NotificationActivity;

import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements HomeActivityView {
    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private ActivityHomePresenter presenter;
    private double lat=0.0,lng=0.0;
    private String id;
    private Preferences preference;
    private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
      //  startService(new Intent(getBaseContext(), LocalNotification.class));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);


        //wakeLock.release();
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent  = getIntent();
        preference = Preferences.getInstance();
        userModel = preference.getUserData(this);
        lat = intent.getDoubleExtra("lat",0.0);
        lng = intent.getDoubleExtra("lng",0.0);
        if (intent.getData() != null) {
            List<String> pathSegments = intent.getData().getPathSegments();
            id = pathSegments.get(pathSegments.size() - 1);

            Log.e("llflfllflf", id+ pathSegments.get(pathSegments.size() - 2));
            if( pathSegments.get(pathSegments.size() - 2).equals("share-app")){
                SingleDoctorModel singleDoctorModel=new SingleDoctorModel();
                singleDoctorModel.setId(Integer.parseInt(id));
                Intent intent1 = new Intent(HomeActivity.this, DoctorDetailsActivity.class);
                intent1.putExtra("data", singleDoctorModel);
                startActivity(intent1);
            }
//            Intent intent1 = new Intent(HomeActivity.this, RestuarnantActivity.class);
//            intent1.putExtra("restaurand_id", id);
//            startActivity(intent1);
        }
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        presenter = new ActivityHomePresenter(this, this, fragmentManager,lat,lng);
        binding.navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                presenter.manageFragments(item);
                return true;
            }
        });

        binding.flSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, DoctorActivity.class);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            startActivity(intent);
        });
        binding.flnotification.setOnClickListener(view -> {
            if (userModel!=null){
                Intent intent = new Intent(this, NotificationActivity.class);

                startActivity(intent);
            }else {
                onNavigateToLoginActivity();
            }

        });
    }


    public void refreshActivity(String lang) {
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            Paper.init(this);
            Paper.book().write("lang",lang);
            Language.updateResources(this,lang);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        },1500);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        presenter.backPress();
    }

    @Override
    public void onHomeFragmentSelected() {
        binding.navigationView.setSelectedItemId(R.id.home);
    }


    @Override
    public void onNavigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onFinished() {
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(presenter.fragment_appointment!=null){
            presenter.fragment_appointment.getdata();
        }
    }
}