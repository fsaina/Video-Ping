package com.example.filipsaina.videoping;

/**
 * Adapter definition for the RecyclerView
 * Created by filipsaina on 15/05/15.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.example.filipsaina.videoping.provider.ProviderList;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

    private RecycleViewItemData[] dataItems;
    private RecyclerView rv;
    private HomeActivity home;

    public RecycleViewAdapter(RecycleViewItemData[] items, RecyclerView rv, HomeActivity home){
        dataItems = items;
        this.rv = rv;
        this.home = home;
    }

    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        final View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_view, parent, false);

        //add a listener to a item
        itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(home, R.anim.on_recycle_view_item_click_animation));

                int itemPosition = rv.getChildPosition(v);
                RecycleViewItemData element = dataItems[itemPosition];

                Intent intent = new Intent(home, PlayerActivity.class);
                intent.putExtra("videoTitle", element.getVideoTitle());
                intent.putExtra("videoDescription", element.getVideoDescription());
                intent.putExtra("imageUrl", element.getImageURL());
                intent.putExtra("videoId", element.getVideoId());
                intent.putExtra("duration", element.getDurationInSeconds());
                intent.putExtra("providerIndex", element.getProviderIndex());

                home.startActivity(intent);
                home.overridePendingTransition(R.anim.right_to_left_enter_element, R.anim.right_to_left_exit_element);
            }
        });
        // create ViewHolder and return it
        return new ViewHolder(itemLayoutView);
    }

    // the >int i< variable refers to the positin of the element inside the view
    // - get data from your itemsData at this position
    // - replace the contents of the view with that itemsData
    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder viewHolder, int i) {


        RecycleViewItemData element = dataItems[i];  //grab the element in out data structure

        //set Textual widgets
        viewHolder.txtViewTitle.setText(element.getVideoTitle());
        viewHolder.txtViewDescription.setText(element.getVideoDescription());
        viewHolder.providerName.setText(ProviderList.getProviderNameWithIndex(element.getProviderIndex()));
        viewHolder.duration.setText(getFromatedTime(element.getDurationInSeconds()));

        //set Images(Thumbnails)
        AQuery aq = new AQuery(viewHolder.imgViewIcon);
        //round the corners of the images and define other image parameters
        ImageOptions options = new ImageOptions();
        options.round = 15;
        options.animation = AQuery.FADE_IN;
        options.memCache = true;
        options.fileCache = true;
        aq.id(viewHolder.imgViewIcon.getId()).image(dataItems[i].getImageURL(),options);
    }

    //return the number of elements inside the recycleView
    @Override
    public int getItemCount() {
        return dataItems.length;
    }

    //method formats seconds to standard time (Joda-time)
    public static String getFromatedTime(int seconds){
        LocalTime time = new LocalTime(0, 0); // midnight
        time = time.plusSeconds(seconds);
        String output = DateTimeFormat.forPattern("mm:ss").print(time);
        return output;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView txtViewTitle;
        public TextView txtViewDescription;
        public TextView providerName;
        public ImageView imgViewIcon;
        public TextView duration;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            txtViewDescription = (TextView) itemLayoutView.findViewById(R.id.item_description);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            providerName = (TextView) itemLayoutView.findViewById(R.id.providerName);
            duration = (TextView) itemLayoutView.findViewById(R.id.duration);
        }
    }
}