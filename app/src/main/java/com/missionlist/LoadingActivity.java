package com.missionlist;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.Map;


public class LoadingActivity extends Activity {

    private Intent intent = new Intent();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initView();
    }

    private void initView(){
        switch (getIntent().getExtras().getInt(MListApp.REQ_TYPE)){
            case MListApp.REQ_ITEM_DETAIL:
                String reqArray[] = {Integer.toString(MListApp.REQ_ITEM_DETAIL),getIntent().getExtras().getString(Mission.ID)};
                Map<String,Object> result = new HashMap<String, Object>();
                new LoadingTask().execute(reqArray);
                break;
            case MListApp.REQ_ITEM_EDIT:
                new LoadingTask().execute(Integer.toString(MListApp.REQ_ITEM_EDIT));
                break;
            case MListApp.REQ_ITEM_NEW:
                new LoadingTask().execute(Integer.toString(MListApp.REQ_ITEM_NEW));
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loading, menu);
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

    //Implementation of AsyncTask
    private Map<String,Object> getItemDetail(String ID){
        Map<String,Object> mapObject = new HashMap<String, Object>();
        ParseQuery<Mission> query = Mission.getQuery();
        try {
            Mission mission = query.get(ID);
            mapObject.put(MListApp.REQ_TYPE,MListApp.REQ_ITEM_DETAIL);
            mapObject.put(Mission.TITLE,mission.getTitle());
            mapObject.put(Mission.DESCRIPTION,mission.getDescription());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mapObject;
    }

    private Map<String,Object> saveItemEdit(){
        Map<String,Object> mapObject = new HashMap<String, Object>();
        return mapObject;
    }

    private Map<String,Object> saveItemNew(){
        Map<String,Object> mapObject = new HashMap<String, Object>();
        return mapObject;
    }

    class LoadingTask extends AsyncTask<String, Integer, Map<String,Object>> {

        @Override
        protected Map<String,Object> doInBackground(String... params) {
            Map<String,Object> mapObject = new HashMap<String, Object>();
            switch (Integer.parseInt(params[0])){
                case MListApp.REQ_ITEM_DETAIL:
                    mapObject = getItemDetail(params[1]);
                    break;
                case MListApp.REQ_ITEM_EDIT:
                    mapObject = saveItemEdit();
                    break;
                case MListApp.REQ_ITEM_NEW:
                    mapObject = saveItemNew();
                    break;
            }
            return mapObject;
        }

        protected void onPostExecute(Map<String,Object> result) {
            int reqType = Integer.parseInt(result.get(MListApp.REQ_TYPE).toString());
            if (reqType == MListApp.REQ_ITEM_DETAIL){
                intent.putExtra(Mission.TITLE, result.get(Mission.TITLE).toString());
                intent.putExtra(Mission.DESCRIPTION, result.get(Mission.DESCRIPTION).toString());
                setResult(Activity.RESULT_OK,intent);
                LoadingActivity.this.finish();
            }
        }
    }
}
