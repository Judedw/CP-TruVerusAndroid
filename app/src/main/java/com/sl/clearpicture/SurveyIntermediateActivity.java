package com.sl.clearpicture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sl.clearpicture.model.SurveyCallResponse;

public class SurveyIntermediateActivity extends AppCompatActivity {

    private Button btnContinue;
    private SurveyCallResponse surveyCallResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_intermediate);
        btnContinue = findViewById(R.id.btnContinue);
        surveyCallResponse = getIntent().getParcelableExtra("survey");
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SurveyIntermediateActivity.this,SurwayActivity.class);
                intent.putExtra("survey",surveyCallResponse);
                startActivity(intent);
                finish();
            }
        });
    }
}
