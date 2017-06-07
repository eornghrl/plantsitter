package com.example.zzamtiger.textview;

import java.util.Date;

/**
 * Created by 성호기 on 2017-05-31.
 */

public class list_item {
    //추가한 변수
    private String profile_image;
    private String nickname;
    private String title;
    private String write_date;
    private String content;

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWrite_date() {
        return write_date;
    }

    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }


    public list_item(String profile_image, String nickname, String title, String write_date, String content) {
        this.profile_image = profile_image;
        this.nickname = nickname;
        this.title = title;
        this.write_date = write_date;
        this.content = content;
    }
}
