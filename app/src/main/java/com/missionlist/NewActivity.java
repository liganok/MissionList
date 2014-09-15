package com.missionlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.missionlist.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewActivity extends Activity {
    private Mission mission;
    private String missionId = null;
    private EditText title;
    private EditText start_date;
    private EditText due_date;
    private EditText category;
    private EditText priority;
    private EditText occurrence;
    private EditText description;
    private FrameLayout save;
    private FrameLayout close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        initView();
    }

    private void initView(){
         //todo initial the date for view

        save = (FrameLayout)findViewById(R.id.fl_head_save);
        close = (FrameLayout)findViewById(R.id.fl_head_close);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                title = (EditText)findViewById(R.id.et_new_title);
                start_date = (EditText)findViewById(R.id.et_new_start_date);
                due_date = (EditText)findViewById(R.id.et_new_due_date);
                priority = (EditText)findViewById(R.id.et_priority);
                occurrence = (EditText)findViewById(R.id.et_new_occurrence);
                description = (EditText)findViewById(R.id.et_new_des);

                if(mission == null){
                    mission = new Mission();
                }
                mission.setTitle(title.getText().toString());
                mission.setPriority(priority.getText().toString());
                mission.setOccurrence(occurrence.getText().toString());
                mission.setDescription(description.getText().toString());
               // mission.setAuthor(ParseUser.getCurrentUser());
                mission.setStatus("New");
                mission.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (isFinishing()) {
                            return;
                        }
                        if (e == null) {
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
