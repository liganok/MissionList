package com.missionlist.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.missionlist.ui.activity.DialogActivity;
import com.missionlist.ui.activity.ItemActivity;
import com.missionlist.R;
import com.missionlist.adapter.MListAdapter;
import com.missionlist.model.Mission;
import com.missionlist.utils.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TaskFragment extends Fragment {

    //Variable
    private Button btn_to_do;
    private Button btn_done;
    private RelativeLayout pb_main;
    private int tab_type;
    private SimpleAdapter simpleAdapter;
    private MListAdapter mListAdapter;
    private ListView list;
    private final static int TO_DO = 1;
    private final static int DONE = 2;
    private int listType;
    private GestureDetector gestureDetector;

    private static final int ACTIVITY_EDIT = 1;
    private static final int ACTIVITY_DIALOG = 2;

    private View view;
    private Activity activity;

    final List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();
    private List<Mission> mList = new ArrayList<Mission>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (view != null){
            //view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me, null);
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null){
                viewGroup.removeAllViewsInLayout();
            }
        }
        view = inflater.inflate(R.layout.fragment_task, container, false);
        initView();
        getList(listType);
        return view;
    }

    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
    }

    //Initial view
    private void initView(){
        btn_to_do = (Button) view.findViewById(R.id.btn_todo);
        btn_done = (Button) view.findViewById(R.id.btn_done);
        pb_main = (RelativeLayout)view.findViewById(R.id.rl_progressBar);
        list = (ListView) view.findViewById(R.id.task_List);
        tab_type = TO_DO;
        pb_main.setVisibility(View.VISIBLE);
        mListAdapter = new MListAdapter(activity,mList);
        list.setAdapter(mListAdapter);

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
               Intent intent = new Intent(activity,ItemActivity.class);
               Mission mission = mList.get(position);

               if (mission.getObjectId() != null){
                   intent.putExtra(Mission.ID,mission.getObjectId());
               }else{
                   if (mission.getLocalId() != null){
                       intent.putExtra(Mission.LOCAL_ID,mission.getLocalId());
                   }
               }
               startActivityForResult(intent, ACTIVITY_EDIT);
           }
       });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, DialogActivity.class);
                Mission mission = mList.get(position);

                if (mission.getObjectId() != null){
                    intent.putExtra(Mission.ID,mission.getObjectId());
                }else{
                    if (mission.getLocalId() != null){
                        intent.putExtra(Mission.LOCAL_ID,mission.getLocalId());
                    }
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            getList(listType);
        }
    }

    public void getList(final int listType){
        ParseQuery<Mission> query = Mission.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(Mission.AUTHOR, ParseUser.getCurrentUser());
        query.whereEqualTo(Mission.IS_DELETE,false);
        query.whereEqualTo(Mission.CATEGORY,0);//Only get task list
        query.orderByDescending(Mission.DUE_DATE);
        if (listType == DONE){
            query.whereEqualTo(Mission.STATUS, getResources().getIntArray(R.array.status)[2]);
        }else {
            query.whereNotEqualTo(Mission.STATUS, getResources().getIntArray(R.array.status)[2]);
        }
        pb_main.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<Mission>() {
            @Override
            public void done(List<Mission> missions, ParseException e) {
                mList.clear();
                pb_main.setVisibility(View.INVISIBLE);
                if (e == null){
                    if ((missions != null) && !(missions.isEmpty())){
                        mList.addAll(missions);
                    }
                    Util.showMessage(activity.getApplicationContext(),"Get local data success",Toast.LENGTH_SHORT);
                }else {
                    Util.showMessage(activity.getApplicationContext(),"Get local data failed",Toast.LENGTH_SHORT);
                }
                mListAdapter.notifyDataSetChanged();
            }
        });


    }

}
