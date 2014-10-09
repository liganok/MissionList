package com.missionlist;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.missionlist.model.Mission;
import com.missionlist.util.Util;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ItemActivity extends Activity {
    //private Mission mission;
    private String ID ;
    private String LOCAL_ID ;
    private EditText title;
    private EditText start_date;
    private EditText due_date;
    private EditText category;
    private EditText priority;
    private EditText occurrence;
    private EditText description;
    private FrameLayout save;
    private FrameLayout close;
    private Dialog dialog;
    private Mission mission;

    private static final int MISSION_DETAIL = 3;
    private static final int MISSION_CREATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        initView();
    }

    private void initView(){
        title = (EditText)findViewById(R.id.et_new_title);
        start_date = (EditText)findViewById(R.id.et_new_start_date);
        due_date = (EditText)findViewById(R.id.et_new_due_date);
        priority = (EditText)findViewById(R.id.et_priority);
        occurrence = (EditText)findViewById(R.id.et_new_occurrence);
        description = (EditText)findViewById(R.id.et_new_des);
        save = (FrameLayout)findViewById(R.id.fl_head_save);
        close = (FrameLayout)findViewById(R.id.fl_head_close);
        dialog = Util.createLoadingDialog(ItemActivity.this);


        if (getIntent().hasExtra(Mission.ID)){
            ID = getIntent().getExtras().getString(Mission.ID);
        }
        if (getIntent().hasExtra(Mission.LOCAL_ID)){
            LOCAL_ID = getIntent().getExtras().getString(Mission.LOCAL_ID);
        }

        if (ID != null){
            //dialog.show();
            ParseQuery<Mission> query = Mission.getQuery();
            query.fromLocalDatastore();
            //query.whereEqualTo(Mission.ID, ID);
            query.getInBackground(ID,new GetCallback<Mission>() {
                @Override
                public void done(Mission mission, ParseException e) {
                    if (e == null){
                        title.setText(mission.getTitle());
                        description.setText(mission.getDescription());
                        dialog.cancel();
                    }
                }
            });
            query.getFirstInBackground(new GetCallback<Mission>() {
                @Override
                public void done(Mission o, ParseException e) {
                    if (e == null) {
                        mission = o;
                        title.setText(mission.getTitle());
                        description.setText(mission.getDescription());
                        dialog.cancel();
                    }
                }
            });

        }else {
            if (LOCAL_ID != null){
                //dialog.show();
                ParseQuery<Mission> query = Mission.getQuery();
                query.fromLocalDatastore();
                query.whereEqualTo(Mission.LOCAL_ID,LOCAL_ID);
                query.getFirstInBackground(new GetCallback<Mission>() {
                    @Override
                    public void done(Mission o, ParseException e) {
                        if (e==null){
                            mission = o;
                            title.setText(mission.getTitle());
                            description.setText(mission.getDescription());
                            dialog.cancel();
                        }
                    }
                });

            }else {
                mission = new Mission();
                mission.setLocalId();
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.show();
                mission.setTitle(title.getText().toString());
                mission.setDescription(description.getText().toString());
                mission.setDraft(true);
                mission.setAuthor(ParseUser.getCurrentUser());
                mission.saveEventually();
                setResult(Activity.RESULT_OK);
                Util.showMessage(getApplicationContext(),"Save in local success", Toast.LENGTH_SHORT);
                finish();
                /*mission.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.cancel();
                        if (e==null){
                            setResult(Activity.RESULT_OK);
                            Util.showMessage(getApplicationContext(),"Save in local success", Toast.LENGTH_SHORT);
                            finish();
                        }else {
                            Util.showMessage(getApplicationContext(),"Save in local failed", Toast.LENGTH_SHORT);
                        }
                    }
                });*/
                /*mission.pinInBackground(MListApp.GROUP_NAME,new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.cancel();
                        if (e==null){
                            setResult(Activity.RESULT_OK);
                            Util.showMessage(getApplicationContext(),"Save in local success", Toast.LENGTH_SHORT);
                            finish();
                        }else {
                            Util.showMessage(getApplicationContext(),"Save in local failed", Toast.LENGTH_SHORT);
                        }
                    }
                });*/
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
