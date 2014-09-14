package com.missionlist;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2014/9/13.
 */
@ParseClassName("")
public class Mission extends ParseObject {
    public String getTitle(){return getString("title");}
    public void  setTitle(String title) {put("title",title);}

    public Date getStartDate(){return getDate("start_date");}
    public void setStartDate(Date start_date){put("start_date",start_date);}

    public Date getDueDate(){return getDate("due_date");}
    public void setDueDate(Date due_date){put("start_time",due_date);}

    public String getCategory(){return getString("category");}
    public void  setCategory(String category) {put("category",category);}

    public String getPriority(){return getString("priority");}
    public void  setPriority(String priority) {put("priority",priority);}

    public String getOccurrence(){return getString("occurrence");}
    public void  setOccurrence(String occurrence) {put("occurrence",occurrence);}

    public String getDescription(){return getString("description");}
    public void  setDescription(String description) {put("description",description);}

    public String getStatus(){return getString("status");}
    public void  setStatus(String status) {put("status",status);}

    /*public ParseUser getAuthor() {
        return getParseUser("author");
    }
    public void setAuthor(ParseUser currentUser) {
        put("author", currentUser);
    }*/

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
