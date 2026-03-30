package com.ojassoft.astrosage.customadapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CloudPlanData;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.ui.fragments.PlatinumPlanFrag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;

public class PlanRecyclerViewAdapter extends RecyclerView.Adapter<PlanRecyclerViewAdapter.MyViewHolder> {

    Typeface robotoMedium;
    Typeface robotoRegular;
    Fragment fragment;
    Activity activity;
    private List<CloudPlanData> planDataList;

    public PlanRecyclerViewAdapter(Fragment fragment, Activity activity, List<CloudPlanData> moviesList) {
        this.activity = activity;
        this.fragment = fragment;
        this.planDataList = moviesList;
        robotoMedium = CUtils.getRobotoMedium(activity);
        robotoRegular = CUtils.getRobotoRegular(activity);
    }

    private static void downloadSamplePdf(Activity activity) {
        try {

            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = CGlobalVariables.brihatForFreePdfHoroscope;
            if (activity instanceof PurchasePlanHomeActivity) {
                int langCode = ((PurchasePlanHomeActivity) activity).LANGUAGE_CODE;
                if (langCode == CGlobalVariables.HINDI) {
                    url = CGlobalVariables.brihatForFreePdfHoroscopeHi;
                } else if (langCode == CGlobalVariables.TAMIL) {
                    url = CGlobalVariables.brihatForFreePdfHoroscopeTa;
                } else if (langCode == CGlobalVariables.BANGALI) {
                    url = CGlobalVariables.brihatForFreePdfHoroscopeBn;
                } else if (langCode == CGlobalVariables.MARATHI) {
                    url = CGlobalVariables.brihatForFreePdfHoroscopeMr;
                } else if (langCode == CGlobalVariables.TELUGU) {
                    url = CGlobalVariables.brihatForFreePdfHoroscopeTe;
                } else if (langCode == CGlobalVariables.KANNADA) {
                    url = CGlobalVariables.brihatForFreePdfHoroscopeKn;
                } else if (langCode == CGlobalVariables.GUJARATI) {
                    url = CGlobalVariables.brihatForFreePdfHoroscopeGu;
                } else if (langCode == CGlobalVariables.MALAYALAM) {
                    url = CGlobalVariables.brihatForFreePdfHoroscopeMl;
                }

            }

            i.setData(Uri.parse(url));
            activity.startActivity(i);
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ENGISH_SAMPLE_PDF_DOWNLOAD, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ENGISH_SAMPLE_PDF_DOWNLOAD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        } catch (Exception e) {
            // //Log.e("Download_pdf_error", e.getMessage());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cloud_plan_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CloudPlanData cloudPlanData = planDataList.get(position);
        holder.heading.setText(cloudPlanData.getHeading());
        //holder.desc.setText(cloudPlanData.getDesc());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.desc.setText(Html.fromHtml(cloudPlanData.getDesc(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.desc.setText(Html.fromHtml(cloudPlanData.getDesc()));
        }
        holder.desc.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        holder.imageView.setImageResource(cloudPlanData.getImageID());
        //holder.imgBackgroundLL.setBackgroundResource(cloudPlanData.getImageBackgroundID());
        holder.viewSample.setVisibility(View.GONE);
        if (fragment instanceof PlatinumPlanFrag) {
            // 1 for to make download sample visible
            if (position == 2) {
                holder.viewSample.setVisibility(View.VISIBLE);
            }

        }
        if (position == planDataList.size() - 1) {
            holder.sepratorLL.setVisibility(View.GONE);
        } else {
            holder.sepratorLL.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return planDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView heading, desc;
        public ImageView imageView;
        public LinearLayout sepratorLL;
        public LinearLayout imgBackgroundLL;
        public TextView viewSample;


        public MyViewHolder(View view) {
            super(view);
            heading = (TextView) view.findViewById(R.id.heading_tv);
            desc = (TextView) view.findViewById(R.id.desc_tv);
            imageView = (ImageView) view.findViewById(R.id.plan_iv);
            sepratorLL = (LinearLayout) view.findViewById(R.id.seprator);
            imgBackgroundLL = (LinearLayout) view.findViewById(R.id.img_background_bg);
            imgBackgroundLL.setBackgroundResource(R.drawable.basic_solid_circle);
            viewSample = (TextView) view.findViewById(R.id.view_sample);

            heading.setTypeface(robotoMedium);
            desc.setTypeface(robotoRegular);
            viewSample.setTypeface(robotoRegular);
            viewSample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadSamplePdf(activity);
                }
            });

        }
    }
}