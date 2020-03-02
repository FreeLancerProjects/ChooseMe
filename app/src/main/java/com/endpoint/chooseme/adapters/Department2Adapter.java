package com.endpoint.chooseme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_home.fragments.Fragment_Department;
import com.endpoint.chooseme.databinding.DeptRowBinding;
import com.endpoint.chooseme.models.ServiceModel;

import java.util.List;

public class Department2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ServiceModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Department fragment;
    public Department2Adapter(List<ServiceModel> list, Context context, Fragment_Department fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DeptRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.dept_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        ServiceModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.itemView.setOnClickListener(view -> {
            ServiceModel model2 = list.get(myHolder.getAdapterPosition());
            fragment.setItemData(model2);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public DeptRowBinding binding;

        public MyHolder(@NonNull DeptRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
