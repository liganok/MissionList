package com.missionlist.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.missionlist.R;
import com.missionlist.utils.Util;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {

    private EditText userName;
    private EditText password;
    private Button signUp;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView(){
        userName = (EditText)findViewById(R.id.et_username_signup);
        password = (EditText)findViewById(R.id.et_password_signup);
        signUp = (Button)findViewById(R.id.btn_signup);
        dialog = Util.createLoadingDialog(SignUpActivity.this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                final ParseUser parseUser = new ParseUser();
                parseUser.setUsername(userName.getText().toString());
                parseUser.setPassword(password.getText().toString());
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.cancel();
                        if (e==null){
                            setResult(Activity.RESULT_OK);
                            Util.showMessage(getApplicationContext(),"Sign up success", Toast.LENGTH_SHORT);
                            finish();
                        }else{

                        }
                    }
                });
            }
        });
    }

















}
