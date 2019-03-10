package com.sl.clearpicture.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.sl.clearpicture.R;
import com.sl.clearpicture.api.ConfigURL;
import com.sl.clearpicture.model.ImageObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    List<ImageObject> banners;
    LayoutInflater inflater;
 
    public ImageAdapter(Context context, List<ImageObject> banners) {
        this.context = context;
        this.banners = banners;
    
    }
 
    @Override
    public int getCount() {
        return banners.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
 
        // Declare Variables
        
        ImageView imgflag;
        
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.image_banner_item, container,
                false);
 
      
        // Locate the ImageView in viewpager_item.xml
        imgflag = (ImageView) itemView.findViewById(R.id.imageView1);
        
        
          //imgflag.setImageResource(imageArray[position]);
        if (!(TextUtils.isEmpty(banners.get(position).getId()))) {
            Picasso.with(context)
                    .load(ConfigURL.PRODUCT_IMAGE.replace("{productId}",banners.get(position).getId()))
                    .into(imgflag);
        }
        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);
 
        return itemView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
 
    }
}

