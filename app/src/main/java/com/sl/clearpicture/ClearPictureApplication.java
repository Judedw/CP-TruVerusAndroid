package com.sl.clearpicture;

import android.app.Application;

import com.sl.clearpicture.utils.MyVolley;

public class ClearPictureApplication extends Application{

    public static  ClearPictureApplication instance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MyVolley.init(this);
    }

    public static ClearPictureApplication get(){
        return instance;
    }


}
