package com.example.jojo.bangguseok.login;

import android.app.Application;

//전역 변수 설정
public class MyApplication extends Application {

    //전역 변수 설정


        private String name;   //여기서 name 이 id임
       // private String level;
        private String tier;
        private String send_url;
        private String get_url;
        private String url_room;

//
        @Override
        public void onCreate() {
            //전역 변수 초기화

            tier = "bronze";
            name="";
            super.onCreate();
        }

        @Override
        public void onTerminate() {
            super.onTerminate();
        }



        public void settier(String state){
            this.tier = state;
        }
        public void setname(String name){
            this.name = name;
        }
        public void setSend_url(String send_url) {this.send_url=send_url;}
        public void setGet_url(String get_url) {this.get_url=get_url;}
    public void setUrl_room(String url_room) {this.url_room=url_room;}
///


        public String gettier(){
            return tier;
        }
        public String getname(){
            return name;
        }

        public String getSend_url(){
        return send_url;
    }
       public String getGet_url(){
        return get_url;
    }

    public String getUrl_room(){
        return url_room;
    }
}





