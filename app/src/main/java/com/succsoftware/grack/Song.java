package com.succsoftware.grack;

/**
 * Created by Matt on 04/02/2017.
 */

public class Song {
    int type; //0 local mp3, 1 spotify, 2 soundcloud
    String title;
    String artist;

    public void getDetails(String data){
        if(data.toLowerCase().contains("spotify")){
            type = 1;
        }
        else if(data.toLowerCase().contains("soundcloud")){
            type = 2;
        }
    }

}
