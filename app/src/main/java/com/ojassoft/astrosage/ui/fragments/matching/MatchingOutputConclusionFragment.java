package com.ojassoft.astrosage.ui.fragments.matching;

import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class MatchingOutputConclusionFragment extends Fragment {
    // public Typeface typeface = Typeface.DEFAULT;

    private ArrayList<AdData> adList;
    private NetworkImageView bottomoAdImage;
    AdData bottomAdData;
    private String IsShowBanner1 = "False";

    public MatchingOutputConclusionFragment() {
        setRetainInstance(true);
    }

    int LANGUAGE_CODE;
    Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        // typeface = CUtils.getUserSelectedLanguageFontType(activity, LANGUAGE_CODE);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lay_matching_conclusion, container, false);
        initValues(view);
        return view;
    }

    private void getData() {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                bottomAdData = CUtils.getSlotData(adList, "10");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initValues(View view) {
        try {
            if (activity == null) {
                return;
            }
            TextView tvMatchingConclusionHeading = (TextView) view.findViewById(R.id.tvMatchingConclusionHeading);
            TextView tvMatchingConclusionPoints = (TextView) view.findViewById(R.id.tvMatchingConclusionPoints);
            TextView tvMatchingConclusionBoy2 = (TextView) view.findViewById(R.id.tvMatchingConclusionBoy2);
            TextView tvMatchingConclusionGirl2 = (TextView) view.findViewById(R.id.tvMatchingConclusionGirl2);
            TextView tvMatchingConclusionBoy3 = (TextView) view.findViewById(R.id.tvMatchingConclusionBoy3);
            TextView tvMatchingConclusionGirl3 = (TextView) view.findViewById(R.id.tvMatchingConclusionGirl3);
            TextView tvMatchingConclusionResult = (TextView) view.findViewById(R.id.tvMatchingConclusionResult);
            TextView tvMatchingConclusionResultValues = (TextView) view.findViewById(R.id.tvMatchingConclusionResultValues);
            Button tvSugMarriageDate = (Button) view.findViewById(R.id.tvSugMarriageDate);

            Button sharePdf = (Button) view.findViewById(R.id.share_pdf_btn);

            bottomoAdImage = (NetworkImageView) view.findViewById(R.id.adImage);

            tvMatchingConclusionHeading.setText(getResources().getStringArray(R.array.matching_detail_heading_list)[OutputMatchingMasterActivity.MATCHING_CONCLUSION]);
//		tvMatchingConclusionPoints.setTypeface(typeface);
            // tvMatchingConclusionBoy3.setTypeface(((OutputMatchingMasterActivity)activity).regularTypeface);
            // tvMatchingConclusionGirl3.setTypeface(((OutputMatchingMasterActivity)activity).regularTypeface);
            tvMatchingConclusionResult.setTypeface(((OutputMatchingMasterActivity) activity).regularTypeface);

            try {
                tvMatchingConclusionPoints.setText(getPoints());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //tvMatchingConclusionBoy2.setText(getBoyConclusion2());
            tvMatchingConclusionBoy2.setTypeface(((OutputMatchingMasterActivity) activity).regularTypeface);
            //tvMatchingConclusionBoy3.setText(" " + getBoyConclusion3());
            //tvMatchingConclusionBoy3.setTypeface(((OutputMatchingMasterActivity) activity).regularTypeface);

            //tvMatchingConclusionGirl2.setText(getGirlConclusion2());
            tvMatchingConclusionGirl2.setTypeface(((OutputMatchingMasterActivity) activity).regularTypeface);
            //tvMatchingConclusionGirl3.setText(" " + getGirlConclusion3());
            //tvMatchingConclusionGirl3.setTypeface(((OutputMatchingMasterActivity) activity).regularTypeface);

            tvMatchingConclusionBoy2.setText(getBoyConclusion2() + "" + getBoyConclusion3()
                    + " " + getResources().getString(R.string.and) + " " +
                    getGirlConclusion2() + "" + getGirlConclusion3());
            tvMatchingConclusionGirl2.setVisibility(View.GONE);
            tvMatchingConclusionBoy3.setVisibility(View.GONE);
            tvMatchingConclusionGirl3.setVisibility(View.GONE);

            tvMatchingConclusionResult.setText(getResources().getString(R.string.matching_conclusion_north));
            tvMatchingConclusionResultValues.setText("   " + CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getConclusion());
            tvMatchingConclusionHeading.setTypeface(((OutputMatchingMasterActivity) activity).regularTypeface, Typeface.BOLD);
            sharePdf.setTypeface(((OutputMatchingMasterActivity) activity).mediumTypeface);
            bottomoAdImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CUtils.googleAnalyticSendWitPlayServie(activity,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SLOT_10_Add, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_10_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    CUtils.createSession(activity, "S10");

                    CustomAddModel modal = bottomAdData.getImageObj().get(0);

                /*if(modal.getImgthumbnailurl().contains(CGlobalVariables.buy_astrosage_url)){
                    CUtils.getUrlLink(modal.getImgthumbnailurl(), activity,LANGUAGE_CODE, 0);
                }
                else {
                    if (modal.getImgthumbnailurl() != null && !modal.getImgthumbnailurl().isEmpty()) {
                        Uri uri = Uri.parse(modal.getImgthumbnailurl());
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(uri);
                        startActivity(i);
                    }
                }*/
                    CUtils.divertToScreen(activity, modal.getImgthumbnailurl(), LANGUAGE_CODE);
                }
            });


            if (bottomAdData != null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
                setBottomAd(bottomAdData);
            }

            sharePdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getActivity() instanceof OutputMatchingMasterActivity) {
                        ((OutputMatchingMasterActivity) getActivity()).sharePDF(true);
                    }
                    CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SHARE_BOTTOM_BUTTON, null);
                    //CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SHARE_BOTTOM_BUTTON, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,GOOGLE_ANALYTIC_DOWNLOAD_PDF,CGlobalVariables.GOOGLE_ANALYTIC_SHARE_BOTTOM_BUTTON);
                }
            });

            tvSugMarriageDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, InputPanchangActivity.class);
                    intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_MUHURAT);
                    intent.putExtra("date", Calendar.getInstance());
                    intent.putExtra("place", "");
                    activity.startActivity(intent);

                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_MATCHING_MUHURAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }
            });
        }catch (Exception e){
           //
        }


    }

    public void setBottomAd(AdData bottomData) {
        getData();
        if (bottomData != null) {
            IsShowBanner1 = bottomData.getIsShowBanner();
            IsShowBanner1 = IsShowBanner1 == null ? "" : IsShowBanner1;

        }
        if (bottomAdData == null || bottomData.getImageObj() == null || bottomData.getImageObj().size() <= 0 || IsShowBanner1.equalsIgnoreCase("False")) {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }
        } else {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.VISIBLE);
                Log.e("URL_CHECK", "setBottomAd: " + bottomData.getImageObj().get(0).getImgurl());
                int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                String imageURL = bottomData.getImageObj().get(0).getImgurl();
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    bottomoAdImage.setImageUrl(imageURL.replace(".jpg", "-dark.jpg"), VolleySingleton.getInstance(activity).getImageLoader());
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    bottomoAdImage.setImageUrl(imageURL, VolleySingleton.getInstance(activity).getImageLoader());
                }

            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }
        }

    }

    private String getResult() {
        String strConclusion = "";
        strConclusion = "   " + CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getConclusion();
        return strConclusion;
    }

    private String getBoyConclusion1() {
        String boyConclusion = "";

        boyConclusion += getResources().getString(R.string.matching_result_north4) + " ";
        return boyConclusion;
    }

    private String getBoyConclusion2() {
        String boyConclusion = "";
        try {
            if (Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getBoyMangalDosha().trim()) == Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getGirlMangalDosha().trim())) {
                boyConclusion += CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().getName();
            } else {
                boyConclusion += CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().getName() + " ";
            }
        } catch (Exception e) {

        }

        return boyConclusion;
    }

    private String getBoyConclusion3() {
        String boyConclusion = "";

        try {
            /*if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                boyConclusion += "\' " + getResources().getStringArray(R.array.matching_mangal_dosh_list)[Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getBoyMangalDosha().trim())].replace(".", "") + " " + getResources().getString(R.string.matching_result_north5).replace("\'", ".");
            } else {
                boyConclusion += getResources().getString(R.string.matching_result_north5) + " " + getResources().getStringArray(R.array.matching_mangal_dosh_list)[Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getBoyMangalDosha().trim())];
            }*/

            if (Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getBoyMangalDosha().trim()) == Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getGirlMangalDosha().trim())) {
                boyConclusion.trim();
            } else {
                boyConclusion += getResources().getString(R.string.matching_result_north5) + " " + getResources().getStringArray(R.array.matching_mangal_dosh_list)[Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getBoyMangalDosha().trim())];
            }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception ex) {

        }

        return boyConclusion;
    }

    private String getGirlConclusion1() {
        String girlConclusion = "";
        girlConclusion += getResources().getString(R.string.matching_result_north7) + " ";
        return girlConclusion;
    }

    private String getGirlConclusion2() {
        String girlConclusion = "";
        girlConclusion += CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail().getName() + " ";

        return girlConclusion;
    }

    private String getGirlConclusion3() {
        String girlConclusion = "";
        try {
            /*if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                girlConclusion += "\' " + getResources().getStringArray(R.array.matching_mangal_dosh_list)[Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getGirlMangalDosha().trim())].replace(".", "") + " " + getResources().getString(R.string.matching_result_north5).replace("\'", ".");
            } else {
                girlConclusion += getResources().getString(R.string.matching_result_north5) + " " + getResources().getStringArray(R.array.matching_mangal_dosh_list)[Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getGirlMangalDosha().trim())];
            }*/

            girlConclusion += getResources().getString(R.string.matching_result_north5) + " " + getResources().getStringArray(R.array.matching_mangal_dosh_list)[Integer.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getGirlMangalDosha().trim())]
                    + getResources().getString(R.string.dot);

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return girlConclusion;
    }

    private String getPoints() {
        String _result = "";
        /*if(LANGUAGE_CODE == CGlobalVariables.MM_ENGLISH)
        {
		_result += getResources().getString(R.string.matching_result_north1)+ CGlobalVariables.eol+"  "+getResources().getString(R.string.matching_result_north2)+" "
				+ String.valueOf((int)CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getTotalPointsObtained())+" "+ getResources().getString(R.string.matching_result_north3);
				
		}
		if(LANGUAGE_CODE == CGlobalVariables.MM_HINDI)
		{
		_result += getResources().getString(R.string.matching_result_north1)+ CGlobalVariables.eol+"  "+getResources().getString(R.string.matching_result_north2)+" "
				+ String.valueOf((int)CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getTotalPointsObtained())+" "+ getResources().getString(R.string.matching_result_north3);
				
		}*/
        _result = String.valueOf(CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth().getTotalPointsObtained());
        return _result;
    }

}
