package com.missionlist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.missionlist.com.missionlist.util.Util;

public class NewActivity extends Activity {
    private Mission mission;
    private String ID = null;
    private EditText title;
    private EditText start_date;
    private EditText due_date;
    private EditText category;
    private EditText priority;
    private EditText occurrence;
    private EditText description;
    private FrameLayout save;
    private FrameLayout close;
    private RelativeLayout rl_loading_unblock;

    private static final int MISSION_DETAIL = 3;
    private static final int MISSION_CREATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        initView();
    }

    private void initView(){
         //Fetch the data from extra data
        ID = null;
        /*if (getIntent().hasExtra(Mission.ID)){
            ID = getIntent().getExtras().getString(Mission.ID);
            Intent intent = new Intent(this,LoadingActivity.class);
            intent.putExtra(MListApp.REQ_TYPE, MListApp.REQ_ITEM_DETAIL);
            intent.putExtra(Mission.ID,ID);
            startActivityForResult(intent,MISSION_DETAIL);
        }*/

        rl_loading_unblock = (RelativeLayout)findViewById(R.id.rl_loading_unblock);
        rl_loading_unblock.setVisibility(View.VISIBLE);

        save = (FrameLayout)findViewById(R.id.fl_head_save);
        close = (FrameLayout)findViewById(R.id.fl_head_close);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                title = (EditText)findViewById(R.id.et_new_title);
                start_date = (EditText)findViewById(R.id.et_new_start_date);
                due_date = (EditText)findViewById(R.id.et_new_due_date);
                priority = (EditText)findViewById(R.id.et_priority);
                occurrence = (EditText)findViewById(R.id.et_new_occurrence);
                description = (EditText)findViewById(R.id.et_new_des);

                Intent intent = new Intent(NewActivity.this,LoadingActivity.class);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("title",title.toString());
                intent.putExtra("title",title.toString());
                intent.putExtra("title",title.toString());
                intent.putExtra("title",title.toString());

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_loading_unblock.setVisibility(View.INVISIBLE);
                //finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_item, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == MISSION_DETAIL){
                if (intent.hasExtra("title")){
                    title.setText(intent.getExtras().getString("title"));
                }
                if (intent.hasExtra("description")){
                    description.setText(intent.getExtras().getString("description"));
                }
            }

            if (requestCode == MISSION_CREATE){
                finish();
            }
        }
    }
}
