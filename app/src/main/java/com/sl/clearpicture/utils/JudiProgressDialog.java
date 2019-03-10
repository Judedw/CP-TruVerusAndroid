package com.sl.clearpicture.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.sl.clearpicture.R;


/**
 * Created by Ramesh on 14/03/2018.
 */

public class JudiProgressDialog extends ProgressDialog {
    private AnimationDrawable animation;
    private LottieAnimationView la;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.judi_progress_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        la = (LottieAnimationView) findViewById(R.id.animation_view);
        la.setAnimation("loading_animation.json");
        la.loop(true);

    }

    public JudiProgressDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        try {
            super.show();
            la.playAnimation();
        }catch (Exception e){

        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
            la.clearAnimation();
        }catch (Exception e){

        }
    }
}
