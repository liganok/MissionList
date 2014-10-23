package com.missionlist.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.missionlist.MListApp;
import com.missionlist.R;
import com.missionlist.SignInActivity;
import com.parse.ParseUser;

public class MeFragment extends Fragment {

    private Button userSign;
    private TextView username_me;
    private View view;
    private Activity parentActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (view != null){
            //view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me, null);
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null){
                viewGroup.removeAllViewsInLayout();
            }
        }
        view = inflater.inflate(R.layout.fragment_me, container, false);
        initView();
        return view;
    }

    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.parentActivity = activity;
    }

    private void initView(){
        userSign = (Button)view.findViewById(R.id.btn_user);
        username_me = (TextView)view.findViewById(R.id.username_me);
        if (ParseUser.getCurrentUser() != null){
            username_me.setText(ParseUser.getCurrentUser().getUsername());
        }

        userSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentActivity,SignInActivity.class);
                startActivityForResult(intent, MListApp.REQ_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MListApp.REQ_SIGN_IN){
                ParseUser user = ParseUser.getCurrentUser();
                user.getUsername();
                username_me.setText(user.getUsername());
            }
        }
    }
}
