package com.endpoint.chooseme.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_home.HomeActivity;
import com.endpoint.chooseme.adapters.DepartmentAdapter;
import com.endpoint.chooseme.databinding.FragmentHomeBinding;
import com.endpoint.chooseme.models.ServiceModel;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    private HomeActivity activity;
    private String lang;
    private DepartmentAdapter departmentAdapter;
    private List<ServiceModel>serviceModelList;



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
        serviceModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        addService();
        binding.recViewService.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        departmentAdapter = new DepartmentAdapter(serviceModelList,activity,this);
        binding.recViewService.setAdapter(departmentAdapter);


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


}
