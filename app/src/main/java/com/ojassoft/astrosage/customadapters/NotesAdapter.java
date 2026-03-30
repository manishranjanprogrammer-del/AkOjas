package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.libojassoft.android.models.NotesModel;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private RecyclerClickListner recyclerClickListner;
    private Context context;
    private List<NotesModel> notesModelList;

    public NotesAdapter(Context context, List<NotesModel> notesModelList) {
        this.notesModelList = notesModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_notes, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (notesModelList != null && notesModelList.size() > position) {
            NotesModel notesModel = notesModelList.get(position);
            if (notesModel == null) {
                return;
            }
            if (notesModel.isCurrentDay()) {
                holder.dateText.setText(notesModel.getDate() + " (" + context.getString(R.string.text_today) + ")");
                holder.containerLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.resultBg));
            } else {
                holder.dateText.setText(notesModel.getDate() + "");
                holder.containerLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundColorView));
            }

            holder.notesText.setText(notesModel.getNotes());
            if (TextUtils.isEmpty(notesModel.getNotes())) {
                holder.notesText.setVisibility(View.GONE);
            } else {
                holder.notesText.setVisibility(View.VISIBLE);
            }
            if (holder.lineViewLL != null) {
                if (position == notesModelList.size() - 1) {
                    holder.lineViewLL.setVisibility(View.GONE);
                } else {
                    holder.lineViewLL.setVisibility(View.VISIBLE);
                }
            }

            if (!TextUtils.isEmpty(notesModel.getNotes())) {
                holder.iconEdit.setVisibility(View.VISIBLE);
                holder.iconAdd.setVisibility(View.GONE);
            } else {
                holder.iconEdit.setVisibility(View.GONE);
                holder.iconAdd.setVisibility(View.VISIBLE);
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
        private ImageView iconAdd;
        private ImageView iconEdit;

        public MyViewHolder(View view) {
            super(view);

            lineViewLL = view.findViewById(R.id.lineViewLL);
            containerLayout = view.findViewById(R.id.containerLayout);
            dateText = view.findViewById(R.id.date_text);
            notesText = view.findViewById(R.id.notes_text);
            iconEdit = view.findViewById(R.id.iconEdit);
            iconAdd = view.findViewById(R.id.iconAdd);

            FontUtils.changeFont(context, notesText, AppConstants.FONT_ROBOTO_REGULAR);
            FontUtils.changeFont(context, dateText, AppConstants.FONT_ROBOTO_REGULAR);
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
