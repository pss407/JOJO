package com.example.jojo.bangguseok.broadcast.liveVideoBroadcaster;

//test

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.jojo.bangguseok.R;
import com.example.jojo.bangguseok.broadcast.broadcaster.ILiveVideoBroadcaster;
import com.example.jojo.bangguseok.broadcast.broadcaster.LiveVideoBroadcaster;
import com.example.jojo.bangguseok.broadcast.broadcaster.utils.Resolution;
import com.example.jojo.bangguseok.broadcast.liveVideoPlayer.DefaultExtractorsFactoryForFLV;
import com.example.jojo.bangguseok.chatting.ChatAdapter;
import com.example.jojo.bangguseok.chatting.ChatVO;
import com.example.jojo.bangguseok.login.FirebasePost;
import com.example.jojo.bangguseok.login.FirebasePost_music;
import com.example.jojo.bangguseok.login.FirebasePost_url;
import com.example.jojo.bangguseok.login.MyApplication;
import com.google.android.exoplayer2.BuildConfig;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;


import static com.example.jojo.bangguseok.broadcast.BroadcastActivity.RTMP_BASE_URL;



/////////////////
//채팅 추가

public class LiveVideoBroadcasterActivity extends AppCompatActivity implements View.OnClickListener, ExoPlayer.EventListener,
        PlaybackControlView.VisibilityListener, MediaPlayer.OnPreparedListener{

    //노래

    MediaPlayer m;

    AlertDialog alertDialog;

    //채팅 변수

    ArrayList<ChatVO> list = new ArrayList<>();
    ListView lv;
    ImageButton btn;
    EditText edt;
    int imageID = R.drawable.profile;

    String id = "";
    String message = "chat";
    //int chatId = 0;

    public String start_second="false";

    String numm;

    ValueEventListener postListener2;

    Query sortby;
    Query sortby2;

    String tmp="";


    String correc="";

    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;
    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private Handler mainHandler;
    private EventLogger eventLogger;
    private SimpleExoPlayerView simpleExoPlayerView;
    private LinearLayout debugRootView;
    private TextView debugTextView;
    private Button retryButton;

    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private DebugTextViewHelper debugViewHelper;
    private boolean needRetrySource;

    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;
    private com.example.jojo.bangguseok.broadcast.liveVideoPlayer.RtmpDataSource.RtmpDataSourceFactory rtmpDataSourceFactory;
    protected String userAgent;
    private EditText videoNameEditText;
    private View videoStartControlLayout;

    private static final String TAG = LiveVideoBroadcasterActivity.class.getSimpleName();
    private ViewGroup mRootView;
    boolean mIsRecording = false;
    private EditText mStreamNameEditText;
    private Timer mTimer;
    private long mElapsedTime;
    public TimerHandler mTimerHandler;
    private ImageButton mSettingsButton;
    private CameraResolutionsFragment mCameraResolutionsDialog;
    private Intent mLiveVideoBroadcasterServiceIntent;
    private TextView mStreamLiveStatus;
    private GLSurfaceView mGLView;
    private ILiveVideoBroadcaster mLiveVideoBroadcaster;
    private Button mBroadcastControlButton;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference();

    public String listener2_remove="true";

    public String Listener4_finish="false";
    public String Listener4_exist="false";

    public ValueEventListener postListener4;

    AlertDialog.Builder builder;

    int vote_tmp;
    String winner="";


    AudioManager am;
    String experience="0";

    private MediaPlayer mMediaplayer;

    String music_title;

    String music_url="";

    MediaPlayer mediaplayer;

    TextView textView13;

    String lyrics="";

    float pitchInHz_tmp;
    String tmp2="";

    String vote1;
    String vote2;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LiveVideoBroadcaster.LocalBinder binder = (LiveVideoBroadcaster.LocalBinder) service;
            if (mLiveVideoBroadcaster == null) {
                mLiveVideoBroadcaster = binder.getService();
                mLiveVideoBroadcaster.init(LiveVideoBroadcasterActivity.this, mGLView);
                mLiveVideoBroadcaster.setAdaptiveStreaming(true);
            }
            mLiveVideoBroadcaster.openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mLiveVideoBroadcaster = null;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         mediaplayer = new MediaPlayer();



  //
        // Hide title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //binding on resume not to having leaked service connection
        mLiveVideoBroadcasterServiceIntent = new Intent(this, LiveVideoBroadcaster.class);
        //this makes service do its job until done
        startService(mLiveVideoBroadcasterServiceIntent);

        setContentView(R.layout.activity_live_video_broadcaster);

        mTimerHandler = new TimerHandler();
        //mStreamNameEditText = (EditText) findViewById(R.id.stream_name_edit_text);

        mRootView = (ViewGroup)findViewById(R.id.root_layout);
        mSettingsButton = (ImageButton)findViewById(R.id.settings_button);
        mStreamLiveStatus = (TextView) findViewById(R.id.stream_live_status);

        mBroadcastControlButton = (Button) findViewById(R.id.toggle_broadcasting);

        // Configure the GLSurfaceView.  This will start the Renderer thread, with an
        // appropriate EGL activity.
        mGLView = (GLSurfaceView) findViewById(R.id.cameraPreview_surfaceView);
        if (mGLView != null) {
            mGLView.setEGLContextClientVersion(2);     // select GLES 2.0
        }

        ///////*

        userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        shouldAutoPlay = true;
        clearResumePosition();
        mediaDataSourceFactory = buildDataSourceFactory(true);
        rtmpDataSourceFactory = new com.example.jojo.bangguseok.broadcast.liveVideoPlayer.RtmpDataSource.RtmpDataSourceFactory();
        mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }


        View rootView = findViewById(R.id.root_layout);
        rootView.setOnClickListener(this);

        debugRootView = (LinearLayout) findViewById(R.id.controls_root);
        debugTextView = (TextView) findViewById(R.id.debug_text_view2);


        retryButton = (Button) findViewById(R.id.retry_button);
        retryButton.setOnClickListener(this);

        //videoNameEditText = (EditText) findViewById(R.id.video_name_edit_text2);
        //videoStartControlLayout = findViewById(R.id.video_start_control_layout);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setUseController(false);

         am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);  //마이크
        am.setMicrophoneMute(false);


        //노래 가사 띄우기

        textView13 = (TextView) findViewById(R.id.textView13);

        //textView13.setMovementMethod(new ScrollingMovementMethod());

        //"https://gkbjsozvwply2376889.cdn.ntruss.com/video/ls-20190919204002-vFu5I_270p_a_l.m3u8";
        //String URL = "http://192.168.1.34:5080/vod/streams/test_adaptive.m3u8";

        //videoStartControlLayout.setVisibility(View.GONE);
        /////////송출

        Toast toast = Toast.makeText(getApplicationContext(), "8초후에 대결이 시작됩니다. 대결을 준비하세요", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER , 0, 0);
        toast.show();


        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //여기에 딜레이 후 시작할 작업들을 입력
                com.example.jojo.bangguseok.login.MyApplication myApp = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();
                if (!mIsRecording)
                {
                    if (mLiveVideoBroadcaster != null) {
                        if (!mLiveVideoBroadcaster.isConnected()) {
                            String streamName = "Stream Name";  //stream name 설정

                            new AsyncTask<String, String, Boolean>() {
                                ContentLoadingProgressBar
                                        progressBar;
                                @Override
                                protected void onPreExecute() {
                                    progressBar = new ContentLoadingProgressBar(LiveVideoBroadcasterActivity.this);
                                    progressBar.show();
                                }

                                @Override
                                protected Boolean doInBackground(String... url) {
                                    return mLiveVideoBroadcaster.startBroadcasting(url[0]);

                                }

                                @SuppressLint("WrongConstant")
                                @Override
                                protected void onPostExecute(Boolean result) {
                                    progressBar.hide();
                                    mIsRecording = result;
                                    if (result) {
                                        mStreamLiveStatus.setVisibility(View.VISIBLE);

                                        mBroadcastControlButton.setText("방나가기");
                                        mSettingsButton.setVisibility(View.GONE);
                                        startTimer();//start the recording duration
                                    }
                                    else {
                                        Snackbar.make(mRootView, R.string.stream_not_started, Snackbar.LENGTH_LONG).show();

                                        triggerStopRecording();
                                    }
                                }
                            }.execute(myApp.getSend_url());//"rtmp://rtmp-ls-k1.video.media.ntruss.com/live/8KTNWDG3mv"//myApp.getSend_url()//"rtmp://rtmp-ls-k1.video.media.ntruss.com/live/aKSuwKH7fg"
                        }
                        else {
                            //Snackbar.make(mRootView, R.string.streaming_not_finished, Snackbar.LENGTH_LONG).show();
                            Toast toast = Toast.makeText(getApplicationContext(), "streaming not finished", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER , 0, 0);
                            toast.show();
                        }
                    }
                    else {
                        // Snackbar.make(mRootView, R.string.oopps_shouldnt_happen, Snackbar.LENGTH_LONG).show();
                        Toast toast = Toast.makeText(getApplicationContext(), "oopps shouldnt happen", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER , 0, 0);
                        toast.show();
                    }
                }
                else
                {
                    triggerStopRecording();
                }

            }
        }, 300); // 0.3초 지연을 준 후 시작




        Handler delayHandler2 = new Handler();
        delayHandler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                //여기에 딜레이 후 시작할 작업들을 입력
                com.example.jojo.bangguseok.login.MyApplication myApp = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();
                String URL = myApp.getGet_url();//"https://orrkzjbnurrk2465864.cdn.ntruss.com/video/235_360p_s_l.m3u8";//myApp.getGet_url()   //진홍//"https://gkbjsozvwply2376889.cdn.ntruss.com/video/253_270p_s_l.m3u8";

                initializePlayer(URL);

                //////////////대결 순서대로 진행

                MyApplication myApp3 = (MyApplication)getApplicationContext();
                String num= myApp3.getUrl_room();
                tmp=num;



                ///
                MyApplication myApp2 = (MyApplication)getApplicationContext();




                if(myApp2.getOrder().equals("1")) //첫번째면
                {

                    Toast toast2 = Toast.makeText(getApplicationContext(), "자신의 차례입니다. 10초후에 노래가 시작됩니다.", Toast.LENGTH_LONG);
                    toast2.setGravity(Gravity.CENTER , 0, 0);
                    toast2.show();


                    Handler delayHandler7 = new Handler();
                    delayHandler7.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            music_play();


                            Handler delayHandler4 = new Handler();
                            delayHandler4.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        public void onCompletion(MediaPlayer mp) {
                                            MyApplication myApp4 = (MyApplication)getApplicationContext();
                                            String num= myApp4.getUrl_room();

                                            textView13.setText("");

                                            Toast toast = Toast.makeText(getApplicationContext(), "자신의 노래가 끝났습니다. 이제 상대의 차례입니다.", Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER , 0, 0);
                                            toast.show();

                                            am.setMicrophoneMute(true);


                                            Handler delayHandler6 = new Handler();
                                            delayHandler6.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {


                                                    databaseReference.child("URL").child("room" + tmp).child("url_1").child("music_finish").setValue("true");
                                                }
                                            }, 9000);   //나보다 상대는 조금 늦게 노래가끝나기때문에 딜레이를 줌


                                        }
                                    });


                                }
                            }, 500);



                        }
                    }, 6000);

                }
                else   //두번째면
                {

                    am.setMicrophoneMute(true);

                    listener2_remove="false";
                     postListener2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            int count = 1;


                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String key = postSnapshot.getKey();
                                FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                                String[] info = {get.music_finish};



                                if(count==1&&info[0].equals("true"))
                                {
                                    Toast toast4 = Toast.makeText(getApplicationContext(), "상대방의 노래가 끝났습니다. 5초뒤에 자신의 노래가 시작됩니다.", Toast.LENGTH_LONG);
                                    toast4.setGravity(Gravity.CENTER , 0, 0);
                                    toast4.show();

                                    am.setMicrophoneMute(false);

                                    Handler delayHandler5 = new Handler();
                                    delayHandler5.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            music_play();

                                            Handler delayHandler4 = new Handler();
                                            delayHandler4.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {


                                                    mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                        public void onCompletion(MediaPlayer mp) {
                                                            MyApplication myApp4 = (MyApplication)getApplicationContext();
                                                            String num= myApp4.getUrl_room();

                                                            textView13.setText("");

                                                            databaseReference.child("URL").child("room" + num).child("url_2").child("music_finish").setValue("true");


                                                        }
                                                    });

                                                    Handler delayHandler5 = new Handler();
                                                    delayHandler5.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {


                                                            listener2_remove="true";
                                                            sortby.removeEventListener(postListener2);

                                                        }
                                                    }, 4000);


                                                }
                                            }, 500);



                                        }
                                    }, 6000);


                                }
                                count++;


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
            }
        }, 15000);

        MyApplication myApp6 = (MyApplication)getApplicationContext();
        numm=myApp6.getUrl_room();

        builder = new AlertDialog.Builder(this);

        postListener4 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int count=1;
                /*
                if(Listener4_finish.equals("true"))
                {
                    sortby.removeEventListener(postListener4);
                }

                 */


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                    String[] info = {get.music_finish,get.vote};


                  if(info[0].equals("true")&&Listener4_finish.equals("false")) // 한번 둘다 true되면 중지
                  {

                      if(count==2)//음악이 둘다 끝날경우
                      {
                          Listener4_finish="true";

                          Toast toast = Toast.makeText(getApplicationContext(), "투표 마감후 결과가 발표됩니다. 잠시만 기다려주세요", Toast.LENGTH_LONG);
                          toast.setGravity(Gravity.CENTER , 0, 0);
                          toast.show();

                          Handler delayHandler6 = new Handler();
                          delayHandler6.postDelayed(new Runnable() {
                              @Override
                              public void run() {

                                  ValueEventListener postListener5 = new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {

                                          int count=1;

                                          for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                              String key = postSnapshot.getKey();
                                              FirebasePost_url get = postSnapshot.getValue(FirebasePost_url.class);
                                              String[] info = { get.vote};
                                              if(count==1)
                                              {
                                                  vote1=info[0];
                                              }

                                              if(count==2)
                                              {
                                                  vote2=info[0];
                                              }

                                              count++;

                                          }

                                      }
                                      @Override
                                      public void onCancelled(DatabaseError databaseError) {
                                          Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());

                                      }
                                  };


                                  String value = "room" + numm;
                                  // String sort_column_name = "get_url";
                                  Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("URL").child(value);
                                  // sortbyAge.addValueEventListener(postListener);
                                  sortbyAge.addListenerForSingleValueEvent(postListener5);


                                  Handler delayHandler7 = new Handler();
                                  delayHandler7.postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          int vote1_int=Integer.parseInt(vote1);
                                          int vote2_int=Integer.parseInt(vote2);


                                          if(vote1_int==vote2_int) //투표 같을 때
                                          {

                                                  builder.setTitle("").setMessage(" 1번 투표: "+vote1+" \n 2번 투표: "+vote2+" \n\n 무승부 입니다! 7초 후에 방을 나갑니다.");
                                                  winner="";

                                          }
                                          else if(vote1_int>vote2_int)  //1번 투표수가더많을때
                                          {

                                                  builder.setTitle("").setMessage(" 1번 투표: "+vote1+" \n 2번 투표: "+vote2+" \n\n 1번 우승 입니다! 7초 후에 방을 나갑니다.");
                                                  winner="1";

                                          }
                                          else   //2번 투표수가 더 많을 때
                                          {

                                                  builder.setTitle("").setMessage(" 1번 투표: "+vote1+" \n 2번 투표: "+vote2+" \n\n 2번 우승 입니다! 7초 후에 방을 나갑니다.");
                                                  winner="2";

                                          }

                                            //  builder.setTitle("").setMessage("수고하셨습니다. 우승자는 " +winner+ "번 입니다. 7초 후에 방을 나갑니다.");


                                              MyApplication myApp = (MyApplication)getApplicationContext();
                                              String turn=myApp.getOrder();

                                              if(turn.equals(winner)) {


                                                  ValueEventListener postListener = new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(DataSnapshot dataSnapshot) {



                                                          for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                              String key = postSnapshot.getKey();
                                                              FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                                                              String[] info = {get.id, get.experience};

                                                              MyApplication myApp2 = (MyApplication)getApplicationContext();
                                                              String id=myApp2.getname();
                                                              if(id.equals(info[0]))  //해당 id면
                                                              {

                                                                  experience=info[1];

                                                              }


                                                          }


                                                      }

                                                      @Override
                                                      public void onCancelled(DatabaseError databaseError) {
                                                          Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());
                                                      }
                                                  };

                                                  String sort_column_name = "id";
                                                  Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("id_list").orderByChild(sort_column_name);
                                                  sortbyAge.addListenerForSingleValueEvent(postListener);


                                                  Handler delayHandler6 = new Handler();
                                                  delayHandler6.postDelayed(new Runnable() {
                                                      @Override
                                                      public void run() {


                                                          MyApplication myApp3 = (MyApplication)getApplicationContext();
                                                          String id=myApp3.getname();

                                                          int exp = Integer.parseInt(experience);
                                                          exp += 10;

                                                          databaseReference.child("id_list").child(id).child("experience").setValue(""+exp);


                                                      }
                                                  }, 500);   //이거 나중

                                              }






                                          builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                              @Override
                                              public void onClick(DialogInterface dialog, int id)
                                              {

                                              }
                                          });
                                              alertDialog = builder.create();
                                          alertDialog.show();



                                          Handler delayHandler8 = new Handler();
                                          delayHandler8.postDelayed(new Runnable() {
                                              @Override
                                              public void run() {

                                                  alertDialog.cancel();


                                                  finish();


                                              }
                                          }, 10000);   //이거 나중에 바꾸기

                                      }  //run
                                  }, 1500);   //이거 나중에 바꾸기

                              }
                          }, 17000);   //이거 나중에 바꾸기

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

        String value = "room" + numm;

        sortby2 = FirebaseDatabase.getInstance().getReference().child("URL").child(value);
        // sortbyAge.addValueEventListener(postListener);
        sortby2.addValueEventListener(postListener4);




        mBroadcastControlButton.setVisibility(View.VISIBLE);

        /////

        simpleExoPlayerView.bringToFront();
