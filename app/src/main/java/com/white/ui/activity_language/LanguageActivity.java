package com.white.ui.activity_language;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.white.R;
import com.white.databinding.ActivityLanguageBinding;
import com.white.language.Language;

import io.paperdb.Paper;

public class LanguageActivity extends AppCompatActivity {
    private ActivityLanguageBinding binding;
    private String lang = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Transition transition = new Fade();
            transition.setInterpolator(new LinearInterpolator());
            transition.setDuration(500);
            getWindow().setEnterTransition(transition);
            getWindow().setExitTransition(transition);

        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_language);
        initView();
    }

    private void initView() {
        binding.tv1.setText(Html.fromHtml(getString(R.string.choose_language)));
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        if (lang.equals("ar")){
            binding.flAr.setBackgroundResource(R.drawable.small_rounded_red_strock);
            binding.flEn.setBackgroundResource(0);
        }else {
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_rounded_red_strock);
        }
        binding.btnNext.setVisibility(View.VISIBLE);

        binding.cardAr.setOnClickListener(view -> {
            lang = "ar";
            binding.flAr.setBackgroundResource(R.drawable.small_rounded_red_strock);
            binding.flEn.setBackgroundResource(0);
            binding.btnNext.setVisibility(View.VISIBLE);

        });

        binding.cardEn.setOnClickListener(view -> {
            lang = "en";
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_rounded_red_strock);
            binding.btnNext.setVisibility(View.VISIBLE);
        });

        binding.btnNext.setOnClickListener(view -> {
            Intent intent = getIntent();
            intent.putExtra("lang",lang);
            setResult(RESULT_OK,intent);
            onBackPressed();
        });
    }
}