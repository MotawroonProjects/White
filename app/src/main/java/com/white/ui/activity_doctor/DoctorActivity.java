package com.white.ui.activity_doctor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.white.R;
import com.white.adapters.CityAdapter;
import com.white.adapters.DoctorsAdapter;
import com.white.adapters.FilterAdapter;
import com.white.adapters.SpicialAdapter;
import com.white.databinding.ActivityDoctorBinding;
import com.white.databinding.DoctorRowBinding;
import com.white.language.Language;
import com.white.models.AllCityModel;
import com.white.models.AllSpiclixationModel;
import com.white.models.CityModel;
import com.white.models.DoctorModel;
import com.white.models.FilterModel;
import com.white.models.SingleDoctorModel;
import com.white.models.SpecializationModel;
import com.white.mvp.activity_doctors_mvp.ActivityDoctorsPresenter;
import com.white.mvp.activity_doctors_mvp.DoctorsActivityView;
import com.white.share.Common;
import com.white.ui.activity_doctor_details.DoctorDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class DoctorActivity extends AppCompatActivity implements DoctorsActivityView {
    private ActivityDoctorBinding binding;
    private ActivityDoctorsPresenter presenter;
    private String lang;
    private double lat = 0.0, lng = 0.0;
    private float distance = 0f;
    private String city_id = "";
    private String filter = "";
    private String specialization_id = "all";
    private DoctorsAdapter adapter;
    private FilterAdapter filterAdapter;
    private List<FilterModel> filterModelList;
    private List<SpecializationModel> specializationModels;
    private SpicialAdapter spicialAdapter;
    private List<CityModel> cityModels;
    private List<SingleDoctorModel> singleDoctorModelList;
    private CityAdapter cityAdapter;
    private String query, near = "on", price = "all", rates = "all", available = "all";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0.0);
        lng = intent.getDoubleExtra("lng", 0.0);

    }

    private void initView() {
        presenter = new ActivityDoctorsPresenter(this, this);
        singleDoctorModelList = new ArrayList<>();
        filterModelList = new ArrayList<>();
        specializationModels = new ArrayList<>();
        cityModels = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        adapter = new DoctorsAdapter(singleDoctorModelList, this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        //binding.progBar.setVisibility(View.GONE);
        presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, price, rates, available, 2);
        binding.llBack.setOnClickListener(view -> {
            presenter.backPress();
        });
        binding.llSpecialization.setOnClickListener(view -> openSheet(1));
        binding.llNearby.setOnClickListener(view -> openSheet(2));
        binding.llCity.setOnClickListener(view -> openSheet(3));

        binding.imageCloseSpecialization.setOnClickListener(view -> closeSheet(1));
        binding.imageCloseNearby.setOnClickListener(view -> closeSheet(2));
        binding.imageCloseCity.setOnClickListener(view -> closeSheet(3));


        filterAdapter = new FilterAdapter(filterModelList, this);
        binding.recViewFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recViewFilter.setAdapter(filterAdapter);
        spicialAdapter = new SpicialAdapter(specializationModels, this);
        binding.recViewSpecialization.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewSpecialization.setAdapter(spicialAdapter);
        cityAdapter = new CityAdapter(cityModels, this);
        binding.recViewCity.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewCity.setAdapter(cityAdapter);

        binding.editQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                query = binding.editQuery.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(DoctorActivity.this, binding.editQuery);
                    presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, price, rates, available, 2);
                    return false;
                } else {
                    query = "";
                }
                presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, price, rates, available, 2);

            }
            return false;
        });
        binding.btnFilter.setOnClickListener(view -> {
            String title = "";
            if (binding.rbHighPrice.isChecked()) {
                title = getString(R.string.high_price);
                price = "DESC";
                rates = "all";
                available = "all";
            } else if (binding.rbLowPrice.isChecked()) {
                title = getString(R.string.low_price);
                price = "ASC ";
                rates = "all";
                available = "all";

            } else if (binding.rbRate.isChecked()) {
                title = getString(R.string.rate);
                price = "all";
                rates = "DESC ";
                available = "all";


            } else if (binding.rbTime.isChecked()) {
                title = getString(R.string.less_time);
            } else if (binding.rbavailable.isChecked()) {
                title = getString(R.string.available);
                price = "all";
                rates = "ASC ";
                available = "on";
            } else if (binding.rbnotavailable.isChecked()) {
                title = getString(R.string.rate);
                price = "all";
                rates = "ASC ";
                available = "off";
            }
            int pos = getItemPos("filter");
            if (title != null && !title.isEmpty()) {
                if (pos == -1) {
                    FilterModel filterModel = new FilterModel(title, "filter");
                    filterModelList.add(filterModel);
                    filterAdapter.notifyDataSetChanged();
                } else {
                    FilterModel filterModel = filterModelList.get(pos);
                    filterModel.setTitle(title);
                    filterModelList.set(pos, filterModel);
                    filterAdapter.notifyItemChanged(pos);
                }
                binding.llFilter.setVisibility(View.VISIBLE);

            }
            closeSheet(2);
            presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, price, rates, available, 2);

        });

        presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, price, rates, available, 2);


    }


    private void openSheet(int type) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        if (type == 1) {
            binding.flSpecializationSheet.clearAnimation();
            binding.flSpecializationSheet.startAnimation(animation);

            presenter.getSpecilization(type);

        } else if (type == 2) {
            binding.flFilterSheet.clearAnimation();
            binding.flFilterSheet.startAnimation(animation);


        } else if (type == 3) {
            binding.flCitySheet.clearAnimation();
            binding.flCitySheet.startAnimation(animation);
            presenter.getcities(type);


        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (type == 1) {
                    binding.flSpecializationSheet.setVisibility(View.VISIBLE);


                } else if (type == 2) {
                    binding.flFilterSheet.setVisibility(View.VISIBLE);


                } else if (type == 3) {
                    binding.flCitySheet.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void closeSheet(int type) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        if (type == 1) {
            binding.flSpecializationSheet.clearAnimation();
            binding.flSpecializationSheet.startAnimation(animation);

        } else if (type == 2) {
            binding.flFilterSheet.clearAnimation();
            binding.flFilterSheet.startAnimation(animation);


        } else if (type == 3) {
            binding.flCitySheet.clearAnimation();
            binding.flCitySheet.startAnimation(animation);


        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (type == 1) {
                    binding.flSpecializationSheet.setVisibility(View.GONE);


                } else if (type == 2) {
                    binding.flFilterSheet.setVisibility(View.GONE);


                } else if (type == 3) {
                    binding.flCitySheet.setVisibility(View.GONE);


                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    private int getItemPos(String type) {
        int pos = -1;
        for (int index = 0; index < filterModelList.size(); index++) {
            FilterModel filterModel = filterModelList.get(index);
            if (filterModel.getType().equals(type)) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    public void deleteFilter(FilterModel model, int adapterPosition) {
        filterModelList.remove(adapterPosition);
        filterAdapter.notifyItemRemoved(adapterPosition);
        if (model.getType().equals("filter")) {
            binding.rbHighPrice.setChecked(false);
            binding.rbLowPrice.setChecked(false);
            binding.rbRate.setChecked(false);
            binding.rbTime.setChecked(false);
            filter = "";
            price = "";
            rates = "";
        } else if (model.getType().equals("specialization")) {
            specialization_id = "all";
        } else if (model.getType().equals("city")) {
            city_id = "all";
        }

        if (filterModelList.size() > 0) {
            binding.llFilter.setVisibility(View.VISIBLE);
        } else {
            binding.llFilter.setVisibility(View.GONE);

        }
        presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, price, rates, available, 2);

    }

    public void setItemData(SingleDoctorModel doctorModel, DoctorRowBinding binding, int adapterPosition) {
        Intent intent = new Intent(this, DoctorDetailsActivity.class);
        intent.putExtra("data", doctorModel);
        List<Pair<View, String>> pairs = new ArrayList<>();
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(binding.image, binding.image.getTransitionName()),
                Pair.create(binding.rateBar, binding.rateBar.getTransitionName()),
                Pair.create(binding.llAddress, binding.llAddress.getTransitionName()),
                Pair.create(binding.tvDistance, binding.tvDistance.getTransitionName())
        );
        startActivityForResult(intent, 1, options.toBundle());
    }

    @Override
    public void onFinished() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);

            finish();
        }
    }

    @Override
    public void onProgressShow(int type) {
        if (type == 1) {
            binding.progBarSpecialization.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            binding.progBarCity.setVisibility(View.VISIBLE);

        } else if (type == 2) {
            binding.progBar.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onProgressHide(int type) {
        if (type == 1) {
            binding.progBarSpecialization.setVisibility(View.GONE);
        } else if (type == 3) {
            binding.progBarCity.setVisibility(View.GONE);

        } else if (type == 2) {
            binding.progBar.setVisibility(View.GONE);

        }

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(DoctorActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(AllSpiclixationModel allSpiclixationModel) {
        specializationModels.clear();
        specializationModels.addAll(allSpiclixationModel.getData());
        spicialAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSuccesscitie(AllCityModel allCityModel) {
        cityModels.clear();
        cityModels.addAll(allCityModel.getData());
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void ondoctorsucess(DoctorModel body) {
        singleDoctorModelList.clear();
        singleDoctorModelList.addAll(body.getData());
        adapter.notifyDataSetChanged();
        if (singleDoctorModelList.size() == 0) {
            binding.tvNoData.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoData.setVisibility(View.GONE);
        }
    }

    public void setspicialization(int id) {
//        specializationModels.clear();
//        spicialAdapter.notifyDataSetChanged();
        int pos = getItemPos("specialization");
        String title = getString(R.string.specialization);

        if (pos == -1) {
            FilterModel filterModel = new FilterModel(title, "specialization");
            filterModelList.add(filterModel);
            filterAdapter.notifyDataSetChanged();
        } else {
            FilterModel filterModel = filterModelList.get(pos);
            filterModel.setTitle(title);
            filterModelList.set(pos, filterModel);
            filterAdapter.notifyItemChanged(pos);
        }
        binding.llFilter.setVisibility(View.VISIBLE);
        specialization_id = id + "";

        closeSheet(1);
        presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, price, rates, available, 2);

    }

    public void setcity(int id) {
//        specializationModels.clear();
//        spicialAdapter.notifyDataSetChanged();
        int pos = getItemPos("city");
        String title = getString(R.string.city);

        if (pos == -1) {
            FilterModel filterModel = new FilterModel(title, "city");
            filterModelList.add(filterModel);
            filterAdapter.notifyDataSetChanged();
        } else {
            FilterModel filterModel = filterModelList.get(pos);
            filterModel.setTitle(title);
            filterModelList.set(pos, filterModel);
            filterAdapter.notifyItemChanged(pos);
        }
        binding.llFilter.setVisibility(View.VISIBLE);
        city_id = id + "";

        closeSheet(3);
        presenter.getdoctors(query, specialization_id, city_id, lat + "", lng + "", near, price, rates, available, 2);

    }
}