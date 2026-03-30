package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActUserPlanDetails;
import com.ojassoft.astrosage.ui.act.AffliateAgreementActivity;
import com.ojassoft.astrosage.ui.act.AffliateHomeActivity;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.DhruvDashBoardActivity;
import com.ojassoft.astrosage.ui.act.ListingCreationActivity;
import com.ojassoft.astrosage.ui.act.MyAppointmentActivity;
import com.ojassoft.astrosage.ui.act.NewAppointmentActivity;
import com.ojassoft.astrosage.ui.act.TopupRechargeActivity;
import com.ojassoft.astrosage.ui.act.VartaReqJoinActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;

public class DhruvDashboardAdapter extends RecyclerView.Adapter<DhruvDashboardAdapter.MyViewHolder> {
    List<String> list;
    List<Integer> imageList;
    List<Integer> bgImageList;
    Context context;


    public DhruvDashboardAdapter(Context context, List<String> list, List<Integer> imageList, List<Integer> bgImageList) {
        this.list = list;
        this.imageList = imageList;
        this.context = context;
        this.bgImageList = bgImageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dhruv_dashboard_list_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {

        viewHolder.textView.setText(list.get(i));
        viewHolder.imageView.setImageResource(imageList.get(i));
        viewHolder.linearLayout.setBackground(context.getResources().getDrawable(bgImageList.get(i)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AffliateAgreementActivity.class);
                    switch (getLayoutPosition()) {
                        case 0:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_NEW_APPOINTMENT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            intent = new Intent(context, NewAppointmentActivity.class);
                            break;
                        case 1:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_MY_APPOINTMENT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            intent = new Intent(context, MyAppointmentActivity.class);
                            break;
                        case 2:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_FOOTER_DETAIL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            intent = new Intent(context, ActUserPlanDetails.class);
                            break;
                        case 3:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_VARTA_JOIN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            intent = new Intent(context, VartaReqJoinActivity.class);
                            break;
                        case 4:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_AFFILIATE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                            String affiliatePartnerId = CUtils.getStringData(context, "affiliatePartnerId", "");
                            if (TextUtils.isEmpty(affiliatePartnerId)) {
                                intent = new Intent(context, AffliateAgreementActivity.class);
                            } else {
                                intent = new Intent(context, AffliateHomeActivity.class);
                            }
                            break;
                        case 5:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_TOP_UP_RECHARGE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            intent = new Intent(context, TopupRechargeActivity.class);
                            break;
                        case 6:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_TOP_UP_RECHARGE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            //intent = new Intent(context, ListingCreationActivity.class);
                            intent = null;
                            if(context instanceof DhruvDashBoardActivity) {
                                ((DhruvDashBoardActivity) context).getDirectoryHome();
                            }
                            break;

                    }
                    if(intent != null)
                    ((BaseInputActivity) context).startActivity(intent);
                }
            });
        }
    }
}