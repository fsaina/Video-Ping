package com.example.filipsaina.videoping;

/**
Data model used for defining an element that populates the RecycleView widget.
Created by filipsaina on 15/05/15.
 */

public class RecycleViewItemData {

    private String videoTitle;
    private String videoId;
    private String imageURL;
    private String videoDescrition;
    private String duration;
    private int providerIndex;

    public RecycleViewItemData(String videoTitle, String imageURL, String videoId, String videoDescription, String duration, int providerIndex){
            this.videoTitle = videoTitle;
            this.imageURL = imageURL;
            this.videoId = videoId;
            this.videoDescrition = videoDescription;
            this.duration = duration;
            this.providerIndex = providerIndex;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoDescription(){
        return videoDescrition;
    }
    public String getVideoId(){
        return videoId;
    }
    public String getDuration() {return duration;}
    public int getProviderIndex(){
        return providerIndex;
    }
}
