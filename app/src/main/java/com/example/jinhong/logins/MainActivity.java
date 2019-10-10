package com.example.jinhong.logins;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "wlshddlek.cafe24.com";
    private static String TAG = "phptest";

    private EditText mEditTextName;
    private EditText mEditTextPassword;
    private TextView mTextViewResult;


    private EditText mEditTextSearchKeyword;
    private String mJsonString;
    String rid;
    String rpassword;
    EditText editText;
    EditText editText2;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText=(EditText)findViewById(R.id.editText);
        editText2=(EditText)findViewById(R.id.editText2);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rid=editText.getText().toString();
                rpassword=editText2.getText().toString();

                GetData task = new GetData();
                task.execute( "http://" + IP_ADDRESS + "/getjson.php", "");

            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }
        });



    }



    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "response - " + result);




                mJsonString = result;
                showResult();

        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "password=" + params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){

        String TAG_JSON="webnautes";
        String TAG_ID = "id";
        String TAG_NAME = "name";
        String TAG_PASSWORD ="password";
        String TAG_LEVEL ="level";
        String TAG_TIER ="tier";



            try {
                JSONObject jsonObject = new JSONObject(mJsonString); //JSONArray

                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                boolean check = false;

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String name = item.getString(TAG_NAME);
                    String password = item.getString(TAG_PASSWORD);
                    String level = item.getString(TAG_LEVEL);
                    String tier = item.getString(TAG_TIER);


                    if (rid.equals(name) && rpassword.equals(password)) {

                        // 전역변수 설정 (로그인한 유저 정보들)
                        MyApplication myApp = (MyApplication) getApplicationContext();
                        myApp.setlevel(level);
                        myApp.settier(tier);
                        myApp.setname(name);

                        Intent intent = new Intent(getApplicationContext(), SelectMode.class);
                        startActivity(intent);
                        check = true;
                    }


                }
                if (check == false) {
                    Toast toast = Toast.makeText(getApplicationContext(), "틀렸습니다", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.LEFT, 350, 200);
                    toast.show();
                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

    }

}


