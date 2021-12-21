package com.white.ui.activity_edit_profile;

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
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.white.R;
import com.white.adapters.DiseasesAdapter;
import com.white.adapters.SpinnerAdapter;
import com.white.adapters.SpinnerDiseasesAdapter;
import com.white.databinding.ActivityEditprofileBinding;
import com.white.databinding.DialogSelectImageBinding;
import com.white.language.Language;
import com.white.models.DiseaseModel;
import com.white.models.EditProfileModel;
import com.white.models.UserModel;
import com.white.mvp.activity_editprofile_mvp.ActivityEditprofilePresenter;
import com.white.mvp.activity_editprofile_mvp.EditprofileActivityView;
import com.white.preferences.Preferences;
import com.white.share.Common;
import com.white.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class EditprofileActivity extends AppCompatActivity implements EditprofileActivityView {
    private ActivityEditprofileBinding binding;
    private EditProfileModel model;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri uri = null;
    private List<String> genderList;
    private List<String> bloodList;
    private SpinnerAdapter genderAdapter, bloodAdapter;
    private ActivityEditprofilePresenter presenter;
    private List<DiseaseModel> diseaseModelList;
    private List<DiseaseModel> selectedDiseasesList;
    private DiseasesAdapter adapter;
    private SpinnerDiseasesAdapter spinnerDiseasesAdapter;
    private android.app.AlertDialog dialog;
    private ProgressDialog dialog2;
    private double lat = 0.0, lng = 0.0;
    private String lang;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editprofile);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        model = new EditProfileModel();
        updatedata();
        binding.setModel(model);
        binding.setLang(lang);
        presenter = new ActivityEditprofilePresenter(this, this);
        diseaseModelList = new ArrayList<>();
        selectedDiseasesList = new ArrayList<>();
        genderList = new ArrayList<>();
        bloodList = new ArrayList<>();
        genderAdapter = new SpinnerAdapter(genderList, this);
        binding.spinnerGender.setAdapter(genderAdapter);
        bloodAdapter = new SpinnerAdapter(bloodList, this);
        binding.spinnerBlood.setAdapter(bloodAdapter);

        spinnerDiseasesAdapter = new SpinnerDiseasesAdapter(diseaseModelList, this);
        binding.spinnerDiseases.setAdapter(spinnerDiseasesAdapter);

        binding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    model.setGender("");
                } else {
                    if (lang.equals("ar")) {
                        if (i == 1) {
                            model.setGender("male");
                        } else {
                            model.setGender("female");
                        }
                    } else {

                        model.setGender(genderList.get(i));
                    }
                }
                binding.setModel(model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerBlood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    model.setBlood_type("");
                } else {
                    model.setBlood_type(bloodList.get(i));
                }
                binding.setModel(model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerDiseases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DiseaseModel model = diseaseModelList.get(i);
                if (model.getId() != 0) {
                    if (!isItemInDiseasesList(model)) {
                        selectedDiseasesList.add(model);
                        adapter.notifyItemInserted(selectedDiseasesList.size() - 1);
                        EditprofileActivity.this.model.setDiseaseModelList(selectedDiseasesList);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter = new DiseasesAdapter(selectedDiseasesList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recView.setAdapter(adapter);

        binding.flSelectImage.setOnClickListener(view -> {
            dialog.show();
        });
        binding.checkbox.setOnClickListener(view -> {
            if (binding.checkbox.isChecked()) {
                model.setHave_diseases(true);
                binding.recView.setVisibility(View.VISIBLE);
                binding.cardViewDiseases.setVisibility(View.VISIBLE);
            } else {
                model.setHave_diseases(false);
                binding.recView.setVisibility(View.GONE);
                binding.spinnerDiseases.setSelection(0);
                binding.cardViewDiseases.setVisibility(View.GONE);
                selectedDiseasesList.clear();
                adapter.notifyDataSetChanged();

            }
            binding.setModel(model);
        });
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.backPress();
            }
        });
        binding.llDate.setOnClickListener(view -> {
            presenter.showDateDialog(getFragmentManager());
        });

        binding.btnSignUp.setOnClickListener(view -> {
            presenter.checkData(model, userModel);
        });


        createImageDialogAlert();
        presenter.getDiseases();
        presenter.getGender();
        presenter.getBloodType();
    }

    private void updatedata() {
        model.setBirth_date(userModel.getData().getBirth_day());
        model.setBlood_type(userModel.getData().getBlood_type());
        model.setFat(userModel.getData().getFat_percentage());
        model.setWeight(userModel.getData().getWeight());
        model.setHeight(userModel.getData().getLength());
        if (userModel.getData().getUsers_with_diseases() != null && userModel.getData().getUsers_with_diseases().size() > 0) {
            model.setDiseaseModelList(userModel.getData().getUsers_with_diseases());
            model.setHave_diseases(true);
            binding.checkbox.setChecked(true);
            binding.cardViewDiseases.setVisibility(View.VISIBLE);
            binding.recView.setVisibility(View.VISIBLE);
        }
        model.setGender(userModel.getData().getGender());
        model.setName(userModel.getData().getName());
        model.setPhone(userModel.getData().getPhone());
        model.setPhone_code(userModel.getData().getPhone_code());

        if (userModel.getData().getLogo() != null) {
            Picasso.get().load(Tags.IMAGE_URL + userModel.getData().getLogo()).resize(720, 480).onlyScaleDown().into(binding.image);
            binding.icon.setVisibility(View.GONE);
        }
    }

    private void createImageDialogAlert() {
        dialog = new AlertDialog.Builder(this)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);
        binding.btnCamera.setOnClickListener(view -> {
            dialog.dismiss();
            checkCameraPermission();
        });
        binding.btnGallery.setOnClickListener(view -> {
            dialog.dismiss();
            checkReadPermission();
        });
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {

            uri = data.getData();
            File file = new File(Common.getImagePath(this, uri));
            Picasso.get().load(file).fit().into(binding.image);
            binding.icon.setVisibility(View.GONE);
            model.setImageUrl(uri.toString());
            binding.setModel(model);

        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = getUriFromBitmap(bitmap);
            binding.icon.setVisibility(View.GONE);

            if (uri != null) {
                model.setImageUrl(uri.toString());
                binding.setModel(model);
                String path = Common.getImagePath(this, uri);
                if (path != null) {
                    Picasso.get().load(new File(path)).fit().into(binding.image);

                } else {
                    Picasso.get().load(uri).fit().into(binding.image);

                }
            }


        }

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }

    private boolean isItemInDiseasesList(DiseaseModel diseaseModel) {
        for (DiseaseModel model : selectedDiseasesList) {
            if (diseaseModel.getId() == model.getId()) {
                return true;
            }
        }
        return false;
    }

    private int getItemSpinnerPos(DiseaseModel diseaseModel) {
        int pos = -1;
        for (int index = 0; index < diseaseModelList.size(); index++) {
            DiseaseModel model = diseaseModelList.get(index);
            if (model.getId() == diseaseModel.getId()) {
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    public void deleteSelectedDisease(int adapterPosition) {
        selectedDiseasesList.remove(adapterPosition);
        adapter.notifyItemRemoved(adapterPosition);
        if (selectedDiseasesList.size() > 0) {
            int pos = getItemSpinnerPos(selectedDiseasesList.get(selectedDiseasesList.size() - 1));

            if (pos != -1) {
                binding.spinnerDiseases.setSelection(pos);
            } else {
                binding.spinnerDiseases.setSelection(0);
            }
        } else {
            binding.spinnerDiseases.setSelection(0);

        }
    }

    @Override
    public void onGenderSuccess(List<String> genderList) {
        this.genderList.clear();
        this.genderList.addAll(genderList);
        genderAdapter.notifyDataSetChanged();
        if (userModel.getData().getGender().equals("male")) {
            binding.spinnerGender.setSelection(1);
        } else {
            binding.spinnerGender.setSelection(2);
        }
        genderAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBloodSuccess(List<String> bloodList) {
        this.bloodList.clear();
        this.bloodList.addAll(bloodList);
        bloodAdapter.notifyDataSetChanged();
        for (int i = 0; i < bloodList.size(); i++) {
            if (bloodList.get(i).equals(userModel.getData().getBlood_type())) {
                binding.spinnerBlood.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onDiseasesSuccess(List<DiseaseModel> diseaseModelList) {
        this.diseaseModelList.add(new DiseaseModel(getResources().getString(R.string.select_disease)));
        this.diseaseModelList.addAll(diseaseModelList);
        runOnUiThread(() -> spinnerDiseasesAdapter.notifyDataSetChanged());
        Log.e("ldldld", userModel.getData().getUsers_with_diseases().size() + "");
        for (int i = 0; i < userModel.getData().getUsers_with_diseases().size(); i++) {
            DiseaseModel model = userModel.getData().getUsers_with_diseases().get(i);
            if (!isItemInDiseasesList(model)) {
                selectedDiseasesList.add(model);
                adapter.notifyItemInserted(selectedDiseasesList.size() - 1);
                EditprofileActivity.this.model.setDiseaseModelList(selectedDiseasesList);
            }

        }
    }

    @Override
    public void onDateSelected(String date) {
        binding.tvDate.setText(date);
        model.setBirth_date(date);
        binding.setModel(model);
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
    public void onFinished() {
        finish();
    }

    @Override
    public void onupdateValid(UserModel body) {
        preferences.create_update_userdata(EditprofileActivity.this, body);
        finish();
    }


    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onnotconnect(String msg) {
        Toast.makeText(EditprofileActivity.this, msg, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onFailed() {
        Toast.makeText(EditprofileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServer() {
        Toast.makeText(EditprofileActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

    }
}