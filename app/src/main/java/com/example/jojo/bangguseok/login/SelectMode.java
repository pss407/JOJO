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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectMode extends AppCompatActivity {

    private String level;
    private String tier;
    private String name;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        MyApplication myApp = (MyApplication)getApplicationContext();


        tier = myApp.gettier();
        name = myApp.getname();


        TextView textView10 = (TextView)findViewById(R.id.textView10);
        TextView textView = (TextView)findViewById(R.id.textView);


        textView10.setText("티어:"+tier);
        textView.setText("아이디:"+name);

    }

    public void onButton5Clicked(View view)
    {
        MyApplication myApp = (MyApplication)getApplicationContext();
        Intent i = new Intent(this, LiveVideoBroadcasterActivity.class);
        i.putExtra("id", myApp.getname());
        startActivity(i);
    }

    public void onButton7Clicked(View view) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        com.example.jojo.bangguseok.login.MyApplication myApp = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();

        databaseReference.child("id_list").child(myApp.getname()).child("using").setValue("false");

    }
}
