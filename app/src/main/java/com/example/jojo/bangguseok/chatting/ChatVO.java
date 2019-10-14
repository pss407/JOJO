package com.example.jojo.bangguseok.chatting;

public class ChatVO {

    private int imageID ;
    private String id;
    private String content;
    private String time;

    public ChatVO(){}

    public ChatVO(int imageID, String id, String content, String time) {
        this.imageID = imageID;
        this.id = id;
        this.content = content;
        this.time = time;
    }

    public int getImageID() {
        return imageID;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
