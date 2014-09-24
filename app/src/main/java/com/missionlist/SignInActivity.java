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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class signTask extends AsyncTask<Integer, Integer, Map<String,Object>> {

        protected void onPreExecute(){
            dialog = Util.createLoadingDialog(SignInActivity.this);
            dialog.show();
        }

        @Override
        protected Map<String,Object> doInBackground(Integer... params) {
            Map<String,Object> mapObject = new HashMap<String, Object>();


            return mapObject;
        }

        protected void onPostExecute(Map<String,Object> result) {


        }
    }
}
