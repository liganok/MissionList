package com.missionlist.util;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.missionlist.R;

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





























}
