package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG;
import static com.ojassoft.astrosage.utils.CUtils.urlEncodeData;
import static com.ojassoft.astrosage.varta.utils.CUtils.getProfileForChatFromPreference;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.model.AIHoroscopeResponse;
import com.ojassoft.astrosage.varta.model.ForecastOutput;
import com.ojassoft.astrosage.varta.model.Score;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.ui.activity.ProfileForChat;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A Fragment responsible for displaying the AI-generated Personalized Horoscope.
 * <p>
 * This fragment handles:
 * 1. Checking if the user has a valid profile (Name, Place, Date, Time).
 * 2. Fetching horoscope data from the server or local cache.
 * 3. Displaying daily forecast details (Vibe, Lucky Color, Energy levels).
 * 4. Displaying various scores (Luck, Career, Relationship) using a RecyclerView.
 * 5. Managing navigation to Login or Profile Creation screens if data is missing.
 */
public class AIHoroscopeFragment extends Fragment implements AiHoroscopeBottomSheet.OnUserProfileSavedListener{
    private boolean isExpanded = false;
    private String originalText;
    private TextView tvHoroscope, tvReadMore, read_less,username,tv_vibe_of_day,tv_lucky_color,tv_lucky_hour,tv_energy_peak, tv_energy_dip,tv_best_activity,tv_stress_trigger;
    private ScrollView scrollView;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    static Activity activity;
    public static String LANG_CHANGE_KEY = "horo_lang_change_key";
    private ScoreAdapter scoreAdapter;
    ImageView profile_icon;
    CustomProgressDialog pd;
    FrameLayout frameLayoutUnloackAIHoroscope;
    ConstraintLayout consLayoutTop;
    TextView btnProceed;
    public final static int BACK_FROM_LOGIN_SIGNUP_ACTIVITY=1010;
    public static String aiHoroscopeLogData = "";

    public static AIHoroscopeFragment newInstance(int rashiType) {

        AIHoroscopeFragment aiHoroscopeFragment = new AIHoroscopeFragment();
        Bundle args = new Bundle();
        args.putInt("rashiType", rashiType);
        aiHoroscopeFragment.setArguments(args);

        return aiHoroscopeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_HOROSCOPE_SCREEN_OPEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ai_horoscope, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        AIHoroscopeFragment.activity = activity;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplicationContext())
                .getLanguageCode();
        String _pwd = CUtils.getUserLoginPassword(requireContext());
        Log.d("AIHoroscopeFragment", "onViewCreated: LANGUAGE_CODE = " + LANGUAGE_CODE+" pwd = "+_pwd);
        // --- View Initialization ---
        CardView cardViewHoroscope = view.findViewById(R.id.cardViewHoroscope);
        CardView cardViewHoroscopeTodayForcast = view.findViewById(R.id.cardViewHoroscopeTodayForcast);
        LinearLayout linLayoutCardView = view.findViewById(R.id.linLayoutCardView);
        RelativeLayout relLayReadMore = view.findViewById(R.id.relLayReadMore);
        RecyclerView recyclerViewScore = view.findViewById(R.id.recyclerViewScore);
        scrollView = view.findViewById(R.id.scrollView);
        tvReadMore = view.findViewById(R.id.read_more);
        tvHoroscope = view.findViewById(R.id.horoscope_text);
        tv_vibe_of_day = view.findViewById(R.id.tv_vibe_of_day);
        tv_lucky_color = view.findViewById(R.id.tv_lucky_color);
        tv_lucky_hour = view.findViewById(R.id.tv_lucky_hour);
        tv_energy_peak = view.findViewById(R.id.tv_energy_peak);
        tv_energy_dip = view.findViewById(R.id.tv_energy_dip);
        tv_best_activity = view.findViewById(R.id.tv_best_activity);
        tv_stress_trigger = view.findViewById(R.id.tv_stress_trigger);
        username = view.findViewById(R.id.username);
        profile_icon = view.findViewById(R.id.profile_icon);
        frameLayoutUnloackAIHoroscope = view.findViewById(R.id.frameLayoutUnloackAIHoroscope);
        consLayoutTop = view.findViewById(R.id.consLayoutTop);
        btnProceed = view.findViewById(R.id.btnProceed);
        TextView txtViewSetUpBirthProfile = view.findViewById(R.id.txtViewSetUpBirthProfile);

