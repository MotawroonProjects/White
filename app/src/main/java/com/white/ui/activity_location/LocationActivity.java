package com.white.ui.activity_location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import com.white.R;
import com.white.databinding.ActivityLocationBinding;
import com.white.databinding.ActivityLoginBinding;
import com.white.language.Language;
import com.white.models.UserModel;
import com.white.mvp.activity_location_presenter.ActivityLocationPresenter;
import com.white.mvp.activity_location_presenter.ActivityLocationView;
import com.white.preferences.Preferences;
import com.white.ui.activity_home.HomeActivity;
import com.white.ui.activity_login.LoginActivity;
import com.google.android.gms.maps.model.LatLng;

import io.paperdb.Paper;

public class LocationActivity extends AppCompatActivity implements ActivityLocationView {
    private ActivityLocationBinding binding;
    private ActivityLocationPresenter presenter;
    private UserModel userModel;
    private Preferences preference;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_location);
        initView();
    }

    private void initView() {
        binding.tv.setText(Html.fromHtml(getString(R.string.get_location)));
        preference = Preferences.getInstance();
        userModel = preference.getUserData(this);
        presenter = new ActivityLocationPresenter(this,this);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == ActivityLocationPresenter.loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.initGoogleApiClient();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1255 && resultCode == RESULT_OK) {
            presenter.startLocationUpdate();
        }
    }

    @Override
    public void onLocationChanged(LatLng latLng) {
        Intent intent;
        if (userModel!=null){
            intent = new Intent(this, HomeActivity.class);
        }else {
            intent = new Intent(this, LoginActivity.class);
        }
        intent.putExtra("lat",latLng.latitude);
        intent.putExtra("lng",latLng.longitude);
        startActivity(intent);
        finish();
    }
}