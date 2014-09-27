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
    public static final String AUTHOR = "author";

    public ParseUser getAuthor() {return getParseUser("author"); }
    public void setAuthor(ParseUser currentUser) {put("author", currentUser); }

}
