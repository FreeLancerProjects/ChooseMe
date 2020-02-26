package com.endpoint.chooseme.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_about.AboutActivity;
import com.endpoint.chooseme.activities_fragments.activity_home.HomeActivity;
import com.endpoint.chooseme.databinding.DialogLanguageBinding;
import com.endpoint.chooseme.databinding.FragmentSettingBinding;
import com.endpoint.chooseme.interfaces.Listeners;

import io.paperdb.Paper;

public class Fragment_Setting extends Fragment implements Listeners.SettingActionListener {
    private FragmentSettingBinding binding;
    private HomeActivity activity;
    private String lang;



    public static Fragment_Setting newInstance() {
        return new Fragment_Setting();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setAction(this);

    }

    private void createLangDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_language, null, false);
        String lang = Paper.book().read("lang","ar");
        if (lang.equals("ar")) {
            binding.rbAr.setChecked(true);
        } else {
            binding.rbEn.setChecked(true);

        }
        binding.btnCancel.setOnClickListener((v) ->
                dialog.dismiss()

        );
        binding.rbAr.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> {
                        if (!lang.equals("ar"))
                        {
                            activity.refreshActivity("ar");
                        }
                    },200);
        });
        binding.rbEn.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> {
                        if (!lang.equals("en"))
                        {
                            activity.refreshActivity("en");
                        }
                    },200);
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    @Override
    public void contact() {

    }

    @Override
    public void changeLanguage() {
        createLangDialog();
    }

    @Override
    public void about() {
        navigateToActivityAbout(2);

    }

    @Override
    public void rateApp() {
        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Override
    public void terms() {

        navigateToActivityAbout(1);
    }

    private void navigateToActivityAbout(int type) {

        Intent intent = new Intent(activity, AboutActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }

    @Override
    public void logout() {
        activity.logout();
    }



}
