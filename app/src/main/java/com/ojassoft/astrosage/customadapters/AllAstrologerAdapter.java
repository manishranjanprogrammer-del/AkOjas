package com.ojassoft.astrosage.customadapters;


import android.content.Context;

import android.os.Build;

import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;

import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CircularNetworkImageView;
import com.ojassoft.astrosage.model.AllAstrologerInfo;

import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.util.List;

/**
 * Created by ojas on ७/६/१६.
 */
public class AllAstrologerAdapter extends RecyclerView.Adapter<AllAstrologerAdapter.ViewHolder> {
    private List<AllAstrologerInfo> _nameList;
    private Context cxt;
    private VolleySingleton vsing;
    private ImageLoader imageLoader;

    FragmentManager fm;

    public AllAstrologerAdapter(Context context, FragmentManager fragmentManager, List<AllAstrologerInfo> _nameList) {
        this.cxt = context;
        this._nameList = _nameList;
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();
        fm = fragmentManager;

    }


    // Create new views (invoked by the layout manager)
    @Override
    public AllAstrologerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.act_all_astrologer_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        try {
            viewHolder.image_url.setImageUrl(_nameList.get(position).getImageUrl(), imageLoader);
            viewHolder.astrologer_name.setText(_nameList.get(position).getAstrologerName());
            String expertIn = _nameList.get(position).getExpertIn();

            if(expertIn.equals("")){
                viewHolder.astrologer_des.setText("(Vedic Astrologer)");
            }else{
                viewHolder.astrologer_des.setText(checkDescription(expertIn));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                viewHolder.astrologer_des.setAlpha(0.5f);
            }



            /*
            else if(expertIn.equalsIgnoreCase("Vedic")){
                viewHolder.astrologer_des.setText("Expertise: Vedic Astrologer");
            }else if(expertIn.equalsIgnoreCase("KP")){
                viewHolder.astrologer_des.setText("Expertise: Krishnamurthi Paddhati");
            }else if(expertIn.equalsIgnoreCase("Lalkitab")){
                viewHolder.astrologer_des.setText("Expertise: Lal Kitab");
            }else {
                viewHolder.astrologer_des.setText(_nameList.get(position).getExpertIn());
            }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircularNetworkImageView image_url;
        public TextView astrologer_name;
        public TextView astrologer_des;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            image_url = (CircularNetworkImageView) itemLayoutView.findViewById(R.id.image_view);
            astrologer_name = (TextView) itemLayoutView.findViewById(R.id.text_title);
            astrologer_des = (TextView) itemLayoutView.findViewById(R.id.text_title_description);
            astrologer_name.setTypeface(((BaseInputActivity) cxt).robotMediumTypeface);
            astrologer_des.setTypeface(((BaseInputActivity) cxt).robotRegularTypeface);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = 0;

        if(_nameList != null)
            size = _nameList.size();

        //Log.e("Size=" + size);
        return size;
    }

    private String checkDescription(String data){
        String result = "(";
        if(data.toUpperCase().contains("VEDIC")){
            data = data.replace("VEDIC","Vedic Astrologer");
        }

        if(data.toUpperCase().contains("KP")){
            data = data.replace("KP","Krishnamurthi Paddhati");
        }

        if(data.toUpperCase().contains("LALKITAB")){
            data = data.replace("LALKITAB","Lal Kitab");
        }

        result = result +data+ ")";

        return result;
    }
}