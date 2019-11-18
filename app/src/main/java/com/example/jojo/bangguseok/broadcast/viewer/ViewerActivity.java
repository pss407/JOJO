package com.example.jojo.bangguseok.broadcast.viewer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;
import com.example.jojo.bangguseok.login.FirebasePost_url;
import com.example.jojo.bangguseok.login.MyApplication;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewerActivity extends AppCompatActivity {

    private ListView chat_list;

    private int channel_count=2;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);


        chat_list = (ListView) findViewById(R.id.chat_list);

        showChatList();

        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String strText = (String)parent.getItemAtPosition(position);
                Intent i = new Intent(ViewerActivity.this, LiveViewerActivity.class);
                i.putExtra("room", strText);
                startActivity(i);
            }
        });

    }

    private void showChatList() {


 /*
        for(int i=1;i<=channel_count;i++)
        {



            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {



                    int count=1;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                        String[] info = {get.check, get.send_url, get.get_url, get.num};



                        if(info[0].equals("true"))
                        {
                            if(count==2)
                            {


                                databaseReference.child("chat").child("room" + info[3]).setValue("");
                            }
                            count++;
                        }




                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());
                }
            };
            //채널 늘리면 room에 숫자 증가시키며 더하면됨
            String value = "room" + i; //room번호 증가시키며 탐색
            //String sort_column_name = "get_url";
            Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("URL").child(value);
            // sortbyAge.addValueEventListener(postListener);
            sortbyAge.addValueEventListener(postListener);

        }


*/



        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_list.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        myRef.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
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
