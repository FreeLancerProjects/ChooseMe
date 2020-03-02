package com.endpoint.chooseme.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;


import com.endpoint.chooseme.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SignUpModel extends BaseObservable implements Serializable {

    private String name;
    private String phone;
    private String email;
    private String password;
    private List<ServiceModel> serviceModelList;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();

    public ObservableField<String> error_password = new ObservableField<>();


    public SignUpModel() {
        serviceModelList = new ArrayList<>();
        this.name = "";
        this.phone = "";
        this.password = "";
        this.email = "";
    }


    public SignUpModel(String name, String phone, String email, String password) {
        setName(name);
        setPhone(phone);
        setEmail(email);
        setPassword(password);


    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);


    }


    @Bindable

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);


    }

    @Bindable

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    public List<ServiceModel> getServiceModelList() {
        return serviceModelList;
    }

    public void setServiceModelList(List<ServiceModel> serviceModelList) {
        this.serviceModelList = serviceModelList;
    }

    public boolean isDataValid(Context context) {
        if (!TextUtils.isEmpty(phone) &&
                phone.length()==9&&
                (password.length() >= 6) &&
                !TextUtils.isEmpty(name) &&!TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                serviceModelList.size()>0


        ) {
            error_name.set(null);
            error_phone_code.set(null);
            error_phone.set(null);
            error_email.set(null);
            error_password.set(null);

            return true;
        } else {
            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);
            }


            if (email.isEmpty()) {
                error_email.set(context.getString(R.string.field_required));
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                error_email.set(context.getString(R.string.inv_email));
            } else {
                error_email.set(null);
            }


            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));
            } else if (phone.length()!=9){
                error_phone.set(context.getString(R.string.inv_phone));

            }else {
                error_phone.set(null);
            }

            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_required));
            } else if (password.length() < 6) {
                error_password.set(context.getString(R.string.pass_short));
            } else {
                error_password.set(null);

            }


            if (serviceModelList.size()==0) {
                Toast.makeText(context, R.string.ch_serv, Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }
}
