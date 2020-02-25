package com.endpoint.chooseme.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_home.HomeActivity;
import com.endpoint.chooseme.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.endpoint.chooseme.databinding.FragmentSignInBinding;
import com.endpoint.chooseme.interfaces.Listeners;
import com.endpoint.chooseme.models.LoginModel;
import com.endpoint.chooseme.models.UserModel;
import com.endpoint.chooseme.preferences.Preferences;
import com.endpoint.chooseme.share.Common;
import com.endpoint.chooseme.tags.Tags;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Sign_In extends Fragment implements Listeners.CreateAccountListener, Listeners.SkipListener {
    private FragmentSignInBinding binding;
    private SignInActivity activity;
    private String lang;
    private Preferences preferences;
    private LoginModel loginModel;
    private DatabaseReference dRef;


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
        FirebaseApp.initializeApp(activity);
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME);

        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setCreateAccountListener(this);
        binding.setSkipListener(this);
        binding.setLoginModel(loginModel);

        binding.btnSignUp.setOnClickListener(view -> {
            activity.DisplayFragmentSignUp();
        });
        binding.btnLogin.setOnClickListener(view -> {

            if (loginModel.isDataValid(activity)) {
                login();
            }
        });

    }

    private void login() {
        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(loginModel.getEmail(), loginModel.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = task.getResult().getUser().getUid();
                        getUserById(id,dialog);
                    }
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(activity, getString(R.string.inc_em_pas), Toast.LENGTH_SHORT).show();

                    if (e.getMessage() != null) {
                        Log.e("Error", e.getMessage());
                    }
        });

    }

    private void getUserById(String id, ProgressDialog dialog) {

        dRef.child(Tags.TABLE_USER).child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dialog.dismiss();
                        if (dataSnapshot.getValue()!=null)
                        {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            preferences.create_update_userdata(activity,userModel);
                            navigateToHomeActivity();
                        }else
                            {
                                Toast.makeText(activity, R.string.inc_em_pas, Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }




    private void navigateToHomeActivity() {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        activity.finish();
    }

    @Override
    public void createNewAccount() {
        activity.DisplayFragmentSignUp();
    }


    @Override
    public void skip() {
       Intent intent = new Intent(activity,HomeActivity.class);
       startActivity(intent);

    }
}
