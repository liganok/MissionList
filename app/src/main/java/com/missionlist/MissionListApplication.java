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
    private static final String APPLICATION_ID = "zNx5pC3cWCUVI3vvPeCEXK6EJuhpe3E4GXvK3Dvt";
    private static final String CLIENT_KEY = "8OucbO7SHfnr31mDcK6BKfga0Z8RWCYUEv0yn4qJ";
    public static final String MISSION = "Mission";

    public void onCreate(){
        super.onCreate();
        ParseObject.registerSubclass(Mission.class);

        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
