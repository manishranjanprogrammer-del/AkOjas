package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Suggestions Adapter.
 *
 * @author Miguel Catalan Bañuls
 */
public class SearchAdapter extends BaseAdapter implements Filterable {

    private ArrayList<String> data;
    private ArrayList<String> suggestions;
    private Drawable suggestionIcon;
    private LayoutInflater inflater;
    private boolean ellipsize;
    Context context;

    public SearchAdapter(Context context, ArrayList<String> suggestions) {
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.suggestions = suggestions;
    }

    public SearchAdapter(Context context, ArrayList<String> suggestions, Drawable suggestionIcon, boolean ellipsize) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.suggestions = suggestions;
        this.suggestionIcon = suggestionIcon;
        this.ellipsize = ellipsize;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {

                    // Retrieve the autocomplete results.
                    List<String> searchData = new ArrayList<>();

                    for (int i = 0; i < suggestions.size(); i++) {
                        if (context instanceof ActAppModule) {
                            if (suggestions.get(i).toLowerCase().contains(constraint.toString().toLowerCase())) {
                                searchData.add(suggestions.get(i));
                            }
                        } else {
                            if (suggestions.get(i).toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                                searchData.add(suggestions.get(i));
                            }
                        }

                    }

                    // Assign the data to the FilterResults
                    if (searchData.isEmpty()) {
                        searchData.add(context.getString(R.string.search_not_available));
                    }
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    data = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        SuggestionsViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.suggest_item, parent, false);
            viewHolder = new SuggestionsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuggestionsViewHolder) convertView.getTag();
        }

        String currentListData = (String) getItem(position);
        String[] separated = currentListData.split("###");
        viewHolder.textView.setText(separated[0]);
        if (ellipsize) {
            viewHolder.textView.setSingleLine();
            viewHolder.textView.setEllipsize(TextUtils.TruncateAt.END);
        }

        viewHolder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context != null && context instanceof ActAppModule) {
                    if(data != null && data.size() > 0) {
                        try{
                            String dataStr = data.get(position);
                            if (!TextUtils.isEmpty(dataStr) && dataStr.equalsIgnoreCase(context.getString(R.string.search_not_available))) {
                                return;
                            }
                            ((ActAppModule) context).openActivity(data.get(position));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } else if (context instanceof OutputMasterActivity) {
                    //((OutputMasterActivity) context).gotoSelectedPosition(data.get(position));
                }
            }
        });
        return convertView;
    }

    private class SuggestionsViewHolder {

        TextView textView;
        ImageView imageView;
        RelativeLayout mainContainer;

        public SuggestionsViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.suggestion_text);
            mainContainer = (RelativeLayout) convertView.findViewById(R.id.suggetion_container);

            if (suggestionIcon != null) {
                imageView = (ImageView) convertView.findViewById(R.id.suggestion_icon);
                imageView.setImageDrawable(suggestionIcon);
            }

        }
    }
}