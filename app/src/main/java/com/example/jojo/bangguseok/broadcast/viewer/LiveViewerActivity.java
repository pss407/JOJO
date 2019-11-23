package com.example.jojo.bangguseok.broadcast.viewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;
import com.example.jojo.bangguseok.chatting.ChatAdapter;
import com.example.jojo.bangguseok.chatting.ChatVO;
import com.example.jojo.bangguseok.login.MyApplication;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LiveViewerActivity extends AppCompatActivity {
    ArrayList<ChatVO> list = new ArrayList<>();
    ListView lv;
    Button btn;
    EditText edt;
    int imageID = R.drawable.profile;

    String id = "";
    String message = "chat";
    String room = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_video_viewer);






        Handler delayHandler6 = new Handler();
        delayHandler6.postDelayed(new Runnable() {
            @Override
            public void run() {

                com.example.jojo.bangguseok.login.MyApplication myApp2 = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();

                Toast toast = Toast.makeText(getApplicationContext(), myApp2.getGet_url(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 350, 200);
                toast.show();



            }
        }, 2000);   //이거 나중에 바꾸기



        lv = findViewById(R.id.chat_list2);
        edt = findViewById(R.id.chat_message2);
        btn = findViewById(R.id.bnt_send2);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(message);

//로그인한 아이디
        MyApplication myApp = (MyApplication)getApplicationContext();
        id = myApp.getname();
        room = getIntent().getStringExtra("room");

        final ChatAdapter adapter = new ChatAdapter(getApplicationContext(), R.layout.talklist, list, id);
        ((ListView) findViewById(R.id.chat_list2)).setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    Date today = new Date();
                    SimpleDateFormat timeNow = new SimpleDateFormat("a K:mm");

                    StringBuffer sb = new StringBuffer(edt.getText().toString());
                    if (sb.length() >= 15) {
                        for (int i = 1; i <= sb.length() / 15; i++) {
                            sb.insert(15 * i, "\n");
                        }
                    }

//list.add(new ChatVO(R.drawable.profile1, id, sb.toString(), timeNow.format(today)));
//adapter.notifyDataSetChanged();

                    myRef.child(room).push().setValue(new ChatVO(R.drawable.profile, id, sb.toString(), timeNow.format(today)));
                    edt.setText("");
                }
            }
        });

        myRef.child(room).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatVO value = dataSnapshot.getValue(ChatVO.class); // 괄호 안 : 꺼낼 자료 형태
                list.add(value);
                adapter.notifyDataSetChanged();
                lv.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}


