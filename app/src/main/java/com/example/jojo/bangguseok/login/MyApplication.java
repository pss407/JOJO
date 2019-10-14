package com.example.jojo.bangguseok.login;

import android.app.Application;

//전역 변수 설정
public class MyApplication extends Application {

    //전역 변수 설정


        private String name;
        private String level;
        private String tier;

//
        @Override
        public void onCreate() {
            //전역 변수 초기화
            level = "1";
            tier = "bronze";
            name="";
            super.onCreate();
        }

        @Override
        public void onTerminate() {
            super.onTerminate();
        }

        public void setlevel(String state){
            this.level = state;
        }

        public void settier(String state){
            this.tier = state;
        }
        public void setname(String name){
            this.name = name;
        }
///


        public String getlevel(){
            return level;
        }
        public String gettier(){
            return tier;
        }
        public String getname(){
            return name;
        }

}





