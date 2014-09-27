package com.missionlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.missionlist.R;
import com.parse.ParseUser;

public class MeActivity extends Activity {

    private Button userSign;
    private TextView username_me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        initView();
    }

    private void initView(){
        userSign = (Button)findViewById(R.id.btn_user );
        username_me = (TextView)findViewById(R.id.username_me);
        if (ParseUser.getCurrentUser() != null){
            username_me.setText(ParseUser.getCurrentUser().getUsername());
        }

        userSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeActivity.this,SignInActivity.class);
                startActivityForResult(intent,MListApp.REQ_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == MListApp.REQ_SIGN_IN){
                ParseUser user = ParseUser.getCurrentUser();
                user.getUsername();
                username_me.setText(user.getUsername());
            }
        }
    }
}
