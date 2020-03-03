package com.endpoint.chooseme.activities_fragments.activity_home.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.activities_fragments.activity_add_work.AddWorkActivity;
import com.endpoint.chooseme.activities_fragments.activity_edit_profile.EditProfileActivity;
import com.endpoint.chooseme.activities_fragments.activity_home.HomeActivity;
import com.endpoint.chooseme.adapters.SliderAdapter;
import com.endpoint.chooseme.adapters.WorksProfileAdapter;
import com.endpoint.chooseme.databinding.FragmentProfileBinding;
import com.endpoint.chooseme.models.ServiceModel;
import com.endpoint.chooseme.models.UserModel;
import com.endpoint.chooseme.preferences.Preferences;
import com.endpoint.chooseme.share.Common;
import com.endpoint.chooseme.tags.Tags;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class Fragment_Profile extends Fragment {
    private FragmentProfileBinding binding;
    private HomeActivity activity;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<UserModel.Works> worksList;
    private WorksProfileAdapter adapter;
    private SliderAdapter sliderAdapter;
    private TimerTask timerTask;
    private Timer timer;
    private DatabaseReference dRef;



    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME).child(Tags.TABLE_USER);
        worksList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        if (userModel.getWorksList()!=null)
        {

            worksList.addAll(userModel.getWorksList());

           updateSliderAdapter();
        }
        binding.recView.setLayoutManager(new GridLayoutManager(activity,2));
        adapter = new WorksProfileAdapter(worksList,activity,this);
        binding.recView.setAdapter(adapter);

        String service ="";
        for (ServiceModel serviceModel:userModel.getServiceModelList())
        {
            service = service+serviceModel.getName()+ "-";
        }

        if (service.length()>1)
        {
            binding.tvDeparts.setText(service.substring(0,service.length()-1));

        }else
        {
            binding.tvDeparts.setText(service);

        }


        binding.imageEdit.setOnClickListener(view -> {

            Intent intent = new Intent(activity, EditProfileActivity.class);
            startActivityForResult(intent,100);

        });


        binding.facebookIcon.setOnClickListener(view -> {

            if (userModel.getFacebook().isEmpty())
            {
                Toast.makeText(activity, R.string.not_av, Toast.LENGTH_SHORT).show();
            }else
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userModel.getFacebook()));
                startActivity(intent);
            }

        });

        binding.TwitterIcon.setOnClickListener(view -> {

            if (userModel.getTwitter().isEmpty())
            {
                Toast.makeText(getActivity(), R.string.not_av, Toast.LENGTH_SHORT).show();
            }else
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userModel.getTwitter()));
                startActivity(intent);
            }

        });

        binding.linkedinIcon.setOnClickListener(view -> {

            if (userModel.getLinkedin().isEmpty())
            {
                Toast.makeText(activity, R.string.not_av, Toast.LENGTH_SHORT).show();
            }else
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userModel.getLinkedin()));
                startActivity(intent);
            }

        });

        binding.whatsIcon.setOnClickListener(view -> {

            if (userModel.getWhatsapp().isEmpty())
            {
                Toast.makeText(activity, R.string.not_av, Toast.LENGTH_SHORT).show();
            }else
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+966"+userModel.getWhatsapp()));
                startActivity(intent);
            }

        });

        binding.btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddWorkActivity.class);
            startActivityForResult(intent,200);
        });

    }

    private void updateSliderAdapter() {
        sliderAdapter = new SliderAdapter(worksList,activity);
        binding.pager.setAdapter(sliderAdapter);
        if (worksList.size()>0)
        {
            binding.tvPagerCount.setText("1/"+worksList.size());
        }
        stopTimer();
        if (worksList.size()>1)
        {
            binding.pager.setCurrentItem(0);

            startTimer();
        }
    }

    private void startTimer() {

        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask,6000,6000);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100&&resultCode == Activity.RESULT_OK)
        {
            userModel = preferences.getUserData(activity);
            binding.setModel(userModel);


        }else if (requestCode == 200&&resultCode == Activity.RESULT_OK)
        {

            userModel = preferences.getUserData(activity);
            binding.setModel(userModel);
            worksList.clear();

            binding.view.setVisibility(View.GONE);
            binding.tvPagerCount.setVisibility(View.VISIBLE);
            if (userModel.getWorksList()!=null)
            {
                worksList.addAll(userModel.getWorksList());
                adapter = new WorksProfileAdapter(worksList,activity,this);
                binding.recView.setAdapter(adapter);
                updateSliderAdapter();
            }


        }
    }

    public void setItemDelete(UserModel.Works model, int adapterPosition) {
        stopTimer();
        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        FirebaseStorage.getInstance().getReferenceFromUrl(model.getImage())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    deleteItemFromDatabase(dialog,adapterPosition);

                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    if (e.getMessage()!=null)
                    {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else
                        {
                            Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        }
                });


    }

    private void deleteItemFromDatabase(ProgressDialog dialog, int adapterPosition) {

        worksList.remove(adapterPosition);
        userModel.setWorksList(worksList);


        dRef.child(userModel.getId()).setValue(userModel)
                .addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
                    preferences.create_update_userdata(activity,userModel);
                    binding.setModel(userModel);
                    adapter.notifyDataSetChanged();
                    updateSliderAdapter();
                }).addOnFailureListener(e -> {
                   dialog.dismiss();
                   if (e.getMessage()!=null)
                   {
                       Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

                   }else {
                       Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                   }
                });

    }

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                if (binding.pager.getCurrentItem()<worksList.size()-1)
                {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem()+1);
                    binding.tvPagerCount.setText((binding.pager.getCurrentItem()+1)+"/"+worksList.size());
                }else
                    {
                        binding.pager.setCurrentItem(0);
                        binding.tvPagerCount.setText(1+"/"+worksList.size());

                    }
            });
        }
    }

    private void stopTimer() {
        if (timer!=null&&timerTask!=null)
        {
            timer.purge();
            timer.cancel();
            timerTask.cancel();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }


}
