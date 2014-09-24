package com.missionlist.com.missionlist.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.missionlist.R;

/**
 * Created by Administrator on 2014/9/23.
 */
public class Util {
    public static Dialog createLoadingDialog(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.activity_loading,null);
        Dialog loadingDialog = new Dialog(context,R.style.Loading_dialog);
        loadingDialog.setContentView(R.layout.activity_loading);
        loadingDialog.setCancelable(false);
        return loadingDialog;
    }

    public static int getNetworkStatus(){
        return 0;
    }

    public static void showMessage(String msg){

    }





























}
