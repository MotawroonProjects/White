package com.white.mvp.activity_confirm_code_mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.white.R;
import com.white.models.UserModel;
import com.white.remote.Api;
import com.white.share.Common;
import com.white.tags.Tags;
import com.white.ui.activity_confirm_code.ConfirmCodeActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityConfirmCodePresenter {
    private Context context;
    private ActivityConfirmCodeView view;
    private String phone_code="",phone="";
    private CountDownTimer countDownTimer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode = "";
    private ConfirmCodeActivity activity;
    private String lang;
    public PhoneAuthCredential phoneAuthCredential;

    public ActivityConfirmCodePresenter(Context context, ActivityConfirmCodeView view, String phone,String phone_code)
    {
        this.context = context;
        this.view = view;
        this.phone = phone;
        this.phone_code = phone_code;
        activity = (ConfirmCodeActivity) context;
        mAuth = FirebaseAuth.getInstance();
        Paper.init(context);
        lang = Paper.book().read("lang", "ar");
        sendSmsCode();
    }

    public void sendSmsCode()
    {
        startCounter();
        mAuth.setLanguageCode(lang);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                ActivityConfirmCodePresenter.this.phoneAuthCredential=phoneAuthCredential;
                smsCode = phoneAuthCredential.getSmsCode();
                checkValidCode(smsCode);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                ActivityConfirmCodePresenter.this.verificationId = verification_id;
                Log.e("verification_id", verification_id);
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                if (e.getMessage() != null) {
                    view.onCodeFailed(e.getMessage());
                } else {
                    view.onCodeFailed(context.getString(R.string.failed));

                }
            }
        };
        Log.e("phone",phone_code+phone);
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        phone_code+phone,
                        120,
                        TimeUnit.SECONDS,
                        activity,
                        mCallBack

                );
    }
    public void checkValidCode(String code)
    {
        ProgressDialog dialog = Common.createProgressDialog(context,context.getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        if (verificationId != null) {
       phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
        }

            mAuth.signInWithCredential(phoneAuthCredential)
                    .addOnSuccessListener(authResult -> {
                        dialog.dismiss();
                        login();
                    }).addOnFailureListener(e -> {
                        dialog.dismiss();
                if (e.getMessage() != null) {
                    try {
                        view.onCodeFailed(e.getMessage());

                    }catch (Exception ex)
                    {

                    }
                } else {
                    view.onCodeFailed(context.getString(R.string.failed));

                }
            });


    }



    private void startCounter()
    {
        countDownTimer = new CountDownTimer(120000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) ((millisUntilFinished / 1000) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);

                String time = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
                view.onCounterStarted(time);



            }

            @Override
            public void onFinish() {
                view.onCounterFinished();


            }
        };

        countDownTimer.start();
    }
    public void resendCode()
    {
        if (countDownTimer != null) {
            countDownTimer.start();
        }
        sendSmsCode();
    }
    public void stopTimer()
    {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void login() {
        Log.e("phone",phone_code+phone);

        view.onLoad();
        Api.getService(Tags.base_url)
                .login(phone_code, phone)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                             Log.e("eeeeee", response.body().getData().getName());
                            view.onUserFound(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.code()+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            }  else if (response.code() == 401) {
                                view.onUserNoFound();
                            }else if (response.code() == 404) {
                                view.onUserNoFound();
                            }else {
                                view.onFailed();
                                //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
}
