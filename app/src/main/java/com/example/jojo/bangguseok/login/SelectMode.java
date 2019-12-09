package com.example.jojo.bangguseok.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.jojo.bangguseok.R;
import com.example.jojo.bangguseok.broadcast.liveVideoBroadcaster.LiveVideoBroadcasterActivity;
import com.example.jojo.bangguseok.broadcast.viewer.ViewerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//import com.bumptech.glide.Glide;
////

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

    AlertDialog alertDialog;

   int i=0;

   private boolean isUrl_1=true;

    public MediaPlayer m;
    public Context c;

    String listener2_stop="false";

    private String ismatching="false";

    String tmp;

    String music_title1;
    String music_title2;

    Intent t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        this.InitializeLayout();
        music_stop();

        MyApplication myApp = (MyApplication)getApplicationContext();

        tier = myApp.gettier();
        name = myApp.getname();


        MyApplication myApp2 = (MyApplication)getApplicationContext();

        t = new Intent(this, LiveVideoBroadcasterActivity.class);
        t.putExtra("id", myApp2.getname());


        TextView Tier = (TextView)findViewById(R.id.Tier);
        TextView Id = (TextView)findViewById(R.id.Id);
        TextView Exp = (TextView)findViewById(R.id.Exp);


        int exper = Integer.parseInt(myApp2.getExperience());
        int exper_tier= exper/100;
        exper = exper%100;

        if(exper_tier==0)tier="Bronze";
        else if(exper_tier==1)tier="Silver";
        else if(exper_tier==2)tier="Gold";
        else if(exper_tier==3)tier="Platinum";
        else tier="Master";

        Tier.setText("티어:"+tier);
        Id.setText("아이디:"+name);
        Exp.setText("경험치:"+ exper+"%");
    }
//
    public void onButton5Clicked(View view)
    {


        MyApplication myApp = (MyApplication)getApplicationContext();
        String music_title= myApp.getMusic_title();
        if(music_title.equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "먼저 노래를 선택하세요", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER , 0, 0);
            toast.show();

            return;
        }


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
                                    myApp.setOrder("2");



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
                                        myApp.setOrder("1");


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


                final ProgressDialog progressDialog = new ProgressDialog(SelectMode.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("매칭중 입니다");
                progressDialog.show();


                com.example.jojo.bangguseok.login.MyApplication myApp = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();
                myApp.setSend_url(send_url);
                myApp.setGet_url(get_url);
                myApp.setUrl_room(num);

                MyApplication myApp3 = (MyApplication)getApplicationContext();
                String num3= myApp3.getUrl_room();
                tmp=num3;

                //노래제목 url db에 넣기
                MyApplication myApp9 = (MyApplication)getApplicationContext();
                String music_title_tmp=myApp9.getMusic_title();

                databaseReference.child("URL").child("room" + tmp).child("url_"+myApp9.getOrder()).child("music_title").setValue(music_title_tmp);


                if(!num.equals(""))
                {
                    if (isUrl_1 == true) {
                        databaseReference.child("URL").child("room" + num).child("url_1").child("check").setValue("true");
                    } else {
                        databaseReference.child("URL").child("room" + num).child("url_2").child("check").setValue("true");

                    }
                }

                if(send_url.length()>3)  //url잘 받아왔으면 그다음 진행
                {




                    ///

                    if(myApp9.getOrder().equals("1"))
                    {


                        postListener2 = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                int count = 1;

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                                    String[] info = {get.check, get.music_title};

                                    if(count==1)
                                    {
                                        music_title1=info[1];
                                    }


                                    if (info[0].equals("true")) {


                                        if (count == 2&&listener2_stop.equals("false"))
                                        {
                                            music_title2=info[1];

                                            MyApplication myApp5 = (MyApplication)getApplicationContext();
                                            myApp5.setMusic_title1(music_title1);
                                            myApp5.setMusic_title2(music_title2);


                                            ismatching="true";
                                            progressDialog.cancel();
                                            listener2_stop="true";

                                            startActivity(t);
                                           // databaseReference.child("chat").child("room" + num+" "+music_title1+" "+music_title2).setValue("");


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


                    }
                    else
                    {
                        postListener2 = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                int count = 1;


                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                                    String[] info = {get.check, get.music_title};

                                    if(count==1)
                                    {
                                        music_title1=info[1];
                                    }


                                    if (info[0].equals("true")) {


                                        if (count == 2&&listener2_stop.equals("false"))
                                        {
                                            music_title2=info[1];
                                            listener2_stop="true";

                                            MyApplication myApp5 = (MyApplication)getApplicationContext();
                                            myApp5.setMusic_title1(music_title1);
                                            myApp5.setMusic_title2(music_title2);

                                            ismatching="true";
                                            progressDialog.cancel();

                                            startActivity(t);
                                            databaseReference.child("chat").child("room" + num+": "+music_title1+" vs "+music_title2).setValue("");


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

                    }




                    Handler delayHandler6 = new Handler();
                    delayHandler6.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            sortby.removeEventListener(postListener2);   //리스너 그만 대기하고 정지시키기

                            if(ismatching.equals("false")) {
                                databaseReference.child("URL").child("room" + num).child("url_1").child("check").setValue("false");

                                progressDialog.cancel();
                                Toast toast = Toast.makeText(getApplicationContext(), "현재 가능한 매칭상대가 없습니다", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER , 0, 0);
                                toast.show();

                            }



                        }
                    }, 10000);   //이거 나중에 바꾸기

                }
                else   //url 잘못받아왔으면
                {
                    progressDialog.cancel();
                    Toast toast = Toast.makeText(getApplicationContext(), "매칭 실패", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER , 0, 0);
                    toast.show();
                    progressDialog.cancel();

                    databaseReference.child("URL").child("room" + num).child("url_1").child("check").setValue("false");
                    databaseReference.child("URL").child("room" + num).child("url_2").child("check").setValue("false");

                }





            }
        }, 1300);



/*
        MyApplication myApp = (MyApplication) getApplicationContext();
        Intent i = new Intent(this, LiveVideoBroadcasterActivity.class);
        i.putExtra("id", myApp.getname());
        startActivity(i);

 */

    }


    public void  music_choice(View view) {

        Intent i = new Intent(this, com.example.jojo.bangguseok.music_lists.class);
        startActivity(i);
    }

    public void onButton7Clicked(View view) {
        music_stop();

        Intent i = new Intent(this, ViewerActivity.class);
        startActivity(i);
    }



    public void Sample_music(View view) {

        music_play();

        Handler delayHandler6 = new Handler();
        delayHandler6.postDelayed(new Runnable() {
            @Override
            public void run() {

           music_stop();

            }
        }, 20000);   //이거 나중에 바꾸기

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


    public void InitializeLayout()
    {
        //toolBar를 통해 App Bar 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //App Bar의 좌측 영영에 Drawer를 Open 하기 위한 Incon 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_24px);

        DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawLayout,
                toolbar,
                R.string.open,
                R.string.closed
        );

        drawLayout.addDrawerListener(actionBarDrawerToggle);
    }

    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ismatching="false";
        threading=false;

        start_battle=false;
        num="";
        i=0;
        isUrl_1=true;
        listener2_stop="false";

    }

    @Override
    protected void onStop() {
        super.onStop();





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

         music_stop();

    }
}
