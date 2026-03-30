package com.ojassoft.astrosage.varta.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.Language;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private List<Language> languageList;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnLanguageClickListener listener;
    private int oldLanguageIndexo=0;


    public interface OnLanguageClickListener {
        void onLanguageClick(Language language);
    }

    public LanguageAdapter(List<Language> languageList, OnLanguageClickListener listener,int oldLanguageIndex) {
        this.languageList = languageList;
        this.listener = listener;
        this.oldLanguageIndexo = oldLanguageIndex;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_language, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        Language language = languageList.get(position);
        holder.tvLanguage.setText(language.getName());
        holder.imgPortrait.setImageResource(language.getFlagResId());


        if (oldLanguageIndexo == language.getLanguageId())
            selectedPosition = position;

        // Show overlay if selected
        holder.selectedOverlay.setVisibility(
                position == selectedPosition ? View.VISIBLE : View.GONE
        );

        holder.itemView.setOnClickListener(v -> {
            int prevSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            oldLanguageIndexo = language.getLanguageId();
            // Update old + new selected item
            notifyItemChanged(prevSelected);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onLanguageClick(language);
            }
        });
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public Language getSelectedLanguage() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return languageList.get(selectedPosition);
        }
        return null;
    }

    static class LanguageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPortrait;
        TextView tvLanguage;
        View selectedOverlay;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPortrait = itemView.findViewById(R.id.imgPortrait);
            tvLanguage = itemView.findViewById(R.id.tvLanguage);
            selectedOverlay = itemView.findViewById(R.id.selectedOverlay);
        }
    }
}
