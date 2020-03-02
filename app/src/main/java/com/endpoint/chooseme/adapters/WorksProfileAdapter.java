package com.endpoint.chooseme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.databinding.WorksProfileRowBinding;
import com.endpoint.chooseme.models.UserModel;

import java.util.List;

public class WorksProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserModel.Works> list;
    private Context context;
    private LayoutInflater inflater;
    public WorksProfileAdapter(List<UserModel.Works> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        WorksProfileRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.works_profile_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        UserModel.Works model = list.get(position);
        myHolder.binding.setModel(model);

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public WorksProfileRowBinding binding;

        public MyHolder(@NonNull WorksProfileRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