        // --- Adapter Setup ---
        scoreAdapter = new ScoreAdapter(new ArrayList<>());
        recyclerViewScore.setAdapter(scoreAdapter);
        loadDataIfFragmentVisible();
//        UserProfileData userProfileData = getProfileForChatFromPreference(activity);
//       // Log.d("AIHoroscopeFragment", "onViewCreated: userProfileData = " + userProfileData.getMaritalStatus()+" Occupation = "+userProfileData.getOccupation());
//        // Check if user profile exists and has a valid name, place, date, and time
//        if (userProfileData != null && !TextUtils.isEmpty(userProfileData.getName()) && !TextUtils.isEmpty(userProfileData.getPlace()) && !TextUtils.isEmpty(userProfileData.getDay()) && !TextUtils.isEmpty(userProfileData.getHour())) {
//            scrollView.setBackground(
//                    new BitmapDrawable(
//                            getResources(),
//                            BitmapFactory.decodeResource(getResources(), R.drawable.ai_horoscope_bg)
//                    )
//            );
//            getDataFromServerOrLocal();
//        } else {
//            // If profile is incomplete, show the "Unlock/Setup" view
//            frameLayoutUnloackAIHoroscope.setVisibility(View.VISIBLE);
//            consLayoutTop.setVisibility(View.GONE);
//            scrollView.setBackground(
//                    new BitmapDrawable(
//                            getResources(),
//                            BitmapFactory.decodeResource(getResources(), R.drawable.ai_horoscope_blur)
//                    )
//            );
//        }


        // --- UI Text Setup ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtViewSetUpBirthProfile.setText(Html.fromHtml(
                    getString(R.string.set_up_your_b_birth_profile_b_to_read_your_personalized_b_ai_horoscope_b),
                    Html.FROM_HTML_MODE_LEGACY
            ));
        }


        relLayReadMore.setOnClickListener(v -> {
           // linLayoutCardView.setVisibility(View.VISIBLE);
           // cardViewHoroscope.setVisibility(View.GONE);
            tvHoroscope.setMaxLines(Integer.MAX_VALUE);
            tvHoroscope.setEllipsize(null);
            cardViewHoroscopeTodayForcast.setVisibility(View.VISIBLE);
            relLayReadMore.setVisibility(View.GONE);

        });


        btnProceed.setOnClickListener(v -> {
            if (!com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity)) {
                Intent intent1 = new Intent(requireContext(), FlashLoginActivity.class);
                //startActivity(intent1);
                startActivityForResult(intent1, BACK_FROM_LOGIN_SIGNUP_ACTIVITY);
            }else {
                openProfileDialog();
            }

        });

        profile_icon.setOnClickListener(v -> {
            //openProfileDialog(true);
            openProfileDialog();
                });
