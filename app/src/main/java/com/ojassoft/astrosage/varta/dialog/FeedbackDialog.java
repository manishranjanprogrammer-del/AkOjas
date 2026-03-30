package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.facebook.appevents.AppEventsConstants;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.model.UserReviewBean;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackDialog extends DialogFragment implements View.OnClickListener, VolleyResponse {

    Activity activity;
    AppCompatRatingBar ratingStar;
    EditText feedbackEdt;
    TextView submitBtn, closeBtn, giveFeedbackTxt;
    LinearLayout mainlayout, llFeedbackButtons;
    RelativeLayout success_layout;
    CustomProgressDialog pd;
    RequestQueue queue;
    String mobilePhoneNo = "", astrologerID = "";
    UserReviewBean userOwnReviewBean;
    AstrologerDetailBean astrologerDetailBean;
    int ratingInInt = 0;
    FragmentManager fragmentManager;
    private String astroName, astroImage;

    String consultationType, callsId;

    ImageView badRateIV, poorRateIV, averageRateIV, goodRateIV, excellentRateIV;
    TextView badRateTV, poorRateTV, averageRateTV, goodRateTV, excellentRateTV;


    public FeedbackDialog(String mobilePhoneNo, String astrologerID, UserReviewBean userOwnReviewBean,
                          String consultationType, String callsId) {
        this.mobilePhoneNo = mobilePhoneNo;
        this.astrologerID = astrologerID;
        this.userOwnReviewBean = userOwnReviewBean;
        this.consultationType = consultationType;
        this.callsId = callsId;
    }

    public FeedbackDialog(String consultationType, String callsId, FragmentManager fragmentManager, AstrologerDetailBean astrologerDetailBean) {
        this.userOwnReviewBean = astrologerDetailBean.getUserOwnReviewModel();
        this.consultationType = consultationType;
        this.callsId = callsId;
        this.fragmentManager = fragmentManager;
        this.astrologerDetailBean = astrologerDetailBean;
    }

    public FeedbackDialog() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag).addToBackStack(null);
            //ft.commitAllowingStateLoss();
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.feedback_dialog_layout, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inItViews(view);

        setUpRatingChangeListener();

        //LayerDrawable stars = (LayerDrawable) ratingStar.getProgressDrawable();
        //stars.getDrawable(2).setColorFilter(getActivity().getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);

        FontUtils.changeFont(getActivity(), giveFeedbackTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), feedbackEdt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        // FontUtils.changeFont(getActivity(), submitBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        queue = VolleySingleton.getInstance(activity).getRequestQueue();

        //Log.e("LoadMore review ", ""+ userOwnReviewBean.getComment() + " , "+ userOwnReviewBean.getRate());

        if (userOwnReviewBean != null) {
            if (userOwnReviewBean.getRate() != null && userOwnReviewBean.getRate().length() > 0) {
                ratingStar.setRating(Float.parseFloat(userOwnReviewBean.getRate()));
            }

            if (userOwnReviewBean.getComment() != null && userOwnReviewBean.getComment().length() > 0) {
                feedbackEdt.setText(userOwnReviewBean.getComment());
            }
        }

        submitBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);

        mobilePhoneNo = CUtils.getUserID(activity);
        if (astrologerDetailBean != null) {
            astrologerID = astrologerDetailBean.getAstrologerId();
        }
        if(astrologerID == null) astrologerID = "";
        return view;
    }

    private void inItViews(View view) {
        ratingStar = view.findViewById(R.id.rating_star);
        feedbackEdt = view.findViewById(R.id.feedback_edt);
        submitBtn = view.findViewById(R.id.submit_btn);
        closeBtn = view.findViewById(R.id.rate_close_btn);
        giveFeedbackTxt = view.findViewById(R.id.give_feedback_txt);
        mainlayout = view.findViewById(R.id.mainlayout);
        llFeedbackButtons = view.findViewById(R.id.llFeedbackButtons);
        success_layout = view.findViewById(R.id.success_layout);

        badRateIV = view.findViewById(R.id.imgViewBad);
        poorRateIV = view.findViewById(R.id.imgViewPoor);
        averageRateIV = view.findViewById(R.id.imgViewAverage);
        goodRateIV = view.findViewById(R.id.imgViewGood);
        excellentRateIV = view.findViewById(R.id.imgViewExcellent);

        badRateTV = view.findViewById(R.id.bad_TV);
        poorRateTV = view.findViewById(R.id.poor_tv);
        averageRateTV = view.findViewById(R.id.average_tv);
        goodRateTV = view.findViewById(R.id.good_tv);
        excellentRateTV = view.findViewById(R.id.excellent_tv);
        setUpEmojiInFeedbackResponse(0);

    }

    private void setUpRatingChangeListener() {
        ratingStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
              setUpEmojiInFeedbackResponse((int)(v));
            }
        });

        badRateIV.setOnClickListener(v -> setUpFeedbackRatingValue(1));
        poorRateIV.setOnClickListener(v -> setUpFeedbackRatingValue(2));
        averageRateIV.setOnClickListener(v -> setUpFeedbackRatingValue(3));
        goodRateIV.setOnClickListener(v -> setUpFeedbackRatingValue(4));
        excellentRateIV.setOnClickListener(v -> setUpFeedbackRatingValue(5));
    }

    private void setUpFeedbackRatingValue(int rating){
        ratingStar.setRating(rating);
        setUpEmojiInFeedbackResponse(rating);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submit_btn:
                if (validation()) {
                    String feedbackStr = feedbackEdt.getText().toString();
                    float rating = ratingStar.getRating();
                    int ratingInt = (int) rating;
                    ratingInInt = ratingInt;
                    CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_RATED, "user_rating_"+ratingInInt, "FeedbackDialog");
                    CUtils.addUserRatingEventForAstrologer(astrologerID, ratingInInt, "FeedbackDialog");
                    //Log.e("LoadMore rating ", "" + ratingInt);
                    submitFeedback(mobilePhoneNo, astrologerID, ratingInt, feedbackStr);
                }
                break;

            case R.id.rate_close_btn:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_BTN_CLOSE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                dismiss();
                break;

        }
    }

    public boolean validation() {
        boolean isValid = true;
        if (ratingStar.getRating() == 0.0) {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.select_rating), activity);
            isValid = false;
        }

       /* if(feedbackEdt.getText().toString() == null || feedbackEdt.getText().toString().trim().length() == 0)
        {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.enter_feedback), activity);
            isValid = false;
        }*/

        return isValid;
    }

    private void submitFeedback(String mobileNo, String astrologerId, int rating, String feedback) {

        if (!CUtils.isConnectedWithInternet(activity)) {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), activity);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(activity);
            pd.show();
            pd.setCancelable(false);
            enableDesableSubmitBtn(false);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ASTROLOGER_FEEDBACK_SUBMIT_URL,
