package com.white.ui.activity_reservision_detials;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.white.R;
import com.white.adapters.ReasonAdapter;
import com.white.databinding.ActivityReservisionDetialsBinding;
import com.white.language.Language;
import com.white.models.ApointmentModel;
import com.white.models.ReasonModel;
import com.white.models.UserModel;
import com.white.mvp.actvity_reservision_detials_mvp.ActivityReservationDetialsPresenter;
import com.white.mvp.actvity_reservision_detials_mvp.ActivityReservationDetialsView;
import com.white.preferences.Preferences;
import com.white.share.Common;
import com.white.ui.activity_live.LiveActivity;
import com.white.ui.activity_reservation.ReservationActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ReservationDetialsActivity extends AppCompatActivity implements ActivityReservationDetialsView {
    private String lang;
    private ActivityReservisionDetialsBinding binding;
    private ApointmentModel.Data apointmentModel;
    private List<ReasonModel.Data> dataList;
    private ReasonAdapter reasonAdapter;
    private ActivityReservationDetialsPresenter presenter;

    private ProgressDialog dialog2;
    private UserModel usermodel;
    private Preferences preferences;
    private static final int REQUEST_PHONE_CALL = 3;
    private Intent intent;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservision_detials);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        apointmentModel = (ApointmentModel.Data) intent.getSerializableExtra("data");

    }

    private void initView() {
        dataList = new ArrayList<>();
        preferences = Preferences.getInstance();
        usermodel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        binding.setModel(apointmentModel);

        reasonAdapter = new ReasonAdapter(dataList, this);
        binding.recViewreason.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewreason.setAdapter(reasonAdapter);
        presenter = new ActivityReservationDetialsPresenter(this, this);

        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.cancelreserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSheet();
            }
        });
        binding.imageCloseSpecialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSheet();

            }
        });
        binding.btncahngeresev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservationDetialsActivity.this, ReservationActivity.class);
                intent.putExtra("DATA", apointmentModel);
                startActivity(intent);
                //  presenter.addresrevision(usermodel, doctorModel, singletimemodel, date, dayname);

            }
        });
        binding.btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(apointmentModel);
            }
        });
        String myTime = apointmentModel.getDate() + " " + apointmentModel.getTime() + " " + apointmentModel.getTime_type();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa", Locale.US);
        Date d = null;
        Log.e("dlldl", myTime);
        try {
            d = df.parse(myTime);
        } catch (ParseException e) {
            Log.e("llflfl", e.toString());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, Integer.parseInt(apointmentModel.getDoctor_fk().getDetection_time()));
        long time = System.currentTimeMillis();

//        if (time > cal.getTimeInMillis()) {
//if(apointmentModel.getReservation_type().equals("online")){
//            binding.btCall.setVisibility(View.GONE);}

//        }
//        if (apointmentModel.getReservation_status().equals("open")) {
//            binding.btCall.setVisibility(View.VISIBLE);
//        } else {
//            binding.btCall.setVisibility(View.GONE);
//
//        }


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
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onsucsess() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onServer() {
        Toast.makeText(com.white.ui.activity_reservision_detials.ReservationDetialsActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onnotlogin() {


        Common.CreateDialogAlert(this, getResources().getString(R.string.please_sign_in_or_sign_up));
    }


    @Override
    public void onFailed() {
        Toast.makeText(ReservationDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressShow() {
        binding.progBarreason.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBarreason.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(ReasonModel body) {
        if (body.getData() != null && body.getData().size() > 0) {
            dataList.addAll(body.getData());
            reasonAdapter.notifyDataSetChanged();
            binding.tvNoDatareason.setVisibility(View.GONE);

        } else {
            binding.tvNoDatareason.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(ApointmentModel.Data data) {
        if (data.getReservation_type().equals("online")) {
            Intent intent = new Intent(ReservationDetialsActivity.this, LiveActivity.class);
            intent.putExtra("room", data.getId());
            intent.putExtra("type", data.getReservation_type());
            startActivityForResult(intent, 1);
        } else {
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.getDoctor_fk().getPhone_code() + data.getDoctor_fk().getPhone(), null));
            if (intent != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        startActivityForResult(intent,1);
                    }
                } else {
                    startActivityForResult(intent,1);
                }
            }
        }
    }

    @Override
    public void oncloseSuccess() {
        finish();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(ReservationDetialsActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    private void openSheet() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        binding.flSpecializationSheet.clearAnimation();
        binding.flSpecializationSheet.startAnimation(animation);
        presenter.getreasons();

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.flSpecializationSheet.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void closeSheet() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);


        binding.flSpecializationSheet.clearAnimation();
        binding.flSpecializationSheet.startAnimation(animation);


        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.flSpecializationSheet.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void setreason(String reason) {
        presenter.cancelreserv(reason, apointmentModel);
    }

    public void open(ApointmentModel.Data data) {


        presenter.opencall(data, usermodel);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                } else {

                }
                return;
            }
        }

    }

    public void CreateAddRateAlertDialog() {
        final androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rate, null);
        ImageView img_close = view.findViewById(R.id.img_close);
        CircleImageView image = view.findViewById(R.id.image);
        TextView tv_name = view.findViewById(R.id.tv_name);
        image.setVisibility(View.GONE);

        final EditText edt_comment = view.findViewById(R.id.edt_comment);
        final TextView tv_rate = view.findViewById(R.id.tv_rate);
        final Button btn_rate = view.findViewById(R.id.btn_rate);
        final SimpleRatingBar rate = view.findViewById(R.id.simplarate);
        //  Picasso.get().load(Uri.parse(Tags.IMAGE_URL + notificationModel.getFrom_user_image())).fit().into(image);
        //tv_name.setText(notificationModel.getFrom_user_full_name());


        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edt_comment.getText().toString().trim();
                presenter.closecall(apointmentModel, usermodel, comment, rate.getRating());
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        // dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == 1) {
            CreateAddRateAlertDialog();
        }
    }
}