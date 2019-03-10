package com.sl.clearpicture.utils;

import android.content.Context;
import android.text.TextUtils;
import com.sl.clearpicture.model.FieldError;


/**
 * Created by Jamie on 12/02/2017.
 */

public class FieldValidator {

    public static final String NO_ERROR = "NO_ERROR";
    public static final String ERROR_EMPTY = "EMPTY";
    public static final String ERROR_INVALID_EMAIL = "INVALID_EMAIL";
    public static final String ERROR_INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String ERROR_PASSWORD_MISMATCH = "PASSWORD_MISMATCH";
    public static final String ERROR_INVALID_PHONE_NUMBER = "INVALID_PHONE_NUMBER";
    public static final String ERROR_SHORT_PASSWORD = "ERROR_SHORT_PASSWORDD";

    public static FieldError isFieldEmpty(String string){
        boolean isValid = false;
        String error = NO_ERROR;
        if(!string.isEmpty()){
            isValid = true;
            error = NO_ERROR;
        }else{
            isValid = false;
            error = ERROR_EMPTY;
        }
        return new FieldError.Builder().setReason(error).setValidation(isValid).create();
    }

    public static FieldError isValidEmail(CharSequence s){
        boolean isValid = false;
        String error = NO_ERROR;
        if(TextUtils.isEmpty(s)){
            isValid = false;
            error = ERROR_EMPTY;
        }else if(s.length() > 64){
            isValid = false;
            error = ERROR_INVALID_EMAIL;
        }else if( !android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()){
            isValid = false;
            error = ERROR_INVALID_EMAIL;
        }else{
            isValid = true;
            error = NO_ERROR;
        }
        return new FieldError.Builder().setReason(error).setValidation(isValid).create();
    }

    public static FieldError isValidPasswrod(String s){
        boolean isValid = false;
        String error = NO_ERROR;
        if(s.isEmpty()){
            isValid = false;
            error = ERROR_EMPTY;
        }else if(s.length() < 8 || s.length() > 16){
            isValid = false;
            error = ERROR_SHORT_PASSWORD;
        }else{
            isValid = true;
            error = NO_ERROR;
        }
        return new FieldError.Builder().setReason(error).setValidation(isValid).create();
    }

    public static FieldError isPasswordMatch(String first,String second){
        boolean isValid = false;
        String error = NO_ERROR;
        if(!first.equals(second)){
            isValid = false;
            error = ERROR_PASSWORD_MISMATCH;
        }else{
            isValid = true;
            error = NO_ERROR;
        }
        return new FieldError.Builder().setReason(error).setValidation(isValid).create();
    }

//    public static FieldError isValidPhoneNumber(String phNumber,String countryCode, Context context) {
//        boolean isValid = false;
//        String error = NO_ERROR;
//
//        if (isFieldEmpty(phNumber).isValidation()) {
//            boolean isValidNumber = processPhoneNumber(phNumber,countryCode, context);
//            if (!isValidNumber) {
//                isValid = false;
//                error = ERROR_INVALID_PHONE_NUMBER;
//            } else {
//                isValid = true;
//                error = NO_ERROR;
//            }
//        } else {
//            isValid = false;
//            error = ERROR_EMPTY;
//        }
//        return new FieldError.Builder().setReason(error).setValidation(isValid).create();
//    }

//    private static boolean processPhoneNumber(String phNumber,String countryCode, Context context) {
//
//
//        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//        String iso = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
//        Phonenumber.PhoneNumber phoneNumber = null;
//        try {
//            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
//            phoneNumber = phoneNumberUtil.parse(phNumber, iso);
//        } catch (NumberParseException e) {
//            System.err.println(e);
//        }
//        boolean isvalid = false;
//        if (phoneNumber == null) {
//            isvalid = false;
//        }else {
//            isvalid = phoneNumberUtil.isValidNumber(phoneNumber);
//        }
//        return isvalid;
//    }

    public static FieldError isValidPrefix(String string){
        boolean isValid = false;
        String error = NO_ERROR;
        if(!string.trim().equals("Title")){
            isValid = true;
            error = NO_ERROR;
        }else{
            isValid = false;
            error = ERROR_EMPTY;
        }
        return new FieldError.Builder().setReason(error).setValidation(isValid).create();
    }

    public static FieldError isValidSuffix(String string){
        boolean isValid = false;
        String error = NO_ERROR;
        if(!string.trim().equals("Tap to select")){
            isValid = true;
            error = NO_ERROR;
        }else{
            isValid = false;
            error = ERROR_EMPTY;
        }
        return new FieldError.Builder().setReason(error).setValidation(isValid).create();
    }
}
