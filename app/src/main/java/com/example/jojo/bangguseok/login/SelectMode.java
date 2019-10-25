package com.example.jojo.bangguseok.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;
import com.example.jojo.bangguseok.broadcast.BroadcastActivity;
import com.example.jojo.bangguseok.broadcast.liveVideoBroadcaster.LiveVideoBroadcasterActivity;
import com.example.jojo.bangguseok.chatting.ChatActivity;

public class SelectMode extends AppCompatActivity {

    private String level;
    private String tier;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        MyApplication myApp = (MyApplication)getApplicationContext();

        level = myApp.getlevel();
        tier = myApp.gettier();
        name = myApp.getname();

        TextView textView3 = (TextView)findViewById(R.id.textView3);
        TextView textView10 = (TextView)findViewById(R.id.textView10);
        TextView textView = (TextView)findViewById(R.id.textView);

        textView3.setText("레벨:"+level);
        textView10.setText("티어:"+tier);
        textView.setText("아이디:"+name);

    }

    public void onButton5Clicked(View view)
    {
        //Intent intent = new Intent(SelectMode.this, BroadcastActivity.class);
        //startActivity(intent);
        Intent i = new Intent(this, LiveVideoBroadcasterActivity.class);
        startActivity(i);
    }

    public void onButton7Clicked(View view) {
        MyApplication myApp = (MyApplication)getApplicationContext();
        Intent intent = new Intent(SelectMode.this, ChatActivity.class);
        intent.putExtra("id", myApp.getname());
        startActivity(intent);
    }
}
