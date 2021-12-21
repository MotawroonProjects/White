package com.white.mvp.activity_editprofile_mvp;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.white.R;
import com.white.models.AllDiseasesModel;
import com.white.models.DiseaseModel;
import com.white.models.EditProfileModel;
import com.white.models.UserModel;
import com.white.preferences.Preferences;
import com.white.remote.Api;
import com.white.share.Common;
import com.white.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditprofilePresenter implements DatePickerDialog.OnDateSetListener {
    private UserModel userModel;
    private Preferences preferences;
    private EditprofileActivityView view;
    private Context context;
    private List<String> genderList;
    private List<String> bloodList;
    private List<DiseaseModel> diseaseModelList;
    private DatePickerDialog datePickerDialog;

    public ActivityEditprofilePresenter(EditprofileActivityView view, Context context) {
        this.context = context;
        this.view = view;
        genderList = new ArrayList<>();
        bloodList = new ArrayList<>();
        diseaseModelList = new ArrayList<>();
        createDateDialog();
    }

    public void backPress() {

        view.onFinished();


    }

    public void checkData(EditProfileModel editProfileModel, UserModel userModel) {
        if (editProfileModel.isDataValid(context)) {
            if (!editProfileModel.getImageUrl().isEmpty()) {
                editprofileimage(editProfileModel, userModel, context);
            } else {
                editprofile(editProfileModel, userModel);
            }
        }
    }

    private void editprofileimage(

            EditProfileModel editProfileModel, UserModel userModel, Context context) {

        RequestBody name_part = Common.getRequestBodyText(editProfileModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(editProfileModel.getPhone_code().replace("+", "00"));
        RequestBody phone_part = Common.getRequestBodyText(editProfileModel.getPhone());
        RequestBody birth_part = Common.getRequestBodyText(editProfileModel.getBirth_date());
        RequestBody blood_part = Common.getRequestBodyText(editProfileModel.getBlood_type());
        RequestBody gender_part = Common.getRequestBodyText(editProfileModel.getGender().toLowerCase());
        RequestBody user_part = Common.getRequestBodyText(userModel.getData().getId() + "");
        RequestBody weight_part = Common.getRequestBodyText(editProfileModel.getWeight());
        RequestBody heghit_part = Common.getRequestBodyText(editProfileModel.getHeight());
        RequestBody fat_part = Common.getRequestBodyText(editProfileModel.getFat()+"%");
        RequestBody type_part = Common.getRequestBodyText("patient");
        RequestBody soft_part = Common.getRequestBodyText("android");

        List<RequestBody> diseases_part = new ArrayList<>();
        for (int i = 0; i < editProfileModel.getDiseaseModelList().size(); i++) {
            diseases_part.add(Common.getRequestBodyText(editProfileModel.getDiseaseModelList().get(i).getId() + ""));
        }


        MultipartBody.Part image_form_part = Common.getMultiPart(context, Uri.parse(editProfileModel.getImageUrl()), "logo");
        view.onLoad();
        Api.getService(Tags.base_url)
                .editprofile("Bearer " + userModel.getData().getToken(), phone_code_part, phone_part, name_part, birth_part, blood_part, gender_part, type_part, soft_part, diseases_part, user_part,weight_part,heghit_part,fat_part, image_form_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onupdateValid(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                if (response.code() == 409) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }                                 //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onnotconnect(t.getMessage().toLowerCase());
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed();
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void editprofile(EditProfileModel editProfileModel, UserModel userModel) {
        Log.e("ddddd", editProfileModel.getPhone_code() + " " + editProfileModel.getPhone() + " " + editProfileModel.getName() + " " + editProfileModel.getBirth_date() + " " + editProfileModel.getBlood_type() + " " + editProfileModel.getGender() + " " + "patient" + " " + "android");
        List<String> diseases_part = new ArrayList<>();
        for (int i = 0; i < editProfileModel.getDiseaseModelList().size(); i++) {
            diseases_part.add(editProfileModel.getDiseaseModelList().get(i).getId() + "");
        }
        view.onLoad();
        Api.getService(Tags.base_url)
                .editprofile("Bearer " + userModel.getData().getToken(), editProfileModel.getPhone_code(), editProfileModel.getPhone(), editProfileModel.getName(), editProfileModel.getBirth_date(), editProfileModel.getBlood_type(), editProfileModel.getGender().toLowerCase(), "patient", "android", diseases_part,editProfileModel.getWeight(),editProfileModel.getHeight(),editProfileModel.getFat()+"%", userModel.getData().getId() + "")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onupdateValid(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                if (response.code() == 409) {
                                    view.onFailed(context.getString(R.string.phone_found));
                                } else {
                                    view.onFailed(response.message() + "");
                                }                                 //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onnotconnect(t.getMessage().toLowerCase());
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed();
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void createDateDialog() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOkText(context.getString(R.string.select));
        datePickerDialog.setCancelText(context.getString(R.string.cancel));
        datePickerDialog.setAccentColor(ContextCompat.getColor(context, R.color.colorPrimary));
        datePickerDialog.setOkColor(ContextCompat.getColor(context, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ContextCompat.getColor(context, R.color.gray4));
        datePickerDialog.setLocale(Locale.ENGLISH);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setMaxDate(calendar);

    }

    public void getGender() {
        genderList.clear();
        genderList.add(context.getString(R.string.ch_gender));
        genderList.add(context.getString(R.string.male));
        genderList.add(context.getString(R.string.female));
        view.onGenderSuccess(genderList);

    }

    public void getBloodType() {
        bloodList.clear();
        bloodList.add(context.getString(R.string.ch_blood));
        bloodList.add("A+");
        bloodList.add("A-");
        bloodList.add("B+");
        bloodList.add("B-");
        bloodList.add("O+");
        bloodList.add("O-");
        bloodList.add("AB+");
        bloodList.add("AB-");
        view.onBloodSuccess(bloodList);
    }

    public void getDiseases() {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
        view.onLoad();

        Api.getService(Tags.base_url)
                .getdiseas()
                .enqueue(new Callback<AllDiseasesModel>() {
                    @Override
                    public void onResponse(Call<AllDiseasesModel> call, Response<AllDiseasesModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            view.onDiseasesSuccess(response.body().getData());
                        } else {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllDiseasesModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });


    }

    public void showDateDialog(FragmentManager fragmentManager) {
        try {
            datePickerDialog.show(fragmentManager, "");

        } catch (Exception e) {
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date = dateFormat.format(new Date(calendar.getTimeInMillis()));
        ActivityEditprofilePresenter.this.view.onDateSelected(date);
    }
}
