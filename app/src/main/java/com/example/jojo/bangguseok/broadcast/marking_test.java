package com.example.jojo.bangguseok.broadcast;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;

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


    String tmp="";
    TextView pitchText;
    TextView textView12;
    TextView noteText;
    TextView textView11;
    float pitchInHz_tmp;
    AudioDispatcher dispatcher; //70
   //falling slowly //public String correc="111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbcccccccccccccccbbbbbbbbbbbbbbbbbbb1111111aaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbdddddddddddddddddddd1111111111111aaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbcccccccccccccccaaaaaaaaaa11111111111111111111111111111111111111111111111111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbcccccccccccccccbbbbbbbbbbbbb1111111111aaaaaaaaaaaaabbbbbbbbbbbbbbbbbccccccccdddddddddccccccc11111111111111aaaaaaaaaaaaaabbbbbbbbbbbbbbbbccccccccccccbbbbbaaaaaaaaa11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111bbbbbbbcccccccccccccccccccccbabbbbbbbbaaaaaaaaaaaaaaaaaaaa111111111111111111111111111cccccccccccccccbbbbbbbbbbbaaaaaaaaaaaaa1111111111111111111111111111111111111111111cccccccccccccccccccbbbbbbbbbaaaaaaaaaaaaaaaa1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111AAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBgggggggggggggggggggggAAAAAAAAAAAAAAAAAAAAAFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF111111111111111111111111111111111111111111111111111111111111111111111111111111111";

    //public String correc="11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111bbbbbbbbbbbbbbbbbcccccccccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd1111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd11111111111111111111111111111111111111";


    public String correc="11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111bbbbbbbbbbbbbbbbbcccccccccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd1111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd111111111111111111111bbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeebbbbbbbbcccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeedddddddddddd11111111111111111111111111111111111111";



    int correc_count=-1;// "111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbeeeee1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111                         cccccccccccccccc bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbcccccccc bbbbbbbbccccccccbbbbbbbbccccccccbbbbbbbbcccccccc                            bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb11111111111111111111                                                  1111111111111111bbbbbbbbbbbbbbbbbbbbb                                           aaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbcccccccccccccccbbbbbbbbbbbbb111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111bbbbbb                                      1111111111111111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbcccccccccccccccbbbbbbbbbbbbbbbbbbb1111111aaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbdddddddddddddddddddd11111111111111111111aaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbcccccccccccccccaaaaaaaaaa                                                                11111111111111111111                                      aaaaaaaaaaaaaaaaaaaaaaaccccccccccccccccccccc       11111111111111111111111111111111111111111111111111111111111111111ccccccccccccccccccccc111111cccccccccccccccccccccccccccccccccccccccccc111111cccccccccccccccccccccccccccccccccccccccccccccccccaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc111aaaacccccccccccccccccccccaaaaaaaaaaaaaaaaaaaa111111111                                 1111111111111111111111111111111111111111                         1111111111111111111111111111111111111111                                1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111                                  1111111111                         1111111111                          1111111111                    1111111111                  1111111111                 11111111111111111111           1111111111

    public MediaPlayer m;
    public Context c;

    Thread audioThread;

    MediaPlayer mediaplayer;

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


        ///d음악 틀기

        try {
            // music_stop();
            mediaplayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/chatting-570cd.appspot.com/o/%5BDingaStar%5D%20%ED%95%91%ED%81%AC%ED%90%81(PinkFong)-%EC%83%81%EC%96%B4%EA%B0%80%EC%A1%B1(Baby%20Shark)%20(Karaoke%20App%20No.1%20DingaStar).mp3?alt=media&token=8952a8be-62bc-463b-9ffc-b65ac5d23a8d");

            mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp){

                    mp.start();

                }


            });
            mediaplayer.prepare();
        }catch (Exception e) {
            e.printStackTrace();
        }


        //시작
        audioThread.start();



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

        pitchText.setText("" + pitchInHz);
        textView12.setText(""+score);
        char pitch='3';
        if(correc.length()-10>correc_count)
        {
            pitch = correc.charAt(correc_count);
        }
        else
        {
            score-=3;
        }
        tmp+="1";
        if(pitchInHz >= 125&&pitchInHz < 137) {
            //A
          if(pitch=='a')
          {
              score++;

          }


        }
        else if(pitchInHz >= 144&& pitchInHz < 153) {
            //B
            if(pitch=='b')
            {
                score++;

            }

        }
        else if(pitchInHz >= 160 && pitchInHz < 170) {
            //C
            if(pitch=='c')
            {
                score++;

            }

        }
        else if(pitchInHz >= 171 && pitchInHz < 180) {
            //D
            if(pitch=='d')
            {
                score++;

            }

        }
        else if(pitchInHz >= 190 && pitchInHz <=203) {
            //E
            if(pitch=='e')
            {
                score++;

            }

        }
        else if(pitchInHz >= 215 && pitchInHz < 230) {
            //F
            if(pitch=='f')
            {
                score++;

            }

        }
        else if(pitchInHz >= 243 && pitchInHz < 253) {
            //G
            if(pitch=='g')
            {
                score++;

            }

        }
        else if(pitchInHz >= 254 && pitchInHz < 280) {
            //G
            if(pitch=='A')
            {
                score++;

            }

        }
        else if(pitchInHz >= 285 && pitchInHz <308) {
            //G
            if(pitch=='B')
            {
                score++;

            }

        }
        else if(pitchInHz >= 327 && pitchInHz < 340) {
            //G

            if(pitch=='C')
            {
                score++;

            }
        }
        else if(pitchInHz >= 343 && pitchInHz < 362) {
            //G

            if(pitch=='D')
            {
                score++;

            }
        }
        else if(pitchInHz >= 386 && pitchInHz < 399) {
            //G

            if(pitch=='E')
            {
                score++;

            }
        }
        else if(pitchInHz >= 435 && pitchInHz < 448) {
            //G

            if(pitch=='F')
            {
                score++;

            }
        }
        else if(pitchInHz >= 490 ) {
            //G
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


}
