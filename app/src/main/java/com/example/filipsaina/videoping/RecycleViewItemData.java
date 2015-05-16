package com.example.filipsaina.videoping;

/**
 * Created by filipsaina on 15/05/15.
 */



public class RecycleViewItemData {
    //TODO define the view and define the data model that the layout will use(missing basic data features)

    private String videoTitle;
    private String videoId;
    private String imageURL;
    private String videoDescrition;

    public RecycleViewItemData(String videoTitle, String imageURL, String videoId, String videoDescription){
            this.videoTitle = videoTitle;
            this.imageURL = imageURL;
            this.videoId = videoId;
            this.videoDescrition = videoDescription;
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

//    public Provider getProvider(){
//        return provider;
//    }


    public String getVideoUrl(){
        //http://asdfadsfadsf.com/+ ... + videId
        return  null;  //TODO implement this
    }
}
