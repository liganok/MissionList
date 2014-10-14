package com.missionlist.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2014/9/13.
 */
@ParseClassName("Mission")
public class Mission extends ParseObject implements Serializable {
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String START_DATE = "START_DATE";
    public static final String DUE_DATE = "DUE_DATE";
    public static final String CATEGORY = "CATEGORY";
    public static final String PRIORITY = "PRIORITY";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String IS_DRAFT = "IS_DRAFT";
    public static final String IS_DELETE = "IS_DELETE";
    public static final String STATUS = "STATUS";
    public static final String OCCURRENCE = "OCCURRENCE";
    public static final String AUTHOR = "AUTHOR";
    public static final String LOCAL_ID = "LOCAL_ID";


    public String getTitle(){return getString(Mission.TITLE);}
    public void  setTitle(String title) {if(title != null){put(Mission.TITLE,title);}}

    public Date getStartDate(){return getDate(Mission.START_DATE);}
    public void setStartDate(Date start_date){if (start_date != null){put(Mission.START_DATE,start_date);}}

    public Date getDueDate(){return getDate(Mission.DUE_DATE);}
    public void setDueDate(Date due_date){if (due_date != null){put(Mission.DUE_DATE,due_date);}}

    public String getCategory(){return getString(Mission.CATEGORY);}
    public void  setCategory(String category) {if(category != null){put(Mission.CATEGORY,category);}}

    public int getPriority(){return getInt(Mission.PRIORITY);}
    public void  setPriority(int priority) {put(Mission.PRIORITY,priority);}

    public String getOccurrence(){return getString(Mission.OCCURRENCE);}
    public void  setOccurrence(String occurrence) {if (occurrence != null){put(Mission.OCCURRENCE,occurrence);}}

    public String getDescription(){return getString(Mission.DESCRIPTION);}
    public void  setDescription(String description) {if (description != null){put(Mission.DESCRIPTION,description);}}

    public int getStatus(){return getInt(Mission.STATUS);}
    public void  setStatus(int status) {put(Mission.STATUS,status);}

    public boolean getDraft(){return getBoolean(Mission.IS_DRAFT);}
    public void  setDraft(boolean isDraft) {put(Mission.IS_DRAFT,isDraft);}

    public boolean getDelete(){return getBoolean(Mission.IS_DELETE);}
    public void  setDelete(boolean isDelete) {put(Mission.IS_DELETE,isDelete);}

    public ParseUser getAuthor() {return getParseUser(Mission.AUTHOR); }
    public void setAuthor(ParseUser currentUser) {if (currentUser != null){put(Mission.AUTHOR, currentUser);}}

    public void setLocalId() {
        UUID uuid = UUID.randomUUID();
        put(Mission.LOCAL_ID, uuid.toString());
    }
    public String getLocalId() {
        return getString(Mission.LOCAL_ID);
    }

    public static ParseQuery<Mission> getQuery() {
        return ParseQuery.getQuery(Mission.class).whereEqualTo(Mission.AUTHOR, ParseUser.getCurrentUser());
    }

}
