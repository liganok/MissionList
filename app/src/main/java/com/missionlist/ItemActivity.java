package com.missionlist;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.missionlist.model.Mission;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
        title = (EditText)findViewById(R.id.et_new_title);
        start_date = (EditText)findViewById(R.id.et_new_start_date);
        due_date = (EditText)findViewById(R.id.et_new_due_date);
        priority = (EditText)findViewById(R.id.et_priority);
        occurrence = (EditText)findViewById(R.id.et_new_occurrence);
        description = (EditText)findViewById(R.id.et_new_des);
        rl_loading_unblock = (RelativeLayout)findViewById(R.id.rl_loading_unblock);
        save = (FrameLayout)findViewById(R.id.fl_head_save);
        close = (FrameLayout)findViewById(R.id.fl_head_close);

        //Fetch the data from extra data
        if (getIntent().hasExtra(Mission.ID)){
            ID = getIntent().getExtras().getString(Mission.ID);
            new processDataTask().execute(MListApp.REQ_ITEM_DETAIL);
        }else {
            rl_loading_unblock.setVisibility(View.INVISIBLE);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra(Mission.ID)){
                    ID = getIntent().getExtras().getString(Mission.ID);
                    new processDataTask().execute(MListApp.REQ_ITEM_EDIT);
                }else{
                    new processDataTask().execute(MListApp.REQ_ITEM_NEW);
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



    class processDataTask extends AsyncTask<Integer, Integer, Map<String,Object>> {

        protected void onPreExecute(){
            rl_loading_unblock.setVisibility(View.VISIBLE);
        }

        @Override
        protected Map<String,Object> doInBackground(Integer... params) {
            Map<String,Object> mapObject = new HashMap<String, Object>();
            switch (params[0]){
                case MListApp.REQ_ITEM_DETAIL:
                    mapObject = getItemDetail(ID);
                    break;
                case MListApp.REQ_ITEM_NEW:
                    mapObject = saveItemNew();
                    break;
                case MListApp.REQ_ITEM_EDIT:
                    break;
            }

            return mapObject;
        }

        protected void onPostExecute(Map<String,Object> result) {
            int reqType = Integer.parseInt(result.get(MListApp.REQ_TYPE).toString());
            switch (reqType){
                case MListApp.REQ_ITEM_DETAIL:
                    if (result.get(Mission.TITLE) != null){
                        title.setText(result.get(Mission.TITLE).toString());
                    }

                    if (result.get(Mission.DESCRIPTION) != null){
                        description.setText(result.get(Mission.DESCRIPTION).toString());
                    }

                    if (result.get(Mission.PRIORITY) != null){
                        priority.setText(result.get(Mission.PRIORITY).toString());
                    }

                    if (result.get(Mission.DUE_DATE) != null){
                        due_date.setText(result.get(Mission.DUE_DATE).toString());
                    }

                    if (result.get(Mission.OCCURRENCE) != null){
                        occurrence.setText(result.get(Mission.OCCURRENCE).toString());
                    }
                    rl_loading_unblock.setVisibility(View.INVISIBLE);
                    break;
                case MListApp.REQ_ITEM_NEW:
                    rl_loading_unblock.setVisibility(View.INVISIBLE);
                    if (result.containsKey(MListApp.REQ_STATUS)){
                        setResult(Activity.RESULT_OK);
                        ItemActivity.this.finish();
                    }
                    break;
                case MListApp.REQ_ITEM_EDIT:
                    rl_loading_unblock.setVisibility(View.INVISIBLE);
                    if (result.containsKey(MListApp.REQ_STATUS)){
                        setResult(Activity.RESULT_OK);
                        ItemActivity.this.finish();
                    }
                    break;
            }

        }
    }

    //Implementation of AsyncTask
    private Map<String,Object> getItemDetail(String ID){
        Map<String,Object> mapObject = new HashMap<String, Object>();
        ParseQuery<Mission> query = Mission.getQuery();
        try {
            Mission mission = query.get(ID);
            mapObject.put(MListApp.REQ_TYPE,MListApp.REQ_ITEM_DETAIL);
            mapObject.put(Mission.ID,mission.getObjectId());
            mapObject.put(Mission.TITLE,mission.getTitle());
            mapObject.put(Mission.DESCRIPTION,mission.getDescription());
            mapObject.put(Mission.CATEGORY,mission.getCategory());
            mapObject.put(Mission.DUE_DATE,mission.getDueDate());
            mapObject.put(Mission.IS_DRAFT,mission.getDraft());
            mapObject.put(Mission.START_DATE,mission.getStartDate());
            mapObject.put(Mission.PRIORITY,mission.getPriority());
            mapObject.put(Mission.STATUS,mission.getStatus());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mapObject;
    }

    private Map<String,Object> saveItemEdit(){
        Map<String,Object> mapObject = new HashMap<String, Object>();
        mapObject.put(MListApp.REQ_TYPE,MListApp.REQ_ITEM_NEW);
        ParseQuery<Mission> query = Mission.getQuery();
        try {
            Mission mission = query.get(ID);
            mapObject.put(MListApp.REQ_STATUS,true);
            if (!(mission.get(Mission.TITLE).equals(title.getText().toString()))){
                mission.put(Mission.TITLE,title.getText().toString());
            }

            if (!(mission.get(Mission.DESCRIPTION).equals(description.getText().toString()))){
                mission.put(Mission.DESCRIPTION,description.getText().toString());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mapObject;
    }

    private Map<String,Object> saveItemNew(){
        final Map<String,Object> mapObject = new HashMap<String, Object>();
        Mission mission = new Mission();
        mission.put(Mission.TITLE,title.getText().toString());
        mission.put(Mission.DESCRIPTION,description.getText().toString());
        //mission.put(Mission.START_DATE,start_date.getText().toString());
        mission.put(Mission.IS_DRAFT,false);
        mission.put(Mission.OCCURRENCE,occurrence.getText().toString());
        mission.put(Mission.STATUS,getResources().getIntArray(R.array.status)[0]);
        mission.put(Mission.PRIORITY,Integer.parseInt(priority.getText().toString()));
        mission.setAuthor(ParseUser.getCurrentUser());
        mapObject.put(MListApp.REQ_TYPE,MListApp.REQ_ITEM_NEW);
        try {
            mission.save();
            mapObject.put(MListApp.REQ_STATUS,true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mapObject;
    }
}
