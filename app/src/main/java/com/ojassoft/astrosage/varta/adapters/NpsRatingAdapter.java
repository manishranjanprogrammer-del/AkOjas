package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.List;

/**
 * Created by ojas on २८/३/१६.
 */
public class NpsRatingAdapter extends RecyclerView.Adapter<NpsRatingAdapter.MyViewHolder> {
    List<Integer> ratingIntegerList;
    Context context;
    private RecyclerClickListner mRecyclerClickListner;

    public NpsRatingAdapter(Context context, List<Integer> ratingIntegerList) {
        this.context = context;
        this.ratingIntegerList = ratingIntegerList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nps_rating_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (ratingIntegerList != null) {
            FontUtils.changeFont(context, holder.tvRating, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            //holder.tvRating.setTextColor(context.getResources().getColor(R.color.darker_gray));
            holder.tvRating.setText(String.valueOf(ratingIntegerList.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        if (ratingIntegerList == null) {
            return 0;
        }
        return ratingIntegerList.size();
    }

    public void setOnClickListener(RecyclerClickListner recyclerClickListner) {
        mRecyclerClickListner = recyclerClickListner;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvRating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mRecyclerClickListner.onItemClick(getAdapterPosition(), v);
        }
    }
}
