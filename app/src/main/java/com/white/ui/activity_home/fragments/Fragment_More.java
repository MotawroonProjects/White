package com.white.ui.activity_home.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.white.R;
import com.white.databinding.FragmentMedicineBinding;
import com.white.databinding.FragmentMoreBinding;
import com.white.models.SettingModel;
import com.white.models.UserModel;
import com.white.mvp.fragment_more_mvp.FragmentMorePresenter;
import com.white.mvp.fragment_more_mvp.MoreFragmentView;
import com.white.preferences.Preferences;
import com.white.share.Common;
import com.white.tags.Tags;
import com.white.ui.activity_contactus.ContactusActivity;
import com.white.ui.activity_edit_profile.EditprofileActivity;
import com.white.ui.activity_favourite.FavouriteActivity;
import com.white.ui.activity_home.HomeActivity;
import com.white.ui.activity_language.LanguageActivity;
import com.white.ui.activity_login.LoginActivity;
import com.white.ui.activity_room.ChatRoomActivity;
import com.white.ui.activity_web_view.WebViewActivity;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Fragment_More extends Fragment implements MoreFragmentView {
    private FragmentMoreBinding binding;
    private String lang;
    private HomeActivity activity;
    private FragmentMorePresenter presenter;
    private Preferences preferences;
    private UserModel userModel;
    private ProgressDialog dialog;
    private SettingModel setting;
    private String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public static Fragment_More newInstance() {
        return new Fragment_More();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        presenter = new FragmentMorePresenter(this, activity);
        binding.llChangeLanguage.setOnClickListener(view -> {
            Intent intent = new Intent(activity, LanguageActivity.class);
            startActivityForResult(intent, 100);
        });
        binding.llcontactus.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ContactusActivity.class);
            startActivity(intent);
        });

        binding.lleditprofile.setOnClickListener(view -> {
            if (userModel!=null) {
                Intent intent = new Intent(activity, EditprofileActivity.class);
                startActivity(intent);
            } else {
                Common.CreateDialogAlert(activity, activity.getResources().getString(R.string.please_sign_in_or_sign_up));
            }
        });
        binding.llchat.setOnClickListener(view -> {
            if (userModel!=null) {
                Intent intent = new Intent(activity, ChatRoomActivity.class);
                startActivity(intent);
            } else {
                Common.CreateDialogAlert(activity, activity.getResources().getString(R.string.please_sign_in_or_sign_up));
            }
        });
        binding.llfav.setOnClickListener(view -> {
            if (userModel!=null) {
                Intent intent = new Intent(activity, FavouriteActivity.class);
                startActivity(intent);
            } else {
                Common.CreateDialogAlert(activity, activity.getResources().getString(R.string.please_sign_in_or_sign_up));
            }
        });
        binding.logout.setOnClickListener(view -> {
            if (userModel!=null) {
                presenter.logout(userModel);
            } else {
                Common.CreateDialogAlert(activity, activity.getResources().getString(R.string.please_sign_in_or_sign_up));
            }
        });
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setting != null && setting.getSettings() != null && setting.getSettings().getWhatsapp() != null) {
                    presenter.open("https://api.whatsapp.com/send?phone="+setting.getSettings().getWhatsapp());
                }
                else {
                    Toast.makeText(activity, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.facebook.setOnClickListener(v -> {
           // Log.e("ldlldl",setting.getSettings().getInstagram());
            if (setting!=null && setting.getSettings()!=null && setting.getSettings().getFacebook()!=null) {
                if (setting.getSettings().getFacebook().matches(regex)) {
                    presenter.open(setting.getSettings().getFacebook());

                } else {
                    Toast.makeText(activity, R.string.link_inc, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
            }
        });
        binding.instgram.setOnClickListener(v -> {
            if (setting!=null && setting.getSettings()!=null && setting.getSettings().getInstagram()!=null) {
                if (setting.getSettings().getInstagram().matches(regex)) {
                    presenter.open(setting.getSettings().getInstagram());

                } else {
                    Toast.makeText(activity, R.string.link_inc, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
            }
        });

        binding.twitter.setOnClickListener(v -> {
            if (setting!=null && setting.getSettings()!=null&& setting.getSettings().getTwitter()!=null) {
                if (setting.getSettings().getTwitter().matches(regex)) {
                    presenter.open(setting.getSettings().getTwitter());

                } else {
                    Toast.makeText(activity, R.string.link_inc, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
            }
        });
        binding.google.setOnClickListener(v -> {
            if (setting!=null && setting.getSettings()!=null && setting.getSettings().getGoogle_plus()!=null) {
                if (setting.getSettings().getGoogle_plus().matches(regex)) {
                    presenter.open(setting.getSettings().getGoogle_plus());

                } else {
                    Toast.makeText(activity, R.string.link_inc, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, R.string.not_avail_now, Toast.LENGTH_SHORT).show();
            }
        });
        binding.llterms.setOnClickListener(view -> {
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra("url", Tags.base_url+setting.getSettings().getTerms_link());
            startActivity(intent);
        });
        presenter.getSetting();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data!=null) {
            String lang = data.getStringExtra("lang");
            activity.refreshActivity(lang);

        }
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoad() {
        if (dialog == null) {
            dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
        } else {
            dialog.dismiss();
        }
        dialog.show();
    }

    @Override
    public void onFinishload() {
        dialog.dismiss();
    }


    @Override
    public void logout() {
        preferences.clear(activity);
        navigateToSignInActivity();
    }

    @Override
    public void onsetting(SettingModel body) {
        this.setting = body;
//        if (setting.getSettings().getGoogle_plus() == null) {
//            binding.google.setVisibility(View.GONE);
//        }
//        if (setting.getSettings().getInstagram() == null) {
//            binding.instgram.setVisibility(View.GONE);
//        }
//        if (setting.getSettings().getFacebook() == null) {
//            binding.facebook.setVisibility(View.GONE);
//        }
//        if (setting.getSettings().getTwitter() == null) {
//            binding.twitter.setVisibility(View.GONE);
//        }
//        if (setting.getSettings().getGoogle_plus() == null && setting.getSettings().getInstagram() == null && setting.getSettings().getFacebook() == null && setting.getSettings().getTwitter() == null) {
//            binding.tv.setVisibility(View.GONE);
//        } else {
//            binding.tv.setVisibility(View.VISIBLE);
//
//        }

    }

    private void navigateToSignInActivity() {

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.finish();
        startActivity(intent);
    }

    @Override
    public void ViewSocial(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferences!=null) {
            userModel = preferences.getUserData(activity);
            udpate();
        }
    }

    private void udpate() {
        if (userModel!=null && userModel.getData().getLogo()!=null) {
            Picasso.get().load(Tags.IMAGE_URL + userModel.getData().getLogo()).resize(720, 480).onlyScaleDown().into(binding.image);
        }
        if (userModel!=null) {
            binding.tvName.setText(userModel.getData().getName());
        }
    }
}
