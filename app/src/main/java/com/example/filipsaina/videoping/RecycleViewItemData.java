package com.example.filipsaina.videoping;

/**
 * Created by filipsaina on 15/05/15.
 */



public class RecycleViewItemData {
    //TODO define the view and define the data model that the layout will use(missing basic data features)

    private String videoTitle;
    private String videoId;
    private String imageURL;

    public RecycleViewItemData(String title, String url, String videoId){
            videoTitle = title;
            imageURL = url;
            this.videoId = videoId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
}
