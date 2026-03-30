package com.ojassoft.astrosage.customadapters;

import android.app.Activity;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.HomeMenuItemInformation;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.varta.ui.activity.ProfileForChat;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Amit Rautela on 24/2/16.
 * This Class is used to for Adapter
 */

public class HomeNavigationAdapter extends RecyclerView.Adapter<HomeNavigationAdapter.MyViewHolder> {

    // private Activity context;
    private LayoutInflater inflater;
    private List<HomeMenuItemInformation> data = Collections.emptyList();
    //IHomeNavigationDrawerFragment iHomeNavigationDrawerFragment;
    HomeNavigationDrawerFragment homeNavigationDrawerFragment;
    Typeface typeface;

    public HomeNavigationAdapter(Activity context, HomeNavigationDrawerFragment homeNavigationDrawerFragment, List<HomeMenuItemInformation> data, Typeface typefaces) {
        //this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.homeNavigationDrawerFragment = homeNavigationDrawerFragment;
        //iHomeNavigationDrawerFragment = (IHomeNavigationDrawerFragment) context;
        this.typeface = typefaces;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_recyclerview_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomeMenuItemInformation current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageDrawable(current.icon);
        if (current.isSeparator) {
            holder.viewSeparator.setVisibility(View.VISIBLE);
        } else {
            holder.viewSeparator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        LinearLayout viewSeparator;
        RelativeLayout rlParent;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            FontUtils.changeFont(itemView.getContext(), title, CGlobalVariables.FONTS_POPPINS_REGULAR);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            viewSeparator = (LinearLayout) itemView.findViewById(R.id.viewSeparator);
            rlParent = (RelativeLayout) itemView.findViewById(R.id.llParent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();

                    //homeNavigationDrawerFragment.switchToModule(position);
                    int indexValue = data.get(position).index;
                    homeNavigationDrawerFragment.switchContent(indexValue);

                }
            });

        }
    }
}
