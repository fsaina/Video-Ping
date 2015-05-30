package com.example.filipsaina.videoping;

import android.os.Looper;

import com.example.filipsaina.videoping.provider.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/*
 * The idea behind ThreadManager class is to provide a simple way to manage threads(or operation if the ThreadManager thread does everything).
 * As long as Listeners agree to the 'ThreadCompleteListener' interface and implement the methods inside in the UI thread, this thread will
 * succeed in notifying of job completion everyone listed
 * Created by filipsaina on 15/05/15.
 */
public class ThreadManager extends Thread{

    private ThreadCompleteListener listener = null;
    private Set<Provider> jobs = new CopyOnWriteArraySet<>();
    private List<RecycleViewItemData> fullResponse = new ArrayList<>();
    private String searchTerm;

    public ThreadManager(String searchTerml){
        this.searchTerm = searchTerml;
    }


    public void addProvider(Provider job){
        jobs.add(job);
    }

    public void addListener(ThreadCompleteListener listener){
        this.listener = listener;
    }

    private void notifyListener(){
        this.listener.notifyOfThreadComplete(fullResponse);
    }

    @Override
    public void run() {
        Looper.prepare();       //necessary -> (may cause exceptions on some Android devices)
        try{

            for(Provider provider: jobs){
                List<RecycleViewItemData> response = provider.fetchDataFromServer(searchTerm);
                if(response != null){
                    fullResponse.addAll(response);
                }
                response.clear();           //due to processor caching - explicitly perform clear
            }

        }finally {
            notifyListener();
        }
    }
}