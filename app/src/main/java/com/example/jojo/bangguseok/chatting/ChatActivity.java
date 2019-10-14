package com.example.jojo.bangguseok.chatting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatActivity extends AppCompatActivity {

    ArrayList<com.example.jojo.bangguseok.chatting.ChatVO> list = new ArrayList<>();
    ListView lv;
    Button btn;
    EditText edt;
    int imageID = R.drawable.profile;//int[] imageID = {R.drawable.profile1, R.drawable.profile2, R.drawable.profile3};

    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_test);

/* 텍스트뷰만 있을때만 쓸수있어
ArrayAdapter<ChatVO> adapter = new ArrayAdapter<ChatVO>(getApplicationContext(), R.layout.talklist, list);*/

        lv = findViewById(R.id.listView);
        edt = findViewById(R.id.editText);
        btn = findViewById(R.id.bnt_send);

// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("message");


//로그인한 아이디
        id = getIntent().getStringExtra("id");

//list.add(new ChatVO(R.drawable.profile3, "찡찡이", "안녕", "오후 4:42"));

        final com.example.jojo.bangguseok.chatting.ChatAdapter adapter = new com.example.jojo.bangguseok.chatting.ChatAdapter(getApplicationContext(), R.layout.talklist, list, id);
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);

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

                    myRef.push().setValue(new com.example.jojo.bangguseok.chatting.ChatVO(R.drawable.profile, id, sb.toString(), timeNow.format(today)));
                    edt.setText("");
                }
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                com.example.jojo.bangguseok.chatting.ChatVO value = dataSnapshot.getValue(com.example.jojo.bangguseok.chatting.ChatVO.class); // 괄호 안 : 꺼낼 자료 형태
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