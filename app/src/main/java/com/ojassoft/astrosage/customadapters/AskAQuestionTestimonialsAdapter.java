package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AskAQuestionTestimonials;
import com.ojassoft.astrosage.beans.CircularNetworkImageView;

import java.util.List;

/**
 * Created by ojas on 6/6/17.
 */

public class AskAQuestionTestimonialsAdapter extends RecyclerView.Adapter<AskAQuestionTestimonialsAdapter.MyViewHolder>{

    private List<AskAQuestionTestimonials> testimonialList;
    private ImageLoader mImageLoader;
    private Typeface typeface;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContent;
        public CircularNetworkImageView imgUser;
        public TextView tvContentBy;

        public MyViewHolder(View view) {
            super(view);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            imgUser = (CircularNetworkImageView) view.findViewById(R.id.imgUser);
            tvContentBy = (TextView) view.findViewById(R.id.tvContentBy);
        }
    }


    public AskAQuestionTestimonialsAdapter(List<AskAQuestionTestimonials> testimonialList, Typeface typeface,Context context) {
        this.testimonialList = testimonialList;
        this.mImageLoader  = VolleySingleton.getInstance(context).getImageLoader();;
        this.typeface = typeface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ask_a_question_testimonials, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AskAQuestionTestimonials testimonials = testimonialList.get(position);

        holder.tvContent.setTypeface(typeface);
        holder.tvContentBy.setTypeface(typeface);

        holder.tvContent.setText(testimonials.getContent());
        holder.imgUser.setImageResource(testimonials.getImgUrl());

        holder.tvContentBy.setText(testimonials.getContentBy());
    }

    @Override
    public int getItemCount() {
        return testimonialList.size();
    }

}
