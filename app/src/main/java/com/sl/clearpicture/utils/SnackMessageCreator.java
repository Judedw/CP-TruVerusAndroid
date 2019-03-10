package com.sl.clearpicture.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SnackMessageCreator {
    private static final int LENGTH_LONG_EXTENDED = 8000;
    public static void  createSnackBar(String msg, View snackRoot, Context context, int color) {
        Snackbar snackError = Snackbar.make(snackRoot,msg, LENGTH_LONG_EXTENDED);
        ViewGroup group = (ViewGroup) snackError.getView();
        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setMaxLines(5);
        group.setBackgroundColor(ContextCompat.getColor(context, color));
        snackError.show();
    }

    public static void  createSnackBar(SpannableStringBuilder msg, View snackRoot, Context context, int color) {
        Snackbar snackError = Snackbar.make(snackRoot,msg,LENGTH_LONG_EXTENDED);
        ViewGroup group = (ViewGroup) snackError.getView();
        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setMaxLines(5);
        group.setBackgroundColor(ContextCompat.getColor(context, color));
        snackError.show();
    }

    public static void  createSnackBar(Spanned msg, View snackRoot, Context context, int color) {
        Snackbar snackError = Snackbar.make(snackRoot,msg, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackError.getView();
        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setMaxLines(5);
        group.setBackgroundColor(ContextCompat.getColor(context, color));
        snackError.show();
    }
}
