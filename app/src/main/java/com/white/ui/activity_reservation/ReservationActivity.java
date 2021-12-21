package com.white.ui.activity_reservation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.white.R;
import com.white.adapters.ChildReservisionHourAdapter;
import com.white.adapters.ReservisionHourAdapter;
import com.white.databinding.ActivityReservationBinding;
import com.white.language.Language;
import com.white.models.ApointmentModel;
import com.white.models.ChatUserModel;
import com.white.models.DayModel;
import com.white.models.ReservisionTimeModel;
import com.white.models.RoomIdModel;
import com.white.models.SingleDoctorModel;
import com.white.models.SingleReservisionTimeModel;
import com.white.models.UserModel;
import com.white.mvp.activity_reservation_mvp.ActivityReservationPresenter;
import com.white.mvp.activity_reservation_mvp.ActivityReservationView;
import com.white.preferences.Preferences;
import com.white.share.Common;
import com.white.ui.activity_complete_clinic_reservision.CompleteClinicReservationActivity;
import com.white.ui.chat_activity.ChatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ReservationActivity extends AppCompatActivity implements ActivityReservationView {
    private String lang;
    private ActivityReservationBinding binding;
    private SingleDoctorModel doctorModel;
    private String type = "";
    private String date = "";
    private String time = "";
    private String dayname = "";

    private ApointmentModel.Data apointmentModel;


    private List<SingleReservisionTimeModel> singleReservisionTimeModelList;
    private ReservisionHourAdapter reservisionHourAdapter;
    private List<SingleReservisionTimeModel.Detials> detialsList;
    private ChildReservisionHourAdapter childReservisionHourAdapter;
    private ProgressDialog dialog2;
    private ActivityReservationPresenter presenter;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservation);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("data") != null) {
            doctorModel = (SingleDoctorModel) intent.getSerializableExtra("data");
        } else {
            apointmentModel = (ApointmentModel.Data) intent.getSerializableExtra("DATA");
        }
    }

    private void initView() {
        singleReservisionTimeModelList = new ArrayList<>();
        detialsList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(doctorModel);
        binding.setAppoinmentmodel(apointmentModel);
        presenter = new ActivityReservationPresenter(this, this);
        reservisionHourAdapter = new ReservisionHourAdapter(singleReservisionTimeModelList, this);
        childReservisionHourAdapter = new ChildReservisionHourAdapter(detialsList, this);
        binding.recViewhour.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewhour.setAdapter(reservisionHourAdapter);
        binding.recViewchildhour.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recViewchildhour.setAdapter(childReservisionHourAdapter);
//        type = "normal";
//        binding.flCall.setBackgroundResource(R.drawable.small_rounded_red_strock);
//        binding.flLive.setBackgroundResource(0);
//        binding.flChat.setBackgroundResource(0);

        binding.cardCall.setOnClickListener(view -> {
            type = "normal";
            binding.flCall.setBackgroundResource(R.drawable.small_rounded_red_strock);
            binding.flLive.setBackgroundResource(0);
            binding.flChat.setBackgroundResource(0);

        });

        binding.cardLive.setOnClickListener(view -> {
            type = "online";
            binding.flCall.setBackgroundResource(0);
            binding.flChat.setBackgroundResource(0);
            binding.flLive.setBackgroundResource(R.drawable.small_rounded_red_strock);


        });

        binding.cardChat.setOnClickListener(view -> {
            type = "chat";
            binding.flCall.setBackgroundResource(0);
            binding.flLive.setBackgroundResource(0);
            binding.flChat.setBackgroundResource(R.drawable.small_rounded_red_strock);
            if (userModel != null) {
                if (apointmentModel != null) {
                    presenter.createroom(apointmentModel, userModel);
                } else {
                    presenter.createroom(doctorModel, userModel);
                }
            } else {
                Common.CreateDialogAlert(this, getResources().getString(R.string.please_sign_in_or_sign_up));
            }

        });

        binding.imageBack.setOnClickListener(view -> {
            finish();
        });

        binding.llDate.setOnClickListener(view -> presenter.showDateDialog(getFragmentManager()));
        gettimes();
        if (apointmentModel != null) {
            presenter.getDays(userModel, apointmentModel.getDoctor_id());

            if (apointmentModel.getReservation_type().equals("normal")) {
                binding.cardLive.setVisibility(View.GONE);
                binding.flCall.setBackgroundResource(R.drawable.small_rounded_red_strock);
                binding.flLive.setBackgroundResource(0);
                binding.flChat.setBackgroundResource(0);

            } else {
                binding.cardCall.setVisibility(View.GONE);
                binding.flCall.setBackgroundResource(0);
                binding.flChat.setBackgroundResource(0);
                binding.flLive.setBackgroundResource(R.drawable.small_rounded_red_strock);

            }
        } else {
            presenter.getDays(userModel, doctorModel.getId());

        }

    }

    private void gettimes() {
        if (apointmentModel == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String date = dateFormat.format(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
            String stringDate = sdf.format(System.currentTimeMillis());
            this.date = date;
            this.dayname = stringDate;
            binding.tvDate.setText(date);

        } else if (apointmentModel != null) {
            this.date = apointmentModel.getDate();
            this.dayname = apointmentModel.getDay_name();
            binding.tvDate.setText(date);
        }
        if (doctorModel != null) {
            presenter.getreservisiontime(doctorModel, "normal", date, dayname.toUpperCase());
        } else {
            presenter.getreservisiontime(apointmentModel.getDoctor_fk(), "normal", date, dayname.toUpperCase());
        }
    }

    @Override
    public void onDateSelected(String date, String dayname) {
        this.date = date;
        binding.tvDate.setText(date);
        this.dayname = dayname;
        detialsList.clear();
        singleReservisionTimeModelList.clear();
        reservisionHourAdapter.notifyDataSetChanged();
        childReservisionHourAdapter.notifyDataSetChanged();
        Log.e("llll", dayname);
        if (doctorModel != null) {
            presenter.getreservisiontime(doctorModel, "normal", date, dayname);
        } else {
            presenter.getreservisiontime(apointmentModel.getDoctor_fk(), "normal", date, dayname);

        }
    }

    @Override
    public void onLoad() {
        if (dialog2 == null) {
            dialog2 = Common.createProgressDialog(this, getString(R.string.wait));
            dialog2.setCancelable(false);
        } else {
            dialog2.dismiss();
        }
        dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onreservtimesucess(ReservisionTimeModel body) {
        singleReservisionTimeModelList.clear();
        singleReservisionTimeModelList.addAll(body.getData());
        reservisionHourAdapter.notifyDataSetChanged();
        if (singleReservisionTimeModelList.size() == 0) {
            binding.tvNoData.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoData.setVisibility(View.GONE);
        }

    }

    @Override
    public void onsucess(RoomIdModel body) {
        ChatUserModel chatUserModel;
        if (apointmentModel != null) {
            chatUserModel = new ChatUserModel(apointmentModel.getDoctor_fk().getName(), apointmentModel.getDoctor_fk().getLogo(), apointmentModel.getDoctor_id() + "", body.getData().getId());
        } else {
            chatUserModel = new ChatUserModel(doctorModel.getName(), doctorModel.getLogo(), doctorModel.getId() + "", body.getData().getId());

        }
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_user_data", chatUserModel);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void ondata(DayModel body) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
// Get the number of days in that month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.e("llldlld", daysInMonth + "");
        List<String> alldate = new ArrayList<>();
        for (int i = 0; i < body.getData().size(); i++) {
            Log.e("dkdkdk", body.getData().get(i).getDay_name());
            alldate.add(body.getData().get(i).getDay_name().toLowerCase());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
        List<Calendar> calendarList = new ArrayList<Calendar>();
        for (int j = 1; j <= daysInMonth; j++) {
            Calendar calendar2 = Calendar.getInstance(Locale.ENGLISH);
            calendar2.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            calendar2.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            calendar2.set(Calendar.DAY_OF_MONTH, j);
            String day = sdf.format(new Date(calendar2.getTimeInMillis()));
            Log.e("llll", day);
            if (alldate.contains(day.toLowerCase())) {
                calendarList.add(calendar2);
            }


        }
        Calendar calendars1[] = new Calendar[calendarList.size()];
        for (int i = 0; i < calendarList.size(); i++) {
            calendars1[i] = calendarList.get(i);
            Log.e("sssssss", calendarList.get(i).getTime() + " ");

        }
        presenter.setdisable(calendars1);

        Log.e("dldldll", calendarList.size() + " ");
    }

    public void getchild(int position) {
        detialsList.clear();
        detialsList.addAll(singleReservisionTimeModelList.get(position).getDetials());
        childReservisionHourAdapter.notifyDataSetChanged();
    }

    public void Setitem(SingleReservisionTimeModel.Detials detials) {
        if(!type.isEmpty()) {
            Intent intent = new Intent(this, CompleteClinicReservationActivity.class);
            if (doctorModel != null) {
                intent.putExtra("data", doctorModel);
                intent.putExtra("type", type);
                intent.putExtra("resrvid", 0);

            } else {
                intent.putExtra("type", type);
                intent.putExtra("resrvid", apointmentModel.getId());
                intent.putExtra("data", apointmentModel.getDoctor_fk());
                if (dayname.isEmpty()) {
                    dayname = apointmentModel.getDay_name();
                }

            }
            intent.putExtra("time", detials);

            intent.putExtra("dayname", dayname);

            intent.putExtra("date", date);
            startActivityForResult(intent, 1);
        }
        else {
            Toast.makeText(this,getResources().getString( R.string.ch_resv),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);

            finish();
        }
    }

}