package com.example.jojo.bangguseok.login;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasePost {
    public String id;
    public String password;
    public String tier;
    public String using;   //현재 로그인 중인지
    public String start_matching; //매칭버튼을 눌렀는지
    public String experience;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String id, String password,String tier,String using, String start_matching, String experience) {
        this.id = id;
        this.password = password;
        this.tier = tier;
        this.using = using;
        this.start_matching = start_matching;
        this.experience = experience;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("password", password);
        result.put("tier", tier);
        result.put("using", using);
        result.put("start_matching", start_matching);
        result.put("experience",experience);
        return result;
    }
}
