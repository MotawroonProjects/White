package com.white.mvp.activity_complete_clinic_reservision;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.white.models.SingleDoctorModel;
import com.white.models.SingleReservisionTimeModel;
import com.white.models.UserModel;
import com.white.remote.Api;
import com.white.share.Common;
import com.white.tags.Tags;
import com.white.ui.activity_web_view.WebViewActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCompleteClinicReservationPresenter {
    private Context context;
    private ActivityCompleteClinicReservationView view;

    public ActivityCompleteClinicReservationPresenter(Context context, ActivityCompleteClinicReservationView view) {
        this.context = context;
        this.view = view;
    }

    public void addresrevision(UserModel userModel, SingleDoctorModel singleDoctorModel, SingleReservisionTimeModel.Detials detials, String date, String dayname,String detialss,String type) {

        Log.e("llll",type);
        if (userModel != null) {
            view.onLoad();
            Api.getService(Tags.base_url)
                    .addreservision(userModel.getData().getId() + "", singleDoctorModel.getId() + "", date, detials.getFrom(), singleDoctorModel.getDetection_price() + "", type, dayname.toUpperCase(), detials.getFrom_hour_type(),detialss)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            view.onFinishload();
                            if (response.isSuccessful() && response.body() != null) {
                                //  Log.e("eeeeee", response.body().getUser().getName());
                                //view.onSignupValid(response.body());
                                //view.onaddsucsess();
                                Intent intent=new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", "http://motmain.com/testingPayment?"+"user_id="+userModel.getData().getId()+"&amount="+singleDoctorModel.getDetection_price());
                                context.startActivity(intent);

                            } else {
                                try {
                                    Log.e("mmmmmmmmmmssss", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                if (response.code() == 500) {
                                    view.onServer();
                                } else {
                                    try {
                                        view.onFailed(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
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

        } else {
            view.onnotlogin();
        }
    }

    public void updateresrevision(UserModel userModel, SingleReservisionTimeModel.Detials singletimemodel, String date, int reservid, String dayname) {
        if (userModel != null) {
            view.onLoad();
            Api.getService(Tags.base_url)
                    .updatereservision(reservid + "", date, singletimemodel.getFrom(), singletimemodel.getFrom_hour_type(), dayname)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            view.onFinishload();
                            if (response.isSuccessful() && response.body() != null) {
                                //  Log.e("eeeeee", response.body().getUser().getName());
                                //view.onSignupValid(response.body());
                          view.onsucsess();

                            } else {
                                try {
                                    Log.e("mmmmmmmmmmssss", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                if (response.code() == 500) {
                                    view.onServer();
                                } else {
                                    try {
                                        view.onFailed(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
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

        } else {
            view.onnotlogin();
        }
    }


    public void addresrevision(UserModel usermodel, SingleDoctorModel doctorModel, SingleReservisionTimeModel.Detials singletimemodel, String date, String dayname, List<Uri> imagesList,String detials,String type) {
        List<MultipartBody.Part> imageparts=getMultiPartImages(imagesList);
        RequestBody detials_part = Common.getRequestBodyText(detials);
        RequestBody type_part = Common.getRequestBodyText(type);
        RequestBody user_part = Common.getRequestBodyText(usermodel.getData().getId()+"");
        RequestBody doctor_part = Common.getRequestBodyText(doctorModel.getId()+"");
        RequestBody date_part = Common.getRequestBodyText(date);
        RequestBody day_part = Common.getRequestBodyText(dayname.toUpperCase()+"");
        RequestBody fromtype_part = Common.getRequestBodyText(singletimemodel.getFrom_hour_type()+"");
        RequestBody from_part = Common.getRequestBodyText(singletimemodel.getFrom()+"");
        RequestBody price_part = Common.getRequestBodyText(doctorModel.getDetection_price()+"");
        if (usermodel != null) {
            view.onLoad();
            Api.getService(Tags.base_url)
                    .addreservision(user_part , doctor_part, date_part, from_part, price_part, type_part, day_part, fromtype_part,detials_part,imageparts)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            view.onFinishload();
                            if (response.isSuccessful() && response.body() != null) {
                                //  Log.e("eeeeee", response.body().getUser().getName());
                                //view.onSignupValid(response.body());
                             //   view.onsucsess();
                                Intent intent=new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", "http://motmain.com/testingPayment?"+"user_id="+usermodel.getData().getId()+"&amount="+doctorModel.getDetection_price());
                                context.startActivity(intent);
                            } else {
                                try {
                                    Log.e("mmmmmmmmmmssss", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                if (response.code() == 500) {
                                    view.onServer();
                                } else {
                                    try {
                                        view.onFailed(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
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

        } else {
            view.onnotlogin();
        }

    }

    private List<MultipartBody.Part> getMultiPartImages(List<Uri> imagesList) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri uri : imagesList) {
            if (uri != null) {
                MultipartBody.Part part = Common.getMultiPartImage(context, uri, "reservations_images[]");
                parts.add(part);
            }

        }
        return parts;
    }
}
