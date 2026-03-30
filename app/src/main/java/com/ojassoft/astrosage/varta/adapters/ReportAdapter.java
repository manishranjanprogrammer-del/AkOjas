package com.ojassoft.astrosage.varta.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.fragments.ReportsFragment;

import java.util.List;

/**
 * RecyclerView Adapter for displaying a list of report categories in the ReportsFragment.
 * Handles selection logic and communicates with the fragment to control the Buy Now button.
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private final List<ServicelistModal> reports;
    private int selectedPosition = -1;
    private ReportsFragment fragment;

    /**
     * Constructs a new ReportAdapter.
     * @param reports List of ServicelistModal objects to display.
     * @param reportsFragment The fragment that hosts this adapter (for callbacks).
     */
    public ReportAdapter(List<ServicelistModal> reports, ReportsFragment reportsFragment) {
        this.fragment = reportsFragment;
        this.reports = reports;
    }

    /**
     * Inflates the view for each report item.
     */
    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(view);
    }

    /**
     * Binds data to each report item and handles selection logic.
     * @param holder The ViewHolder for the item.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ServicelistModal item = reports.get(position);

        holder.title.setText(item.getTitle());
        // Load the report icon using Glide if available
        if (item != null && item.getSmallImgURL() != null && !item.getSmallImgURL().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getSmallImgURL())
                    .into(holder.reportIcon);
        }
        // Styling for selected/unselected state
        if (item.isSelected()) {
            holder.root.setBackgroundResource(R.drawable.bg_report_item_selected);
            holder.title.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        } else {
            holder.root.setBackgroundResource(R.drawable.bg_report_item_unselected);
            holder.title.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
        }

        // Handle item selection and update Buy Now button visibility
        holder.root.setOnClickListener(v -> {
            if (selectedPosition == position) {
                // Deselect if already selected
                item.setSelected(false);
                selectedPosition = -1;
                fragment.setBuyButtonVisible(selectedPosition, item.getTitle(), item.getPdfType());
            } else {
                // Deselect all, select the clicked item
                for (ServicelistModal report : reports) report.setSelected(false);
                item.setSelected(true);
                selectedPosition = position;
                fragment.setBuyButtonVisible(selectedPosition, item.getTitle(), item.getPdfType());
            }
            notifyDataSetChanged();
        });
    }

    /**
     * Returns the total number of report items.
     */
    @Override
    public int getItemCount() {
        return reports.size();
    }

    /**
     * ViewHolder for report items in the RecyclerView.
     */
    static class ReportViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        TextView title;
        ImageView reportIcon;

        /**
         * Constructs a new ReportViewHolder and binds view references.
         * @param itemView The root view of the item.
         */
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.reportItemRoot);
            title = itemView.findViewById(R.id.reportTitle);
            reportIcon = itemView.findViewById(R.id.reportIcon);
        }
    }
}
