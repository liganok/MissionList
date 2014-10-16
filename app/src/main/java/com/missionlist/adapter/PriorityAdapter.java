package com.missionlist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionlist.R;
import com.missionlist.model.Mission;
import com.missionlist.model.Priority;

import java.util.List;

/**
 * Created by Administrator on 2014/10/15.
 */
public class PriorityAdapter extends BaseAdapter {

    private List<Priority> mList;
    private Context mContext;
    private LayoutInflater inflater = null;

    public PriorityAdapter(Context mContext, List<Priority> mList){
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mList == null? 0: mList.size();
    }

    public int getPosition(int ID){
        int i;
        for(i = 0;i<mList.size();i++){
            if (ID == mList.get(i).getID()){
                return i;
            }
        }
        return i;
    }

    @Override
    public Priority getItem(int position) {
        if (mList != null && mList.size()!=0){
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        View view = convertView;
        if (view == null){
            mHolder = new ViewHolder();
            view = inflater.inflate(R.layout.spinner_priority,null);
            mHolder.priorityText = (TextView)view.findViewById(R.id.spinner_priority_priority);
            view.setTag(mHolder);
        }else {
            mHolder = (ViewHolder)view.getTag();
        }

        Priority priority = getItem(position);
        if (priority != null){
            mHolder.priorityText.setText(priority.getText());
        }
        return view;
    }

    static class ViewHolder{
        TextView priorityText;
    }
}
