package com.white.ui.activity_complete_clinic_reservision;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.white.R;
import com.white.adapters.ImagesAdapter;
import com.white.databinding.ActivityCompleteClinicReservisionBinding;
import com.white.databinding.DialogAddReservBinding;
import com.white.databinding.DialogAlertBinding;
import com.white.databinding.DialogSelectImageBinding;
import com.white.language.Language;
import com.white.models.ApointmentModel;
import com.white.models.SingleDoctorModel;
import com.white.models.SingleReservisionTimeModel;
import com.white.models.UserModel;
import com.white.mvp.activity_complete_clinic_reservision.ActivityCompleteClinicReservationPresenter;
import com.white.mvp.activity_complete_clinic_reservision.ActivityCompleteClinicReservationView;
import com.white.preferences.Preferences;
import com.white.share.Common;
import com.white.ui.activity_live.LiveActivity;
import com.white.ui.activity_login.LoginActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CompleteClinicReservationActivity extends AppCompatActivity implements ActivityCompleteClinicReservationView {
    private String lang;
    private ActivityCompleteClinicReservisionBinding binding;
    private SingleDoctorModel doctorModel;
    private String date = "";
    private String dayname = "";
    private int reservid;
    private ActivityCompleteClinicReservationPresenter presenter;
    private SingleReservisionTimeModel.Detials singletimemodel;
    private ProgressDialog dialog2;
    private UserModel usermodel;
    private Preferences preferences;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;

    private static final int REQUEST_PHONE_CALL = 3;
    private List<Uri> imagesList;
    private ImagesAdapter imagesAdapter;
    private AlertDialog dialog;
    private String type;
    private Intent intent;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_complete_clinic_reservision);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        doctorModel = (SingleDoctorModel) intent.getSerializableExtra("data");
        singletimemodel = (SingleReservisionTimeModel.Detials) intent.getSerializableExtra("time");
        date = intent.getStringExtra("date");
        dayname = intent.getStringExtra("dayname");
        reservid =intent.getIntExtra("resrvid",0);
        type =intent.getStringExtra("type");

    }

    private void initView() {

        preferences = Preferences.getInstance();
        usermodel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setDate(date);
        binding.setModel(doctorModel);
        binding.setTimemodel(singletimemodel);
        presenter = new ActivityCompleteClinicReservationPresenter(this, this);

        imagesList = new ArrayList<>();

        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesAdapter = new ImagesAdapter(imagesList, this);
        binding.recViewImages.setAdapter(imagesAdapter);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });

        if(reservid!=0){
            binding.btnConsultationReserve.setText(getResources().getString(R.string.Update_resev));
            binding.tv.setText(getResources().getString(R.string.Update_resev));
            binding.card.setVisibility(View.GONE);

        }
        binding.btnConsultationReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String detials=binding.edtdetials.getText().toString();

                if(reservid ==0) {
                    if(imagesList.size()==0){
                    presenter.addresrevision(usermodel, doctorModel, singletimemodel, date, dayname,detials,type);}
                    else {
                        presenter.addresrevision(usermodel,doctorModel,singletimemodel,date,dayname,imagesList,detials,type);
                    }
                }
                else {
                    Log.e("dkdkdk",dayname+" "+reservid+" "+usermodel.getData().getId());
                    presenter.updateresrevision(usermodel,  singletimemodel, date,  reservid,dayname);

                }
            }
        });
        binding.imageCamera.setOnClickListener(v -> createDialogAlert());


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

        Common.CreateDialogAlert(this,getResources().getString(R.string.suc));
