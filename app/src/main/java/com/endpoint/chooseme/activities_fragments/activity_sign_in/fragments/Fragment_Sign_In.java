package com.endpoint.chooseme.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.endpoint.chooseme.databinding.FragmentSignInBinding;
import com.endpoint.chooseme.interfaces.Listeners;
import com.endpoint.chooseme.models.LoginModel;
import com.endpoint.chooseme.preferences.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Sign_In extends Fragment implements Listeners.LoginListener, Listeners.CreateAccountListener {
    private FragmentSignInBinding binding;
    private SignInActivity activity;
    private String current_language;
    private Preferences preferences;
    private LoginModel loginModel;


    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        loginModel = new LoginModel();
        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_language);
        binding.setLoginListener(this);
        binding.setCreateAccountListener(this);
        binding.setLoginModel(loginModel);


    }


    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }


    @Override
    public void checkDataLogin(String email, String password) {

    }


    private void navigateToHomeActivity() {
//        Intent intent = new Intent(activity, Ho.class);
//        startActivity(intent);
//        activity.finish();
    }

    @Override
    public void createNewAccount() {
        activity.DisplayFragmentSignUp();
    }


}
