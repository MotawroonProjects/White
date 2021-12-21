package com.white.mvp.activity_Emrgency_mvp;

import android.content.Context;
import android.util.Log;

import com.white.R;
import com.white.models.DoctorModel;
import com.white.models.UserModel;
import com.white.preferences.Preferences;
import com.white.remote.Api;
import com.white.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEmergencyPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private EmergencyActivityView view;
    private Context context;

    public ActivityEmergencyPresenter(EmergencyActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void backPress() {

        view.onFinished();


    }


    public void getdoctors()
    {
        view.onProgressShow();

        Api.getService(Tags.base_url)
                .getdoctors()
                .enqueue(new Callback<DoctorModel>() {
                    @Override
                    public void onResponse(Call<DoctorModel> call, Response<DoctorModel> response) {
                        view.onProgressHide();
                        if (response.isSuccessful() && response.body() != null) {
                            view.ondoctorsucess(response.body());
                        } else {
                            view.onProgressHide();
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_code",response.code()+  response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<DoctorModel> call, Throwable t) {
                        try {
                            view.onProgressHide();
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
}
