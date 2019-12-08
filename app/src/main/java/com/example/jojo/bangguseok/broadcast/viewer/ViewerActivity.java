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
import com.example.jojo.bangguseok.broadcast.liveVideoBroadcaster.LiveVideoBroadcasterActivity;
import com.example.jojo.bangguseok.login.FirebasePost_url;
import com.example.jojo.bangguseok.login.MyApplication;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;

public class ViewerActivity extends AppCompatActivity {

    private ListView chat_list;

    private int channel_count=2;

    String strText;

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

                strText="";

                String str_tmp = (String)parent.getItemAtPosition(position);

                //StringTokenizer st1 = new StringTokenizer(str_tmp,"");

               // strText=st1.nextToken();
                for(int i=0;i<5;i++)
                {
                    strText+=str_tmp.charAt(i);
                }

//////////////////////////////

                for(int i=1;i<=channel_count;i++) {
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int count=1;


                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String key = postSnapshot.getKey();
                                FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                                String[] info = {get.check, get.send_url, get.get_url, get.num};



                                com.example.jojo.bangguseok.login.MyApplication myApp = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();

                                if (strText.equals("room"+info[3])) {
                                    myApp.setUrl_room(info[3]);

                                    if(count==1) {
                                        myApp.setSend_url(info[2]);
                                    }
                                    else
                                    {
                                        myApp.setGet_url(info[2]);
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
                    // String sort_column_name = "get_url";
                    Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("URL").child(value);
                    // sortbyAge.addValueEventListener(postListener);
                    sortbyAge.addListenerForSingleValueEvent(postListener);


                }

////////////////////////////////////

                Handler delayHandler6 = new Handler();
                delayHandler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(ViewerActivity.this, LiveViewerActivity.class);
                        i.putExtra("room", strText);
                        startActivity(i);

                    }
                }, 1500);   //이거 나중에 바꾸기

            }
        });

    }

    private void showChatList() {

        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.musiclist);
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
                adapter.remove(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
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
