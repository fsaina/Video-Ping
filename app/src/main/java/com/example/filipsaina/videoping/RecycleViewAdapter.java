package com.example.filipsaina.videoping;

/**
 * Created by filipsaina on 15/05/15.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

    private RecycleViewItemData[] dataItems;
    private RecyclerView rv;

    public RecycleViewAdapter(RecycleViewItemData[] items, RecyclerView rv){
        dataItems = items;
        this.rv = rv;
    }

    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_view, parent, false);


        itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = rv.getChildPosition(v);
                System.out.println("klinuto " + dataItems[itemPosition].getVideoTitle());
            }
        });


        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder viewHolder, int i) {
        // the >int i< variable refers to the positin of the element inside the view
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData


        //TODO defien the data structure which will be used
        viewHolder.txtViewTitle.setText(dataItems[i].getVideoTitle());
        //viewHolder.imgViewIcon.setImageResource(dataItems[i].getImageURL());          //good code, false data
    }


    //return the number of elements inside the recycleView
    @Override
    public int getItemCount() {
        return dataItems.length;
    }



    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
        }

    }

}