/////////////////////sdfsdfsdfsdf
        // music.mp3 파일 역시 getResources().openRawResource()로
        //가져올 수 있다.
        // 여기서는 MediaPlayer로도 음악 파일을 가져오고
        // start()로 실행할 수 있다.

/*
        Handler delayHandler3 = new Handler();
        delayHandler2.postDelayed(new Runnable() {
            @Override
            public void run() {

                music_play();
            }
        }, 4000);

*/
////sdfsdf
        //채팅 추가
        lv = findViewById(R.id.listView);
        edt = findViewById(R.id.chat_message);
        btn = findViewById(R.id.bnt_send);

// Write a message to the database
        //chatId = getIntent().getIntExtra("chatId",0);
        //message = message.concat(Integer.toString(chatId));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(message);

//로그인한 아이디
        id = getIntent().getStringExtra("id");

        final com.example.jojo.bangguseok.login.MyApplication myApp = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();

        final ChatAdapter adapter = new ChatAdapter(getApplicationContext(), R.layout.talklist, list, id);
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    Date today = new Date();
                    SimpleDateFormat timeNow = new SimpleDateFormat("a K:mm");

                    StringBuffer sb = new StringBuffer(edt.getText().toString());
                    if (sb.length() >= 15) {
                        for (int i = 1; i <= sb.length() / 15; i++) {
                            sb.insert(15 * i, "\n");
                        }
                    }

                    MyApplication myApp = (MyApplication) getApplicationContext();


                    myRef.child("room"+myApp.getUrl_room()+": "+myApp.getMusic_title1()+" vs "+myApp.getMusic_title2()+"/").push().setValue(new ChatVO(R.drawable.profile, id, sb.toString(), timeNow.format(today)));
                    edt.setText("");
                }
            }
        });
