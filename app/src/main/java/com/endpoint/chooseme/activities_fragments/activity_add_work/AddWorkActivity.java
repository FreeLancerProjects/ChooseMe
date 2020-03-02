package com.endpoint.chooseme.activities_fragments.activity_add_work;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.databinding.ActivityAddWorkBinding;
import com.endpoint.chooseme.interfaces.Listeners;
import com.endpoint.chooseme.language.LanguageHelper;
import com.endpoint.chooseme.models.UserModel;
import com.endpoint.chooseme.preferences.Preferences;
import com.endpoint.chooseme.share.Common;
import com.endpoint.chooseme.tags.Tags;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.paperdb.Paper;

public class AddWorkActivity extends AppCompatActivity implements Listeners.BackListener{
    private ActivityAddWorkBinding binding;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private DatabaseReference dRef;
    private StorageReference sRef;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int read_req = 1000;
    private Uri uri = null;
    






    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_work);
        initView();
    }



    private void initView()
    {
        sRef = FirebaseStorage.getInstance().getReference();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME).child(Tags.TABLE_USER);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);

        binding.image.setOnClickListener(view -> checkPermission());
        
        binding.btnAdd.setOnClickListener(view -> {
            String title = binding.edtTitle.getText().toString().trim();
            String price = binding.edtPrice.getText().toString().trim();
            
            if (!title.isEmpty()&&
                    !price.isEmpty()&&
                    uri !=null
            ){

                binding.edtTitle.setError(null);
                binding.edtPrice.setError(null);
                Common.CloseKeyBoard(this,binding.edtTitle);

                uploadImage(title,price);
                
            }else 
                {
                    
                    if (title.isEmpty())
                    {
                        binding.edtTitle.setError(getString(R.string.field_required));

                    }else 
                        {
                            binding.edtTitle.setError(null);

                        }

                    if (price.isEmpty())
                    {
                        binding.edtPrice.setError(getString(R.string.field_required));

                    }else
                    {
                        binding.edtPrice.setError(null);

                    }


                    if (uri==null)
                    {
                        Toast.makeText(this, R.string.ch_img, Toast.LENGTH_SHORT).show();
                    }
                    
                }
            
        });
        

    }

    private void uploadImage(String title, String price) {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        StorageReference ref = sRef.child("images").child(userModel.getId()).child(UUID.randomUUID().toString());
        ref.putFile(uri).addOnSuccessListener(taskSnapshot -> {

            ref.getDownloadUrl().addOnSuccessListener(uri -> updateUserData(uri.toString(),title,price,dialog));

        }).addOnFailureListener(e -> {
            dialog.dismiss();
            if (e.getMessage()!=null)
            {
                Log.e("error",e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }else
                {
                    Toast.makeText(this,getString(R.string.failed), Toast.LENGTH_SHORT).show();

                }
        });

    }

    private void updateUserData(String downLoadImageUrl, String title, String price, ProgressDialog dialog) {

        UserModel.Works works = new UserModel.Works(downLoadImageUrl,price,title,0.0);

        if (userModel.getWorksList()!=null)
        {
            userModel.getWorksList().add(works);

        }else
            {
                List<UserModel.Works> worksList = new ArrayList<>();
                worksList.add(works);
                userModel.setWorksList(worksList);
            }

        dRef.child(userModel.getId())
                .setValue(userModel)
                .addOnCompleteListener(task -> {
                    dialog.dismiss();
                    if (task.isSuccessful())
                    {
                        preferences.create_update_userdata(this,userModel);
                        setResult(RESULT_OK);
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    if (e.getMessage()!=null)
                    {
                        Log.e("error",e.getMessage());
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this,read_permission)==PackageManager.PERMISSION_GRANTED)
        {
            openGallery();
        }else
            {
                ActivityCompat.requestPermissions(this,new String[]{read_permission},read_req);

            }
    }

    private void openGallery() {

        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        }else
            {
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            }

        intent.setType("image/*");
        startActivityForResult(intent,2000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==read_req)
        {
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                openGallery();
            }else
                {
                    Toast.makeText(this, R.string.perm_img_denied,Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2000&&resultCode==RESULT_OK&&data!=null)
        {
            uri = data.getData();
            if (uri!=null)
            {
                Picasso.with(this).load(uri).fit().into(binding.image);
                binding.llIcon.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void back() {
        finish();
    }

}

