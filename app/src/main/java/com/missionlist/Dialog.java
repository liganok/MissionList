package com.missionlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;


public class Dialog extends Activity {
    private TextView tv_dialog_title;
    private TextView tv_dialog_action;
    private RelativeLayout rl_dialog_set_status;
    private RelativeLayout rl_dialog_delete;

    private String status;
    private String ID;
    private Mission mission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initView();
    }

    private void initView(){
        tv_dialog_title = (TextView)findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText(getIntent().getExtras().getString("title"));
        tv_dialog_action = (TextView)findViewById(R.id.tv_dialog_action);
        rl_dialog_set_status = (RelativeLayout)findViewById(R.id.rl_dialog_set_status);
        rl_dialog_delete = (RelativeLayout)findViewById(R.id.rl_dialog_delete);
        status = getIntent().getExtras().getString("status");
        ID = getIntent().getExtras().getString("ID");
        if (status == getResources().getStringArray(R.array.status)[2]){
            tv_dialog_action.setText("Reset to in process");
        }else{
            tv_dialog_action.setText("Set to done");
        }

        rl_dialog_set_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == getResources().getStringArray(R.array.status)[2]){
                    if (mission == null){
                        mission = new Mission();
                    }
                    ParseQuery<Mission> query = Mission.getQuery();
                    query.whereEqualTo("objectId",ID);
                    query.getFirstInBackground(new GetCallback<Mission>() {
                        @Override
                        public void done(Mission object, ParseException e) {
                            if (!isFinishing()) {
                                mission = object;
                                mission.setStatus(getResources().getStringArray(R.array.status)[1]);
                                setResult(Activity.RESULT_OK);
                                finish();
                             }
                        }
                    });

                }else{
                    if (mission == null){
                        mission = new Mission();
                    }
                    ParseQuery<Mission> query = Mission.getQuery();
                    query.whereEqualTo("objectId",ID);
                    query.getFirstInBackground(new GetCallback<Mission>() {
                        @Override
                        public void done(Mission object, ParseException e) {
                            if (!isFinishing()) {
                                mission = object;
                                mission.setStatus(getResources().getStringArray(R.array.status)[2]);
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dialog, menu);
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
