package com.white.mvp.activity_sign_up_mvp;

import com.white.models.DiseaseModel;
import com.white.models.UserModel;

import java.util.List;

public interface ActivitySignUpView {
    void onGenderSuccess(List<String>genderList);
    void onBloodSuccess(List<String>bloodList);
    void onDiseasesSuccess(List<DiseaseModel> diseaseModelList);
    void onDateSelected(String date);
    void onFailed(String msg);
    void onLoad();
    void onFinishload();
    void onSignupValid(UserModel userModel);
    void onFailed();
    void onServer();

    void onnotconnect(String msg);
}
