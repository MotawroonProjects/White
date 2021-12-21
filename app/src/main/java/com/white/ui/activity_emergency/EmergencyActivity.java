package com.white.ui.activity_emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.white.R;
import com.white.adapters.EmergencyAdapter;
import com.white.databinding.ActivityEmergencyBinding;
import com.white.databinding.ActivityMedicalAdviceBinding;
import com.white.databinding.EmergencyDoctorRowBinding;
import com.white.language.Language;
import com.white.models.DoctorModel;
import com.white.models.SingleDoctorModel;
import com.white.mvp.activity_Emrgency_mvp.ActivityEmergencyPresenter;
import com.white.mvp.activity_Emrgency_mvp.EmergencyActivityView;
import com.white.ui.activity_doctor_details.DoctorDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class EmergencyActivity extends AppCompatActivity implements EmergencyActivityView {
    private ActivityEmergencyBinding binding;
    private ActivityEmergencyPresenter presenter;
    private String lang;
    private EmergencyAdapter adapter;
    private List<SingleDoctorModel> singleDoctorModelList;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emergency);
        initView();

    }

    private void initView() {
        singleDoctorModelList = new ArrayList<>();
        presenter = new ActivityEmergencyPresenter(this, this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        adapter = new EmergencyAdapter(singleDoctorModelList, this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(view -> presenter.backPress());
        presenter.getdoctors();

    }

    @Override
    public void onFinished() {
        finish();
    }

    @Override
    public void onProgressShow() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void ondoctorsucess(DoctorModel body) {
        singleDoctorModelList.clear();
        singleDoctorModelList.addAll(body.getData());
        adapter.notifyDataSetChanged();
        if(singleDoctorModelList.size()==0){
            binding.tvNoDoctor.setVisibility(View.VISIBLE);
        }
        else {
            binding.tvNoDoctor.setVisibility(View.GONE);
        }
    }
    public void setItemData(SingleDoctorModel doctorModel, EmergencyDoctorRowBinding binding, int adapterPosition) {
        Intent intent = new Intent(this, DoctorDetailsActivity.class);
        intent.putExtra("data",doctorModel);
        List<Pair<View, String>> pairs = new ArrayList<>();
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(binding.image, binding.image.getTransitionName()),
                Pair.create(binding.rateBar, binding.rateBar.getTransitionName()),
                Pair.create(binding.llAddress, binding.llAddress.getTransitionName()),
                Pair.create(binding.tvDistance, binding.tvDistance.getTransitionName())
        );
        startActivityForResult(intent, 1,options.toBundle());
    }

}