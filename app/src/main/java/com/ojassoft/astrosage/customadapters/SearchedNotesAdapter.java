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
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.libojassoft.android.models.NotesModel;
import com.ojassoft.astrosage.utils.indnotes.DateTimeUtils;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.List;


public class SearchedNotesAdapter extends RecyclerView.Adapter<SearchedNotesAdapter.MyViewHolder> {

    private RecyclerClickListner recyclerClickListner;
    private Context context;
    private List<NotesModel> notesModelList;
    private int rowPos;

    public SearchedNotesAdapter(Context context, List<NotesModel> notesModelList) {
        this.notesModelList = notesModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_searched_notes, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (notesModelList != null && notesModelList.size() > position) {
            NotesModel notesModel = notesModelList.get(position);
            if (notesModel == null) {
                return;
            }
            holder.dateText.setText(DateTimeUtils.getMonthString(context, notesModel.getMonth(), notesModel.getDay()));
            holder.notesText.setText(notesModel.getNotes());

            holder.lineViewLL.setVisibility(View.VISIBLE);

            if (notesModel.getMonth() == 0 || notesModel.getDay() == 0) {
                holder.dateText.setVisibility(View.GONE);
            } else {
                holder.dateText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (notesModelList == null) return 0;
        return notesModelList.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout containerLayout;
        public LinearLayout lineViewLL;
        public TextView dateText;
        public TextView notesText;

        public MyViewHolder(View view) {
            super(view);

            lineViewLL = view.findViewById(R.id.lineViewLL);
            containerLayout = view.findViewById(R.id.containerLayout);
            dateText = view.findViewById(R.id.date_text);
            notesText = view.findViewById(R.id.notes_text);

            FontUtils.changeFont(context, dateText, AppConstants.FONT_ROBOTO_REGULAR);
            FontUtils.changeFont(context, notesText, AppConstants.FONT_ROBOTO_REGULAR);

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
