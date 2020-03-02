package com.endpoint.chooseme.activities_fragments.activity_department_users;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_profile.ProfileActivity;
import com.endpoint.chooseme.adapters.UsersAdapter;
import com.endpoint.chooseme.databinding.ActivityDepartmentUsersBinding;
import com.endpoint.chooseme.interfaces.Listeners;
import com.endpoint.chooseme.language.LanguageHelper;
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
import java.util.Locale;

import io.paperdb.Paper;

public class DepartmentUsersActivity extends AppCompatActivity implements Listeners.BackListener{
    private ActivityDepartmentUsersBinding binding;
    private String lang;
    private DatabaseReference dRef;
    private ServiceModel serviceModel;
    private Preferences preferences;
    private UserModel userModel;
    private List<UserModel> userModelList;
    private UsersAdapter adapter;





    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_department_users);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("data"))
        {
            serviceModel = (ServiceModel) intent.getSerializableExtra("data");

        }
    }


    private void initView()
    {
        userModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME).child(Tags.TABLE_USER);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);

        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsersAdapter(userModelList,this,null);
        binding.recView.setAdapter(adapter);

        getUsers();


    }

    private void getUsers() {

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.progBar.setVisibility(View.GONE);
                userModelList.clear();
                if (dataSnapshot.getValue()!=null)
                {
                    for (DataSnapshot ds :dataSnapshot.getChildren())
                    {
                        UserModel model = ds.getValue(UserModel.class);

                        if (model!=null)
                        {
                            if (userModel==null)
                            {
                                if (isUserHasDepartment(model))
                                {
                                    userModelList.add(model);
                                }
                            }else
                            {
                                if (!userModel.getId().equals(model.getId()))
                                {
                                    if (isUserHasDepartment(model))
                                    {
                                        userModelList.add(model);
                                    }
                                }
                            }
                        }


                    }

                    if (userModelList.size()>0)
                    {
                        binding.tvNoData.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }else
                        {
                            binding.tvNoData.setVisibility(View.VISIBLE);

                        }
                }else
                    {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private boolean isUserHasDepartment(UserModel userModel)
    {
        for (ServiceModel model :userModel.getServiceModelList())
        {
            if (model.getId() ==serviceModel.getId())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void back() {
        finish();
    }

    public void setItemData(UserModel model) {

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("data",model);
        startActivity(intent);
    }
}

