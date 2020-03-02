package com.endpoint.chooseme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.endpoint.chooseme.R;
import com.endpoint.chooseme.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private List<UserModel.Works> list;
    private Context context;
    private LayoutInflater inflater;

    public SliderAdapter(List<UserModel.Works> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.slider_row,container,false);
        ImageView image = view.findViewById(R.id.image);
        LinearLayout llIcon = view.findViewById(R.id.llIcon);


        if (list.get(position).getImage().isEmpty())
        {
            llIcon.setVisibility(View.VISIBLE);
        }else
            {
                llIcon.setVisibility(View.GONE);

                Picasso.with(context).load(list.get(position).getImage()).fit().into(image);

            }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
