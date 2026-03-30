package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.ChalisaDataModel;
import com.ojassoft.astrosage.ui.act.ChalisaDetailsActivity;
import com.ojassoft.astrosage.ui.act.MantraChalisaHomeActivity;

import java.util.List;

public class ChalisaPlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    MyViewHolder myViewHolder;
    private List<ChalisaDataModel> chalisaList;
    private Context context;

    public ChalisaPlayListAdapter(Context ctx, List<ChalisaDataModel> chalisaList) {
        context = ctx;
        this.chalisaList = chalisaList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chalisa, parent, false);

            return new MyViewHolder(view);
        }
        return null;
    }

    private void startVedio(int position) {
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("DATA", chalisaList.get(position));
            Intent i = new Intent(context, ChalisaDetailsActivity.class);
            i.putExtra("DATA", bundle);
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return chalisaList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            myViewHolder = (MyViewHolder) holder;
            ChalisaDataModel bean = chalisaList.get(position);
            myViewHolder.titleTV.setText(bean.getTitle());
            myViewHolder.desc.setText(Html.fromHtml(bean.getDescription()));
            myViewHolder.imageView.setImageUrl(bean.getThumbnail(), VolleySingleton.getInstance(context).getImageLoader());
            myViewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            if (getItemCount() - 1 == position) {
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 25);
                myViewHolder.card_view.setLayoutParams(buttonLayoutParams);
            } else {
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 0);
                myViewHolder.card_view.setLayoutParams(buttonLayoutParams);
            }
        }
    }

    @Override
    public int getItemCount() {
        return chalisaList == null ? 0 : chalisaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTV;
        public TextView desc;
        public NetworkImageView imageView;
        public LinearLayout linearLayout;
        public CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            titleTV = view.findViewById(R.id.titleTV);
            desc = view.findViewById(R.id.descTV);
            if (context != null) {
                desc.setTypeface(((MantraChalisaHomeActivity) context).regularTypeface);
                titleTV.setTypeface(((MantraChalisaHomeActivity) context).mediumTypeface);
            }
            imageView = view.findViewById(R.id.imageview);
            linearLayout = view.findViewById(R.id.main_container);
            card_view = view.findViewById(R.id.card_view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startVedio(getLayoutPosition());
                }
            });
            desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startVedio(getLayoutPosition());
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startVedio(getLayoutPosition());
                }
            });

        }
    }
}