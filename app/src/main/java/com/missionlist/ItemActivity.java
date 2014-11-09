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
    private EditText title;
    private EditText start_date;
    private EditText due_date;
    private EditText category;
    private EditText occurrence;
    private EditText description;
    private Spinner  prioritySpinner;
    private Dialog dialog;
    private Mission mission;


    private static final int MISSION_DETAIL = 3;
    private static final int MISSION_CREATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
        prioritySpinner = (Spinner)findViewById(R.id.spinner_priority);
        occurrence = (EditText)findViewById(R.id.et_new_occurrence);
        description = (EditText)findViewById(R.id.et_new_des);
        dialog = Util.createLoadingDialog(ItemActivity.this);

        mission = Util.getMissionObject(getIntent());
        if (mission == null){return;}
        if (mission.getTitle() != null){title.setText(mission.getTitle());}
        if (mission.getDescription() != null){description.setText(mission.getDescription());}
        prioritySpinner.setSelection(mission.getPriority(),true);
        SimpleDateFormat    formatter    =   new SimpleDateFormat("yyyy年MM月dd日");
        if (mission.getStartDate() != null){start_date.setText(formatter.format(mission.getStartDate()));}
        if (mission.getDueDate() != null){due_date.setText(formatter.format(mission.getDueDate()));}
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
        mission.setPriority(prioritySpinner.getSelectedItemPosition());
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
