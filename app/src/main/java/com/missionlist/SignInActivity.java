package com.missionlist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.missionlist.util.Util;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class SignInActivity extends Activity {
    private EditText userName;
    private EditText password;
    private Button signIn;
    private Button signUp;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView(){
        userName = (EditText)findViewById(R.id.et_username);
        password = (EditText)findViewById(R.id.et_password);
        signIn = (Button)findViewById(R.id.btn_signin);
        signUp = (Button)findViewById(R.id.btn_for_signup);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = Util.createLoadingDialog(SignInActivity.this);
                dialog.show();
                ParseUser.logInInBackground(userName.getText().toString(),password.getText().toString(),new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e==null){
                            dialog.cancel();
                            setResult(Activity.RESULT_OK);
                            Util.showMessage(getApplicationContext(),"Success sign in", Toast.LENGTH_SHORT);
                            finish();
                        }
                    }
                });
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivityForResult(intent,MListApp.REQ_SIGN_UP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == MListApp.REQ_SIGN_UP){
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    }

}
