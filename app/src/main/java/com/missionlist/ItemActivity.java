package com.missionlist;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.missionlist.model.Mission;
import com.missionlist.util.Util;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Map;

public class ItemActivity extends Activity {
    //private Mission mission;
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
    private Dialog dialog;

    private static final int MISSION_DETAIL = 3;
    private static final int MISSION_CREATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
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

        //Initial the detail list if needed
        if (getIntent().hasExtra(Mission.ID)){
            dialog.show();
            ID = getIntent().getExtras().getString(Mission.ID);
            if (getIntent().getExtras().getString(Mission.TITLE) != null){
                title.setText(getIntent().getExtras().getString(Mission.TITLE));
            }
            if (getIntent().getExtras().getString(Mission.DESCRIPTION) != null){
                title.setText(getIntent().getExtras().getString(Mission.DESCRIPTION));
            }

            if (getIntent().getExtras().getString(Mission.PRIORITY) != null){
                title.setText(getIntent().getExtras().getString(Mission.PRIORITY));
            }
            dialog.cancel();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra(Mission.ID)){
                    ID = getIntent().getExtras().getString(Mission.ID);
                    saveItemEdit(ID);
                }else{
                    //new processDataTask().execute(MListApp.REQ_ITEM_NEW);
                    saveItemNew();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void saveItemEdit(String ID){
        dialog.show();
        ParseQuery<Mission> query = Mission.getQuery();
        query.fromLocalDatastore();
        query.getInBackground(ID,new GetCallback<Mission>() {
            @Override
            public void done(final Mission mission, ParseException e) {
                if (mission.getTitle().equals(title.getText().toString())){
                    mission.setTitle(title.getText().toString());
                }
                if (mission.getDescription().equals(description.getText().toString())){
                    mission.setTitle(description.getText().toString());
                }
                mission.setDraft(true);
                if (mission.getOccurrence().equals(occurrence.getText().toString())){
                    mission.setTitle(occurrence.getText().toString());
                }
                if (mission.getPriority() == Integer.parseInt(priority.getText().toString())){
                    mission.setPriority(Integer.parseInt(priority.getText().toString()));
                }
                mission.pinInBackground();
                if (Util.isNetworkConnected(getApplicationContext())){
                    mission.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                mission.setDraft(false);
                                mission.pinInBackground();
                                Util.showMessage(getApplicationContext(),"Save in cloud success", Toast.LENGTH_SHORT);
                            }else{
                                Util.showMessage(getApplicationContext(),"Save in cloud failed", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
                setResult(Activity.RESULT_OK);
                dialog.cancel();
                finish();
            }
        });

    }

    private void saveItemNew(){
        dialog.show();
        final Mission mission = new Mission();
        mission.setTitle(title.getText().toString());
        mission.setAuthor(ParseUser.getCurrentUser());
        mission.setDescription(description.getText().toString());
        mission.setDraft(true);
        mission.setOccurrence(occurrence.getText().toString());
        mission.setStatus(getResources().getIntArray(R.array.status)[0]);
        mission.setPriority(Integer.parseInt(priority.getText().toString()));

        mission.pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Util.showMessage(getApplicationContext(),"Save in local success", Toast.LENGTH_SHORT);
                    setResult(Activity.RESULT_OK);

                    if (Util.isNetworkConnected(getApplicationContext())){
                        mission.setDraft(true);
                        mission.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){
                                    mission.setDraft(false);
                                    mission.pinInBackground();
                                    Util.showMessage(getApplicationContext(),"Save in cloud success", Toast.LENGTH_SHORT);
                                }else {
                                    Util.showMessage(getApplicationContext(),"Save in cloud failed", Toast.LENGTH_SHORT);
                                }
                            }
                        });
                    }
                    setResult(Activity.RESULT_OK);
                    dialog.cancel();
                    finish();
                }else {
                    Util.showMessage(getApplicationContext(),"Save in local failed", Toast.LENGTH_SHORT);
                }
            }
        });

    }

}
