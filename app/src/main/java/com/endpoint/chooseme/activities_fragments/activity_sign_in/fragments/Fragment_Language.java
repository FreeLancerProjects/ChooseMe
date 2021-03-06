package com.endpoint.chooseme.activities_fragments.activity_sign_in.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.endpoint.chooseme.databinding.FragmentLanguageBinding;

import io.paperdb.Paper;

public class Fragment_Language extends Fragment {
    private String selected_language = "ar";
    private SignInActivity activity;
    private FragmentLanguageBinding binding;



    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_language,container,false);
        initView();
        return binding.getRoot();
    }

    public static Fragment_Language newInstance()
    {
        return new Fragment_Language();
    }
    private void initView() {
        activity = (SignInActivity) getActivity();
        Paper.init(activity);

        binding.rbAr.setOnClickListener((View.OnClickListener) v -> selected_language = "ar"
        );

        binding.rbEn.setOnClickListener((View.OnClickListener) v -> selected_language = "en"
        );
        binding.fab.setOnClickListener((View.OnClickListener) v -> activity.RefreshActivity(selected_language)

        );
    }
}
