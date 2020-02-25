package com.endpoint.chooseme.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_home.fragments.Fragment_Department;
import com.endpoint.chooseme.activities_fragments.activity_home.fragments.Fragment_Home;
import com.endpoint.chooseme.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.endpoint.chooseme.activities_fragments.activity_home.fragments.Fragment_Setting;
import com.endpoint.chooseme.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.endpoint.chooseme.databinding.ActivityHomeBinding;
import com.endpoint.chooseme.language.LanguageHelper;
import com.endpoint.chooseme.models.UserModel;
import com.endpoint.chooseme.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_Department fragment_department;
    private Fragment_Setting fragment_setting;
    private Fragment_Profile fragment_profile;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState==null)
        {
            DisplayFragmentHome();
        }
        initView();
        setUpBottomNavigation();



    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

    }

    private void setUpBottomNavigation()
    {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.depts), R.drawable.ic_squares);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.settings), R.drawable.ic_setting);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.profile), R.drawable.ic_user);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.ahBottomNav.setTitleTextSizeInSp(14, 12);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.black));
        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);
        binding.ahBottomNav.addItem(item4);

        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:
                    DisplayFragmentHome();
                    break;
                case 1:
                    DisplayFragmentDepartments();
                    break;
                case 2:
                    DisplayFragmentSetting();
                    break;
                case 3:
                    DisplayFragmentProfile();
                    break;


            }
            return false;
        });

        binding.ahBottomNav.setCurrentItem(0, false);


    }


    private void DisplayFragmentHome() {
        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_department != null && fragment_department.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_department).commit();
        }

        if (fragment_setting != null && fragment_setting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_setting).commit();
        }

        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }


        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();

        }
        binding.ahBottomNav.setCurrentItem(0, false);
        binding.tvTitle.setText(getString(R.string.home));
    }

    public void DisplayFragmentDepartments() {

        if (fragment_department == null) {
            fragment_department = Fragment_Department.newInstance();
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_setting != null && fragment_setting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_setting).commit();
        }

        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }


        if (fragment_department.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_department).commit();


        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_department, "fragment_department").addToBackStack("fragment_department").commit();

        }


        binding.ahBottomNav.setCurrentItem(1, false);
        binding.tvTitle.setText(getString(R.string.depts));


    }

    private void DisplayFragmentSetting() {


        if (fragment_setting == null) {
            fragment_setting = Fragment_Setting.newInstance();
        }

        if (fragment_home != null && fragment_home.isAdded()) {

            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_department != null && fragment_department.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_department).commit();
        }

        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }


        if (fragment_setting.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_setting).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_setting, "fragment_setting").addToBackStack("fragment_setting").commit();

        }
        binding.ahBottomNav.setCurrentItem(2, false);
        binding.tvTitle.setText(getString(R.string.settings));


    }

    private void DisplayFragmentProfile() {

        if (fragment_profile == null) {
            fragment_profile = Fragment_Profile.newInstance();
        }

        if (fragment_home != null && fragment_home.isAdded()) {

            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_department != null && fragment_department.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_department).commit();
        }

        if (fragment_setting != null && fragment_setting.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_setting).commit();
        }


        if (fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
        binding.ahBottomNav.setCurrentItem(3, false);
        binding.tvTitle.setText(getString(R.string.profile));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    @Override
    public void onBackPressed() {

        if (fragment_home!=null&&fragment_home.isVisible())
        {
            if (userModel==null){
                finish();
            }else
                {
                    Intent intent = new Intent(this, SignInActivity.class);
                    navigateUpTo(intent);
                }
        }else
            {
                DisplayFragmentHome();
            }
    }
}
