package com.white.mvp.activity_doctors_mvp;

import android.content.Context;
import android.util.Log;

import com.white.R;
import com.white.models.AllCityModel;
import com.white.models.AllSpiclixationModel;
import com.white.models.DoctorModel;
import com.white.models.UserModel;
import com.white.preferences.Preferences;
import com.white.remote.Api;
import com.white.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDoctorsPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private DoctorsActivityView view;
    private Context context;

    public ActivityDoctorsPresenter(DoctorsActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void backPress() {

        view.onFinished();


    }

    public void getSpecilization(int type)
    {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
        view.onProgressShow(type);

        Api.getService(Tags.base_url)
                .getspicailest()
                .enqueue(new Callback<AllSpiclixationModel>() {
                    @Override
                    public void onResponse(Call<AllSpiclixationModel> call, Response<AllSpiclixationModel> response) {
                        view.onProgressHide(type);

                        if (response.isSuccessful() && response.body() != null) {
                            view.onSuccess(response.body());
                        } else {
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess",response.code()+ response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllSpiclixationModel> call, Throwable t) {
                        try {
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
    public void getcities(int type)
    {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
        view.onProgressShow(type);

        Api.getService(Tags.base_url)
                .getcities()
                .enqueue(new Callback<AllCityModel>() {
                    @Override
                    public void onResponse(Call<AllCityModel> call, Response<AllCityModel> response) {
                        view.onProgressHide(type);

                        if (response.isSuccessful() && response.body() != null) {
                            view.onSuccesscitie(response.body());
                        } else {
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess",response.code()+ response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllCityModel> call, Throwable t) {
                        try {
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void getdoctors(String name,String specialization_id,String city_id,String latitude,String longitude,String near,String price,String rates,String available,int type)
    {
        Log.e("dldlldld",latitude+" "+longitude+" "+price);
        view.onProgressShow(type);

        Api.getService(Tags.base_url)
                .getdoctors(name,specialization_id,city_id,latitude,longitude,price,rates,available)
                .enqueue(new Callback<DoctorModel>() {
                    @Override
                    public void onResponse(Call<DoctorModel> call, Response<DoctorModel> response) {
                        view.onProgressHide(type);
                        if (response.isSuccessful() && response.body() != null) {
                            view.ondoctorsucess(response.body());
                        } else {
                            view.onProgressHide(type);
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
                            view.onProgressHide(type);
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
}
