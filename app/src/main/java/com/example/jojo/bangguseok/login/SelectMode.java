package com.example.jojo.bangguseok.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;
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

    public void onButton5Clicked(View v)
    {
        MyApplication myApp = (MyApplication)getApplicationContext();
        Intent intent = new Intent(SelectMode.this, ChatActivity.class);
        intent.putExtra("id", myApp.getname());
        startActivity(intent);
        finish();
    }
}
