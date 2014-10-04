package com.missionlist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.missionlist.model.Mission;
import com.missionlist.util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    //Variable
    private FrameLayout top_head_me;
    private FrameLayout top_head_add;
    private Button btn_to_do;
    private Button btn_done;
    //private ProgressBar pb_main;
    private RelativeLayout pb_main;
    private int tab_type;
    private SimpleAdapter simpleAdapter;
    private ListView list;
    private final static int TO_DO = 1;
    private final static int DONE = 2;
    private int listType;
    private GestureDetector gestureDetector;

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int ACTIVITY_DIALOG = 2;

    final List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getList(listType);
    }

    //Initial view
    private void initView(){
        top_head_me = (FrameLayout) findViewById(R.id.top_head_me);
        top_head_add = (FrameLayout) findViewById(R.id.top_head_add);
        btn_to_do = (Button) findViewById(R.id.btn_todo);
        btn_done = (Button) findViewById(R.id.btn_done);
        pb_main = (RelativeLayout)findViewById(R.id.rl_progressBar);
        list = (ListView) findViewById(R.id.task_List);
        tab_type = TO_DO;
        pb_main.setVisibility(View.VISIBLE);
        //new InitListTask().execute(listType);
        simpleAdapter = new SimpleAdapter(this,listItems,
                R.layout.item_style,
                new String[]{"pic","title",Mission.DESCRIPTION},new int[]{R.id.header,R.id.list_title,R.id.list_des});
        list.setAdapter(simpleAdapter);
        top_head_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getApplicationContext(), "me", Toast.LENGTH_SHORT);
                //toast.show();
                //Intent intent = new Intent(MainActivity.this,MeActivity.class);
                Intent intent = new Intent(MainActivity.this,MeActivity.class);
                startActivity(intent);
            }
        });

        top_head_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT);
                //toast.show();
                Intent intent = new Intent(MainActivity.this,ItemActivity.class);
                startActivityForResult(intent, ACTIVITY_CREATE);
            }
        });

        btn_to_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listType = TO_DO;
                prepareSwitch(listType);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listType = DONE;
                prepareSwitch(listType);
            }
        });

       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(MainActivity.this,ItemActivity.class);
               Map<String,Object> listItem = listItems.get(position);
               intent.putExtra("ID",listItem.get("ID").toString());
               intent.putExtra("status",listItem.get("status").toString());
               intent.putExtra(Mission.DESCRIPTION,listItem.get(Mission.DESCRIPTION).toString());
               intent.putExtra(Mission.TITLE,listItem.get(Mission.TITLE).toString());
               startActivityForResult(intent, ACTIVITY_EDIT);
           }
       });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                Map<String,Object> listItem = listItems.get(position);
                intent.putExtra("ID",listItem.get("ID").toString());
                intent.putExtra("title",listItem.get("title").toString());
                intent.putExtra("status",listItem.get("status").toString());
                startActivityForResult(intent,ACTIVITY_DIALOG);
                return true;
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void prepareSwitch(int target){
        //pb_main.setVisibility(View.VISIBLE);
        if (target == TO_DO){
            tab_type = TO_DO;
            btn_to_do.setBackground(getResources().getDrawable(R.drawable.btn_title_todo_pressed));
            btn_done.setBackground(getResources().getDrawable(R.drawable.btn_title_done_nomal));
            getList(TO_DO);
        }
        if (target == DONE){
            tab_type = DONE;
            btn_to_do.setBackground(getResources().getDrawable(R.drawable.btn_title_todo_nomal));
            btn_done.setBackground(getResources().getDrawable(R.drawable.btn_title_done_pressed));
            getList(DONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            getList(listType);
        }
    }

    private void getList(final int listType){
        listItems.clear();
        ParseQuery<Mission> query = Mission.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(Mission.AUTHOR, ParseUser.getCurrentUser());
        query.orderByAscending(Mission.TITLE);
        if (listType == DONE){
            query.whereEqualTo(Mission.STATUS, getResources().getIntArray(R.array.status)[2]);
        }else {
            query.whereNotEqualTo(Mission.STATUS, getResources().getIntArray(R.array.status)[2]);
        }
        query.findInBackground(new FindCallback<Mission>() {
            @Override
            public void done(List<Mission> missions, ParseException e) {
                pb_main.setVisibility(View.INVISIBLE);
                if (e == null){
                    if ((missions != null) && !(missions.isEmpty())){
                        listFeed(listType,missions);
                    }
                    Util.showMessage(getApplicationContext(),"Get local data success",Toast.LENGTH_SHORT);
                }else {
                    Util.showMessage(getApplicationContext(),"Get local data failed",Toast.LENGTH_SHORT);
                }
            }
        });

        if (Util.isNetworkConnected(getApplicationContext())){
            ParseQuery<Mission> queryOnLine = Mission.getQuery();
            queryOnLine.whereEqualTo(Mission.AUTHOR, ParseUser.getCurrentUser());
            queryOnLine.orderByAscending(Mission.TITLE);
            if (listType == DONE){
                queryOnLine.whereEqualTo(Mission.STATUS, getResources().getIntArray(R.array.status)[2]);
            }else {
                queryOnLine.whereNotEqualTo(Mission.STATUS, getResources().getIntArray(R.array.status)[2]);
            }
            queryOnLine.findInBackground(new FindCallback<Mission>() {
                @Override
                public void done(List<Mission> missions, ParseException e) {
                    if ((missions != null) && !(missions.isEmpty())){
                        listFeed(listType,missions);
                        ParseObject.pinAllInBackground((List<Mission>)missions, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){
                                    Util.showMessage(getApplicationContext(),"pin all success",Toast.LENGTH_SHORT);
                                }else {
                                    Util.showMessage(getApplicationContext(),"pin all failed",Toast.LENGTH_SHORT);
                                }
                            }
                        });

                    }
                    Util.showMessage(getApplicationContext(),"Get cloud data success",Toast.LENGTH_SHORT);
                }
            });
        }

    }
    private void listFeed(int listType,List<Mission> missions){
        for (Mission mission:missions){
            Map<String,Object> listItem = new HashMap<String, Object>();
            if (mission.getObjectId() == null){
                listItem.put(Mission.ID,mission.get("localId"));
            }else {
                listItem.put(Mission.ID,mission.getObjectId());
            }

            listItem.put(Mission.STATUS,mission.getStatus());
            if (listType == DONE){
                listItem.put("pic",R.drawable.ic_done);
            }else{
                listItem.put("pic",R.drawable.ic_todo);
            }
            listItem.put(Mission.TITLE,mission.getTitle());
            listItem.put(Mission.DESCRIPTION,mission.getDescription());
            if (!listItems.contains(listItem)){
                listItems.add(listItem);
            }
        }
        simpleAdapter.notifyDataSetChanged();
    }

}
