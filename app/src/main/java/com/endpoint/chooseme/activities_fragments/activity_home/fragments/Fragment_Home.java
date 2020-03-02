package com.endpoint.chooseme.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_department_users.DepartmentUsersActivity;
import com.endpoint.chooseme.activities_fragments.activity_home.HomeActivity;
import com.endpoint.chooseme.activities_fragments.activity_profile.ProfileActivity;
import com.endpoint.chooseme.adapters.DepartmentAdapter;
import com.endpoint.chooseme.adapters.UsersAdapter;
import com.endpoint.chooseme.databinding.FragmentHomeBinding;
import com.endpoint.chooseme.models.ServiceModel;
import com.endpoint.chooseme.models.UserModel;
import com.endpoint.chooseme.preferences.Preferences;
import com.endpoint.chooseme.tags.Tags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    private HomeActivity activity;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private DepartmentAdapter departmentAdapter;
    private List<ServiceModel>serviceModelList;
    private DatabaseReference dRef;
    private List<UserModel> userModelList;
    private UsersAdapter usersAdapter;



    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME);
        serviceModelList = new ArrayList<>();
        userModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        addService();
        binding.recViewService.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        departmentAdapter = new DepartmentAdapter(serviceModelList,activity,this);
        binding.recViewService.setAdapter(departmentAdapter);
        binding.recViewUsers.setLayoutManager(new LinearLayoutManager(activity));
        usersAdapter = new UsersAdapter(userModelList,activity,this);
        binding.recViewUsers.setAdapter(usersAdapter);

        binding.tvAll.setOnClickListener(view -> activity.DisplayFragmentDepartments());




        getUsers();


    }

    private void getUsers() {
        dRef.child(Tags.TABLE_USER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        binding.progBar.setVisibility(View.GONE);
                        if (dataSnapshot.getValue()!=null)
                        {
                            for (DataSnapshot ds :dataSnapshot.getChildren())
                            {
                                UserModel model = ds.getValue(UserModel.class);
                                if (model!=null)
                                {

                                    if (userModel!=null)
                                    {
                                        if (!userModel.getId().equals(model.getId()))
                                        {
                                            userModelList.add(model);
                                        }
                                    }else
                                        {
                                            userModelList.add(model);

                                        }

                                }
                            }

                            if (userModelList.size()>0)
                            {
                                binding.tvUsersService.setVisibility(View.GONE);
                                usersAdapter.notifyDataSetChanged();

                            }else
                                {
                                    binding.tvUsersService.setVisibility(View.VISIBLE);

                                }

                        }else
                            {
                                binding.tvUsersService.setVisibility(View.VISIBLE);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void addService() {
        ServiceModel model1 = new ServiceModel(1, "تسويق إلكتروني");
        model1.setImage_resource(R.drawable.service_market);
        serviceModelList.add(model1);

        ServiceModel model2 = new ServiceModel(2, "تصميم جرافيك");
        model2.setImage_resource(R.drawable.service_graphic);
        serviceModelList.add(model2);
        ServiceModel model3 = new ServiceModel(3, "كارت شخصي");
        model3.setImage_resource(R.drawable.service_card);
        serviceModelList.add(model3);

        ServiceModel model4 = new ServiceModel(4, "تصميم لوجو");
        model4.setImage_resource(R.drawable.service_logo);
        serviceModelList.add(model4);
        ServiceModel model5 = new ServiceModel(5, "تصميم مواقع تواصل");
        model5.setImage_resource(R.drawable.service_social);
        serviceModelList.add(model5);
    }


    public void setItemData(ServiceModel model) {
        Intent intent = new Intent(activity, DepartmentUsersActivity.class);
        intent.putExtra("data",model);
        startActivity(intent);
    }

    public void setUserItemData(UserModel model) {

        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra("data",model);
        startActivity(intent);
    }
}
