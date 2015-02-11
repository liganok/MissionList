package com.missionlist.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.missionlist.R;
import com.missionlist.model.Mission;
import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * Created by Administrator on 2014/9/23.
 */
public class Util {
    public static Dialog createLoadingDialog(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading,null);
        Dialog loadingDialog = new Dialog(context,R.style.Loading_dialog);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.setCancelable(true);
        return loadingDialog;
    }

    public static int getNetworkStatus(){
        return 0;
    }

    public static void showMessage( Context context,String msg, int duration){
        Toast toast=Toast.makeText(context, msg, duration);
        toast.show();
    }

    public static Boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public static Mission getMissionObject(Intent intent){
        String ID = null ;
        String LOCAL_ID = null;
        Mission mission = null;
        if (intent.hasExtra(Mission.ID)){
            ID = intent.getExtras().getString(Mission.ID);
        }
        if (intent.hasExtra(Mission.LOCAL_ID)){
            LOCAL_ID = intent.getExtras().getString(Mission.LOCAL_ID);
        }

        if (ID != null){
            //dialog.show();
            ParseQuery<Mission> query = Mission.getQuery();
            query.fromLocalDatastore();
            //query.whereEqualTo(Mission.ID, ID);
            try {
                mission = query.get(ID);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            if (LOCAL_ID != null){
                //dialog.show();
                ParseQuery<Mission> query = Mission.getQuery();
                query.fromLocalDatastore();
                query.whereEqualTo(Mission.LOCAL_ID,LOCAL_ID);
                try {
                    mission = query.getFirst();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                mission = new Mission();
                mission.setLocalId();
            }
        }
        return mission;
    }





























}