//
        myRef.child("room"+myApp.getUrl_room()+": "+myApp.getMusic_title1()+" vs "+myApp.getMusic_title2()+"/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatVO value = dataSnapshot.getValue(ChatVO.class); // 괄호 안 : 꺼낼 자료 형태
                list.add(value);
                adapter.notifyDataSetChanged();
                lv.setSelection(adapter.getCount() - 1);
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


    public void changeCamera(View v) {
        if (mLiveVideoBroadcaster != null) {
          //  mLiveVideoBroadcaster.changeCamera();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //this lets activity bind
        bindService(mLiveVideoBroadcasterServiceIntent, mConnection, 0);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            play(null);
        } else {
            showToast(R.string.storage_permission_denied);
            finish();
        }

        switch (requestCode) {
            case LiveVideoBroadcaster.PERMISSIONS_REQUEST: {
                if (mLiveVideoBroadcaster.isPermissionGranted()) {
                    mLiveVideoBroadcaster.openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                }
                else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(this,
                                    Manifest.permission.RECORD_AUDIO) ) {
                        mLiveVideoBroadcaster.requestPermission();
                    }
                    else {
                        new AlertDialog.Builder(LiveVideoBroadcasterActivity.this)
                                .setTitle(R.string.permission)
                                .setMessage(getString(R.string.app_doesnot_work_without_permissions))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {
                                            //Open the specific App Info page:
                                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                            startActivity(intent);

                                        } catch ( ActivityNotFoundException e ) {
                                            //e.printStackTrace();

                                            //Open the generic Apps page:
                                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                            startActivity(intent);

                                        }
                                    }
                                })
                                .show();
                    }
                }
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");

        //hide dialog if visible not to create leaked window exception
        if (mCameraResolutionsDialog != null && mCameraResolutionsDialog.isVisible()) {
            mCameraResolutionsDialog.dismiss();
        }
        mLiveVideoBroadcaster.pause();

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }




    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);

        if (Util.SDK_INT > 23) {
            releasePlayer();
        }

        music_stop();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLiveVideoBroadcaster.setDisplayOrientation();
        }

    }

    @SuppressLint("WrongConstant")
    public void showSetResolutionDialog(View v) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragmentDialog = getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragmentDialog != null) {

            ft.remove(fragmentDialog);
        }

        ArrayList<Resolution> sizeList = mLiveVideoBroadcaster.getPreviewSizeList();


        if (sizeList != null && sizeList.size() > 0) {
            mCameraResolutionsDialog = new CameraResolutionsFragment();

            mCameraResolutionsDialog.setCameraResolutions(sizeList, mLiveVideoBroadcaster.getPreviewSize());
            mCameraResolutionsDialog.show(ft, "resolutiton_dialog");
        }
        else {
            Snackbar.make(mRootView, "No resolution available",Snackbar.LENGTH_LONG).show();
        }

    }

    @SuppressLint("WrongConstant")
    public void toggleBroadcasting(View v) {
        /*
        if (!mIsRecording)
        {
            if (mLiveVideoBroadcaster != null) {
                if (!mLiveVideoBroadcaster.isConnected()) {
                    String streamName = mStreamNameEditText.getText().toString();

                    new AsyncTask<String, String, Boolean>() {
                        ContentLoadingProgressBar
                                progressBar;
                        @Override
                        protected void onPreExecute() {
                            progressBar = new ContentLoadingProgressBar(LiveVideoBroadcasterActivity.this);
                            progressBar.show();
                        }

                        @Override
                        protected Boolean doInBackground(String... url) {
                            return mLiveVideoBroadcaster.startBroadcasting(url[0]);

                        }

                        @SuppressLint("WrongConstant")
                        @Override
                        protected void onPostExecute(Boolean result) {
                            progressBar.hide();
                            mIsRecording = result;
                            if (result) {
                                mStreamLiveStatus.setVisibility(View.VISIBLE);

                                mBroadcastControlButton.setText("Quit");
                                mSettingsButton.setVisibility(View.GONE);
                                startTimer();//start the recording duration
                            }
                            else {
                                Snackbar.make(mRootView, R.string.stream_not_started, Snackbar.LENGTH_LONG).show();

                                triggerStopRecording();
                            }
                        }
                    }.execute("rtmp://rtmp-ls-k1.video.media.ntruss.com/live/aKSuwKH7fg");
                }
                else {
                    Snackbar.make(mRootView, R.string.streaming_not_finished, Snackbar.LENGTH_LONG).show();
                }
            }
            else {
                Snackbar.make(mRootView, R.string.oopps_shouldnt_happen, Snackbar.LENGTH_LONG).show();
            }
        }
        else
        {
            triggerStopRecording();
        }

*/

    }


    public void triggerStopRecording() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        if (mIsRecording) {
           // mBroadcastControlButton.setText("방나가기");

            mStreamLiveStatus.setVisibility(View.GONE);
            mStreamLiveStatus.setText(R.string.live_indicator);
            mSettingsButton.setVisibility(View.VISIBLE);

            stopTimer();
            mLiveVideoBroadcaster.stopBroadcasting();
        }
