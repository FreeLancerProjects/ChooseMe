package com.endpoint.chooseme.activities_fragments.activity_profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.adapters.SliderAdapter;
import com.endpoint.chooseme.adapters.WorksAdapter;
import com.endpoint.chooseme.databinding.ActivityProfileBinding;
import com.endpoint.chooseme.interfaces.Listeners;
import com.endpoint.chooseme.language.LanguageHelper;
import com.endpoint.chooseme.models.ServiceModel;
import com.endpoint.chooseme.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity implements Listeners.BackListener{
    private ActivityProfileBinding binding;
    private String lang;
    private UserModel model;
    private WorksAdapter adapter;
    private List<UserModel.Works> worksList;
    private SliderAdapter sliderAdapter;
    private TimerTask timerTask;
    private Timer timer;





    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("data"))
        {
            model = (UserModel) intent.getSerializableExtra("data");

        }
    }


    private void initView()
    {
        worksList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setModel(model);

        String service ="";
        for (ServiceModel serviceModel:model.getServiceModelList())
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


        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new WorksAdapter(model.getWorksList(),this);
        binding.recView.setAdapter(adapter);

        if (model.getWorksList()!=null)
        {
            worksList.addAll(model.getWorksList());

            updateSliderAdapter();
        }

        binding.facebookIcon.setOnClickListener(view -> {

            if (model.getFacebook().isEmpty())
            {
                Toast.makeText(this, R.string.not_av, Toast.LENGTH_SHORT).show();
            }else
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getFacebook()));
                    startActivity(intent);
                }

        });

        binding.TwitterIcon.setOnClickListener(view -> {

            if (model.getTwitter().isEmpty())
            {
                Toast.makeText(this, R.string.not_av, Toast.LENGTH_SHORT).show();
            }else
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getTwitter()));
                startActivity(intent);
            }

        });

        binding.linkedinIcon.setOnClickListener(view -> {

            if (model.getLinkedin().isEmpty())
            {
                Toast.makeText(this, R.string.not_av, Toast.LENGTH_SHORT).show();
            }else
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getLinkedin()));
                startActivity(intent);
            }

        });

        binding.whatsIcon.setOnClickListener(view -> {

            if (model.getWhatsapp().isEmpty())
            {
                Toast.makeText(this, R.string.not_av, Toast.LENGTH_SHORT).show();
            }else
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+966"+model.getWhatsapp()));
                startActivity(intent);
            }

        });






    }

    private void updateSliderAdapter() {
        sliderAdapter = new SliderAdapter(worksList,this);
        binding.pager.setAdapter(sliderAdapter);

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

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            runOnUiThread(() -> {
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
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();

    }

    @Override
    public void back() {
        finish();
    }

}

