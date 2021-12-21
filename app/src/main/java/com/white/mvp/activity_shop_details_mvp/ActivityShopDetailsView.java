package com.white.mvp.activity_shop_details_mvp;

import com.white.models.PlaceDetailsModel;

public interface ActivityShopDetailsView {
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);
    void onSuccess(PlaceDetailsModel model);
}