//지농

        MyApplication myApp5 = (MyApplication)getApplicationContext();
        String tmp1=myApp5.getMusic_title1();
        String tmp2=myApp5.getMusic_title2();

        childUpdates.put("/room"+myApp5.getUrl_room()+": "+tmp1+" vs "+tmp2+"/", postValues);
        myRef.child("chat").updateChildren(childUpdates);

        mIsRecording = false;

        //music_stop();
        //finish();
    }

    //This method starts a mTimer and updates the textview to show elapsed time for recording
    public void startTimer() {

        if(mTimer == null) {
            mTimer = new Timer();
        }

        mElapsedTime = 0;
        mTimer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                mElapsedTime += 1; //increase every sec
                mTimerHandler.obtainMessage(TimerHandler.INCREASE_TIMER).sendToTarget();

                if (mLiveVideoBroadcaster == null || !mLiveVideoBroadcaster.isConnected()) {
                    mTimerHandler.obtainMessage(TimerHandler.CONNECTION_LOST).sendToTarget();
                }
            }
        }, 0, 1000);
    }


    public void stopTimer()
    {
        if (mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = null;
        this.mElapsedTime = 0;
    }

    public void setResolution(Resolution size) {
        mLiveVideoBroadcaster.setResolution(size);
    }



    private class TimerHandler extends Handler {
        static final int CONNECTION_LOST = 2;
        static final int INCREASE_TIMER = 1;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INCREASE_TIMER:
                    mStreamLiveStatus.setText(getString(R.string.live_indicator) + " - " + getDurationString((int) mElapsedTime));
                    break;
                case CONNECTION_LOST:
                    triggerStopRecording();
                    /*
                    new AlertDialog.Builder(LiveVideoBroadcasterActivity.this)
                            .setMessage(R.string.broadcast_connection_lost)
                            .setPositiveButton(android.R.string.yes, null)
                            .show();

                     */

                    break;
            }
        }
    }

    public static String getDurationString(int seconds) {

        if(seconds < 0 || seconds > 2000000)//there is an codec problem and duration is not set correctly,so display meaningfull string
            seconds = 0;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        if(hours == 0)
            return twoDigitString(minutes) + " : " + twoDigitString(seconds);
        else
            return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    public static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

       mp.start();
    }

    public void music_play(){
        MyApplication myApp = (MyApplication)getApplicationContext();
        music_title=myApp.getMusic_title();


        //db에서 해당 music url 받아오기
        ValueEventListener postListener5 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost_music get = postSnapshot.getValue(FirebasePost_music.class);
                    String[] info = { get.title, get.url, get.lyric,get.sheet};

                    if(music_title.equals(info[0])) {
                        music_url=info[1];
                        lyrics=info[2];
                        correc=info[3];


                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());

            }
        };

        String value = "room" + numm;
        // String sort_column_name = "get_url";
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("music");
        // sortbyAge.addValueEventListener(postListener);
        sortbyAge.addListenerForSingleValueEvent(postListener5);






