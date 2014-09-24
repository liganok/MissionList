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
@ParseClassName("Mission")
public class Mission extends ParseObject {
    public static final String ID = "ID";
    public static final String TITLE = "title";
    public static final String START_DATE = "start_date";
    public static final String DUE_DATE = "due_date";
    public static final String CATEGORY = "category";
    public static final String PRIORITY = "priority";
    public static final String DESCRIPTION = "description";
    public static final String IS_DRAFT = "isDraft";
    public static final String STATUS = "status";
    public static final String OCCURRENCE = "occurrence";
    public static final String USERNAME = "userName";

    public String getTitle(){return getString("title");}
    public void  setTitle(String title) {put("title",title);}

    public Date getStartDate(){return getDate("start_date");}
    public void setStartDate(Date start_date){put("start_date",start_date);}

    public Date getDueDate(){return getDate("due_date");}
    public void setDueDate(Date due_date){put("start_time",due_date);}

    public String getCategory(){return getString("category");}
    public void  setCategory(String category) {put("category",category);}

    public int getPriority(){return getInt("priority");}
    public void  setPriority(int priority) {put("priority",priority);}

    public String getOccurrence(){return getString("occurrence");}
    public void  setOccurrence(String occurrence) {put("occurrence",occurrence);}

    public String getDescription(){return getString("description");}
    public void  setDescription(String description) {put("description",description);}

    public int getStatus(){return getInt("status");}
    public void  setStatus(int status) {put("status",status);}

    public boolean getDraft(){return getBoolean("isDraft");}
    public void  setDraft(boolean isDraft) {put("isDraft",isDraft);}

    public ParseUser getAuthor() {return getParseUser("username"); }
    public void setAuthor(ParseUser currentUser) {put("username", currentUser); }

    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put("uuid", uuid.toString());
    }
    public String getUuidString() {
        return getString("uuid");
    }

    public static ParseQuery<Mission> getQuery() {
        return ParseQuery.getQuery(Mission.class);
    }

}
