package com.sl.clearpicture.api;

public class ConfigURL {

    public static  final String ROOT = "https://authentican60aazs63l.ca1.hana.ondemand.com/";

    public static final String VERIFY_TAG = ROOT + "product/api/authenticate?authCode={authCode}";
    public static final String SURVEY = ROOT + "survey/api/surveys/{surveyId}";
    public static final String ANSWER_TEMPLATE = ROOT + "survey/api/answer-templates/{answerId}";
    public static final String SEND_ANSWERS = ROOT +"survey/api/survey/questions/answers";
    public static final String PRODUCT_DETAIL = ROOT + "product/api/products/{productId}";
    public static final String PRODUCT_IMAGE = ROOT + "product/downloadFile/{productId}";

}
