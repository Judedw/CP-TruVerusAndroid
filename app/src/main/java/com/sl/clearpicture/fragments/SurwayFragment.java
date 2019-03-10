package com.sl.clearpicture.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;

import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sl.clearpicture.LoginActivity;
import com.sl.clearpicture.R;
import com.sl.clearpicture.api.DataManager;
import com.sl.clearpicture.model.Answer;
import com.sl.clearpicture.model.AnswerTemplateResponse;
import com.sl.clearpicture.model.Question;
import com.sl.clearpicture.utils.GeneralUtils;
import com.sl.clearpicture.utils.MessageEvent;
import com.sl.clearpicture.utils.SnackMessageCreator;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SurwayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    // TODO: Rename and change types of parameters
    private Question question;
    private String mParam2;
    private String textNextButton;
    private OnSurWayFragmentInteractionListener mListener;
    private TextView tvQuestion,tvCounter;
    private Button btnNext;
    private Button btnCancel;
    private LinearLayout llQuestionWrapper;
    private Activity activity;
    private   AnswerTemplateResponse templateResponse;
    private JSONArray answersSet = new JSONArray();
    private JSONObject object = new JSONObject();
    private CoordinatorLayout snackRoot;
    private int count = 0,countRadio = 0;
    ColorStateList colorStateList = null;
    private boolean isOk;
    public SurwayFragment() {

    }


    public static SurwayFragment newInstance(Question question, String counter,String buttonText) {
        SurwayFragment fragment = new SurwayFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, question);
        args.putString(ARG_PARAM2, counter);
        args.putString(ARG_PARAM3,buttonText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            textNextButton = getArguments().getString(ARG_PARAM3);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surway, container, false);
        activity = getActivity();
        colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {

                        ContextCompat.getColor(activity,R.color.colorSilver) //disabled
                        ,ContextCompat.getColor(activity,R.color.colorSilver) //enabled

                }
        );
        setUpView(view);
        try {
            object.put("id",question.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataManager.getInstance().getANswerTemplate(answerTemplateSuccessListener(),answerTemplateErrorListener(),question.getAnswerTemplate().getId());
        return view;
    }

    private Response.ErrorListener answerTemplateErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
    }

    private Response.Listener<JSONObject> answerTemplateSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("k-->",response.toString());
                Gson gson = new Gson();
                 templateResponse = gson.fromJson(response.toString(),AnswerTemplateResponse.class);
                if(templateResponse != null) {
                    if(templateResponse.getStatusCode() == 200) {
                        btnNext.setVisibility(View.VISIBLE);
                        generateAnswerSheet(templateResponse);
                    }
                }
            }
        };
    }

    private void generateAnswerSheet(AnswerTemplateResponse templateResponse) {
        if(templateResponse != null){
            List<Answer> answerList = new ArrayList<>();
            answerList = templateResponse.getContentAnswer().getAnswers();
            if(answerList != null) {
            if(templateResponse.getContentAnswer().getAnswerTemplateType().equals("M")){
                generateMultiChoiceAnswer(answerList);
            }else if(templateResponse.getContentAnswer().getAnswerTemplateType().equals("S")){
                generateSingleChoiceAnswer(answerList);
            }else if(templateResponse.getContentAnswer().getAnswerTemplateType().equals("F")){
                generateFreeTextAnswer(answerList);
            }

            }
        }
    }

    private void generateFreeTextAnswer(List<Answer> answerList){


            final EditText editText = new TextInputEditText(activity);
        // Add an ID to it
        // editText.setId(View.generateViewId());
        // Get the Hint text for EditText field which will be presented to the
        // user in the TextInputLayout
//        editText.setHint(answer.getLable());
        // Set color of the hint text inside the EditText field
        // editText.setHintTextColor(ContextCompat.getColor(ReportTimeActivity.this, R.color.colorEditTextHintDefault));
        // editText.setHintTextColor(ContextCompat.getColor(ReportTimeActivity.this,R.color.colorRed));
        // Set the font size of the text that the user will enter


        editText.setTextSize(14);

        editText.requestFocus();
        editText.setCursorVisible(true);
//        editText.setText(gigData.getFieldValue());
        editText.setTag(question.getId());
        editText.setBackgroundResource(R.drawable.edittext_background);

        // Set the color of the text inside the EditText field
        editText.setTextColor(ContextCompat.getColor(activity, R.color.colorSilver));


//            if(gigData.getIsEditable().equals("false")){
//                editText.setEnabled(false);
//            }else{
//                editText.setEnabled(true);
//            }
        // Define layout params for the EditTExt field
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // Set editText layout parameters to the editText field

        editText.setLayoutParams(editTextParams);

        /*
         * Next, you do the same thing for the TextInputLayout (instantiate,
         * generate and set ID, set layoutParams, set layoutParamt for
         * TextInputLayout
         */

        // TextInputLayout
        TextInputLayout textInputLayout = new TextInputLayout(activity);
        // textInputLayout.setTag(gigData.getId()+"error");
        LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        float px = GeneralUtils.convertDpToPixel(3);
        textInputLayoutParams.setMargins(0, (int) px, 0, (int) px);
        textInputLayout.setLayoutParams(textInputLayoutParams);
        // textInputLayout.setHintTextAppearance(R.style.TextLabel);

        // Then you add editText into a textInputLayout

        //  mainMenuButton.setBackground(background);
        textInputLayout.addView(editText, editTextParams);
        llQuestionWrapper.addView(textInputLayout);


    }

    @SuppressLint("RestrictedApi")
    private void generateMultiChoiceAnswer(List<Answer> answerList){
        for(Answer answer : answerList){
            AppCompatCheckBox checkBox = new AppCompatCheckBox(activity);
            checkBox.setText(answer.getLable());
            checkBox.setTag(answer.getId());
            checkBox.setTextColor(ContextCompat.getColor(activity,R.color.colorSilver));
            checkBox.setSupportButtonTintList(colorStateList);
            LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            float pxs = GeneralUtils.convertDpToPixel(3);
            spinnerParams.setMargins(0,(int)pxs,0,(int)pxs);
            checkBox.setLayoutParams(spinnerParams);
            llQuestionWrapper.addView(checkBox);

        }
    }

    @SuppressLint("RestrictedApi")
    private void generateSingleChoiceAnswer(List<Answer> answerList){
        RadioGroup rg = new RadioGroup(activity); //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for(Answer answer : answerList){

            AppCompatRadioButton rb = new AppCompatRadioButton(activity);
            rb.setText(answer.getLable());
            rb.setTag(answer.getId());
            rb.setTextColor(ContextCompat.getColor(activity,R.color.colorSilver));
            rb.setSupportButtonTintList(colorStateList);
            LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            float pxs = GeneralUtils.convertDpToPixel(3);
            spinnerParams.setMargins(0,(int)pxs,0,(int)pxs);
            rb.setLayoutParams(spinnerParams);
            rg.addView(rb);
            }

            llQuestionWrapper.addView(rg);


    }

    private void setUpView(View view) {

        tvQuestion = view.findViewById(R.id.tvQuestion);
        btnNext = view.findViewById(R.id.btnNext);
        tvQuestion.setText(question.getName());
        tvCounter = view.findViewById(R.id.tvCounter);
        btnNext.setText(textNextButton);
        llQuestionWrapper = view.findViewById(R.id.llQuestionWrapper);
        btnNext.setVisibility(View.GONE);
        btnCancel = view.findViewById(R.id.btnCancel);
        snackRoot = view.findViewById(R.id.snackRoot);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textNextButton.equals("NEXT")) {
                    collectAnswers();
                    if(isOk) {
                        try {
                            object.put("answers", answersSet);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mListener.onNextButtonClick(object);
                    }
                }else if(textNextButton.equals("DONE")) {
                    collectAnswers();
                    if (isOk){
                        try {
                            object.put("answers", answersSet);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    mListener.onFinishButtonClick(object);
                }

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new MessageEvent(MessageEvent.REFRESH_SCAN,""));
                activity.finish();
            }
        });
        tvCounter.setText(mParam2);
    }

    private void collectAnswers(){
        if(templateResponse != null){
            List<Answer> answerList = new ArrayList<>();
            answerList = templateResponse.getContentAnswer().getAnswers();
            if(answerList != null) {
                if(templateResponse.getContentAnswer().getAnswerTemplateType().equals("M")){
                    validateMultiChoiceAnswer(answerList);
                    if(count == 0){
                        isOk = false;
                        SnackMessageCreator.createSnackBar("Please select atleast one answer",snackRoot,activity,R.color.colorRed);
                        return;
                    }else {
                        isOk = true;
                        collectMultiChoiceAnswer(answerList);
                    }
                }else if(templateResponse.getContentAnswer().getAnswerTemplateType().equals("S")){
                    validateSingleChoiceAnswer(answerList);
                    if(countRadio == 0){
                        isOk = false;
                        SnackMessageCreator.createSnackBar("Please select atleast one answer",snackRoot,activity,R.color.colorRed);
                        return;
                    }else {
                        isOk = true;
                        collectSingleChoiceAnswer(answerList);
                    }

                }else if(templateResponse.getContentAnswer().getAnswerTemplateType().equals("F")){
                   boolean isValid = validateFreeTextAnswer(answerList);
                    if(!isValid){
                        isOk = false;
                        SnackMessageCreator.createSnackBar("Please enter your answer",snackRoot,activity,R.color.colorRed);
                        return;
                    }else {
                        isOk = true;
                        collectFreeTextAnswer(answerList);
                    }
                }

            }
        }
    }

    private void collectMultiChoiceAnswer(List<Answer> answerList){
        for(Answer answer : answerList){
            AppCompatCheckBox checkBox = (AppCompatCheckBox) llQuestionWrapper.findViewWithTag(answer.getId());
            JSONObject jsonObject = new JSONObject();
            try {
                if(checkBox.isChecked()) {
                    jsonObject.put("id", answer.getId());
                    answersSet.put(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void validateMultiChoiceAnswer(List<Answer> answerList){
        for(Answer answer : answerList){
            AppCompatCheckBox checkBox = (AppCompatCheckBox) llQuestionWrapper.findViewWithTag(answer.getId());
            if (checkBox.isChecked()) {
                count++;
            }

        }
    }

    private void validateSingleChoiceAnswer(List<Answer> answerList){
        for(Answer answer : answerList){
            AppCompatRadioButton radioButton= (AppCompatRadioButton) llQuestionWrapper.findViewWithTag(answer.getId());

                if(radioButton.isChecked()) {
                    countRadio++;
                }
        }
    }
    private void collectSingleChoiceAnswer(List<Answer> answerList){
        for(Answer answer : answerList){
            AppCompatRadioButton radioButton= (AppCompatRadioButton) llQuestionWrapper.findViewWithTag(answer.getId());
            JSONObject jsonObject = new JSONObject();
            try {
                if(radioButton.isChecked()) {
                    jsonObject.put("id", answer.getId());
                    answersSet.put(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }


    private boolean validateFreeTextAnswer(List<Answer> answerList){
        boolean isValid = false;

            EditText editText = (EditText) llQuestionWrapper.findViewWithTag(question.getId());

                if(!editText.getText().toString().isEmpty()) {
                    isValid = true;
                }

        return  isValid;
    }

    private void collectFreeTextAnswer(List<Answer> answerList){
        for(Answer answer : answerList){
            EditText editText = (EditText) llQuestionWrapper.findViewWithTag(answer.getId());
            JSONObject jsonObject = new JSONObject();
            try {
                if(!editText.getText().toString().isEmpty()) {
                    jsonObject.put("freeText", editText.getText().toString());
                    answersSet.put(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSurWayFragmentInteractionListener) {
            mListener = (OnSurWayFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSurWayFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSurWayFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNextButtonClick(JSONObject jsonObject);
        void onFinishButtonClick(JSONObject jsonObject);
    }

}