//                    FeedbackDialog.this, false, getCallParams(mobileNo, astrologerId, rating, feedback), 1).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.submitFeedback(getCallParams(mobileNo, astrologerId, rating, feedback));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    Log.e("LoadMore", "response=" + response);
                    if (response.body() != null) {
                        try {
                            //{"status":"1","msg":"Feedback inserted successfully."}
                            String myResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            final String status = jsonObject.getString("status");
                            if (status.contains("1")) {
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_SUBMIT_BTN,
                                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                feedbackEdt.setText("");
                                llFeedbackButtons.setVisibility(View.GONE);
                                success_layout.setVisibility(View.VISIBLE);
                                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.feedback_success), activity);
                            } else {
                                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error), activity);
                            }
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (status.contains("1")) {
                                if (activity != null) {
                                    if (ratingInInt > 4) {
                                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_RATE_APP_DIALOG,
                                                CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                                        String Headingtext = activity
                                                .getString(R.string.app_mainheading_text_kundali);
                                        String SubHeadingtext = activity
                                                .getString(R.string.app_subheading_text_kundali);
                                        String Subchildheading = activity
                                                .getString(R.string.app_subchild_text_kundali);
                                        AppRater.app_launched(activity,
                                                Headingtext, SubHeadingtext, Subchildheading);
                                    }
                                }
                            }
                            dismiss();
                        }catch (Exception e){
                            //
                        }
                    }
                }, 1000); // Millisecond 1000 = 1 sec*/
                            openPostFeedbackDialog();
                            dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error), activity);
                    }
                    enableDesableSubmitBtn(true);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error), activity);
                    enableDesableSubmitBtn(true);
                }
            });
        }
    }

    public Map<String, String> getCallParams(String phoneNo, String astrologerId, int rating, String feedback) {

        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
        String offerType = CUtils.getCallChatOfferType(activity);
        if (consultationType == null) {
            consultationType = "";
        }

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.USER_PHONE_NO, phoneNo);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.STAR_RATING, "" + rating);
        headers.put(CGlobalVariables.FEEDBACK, feedback);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
        headers.put(CGlobalVariables.SOURCE_PARAMS, CUtils.getAppPackageName(activity));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //headers.put(CGlobalVariables.NPS_RATING, String.valueOf(rating));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(activity));
        //headers.put(CGlobalVariables.KEY_CUSTOMID, customId);
        headers.put(CGlobalVariables.KEY_CALLS_ID, callsId);
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(CGlobalVariables.KEY_CONSULTATION_TYPE, consultationType);

        String gender = "NA";
        String hourofbirth = "NA";
        String minuteofbirth = "NA";
        String secondofbirth = "NA";
        String dayofbirth = "NA";
        String monthofbirth = "NA";
        String yearofbirth = "NA";

        if (userProfileData != null) {
            gender = userProfileData.getGender();
            hourofbirth = userProfileData.getHour();
            minuteofbirth = userProfileData.getMinute();
            secondofbirth = userProfileData.getSecond();
            dayofbirth = userProfileData.getDay();
            monthofbirth = userProfileData.getMonth();
            yearofbirth = userProfileData.getYear();
        }

        headers.put(CGlobalVariables.KEY_GENDER, gender);
        headers.put("hourofbirth", hourofbirth);
        headers.put("minuteofbirth", minuteofbirth);
        headers.put("secondofbirth", secondofbirth);
        headers.put("dayofbirth", dayofbirth);
        headers.put("monthofbirth", monthofbirth);
        headers.put("yearofbirth", yearofbirth);

        //Log.e("astrologerUrl", "headers=" + headers);


        //Log.e("LoadMore", "headers="+headers);
        return CUtils.setRequiredParams(headers);
    }

    @Override
    public void onResponse(String response, int method) {
//        hideProgressBar();
//        Log.e("LoadMore", "response="+response);
//        if (response != null && response.length() > 0) {
//            try {
//                //{"status":"1","msg":"Feedback inserted successfully."}
//                JSONObject jsonObject = new JSONObject(response);
//                final String status = jsonObject.getString("status");
//                if (status.contains("1")) {
//                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_SUBMIT_BTN,
//                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//                    feedbackEdt.setText("");
//                    llFeedbackButtons.setVisibility(View.GONE);
//                    success_layout.setVisibility(View.VISIBLE);
//                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.feedback_success), activity);
//                } else {
//                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error), activity);
//                }
//                /*new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            if (status.contains("1")) {
//                                if (activity != null) {
//                                    if (ratingInInt > 4) {
//                                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_RATE_APP_DIALOG,
//                                                CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
//                                        String Headingtext = activity
//                                                .getString(R.string.app_mainheading_text_kundali);
//                                        String SubHeadingtext = activity
//                                                .getString(R.string.app_subheading_text_kundali);
//                                        String Subchildheading = activity
//                                                .getString(R.string.app_subchild_text_kundali);
//                                        AppRater.app_launched(activity,
//                                                Headingtext, SubHeadingtext, Subchildheading);
//                                    }
//                                }
//                            }
//                            dismiss();
//                        }catch (Exception e){
//                            //
//                        }
//                    }
//                }, 1000); // Millisecond 1000 = 1 sec*/
//                openPostFeedbackDialog();
//                dismiss();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error), activity);
//        }
//        enableDesableSubmitBtn(true);
    }

    @Override
    public void onError(VolleyError error) {
        //Log.e("LoadMore", "error="+error);
        hideProgressBar();
        CUtils.showVolleyErrorRespMessage(mainlayout, activity, error, "6");
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {
            //
        }
    }

    private void enableDesableSubmitBtn(boolean isEnabled) {
        if (isEnabled) {
            submitBtn.setEnabled(true);
            submitBtn.setAlpha(1);
            submitBtn.setOnClickListener(this);
        } else {
            submitBtn.setEnabled(false);
            submitBtn.setAlpha(0.6f);
            submitBtn.setOnClickListener(null);
        }
    }

    public void openPostFeedbackDialog() {
        if (astrologerDetailBean != null) {
            PostFeedbackDialog postFeedbackDialog = new PostFeedbackDialog(activity, astrologerDetailBean, userOwnReviewBean);
            postFeedbackDialog.show(fragmentManager, "postFeedbackDialog");
        }
    }

    @Override
    public int getTheme() {
        return R.style.DialogTransformAnimation;
    }


    private void setUpEmojiInFeedbackResponse(int rating) {
        switch (rating) {
            case 0: {
                badRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_bad_grey));
                poorRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_poor_grey));
                averageRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_average_grey));
                goodRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_good_grey));
                excellentRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_excellent_grey));

                badRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                poorRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                averageRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                goodRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                excellentRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                break;
            }
            case 1: {
                badRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_bad));
                poorRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_poor_grey));
                averageRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_average_grey));
                goodRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_good_grey));
                excellentRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_excellent_grey));

                badRateTV.setTextColor(activity.getResources().getColor(R.color.black));
                poorRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                averageRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                goodRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                excellentRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                break;
            }
            case 2:{
                badRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_bad_grey));
                poorRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_poor));
                averageRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_average_grey));
                goodRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_good_grey));
                excellentRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_excellent_grey));

                badRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                poorRateTV.setTextColor(activity.getResources().getColor(R.color.black));
                averageRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                goodRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                excellentRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                break;
            }
            case 3:{
                badRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_bad_grey));
                poorRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_poor_grey));
                averageRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_average));
                goodRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_good_grey));
                excellentRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_excellent_grey));

                badRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                poorRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                averageRateTV.setTextColor(activity.getResources().getColor(R.color.black));
                goodRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                excellentRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                break;
            }
            case 4:{
                badRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_bad_grey));
                poorRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_poor_grey));
                averageRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_average_grey));
                goodRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_good));
                excellentRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_excellent_grey));

                badRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                poorRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                averageRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                goodRateTV.setTextColor(activity.getResources().getColor(R.color.black));
                excellentRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                break;
            }
            case 5:{
                badRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_bad_grey));
                poorRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_poor_grey));
                averageRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_average_grey));
                goodRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_good_grey));
                excellentRateIV.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_excellent));

                badRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                poorRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                averageRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                goodRateTV.setTextColor(activity.getResources().getColor(R.color.hint_color));
                excellentRateTV.setTextColor(activity.getResources().getColor(R.color.black));
                break;
            }
        }

    }

}