//////// 음악 틀기

        Handler delayHandler6 = new Handler();
        delayHandler6.postDelayed(new Runnable() {
            @Override
            public void run() {

                textView13.setText(lyrics);


                try {
                    // music_stop();
                    mediaplayer.setDataSource(music_url);

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

                //audioThread2.start();
//
            }
        }, 1500);   //나보다 상대는 조금 늦게 노래가끝나기때문에 딜레이를 줌
/*
        Handler delayHandler7 = new Handler();
        delayHandler6.postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 500);   //나보다 상대는 조금 늦게 노래가끝나기때문에 딜레이를 줌
*/


    }

/*

    public void music_play(){

        try {
            music_stop();
            m = MediaPlayer.create(this, R.raw.mymusic);
            m.start();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
*/
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


    ////////////////////////////////////////////////////////
    @Override
    public void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);//
        super.onNewIntent(intent);
        releasePlayer();
        shouldAutoPlay = true;
        clearResumePosition();
        setIntent(intent);
    }





    @Override
    public void onClick(View view) {
        if (view == retryButton) {
            play(null);
        }
    }




    // Activity input

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Show the controls on any key event.
        simpleExoPlayerView.showController();
        // If the event was not handled then see if the player view can handle it as a media key event.
        return super.dispatchKeyEvent(event) || simpleExoPlayerView.dispatchMediaKeyEvent(event);
    }


    @Override
    public void onVisibilityChange(int visibility) {
     //   debugRootView.setVisibility(visibility);
    }

    // Internal methods

    private void initializePlayer(String rtmpUrl) {
        Intent intent = getIntent();
        boolean needNewPlayer = player == null;
        if (needNewPlayer) {

            boolean preferExtensionDecoders = intent.getBooleanExtra(PREFER_EXTENSION_DECODERS, false);
            @SimpleExoPlayer.ExtensionRendererMode int extensionRendererMode =
                    useExtensionRenderers()
                            ? (preferExtensionDecoders ? SimpleExoPlayer.EXTENSION_RENDERER_MODE_PREFER
                            : SimpleExoPlayer.EXTENSION_RENDERER_MODE_ON)
                            : SimpleExoPlayer.EXTENSION_RENDERER_MODE_OFF;
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(),
                    null, extensionRendererMode);
            //   player = ExoPlayerFactory.newSimpleInstance(this, trackSelector,
            //           new DefaultLoadControl(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE),  500, 1500, 500, 1500),
            //           null, extensionRendererMode);
            player.addListener(this);

            eventLogger = new com.example.jojo.bangguseok.broadcast.liveVideoBroadcaster.EventLogger(trackSelector);
            player.addListener(eventLogger);
            player.setAudioDebugListener(eventLogger);
            player.setVideoDebugListener(eventLogger);
            player.setMetadataOutput(eventLogger);

            simpleExoPlayerView.setPlayer(player);
            player.setPlayWhenReady(shouldAutoPlay);
            debugViewHelper = new DebugTextViewHelper(player, debugTextView);
            debugViewHelper.start();
        }
        if (needNewPlayer || needRetrySource) {
            //  String action = intent.getAction();
            Uri[] uris;
            String[] extensions;

            uris = new Uri[1];
            uris[0] = Uri.parse(rtmpUrl);
            extensions = new String[1];
            extensions[0] = "";
            if (Util.maybeRequestReadExternalStoragePermission(this, uris)) {
                // The player will be reinitialized if the permission is granted.
                return;
            }
            MediaSource[] mediaSources = new MediaSource[uris.length];
            for (int i = 0; i < uris.length; i++) {
                mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
            }
            MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                    : new ConcatenatingMediaSource(mediaSources);
            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                player.seekTo(resumeWindow, resumePosition);
            }
            player.prepare(mediaSource, !haveResumePosition, false);
            needRetrySource = false;
        }
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
            case C.TYPE_OTHER:
                if (uri.getScheme().equals("rtmp")) {
                    return new ExtractorMediaSource(uri, rtmpDataSourceFactory, new DefaultExtractorsFactoryForFLV(),
                            mainHandler, eventLogger);
                }
                else {
                    return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                            mainHandler, eventLogger);
                }
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }


    private void releasePlayer() {
        if (player != null) {
            debugViewHelper.stop();
            debugViewHelper = null;
            shouldAutoPlay = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
            //trackSelectionHelper = null;
            eventLogger = null;
        }
    }

    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = player.isCurrentWindowSeekable() ? Math.max(0, player.getCurrentPosition())
                : C.TIME_UNSET;
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *     DataSource factory.
     * @return A new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    /**
     * Returns a new HttpDataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *     DataSource factory.
     * @return A new HttpDataSource factory.
     */
    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    // ExoPlayer.EventListener implementation

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // Do nothing.
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            showControls();
        }
    }

    @Override
    public void onPositionDiscontinuity() {
        if (needRetrySource) {
            // This will only occur if the user has performed a seek whilst in the error state. Update the
            // resume position so that if the user then retries, playback will resume from the position to
            // which they seeked.
            updateResumePosition();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        // Do nothing.
    }


    @Override
    public void onPlayerError(ExoPlaybackException e) {
        videoStartControlLayout.setVisibility(View.VISIBLE);
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof DecoderInitializationException) {
                // Special case for decoder initialization failures.
                DecoderInitializationException decoderInitializationException =
                        (DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            showToast(errorString);
        }
        needRetrySource = true;
        if (isBehindLiveWindow(e)) {
            clearResumePosition();
            play(null);
        } else {
            updateResumePosition();
            showControls();
        }
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                    == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                showToast(R.string.error_unsupported_video);
            }
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                    == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                showToast(R.string.error_unsupported_audio);
            }
        }
    }

    private void showControls() {
        debugRootView.setVisibility(View.VISIBLE);
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }


    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }

    public void play(View view) {
        String URL = RTMP_BASE_URL + "";
        //String URL = "http://192.168.1.34:5080/vod/streams/test_adaptive.m3u8";
        initializePlayer(URL);
        videoStartControlLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {



        triggerStopRecording();



        sortby2.removeEventListener(postListener4);


        if(listener2_remove.equals("false"))
        {

            sortby.removeEventListener(postListener2);
        }

        MyApplication myApp = (MyApplication)getApplicationContext();




        databaseReference.child("URL").child("room" + myApp.getUrl_room()).child("url_1").child("check").setValue("false");
        databaseReference.child("URL").child("room" + myApp.getUrl_room()).child("url_2").child("check").setValue("false");

        MyApplication myApp5 = (MyApplication)getApplicationContext();
        String num= myApp5.getUrl_room();
        databaseReference.child("URL").child("room" + num).child("url_1").child("music_finish").setValue("false");
        databaseReference.child("URL").child("room" + num).child("url_2").child("music_finish").setValue("false");

        databaseReference.child("URL").child("room" + num).child("url_1").child("vote").setValue("0");
        databaseReference.child("URL").child("room" + num).child("url_2").child("vote").setValue("0");

        music_stop();

        super.onDestroy();

    }
}
