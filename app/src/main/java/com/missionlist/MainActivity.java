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
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    //Variable
    private Mission mission;
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
    //final List<Map<String,Object>> doneListItems = new_item ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
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
        new InitListTask().execute(listType);
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

        gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float x = e2.getX() - e1.getX();
                float y = e2.getY() - e1.getY();

                if (x > 0) {
                    prepareSwitch(DONE);
                } else if (x < 0) {
                    prepareSwitch(TO_DO);
                }
                return true;
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void prepareSwitch(int target){
        pb_main.setVisibility(View.VISIBLE);
        if (target == TO_DO){
            tab_type = TO_DO;
            btn_to_do.setBackground(getResources().getDrawable(R.drawable.btn_title_todo_pressed));
            btn_done.setBackground(getResources().getDrawable(R.drawable.btn_title_done_nomal));
            new InitListTask().execute(TO_DO);;
        }
        if (target == DONE){
            tab_type = DONE;
            btn_to_do.setBackground(getResources().getDrawable(R.drawable.btn_title_todo_nomal));
            btn_done.setBackground(getResources().getDrawable(R.drawable.btn_title_done_pressed));
            new InitListTask().execute(DONE);
        }
    }

    private void initAdapter(List<Map<String,Object>> listItems){
        if (simpleAdapter == null){
            simpleAdapter = new SimpleAdapter(this,listItems,
                    R.layout.item_style,
                    new String[]{"pic","title","des"},new int[]{R.id.header,R.id.list_title,R.id.list_des});
            list.setAdapter(simpleAdapter);
        }else {
            simpleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            new InitListTask().execute(listType);
        }
    }

    class InitListTask extends AsyncTask<Integer, Integer, List<Map<String,Object>>> {
        private List<Map<String,Object>> list;

        protected void onPreExecute(){
            pb_main.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Map<String,Object>> doInBackground(Integer... params) {
            return getList(params[0]);
        }

        protected void onPostExecute(List<Map<String,Object>> result) {
            pb_main.setVisibility(View.GONE);
            initAdapter(result);
        }
    }

    private List<Map<String,Object>> getList(Integer listType){
        listItems.clear();
        if(mission == null){ mission = new Mission();}
        ParseQuery<Mission> query = Mission.getQuery();
        query.orderByAscending(Mission.TITLE);
        if (listType == DONE){
            query.whereEqualTo(Mission.STATUS, getResources().getIntArray(R.array.status)[2]);
        }else {
            query.whereNotEqualTo(Mission.STATUS, getResources().getIntArray(R.array.status)[2]);
        }

        try {
            List<Mission> missions = query.find();
            for (final Mission mission1:missions){
                //Mission mission1 = missions.get(i);
                Map<String,Object> listItem = new HashMap<String, Object>();
                listItem.put("ID",mission1.getObjectId());
                listItem.put("status",mission1.getStatus());
                if (listType == DONE){
                    listItem.put("pic",R.drawable.ic_done);
                }else{
                    listItem.put("pic",R.drawable.ic_todo);
                }
                listItem.put("title",mission1.getTitle());
                listItem.put("des",mission1.getDescription());
                listItems.add(listItem);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  listItems;
    }

}
