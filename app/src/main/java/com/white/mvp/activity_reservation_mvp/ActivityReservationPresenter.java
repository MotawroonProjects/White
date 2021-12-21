package com.white.mvp.activity_reservation_mvp;

import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.white.R;
import com.white.models.ApointmentModel;
import com.white.models.DayModel;
import com.white.models.RoomIdModel;
import com.white.models.ReservisionTimeModel;
import com.white.models.SingleDoctorModel;
import com.white.models.UserModel;
import com.white.remote.Api;
import com.white.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityReservationPresenter implements DatePickerDialog.OnDateSetListener {
    private Context context;
    private ActivityReservationView view;
    private DatePickerDialog datePickerDialog;

    public ActivityReservationPresenter(Context context, ActivityReservationView view) {
        this.context = context;
        this.view = view;
        createDateDialog();
    }

    private void createDateDialog() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        Calendar calendar2 = Calendar.getInstance(Locale.ENGLISH);
        calendar2.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar2.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar2.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOkText(context.getString(R.string.select));
        datePickerDialog.setCancelText(context.getString(R.string.cancel));
        datePickerDialog.setAccentColor(ContextCompat.getColor(context, R.color.colorPrimary));
        datePickerDialog.setOkColor(ContextCompat.getColor(context, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ContextCompat.getColor(context, R.color.gray4));
        datePickerDialog.setLocale(Locale.ENGLISH);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setMinDate(calendar);
        datePickerDialog.setMaxDate(calendar2);

    }

    public void getDays(UserModel userModel, int id) {

        try {
            Api.getService(Tags.base_url)
                    .getDays("Bearer " + userModel.getData().getToken(), id, "off")
                    .enqueue(new Callback<DayModel>() {
                        @Override
                        public void onResponse(Call<DayModel> call, Response<DayModel> response) {
                            // Log.e("kdkdkdk", response.body().getRooms().size() + "");
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                view.ondata(response.body());
                            } else {
                                if (response.code() == 500) {
                                    //   Toast.makeText(com.ghiar.activities_fragments.activity_room.ChatRoomActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    view.onFailed(context.getResources().getString(R.string.server_error));

                                } else {
                                    view.onFailed(context.getResources().getString(R.string.failed));

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DayModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        view.onFailed(context.getResources().getString(R.string.something));
                                    } else {
                                        view.onFailed(t.getMessage());
                                    }
                                }

                            } catch (Exception e) {

                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("eeee", e.getMessage() + "__");
        }
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String date = dateFormat.format(new Date(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String stringDate = sdf.format(new Date(calendar.getTimeInMillis()));
        ActivityReservationPresenter.this.view.onDateSelected(date, stringDate.toUpperCase());
    }

    public void getreservisiontime(SingleDoctorModel singleDoctorModel, String type, String date, String dayname) {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
        view.onLoad();

        Api.getService(Tags.base_url)
                .getreservision(singleDoctorModel.getId() + "", date, dayname, type)
                .enqueue(new Callback<ReservisionTimeModel>() {
                    @Override
                    public void onResponse(Call<ReservisionTimeModel> call, Response<ReservisionTimeModel> response) {
                        view.onFinishload();

                        if (response.isSuccessful() && response.body() != null) {
                            //     Log.e("dldlldl",response.code()+"");
                            view.onreservtimesucess(response.body());

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
                    public void onFailure(Call<ReservisionTimeModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void createroom(ApointmentModel.Data apointmentModel, UserModel usermodel) {
        view.onLoad();

        try {
            Api.getService(Tags.base_url)
                    .createroom(usermodel.getData().getId() + "", apointmentModel.getDoctor_id() + "", "message", context.getResources().getString(R.string.want_resrv)).enqueue(new Callback<RoomIdModel>() {
                @Override
                public void onResponse(Call<RoomIdModel> call, Response<RoomIdModel> response) {
                    view.onFinishload();
                    if (response.isSuccessful()) {

                        Log.e("llll", response.toString());
                        view.onsucess(response.body());
                    } else {
                        try {
                            view.onFailed(context.getResources().getString(R.string.failed));

                            //  Toast.makeText(CartActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.errorBody().string());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<RoomIdModel> call, Throwable t) {
                    view.onFinishload();
                    try {
                        view.onFailed(context.getResources().getString(R.string.something));
                        //   Toast.makeText(CartActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            view.onFinishload();
            Log.e("error", e.getMessage().toString());
        }
    }

    public void createroom(SingleDoctorModel doctorModel, UserModel usermodel) {
        view.onLoad();

        try {
            Api.getService(Tags.base_url)
                    .createroom(usermodel.getData().getId() + "", doctorModel.getId() + "", "message", context.getResources().getString(R.string.want_resrv)).enqueue(new Callback<RoomIdModel>() {
                @Override
                public void onResponse(Call<RoomIdModel> call, Response<RoomIdModel> response) {
                    view.onFinishload();
                    if (response.isSuccessful()) {

                        Log.e("llll", response.toString());
                        view.onsucess(response.body());
                    } else {
                        try {
                            view.onFailed(context.getResources().getString(R.string.failed));

                            //  Toast.makeText(CartActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.errorBody().string());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<RoomIdModel> call, Throwable t) {
                    view.onFinishload();
                    try {
                        view.onFailed(context.getResources().getString(R.string.something));
                        //   Toast.makeText(CartActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            view.onFinishload();
            Log.e("error", e.getMessage().toString());
        }
    }

    public void setdisable(Calendar[] toArray) {

       // datePickerDialog.setDisabledDays(toArray);
        datePickerDialog.setSelectableDays(toArray);

    }
}
