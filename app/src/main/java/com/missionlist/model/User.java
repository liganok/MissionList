package com.missionlist.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2014/9/13.
 */
@ParseClassName("User")
public class User extends ParseUser {
    public static final String ID = "ID";
    public static final String EMAIL = "email";
    public static final String USERNAME = "userName";

    public String getTitle(){return getString("title");}
    public void  setTitle(String title) {put("title",title);}

    public Date getStartDate(){return getDate("start_date");}
    public void setStartDate(Date start_date){put("start_date",start_date);}

}
