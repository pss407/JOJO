package com.example.jojo.bangguseok.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.Glide;
import com.example.jojo.bangguseok.R;
import com.example.jojo.bangguseok.broadcast.liveVideoBroadcaster.LiveVideoBroadcasterActivity;
import com.example.jojo.bangguseok.broadcast.viewer.ViewerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

////
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectMode extends AppCompatActivity {

    private String level;
    private String tier;
    private String name;

    private Activity thisAct= this;

    ImageView load;

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

    ValueEventListener postListener;
    ValueEventListener postListener2;
    ValueEventListener postListener3;

    Query sortbyAge;

   int i=0;

   private boolean isUrl_1=true;

    public MediaPlayer m;
    public Context c;

    private String ismatching="false";


    Intent t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        music_stop();


        MyApplication myApp = (MyApplication)getApplicationContext();

        t = new Intent(this, LiveVideoBroadcasterActivity.class);
        t.putExtra("id", myApp.getname());

        tier = myApp.gettier();
        name = myApp.getname();


        TextView textView10 = (TextView)findViewById(R.id.textView10);
        TextView textView = (TextView)findViewById(R.id.textView);


        textView10.setText("티어:"+tier);
        textView.setText("아이디:"+name);


        load = (ImageView)findViewById(R.id.imageView);





    }

    public void onButton5Clicked(View view)
    {

        music_stop();

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


                postListener = new ValueEventListener() {
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
                                    MyApplication myApp = (MyApplication)getApplicationContext(); //노래 순서 두번째 할당
                                    myApp.setOrder("second");



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
                sortbyAge = FirebaseDatabase.getInstance().getReference().child("URL").child(value);
                // sortbyAge.addValueEventListener(postListener);
                sortbyAge.addListenerForSingleValueEvent(postListener);




        }


  //대기방 있는 지 탐색후에 0.8초후에 방 만들기

        Handler delayHandler3 = new Handler();
        delayHandler3.postDelayed(new Runnable() {
            @Override
            public void run() {





                if(send_url.equals(""))//대기하고있는 방채널 없으면 방파기
                {
                    i=1;


                    for(;i<=channel_count;i++) {

                        postListener3 = new ValueEventListener() {
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
                                        MyApplication myApp = (MyApplication)getApplicationContext();
                                        myApp.setOrder("first");


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
                        Query sortby2 = FirebaseDatabase.getInstance().getReference().child("URL").child(value);
                        // sortbyAge.addValueEventListener(postListener);
                        sortby2.addListenerForSingleValueEvent(postListener3);


                    }
                }


            }
        }, 800);


          //채널을 할당받았음.
        Handler delayHandler4 = new Handler();
        delayHandler4.postDelayed(new Runnable() {
            @Override
            public void run() {



                if(!num.equals(""))
                {
                    if (isUrl_1 == true) {
                        databaseReference.child("URL").child("room" + num).child("url_1").child("check").setValue("true");
                    } else {
                        databaseReference.child("URL").child("room" + num).child("url_2").child("check").setValue("true");

                    }
                }

                Toast toast = Toast.makeText(getApplicationContext(), num+" "+send_url, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 350, 200);
                toast.show();

                com.example.jojo.bangguseok.login.MyApplication myApp = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();
                myApp.setSend_url(send_url);
                myApp.setGet_url(get_url);
                myApp.setUrl_room(num);


                //Glide.with(thisAct).load(R.raw.loading).into(load); //로딩 화면




                postListener2 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        int count = 1;


                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                            String[] info = {get.check};


                            if (info[0].equals("true")) {


                                if (count == 2)
                                {

                                    ismatching="true";

                                        startActivity(t);
                                    databaseReference.child("chat").child("room" + num).setValue("");


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

                        if(ismatching.equals("false")) {
                            databaseReference.child("URL").child("room" + num).child("url_1").child("check").setValue("false");

                            Toast toast = Toast.makeText(getApplicationContext(), "현재 가능한 매칭상대가 없습니다", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP | Gravity.LEFT, 350, 200);
                            toast.show();
                        }



                    }
                }, 10000);   //이거 나중에 바꾸기




            }
        }, 1300);



/*
        MyApplication myApp = (MyApplication) getApplicationContext();
        Intent i = new Intent(this, LiveVideoBroadcasterActivity.class);
        i.putExtra("id", myApp.getname());
        startActivity(i);

 */

    }


    public void onButton7Clicked(View view) {
        music_stop();

        Intent i = new Intent(this, ViewerActivity.class);
        startActivity(i);
    }

    public void Sample_music(View view) {

        music_play();

    }

    public void music_play(){
        c = getApplicationContext();
        try {
            music_stop();
            m = MediaPlayer.create(c, R.raw.mymusic);
            m.setLooping(true);
            m.start();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    //Stop music play
    public void music_stop()
    {
        try {
            if(m != null)
            {
                m.stop();
                m.release();
                m = null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication myApp = (MyApplication) getApplicationContext();
        if(myApp.getname().equals(""))
        {
            databaseReference.child("id_list").child("error").child("using").setValue("false");

        }
        else
        {
            databaseReference.child("id_list").child(myApp.getname()).child("using").setValue("false");

        }








    }
}
