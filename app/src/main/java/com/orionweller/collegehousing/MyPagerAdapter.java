package com.orionweller.collegehousing;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
    private Context context;
    private String apartmentName;
    private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };


    MyPagerAdapter(Context context, String apartmentName) {
        this.context = context;
        this.apartmentName = apartmentName;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String strippedApartmentName = apartmentName.replaceAll("\\s+", "");
        String urlString = "http://orionweller.com/photos/" + strippedApartmentName + String.valueOf(position + 1) + ".jpg";
        Log.d("urlString", urlString);
        ImageView imageView = new ImageView(context);
        Picasso.get()
                .load(urlString)
                .fit()
                .centerCrop()
                .noFade()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.noimage)
                .into(imageView);
        container.addView(imageView);
        return imageView;
//
//        ImageView imageView = new ImageView(context);
//        Picasso.get()
//                .load(imageUrls[position])
//                .fit()
//                .centerCrop()
//                .into(imageView);
//        container.addView(imageView);
//
//        return imageView;
    }

        @Override
        public void destroyItem (@NonNull ViewGroup container,int position, @NonNull Object object){
            container.removeView((View) object);
        }
    }
