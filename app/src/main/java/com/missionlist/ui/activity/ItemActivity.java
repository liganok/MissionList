package com.missionlist.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.missionlist.R;
import com.missionlist.adapter.MListAdapter;
import com.missionlist.model.Mission;
import com.missionlist.utils.Util;
import com.parse.FindCallback;
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
    private EditText titleNew;
    private EditText start_date;
    private EditText due_date;
    private EditText category;
    private EditText occurrence;
    private EditText description;
    private Spinner spinnerPriority;
    private Spinner spinnerCategory;
    private TableLayout tableLayoutMore;
    private TableLayout tableLayoutBrief;
    private TableLayout tableLayoutNew;
    private ScrollView scrollView;
    private ListView list;
    private Dialog dialog;
    private Mission mMission;
    private MListAdapter mListAdapter;
    private int mCategory;

    //Menu items
    private MenuItem menuItemEdit;
    private MenuItem menuItemSave;

    private List<Mission> mList = new ArrayList<Mission>();


    private static final int MISSION_DETAIL = 3;
    private static final int MISSION_CREATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        getList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_item, menu);
        //Menu item
        menuItemEdit = menu.findItem(R.id.action_edit);
        menuItemSave = menu.findItem(R.id.action_save);
        return true;
    }

    private void initView(){
        title = (EditText)findViewById(R.id.et_new_title);
        titleNew = (EditText)findViewById(R.id.item_new_title);
        start_date = (EditText)findViewById(R.id.et_new_start_date);
        due_date = (EditText)findViewById(R.id.et_new_due_date);
        spinnerPriority = (Spinner)findViewById(R.id.spinner_priority);
        spinnerCategory = (Spinner)findViewById(R.id.spinner_category);
        occurrence = (EditText)findViewById(R.id.et_new_occurrence);
        description = (EditText)findViewById(R.id.et_new_des);
        dialog = Util.createLoadingDialog(ItemActivity.this);
        list = (ListView)findViewById(R.id.group_sub_list);
        tableLayoutMore = (TableLayout)findViewById(R.id.table_layout_more);
        tableLayoutBrief = (TableLayout)findViewById(R.id.table_layout_brief);
        tableLayoutNew = (TableLayout)findViewById(R.id.table_layout_new);
        scrollView = (ScrollView)findViewById(R.id.scrollView_item);

        //Set disable as default status
        /*title.setEnabled(false);
        start_date.setEnabled(false);
        due_date.setEnabled(false);
        description.setEnabled(false);
        spinnerCategory.setEnabled(false);
        spinnerPriority.setEnabled(false);
        occurrence.setEnabled(false);*/

        mMission = Util.getMissionObject(getIntent());
        if (mMission == null){return;}
        if (mMission.getTitle() != null){title.setText(mMission.getTitle());}
        if (mMission.getDescription() != null){description.setText(mMission.getDescription());}
        spinnerPriority.setSelection(mMission.getPriority(), true);
        spinnerCategory.setSelection(mMission.getCategory(),true);
        SimpleDateFormat    formatter    =   new SimpleDateFormat("yyyy年MM月dd日");
        if (mMission.getStartDate() != null){start_date.setText(formatter.format(mMission.getStartDate()));}
        if (mMission.getDueDate() != null){due_date.setText(formatter.format(mMission.getDueDate()));}
        if (mMission.getCategory() == 1){tableLayoutMore.setVisibility(View.GONE);}

        if (getIntent().hasExtra(Mission.CATEGORY)){
            mCategory = getIntent().getExtras().getInt(Mission.CATEGORY);
            mMission.setCategory(2);
        }else {
            mCategory = mMission.getCategory();
        }

        switch (mCategory){
            case 1:
                spinnerCategory.setEnabled(false);
                //tableLayoutMore.setVisibility(View.GONE);
                break;
            case 2:
                tableLayoutBrief.setVisibility(View.GONE);
                tableLayoutMore.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                break;
            default:
                tableLayoutNew.setVisibility(View.GONE);
        }

        mListAdapter = new MListAdapter(this,mList);
        list.setAdapter(mListAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1){
                    tableLayoutMore.setVisibility(View.GONE);
                    tableLayoutNew.setVisibility(View.VISIBLE);
                }else {
                    //tableLayoutMore.setVisibility(View.VISIBLE);
                    tableLayoutNew.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ItemActivity.this,ItemActivity.class);
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
                startActivityForResult(intent, MISSION_DETAIL);
            }
        });
    }

    public void actionSave(){
        dialog.show();
        mMission.setTitle(title.getText().toString());
        mMission.setDescription(description.getText().toString());
        mMission.setDraft(true);
        mMission.setStartDate(new Date(System.currentTimeMillis()));
        mMission.setDueDate(new Date(System.currentTimeMillis()));
        mMission.setStatus(getResources().getIntArray(R.array.status)[1]);
        mMission.setAuthor(ParseUser.getCurrentUser());
        mMission.setDelete(false);
        mMission.setCategory(spinnerCategory.getSelectedItemPosition());
        mMission.setPriority(spinnerPriority.getSelectedItemPosition());
        mMission.pinInBackground(MListApp.GROUP_NAME, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.cancel();
                if (e == null) {
                    setResult(Activity.RESULT_OK);
                    Util.showMessage(getApplicationContext(), "Save in local success", Toast.LENGTH_SHORT);
                    finish();
                } else {
                    Util.showMessage(getApplicationContext(), "Save in local failed", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                actionSave();
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit:
                actionEdit();

        }
        return true;
    }

    private void actionEdit(){
        menuItemSave.setVisible(true);
        menuItemEdit.setVisible(false);
        if (mCategory != 0){
            tableLayoutNew.setVisibility(View.VISIBLE);
        }
        title.setEnabled(true);
        start_date.setEnabled(true);
        due_date.setEnabled(true);
        description.setEnabled(true);
        spinnerCategory.setEnabled(true);
        spinnerPriority.setEnabled(true);
        occurrence.setEnabled(true);
    }

    public void getList(){
        ParseQuery<Mission> query = Mission.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(Mission.AUTHOR, ParseUser.getCurrentUser());
        query.whereEqualTo(Mission.IS_DELETE,false);
        switch (mMission.getCategory()){
            case 0:
                return;
            case 1://get sub items
                query.whereNotEqualTo(Mission.PARENT,null);
                query.whereEqualTo(Mission.PARENT,mMission);
                break;
            case 2://get tasks
                query.whereEqualTo(Mission.CATEGORY,0);
                query.whereEqualTo(Mission.PARENT,null);
                break;
        }
        //pb_main.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<Mission>() {
            @Override
            public void done(List<Mission> missions, ParseException e) {
                mList.clear();
                if (e == null){
                    mList.addAll(missions);
                    mListAdapter.notifyDataSetChanged();
                    Util.showMessage(getApplicationContext(), "Get local data success", Toast.LENGTH_SHORT);
                }else {
                    Util.showMessage(getApplicationContext(),"Get local data failed",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public void onNewSubItem(View view){
        dialog.show();
        Mission mission = new Mission();
        mission.setLocalId();
        mission.setTitle(titleNew.getText().toString());
        mission.setDraft(true);
        mission.setStartDate(new Date(System.currentTimeMillis()));
        mission.setDueDate(new Date(System.currentTimeMillis()));
        mission.setStatus(getResources().getIntArray(R.array.status)[1]);
        mission.setAuthor(ParseUser.getCurrentUser());
        mission.setDelete(false);
        mission.setCategory(0);
        mission.setPriority(1);
        titleNew.setText("");
        if(mMission.getCategory() == 1){mission.setParentId(mMission);}
        mission.pinInBackground(MListApp.GROUP_NAME, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.cancel();
                if (e == null) {
                    getList();
                    Util.showMessage(getApplicationContext(), "Save in local success", Toast.LENGTH_SHORT);

                } else {
                    Util.showMessage(getApplicationContext(), "Save in local failed", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
