package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.model.MuhuratModel;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.List;

public class MuhuratAdapter extends RecyclerView.Adapter<MuhuratAdapter.MyViewHolder> {

    private Context context;
    private List<MuhuratModel> muhuratModelList;
    private int rowPos;

    public MuhuratAdapter(Context context, List<MuhuratModel> muhuratModelList) {
        this.muhuratModelList = muhuratModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_muhurat, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (muhuratModelList != null && muhuratModelList.size() > position) {
            MuhuratModel muhuratModel = muhuratModelList.get(position);
            if (muhuratModel == null) {
                return;
            }
            if (position == 0) {
                holder.topViewLL.setVisibility(View.VISIBLE);
            } else {
                holder.topViewLL.setVisibility(View.GONE);
            }

            holder.dateText.setText(muhuratModel.getDateString());
            holder.nameText.setText(muhuratModel.getName());

        }

    }

    @Override
    public int getItemCount() {
        if (muhuratModelList == null) return 0;
        return muhuratModelList.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout topViewLL;
        public TextView dateText;
        public TextView nameText;

        public MyViewHolder(View view) {
            super(view);
            topViewLL = view.findViewById(R.id.topViewLL);
            dateText = view.findViewById(R.id.dateText);
            nameText = view.findViewById(R.id.nameText);

            FontUtils.changeFont(context, dateText, AppConstants.FONT_ROBOTO_REGULAR);
            FontUtils.changeFont(context, nameText, AppConstants.FONT_ROBOTO_MEDIUM);

        }
    }
}
