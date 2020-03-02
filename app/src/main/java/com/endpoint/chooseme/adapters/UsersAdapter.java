package com.endpoint.chooseme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_department_users.DepartmentUsersActivity;
import com.endpoint.chooseme.activities_fragments.activity_home.fragments.Fragment_Home;
import com.endpoint.chooseme.databinding.UserRowBinding;
import com.endpoint.chooseme.models.UserModel;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private AppCompatActivity appCompatActivity;
    public UsersAdapter(List<UserModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        appCompatActivity = (AppCompatActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        UserRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        UserModel model = list.get(position);
        myHolder.binding.setModel(model);



        myHolder.itemView.setOnClickListener(view -> {
            UserModel model2 = list.get(myHolder.getAdapterPosition());

            if (fragment!=null&&fragment instanceof Fragment_Home)
            {

                Fragment_Home fragment_home = (Fragment_Home) fragment;
                fragment_home.setUserItemData(model2);
            }else if (fragment==null&&appCompatActivity instanceof DepartmentUsersActivity)
            {
                DepartmentUsersActivity activity = (DepartmentUsersActivity) appCompatActivity;
                activity.setItemData(model2);
            {

            }
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public UserRowBinding binding;

        public MyHolder(@NonNull UserRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