//        Intent intent=getIntent();
//        setResult(RESULT_OK,intent);
//
//        finish();
    }

    @Override
    public void onServer() {
        Toast.makeText(CompleteClinicReservationActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onnotlogin() {


        Common.CreateDialogAlert(this, getResources().getString(R.string.please_sign_in_or_sign_up));
    }


    @Override
    public void onFailed() {
        Toast.makeText(CompleteClinicReservationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onaddsucsess() {
        CreateDialogAlert(this,getResources().getString(R.string.suc));
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(CompleteClinicReservationActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    public void createDialogAlert() {
        dialog = new AlertDialog.Builder(this)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);
        binding.btnCamera.setOnClickListener(v -> checkCameraPermission());
        binding.btnGallery.setOnClickListener(v -> checkReadPermission());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    public void checkReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {


        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Uri uri = data.getData();
            cropImage(uri);


        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Uri uri = getUriFromBitmap(bitmap);
            if (uri != null) {
                cropImage(uri);

            }


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();

                if (imagesList.size() > 0) {
                    imagesList.add(imagesList.size() - 1, uri);
                    imagesAdapter.notifyItemInserted(imagesList.size() - 1);

                } else {
                    imagesList.add(uri);
                    imagesList.add(null);
                    imagesAdapter.notifyItemRangeInserted(0, imagesList.size());
                }


                dialog.dismiss();

                binding.recViewImages.postDelayed(() -> {
                    binding.recViewImages.smoothScrollToPosition(imagesList.size() - 1);
                }, 100);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }
    private void cropImage(Uri uri) {

        CropImage.activity(uri).setAspectRatio(1, 1).setGuidelines(CropImageView.Guidelines.ON).start(this);

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


    public void delete(int adapterPosition) {
        imagesList.remove(adapterPosition);
        if (imagesList.size() == 1) {
            imagesList.clear();
            imagesAdapter.notifyDataSetChanged();
        } else {
            imagesAdapter.notifyItemRemoved(adapterPosition);
        }
    }
    public void open(ApointmentModel.Data data) {
        String date = data.getDate()+" " + data.getTime()+" "+data.getTime_type();
        Log.e("kdkdk", date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa", Locale.US);
        long datetime = 0;
        try {
            datetime = sdf.parse(date).getTime();
        } catch (ParseException e) {
            Log.e("dldkkd",e.toString());
        }
        long currenttime = System.currentTimeMillis();
        Log.e("kdkdk", date+" "+currenttime+" "+datetime);

          if (currenttime >= datetime) {
        if (data.getReservation_type().equals("online")) {
            Intent intent = new Intent(this, LiveActivity.class);
            intent.putExtra("room", data.getId());
            startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.getPatient_fk().getPhone_code() + data.getPatient_fk().getPhone(), null));
            if (intent != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            }
        }
        } else {
            Toast.makeText(this, getResources().getString(R.string.not_avail_now), Toast.LENGTH_LONG).show();
        }

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
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public  void CreateDialogAlert(Context context, String msg) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogAddReservBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_reserv, null, false);
//        TourGuide mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click) .setPointer(new Pointer()) .setToolTip(new ToolTip().setTitle(getResources().getString(R.string.copy)).setDescription("").setBackgroundColor(Color.parseColor("#e54d26"))
//                .setShadow(true).setGravity(Gravity.TOP | Gravity.RIGHT)).setOverlay(new Overlay()) .playOn(binding.imgCopy);

       // mToolTipsManager.setToolTipAnimator(MyCustomToolTipAnimator());
      //  builder.setTextAppearance(R.style.TooltipTextAppearance); // from `styles.xml`
       // builder.setTypeface(mCustomFontTypeface);
        binding.tvMsg.setText(msg);
        binding.btnCancel.setOnClickListener(v -> {
                    if (context instanceof CompleteClinicReservationActivity) {
                        CompleteClinicReservationActivity completeClinicReservationActivity = (CompleteClinicReservationActivity) context;
                        Intent intent=((CompleteClinicReservationActivity) context).getIntent();
                        ((CompleteClinicReservationActivity) context).setResult(RESULT_OK,intent);
                        ((CompleteClinicReservationActivity) context).finish();
                    } else if (msg.equals(context.getResources().getString(R.string.please_sign_in_or_sign_up))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((AppCompatActivity) context).finishAffinity();
                    }
                    dialog.dismiss();
                }

        );
        binding.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   mTourGuideHandler.cleanUp();

                setClipboard(context,"01030364543");
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}