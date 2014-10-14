package com.missionlist;

import android.app.Application;

import com.missionlist.model.Mission;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Administrator on 2014/9/11.
 */
public class MListApp extends Application {
    private static final String APPLICATION_ID = "zNx5pC3cWCUVI3vvPeCEXK6EJuhpe3E4GXvK3Dvt";
    private static final String CLIENT_KEY = "8OucbO7SHfnr31mDcK6BKfga0Z8RWCYUEv0yn4qJ";
    public static final String MISSION = "Mission";

    //Label for parse object
    public static final String GROUP_NAME = "ALL_MISSIONS";
    public static final String GROUP_DELETE = "DELETE";

    public static final String REQ_TYPE =  "REQ_TYPE";
    public static final int REQ_ITEM_DETAIL =  0;
    public static final int REQ_ITEM_NEW =  1;
    public static final int REQ_ITEM_EDIT =  2;
    public static final int REQ_SIGN_UP = 3;
    public static final int REQ_SIGN_IN = 4;
    public static final String REQ_STATUS = "STATUS";

    public void onCreate(){
        super.onCreate();
        ParseObject.registerSubclass(Mission.class);
        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
