package com.example.jojo.bangguseok.broadcast;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;

import java.io.FileOutputStream;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class marking extends AppCompatActivity {


    TextView pitchText;
    TextView noteText;
    TextView textView11;
    String mus="";
    float pitchInHz_tmp;
    int count=0;
    AudioDispatcher dispatcher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking);

        noteText = (TextView) findViewById(R.id.noteText);
        pitchText = (TextView) findViewById(R.id.pitchText);
        textView11 = (TextView) findViewById(R.id.textView11);

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
                        func();
                        //processPitch(pitchInHz);

                    }
                });
            }
        };
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);

        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();








    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseDispatcher();
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

    public void func()
    {

        Handler delayHandler6 = new Handler();
        delayHandler6.postDelayed(new Runnable() {
            @Override
            public void run() {

                processPitch(pitchInHz_tmp);
                if(count<=20) {
                    func();
                }
                count++;
            }
        }, 2000);


    }

    public void internalStorageSaveFile()
    {
        String filename = "myfile";
        String string = mus;
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();

            Toast.makeText(this, "this is internal storage save success.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(this, "this is internal storage save fail.", Toast.LENGTH_LONG).show();
        }
    }


    public void  save2(View view) {

        internalStorageSaveFile();
    }

    public void button13(View view) {



    }

    public void see_mus(View view) {

        textView11.setText(mus);
    }

    public void stringreset(View view) {

      mus="";
    }

    public void processPitch(float pitchInHz) {

        pitchText.setText("" + pitchInHz);

        if(pitchInHz < 137) {
            //A
            noteText.setText("낮은 도");
            mus+="a";

        }
        else if(pitchInHz >= 144&& pitchInHz < 153) {
            //B
            noteText.setText("낮은 레");
            mus+="b";
        }
        else if(pitchInHz >= 160 && pitchInHz < 170) {
            //C
            noteText.setText("낮은 미");
            mus+="c";
        }
        else if(pitchInHz >= 171 && pitchInHz < 180) {
            //D
            noteText.setText("낮은 파");
            mus+="d";
        }
        else if(pitchInHz >= 190 && pitchInHz <=203) {
            //E
            noteText.setText("낮은 솔");
            mus+="e";
        }
        else if(pitchInHz >= 215 && pitchInHz < 230) {
            //F
            noteText.setText("낮은 라");
            mus+="f";
        }
        else if(pitchInHz >= 243 && pitchInHz < 253) {
            //G
            noteText.setText("낮은 시");
            mus+="g";
        }
        else if(pitchInHz >= 259 && pitchInHz < 168) {
            //G
            noteText.setText("도");
            mus+="A";
        }
        else if(pitchInHz >= 290 && pitchInHz <303) {
            //G
            noteText.setText("레");
            mus+="B";
        }
        else if(pitchInHz >= 327 && pitchInHz < 339) {
            //G
            noteText.setText("미");
            mus+="C";
        }
        else if(pitchInHz >= 347 && pitchInHz < 359) {
            //G
            noteText.setText("파");
            mus+="D";
        }
        else if(pitchInHz >= 386 && pitchInHz < 399) {
            //G
            noteText.setText("솔");
            mus+="E";
        }
        else if(pitchInHz >= 435 && pitchInHz < 448) {
            //G
            noteText.setText("라");
            mus+="F";
        }
        else if(pitchInHz >= 490 ) {
            //G
            noteText.setText("시");
            mus+="G";
        }

    }
}
