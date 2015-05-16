package com.example.filipsaina.videoping;

import android.os.Looper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by filipsaina on 15/05/15.
 * The idea behing threadManager class is to provide a simple way to manage threads(or operation if the ThreadManaer thread does everything).
 * As long as Listeners agree to the 'ThreadCompleteListener' interaface and implement the methods inside in the UI thread, this thread will
 * suceed in notifying of job completion
 */
public class ThreadManager extends Thread{

    private ThreadCompleteListener listener = null;     //TODO check this line of code, provide exceptions
    private Set<Provider> jobs = new CopyOnWriteArraySet<>();
    private List<RecycleViewItemData> fullResponse = null;
    private String searchTerm;

    public ThreadManager(String searchTerm){
        this.searchTerm = searchTerm;
    }


    public void addProvider(Provider job){
        jobs.add(job);
    }

    public void addListener(ThreadCompleteListener listener){
        this.listener = listener;
    }

    private void notifyListener(){
        this.listener.notifyOfThreadComplete(fullResponse);     //TODO insted of 'this', maybe we could deliver the data
    }

    @Override
    public void run() {
        Looper.prepare();       //necessary
        try{

            for(Provider provider: jobs){
                List<RecycleViewItemData> response = provider.fetchDataFromServer(searchTerm);
                if(response != null){
                    fullResponse = response;    //TODO make a list within a list to store responses from other providers
                }
            }

        }finally {
            notifyListener();         //  <<==== should pass data
        }
    }

}