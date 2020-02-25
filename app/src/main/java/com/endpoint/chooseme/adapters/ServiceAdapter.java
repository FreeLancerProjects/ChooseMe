package com.endpoint.chooseme.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.databinding.SpinnerRowBinding;
import com.endpoint.chooseme.models.ServiceModel;

import java.util.List;

public class ServiceAdapter extends BaseAdapter {
    private Context context;
    private List<ServiceModel> serviceList;
    private LayoutInflater inflate;

    public ServiceAdapter(Context context, List<ServiceModel> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
        inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int i) {
        return serviceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(inflate, R.layout.spinner_row,viewGroup,false);
        binding.setName(serviceList.get(i).getName());
        return binding.getRoot();
    }
}
