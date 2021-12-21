package com.white.ui.activity_confirm_code;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.white.R;
import com.white.databinding.ActivityConfirmCodeBinding;
import com.white.language.Language;
import com.white.models.UserModel;
import com.white.mvp.activity_confirm_code_mvp.ActivityConfirmCodePresenter;
import com.white.mvp.activity_confirm_code_mvp.ActivityConfirmCodeView;
import com.white.preferences.Preferences;
import com.white.share.Common;
import com.white.ui.activity_home.HomeActivity;
import com.white.ui.activity_login.LoginActivity;
import com.white.ui.activity_sign_up.SignUpActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class ConfirmCodeActivity extends AppCompatActivity implements ActivityConfirmCodeView {
    private ActivityConfirmCodeBinding binding;
    private String phone_code = "";
    private String phone = "";
    private boolean canSend = false;
    private ActivityConfirmCodePresenter presenter;
    private ProgressDialog dialog;
    private Preferences preferences;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_code);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone_code = intent.getStringExtra("phone_code");
            phone = intent.getStringExtra("phone");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        String mPhone = phone_code + phone;
        binding.setPhone(mPhone);
        presenter = new ActivityConfirmCodePresenter(this, this, phone, phone_code);


        binding.btnConfirm.setOnClickListener(v -> {
            String sms = binding.edtCode.getText().toString().trim();
            if (!sms.isEmpty()) {
                presenter.login();
               // presenter.checkValidCode(sms);
            } else {
                binding.edtCode.setError(getString(R.string.inv_code));
            }
        });
        binding.btnResendCode.setOnClickListener(view -> {
            if (canSend) {
                canSend = false;
                presenter.resendCode();
            }
        });
    }


    @Override
    public void onCounterStarted(String time) {
        binding.btnResendCode.setText(String.format(Locale.ENGLISH, "%s %s", getString(R.string.resend2), time));
        binding.btnResendCode.setTextColor(ContextCompat.getColor(ConfirmCodeActivity.this, R.color.colorPrimary));
        binding.btnResendCode.setBackgroundResource(R.color.transparent);
    }

    @Override
    public void onCounterFinished() {
        canSend = true;
        binding.btnResendCode.setText(R.string.resend);
        binding.btnResendCode.setTextColor(ContextCompat.getColor(ConfirmCodeActivity.this, R.color.gray4));
        binding.btnResendCode.setBackgroundResource(R.color.white);
    }

    @Override
    public void onCodeFailed(String msg) {
        Common.CreateDialogAlert(this, msg);

    }

    @Override
    public void onUserFound(UserModel userModel) {
        preferences.create_update_userdata(ConfirmCodeActivity.this, userModel);

        Intent intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
        finish();
    }


    @Override
    public void onUserNoFound() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone_code", phone_code);
        intent.putExtra("phone", phone);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailed() {
        Toast.makeText(ConfirmCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServer() {
        Toast.makeText(ConfirmCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoad() {
        dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onFinishload() {
        dialog.dismiss();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(ConfirmCodeActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopTimer();
    }
}