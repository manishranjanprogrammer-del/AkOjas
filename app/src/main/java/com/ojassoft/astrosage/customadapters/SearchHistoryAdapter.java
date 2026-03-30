package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {

    private RecyclerClickListner recyclerClickListner;
    private Context context;
    private List<String> historyList;

    public SearchHistoryAdapter(Context context, List<String> historyList) {
        this.historyList = historyList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_search_history, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (historyList != null && historyList.size() > position) {
            String history = historyList.get(position);
            if (history == null) {
                return;
            }
            holder.historyText.setText(history);
        }
    }

    @Override
    public int getItemCount() {
        if (historyList == null) return 0;
        return historyList.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView historyText;

        public MyViewHolder(View view) {
            super(view);

            historyText = view.findViewById(R.id.history_text);

            FontUtils.changeFont(context, historyText, AppConstants.FONT_ROBOTO_REGULAR);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerClickListner.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnClickListner(RecyclerClickListner recyclerClickListner) {
        this.recyclerClickListner = recyclerClickListner;
    }
}
