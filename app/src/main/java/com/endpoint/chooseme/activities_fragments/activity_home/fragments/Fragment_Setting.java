package com.endpoint.chooseme.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_home.HomeActivity;
import com.endpoint.chooseme.databinding.FragmentSettingBinding;

import io.paperdb.Paper;

public class Fragment_Setting extends Fragment {
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


    }






}
