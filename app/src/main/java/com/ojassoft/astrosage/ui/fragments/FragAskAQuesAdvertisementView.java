package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.act.ActAskQuestion;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

/**
 * Created by ojas on 27/10/16.
 */
public class FragAskAQuesAdvertisementView extends Fragment {

    TextView textquestiondes, tvWelcomeUser, textDiscountPlan, msgForBasicPlanText, msgForBasicPlanPrice, unlockPlanText;
    Button buy_now, btnRentry;
    private Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    // private String cacheResult;
    private ServicelistModal itemdetails;
    private ProgressBar progressBar;
    View rootView;
    private boolean isVisibleToUser = false;
    Activity activity;
    //GetData getDataAsync;
    private BroadcastReceiver receiver;
    LinearLayout basicPlanUserLayout;
    private RequestQueue queue;

    public static FragAskAQuesAdvertisementView newInstance() {
        FragAskAQuesAdvertisementView appViewFragment = new FragAskAQuesAdvertisementView();
        return appViewFragment;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if (context instanceof OutputMasterActivity)
            activity = (OutputMasterActivity) context;
        else
            activity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*if (getDataAsync != null && getDataAsync.getStatus() == AsyncTask.Status.RUNNING) {
            getDataAsync.cancel(true);
        }*/
        activity = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = VolleySingleton.getInstance(activity).getRequestQueue();
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.regular);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.lay_frag_ask_a_ques_adverisement_view,
                    container, false);
        }

        setLayRef(rootView);
        initView();
        /*receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String referdata = intent.getStringExtra(AskAQueDataUpdateService.BROAD_RESULT);

                //com.google.analytics.tracking.android.//Log.e("Data trecived on" + referdata);
                if (referdata != null && !referdata.isEmpty()) {
                    parseGsonData(referdata);

                }
            }
        };*/

        return rootView;
    }

    private void setLayRef(View rootView) {

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);


        textquestiondes = (TextView) rootView.findViewById(R.id.textquestiondes);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            textquestiondes.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        buy_now = (Button) rootView.findViewById(R.id.buy_now);
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.isConnectedWithInternet(getActivity());
                {
                    CUtils.createSession(getActivity(), "SAQKU");

                }

                sendToAskQuestion();
            }
        });
        btnRentry = (Button) rootView.findViewById(R.id.btnRentry);
        btnRentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initView();
            }
        });

        tvWelcomeUser = (TextView) rootView.findViewById(R.id.tvWelcomeUser);

        textDiscountPlan = (TextView) rootView.findViewById(R.id.text_discount_plan);
        msgForBasicPlanText = (TextView) rootView.findViewById(R.id.msg_for_basic_plan_text);
        msgForBasicPlanPrice = (TextView) rootView.findViewById(R.id.msg_for_basic_plan_price);
        unlockPlanText = (TextView) rootView.findViewById(R.id.unlock_plan_text);
        basicPlanUserLayout = (LinearLayout) rootView.findViewById(R.id.basic_plan_user_layout);

        basicPlanUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.gotoProductPlanListUpdated(getActivity(),
                        LANGUAGE_CODE, BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV,"frag_ask_a_ques_advertisement_view");
            }
        });
    }

    private void sendToAskQuestion() {

        CUtils.googleAnalyticSendWitPlayServie(activity,
                CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                CGlobalVariables.GOOGLE_ANALYTIC_ASTROSAHE_ASKAQUESTION, null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSAHE_ASKAQUESTION, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");


        BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
        int planId = 1;

        planId = CUtils.getUserPurchasedPlanFromPreference(getActivity());

        Intent intent = new Intent(activity, ActAskQuestion.class);
        intent.putExtra(CGlobalVariables.DataComingFromAskAQuesAdvertisementView, true);
        intent.putExtra(CGlobalVariables.ask_A_Question_Data, CGlobalVariables.ask_A_Question_Android);

        if (planId == CGlobalVariables.SILVER_PLAN_ID ||
                planId == CGlobalVariables.SILVER_PLAN_ID_5 ||
                planId == CGlobalVariables.SILVER_PLAN_ID_4|
                planId == CGlobalVariables.GOLD_PLAN_ID ||
                planId == CGlobalVariables.GOLD_PLAN_ID_7||
                planId == CGlobalVariables.GOLD_PLAN_ID_6 ||
                planId == CGlobalVariables.PLATINUM_PLAN_ID ||
                planId == CGlobalVariables.PLATINUM_PLAN_ID_9 ||
                planId == CGlobalVariables.PLATINUM_PLAN_ID_10  ||
                planId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
            intent.putExtra(CGlobalVariables.IS_USER_HAS_PLAN, true);
        } else {
            intent.putExtra(CGlobalVariables.IS_USER_HAS_PLAN, false);
        }
        intent.putExtra("BeanHoroPersonalInfo", beanHoroPersonalInfo);
        activity.startActivity(intent);
    }

    private void initView() {

        if (!CUtils.isConnectedWithInternet(activity)) {
            MyCustomToast mct = new MyCustomToast(activity, activity
                    .getLayoutInflater(), activity, typeface);
            mct.show(getResources().getString(R.string.no_internet));
            textquestiondes.setText(getResources().getString(R.string.no_internet));
            textquestiondes.setTypeface(typeface);
            btnRentry.setVisibility(View.VISIBLE);
            buy_now.setVisibility(View.GONE);
        } else {

            btnRentry.setVisibility(View.GONE);
            buy_now.setVisibility(View.VISIBLE);
            String url = CGlobalVariables.astroShopServiceAskaQuestion + "service=" + CGlobalVariables.ask_A_Question_Android + "lang=" + String.valueOf(LANGUAGE_CODE);
            checkCachedData(url);
            /*String cacheResult = CUtils.getStringData(activity, CGlobalVariables.ask_A_Question_Android + String.valueOf(LANGUAGE_CODE), "");
            if (cacheResult != null && !cacheResult.equals("")) {
                parseGsonData(cacheResult);
                Intent i = new Intent(activity, AskAQueDataUpdateService.class);
                i.putExtra(CGlobalVariables.ask_A_Question_Data, CGlobalVariables.ask_A_Question_Android);
                activity.startService(i);
            } else {
                getDataAsync = new GetData();
                getDataAsync.execute();
            }*/

            // }

            //  loadAstroShopData();
        }
    }

    private void setVisibility() {
        if (progressBar.isShown())
            progressBar.setVisibility(View.GONE);
    }

    private void parseGsonData(String saveData) {

        try {
            List<ServicelistModal> data;
            Gson gson = new Gson();
            data = gson.fromJson(saveData, new TypeToken<ArrayList<ServicelistModal>>() {
            }.getType());
            itemdetails = data.get(0);
            if (itemdetails != null && itemdetails.getServiceId() != null) {
                setViewItem();
            }
        } catch (Exception ex) {
            //
        }
    }

    private void setViewItem() {
        try {

            int planId = 1;
            planId = CUtils.getUserPurchasedPlanFromPreference(getActivity());

            if (itemdetails != null && activity != null) {
                //textrs.setText(itemdetails.getTitle() + ": " + getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2));
                //  tvWelcomeUser.setText("Hi "+ CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getName()+",");
                textquestiondes.setText(itemdetails.getSmallDesc());
                String buy_now_text = "";


                if (planId == CGlobalVariables.BASIC_PLAN_ID /*|| planId == CGlobalVariables.SILVER_PLAN_ID_4 ||
                        planId == CGlobalVariables.GOLD_PLAN_ID_6*/) {
                    basicPlanUserLayout.setVisibility(View.VISIBLE);
                    msgForBasicPlanText.setText(getResources().getString(R.string.basic_user_text_dhruv));
                    msgForBasicPlanPrice.setText(getResources().getString(R.string.astroshop_dollar_sign) +
                            roundFunction(Double.parseDouble(itemdetails.getNonCloudPriceInDollor()), 2) +
                            " / " + "\n" + getResources().getString(R.string.astroshop_rupees_sign) +
                            " " + roundFunction(Double.parseDouble(itemdetails.getNonCloudPriceInRS()), 2));
                    buy_now_text = itemdetails.getTitle() + ": " + activity.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) + " / " + activity.getResources().getString(R.string.astroshop_rupees_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2);

                } else {
                    basicPlanUserLayout.setVisibility(View.GONE);
                    CUtils.showServiceProductDiscountedText(getActivity(), textDiscountPlan,
                            itemdetails.getMessageOfCloudPlanText1(), itemdetails.getMessageOfCloudPlanText2(),
                            CGlobalVariables.FROM_SERVICE_TEXT);
                    buy_now_text = itemdetails.getTitle() + ": " + activity.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInDollorBeforeCloudPlanDiscount()), 2) + " / " + activity.getResources().getString(R.string.astroshop_rupees_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInRSBeforeCloudPlanDiscount()), 2);

                }

                buy_now.setText(buy_now_text);
            }
        } catch (Exception ex) {
            //Log.e("", ex.getMessage().toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

      /*  LocalBroadcastManager.getInstance(activity).registerReceiver((receiver),
                new IntentFilter(AskAQueDataUpdateService.BROAD_ACTION));
*/
    }

    @Override
    public void onStop() {
        super.onStop();
        //LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver);
    }

   /* private class GetData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = "";
            InputStream is = null;

            try {


                HttpClient hc = CUtils.getNewHttpClient();
                HttpPost postMethod = new HttpPost(CGlobalVariables.astroShopServiceAskaQuestion);

                postMethod.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Askquestion(
                        String.valueOf(LANGUAGE_CODE), CUtils.getApplicationSignatureHashCode(getActivity())), HTTP.UTF_8));
                HttpResponse httpResponse = null;

                httpResponse = hc.execute(postMethod);

                HttpEntity httpEntity = httpResponse.getEntity();

                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                System.out.println("Responec=====" + result);
                if (result != null && !result.isEmpty()) {
                    // CUtils.saveStringData(activity, "ASK" + String.valueOf(LANGUAGE_CODE), result);
                    return result;
                }


            } catch (Exception ex) {
                //Log.i("Tag", "1 - " + ex.getMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                parseGsonData(result);
                if (activity != null) {
                    CUtils.saveStringData(activity, CGlobalVariables.ask_A_Question_Android + String.valueOf(LANGUAGE_CODE), result);
                }
            }
            setVisibility();

        }


        private List<NameValuePair> getNameValuePairs_Askquestion(
                String languagecode, String key) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("servicetype", CUtils.getAskAQuestionServiceTypeCodeFromPage(CGlobalVariables.ask_A_Question_Android)));
            nameValuePairs.add(new BasicNameValuePair("languageCode", languagecode));
            nameValuePairs.add(new BasicNameValuePair("Key", key));

            nameValuePairs.add(new BasicNameValuePair(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(getActivity()))));
            nameValuePairs.add(new BasicNameValuePair("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(getActivity()))));
            //nameValuePairs.add(new BasicNameValuePair("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST));
            //nameValuePairs.add(new BasicNameValuePair("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST));

            return nameValuePairs;

        }

    }*/

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            setViewItem();
        }
    }

    /**
     * Check and load cache data
     */
    private void checkCachedData(String url) {
        Cache cache = VolleySingleton.getInstance(activity).getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1-"+url);
        // cache data
        try {
            if (entry != null) {
                String saveData = new String(entry.data, "UTF-8");
                if (!TextUtils.isEmpty(saveData)) {
                    parseGsonData(saveData);
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!CUtils.isConnectedWithInternet(activity)) {
            MyCustomToast mct = new MyCustomToast(activity, activity
                    .getLayoutInflater(), activity, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            getAskQuestionDesc(url);
        }
    }


    /**
     * get Ask a question desc data
     */
    public void getAskQuestionDesc(String url) {
        showProgressBar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        if (response != null && !response.isEmpty()) {
                            try {
                                response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                parseGsonData(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());
                }
            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("servicetype", CUtils.getAskAQuestionServiceTypeCodeFromPage(CGlobalVariables.ask_A_Question_Android));
                headers.put("languageCode", String.valueOf(LANGUAGE_CODE));
                headers.put("Key", CUtils.getApplicationSignatureHashCode(activity));
                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(activity)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(activity)));

                return headers;
            }

        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (progressBar != null && progressBar.isShown())
                progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
