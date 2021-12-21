package com.white.ui.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.white.R;
import com.white.adapters.SliderAdapter;
import com.white.databinding.FragmentHomeBinding;
import com.white.models.Slider_Model;
import com.white.mvp.fragment_home_mvp.FragmentHomePresenter;
import com.white.mvp.fragment_home_mvp.HomeFragmentView;
import com.white.ui.activity_doctor.DoctorActivity;
import com.white.ui.activity_emergency.EmergencyActivity;
import com.white.ui.activity_google_nearby_places.GoogleNearybyPlacesActivity;
import com.white.ui.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import io.paperdb.Paper;

public class Fragment_Home extends Fragment implements HomeFragmentView {
    private FragmentHomeBinding binding;
    private double lat = 0.0, lng = 0.0;
    private HomeActivity activity;
    private SliderAdapter sliderAdapter;
    private FragmentHomePresenter fragmentHomePresenter;
    private List<Slider_Model.Data> sliDataList;
    private Timer timer;
    private TimerTask timerTask;
    private String lang;

    public static Fragment_Home newInstance(double lat, double lng) {
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lng", lng);
        Fragment_Home fragment_home = new Fragment_Home();
        fragment_home.setArguments(bundle);
        return fragment_home;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        sliDataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang");

        Bundle bundle = getArguments();
        if (bundle != null) {
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
        }

        binding.setLang(lang);
        binding.tab.setupWithViewPager(binding.pager);

        fragmentHomePresenter = new FragmentHomePresenter(this, activity);

        sliderAdapter = new SliderAdapter(sliDataList, activity);

        binding.cardViewHospitals.setOnClickListener(view -> {
            Intent intent = new Intent(activity, GoogleNearybyPlacesActivity.class);
            intent.putExtra("query", "hospital");
            intent.putExtra("ar_title", "أقرب مستشفى");
            intent.putExtra("en_title", "Hospitals");
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            startActivity(intent);
        });


        binding.cardViewEmergency.setOnClickListener(view -> {
            Intent intent = new Intent(activity, EmergencyActivity.class);
            startActivity(intent);
        });
        binding.cardViewNursing.setOnClickListener(view -> {
            Intent intent = new Intent(activity, EmergencyActivity.class);
            startActivity(intent);
        });
        binding.cardViewPharmacy.setOnClickListener(view -> {
            Intent intent = new Intent(activity, DoctorActivity.class);
            startActivity(intent);
        });
        binding.cardViewAdvice.setOnClickListener(view -> {
            Intent intent = new Intent(activity, DoctorActivity.class);
            startActivity(intent);
        });
        binding.cardViewDoctor.setOnClickListener(view -> {
            Intent intent = new Intent(activity, DoctorActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            startActivity(intent);
        });
        fragmentHomePresenter.getSlider();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProgressSliderShow() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressSliderHide() {
        binding.progBar.setVisibility(View.GONE);
    }

    @Override
    public void onSliderSuccess(List<Slider_Model.Data> sliderModelList) {


        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setAdapter(sliderAdapter);
        sliDataList.addAll(sliderModelList);
        sliderAdapter.notifyDataSetChanged();
        if (sliDataList.size() > 1) {
            timer = new Timer();
            timerTask = new MyTask();
            timer.scheduleAtFixedRate(timerTask, 6000, 6000);
        }

        binding.flNoAds.setVisibility(View.GONE);

        if (sliDataList.size() == 0) {
            binding.flPager.setVisibility(View.GONE);
            binding.flNoAds.setVisibility(View.VISIBLE);
        } else {
            binding.flPager.setVisibility(View.VISIBLE);
            binding.flNoAds.setVisibility(View.GONE);
        }
        sliderAdapter.notifyDataSetChanged();
    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }
    }
}
