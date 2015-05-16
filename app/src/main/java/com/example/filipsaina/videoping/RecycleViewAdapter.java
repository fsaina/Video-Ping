package com.example.filipsaina.videoping;

/**
 * Created by filipsaina on 15/05/15.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;


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
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_view, parent, false);

        //add a listener to a item
        itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = rv.getChildPosition(v);
                RecycleViewItemData element = dataItems[itemPosition];

                Intent intent = new Intent(home,PlayerActivity.class);
                intent.putExtra("videoTitle", element.getVideoTitle());
                intent.putExtra("videoDescription", element.getVideoDescription());
                intent.putExtra("imageUrl", element.getImageURL());
                intent.putExtra("videoId", element.getVideoId());


                home.startActivity(intent);
                home.overridePendingTransition(R.anim.right_to_left_enter_element, R.anim.right_to_left_exit_element);
            }
        });

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
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

        //set Images(Thumbnails)
        //TODO check AQuery doc, example code (prefferably add some animations or better image caching)
        AQuery aq = new AQuery(viewHolder.imgViewIcon);
        aq.id(viewHolder.imgViewIcon.getId()).image(dataItems[i].getImageURL(), true, true);

    }

    //return the number of elements inside the recycleView
    @Override
    public int getItemCount() {
        return dataItems.length;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView txtViewTitle;
        public TextView txtViewDescription;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            txtViewDescription = (TextView) itemLayoutView.findViewById(R.id.item_description);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
        }
    }

}
