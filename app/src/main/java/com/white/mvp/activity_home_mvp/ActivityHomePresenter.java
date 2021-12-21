package com.white.mvp.activity_home_mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.white.R;
import com.white.models.UserModel;
import com.white.preferences.Preferences;
import com.white.remote.Api;
import com.white.share.Common;
import com.white.tags.Tags;
import com.white.ui.activity_home.fragments.Fragment_Appointment;
import com.white.ui.activity_home.fragments.Fragment_Home;
import com.white.ui.activity_home.fragments.Fragment_Medicine;
import com.white.ui.activity_home.fragments.Fragment_More;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHomePresenter {
    private Context context;
    private FragmentManager fragmentManager;
    private HomeActivityView view;
    private Fragment_Home fragment_home;
    public Fragment_Appointment fragment_appointment;
    private Fragment_Medicine fragment_medicine;
    private Fragment_More fragment_more;
    private Preferences preference;
    private UserModel userModel;
    private double lat=0.0,lng=0.0;

    public ActivityHomePresenter(Context context,HomeActivityView view, FragmentManager fragmentManager,double lat,double lng) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.view = view;
        preference = Preferences.getInstance();
        userModel = preference.getUserData(context);
        this.lat = lat;
        this.lng = lng;

        if (userModel!=null){
            updateTokenFireBase();

        }

        displayFragmentHome();
    }

    @SuppressLint("NonConstantResourceId")
    public void manageFragments(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.appointment :
                displayFragmentAppointment();
                break;
            case R.id.medicine :
                displayFragmentMedicine();
                break;
            case R.id.more :
                displayFragmentMore();
                break;
            default:
                displayFragmentHome();
                break;
        }
    }
    private void displayFragmentHome(){
        if (fragment_home==null){
            fragment_home = Fragment_Home.newInstance(lat,lng);
        }

        if (fragment_appointment!=null&&fragment_appointment.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_appointment).commit();
        }

        if (fragment_medicine!=null&&fragment_medicine.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_medicine).commit();
        }


        if (fragment_more!=null&&fragment_more.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_home.isAdded()){
            fragmentManager.beginTransaction().show(fragment_home).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment_home,"fragment_home").commit();
        }
    }

    private void displayFragmentAppointment(){
        if(userModel!=null){
        if (fragment_appointment==null){
            fragment_appointment = Fragment_Appointment.newInstance();
        }

        if (fragment_home!=null&&fragment_home.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_medicine!=null&&fragment_medicine.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_medicine).commit();
        }

        if (fragment_more!=null&&fragment_more.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_appointment.isAdded()){
            fragmentManager.beginTransaction().show(fragment_appointment).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment_appointment,"fragment_appointment").commit();
        }}
        else {
            Common.CreateDialogAlert(context, context.getResources().getString(R.string.please_sign_in_or_sign_up));
        }
    }


    private void displayFragmentMedicine(){
        if (fragment_medicine==null){
            fragment_medicine = Fragment_Medicine.newInstance();
        }

        if (fragment_home!=null&&fragment_home.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }


        if (fragment_appointment!=null&&fragment_appointment.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_appointment).commit();
        }
        if (fragment_more!=null&&fragment_more.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_medicine.isAdded()){
            fragmentManager.beginTransaction().show(fragment_medicine).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment_medicine,"fragment_medicine").commit();
        }
    }

    private void displayFragmentMore(){
        if (fragment_more==null){
            fragment_more = Fragment_More.newInstance();
        }

        if (fragment_home!=null&&fragment_home.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }


        if (fragment_appointment!=null&&fragment_appointment.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_appointment).commit();
        }
        if (fragment_medicine!=null&&fragment_medicine.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_medicine).commit();
        }
        if (fragment_more.isAdded()){
            fragmentManager.beginTransaction().show(fragment_more).commit();
        }else {
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment_more,"fragment_more").commit();
        }
    }

    public void backPress(){
        if (fragment_home!=null&&fragment_home.isAdded()&&fragment_home.isVisible()){
            if (userModel==null){
                view.onNavigateToLoginActivity();
            }else {
                view.onFinished();
            }
        }else {
            displayFragmentHome();
            view.onHomeFragmentSelected();
        }
    }
    private void updateTokenFireBase() {
        Log.e("ssss", userModel.getData().getPhone()+" "+userModel.getData().getId());
        if (userModel != null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        Log.e("token", token);
                        Api.getService(Tags.base_url)
                                .updateFirebaseToken(userModel.getData().getToken(), userModel.getData().getId(), token, "android")
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            userModel.getData().setFireBaseToken(token);
                                            preference.create_update_userdata(context, userModel);

                                        } else {
                                            try {

                                                Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        try {

                                            if (t.getMessage() != null) {
                                                Log.e("errorToken2", t.getMessage());
                                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                    Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } catch (Exception e) {
                                        }
                                    }
                                });
                    }
                }
            });

        }
    }

}
