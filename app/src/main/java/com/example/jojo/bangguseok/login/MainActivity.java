package com.example.jojo.bangguseok.login;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;




public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private EditText mEditTextName;
    private EditText mEditTextPassword;
    private TextView mTextViewResult;


    private EditText mEditTextSearchKeyword;
    private String mJsonString;
    String rid;
    String rpassword;
    EditText editText;
    EditText editText2;
    String tmp_id = "";   //id 저장
    String tmp_tier = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AudioManager am;

        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);  //마이크
        am.setMicrophoneMute(false);
////

//////
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean check = false;
                        String check_using = "false";

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                            String[] info = {get.id, get.password, get.tier, get.using, get.experience};


                            check_using = info[3];

                            if (info[0].equals(editText.getText().toString()) && info[1].equals(editText2.getText().toString())) {
                                tmp_id = info[0];
                                tmp_tier = info[2];
                                com.example.jojo.bangguseok.login.MyApplication myApp = (com.example.jojo.bangguseok.login.MyApplication) getApplicationContext();
                                myApp.setname(tmp_id);
                                myApp.settier(tmp_tier);
                                myApp.setExperience(info[4]);


                                check = true;
                                break;
                            }

                        }



                            if (check == true && check_using.equals("false")) {
                                Intent intent = new Intent(getApplicationContext(), com.example.jojo.bangguseok.login.SelectMode.class);
                                startActivity(intent);

                                databaseReference.child("id_list").child(tmp_id).child("using").setValue("true");
                            } else if (check == true && check_using.equals("true")) {
                                Toast toast = Toast.makeText(getApplicationContext(), "이미 로그인 되어있습니다", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER , 0, 0);
                                toast.show();

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "틀렸습니다", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER , 0, 0);
                                toast.show();
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


            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.jojo.bangguseok.login.SignIn.class);
                startActivity(intent);


            }
        });

        Button button10 = (Button) findViewById(R.id.button10);
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.jojo.bangguseok.broadcast.marking_test.class);
                startActivity(intent);

            }
        });

    }



}


