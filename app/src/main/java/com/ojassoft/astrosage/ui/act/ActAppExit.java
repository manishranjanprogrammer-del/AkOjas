package com.ojassoft.astrosage.ui.act;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

//import com.google.analytics.tracking.android.EasyTracker;

public class ActAppExit extends BaseInputActivity {
    //boolean isExitApp = false;
   // AdView adViewOnExit = null;
    LinearLayout advLayout;
    TextView textViewHeading;
    Button yes, no;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    //public Typeface typeface;
    private NetworkImageView networkImageView;

    public ActAppExit() {
        super(R.string.app_name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lay_act_app_exit);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        //typeface = CUtils.getRobotoFont(this, LANGUAGE_CODE, CGlobalVariables.regular);

        advLayout = (LinearLayout) findViewById(R.id.advLayout);
        networkImageView = (NetworkImageView) findViewById(R.id.imageView);

        textViewHeading = (TextView) findViewById(R.id.textViewHeading);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        yes.setTypeface(regularTypeface);
        no.setTypeface(regularTypeface);

        textViewHeading.setTypeface(regularTypeface);
        //initializeAdViewOnExit();
        if (LANGUAGE_CODE == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                no.setAllCaps(true);
                yes.setAllCaps(true);
            }
        }

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCancel(v);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoExit(v);
            }
        });

        try {
            if (false) {
                advLayout.setVisibility(View.VISIBLE);
                networkImageView.setVisibility(View.GONE);
            } else {
                advLayout.setVisibility(View.GONE);
                networkImageView.setVisibility(View.VISIBLE);
                final CustomAddModel customAddModel = getBannerImageUrl();
                // String url = "https://www.astrosage.com/ads/images/big-horoscope-300.jpg";
                String url = "";
                if (customAddModel != null) {
                    url = customAddModel.getImgurl();
                }

                if (!TextUtils.isEmpty(url)) {
                    networkImageView.setImageUrl(url, VolleySingleton.getInstance(this).getImageLoader());
                }
                networkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CUtils.googleAnalyticSendWitPlayServie(ActAppExit.this,
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_28_Add, null);
                        CUtils.createSession(ActAppExit.this, "SEXIT");

                        if (customAddModel != null) {
                            CUtils.divertToScreen(ActAppExit.this, customAddModel.getImgthumbnailurl(), LANGUAGE_CODE);
                        }


                    }
                });


                ///String url = CUtils.getStringData(ActAppExit.this, CGlobalVariables.ExitScreenCustomImageUrl, "");
               /*
                if (url != null && !url.equalsIgnoreCase("null") && !url.equals("")) {
                    networkImageView.setImageUrl(url, VolleySingleton.getInstance(this).getImageLoader());

                    final String listenerUrl = CUtils.getStringData(ActAppExit.this, CGlobalVariables.ExitScreenCustomImageClickListener, "");
                    if (listenerUrl != null && !listenerUrl.equalsIgnoreCase("null") && !listenerUrl.equals("")) {
                        networkImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setNetworkImageClickListner(listenerUrl);
                            }
                        });
                    }
                }*/

                ActAppModule.isExitBannerAdShow = true;
            }
        } catch (Exception ex) {
            //Log.e("",ex.getMessage().toString());
            advLayout.setVisibility(View.VISIBLE);
            networkImageView.setVisibility(View.GONE);
        }
        //setImageBanner();

    }

    private void setNetworkImageClickListner(String url) {

        CUtils.divertToScreen(ActAppExit.this, url, LANGUAGE_CODE);
       /* if(url.contains(CGlobalVariables.buy_astrosage_url)){
            CUtils.getUrlLink(url,ActAppExit.this,LANGUAGE_CODE,0);
        }else if(url.contains(CGlobalVariables.astrosage_offers_url)){

        }else{
            if(url !=null && !url.equals("")) {
                Uri uri = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(uri);
                startActivity(i);
            }
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActAppModule.isExitBannerAdShow = true;
        //EasyTracker.getInstance().activityStop(this);
    }

    /*private void setAdvertisemnet(LinearLayout view){
        AdView adViewOnExit = new AdView(getApplicationContext());
        adViewOnExit.setAdUnitId(CGlobalVariables.APP_EXIT_AD_ID);
        adViewOnExit.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adViewOnExit.loadAd(new AdRequest.Builder().build());

        view.addView(adViewOnExit);
    }*/

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
       /* int purchasePlanId = CUtils.getUserPurchasedPlanFromPreference(ActAppExit.this);

        if (CUtils.isUserLogedIn(ActAppExit.this)
                && purchasePlanId != CGlobalVariables.BASIC_PLAN_ID) {
            advLayout.setVisibility(View.GONE);
        } else {
            removeViewFromAdvLayout();
            advLayout.addView(AstrosageKundliApplication.getAdvertisementOnApplicationExit());
        }*/
        /*
        try {
            if (false) {
                removeViewFromAdvLayout();
                advLayout.addView(adViewOnExit);
            }
        } catch (Exception e) {
        }*/
        //  setAdvertisemnet(advLayout);
    }

    private void removeViewFromAdvLayout() {
        try {
            advLayout.removeAllViews();

        } catch (Exception e) {
            //Log.i("TAG-EXIT Screen",e.getMessage().toString());
        }
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (false) {
            removeViewFromAdvLayout();
        }

    }

    public void gotoExit(View view) {
        setResult(RESULT_OK);
        ActAppExit.this.finish();
    }

    public void gotoCancel(View view) {
        ActAppExit.this.finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }


    private CustomAddModel getBannerImageUrl() {
        CustomAddModel customAddModel = null;
        String result = CUtils.getStringData(ActAppExit.this, "CUSTOMADDS", "");
        String imageUrl = "";

        if (!TextUtils.isEmpty(result)) {
            ArrayList<AdData> sliderList = parseData(result);

            if (sliderList != null && sliderList.size() > 0) {
                for (int i = 0; i < sliderList.size(); i++) {
                    if (Integer.parseInt(sliderList.get(i).getSlot()) == 28) {
                        customAddModel = sliderList.get(i).getImageObj().get(0);
                        break;
                    }
                }
            }
        }
        return customAddModel;
       /* if (customAddModel != null) {
            imageUrl = customAddModel.getImgurl();
        }


        if (!TextUtils.isEmpty(imageUrl)) {
            bannerImage.setImageUrl(imageUrl, VolleySingleton.getInstance(AstrosageKundliApplication.getAppContext()).getImageLoader());
        }
        bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //com.google.analytics.tracking.android.//Log.e("Activitylog"+getActivity());


                CUtils.googleAnalyticSendWitPlayServie(ActAppExit.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_28_Add, null);
                CUtils.createSession(ActAppExit.this, "SEXIT");


                CUtils.divertToScreen(ActAppExit.this, customAddModel.getImgthumbnailurl(), LANGUAGE_CODE);


            }
        });
*/

    }

    /*Parse Recieved Gson Data*/
    public ArrayList<AdData> parseData(String response) {
        ArrayList<AdData> adList = null;
        try {

            adList = new Gson().fromJson(response, new TypeToken<ArrayList<AdData>>() {
            }.getType());


        } catch (Exception e) {
            Log.i("Exception generate", e.getMessage());
        }
        return adList;

    }

    /*
    public void initializeAdViewOnExit() {
        //LOADING ON APPLICATION EXIT ADV
        if (adViewOnExit == null) {
            adViewOnExit = new AdView(this);
            String APP_EXIT_AD_ID = CUtils.getStringData(this, CGlobalVariables.ExitScreenBannerAdId, CGlobalVariables.APP_EXIT_AD_ID);
            adViewOnExit.setAdUnitId(APP_EXIT_AD_ID);
            adViewOnExit.setAdSize(AdSize.MEDIUM_RECTANGLE);

            //  FOR TESTING
           // AdRequest request=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
           // adViewOnExit.loadAd(request);

            adViewOnExit.loadAd(new AdRequest.Builder().build());
            adViewOnExit.setAdListener(new AdListener());
        }
    }*/

}
