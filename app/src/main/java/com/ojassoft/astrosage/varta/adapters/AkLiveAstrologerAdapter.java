package com.ojassoft.astrosage.varta.adapters;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.removeAcharyaTarot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class AkLiveAstrologerAdapter extends RecyclerView.Adapter<AkLiveAstrologerAdapter.MyViewHolder> {

    Context context;
    ArrayList<LiveAstrologerModel> liveAstrologerModelArrayList;
    public boolean layoutTypeSmall;
    private static final int HOME_ASTRO_LIST_LIMIT = 20;

    public AkLiveAstrologerAdapter(Context context, ArrayList<LiveAstrologerModel> liveAstrologerModels) {
        this.context = context;
        this.liveAstrologerModelArrayList = liveAstrologerModels;
        layoutTypeSmall = liveAstrologerModelArrayList.size() > 4;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutTypeSmall ? R.layout.live_astrologer_list_item : R.layout.live_astrologer_list_item_large, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            if (liveAstrologerModelArrayList != null && liveAstrologerModelArrayList.size() > position) {
                LiveAstrologerModel liveAstrologerModel = liveAstrologerModelArrayList.get(position);
                if (liveAstrologerModel == null) return;
                String astroName = liveAstrologerModel.getName();
                astroName = removeAcharyaTarot( astroName);
                if(astroName != null){
                    astroName = astroName.trim();
                }
                holder.txtAstroName.setText(astroName);
                holder.txtAstroName.setTypeface(CUtils.getRobotoFont(context, LANGUAGE_CODE, CGlobalVariables.medium));
//                if (!layoutTypeSmall) {
//                    LinearLayout.LayoutParams layoutParams;
//                    if (liveAstrologerModelArrayList.size() == 1) {
//                        layoutParams = new LinearLayout.LayoutParams((int) (CGlobalVariables.width) - 40, (int) (CGlobalVariables.height * 0.175));
//                        layoutParams.setMargins(20, 0, 20, 0);
//                    } else {
//                        layoutParams = new LinearLayout.LayoutParams((int) (CGlobalVariables.width * 0.8), (int) (CGlobalVariables.height * 0.175));
//                        if (position == liveAstrologerModelArrayList.size() - 1) {
//                            layoutParams.setMargins(20, 0, 20, 0);
//                        } else {
//                            layoutParams.setMargins(20, 0, 0, 0);
//                        }
//                    }
//
//                    holder.mainlayoutFr.setLayoutParams(layoutParams);
//                }
//                Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.blinking_animation);
//                holder.onlineImage.startAnimation(startAnimation);
                //holder.txtAstroName.setText(CUtils.toTitleCases(liveAstrologerModel.getExpertise()));
                String astrologerProfileUrl = "";
                if (liveAstrologerModel.getProfileImgUrl() != null && liveAstrologerModel.getProfileImgUrl().length() > 0) {
                    astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + liveAstrologerModel.getProfileImgUrl();
                    //holder.roundImage.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
                    //Log.e("astrologerProfileUrl", "astrologerProfileUrl="+astrologerProfileUrl);
                    Glide.with(holder.txtAstroImage).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.txtAstroImage);
                }
                holder.mainlayoutFr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (context instanceof ActAppModule) {
                                CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_VARTA_HOME_LIVE_JOIN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                com.ojassoft.astrosage.utils.CUtils.createSession(context, com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_HOME_LIVE_JOIN_PARTNER_ID);
                                ((ActAppModule) context).checkPermissions(liveAstrologerModel);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (liveAstrologerModelArrayList != null) {
            count = Math.min(liveAstrologerModelArrayList.size(), HOME_ASTRO_LIST_LIMIT);
        }

        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtAstroName;
        ImageView txtAstroImage;
        ConstraintLayout mainlayoutFr;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAstroImage = itemView.findViewById(R.id.txtAstroImage);
            txtAstroName = itemView.findViewById(R.id.txtAstroName);
            mainlayoutFr = itemView.findViewById(R.id.mainlayoutFr);
//            FontUtils.changeFont(context, expertiseNameTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        }
    }
}