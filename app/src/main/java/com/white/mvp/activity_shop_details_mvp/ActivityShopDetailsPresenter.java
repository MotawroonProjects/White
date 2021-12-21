package com.white.mvp.activity_shop_details_mvp;

import android.content.Context;
import android.util.Log;

import com.white.R;
import com.white.models.PlaceDetailsModel;
import com.white.remote.Api;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityShopDetailsPresenter {

    private Context context;
    private ActivityShopDetailsView view;

    public ActivityShopDetailsPresenter(Context context, ActivityShopDetailsView view) {
        this.context = context;
        this.view = view;
    }

    public void getPlaceDetails(String place_id, String lang)
    {
        view.onProgressShow();
        String fields = "opening_hours,photos,reviews";

        Api.getService("https://maps.googleapis.com/maps/api/")
                .getPlaceDetails(place_id, fields, lang, context.getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceDetailsModel>() {
                    @Override
                    public void onResponse(Call<PlaceDetailsModel> call, Response<PlaceDetailsModel> response) {
                        view.onProgressHide();
                        if (response.isSuccessful() && response.body() != null) {
                            view.onSuccess(response.body());
                        } else {
                            view.onProgressHide();
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceDetailsModel> call, Throwable t) {
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
