package com.endpoint.chooseme.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_home.HomeActivity;
import com.endpoint.chooseme.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.endpoint.chooseme.adapters.MyServiceAdapter;
import com.endpoint.chooseme.adapters.ServiceAdapter;
import com.endpoint.chooseme.databinding.FragmentSignUpBinding;
import com.endpoint.chooseme.interfaces.Listeners;
import com.endpoint.chooseme.models.ServiceModel;
import com.endpoint.chooseme.models.SignUpModel;
import com.endpoint.chooseme.models.UserModel;
import com.endpoint.chooseme.preferences.Preferences;
import com.endpoint.chooseme.share.Common;
import com.endpoint.chooseme.tags.Tags;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Sign_Up extends Fragment implements Listeners.CreateAccountListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private SignInActivity activity;
    private FragmentSignUpBinding binding;
    private CountryPicker countryPicker;
    private Preferences preferences;
    private String lang;
    private SignUpModel signUpModel;
    private ServiceAdapter serviceAdapter;
    private List<ServiceModel> serviceModelList;
    private MyServiceAdapter adapter;
    private List<ServiceModel> selectedServiceList;
    private DatabaseReference dRef;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (SignInActivity) getActivity();

        mAuth = FirebaseAuth.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME);
        selectedServiceList = new ArrayList<>();
        serviceModelList = new ArrayList<>();
        signUpModel = new SignUpModel();
        preferences = Preferences.newInstance();
        binding.setLang(lang);
        binding.setSignUpListener(this);
        binding.setShowCountryListener(this);
        binding.setSignUpModel(signUpModel);
        binding.recViewservice.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MyServiceAdapter(selectedServiceList, activity, this);
        binding.recViewservice.setAdapter(adapter);

        createCountryDialog();

        addService();

        serviceAdapter = new ServiceAdapter(activity, serviceModelList);
        binding.spinnerService.setAdapter(serviceAdapter);

        binding.spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    if (!isServiceSelected(serviceModelList.get(i).getId())) {
                        selectedServiceList.add(serviceModelList.get(i));
                        adapter.notifyDataSetChanged();
                        signUpModel.setServiceModelList(selectedServiceList);
                        binding.setSignUpModel(signUpModel);
                    }
                }else
                    {
                        signUpModel.setServiceModelList(new ArrayList<>());
                        binding.setSignUpModel(signUpModel);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.tvBack.setOnClickListener(view -> {
            activity.Back();
        });


    }

    private void addService() {
        serviceModelList.add(new ServiceModel(0, getString(R.string.dept)));
        serviceModelList.add(new ServiceModel(1, "تسويق إلكتروني"));
        serviceModelList.add(new ServiceModel(2, "تصميم جرافيك"));
        serviceModelList.add(new ServiceModel(3, "كارت شخصي"));
        serviceModelList.add(new ServiceModel(4, "تصميم لوجو"));
        serviceModelList.add(new ServiceModel(5, "تصميم مواقع تواصل"));

    }


    @Override
    public void createNewAccount() {
        if (signUpModel.isDataValid(activity)) {
            signUp();
        }
    }

    private void signUp() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();
        mAuth.setLanguageCode(lang);
        mAuth.createUserWithEmailAndPassword(signUpModel.getEmail(), signUpModel.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = task.getResult().getUser().getUid();
                        UserModel userModel = new UserModel(id,signUpModel.getName(),signUpModel.getEmail(),signUpModel.getPhone(),signUpModel.getPassword(),"","","","","",0.0,signUpModel.getServiceModelList(),new ArrayList<>());
                        saveInDataBase(userModel,dialog);


                    }
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
            if (e.getMessage() != null) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void saveInDataBase(UserModel userModel, ProgressDialog dialog) {
        dRef.child(Tags.TABLE_USER)
                .child(userModel.getId())
                .setValue(userModel)
                .addOnCompleteListener(task -> {
                    dialog.dismiss();
                    if (task.isSuccessful())
                    {
                        preferences.create_update_userdata(activity,userModel);
                        navigateToHomeActivity();
                    }
                }).addOnFailureListener(e -> {
                    if (e.getMessage()!=null)
                    {
                        Toast.makeText(activity,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else
                        {
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                });
    }


    private boolean isServiceSelected(int id) {
        for (ServiceModel serviceModel : selectedServiceList) {
            if (serviceModel.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public static Fragment_Sign_Up newInstance() {
        return new Fragment_Sign_Up();
    }

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(activity)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        if (countryPicker.getCountryFromSIM() != null) {
            updatePhoneCode(countryPicker.getCountryFromSIM());
        } else if (telephonyManager != null && countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
        } else if (countryPicker.getCountryByLocale(Locale.getDefault()) != null) {
            updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
        } else {
            String code = "+966";
            binding.tvCode.setText(code);
            signUpModel.setPhone_code(code);

        }

    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(activity);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);

    }

    private void updatePhoneCode(Country country) {
        binding.tvCode.setText(country.getDialCode());
        signUpModel.setPhone_code(country.getDialCode());

    }


    public void setItemData(int pos) {
        selectedServiceList.remove(pos);
        adapter.notifyItemRemoved(pos);
        signUpModel.setServiceModelList(selectedServiceList);
        binding.setSignUpModel(signUpModel);
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        activity.finish();
    }


}
