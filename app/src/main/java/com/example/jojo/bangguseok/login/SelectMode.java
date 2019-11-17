package com.example.jojo.bangguseok.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;
import com.example.jojo.bangguseok.broadcast.BroadcastActivity;
import com.example.jojo.bangguseok.broadcast.liveVideoBroadcaster.LiveVideoBroadcasterActivity;
import com.example.jojo.bangguseok.chatting.ChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SelectMode extends AppCompatActivity {

    private String level;
    private String tier;
    private String name;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private int channel_count=2;   //채널수 증가하면 늘리기 방하나당 1임

    private String get_url="";
    private String send_url="";

    private int room_num;
    private boolean threading=false;

    private boolean start_battle=false;

    private String num="";

    Query sortby;
    ValueEventListener postListener2;
   int i=0;

   private boolean isUrl_1=true;

    Intent t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        MyApplication myApp = (MyApplication)getApplicationContext();


        t = new Intent(this, LiveVideoBroadcasterActivity.class);
        t.putExtra("id", myApp.getname());


        tier = myApp.gettier();
        name = myApp.getname();


        TextView textView10 = (TextView)findViewById(R.id.textView10);
        TextView textView = (TextView)findViewById(R.id.textView);


        textView10.setText("티어:"+tier);
        textView.setText("아이디:"+name);




    }

    public void onButton5Clicked(View view)
    {
        send_url="";
        get_url="";

        isUrl_1=false;

        //마치 listener가 쓰레드식으로 여러개가 실행됨


        //이미 대기하고있는 방채널 있으면 합류하기
        for(i=1;i<=channel_count;i++) {



            if(!send_url.equals(""))  //이건 유저가 많을 때 효과있는듯 함 (for문 도는동안 여러 listener가 거의 동시에 실행됨)
            {
               break;
            }


                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        int count = 1;


                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                            String[] info = {get.check, get.send_url, get.get_url,get.num};


                            if (info[0].equals("true")) {
                                count++;

                            } else {
                                if (count == 2) //첫번째 url이 true이고 두번째 url이 false인 경우 할당하기
                                {

                                    send_url = info[1];
                                    get_url = info[2];
                                    num = info[3];
                                    isUrl_1=false;



                                }
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


  //대기방 있는 지 탐색후에 0.5초후에 방 만들기

        Handler delayHandler3 = new Handler();
        delayHandler3.postDelayed(new Runnable() {
            @Override
            public void run() {



                if(send_url.equals(""))//대기하고있는 방채널 없으면 방파기
                {
                    i=1;


                    for(;i<=channel_count;i++) {

                        ValueEventListener postListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {



                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                                    String[] info = {get.check, get.send_url, get.get_url, get.num};



                                    if (info[0].equals("false")) {

                                            send_url = info[1];
                                            get_url = info[2];
                                            num = info[3];
                                            isUrl_1=true;


                                            break;
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
                        sortbyAge.addListenerForSingleValueEvent(postListener);


                    }
                }


            }
        }, 500);


          //채널을 할당받았음.
        Handler delayHandler4 = new Handler();
        delayHandler4.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(isUrl_1==true)
                {
                    databaseReference.child("URL").child("room" + num).child("url_1").child("check").setValue("true");
                }
                else
                {
                    databaseReference.child("URL").child("room"+num).child("url_2").child("check").setValue("true");

                }

                Toast toast = Toast.makeText(getApplicationContext(), num+" "+send_url, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 350, 200);
                toast.show();

                com.example.jojo.bangguseok.login.MyApplication myApp = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();
                myApp.setSend_url(send_url);
                myApp.setGet_url(get_url);
                myApp.setUrl_room(num);




                postListener2 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        int count = 1;


                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                            String[] info = {get.check, get.send_url, get.get_url,get.num};


                            if (info[0].equals("true")) {


                                if (count == 2)
                                {

                                        startActivity(t);

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




                String value = "room" + num;
                // String sort_column_name = "get_url";
                sortby = FirebaseDatabase.getInstance().getReference().child("URL").child(value);
                // sortbyAge.addValueEventListener(postListener);
                sortby.addValueEventListener(postListener2);


                Handler delayHandler6 = new Handler();
                delayHandler6.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        sortby.removeEventListener(postListener2);   //리스너 그만 대기하고 정지시키기



                    }
                }, 4000);   //이거 나중에 바꾸기




            }
        }, 1000);



/*
        MyApplication myApp = (MyApplication) getApplicationContext();
        Intent i = new Intent(this, LiveVideoBroadcasterActivity.class);
        i.putExtra("id", myApp.getname());
        startActivity(i);

 */

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
