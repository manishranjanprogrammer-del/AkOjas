package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.AstrologerBioModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class AstrologerBioAdapter extends RecyclerView.Adapter<AstrologerBioAdapter.MyViewHolder> {

    Context context;
    ArrayList<AstrologerBioModel> astrologerBioModelArrayList;

    public AstrologerBioAdapter(Context context, ArrayList<AstrologerBioModel> astrologerBioModelArrayList)
    {
        this.context = context;
        this.astrologerBioModelArrayList = astrologerBioModelArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bio_row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
        astrologerBioModel = astrologerBioModelArrayList.get(position);
        holder.bio_txt.setText(astrologerBioModel.getAstrologerBioName());
        holder.bio_desription_txt.setText(Html.fromHtml(astrologerBioModel.getAstrologerBioDescription()));
        holder.bio_img.setImageResource(astrologerBioModel.getImageFile());
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(astrologerBioModelArrayList.size()>0)
        {
            count = astrologerBioModelArrayList.size();
        }
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bio_txt, bio_desription_txt;
        ImageView bio_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bio_txt = (TextView) itemView.findViewById(R.id.bio_txt);
            bio_desription_txt = (TextView) itemView.findViewById(R.id.bio_desription_txt);
            bio_img = (ImageView)itemView.findViewById(R.id.bio_img);

            FontUtils.changeFont(context, bio_txt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, bio_desription_txt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        }
    }
}