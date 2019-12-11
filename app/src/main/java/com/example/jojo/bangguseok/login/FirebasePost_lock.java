package com.example.jojo.bangguseok.login;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasePost_lock {
    public String lock1;


    public FirebasePost_lock(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost_lock(String id, String password, String tier, String using, String start_matching, String experience) {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lock1", lock1);

        return result;
    }
}
