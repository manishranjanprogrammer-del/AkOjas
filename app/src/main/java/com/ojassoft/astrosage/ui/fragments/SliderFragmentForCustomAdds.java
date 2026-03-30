package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on २८/२/१७.
 */
public class SliderFragmentForCustomAdds extends Fragment {


    private View view;
    private NetworkImageView imgslide;
    private CustomAddModel modal;
    private Bundle bundle;
    ViewPager viewPager;
    private ProgressBar progressBar;
    String screen_Name;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    // private  ThumbnailListener thumbnailListener;
    public SliderFragmentForCustomAdds() {

    }


    private int width = 0;
    String url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplicationContext())
                .getLanguageCode();
    }

    //http://img.youtube.com/vi/hpUBpQBs5fc/0.jpg

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     /*   if (view == null) {*/
        view = inflater.inflate(R.layout.custom_add_layout, container, false);
      /* } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }*/
        bundle = this.getArguments();
        if (bundle != null) {
            modal = (CustomAddModel) bundle.get("DATA");
            screen_Name= (String) bundle.get("SCREEN_NAME");

        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //com.google.analytics.tracking.android.//Log.e("Activitylog"+getActivity());

                if(screen_Name.equalsIgnoreCase("ActAstroShop")){
                    CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SCREEN_ADD, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SCREEN_ADD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    CUtils.createSession(getActivity(),"SAS");

                    //Log.e("screen_Name",screen_Name);
                }else{
                    CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SLOT_0_Add, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_0_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    CUtils.createSession(getActivity(),"S0");

                    //Log.e("screen_Name",screen_Name);
                }

                try {
                    String bannerUrl = modal.getImgthumbnailurl();
                    if ((bannerUrl.contains(CGlobalVariables.VARTA_ASTROSAGE) || bannerUrl.contains(CGlobalVariables.TALK_ASTROSAGE)) &&
                            bannerUrl.contains(CGlobalVariables.chat_with_astrologers)) {
                        //connect direct chat if user has free offer
                        String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(getActivity());
                        if (offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.popUpLoginFreeChatClicked = true;
                            //com.ojassoft.astrosage.varta.utils.CUtils.switchToConsultTab(FILTER_TYPE_CHAT, getActivity());
                            com.ojassoft.astrosage.varta.utils.CUtils.switchToConsultTab(FILTER_TYPE_CALL, getActivity());//redirect to AI list
                        } else {
                            CUtils.divertToScreen(getActivity(), bannerUrl, LANGUAGE_CODE);
                        }
                    } else {
                        CUtils.divertToScreen(getActivity(), bannerUrl, LANGUAGE_CODE);
                    }
                } catch (Exception e){
                    CUtils.divertToScreen(getActivity(),modal.getImgthumbnailurl(),LANGUAGE_CODE);
                }
            }
        });
        //  thumbnailListener = new ThumbnailListener();
        init(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void init(View view) {
        //http://img.youtube.com/vi/hpUBpQBs5fc/0.jpg
        // String url= CGlobalVariables.youTubeBaseUrl+modal.getVideo_url().trim()+"/0.jpg";
        imgslide = (NetworkImageView) view.findViewById(R.id.imgSlides);
        progressBar= (ProgressBar) view.findViewById(R.id.pBar);
        if (modal.getImgurl() != null && !modal.getImgurl().isEmpty()) {
            url = modal.getImgurl();
        }
       /* VolleySingleton.getInstance(getActivity()).getImageLoader().get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                imgslide.setImageBitmap(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });*/
        imgslide.setImageUrl(url, VolleySingleton.getInstance(getActivity()).getImageLoader());
        // progressBar.setVisibility(View.GONE);
      /*  if (getParentFragment() instanceof KundliModules_Frag) {
            ((KundliModules_Frag) getParentFragment()).indicator.setVisibility(View.VISIBLE);
        }*/

    }

}
