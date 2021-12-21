package com.white.mvp.fragment_home_mvp;

import android.content.Context;
import android.util.Log;

import com.white.R;
import com.white.models.Slider_Model;
import com.white.remote.Api;
import com.white.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHomePresenter {
    private HomeFragmentView view;
    private Context context;

    public FragmentHomePresenter(HomeFragmentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getSlider() {
        view.onProgressSliderShow();

        Api.getService(Tags.base_url).get_slider().enqueue(new Callback<Slider_Model>() {
            @Override
            public void onResponse(Call<Slider_Model> call, Response<Slider_Model> response) {
                view.onProgressSliderHide();

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData() != null) {
                        view.onSliderSuccess(response.body().getData());

                    }

                } else {
                    try {
                        view.onFailed(context.getString(R.string.failed));
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Slider_Model> call, Throwable t) {
                try {
                    view.onProgressSliderHide();

                    Log.e("Error", t.getMessage());

                } catch (Exception e) {

                }

            }
        });

    }}