//        AiHoroscopeBottomSheet bottomSheet = new AiHoroscopeBottomSheet();
//        bottomSheet.show(getChildFragmentManager(), "AI_HOROSCOPE");

    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
           // loadDataIfFragmentVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {
       //     loadDataIfFragmentVisible();
        }
    }


    public void loadDataIfFragmentVisible() {

        UserProfileData userProfileData = getProfileForChatFromPreference(activity);
        // Log.d("AIHoroscopeFragment", "onViewCreated: userProfileData = " + userProfileData.getMaritalStatus()+" Occupation = "+userProfileData.getOccupation());
        // Check if user profile exists and has a valid name, place, date, and time
        if (userProfileData != null && !TextUtils.isEmpty(userProfileData.getName()) && !TextUtils.isEmpty(userProfileData.getPlace()) && !TextUtils.isEmpty(userProfileData.getDay()) && !TextUtils.isEmpty(userProfileData.getHour())) {
            scrollView.setBackground(
                    new BitmapDrawable(
                            getResources(),
                            BitmapFactory.decodeResource(getResources(), R.drawable.ai_horoscope_bg)
                    )
            );
            getDataFromServerOrLocal();
        } else {
            // If profile is incomplete, show the "Unlock/Setup" view
            frameLayoutUnloackAIHoroscope.setVisibility(View.VISIBLE);
            consLayoutTop.setVisibility(View.GONE);
            scrollView.setBackground(
                    new BitmapDrawable(
                            getResources(),
                            BitmapFactory.decodeResource(getResources(), R.drawable.ai_horoscope_blur)
                    )
            );
        }
    }

    private void openProfileDialog() {
        try {
            openProfileOrKundliAct(activity, "", "profile_send", BACK_FROM_PROFILE_CHAT_DIALOG);
            //CUtils.openProfileOrKundliAct(activity, "", "profile_send", BACK_FROM_PROFILE_CHAT_DIALOG);
        } catch (Exception e) {
            //
        }
    }

    public  void openProfileOrKundliAct(Activity activity, String url, String fromWhere, int requestCode) {
        UserProfileData userProfileData = getProfileForChatFromPreference(activity);
        int isKundliAvailable = com.ojassoft.astrosage.utils.CUtils.isLocalKundliAvailable(activity);
        if (userProfileData.getName().isEmpty() && isKundliAvailable == 0) { //0 means local kundli available //com.ojassoft.astrosage.utils.CUtils.isCompleteUserData(userProfileData)
            //OPEN SAVEDKUNDLILISTACTIVITY
            CUtils.openSavedKundliList(activity, url, fromWhere, requestCode);
        } else {
            //OPEN PROFILEFORCHAT
            openProfileForHoroScope(activity, url, fromWhere, null, true, requestCode);
        }
    }
    public static void openProfileForHoroScope(Activity activity, String url, String fromWhere, Bundle bundle, boolean isprefill, int requestCode) {
        Intent i = new Intent(activity, ProfileForChat.class);
        String phone = CUtils.getUserID(activity);
        i.putExtra("phoneNo", phone);
        i.putExtra("urlText", url);
        i.putExtra("fromWhere", fromWhere);
        i.putExtra("prefillData", isprefill);
        i.putExtra("isFromAiHoroscope", true);
        if (bundle != null) i.putExtras(bundle);
        activity.startActivityForResult(i, requestCode);
    }

    /**
     * Opens the Profile setup activity.
     * @param isprefill If true, attempts to prefill data from existing user context.
     */
    private void openProfileDialog(boolean isprefill){
        try {
            Log.d("AIHoroscopeFragment", "openProfileDialog: ");
            Intent i = new Intent(activity, ProfileForChat.class);
            String phone = CUtils.getUserID(activity);
            i.putExtra("phoneNo", phone);
            i.putExtra("urlText", "");
            i.putExtra("fromWhere", "profile_send");
            i.putExtra("prefillData", isprefill);
            i.putExtra("isFromAiHoroscope", true);
            startActivityForResult(i, BACK_FROM_PROFILE_CHAT_DIALOG);
        } catch (Exception e){
            Log.d("AIHoroscopeFragment", "Exception: "+ e.getMessage().toString());

        }
    }

    public void handleUserProfileHoroscope(UserProfileData userProfileDataBean){
                if (userProfileDataBean != null) {
                    if (userProfileDataBean.getName() != null && !userProfileDataBean.getName().isEmpty()) {
                        frameLayoutUnloackAIHoroscope.setVisibility(View.GONE);
                        getAiHoroscopeResponse();
                    }
                }
    }
    /**
     * Handles results from Profile Creation or Login activities.
     * If the user successfully creates a profile or logs in, the screen is refreshed
     * to fetch and display the horoscope.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Log.d("quickrecharge result","requestCode="+requestCode);
        // Log.d("quickrecharge result","data="+data);

//        if (requestCode == BACK_FROM_PROFILECHATDIALOG) {
//            if (data != null) {
//
//                UserProfileData userProfileDataBean = (UserProfileData) data.getExtras().get("USER_DETAIL");
//                if (userProfileDataBean != null) {
//                    if (userProfileDataBean.getName() != null && !userProfileDataBean.getName().isEmpty()) {
//                        frameLayoutUnloackAIHoroscope.setVisibility(View.GONE);
//                        getAiHoroscopeResponse();
//                    }
//                }
//
//            }
//
//        }
        if (requestCode == BACK_FROM_LOGIN_SIGNUP_ACTIVITY){
            frameLayoutUnloackAIHoroscope.setVisibility(View.GONE);
            if (getProfileForChatFromPreference(activity) != null && !TextUtils.isEmpty(getProfileForChatFromPreference(activity).getName()) && !TextUtils.isEmpty(getProfileForChatFromPreference(activity).getPlace()) && !TextUtils.isEmpty(getProfileForChatFromPreference(activity).getDay()) && !TextUtils.isEmpty(getProfileForChatFromPreference(activity).getHour())) {
                getDataFromServerOrLocal();
            } else {
                frameLayoutUnloackAIHoroscope.setVisibility(View.VISIBLE);
                consLayoutTop.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Determines whether to fetch data from the server or use the local cache.
     * Logic:
     * 1. Checks if the language has changed. If so, invalidates cache and fetches from server.
     * 2. Checks if the cached data exists and is valid (e.g., within the same day).
     * 3. If valid, parses local data. If invalid/missing, calls API.
     */
    private void getDataFromServerOrLocal() {
        try {
            aiHoroscopeLogData = aiHoroscopeLogData+"getDataFromServerOrLocal called...\n";
            //com.ojassoft.astrosage.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_HOROSCOPE, "");

            // Retrieve last API call timestamp and current time
            String lastApiCallTimestamp = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_HOROSCOPE_API_CACHE_TIME_KEY, "");
//            long currentTime = System.currentTimeMillis();
            String date = com.ojassoft.astrosage.utils.CUtils.getDialogDate(0);

            //   Log.e(TAG, "getDataFromServerOrLocal: date =  "+date );
            boolean isCacheValid = com.ojassoft.astrosage.utils.CUtils.checkDatesSame(lastApiCallTimestamp);
            // Check if cache is still valid (within 24 hours)
            Log.d("AIHoroscopeFragment", "getDataFromServerOrLocal: isCacheValid =  " + isCacheValid);
            int lastLangCode = com.ojassoft.astrosage.utils.CUtils.getIntData(activity, LANG_CHANGE_KEY, 0);
            int currentLangCode = com.ojassoft.astrosage.utils.CUtils.getLanguageCode(activity);

            // If language has changed, force refresh and update cache keys
            if (isCacheValid && lastLangCode != currentLangCode) {
                aiHoroscopeLogData = aiHoroscopeLogData+" get from server due to lang change...\n";

                Log.d("AIHoroscopeFragment", "getDataFromServerOrLocal: get from server due to lang change. ");
                com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_HOROSCOPE_API_CACHE_TIME_KEY, date);
                com.ojassoft.astrosage.varta.utils.CUtils.saveIntData(activity, LANG_CHANGE_KEY, currentLangCode);
                getAiHoroscopeResponse();
                return;
            }

            if (isCacheValid) {
                // Try to load cached data
                String aiHoroscopeResponse = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_HOROSCOPE, "");

                boolean cacheMiss = false;
                // Handle AI astrologer list
                if (aiHoroscopeResponse.isEmpty()) {
                    aiHoroscopeLogData = aiHoroscopeLogData+" Empty AIHoroscope so loading from server...\n";

                    Log.d("AIHoroscopeFragment", "is empatyy aiHoroscopeResponse: ");

                    getAiHoroscopeResponse();
                    cacheMiss = true;
                } else {
                    try {
                        aiHoroscopeLogData = aiHoroscopeLogData+" loading from cache...\n";

                        Log.d("AIHoroscopeFragment", "parseAiHoroSCopepeResponse" + aiHoroscopeResponse);

                        parseAiHoroSCopepeResponse(aiHoroscopeResponse);
                    } catch (Exception e) {
                        aiHoroscopeLogData = aiHoroscopeLogData+" exception occured while parsing from local..."+e.getMessage()+"\n";

                        Log.d("AIHoroscopeFragment", "Error in getDataFromServerOrLocal tt: " + e.getMessage().toString());

                        getAiHoroscopeResponse();

                        cacheMiss = true;
                    }
                }


                // If either cache is missing or failed to parse, update the cache timestamp
                if (cacheMiss) {
                    aiHoroscopeLogData = aiHoroscopeLogData+" cacheMiss..\n";

                    com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_HOROSCOPE_API_CACHE_TIME_KEY, date);
                }
            } else {
                // Cache expired or missing: fetch both from server and update cache timestamp
                // Log.e(TAG, "getDataFromServerOrLocal: get from server   " );
                aiHoroscopeLogData = aiHoroscopeLogData+" Empty AIHoroscope so loading from server...\n";

                com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_HOROSCOPE_API_CACHE_TIME_KEY, date);
                getAiHoroscopeResponse();
            }
        } catch (Exception ex) {
            aiHoroscopeLogData = aiHoroscopeLogData+" exception occured..."+ex.getMessage()+"\n";

            Log.d("AIHoroscopeFragment", "Error in getDataFromServerOrLocal: ", ex);
            // As a fallback, force fetch from server
            getAiHoroscopeResponse();

        }
    }


    /**
     * Makes a network request to fetch the AI Horoscope from the API.
     * Shows a progress bar while loading and handles the response.
     */
    private void getAiHoroscopeResponse() {
        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(requireContext());

        if (userProfileData.getMaritalStatus().equals("NotSpecified") || userProfileData.getOccupation().equals("NotSpecified")) {
            AiHoroscopeBottomSheet bottomSheet = new AiHoroscopeBottomSheet();
            bottomSheet.show(getChildFragmentManager(), "AI_HOROSCOPE");
            bottomSheet.setCancelable(false);
            return;
        }

        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(requireContext())) {
            Toast.makeText(requireContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressBar();
        ApiList api = RetrofitClient.getKundliAIInstance().create(ApiList.class);
//        ApiList api = RetrofitClient.getInstance().create(ApiList.class);

        Call<ResponseBody> call = api.getAIHoroscopeResponse(getHoroscopeParams(userProfileData));
        Log.d("AIHoroscopeFragment", "url: " + call.request().url());
        aiHoroscopeLogData = aiHoroscopeLogData+" AIHoroscopeFragment URL...: "+call.request().url()+"\n";

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hideProgressBar();
                    String myResponse = response.body().string();
                    aiHoroscopeLogData = aiHoroscopeLogData+" AIHoroscopeFragment Response...: "+myResponse+"\n";

                    com.ojassoft.astrosage.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_HOROSCOPE, myResponse);
                    parseAiHoroSCopepeResponse(myResponse);
                    Log.d("AIHoroscopeFragment", "Response: " + myResponse);
                } catch (Exception e) {
                    hideProgressBar();
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Handle exceptions silently
                    aiHoroscopeLogData = aiHoroscopeLogData+" Exception occured in response...: "+e.getMessage()+"\n";

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                aiHoroscopeLogData = aiHoroscopeLogData+" Api failure...: "+t.getMessage()+"\n";
                Log.d("AIHoroscopeFragment", "onFailure: " + t.getMessage());
                hideProgressBar();
            }
        });


    }

    /**
     * Parses the JSON response string into the AIHoroscopeResponse model
     * and populates the UI views with the data.
     *
     * @param aiHoroscopeResponse The JSON response string.
     */
    private void parseAiHoroSCopepeResponse(String aiHoroscopeResponse) {
        scrollView.setVisibility(View.VISIBLE);
        consLayoutTop.setVisibility(View.VISIBLE);
        frameLayoutUnloackAIHoroscope.setVisibility(View.GONE);
        scrollView.setBackground(
                new BitmapDrawable(
                        getResources(),
                        BitmapFactory.decodeResource(getResources(), R.drawable.ai_horoscope_bg)
                )
        );
        try {
            aiHoroscopeLogData = aiHoroscopeLogData+" parsing start \n";
            aiHoroscopeLogData = aiHoroscopeLogData+" parsing start Response : "+aiHoroscopeResponse+ "\n";

            Gson gson = new Gson();
            AIHoroscopeResponse model = gson.fromJson(aiHoroscopeResponse, AIHoroscopeResponse.class);

            if (model == null || model.getHoroscope() == null) return;

            if (model.getHoroscope().getHoroscope_output().getHOROSCOPE_TEXT() != null){
                consLayoutTop.setVisibility(View.VISIBLE);
                originalText = model.getHoroscope().getHoroscope_output().getHOROSCOPE_TEXT();
                tvHoroscope.setText(originalText);

            }

            if (model.getInput().name != null){
                username.setText(model.getInput().name);
            }
            if(model.getInput().sex != null){
                Log.d("AIHoroscopeFragment","sex:- "+model.getInput().sex);
                int genderRes = R.drawable.ic_male_ai;
                if (!model.getInput().sex.isEmpty() && !model.getInput().sex.equals("N") && !model.getInput().sex.equals("NotSpecified")){
                    if (model.getInput().sex.equals("M") || model.getInput().sex.equals("Male")) {
                        genderRes = R.drawable.ic_male_ai;;
                    } else {
                        genderRes = R.drawable.ic_female_ai;;
                    }
                }else {
                    genderRes = R.drawable.ic_male_ai;;

                }
                profile_icon.setImageResource(genderRes);
            }
            if (model.getHoroscope().getForecast_output() != null){

                ForecastOutput forecastOutput = model.getHoroscope().getForecast_output();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // Vibe of the day
                    String vibeText = "<b>" + forecastOutput.getVIBE_OF_THE_DAY().label + ":</b> "
                            + forecastOutput.getVIBE_OF_THE_DAY().value;
                    tv_vibe_of_day.setText(Html.fromHtml(vibeText, Html.FROM_HTML_MODE_LEGACY));

// Lucky Color
                    String luckyColorText = "<b>" + forecastOutput.getLUCKY_COLOR().label + ":</b> "
                            + forecastOutput.getLUCKY_COLOR().value;
                    tv_lucky_color.setText(Html.fromHtml(luckyColorText, Html.FROM_HTML_MODE_LEGACY));

// Lucky Hour
                    String luckyHourText = "<b>" + forecastOutput.getLUCKY_HOUR().label + ":</b> "
                            + forecastOutput.getLUCKY_HOUR().value;
                    tv_lucky_hour.setText(Html.fromHtml(luckyHourText, Html.FROM_HTML_MODE_LEGACY));

// Energy Peak
                    String energyPeakText = "<b>" + forecastOutput.getENERGY_PEAK().label + ":</b> "
                            + forecastOutput.getENERGY_PEAK().value;
                    tv_energy_peak.setText(Html.fromHtml(energyPeakText, Html.FROM_HTML_MODE_LEGACY));

// Energy Dip
                    String energyDipText = "<b>" + forecastOutput.getENERGY_DIP().label + ":</b> "
                            + forecastOutput.getENERGY_DIP().value;
                    tv_energy_dip.setText(Html.fromHtml(energyDipText, Html.FROM_HTML_MODE_LEGACY));

// Best Activity
                    String bestActivityText = "<b>" + forecastOutput.getBEST_ACTIVITY_OF_THE_DAY().label + ":</b> "
                            + forecastOutput.getBEST_ACTIVITY_OF_THE_DAY().value;
                    tv_best_activity.setText(Html.fromHtml(bestActivityText, Html.FROM_HTML_MODE_LEGACY));

// Stress Trigger
                    String stressTriggerText = "<b>" + forecastOutput.getSTRESS_TRIGGER().label + ":</b> "
                            + forecastOutput.getSTRESS_TRIGGER().value;
                    tv_stress_trigger.setText(Html.fromHtml(stressTriggerText, Html.FROM_HTML_MODE_LEGACY));

                }

            }

            List<Score> scoreList = model.getHoroscope().getScoresOutput();

           // List<ScoreItem> scoreList = convertToScoreList(so);

            // Pass safely to adapter (no crash, no null, no break)
            scoreAdapter.updateList(scoreList);

        } catch (Exception e) {
            Log.e("AIHoroscopeFragment", "Parse Error: " + e.getMessage());
            aiHoroscopeLogData = aiHoroscopeLogData+" Parse Error: "+e.getMessage()+"\n";

        }
    }

    /**
     * Shows a custom progress dialog if not already showing.
     */
    private void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(requireContext());
            }
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            //
        }
    }

    /**
     * Prepares the map of parameters required for the AI Horoscope API call.
     * Extracts user profile data (name, DOB, time, place, coordinates) from preferences.
     *
     * @return A map containing API parameters.
     */
    private void hideProgressBar() {
            try {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
            }
    }

    /**
     * Prepares the map of parameters required for the AI Horoscope API call.
     * Extracts user profile data (name, DOB, time, place, coordinates) from preferences.
     *
     * @return A map containing API parameters.
     */
    public Map<String, String> getHoroscopeParams(UserProfileData userProfileData) {
        return buildHoroscopeParamsForRequest(
                userProfileData,
                LANGUAGE_CODE,
                com.ojassoft.astrosage.utils.CUtils.getUserName(requireContext()),
                com.ojassoft.astrosage.utils.CUtils.getUserPassword(requireContext())
        );
    }

    static Map<String, String> buildHoroscopeParamsForRequest(UserProfileData userProfileData, int languageCode, String userId, String password) {
        Map<String, String> params = new HashMap<>();

        params.put("name", userProfileData.getName());
        params.put("sex", userProfileData.getGender());
        params.put("day", userProfileData.getDay());
        params.put("month", userProfileData.getMonth());
        params.put("year", userProfileData.getYear());
        params.put("hrs", userProfileData.getHour());
        params.put("min", userProfileData.getMinute());
        params.put("sec", userProfileData.getSecond());
        params.put("dst", "0");
        params.put("place", userProfileData.getPlace());
        params.put("longdeg", userProfileData.getLongdeg());
        params.put("longmin", userProfileData.getLongmin());
        params.put("longew", userProfileData.getLongew());
        params.put("latdeg", userProfileData.getLatdeg());
        params.put("latmin", userProfileData.getLatmin());
        params.put("latns", userProfileData.getLatns());
        params.put("timezone", userProfileData.getTimezone());
        params.put("ayanamsa", "0");
        params.put("charting", "0");
        params.put("kphn", "0");
        params.put("languagecode", String.valueOf(languageCode));
        params.put("userid", urlEncodeData(userId));
        params.put("ms", userProfileData.getMaritalStatus());
        params.put("oc", userProfileData.getOccupation());
        params.put("pw", urlEncodeData(password));

        return params;
    }

    @Override
    public void onUserProfileSaved() {
        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(requireContext());
        handleUserProfileHoroscope(userProfileData);
    }

    /**
     * RecyclerView Adapter to display a list of Score items (e.g., Luck, Money, Relationship).
     * Handles dynamic width calculation for custom progress bars.
     */
    public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

        private List<Score> scoreList;

        public ScoreAdapter(List<Score> scoreList) {
            this.scoreList = scoreList != null ? scoreList : new ArrayList<>();
        }

        // Safe update method — prevents null list crash
        public void updateList(List<Score> newList) {
            this.scoreList = newList != null ? newList : new ArrayList<>();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_score, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Score item = scoreList.get(position);
            Log.d("AIHoroscopeFragment", "onBindViewHolder: item = " + item.getLabel()+", value = "+item.getValue());

            // Extra safety (never crashes)
            if (item == null) return;

            holder.tvLabel.setText(item.getLabel() != null ? item.getLabel() : "N/A");
            holder.tvValue.setText(String.valueOf(item.getValue()));


            holder.container.post(() -> {
                int progress = item.getValue();  // your %
                int width = holder.container.getWidth();

                int fillWidth = (width * progress) / 100;
                holder.barFill.getLayoutParams().width = fillWidth;
                holder.barFill.requestLayout();

                holder.thumb.setX(fillWidth - (holder.thumb.getWidth() / 2f));
            });

            if (position == 0){
//                holder.barFill.setBackgroundColor(getResources().getColor(R.color.color_lucky_index));
                holder.barFill.setBackgroundResource(R.drawable.progress_bar_round_lucky);
                holder.thumb.setBackgroundResource(R.drawable.progress_thumb);
            }else if (position == 1) {
//                holder.barFill.setBackgroundColor(getResources().getColor(R.color.color_emotion_index));
                holder.barFill.setBackgroundResource(R.drawable.progress_bar_round_emotion);
                holder.thumb.setBackgroundResource(R.drawable.progress_bar_emotion);
            }else if (position == 2){
//                holder.barFill.setBackgroundColor(getResources().getColor(R.color.color_money_index));
                holder.barFill.setBackgroundResource(R.drawable.progress_bar_round_money);
                holder.thumb.setBackgroundResource(R.drawable.progress_bar_money);
            }else if (position == 3){
//                holder.barFill.setBackgroundColor(getResources().getColor(R.color.color_relation_index));
                holder.barFill.setBackgroundResource(R.drawable.progress_bar_round_relationship);
                holder.thumb.setBackgroundResource(R.drawable.progress_bar_relationship);
            }
        }

        @Override
        public int getItemCount() {
            return scoreList != null ? scoreList.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvLabel, tvValue;
            FrameLayout container;
            View barFill,thumb;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvLabel = itemView.findViewById(R.id.tvLabel);
                tvValue = itemView.findViewById(R.id.tvValue);
                 container = itemView.findViewById(R.id.progressContainerLuckScore);
                 barFill = itemView.findViewById(R.id.progressFillLuckScore);
                 thumb = itemView.findViewById(R.id.progressThumbLuckScore);
            }
        }
    }
}
