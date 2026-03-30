package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.TrackModal;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;

/**
 * Created by ojas on २१/३/१८.
 */





/**
 * Created by ojas on २/३/१६.
 */
public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.ViewHolder> {
    private List<TrackModal> trackList;

    private Context context;
    private int itemPosition;
    private Bundle bundle;
    private TrackModal itemdetails;
    public TrackingAdapter()
    {}

    public TrackingAdapter(Context context, List<TrackModal> trackList) {
        this.context = context;
        this.trackList = trackList;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TrackingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                      int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_track_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        try {
            itemdetails = trackList.get(position);

            viewHolder.location.setText(itemdetails.getLocation());
            viewHolder.status.setText(itemdetails.getStatus_detail());
            viewHolder.time.setText(CUtils.formatDateForShipingDetail(itemdetails.getTime()));

/*if(position%2==0)
{
    viewHolder.llMain.setBackgroundColor(Color.CYAN);
}
else
{
    viewHolder.llMain.setBackgroundColor(Color.DKGRAY);

}*/
            //Log.e("tag","outposition" + position);
            //  viewHolder.setIsRecyclable(false);

        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context, "EXCEP", Toast.LENGTH_LONG).show();
        }
        //viewHolder.genderImage.setText(itemsData[position].getTitle());


    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
     //   private CardView cardView;
        private TextView status,location,time;
        private LinearLayout llMain;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
          //  cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);
            time = (TextView) itemLayoutView.findViewById(R.id.time);
            location = (TextView) itemLayoutView.findViewById(R.id.location);
            status = (TextView) itemLayoutView.findViewById(R.id.status);
            llMain=(LinearLayout) itemLayoutView.findViewById(R.id.llMain);
            time.setTypeface(((BaseInputActivity) context).robotRegularTypeface);
            location.setTypeface(((BaseInputActivity) context).robotRegularTypeface);
            status.setTypeface(((BaseInputActivity) context).robotRegularTypeface);



        /*    cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });*/

        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = trackList.size();
        //Log.e("tag","Size=" + size);
        return size;
    }



}