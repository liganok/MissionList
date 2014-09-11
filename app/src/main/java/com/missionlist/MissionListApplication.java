package com.missionlist;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Administrator on 2014/9/11.
 */
public class MissionListApplication extends Application {
    private static final String APP_ID = "zNx5pC3cWCUVI3vvPeCEXK6EJuhpe3E4GXvK3Dvt";
    private static final String Client_ID = "8OucbO7SHfnr31mDcK6BKfga0Z8RWCYUEv0yn4qJ";

    public void onCreate(){
        super.onCreate();
        //ParseObject.registerSubclass();
        // enable the Local Datastore
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, APP_ID, Client_ID);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
