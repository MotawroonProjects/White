package com.white.mvp.fragment_apointment_mvp;

import android.content.Context;
import android.util.Log;

import com.white.R;
import com.white.models.ApointmentModel;
import com.white.models.UserModel;
import com.white.remote.Api;
import com.white.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApointmentPresenter {
    private ApointmentFragmentView view;
    private Context context;

    public ApointmentPresenter(ApointmentFragmentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getApointment(UserModel userModel) {

        Log.e("mmmmmmm",userModel.getData().getId()+"");
        view.showProgressBar();
        Api.getService(Tags.base_url).getMyApointment("off",1,1,userModel.getData().getId(),"all")
                .enqueue(new Callback<ApointmentModel>() {
                    @Override
                    public void onResponse(Call<ApointmentModel> call, Response<ApointmentModel> response) {

                        if (response.isSuccessful()) {
                            view.hideProgressBar();
                            if (response.body() != null) {
                                view.onSuccess(response.body());

                            }

                        } else {

                            try {
                                view.onFailed(context.getString(R.string.failed));
                                Log.e("error", response.code() + "_" + response.errorBody().string());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApointmentModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onFailed(context.getString(R.string.something));

                                } else {
                                    view.onFailed(context.getString(R.string.failed));

                                }
                            }


                        } catch (Exception e) {
                        }

                    }
                });
    }
}