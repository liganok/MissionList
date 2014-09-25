package com.missionlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.missionlist.R;

public class MeActivity extends Activity {
    private static final int REQ_CODE_SIGN_IN = 0;
    private Button userSign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        initView();
    }

    private void initView(){
        userSign = (Button)findViewById(R.id.btn_user );
        userSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeActivity.this,SignInActivity.class);
                startActivityForResult(intent,REQ_CODE_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQ_CODE_SIGN_IN:
                    break;
            }
        }
    }
}
