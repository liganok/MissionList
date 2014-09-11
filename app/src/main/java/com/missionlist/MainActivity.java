package com.missionlist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
    private int tab_type;
    private SimpleAdapter simpleAdapter;
    private ListView list;
    private final static int TO_DO = 1;
    private final static int DONE = 2;
    private GestureDetector gestureDetector;


    final List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();
    //final List<Map<String,Object>> doneListItems = new_item ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initList();
        initAdapter();
    }

    //Initial view
    private void initView(){
        top_head_me = (FrameLayout) findViewById(R.id.top_head_me);
        top_head_add = (FrameLayout) findViewById(R.id.top_head_add);
        btn_to_do = (Button) findViewById(R.id.btn_todo);
        btn_done = (Button) findViewById(R.id.btn_done);
        tab_type = TO_DO;
        top_head_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getApplicationContext(), "me", Toast.LENGTH_SHORT);
                //toast.show();
                Intent intent = new Intent(MainActivity.this,MeActivity.class);
                startActivity(intent);
            }
        });

        top_head_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT);
                //toast.show();
                Intent intent = new Intent(MainActivity.this,NewActivity.class);
                startActivity(intent);
            }
        });

        btn_to_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareSwitch(TO_DO);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareSwitch(DONE);
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
        if (target == TO_DO){
            tab_type = TO_DO;
            btn_to_do.setBackground(getResources().getDrawable(R.drawable.btn_title_todo_pressed));
            btn_done.setBackground(getResources().getDrawable(R.drawable.btn_title_done_nomal));
            //btn_to_do.setBackgroundColor(getResources().getColor(R.color.bt_title_pressed));
            //btn_done.setBackgroundColor(getResources().getColor(R.color.bt_title_nomal));
            initList();
            simpleAdapter.notifyDataSetChanged();
        }
        if (target == DONE){
            tab_type = DONE;
            btn_to_do.setBackground(getResources().getDrawable(R.drawable.btn_title_todo_nomal));
            btn_done.setBackground(getResources().getDrawable(R.drawable.btn_title_done_pressed));
            //btn_to_do.setBackgroundColor(getResources().getColor(R.color.bt_title_nomal));
            //btn_done.setBackgroundColor(getResources().getColor(R.color.bt_title_pressed));
            initList();
            simpleAdapter.notifyDataSetChanged();
        }
    }
    private void initList(){
        if (tab_type == TO_DO){
            getToDoList();
        }else if (tab_type == DONE){
            getDoneList();
        }
    }

    private void getToDoList(){
        listItems.clear();
        for (int i = 0; i<10; i++){
            Map<String,Object> listItem = new HashMap<String, Object>();
            listItem.put("pic",R.drawable.ic_todo);
            listItem.put("title","Title_todo"+ i + 1);
            listItem.put("des","Description"+ i + 1);
            listItems.add(listItem);
        }
    }

    private void getDoneList(){
        listItems.clear();
        for (int i = 0; i<10; i++){
            Map<String,Object> listItem = new HashMap<String, Object>();
            listItem.put("pic",R.drawable.ic_done);
            listItem.put("title","Title_Done"+ i + 1);
            listItem.put("des","Description"+ i + 1);
            listItems.add(listItem);
        }
    }

    private void initAdapter(){
        simpleAdapter = new SimpleAdapter(this,listItems,
                R.layout.item_style,
                new String[]{"pic","title","des"},new int[]{R.id.header,R.id.list_title,R.id.list_des});
        list = (ListView) findViewById(R.id.task_List);
        list.setAdapter(simpleAdapter);
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
}
