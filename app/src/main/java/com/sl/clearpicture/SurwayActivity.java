package com.sl.clearpicture;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sl.clearpicture.api.DataManager;
import com.sl.clearpicture.fragments.SurwayFragment;
import com.sl.clearpicture.model.Question;
import com.sl.clearpicture.model.SurveyCallResponse;
import com.sl.clearpicture.utils.JudiProgressDialog;
import com.sl.clearpicture.utils.MessageEvent;
import com.sl.clearpicture.utils.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SurwayActivity extends AppCompatActivity implements SurwayFragment.OnSurWayFragmentInteractionListener{
    private FragmentManager fragmentManager;
    private SurveyCallResponse surveyCallResponse;
    private ArrayList<Question> questions;
    private int current = 0;
    private JSONObject fullAnswer = new JSONObject();
    private JSONArray questionsArray = new JSONArray();
    private JudiProgressDialog progressDialog;
    private Prefs prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surway);
        progressDialog = new JudiProgressDialog(SurwayActivity.this);
        questions = new ArrayList<>();
        surveyCallResponse = getIntent().getParcelableExtra("survey");
        prefs = new Prefs(SurwayActivity.this);
        questions = (ArrayList<Question>) surveyCallResponse.getContentSurvey().getQuestions();
        try {
            fullAnswer.put("surveyId",surveyCallResponse.getContentSurvey().getId());
            fullAnswer.put("authCode",prefs.getAuth());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setUpView();
    }

    private void setUpView() {

        loadNextSurwayQuestion();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
                if (f != null) {
                    updateTitleAndDrawer(f);
                }

            }
        });
    }
    private  void loadNextSurwayQuestion(){
        if(questions != null && current < questions.size()){
            Question question = questions.get(current);
            current++;
            String buttonText = "NEXT";
            if(current == questions.size()){
                buttonText = "DONE";
            }
            replaceFragment(question,current+ " of "+questions.size(),buttonText);
        }
    }

    private void replaceFragment(Question question,String counter,String buttonText){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, SurwayFragment.newInstance(question,counter,buttonText), "");
        //ft.setTransition();
        ft.commit();

    }

    private void updateTitleAndDrawer(Fragment fragment) {
    }

    @Override
    public void onNextButtonClick(JSONObject object) {
        questionsArray.put(object);
        loadNextSurwayQuestion();
    }

    @Override
    public void onFinishButtonClick(JSONObject object) {
        questionsArray.put(object);
        try {
            fullAnswer.put("questions",questionsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        DataManager.getInstance().sendAnswers(sendAnswerSuccessListener(),sendAnswersErrorListener(),fullAnswer);
        Log.i("P-->",fullAnswer.toString());
    }

    private Response.ErrorListener sendAnswersErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error-->",error.toString());
                progressDialog.dismiss();
            }
        };
    }

    private Response.Listener<JSONObject> sendAnswerSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i("res-->",response.toString());
                EventBus.getDefault().postSticky(new MessageEvent(MessageEvent.REFRESH_SCAN,""));
                finish();
            }
        };
    }
}
