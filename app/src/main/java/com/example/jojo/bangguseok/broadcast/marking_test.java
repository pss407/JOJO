package com.example.jojo.bangguseok.broadcast;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;
import com.example.jojo.bangguseok.broadcast.viewer.ViewerActivity;
import com.example.jojo.bangguseok.login.FirebasePost_music;
import com.example.jojo.bangguseok.login.MyApplication;
import com.example.jojo.bangguseok.music_lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class marking_test extends AppCompatActivity {

    //int count=0;
    int score=0;
    int count=0;


    String tmp="";
    TextView pitchText;
    TextView textView12;
    TextView noteText;
    TextView textView11;
    float pitchInHz_tmp;
    AudioDispatcher dispatcher; //70
   //falling slowly //public String correc="111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbcccccccccccccccbbbbbbbbbbbbbbbbbbb1111111aaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbdddddddddddddddddddd1111111111111aaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbcccccccccccccccaaaaaaaaaa11111111111111111111111111111111111111111111111111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbcccccccccccccccbbbbbbbbbbbbb1111111111aaaaaaaaaaaaabbbbbbbbbbbbbbbbbccccccccdddddddddccccccc11111111111111aaaaaaaaaaaaaabbbbbbbbbbbbbbbbccccccccccccbbbbbaaaaaaaaa11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111bbbbbbbcccccccccccccccccccccbabbbbbbbbaaaaaaaaaaaaaaaaaaaa111111111111111111111111111cccccccccccccccbbbbbbbbbbbaaaaaaaaaaaaa1111111111111111111111111111111111111111111cccccccccccccccccccbbbbbbbbbaaaaaaaaaaaaaaaa1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111AAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF111111111111111111111111111111111111111111111111111111111111111111111111111111111";

    //public String correc="11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111bbbbbbbbbbbbbbbbbcccccccccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd1111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd11111111111111111111111111111111111111";


    //public String correc="11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111bbbbbbbbbbbbbbbbbcccccccccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd1111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd11111111111111111111111111111111111111";


    public String correc="";

    int correc_count=-1;// "111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbeeeee1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111                         cccccccccccccccc bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbcccccccc bbbbbbbbccccccccbbbbbbbbccccccccbbbbbbbbcccccccc                            bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb11111111111111111111                                                  1111111111111111bbbbbbbbbbbbbbbbbbbbb                                           aaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbcccccccccccccccbbbbbbbbbbbbb111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111bbbbbb                                      1111111111111111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbcccccccccccccccbbbbbbbbbbbbbbbbbbb1111111aaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbdddddddddddddddddddd11111111111111111111aaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbcccccccccccccccaaaaaaaaaa                                                                11111111111111111111                                      aaaaaaaaaaaaaaaaaaaaaaaccccccccccccccccccccc       11111111111111111111111111111111111111111111111111111111111111111ccccccccccccccccccccc111111cccccccccccccccccccccccccccccccccccccccccc111111cccccccccccccccccccccccccccccccccccccccccccccccccaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc111aaaacccccccccccccccccccccaaaaaaaaaaaaaaaaaaaa111111111                                 1111111111111111111111111111111111111111                         1111111111111111111111111111111111111111                                1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111                                  1111111111                         1111111111                          1111111111                    1111111111                  1111111111                 11111111111111111111           1111111111

    public MediaPlayer m;
    public Context c;

    Thread audioThread;

    MediaPlayer mediaplayer;

    String music_url="";

    double total=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking_test);

        mediaplayer = new MediaPlayer();


        pitchText = (TextView) findViewById(R.id.pitchText);
        textView12 = (TextView) findViewById(R.id.textView12);




        dispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e){
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pitchInHz_tmp=pitchInHz;
                        //func();

                        processPitch(pitchInHz);

                    }
                });
            }
        };
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);

        audioThread = new Thread(dispatcher, "Audio Thread");


        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                //여기다 완료 후 코드 작성

                String result= "수고하셨습니다. "+(int)((score/total)*100)+"점 입니다.";

                Toast toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER , 0, 0);
                toast.show();


            }
        });


        }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseDispatcher();

        mediaplayer.stop();
        mediaplayer.release();
        mediaplayer = null;

    }

    public void releaseDispatcher()
    {
        if(dispatcher != null)
        {
            if(!dispatcher.isStopped())
                dispatcher.stop();
            dispatcher = null;
        }
    }

    public void processPitch(float pitchInHz) {
        correc_count++;




        textView12.setText("카운트 : "+score);
        char pitch='3';
        if(correc.length()-10>correc_count)
        {
            pitch = correc.charAt(correc_count);
        }



        tmp+="1";
        if(pitchInHz<120)
        {
            count++;
            if(count>3)
            {
                count=0;
                pitchText.setText("음정: " );
            }

        }
        else if(pitchInHz >= 125&&pitchInHz < 137) {
            //A
            pitchText.setText("음정: 1옥 도" );
          if(pitch=='a')
          {
              score++;

          }


        }
        else if(pitchInHz >= 144&& pitchInHz < 153) {
            //B
            pitchText.setText("음정: 1옥 레" );
            if(pitch=='b')
            {
                score++;
            }

        }
        else if(pitchInHz >= 160 && pitchInHz < 170) {
            //C
            pitchText.setText("음정: 1옥 미" );
            if(pitch=='c')
            {
                score++;
            }

        }
        else if(pitchInHz >= 171 && pitchInHz < 180) {
            //D
            pitchText.setText("음정: 1옥 파" );

            if(pitch=='d')
            {
                score++;

            }

        }
        else if(pitchInHz >= 190 && pitchInHz <=203) {
            //E
            pitchText.setText("음정: 1옥 솔" );
            if(pitch=='e')
            {
                score++;

            }

        }
        else if(pitchInHz >= 215 && pitchInHz < 230) {
            //F
            pitchText.setText("음정: 1옥 라" );
            if(pitch=='f')
            {
                score++;
            }

        }
        else if(pitchInHz >= 243 && pitchInHz < 253) {
            //G
            pitchText.setText("음정: 1옥 시" );
            if(pitch=='g')
            {
                score++;
            }

        }
        else if(pitchInHz >= 254 && pitchInHz < 280) {
            //G
            pitchText.setText("음정: 2옥 도" );
            if(pitch=='A')
            {
                score++;
            }

        }
        else if(pitchInHz >= 285 && pitchInHz <308) {
            //G
            pitchText.setText("음정: 2옥 레" );
            if(pitch=='B')
            {
                score++;
            }

        }
        else if(pitchInHz >= 327 && pitchInHz < 340) {
            //G
            pitchText.setText("음정: 2옥 미" );
            if(pitch=='C')
            {
                score++;


            }
        }
        else if(pitchInHz >= 343 && pitchInHz < 362) {
            //G
            pitchText.setText("음정: 2옥 파" );
            if(pitch=='D')
            {
                score++;

            }
        }
        else if(pitchInHz >= 386 && pitchInHz < 399) {
            //G
            pitchText.setText("음정: 2옥 솔" );
            if(pitch=='E')
            {
                score++;


            }
        }
        else if(pitchInHz >= 435 && pitchInHz < 448) {
            //G
            pitchText.setText("음정: 2옥 라" );
            if(pitch=='F')
            {
                score++;


            }
        }
        else if(pitchInHz >= 490 ) {
            //G
            pitchText.setText("음정: 2옥 시" );
            if(pitch=='G')
            {
                score++;


            }

        }



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


    public void select_music(View view) {
        music_stop();

        Intent i = new Intent(this,  music_lists.class);
        startActivity(i);






    }




    public void music_start(View view) {
        ///d음악 틀기

        MyApplication myApp2 = (MyApplication)getApplicationContext();
        String tmp=myApp2.getMusic_title();
        if(tmp.equals(""))
        {

            Toast toast = Toast.makeText(getApplicationContext(), "먼저 노래를 선택하세요", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER , 0, 0);
            toast.show();


            return;
        }



        ValueEventListener postListener5 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost_music get = postSnapshot.getValue(FirebasePost_music.class);
                    String[] info = { get.title, get.url, get.lyric,get.sheet};

                    MyApplication myApp = (MyApplication)getApplicationContext();
                    String tmp=myApp.getMusic_title();

                    if(tmp.equals(info[0]))
                    {
                        music_url=info[1];
                        correc=info[3];

                    }


                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());

            }
        };


        // String sort_column_name = "get_url";
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("music");
        // sortbyAge.addValueEventListener(postListener);
        sortbyAge.addListenerForSingleValueEvent(postListener5);

        Handler delayHandler6 = new Handler();
        delayHandler6.postDelayed(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<correc.length();i++)
                {
                 if(correc.charAt(i)!='1')total++;
                }

                try {
                    // music_stop();


                    mediaplayer.setDataSource(music_url);

                    mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {

                            mp.start();

                        }


                    });
                    mediaplayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //채점시작
                audioThread.start();

            }
        }, 700);

    }

}
