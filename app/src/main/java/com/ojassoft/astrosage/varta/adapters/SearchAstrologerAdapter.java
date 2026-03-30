package com.ojassoft.astrosage.varta.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.SearchActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class SearchAstrologerAdapter extends ArrayAdapter<AstrologerDetailBean> implements Filterable {

	Context context;
	int resource;
	int textViewResourceId;
	List<AstrologerDetailBean> mList, filteredPeople, mListAll;


	public SearchAstrologerAdapter(Context context, int resource, int textViewResourceId,
								   List<AstrologerDetailBean> mList) {
		super(context, resource, textViewResourceId, mList);
		this.context = context;
		this.resource = resource;
		this.textViewResourceId = textViewResourceId;
		this.mList = mList;
		mListAll = mList;
		filteredPeople = new ArrayList<AstrologerDetailBean>();
	}

	@Override
	public AstrologerDetailBean getItem(int position) {

		return mList.get(position);
	}

	@Override
	public int getCount() {
		//Log.e("COUNT ",""+mList.size());
		if(mList == null){
			return 0;
		}
		return mList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		TextView textView;
		CircularNetworkImageView ri_profile_img;
		LinearLayout mainlayout;
		try {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
				convertView = inflater.inflate(R.layout.search_row_layout, parent, false);
			}
			final AstrologerDetailBean astrologerDetailBean = mList.get(position);
			if(astrologerDetailBean != null) {
				TextView name = (TextView) convertView.findViewById(R.id.astrologer_name_txt);
				if(name != null) {
					name.setText(astrologerDetailBean.getName());
				}

				CircularNetworkImageView riProfileImg = (CircularNetworkImageView) convertView.findViewById(R.id.ri_profile_img);
				String astrologerProfileUrl = "";
				if(riProfileImg != null) {
					if (astrologerDetailBean.getImageFile() != null && astrologerDetailBean.getImageFile().length() > 0) {
						astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFile();
						riProfileImg.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
						Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(riProfileImg);
					}
				}

				mainlayout = (LinearLayout)convertView.findViewById(R.id.mainlayout);
				mainlayout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString("phoneNumber", astrologerDetailBean.getPhoneNumber());
						bundle.putString("urlText", astrologerDetailBean.getUrlText());
						CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_HOME_ASTRO_DETAIL_CLICK,
								CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
						Intent intent = new Intent(context, AstrologerDescriptionActivity.class);
						intent.putExtras(bundle);
						context.startActivity(intent);
						((Activity)context).finish();
						//Toast.makeText(context, "NAME "+ astrologerDetailBean.getName(),Toast.LENGTH_LONG).show();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public Filter getFilter() {

		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				if (charSequence == null || charSequence.toString().isEmpty()) {
					mList = mListAll;
				} else {
					String charString = charSequence.toString();
					ArrayList<AstrologerDetailBean> filteredList = new ArrayList<>();
					for (AstrologerDetailBean row : mListAll) {

						// name match condition. this might differ depending on your requirement
						// here we are looking for name or phone number match
						if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
							filteredList.add(row);
						}
					}
					mList = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = mList;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				if (((ArrayList<AstrologerDetailBean>) filterResults.values) != null &&
						((ArrayList<AstrologerDetailBean>) filterResults.values).size() > 0) {
					mList = (ArrayList<AstrologerDetailBean>) filterResults.values;

				}
				notifyDataSetChanged();
			}
		};
		//return nameFilter;
	}

	/*Filter nameFilter = new Filter() {

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			List<AstrologerDetailBean> filteredList = (List<AstrologerDetailBean>) results.values;

			if (results != null && results.count > 0) {
				clear();
				for (AstrologerDetailBean people : filteredList) {
					add(people);
				}
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			if (constraint != null) {
				filteredPeople.clear();
				for (AstrologerDetailBean people : mListAll) {
					if (people.getName().contains(constraint) || people.getName().startsWith(""+constraint)) {
						filteredPeople.add(people);
					}
				}
				filterResults.values = filteredPeople;
				filterResults.count = filteredPeople.size();
			}
			return filterResults;
		}
	};*/

}