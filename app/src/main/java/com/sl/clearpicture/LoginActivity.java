package com.sl.clearpicture;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.sl.clearpicture.model.FieldError;
import com.sl.clearpicture.utils.FieldValidator;
import com.sl.clearpicture.utils.SnackMessageCreator;

public class LoginActivity extends AppCompatActivity {

    private ImageView ivLogin;
    private EditText etPasword;
    private EditText etEmail;
    private CoordinatorLayout loginRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ivLogin = findViewById(R.id.ivLogin);
        etEmail = findViewById(R.id.etEmail);
        etPasword = findViewById(R.id.etPasword);
        loginRoot = findViewById(R.id.loginRoot);
        ivLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FieldError field1 = FieldValidator.isValidEmail(etEmail.getText().toString());
                FieldError field2 = FieldValidator.isValidPasswrod(etPasword.getText().toString());

                if (!field1.isValidation()) {
                    if (field1.getReason().equals(FieldValidator.ERROR_EMPTY)) {

                        SnackMessageCreator.createSnackBar(getString(R.string.warning_email_empty),loginRoot,LoginActivity.this,R.color.colorRed);
                        return;
                    } else if (field1.getReason().equals(FieldValidator.ERROR_INVALID_EMAIL)) {
                        SnackMessageCreator.createSnackBar(getString(R.string.warning_inavlaid_email),loginRoot,LoginActivity.this,R.color.colorRed);

                        return;
                    }

                } else if (!field2.isValidation()) {
                    if (field2.getReason().equals(FieldValidator.ERROR_EMPTY)) {
                        SnackMessageCreator.createSnackBar(getString(R.string.warning_password_empty),loginRoot,LoginActivity.this,R.color.colorRed);

                        return;
                    }
//            else if (field2.getReason().equals(FieldValidator.ERROR_SHORT_PASSWORD)) {
//                inputLayoutPassword.setError(getString(R.string.warning_short_password));
//                return;
//            }
                } else {
                    if(etEmail.getText().toString().toLowerCase().equals("judi812@gmail.com") && etPasword.getText().toString().equals("12345678")) {

                        Intent intent =  new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        SnackMessageCreator.createSnackBar("Username or password is incorrect" ,loginRoot,LoginActivity.this,R.color.colorRed);
                    }
                }
            }
        });
    }
}
