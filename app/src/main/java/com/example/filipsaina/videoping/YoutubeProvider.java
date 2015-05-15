package com.example.filipsaina.videoping;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by filipsaina on 15/05/15.
 */
public class YoutubeProvider implements Provider {

    private ThreadCompleteListener listener = null;

    //keys to services
    private static final String YOUTUBE_API_KEY = "AIzaSyD0dkCKWmkzLIJJ0ALFokXlnq7e9n9epyo";
    private static final long LIMIT_RESULTS = 20;

    String searchTerm = "Charlie bit my finger";            //TODO implement real user input

    List<RecycleViewItemData> providerResult = new ArrayList<>();

    public YoutubeProvider(String searchTerm){
        this.searchTerm = searchTerm;
    }


    //TODO make documentation
    //method will return null in case of an excepition
    @Override
    public List<RecycleViewItemData> fetchDataFromServer() {
        try {
            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("Video ping").build();
            YouTube.Search.List search = youtube.search().list("id,snippet");


            //set key
            search.setKey(YOUTUBE_API_KEY);
            search.setQ(searchTerm);       //serch term is defined here

            //restric to videos
            search.setType("video");

            //grab from the servers just the stuff we need
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(LIMIT_RESULTS);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), searchTerm);
            }

        } catch (Exception e) {
            //TODO check documentation for thrown exceptions for better exception handling
            e.printStackTrace();
        }
        return providerResult;
    }

    private void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
//
//                System.out.println(" Video Id" + rId.getVideoId());
//                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
//                System.out.println(" Thumbnail: " + thumbnail.getUrl());
//                System.out.println("\n-------------------------------------------------------------\n");

                providerResult.add(new RecycleViewItemData(singleVideo.getSnippet().getTitle(), thumbnail.getUrl(), rId.getVideoId()));

            }
        }
    }
}
