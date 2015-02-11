package com.missionlist.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

/**
 * 通讯录Fragment的界面
 * 
 * http://blog.csdn.net/guolin_blog/article/details/26365683
 * 
 * @author guolin
 */
public class TaskGroupFragment extends Fragment {
    //Variable
    private MListAdapter mListAdapter;
    private ListView list;
    private final static int TO_DO = 1;
    private final static int DONE = 2;
    private int listType;
    private RelativeLayout processBar;
    private RelativeLayout titleBar;

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
        getList();
        return view;
    }

    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
    }

    //Initial view
    private void initView(){
        processBar = (RelativeLayout)view.findViewById(R.id.rl_progressBar);
        titleBar = (RelativeLayout)view.findViewById(R.id.include);
        list = (ListView) view.findViewById(R.id.task_List);
        processBar.setVisibility(View.VISIBLE);
        titleBar.setVisibility(View.GONE);
        mListAdapter = new MListAdapter(activity,mList);
        list.setAdapter(mListAdapter);

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
                    }else {
                        intent.putExtra(Mission.CATEGORY,2);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            getList();
        }
    }

    public void getList(){
        ParseQuery<Mission> query = Mission.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(Mission.AUTHOR, ParseUser.getCurrentUser());
        query.whereEqualTo(Mission.IS_DELETE,false);
        query.whereNotEqualTo(Mission.CATEGORY,1);
        query.whereEqualTo(Mission.PARENT,null);

        processBar.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<Mission>() {
            @Override
            public void done(List<Mission> missions, ParseException e) {
                mList.clear();
                if (e == null){
                    if ((missions != null) && !(missions.isEmpty())){
                        Mission mission = new Mission();
                        mission.setTitle(getResources().getString(R.string.not_assigned));
                        mList.add(mission);
                    }
                    ParseQuery<Mission> query = Mission.getQuery();
                    query.fromLocalDatastore();
                    query.whereEqualTo(Mission.AUTHOR, ParseUser.getCurrentUser());
                    query.whereEqualTo(Mission.IS_DELETE,false);
                    query.whereEqualTo(Mission.CATEGORY, 1);
                    query.whereEqualTo(Mission.PARENT,null);
                    query.findInBackground(new FindCallback<Mission>() {
                        @Override
                        public void done(List<Mission> missions, ParseException e) {
                            if (e == null){
                                processBar.setVisibility(View.INVISIBLE);
                                mList.addAll(missions);
                                mListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    Util.showMessage(activity.getApplicationContext(), "Get local data success", Toast.LENGTH_SHORT);
                }else {
                    Util.showMessage(activity.getApplicationContext(),"Get local data failed",Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
