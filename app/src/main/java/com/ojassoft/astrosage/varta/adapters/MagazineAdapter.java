package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.CMessage;
import com.ojassoft.astrosage.varta.ui.activity.MagazineDescActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.List;

public class MagazineAdapter extends RecyclerView.Adapter<MagazineAdapter.MyView> {

    List<CMessage> list;

    Context context;
    Resources resources;

    public class MyView extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        NetworkImageView imageView;

        public MyView(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            textView = view.findViewById(R.id.textview);
            imageView = view.findViewById(R.id.imageView);
            FontUtils.changeFont(context, textView, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", list.get(getLayoutPosition()).getTitle());
                    bundle.putString("desc", list.get(getLayoutPosition()).getEncoded());
                    bundle.putString("creator", list.get(getLayoutPosition()).getCreator());
                    bundle.putString("pubDate", list.get(getLayoutPosition()).getDate());
                    bundle.putString("category", list.get(getLayoutPosition()).getCategory());
                    bundle.putString("imgUrl", list.get(getLayoutPosition()).getImageUrl());
                    Intent intent = new Intent(context, MagazineDescActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });
        }
    }

    public MagazineAdapter(Context context, List<CMessage> list) {
        this.context = context;
        this.list = list;
        resources = context.getResources();
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.magazine_list_item, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        holder.textView.setText(list.get(position).getTitle());
        if (list.get(position).getImageUrl() != null && list.get(position).getImageUrl().length() > 0)
            holder.imageView.setImageUrl(list.get(position).getImageUrl(), VolleySingleton.getInstance(context).getImageLoader());
        holder.imageView.setDefaultImageResId(R.drawable.magazine_default_image);
        holder.imageView.setErrorImageResId(R.drawable.magazine_default_image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
} 
