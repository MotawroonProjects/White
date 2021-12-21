package com.white.mvp.fragment_more_mvp;

import com.white.models.SettingModel;

public interface MoreFragmentView {
    void onFailed(String msg);

    void onLoad();

    void onFinishload();


    void logout();

    void onsetting(SettingModel body);
    void ViewSocial(String path);
}
