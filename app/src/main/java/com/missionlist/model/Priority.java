package com.missionlist.model;

/**
 * Created by Administrator on 2014/10/16.
 */
public class Priority {
    private int ID;
    private String text;
    public Priority(int ID, String text){
        super();
        this.ID = ID;
        this.text = text;
    }
    public int getID(){
        return ID;
    }
    public String getText(){
        return text;
    }
    public void setID(int ID){
        this.ID = ID;
    }

    public void setText(String text){
        this.text = text;
    }






















}
