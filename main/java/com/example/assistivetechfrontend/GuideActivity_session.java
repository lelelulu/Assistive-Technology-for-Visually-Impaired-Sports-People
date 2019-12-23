package com.example.assistivetechfrontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;

public class GuideActivity_session extends AppCompatActivity {
    private int sessionId;
    private int userId;
    private String name;
    private CommunicationAPI communicationAPI;
    private Boolean success = false;
    TextView tvuserId;
    TextView tvsessionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_session);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        sessionId = intent.getIntExtra("sessionId", 0);
        userId = intent.getIntExtra("userId", 0);
        communicationAPI = new CommunicationAPI();
        tvuserId = (TextView)findViewById(R.id.userId);
        tvsessionId = (TextView)findViewById(R.id.sessionId);
        tvuserId.setText(Integer.toString(userId));
        tvsessionId.setText(Integer.toString(sessionId));

    }

    public void leaveSession(View view) {
         new leaveSessionTask().execute();
    }

    class leaveSessionTask extends AsyncTask<String,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean result = false;
            try {
                result = communicationAPI.leaveSession(sessionId, userId, "http://10.0.2.2:5000/api/leaveSession");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            success = result;
            if (success){
                Toast.makeText(GuideActivity_session.this, "You leave the session successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(GuideActivity_session.this, "You failed to leave the session!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
