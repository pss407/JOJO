package com.example.jojo.bangguseok.login;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasePost_url {
    public String check;
    public String get_url;
    public String send_url;
    public String num;
    public String music_finish;


    public FirebasePost_url(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost_url(String check,String get_url, String send_url,String num,String music_finish) {
      this.check=check;
      this.get_url=get_url;
      this.send_url=send_url;
      this.num=num;
      this.music_finish=music_finish;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("check", check);
        result.put("get_url",get_url);
        result.put("send_url", send_url);
        result.put("num",num);
        result.put("music_finish",music_finish);

        return result;
    }
}
