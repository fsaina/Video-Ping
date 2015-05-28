package com.example.filipsaina.videoping;

/**
Data model used for defining an element that populates the RecycleView widget.
Created by filipsaina on 15/05/15.
 */

public class RecycleViewItemData {

    private String videoTitle;
    private String videoId;
    private String imageURL;
    private String videoDescription;
    private int providerIndex;

    public RecycleViewItemData(String videoTitle, String imageURL, String videoId, String videoDescription, int providerIndex){
            this.videoTitle = videoTitle;
            this.imageURL = imageURL;
            this.videoId = videoId;
            this.videoDescription = videoDescription;
            this.providerIndex = providerIndex;
    }

    public String getImageURL() {
        return imageURL;
    }
    public String getVideoTitle() {
        return videoTitle;
    }
    public String getVideoDescription(){
        return videoDescription;
    }
    public String getVideoId(){
        return videoId;
    }
    public int getProviderIndex(){
        return providerIndex;
    }
}
