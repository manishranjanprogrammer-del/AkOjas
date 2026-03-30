package com.ojassoft.astrosage.model;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActPlayVideo;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.WebViewActivity;

import java.util.List;

public class HomeArticlesAdapter extends RecyclerView.Adapter<HomeArticlesAdapter.ViewHolder> {
    public List<ArticleModel> list;
    public Activity activity;

        /**
     * Constructs a HomeArticlesAdapter for displaying articles in a RecyclerView.
     * @param list List of ArticleModel objects to display
     * @param activity The parent activity context
     */
    public HomeArticlesAdapter(List<ArticleModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

        /**
     * Updates the adapter's article list and refreshes the RecyclerView.
     * @param list New list of ArticleModel objects
     */
    public void setArticleList(List<ArticleModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

        /**
     * Inflates the article item layout and creates a ViewHolder.
     * @param parent The parent ViewGroup
     * @param viewType The view type of the new View
     * @return A new ViewHolder instance
     */
    @NonNull
    @Override
    public HomeArticlesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_article_item_layout, parent, false);
        return new HomeArticlesAdapter.ViewHolder(view);
    }

        /**
     * Binds data to the ViewHolder for each article item.
     * Handles click events to open articles in a WebView.
     * Error handling ensures that null or invalid data does not crash the app.
     * @param holder The ViewHolder to bind data to
     * @param position The position in the data set
     */
    @Override
    public void onBindViewHolder(@NonNull HomeArticlesAdapter.ViewHolder holder, int position) {
        // Defensive check for null list or invalid position
        if (list == null || position < 0 || position >= list.size()) {
            holder.titleText.setText("");
            return;
        }
        ArticleModel article = list.get(position);
        String thumbnail = (article != null && article.getThumbnailUrl() != null) ? article.getThumbnailUrl() : "";
        // Set thumbnail image using Volley; handles empty or null URL gracefully
        holder.thumbnailImage.setImageUrl(thumbnail, VolleySingleton.getInstance(activity).getImageLoader());
        holder.titleText.setText(article != null ? article.getTitle() : "");
        holder.titleText.setTypeface(CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.medium));
        holder.parentLayout.setOnClickListener(v -> {
            // Only proceed if article and link are valid
            CUtils.fcmAnalyticsEvents(CGlobalVariables.ANALYTICS_ARTICLE_CLICK_EVENT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "Home screen");
            if (article != null && article.getLink() != null && !article.getLink().isEmpty()) {
                String urlWithTheme = addParamsForDarkInURL(activity,article.getLink());
            //    Log.e("ARTICLEURL", "onBindViewHolder: "+urlWithTheme );
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("URL", urlWithTheme);
                intent.putExtra("TITLE_TO_SHOW", activity.getString(R.string.article));
                intent.putExtra("is_from_article",true);
                activity.startActivity(intent);
            }
        });
    }

        /**
     * Returns the total number of articles in the list.
     * @return The size of the article list, or 0 if null
     */
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

        /**
     * ViewHolder for article items in the RecyclerView.
     * Holds references to the thumbnail, title, and parent layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView thumbnailImage;
        TextView titleText;
        ConstraintLayout parentLayout;

        /**
         * Initializes the ViewHolder's views.
         * @param itemView The root view of the article item
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImage = itemView.findViewById(R.id.thumbnailIV);
            titleText = itemView.findViewById(R.id.titleTV);
            parentLayout = itemView.findViewById(R.id.articleLayout);
        }
    }


    public static String addParamsForDarkInURL(Activity activity, String url){
        int nightModeFlags = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            url = url+"?theme=darkMode";
        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            url = url+"?theme=lightMode";
        }
        return url;
    }
}

