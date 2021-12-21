package com.white.ui.activity_contactus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.white.R;
import com.white.databinding.ActivityContactUsBinding;
import com.white.databinding.ActivitySignUpBinding;
import com.white.databinding.DialogSelectImageBinding;
import com.white.language.Language;
import com.white.models.ContactUsModel;
import com.white.models.SettingModel;
import com.white.models.UserModel;
import com.white.mvp.activity_contactus_mvp.ActivityContactusPresenter;
import com.white.mvp.activity_contactus_mvp.ActivityContactusView;
import com.white.preferences.Preferences;
import com.white.share.Common;

import io.paperdb.Paper;

public class ContactusActivity extends AppCompatActivity implements ActivityContactusView {
    private ActivityContactUsBinding binding;

    private ContactUsModel contactUsModel;
    private ActivityContactusPresenter presenter;
    private Preferences preferences;
    private String lang;
    private ProgressDialog dialog2;
    private UserModel userModel;
    private SettingModel setting;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setModel(userModel);
        Paper.init(this);

        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        contactUsModel = new ContactUsModel();
        if(userModel!=null){
            contactUsModel.setEmail(userModel.getData().getEmail());
            contactUsModel.setName(userModel.getData().getName());
        }
        binding.setContactModel(contactUsModel);

        presenter = new ActivityContactusPresenter(this, this);
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkData(contactUsModel);
            }
        });
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setting != null && setting.getSettings() != null && setting.getSettings().getFacebook() != null) {
                    presenter.open(setting.getSettings().getFacebook());
                }
                else {
                    Toast.makeText(ContactusActivity.this, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setting != null && setting.getSettings() != null && setting.getSettings().getWhatsapp() != null) {
                    presenter.open("https://api.whatsapp.com/send?phone="+setting.getSettings().getWhatsapp());
                }
                else {
                    Toast.makeText(ContactusActivity.this, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
                }
            }
        });
        presenter.getSetting();

    }


    @Override
    public void onLoad() {
        dialog2 = Common.createProgressDialog(this, getString(R.string.wait));
        dialog2.setCancelable(false);
        dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }

    @Override
    public void onContactVaild() {
        finish();

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(ContactusActivity.this, msg, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onFailed() {
        Toast.makeText(ContactusActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServer() {
        Toast.makeText(ContactusActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onsetting(SettingModel body) {
        this.setting = body;

        if (setting.getSettings().getWhatsapp() == null) {
            binding.whatsapp.setVisibility(View.GONE);
        }
        if (setting.getSettings().getFacebook() == null) {
            binding.facebook.setVisibility(View.GONE);
        }


    }
    @Override
    public void ViewSocial(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        startActivity(intent);

    }

}