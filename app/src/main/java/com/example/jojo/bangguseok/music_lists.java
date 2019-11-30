package com.example.jojo.bangguseok;

import android.media.AudioManager;
import android.media.MediaPlayer;
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

import com.example.jojo.bangguseok.login.FirebasePost_music;
import com.example.jojo.bangguseok.login.MyApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class music_lists extends AppCompatActivity {

    private ArrayList<HashMap<String,String>>Data=new ArrayList<HashMap<String,String>>();
    private HashMap<String,String>InputData1 = new HashMap<>();
    private ListView listView;
    private StorageReference mStorageRef;
    MediaPlayer m;
    AudioManager am;

    int music_num=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_lists);


        mStorageRef = FirebaseStorage.getInstance().getReference();




        listView=(ListView)findViewById(R.id.listview);

        final ArrayList<String> list = new ArrayList<>();



            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    int count = 1;

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        FirebasePost_music get = postSnapshot.getValue(FirebasePost_music.class);
                        String[] info = {get.title};

                        list.add(info[0]);


                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());

                }
            };


            //String value = "music" + i;
            Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("music");
            sortbyAge.addListenerForSingleValueEvent(postListener);




   

        Handler delayHandler6 = new Handler();
        delayHandler6.postDelayed(new Runnable() {
            @Override
            public void run() {

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        music_lists.this, //context(액티비티 인스턴스)
                        android.R.layout.simple_list_item_1, // 한 줄에 하나의 텍스트 아이템만 보여주는 레이아웃 파일
                        // 한 줄에 보여지는 아이템 갯수나 구성을 변경하려면 여기에 새로만든 레이아웃을 지정하면 됩니다.
                        list  // 데이터가 저장되어 있는 ArrayList 객체
                );


                listView.setAdapter(adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int position, long id) {

                        // 클릭한 아이템의 문자열을 가져오기
                        String selected_item = (String)adapterView.getItemAtPosition(position);

                        MyApplication myApp = (MyApplication)getApplicationContext();
                        myApp.setMusic_title(selected_item);


                        finish();

                    }
                });


            }
        }, 500);   //이거 나중에 바꾸기







    }


    public void music_play(){

        MediaPlayer mediaplayer = new MediaPlayer();

        try {
           // music_stop();
            mediaplayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/chatting-570cd.appspot.com/o/mymusic.mp3?alt=media&token=cd3ce6dd-de94-4385-9c93-cbcc066eaba2");

            mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp){

                    mp.start();


                }


            });
            mediaplayer.prepare();
            mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {


                    Toast toast = Toast.makeText(getApplicationContext(), "자신의 노래가 끝났습니다. 이제 상대의 차례입니다.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER , 0, 0);
                    toast.show();




                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

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

}
