package com.ojassoft.astrosage.model;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import android.util.Log;
import com.ojassoft.astrosage.ui.act.ActAstrologerDescription;
import com.ojassoft.astrosage.ui.fragments.Astroshop_Frag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;

/**
 * RecyclerView Adapter for displaying a list of home report items.
 * Each item is represented by a ServicelistModal and displays an icon and title.
 * Handles item click events and supports efficient list updates using DiffUtil.
 */
public class HomeReportItemAdapter extends RecyclerView.Adapter<HomeReportItemAdapter.ViewHolder> {
    /**
     * Context used for inflating layouts and image loading.
     */
    Context context;
    /**
     * List of report items to display.
     */
    List<ServicelistModal> data;
    /**
     * Language code to be passed for navigation or actions.
     */
    int LANGUAGE_CODE = 0;

    /**
     * Efficiently update the list
     * @param newData The new list of ServicelistModal items to display.
     */
    public void setReportList(List<ServicelistModal> newData) {
            this.data = newData;
            notifyDataSetChanged();
    }

    /**
     * Constructs the adapter with the given context, data list, and language code.
     * @param context The context for inflating layouts and image loading.
     * @param data The initial list of report items to display.
     * @param LANGUAGE_CODE The language code for navigation or actions.
     */
    public HomeReportItemAdapter(Context context, List<ServicelistModal> data, int LANGUAGE_CODE) {
        this.context = context;
        this.data = data;
        this.LANGUAGE_CODE = LANGUAGE_CODE;
    }

    /**
     * Inflates the item layout and creates a ViewHolder for each item.
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public HomeReportItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_home_item_layout, parent, false);
        return new HomeReportItemAdapter.ViewHolder(view);
    }

    /**
     * Binds data to the item view at the specified position.
     * Loads the image using Glide and sets up the click listener.
     * @param holder The ViewHolder for the item.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull HomeReportItemAdapter.ViewHolder holder, int position) {

        // Null and bounds check
        if (data == null || position < 0 || position >= data.size()) {
            holder.categoryNameTV.setText("");
            return;
        }
        ServicelistModal itemDetails = data.get(position);
        holder.categoryNameTV.setText(itemDetails != null ? itemDetails.getTitle() : "");
        holder.categoryNameTV.setTypeface(CUtils.getRobotoFont(context, LANGUAGE_CODE, CGlobalVariables.medium));

        if (itemDetails != null && itemDetails.getSmallImgURL() != null && !itemDetails.getSmallImgURL().isEmpty()) {
            Glide.with(context)
                    .load(itemDetails.getSmallImgURL())
                    .into(holder.categoryIcon);

        }
            holder.itemView.setOnClickListener(view1 -> {
                try {
                    HomeReportItemAdapter.this.setDataTosendDescription(holder.getAbsoluteAdapterPosition());
                    String analyticsLabel = CGlobalVariables.HOME_REPORTS_CLICK + "_" + itemDetails.getTitle();
                    analyticsLabel = analyticsLabel.replace(" ", "_");
//                    Log.e("LabelCheck", "PRODUCT onBindViewHolder: " + analyticsLabel);
                    CUtils.fcmAnalyticsEvents(analyticsLabel, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "Home screen");
                }catch(Exception e){
                    Log.e("HomeReportItemAdapter", "onBindViewHolder: exception -"+e.getMessage() );
                }
            });

    }

    /**
     * Returns the total number of items in the data list.
     * @return The item count.
     */
    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    /**
     * ViewHolder class holding references to the views for each item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * ImageView for displaying the category icon.
         */
        ImageView categoryIcon;
        /**
         * TextView for displaying the category name.
         */
        TextView categoryNameTV;

        /**
         * Constructs the ViewHolder and initializes view references.
         * @param itemView The item view.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.imageViewProductIcon);
            categoryNameTV = itemView.findViewById(R.id.textViewProductName);

        }
    }


    /**
     * Handles item click event, safely checks context before navigation.
     * Launches the appropriate activity or action using the item's deep link URL.
     * @param itemPosition The position of the clicked item.
     */
    private void setDataTosendDescription(int itemPosition) {
        if (data == null || itemPosition < 0 || itemPosition >= data.size()) {
            Log.w("HomeReportItemAdapter", "Invalid item position: " + itemPosition);
            return;
        }
        ServicelistModal servicelistModal = data.get(itemPosition);
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            Log.e("HomeReportItemAdapter", "Context is not an Activity");
            return;
        }
        CUtils.getUrlLink(servicelistModal.getServiceDeepLinkURL(), activity, LANGUAGE_CODE, 0);
        CUtils.BRIHAT_KUNDALI_PURCHASE_SOURCE = "Home Report";
    }
}
