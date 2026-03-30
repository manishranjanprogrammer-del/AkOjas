package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.ui.act.ActAstroShop;
import com.ojassoft.astrosage.ui.act.AstroShopItemDescription;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Suggestions Adapter.
 *
 * @author Miguel Catalan Bañuls
 */
public class
ProductSearchAdapter extends BaseAdapter implements Filterable {

    private ArrayList<AstroShopItemDetails>  data;
    private Context ctx;

    private ArrayList<AstroShopItemDetails> suggestions;
    private Drawable suggestionIcon;
    private LayoutInflater inflater;
    private boolean ellipsize;
    MyCustomToast mct;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;


    public ProductSearchAdapter(Context context, ArrayList<AstroShopItemDetails> suggestions) {
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.suggestions = suggestions;
        ctx=context;
    }

    public ProductSearchAdapter(Context context, ArrayList<AstroShopItemDetails> suggestions, Drawable suggestionIcon, boolean ellipsize) {
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
                    List<AstroShopItemDetails> searchData = new ArrayList<>();
                    for (AstroShopItemDetails items : suggestions) {
                        if (items.getPName().toLowerCase().trim().contains(constraint.toString().toLowerCase().trim())) {
                            searchData.add(items);
                        }
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    data = (ArrayList<AstroShopItemDetails>) results.values;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        SuggestionsViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.suggest_item, parent, false);
            viewHolder = new SuggestionsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuggestionsViewHolder) convertView.getTag();
        }

        final AstroShopItemDetails currentListData =  (AstroShopItemDetails) getItem(position);

        viewHolder.textView.setText(currentListData.getPName());
        viewHolder.imageView.setImageUrl(currentListData.getPImgUrl(), VolleySingleton.getInstance(ctx).getImageLoader());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentListData.getP_OutOfStock().equalsIgnoreCase("True"))
                {
                    Typeface typeface = CUtils.getRobotoFont(
                            ctx, LANGUAGE_CODE, CGlobalVariables.regular);
                    LANGUAGE_CODE = ((AstrosageKundliApplication) ((ActAstroShop)ctx).getApplication())
                            .getLanguageCode();
                    MyCustomToast mct = new MyCustomToast(ctx, ((ActAstroShop)ctx).getLayoutInflater(), (ActAstroShop)ctx, typeface);
                    mct.show(ctx.getResources().getString(R.string.out_stock));
                }
                else
                {
                    Intent itemdescriptionIntent = new Intent(ctx, AstroShopItemDescription.class);
                    Bundle bundle = new Bundle();
                    ArrayList<AstroShopItemDetails> list=CUtils.getAllchilditems(currentListData,data);
                    bundle.putSerializable("key",list);
                    bundle.putSerializable("Item", currentListData);

                    itemdescriptionIntent.putExtras(bundle);
                    ctx.startActivity(itemdescriptionIntent);
                }

            }
        });
        //  viewHolder.imageView.setImageUrl(currentListData.getPImgUrl(), VolleySingleton.getInstance(ctx).getImageLoader());
        if (ellipsize) {
            viewHolder.textView.setSingleLine();
            viewHolder.textView.setEllipsize(TextUtils.TruncateAt.END);
        }

        return convertView;
    }

    private class SuggestionsViewHolder {

        TextView textView;
        NetworkImageView imageView;

        public SuggestionsViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.suggestion_text);
            imageView = (NetworkImageView) convertView.findViewById(R.id.suggestion_icon);


            if (suggestionIcon != null) {
                //imageView.setImageDrawable(suggestionIcon);
            }
        }
    }
}