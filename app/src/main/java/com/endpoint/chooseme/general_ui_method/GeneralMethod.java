package com.endpoint.chooseme.general_ui_method;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;


public class GeneralMethod {



    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("imageResource")
    public static void displayImageResource(View view, int resource) {
        if (view instanceof RoundedImageView) {

            RoundedImageView imageView = (RoundedImageView) view;
            imageView.setImageResource(resource);
        }
    }

    @BindingAdapter("url")
    public static void displayImageUrl(View view, String url) {
        if (view instanceof RoundedImageView) {

            RoundedImageView imageView = (RoundedImageView) view;
            Picasso.with(view.getContext()).load(Uri.parse(url)).fit().into(imageView);
        }
    }








}
