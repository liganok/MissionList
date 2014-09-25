package com.missionlist;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.missionlist.util.Util;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;


public class SignInActivity extends Activity {
    private EditText userName;
    private EditText password;
    private Button signIn;
    private Button signUp;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView(){
        userName = (EditText)findViewById(R.id.et_username);
        password = (EditText)findViewById(R.id.et_password);
        signIn = (Button)findViewById(R.id.btn_signin);
        signUp = (Button)findViewById(R.id.btn_signup);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new signTask().execute();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class signTask extends AsyncTask<Integer, Integer, Boolean> {

        protected void onPreExecute(){
            dialog = Util.createLoadingDialog(SignInActivity.this);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            ParseUser user;
            try {
                user = ParseUser.logIn(userName.getText().toString(),password.getText().toString());
                user.pin();
            } catch (ParseException e) {
                user = ParseUser.getCurrentUser();
                e.printStackTrace();
            }
            if (user == null){
                return false;
            }else {
                return true;
            }
        }

        protected void onPostExecute(Boolean result) {
            if (result == true){

            }
            dialog.cancel();
        }
    }
}
