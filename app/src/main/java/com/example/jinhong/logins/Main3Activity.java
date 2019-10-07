package com.example.jinhong.logins;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {


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
}
