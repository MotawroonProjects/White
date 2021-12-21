package com.white.mvp.fragment_home_mvp;

import com.white.models.Slider_Model;

import java.util.List;

public interface HomeFragmentView {
    void onFailed(String msg);

    void onProgressSliderShow();
    void onProgressSliderHide();
    void onSliderSuccess(List<Slider_Model.Data> sliderModelList);
}
