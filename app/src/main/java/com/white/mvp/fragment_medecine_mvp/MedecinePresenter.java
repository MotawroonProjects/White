package com.white.mvp.fragment_medecine_mvp;

import android.content.Context;
import android.util.Log;

import com.white.R;
import com.white.models.DrugDataModel;
import com.white.models.UserModel;
import com.white.remote.Api;
import com.white.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedecinePresenter {
    private MedecineFragmentView view;
    private Context context;
    private Call<DrugDataModel> call;

    public MedecinePresenter(MedecineFragmentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getDrugs( UserModel userModel) {
        if (userModel == null) {
            return;
        }

        view.showProgressBar();
        if (call != null) {
            call.cancel();

        }
        call = Api.getService(Tags.base_url).
                getDrugs("Bearer " + userModel.getData().getToken(),"off", userModel.getData().getId());

        call.enqueue(new Callback<DrugDataModel>() {
            @Override
            public void onResponse(Call<DrugDataModel> call, Response<DrugDataModel> response) {

                if (response.isSuccessful()) {
                    view.hideProgressBar();
                    if (response.body() != null) {
                        view.onSuccess(response.body().getData());

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
            public void onFailure(Call<DrugDataModel> call, Throwable t) {
                try {
                    view.hideProgressBar();
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage() + "__");

                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            view.onFailed(context.getString(R.string.something));

                        } else if (t.getMessage().toLowerCase().equals("Canceled".toLowerCase()) || t.getMessage().toLowerCase().contains("Socket closed".toLowerCase())) {

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