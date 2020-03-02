package com.endpoint.chooseme.activities_fragments.activity_edit_profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.databinding.ActivityEditProfileBinding;
import com.endpoint.chooseme.interfaces.Listeners;
import com.endpoint.chooseme.language.LanguageHelper;
import com.endpoint.chooseme.models.UserModel;
import com.endpoint.chooseme.preferences.Preferences;
import com.endpoint.chooseme.share.Common;
import com.endpoint.chooseme.tags.Tags;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import io.paperdb.Paper;

public class EditProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityEditProfileBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private DatabaseReference dRef;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        initView();
    }


    private void initView() {
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME).child(Tags.TABLE_USER);

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setUserModel(userModel);

        binding.btnUpdate.setOnClickListener(view -> {
            checkData();
        });


    }

    private void checkData() {
        String name = binding.edtName.getText().toString().trim();
        String price = binding.edtPrice.getText().toString().trim();
        String facebook = binding.edtFacebook.getText().toString().trim();
        String whatsApp = binding.edtWhats.getText().toString().trim();
        String twitter = binding.edtTwitter.getText().toString().trim();
        String linkedIn = binding.edtLinkedIn.getText().toString().trim();

        if (!name.isEmpty() &&
                !price.isEmpty()) {

            if (!whatsApp.isEmpty()) {
                //without zero
                if (whatsApp.length() == 9) {
                    binding.edtName.setError(null);
                    binding.edtPrice.setError(null);
                    Common.CloseKeyBoard(this, binding.edtName);
                    updateData(name, price, facebook, whatsApp, twitter, linkedIn);

                } else {
                    binding.edtWhats.setError(getString(R.string.inv_phone));

                }
            } else {
                binding.edtName.setError(null);
                binding.edtPrice.setError(null);
                Common.CloseKeyBoard(this, binding.edtName);
                updateData(name, price, facebook, whatsApp, twitter, linkedIn);
            }


        } else {

            if (name.isEmpty()) {
                binding.edtName.setError(getString(R.string.field_required));

            } else {
                binding.edtName.setError(null);

            }

            if (price.isEmpty()) {
                binding.edtPrice.setError(getString(R.string.field_required));

            } else {
                binding.edtPrice.setError(getString(R.string.field_required));

            }

        }
    }

    private void updateData(String name, String price, String facebook, String whatsApp, String twitter, String linkedIn) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();
        userModel.setName(name);
        userModel.setPrice_in_hour(price);
        userModel.setFacebook(facebook);
        userModel.setWhatsapp(whatsApp);
        userModel.setTwitter(twitter);
        userModel.setLinkedin(linkedIn);

        dRef.child(userModel.getId())
                .setValue(userModel)
                .addOnCompleteListener(task -> {
                    dialog.dismiss();
                    if (task.isSuccessful()) {
                        preferences.create_update_userdata(this,userModel);
                        setResult(RESULT_OK);
                        finish();
                    }

                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
            Log.e("error", e.getMessage() + "_");
        });
    }


    @Override
    public void back() {
        finish();
    }

}
