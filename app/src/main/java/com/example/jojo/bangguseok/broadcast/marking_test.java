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

    int count=0;
    int score=0;


    String tmp="";
    TextView pitchText;
    TextView textView12;
    TextView noteText;
    TextView textView11;
    float pitchInHz_tmp;
    AudioDispatcher dispatcher; //70
    public String correc="111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbccccccccccbbbbbbbbbb11111111111111111111aaaaaaaaaabbbbbbbbbbbbbbbbbbbbdddddddddddddddddddd11111111111111111111aaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbaaaaaaaaaa11111111111111111111111111111111111111111111111111111111111111111111111111111111aaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbccccbbbbbbbbbbbbb1111111111aaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbccccccccdddddddddddccccccccc11111111111111111111aaaaaaaaaaabbbbbbbbbbbbbbbbbccccccbbbbbaaaaaa111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111bbbbbbbccccccccccbabbbbbbaaaaaaaa1111111111111111111111111111111111111111ccccccccccccbbbbaaaaaaaaa1111111111111111111111111111111111111111ccccccccccbbbbbbaaaaaaaaaaaaaaaa1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111ggggggBBBBBBBBBggggggggggggggggggg1111111111BBBBBBBgggggggggggggggggg1111111111BBBBBBBBBBgggggggggggggggg1111111111FFFFFFFFFFFFFFFFFFFF1111111111gggggBBBBggggggggg1111111111BBBBBgggggggggggg11111111111111111111BBBgggggggg1111111111FFFFFFFFFFFf11111111111111111111111111";
    int correc_count=-1;// "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111                                                       11111111111111111111                                                  11111111111111111111                                                                                               11111111111111111111111111111111111111111111111111111111111111111111111111111111         1111111111                                                                11111111111111111111                                             111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111                                 1111111111111111111111111111111111111111                         1111111111111111111111111111111111111111                                1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111                                  1111111111                         1111111111                          1111111111                    1111111111                  1111111111                 11111111111111111111           1111111111

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
            mediaplayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/chatting-570cd.appspot.com/o/Falling%20slowly%20sing%20a%20long%20%5Binstrumental%20%20lyrics%5D.mp3?alt=media&token=a063399f-9860-4a62-b30e-58acb88419dc");

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

        //pitchText.setText("" + pitchInHz);
        textView12.setText(""+score);
        char pitch='3';
        if(correc.length()-20>correc_count)
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
        else if(pitchInHz >= 259 && pitchInHz < 168) {
            //G
            if(pitch=='A')
            {
                score++;

            }

        }
        else if(pitchInHz >= 290 && pitchInHz <303) {
            //G
            if(pitch=='B')
            {
                score++;

            }

        }
        else if(pitchInHz >= 327 && pitchInHz < 339) {
            //G

            if(pitch=='C')
            {
                score++;

            }
        }
        else if(pitchInHz >= 347 && pitchInHz < 359) {
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
