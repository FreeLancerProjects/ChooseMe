package com.endpoint.chooseme.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_home.HomeActivity;
import com.endpoint.chooseme.adapters.Department2Adapter;
import com.endpoint.chooseme.databinding.FragmentDepartmentBinding;
import com.endpoint.chooseme.models.ServiceModel;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Fragment_Department extends Fragment {
    private FragmentDepartmentBinding binding;
    private HomeActivity activity;
    private String lang;
    private Department2Adapter adapter;
    private List<ServiceModel> serviceModelList;



    public static Fragment_Department newInstance() {
        return new Fragment_Department();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_department,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        serviceModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        addService();
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new Department2Adapter(serviceModelList,activity,this);
        binding.recView.setAdapter(adapter);

    }

    private void addService() {

        ServiceModel model1 = new ServiceModel(1, "تسويق إلكتروني");
        model1.setImage_resource(R.drawable.ic_marketing);
        serviceModelList.add(model1);

        ServiceModel model2 = new ServiceModel(2, "تصميم جرافيك");
        model2.setImage_resource(R.drawable.ic_graphic_design);
        serviceModelList.add(model2);
        ServiceModel model3 = new ServiceModel(3, "كارت شخصي");
        model3.setImage_resource(R.drawable.ic_cards_design);
        serviceModelList.add(model3);

        ServiceModel model4 = new ServiceModel(4, "تصميم لوجو");
        model4.setImage_resource(R.drawable.ic_logo_design);
        serviceModelList.add(model4);
        ServiceModel model5 = new ServiceModel(5, "تصميم مواقع تواصل");
        model5.setImage_resource(R.drawable.ic_connection);
        serviceModelList.add(model5);
    }


}
