package com.succsoftware.grack;

/**
 * Created by Matt on 04/02/2017.
 */

public class Song {
    private int type; //0 local mp3, 1 spotify, 2 soundcloud
    private String title;
    private String artist;
    private String domain;

    public Song(String domain){
        this.domain = domain;
    }

    public void getDetails(){
        if(domain.toLowerCase().contains("spotify")){
            type = 1;
        }
        else if(domain.toLowerCase().contains("soundcloud")){
            type = 2;

        }
        else{
            type=0;
        }
    }

    public int getType(){
        return (type);
    }

    public String getDomain() {
        return domain;
    }
}
