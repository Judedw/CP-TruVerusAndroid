package com.sl.clearpicture.api;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.sl.clearpicture.utils.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Amila on 07/08/2018.
 */

public class DataManager {
    private static DataManager instance = null ;
    private static final String TAG = DataManager.class.getSimpleName();



    //  private static final String clientSecret = CebuApplication.get().getResources().getString(R.string.client_secret);


    public static DataManager getInstance() {
        if(instance == null){
            instance = new DataManager();
        }
        return instance;
    }


    public void verifyTag(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,String key) {
        RequestQueue queue = MyVolley.getRequestQueue();

        String surl = ConfigURL.VERIFY_TAG.replace("{authCode}",key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, surl, null, listener, errorListener);
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                30000,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
    public void callSurvey(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,String key) {
        RequestQueue queue = MyVolley.getRequestQueue();

        String surl = ConfigURL.SURVEY.replace("{surveyId}",key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, surl, null, listener, errorListener);
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                30000,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    public void getANswerTemplate(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,String key) {
        RequestQueue queue = MyVolley.getRequestQueue();

        String surl = ConfigURL.ANSWER_TEMPLATE.replace("{answerId}",key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, surl, null, listener, errorListener);
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                30000,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void sendAnswers(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,JSONObject jsonObject) {
        RequestQueue queue = MyVolley.getRequestQueue();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,ConfigURL.SEND_ANSWERS, jsonObject, listener, errorListener);
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                30000,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    public void getProductDetails(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,String key) {
        RequestQueue queue = MyVolley.getRequestQueue();

        String surl = ConfigURL.PRODUCT_DETAIL.replace("{productId}",key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, surl, null, listener, errorListener);
        queue.add(request);
    }

}
