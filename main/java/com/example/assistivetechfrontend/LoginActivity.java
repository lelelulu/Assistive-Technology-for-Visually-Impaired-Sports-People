package com.example.assistivetechfrontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.MalformedURLException;

public class LoginActivity extends AppCompatActivity {

    private String name;
    private int id;
    private EditText enterName;
    /*private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            id = (int) msg.obj;
        }
    };*/
    //CommunicationAPI communicationAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        enterName = (EditText) findViewById(R.id.inputName);

        //communicationAPI = new CommunicationAPI();
    }


    public void fortest(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void guideMode(View view) {
        //getId();
        //System.out.println(name);
        //System.out.println(id);
        name = enterName.getText().toString();
        Intent intent = new Intent(this, GuideActivity_loading.class);
        intent.putExtra("name", name);
        //intent.putExtra("id", id);
        startActivity(intent);
    }

    public void vipMode(View view) {
        //getId();
        name = enterName.getText().toString();
        Intent intent = new Intent(this, VipActivity_loading.class);
        intent.putExtra("name", name);
        //intent.putExtra("id", id);
        startActivity(intent);
    }

    /* leave
    private void getId(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int id = communicationAPI.createUser(name, "utf-8", "http://10.0.2.2:5000/api/createUser");
                    Message message = new Message();
                    message.obj = id;
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}
