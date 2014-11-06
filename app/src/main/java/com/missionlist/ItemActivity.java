package com.missionlist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.missionlist.adapter.PriorityAdapter;
import com.missionlist.model.Mission;
import com.missionlist.model.Priority;
import com.missionlist.util.Util;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Spinner  prioritySpinner;
    private FrameLayout save;
    private FrameLayout close;
    private Dialog dialog;
    private Mission mission;

    private List<Priority> mPrioritys = new ArrayList<Priority>();

    private static final int MISSION_DETAIL = 3;
    private static final int MISSION_CREATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeAsUpIndicator(R.drawable.ic_action_new);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_item, menu);
        return true;
    }

    private void initView(){
        title = (EditText)findViewById(R.id.et_new_title);
        start_date = (EditText)findViewById(R.id.et_new_start_date);
        due_date = (EditText)findViewById(R.id.et_new_due_date);
        //priority = (EditText)findViewById(R.id.et_priority);
        prioritySpinner = (Spinner)findViewById(R.id.spinner_priority);
        occurrence = (EditText)findViewById(R.id.et_new_occurrence);
        description = (EditText)findViewById(R.id.et_new_des);
        save = (FrameLayout)findViewById(R.id.fl_head_save);
        close = (FrameLayout)findViewById(R.id.fl_head_close);
        dialog = Util.createLoadingDialog(ItemActivity.this);
        // Initial priority drop list
        mPrioritys.add(new Priority(getResources().getIntArray(R.array.priority)[0],getString(R.string.priority_low)));
        mPrioritys.add(new Priority(getResources().getIntArray(R.array.priority)[1],getString(R.string.priority_medium)));
        mPrioritys.add(new Priority(getResources().getIntArray(R.array.priority)[2],getString(R.string.priority_high)));
        PriorityAdapter priorityAdapter = new PriorityAdapter(this,mPrioritys);
        prioritySpinner.setAdapter(priorityAdapter);

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
            try {
                mission = query.get(ID);
                title.setText(mission.getTitle());
                description.setText(mission.getDescription());
                prioritySpinner.setSelection(priorityAdapter.getPosition(mission.getPriority()),true);
                SimpleDateFormat    formatter    =   new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
                if (mission.getStartDate() != null){
                    start_date.setText(formatter.format(mission.getStartDate()));
                }
                if (mission.getDueDate() != null){
                    due_date.setText(formatter.format(mission.getDueDate()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            if (LOCAL_ID != null){
                //dialog.show();
                ParseQuery<Mission> query = Mission.getQuery();
                query.fromLocalDatastore();
                query.whereEqualTo(Mission.LOCAL_ID,LOCAL_ID);
                try {
                    mission = query.getFirst();
                    title.setText(mission.getTitle());
                    description.setText(mission.getDescription());
                    prioritySpinner.setSelection(priorityAdapter.getPosition(mission.getPriority()),true);
                    SimpleDateFormat    formatter    =   new SimpleDateFormat("MM月dd日     ");
                    if (mission.getStartDate() != null){
                        start_date.setText(formatter.format(mission.getStartDate()));
                    }
                    if (mission.getDueDate() != null){
                        due_date.setText(formatter.format(mission.getDueDate()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                mission = new Mission();
                mission.setLocalId();
            }
        }
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Priority pri = mPrioritys.get(position);
                if(pri != null){
                    mission.setPriority(pri.getID());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void save(){
        dialog.show();
        mission.setTitle(title.getText().toString());
        mission.setDescription(description.getText().toString());
        mission.setDraft(true);
        mission.setStartDate(new Date(System.currentTimeMillis()));
        mission.setDueDate(new Date(System.currentTimeMillis()));
        mission.setStatus(getResources().getIntArray(R.array.status)[1]);
        mission.setAuthor(ParseUser.getCurrentUser());
        mission.setDelete(false);
        mission.setCategory(1);
        mission.pinInBackground(MListApp.GROUP_NAME,new SaveCallback() {
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
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                save();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


}
