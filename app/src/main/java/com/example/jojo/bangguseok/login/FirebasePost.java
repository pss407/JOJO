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
    public String using;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String id, String password,String tier,String using) {
        this.id = id;
        this.password = password;
        this.tier = tier;
        this.using = using;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("password", password);
        result.put("tier", tier);
        result.put("using", using);
        return result;
    }
}
