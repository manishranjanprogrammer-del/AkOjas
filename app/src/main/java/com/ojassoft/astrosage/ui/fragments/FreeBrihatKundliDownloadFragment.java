package com.ojassoft.astrosage.ui.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;
import static com.ojassoft.astrosage.ui.act.BaseInputActivity.BRIHAT_KUNDLI_PROFILE_CODE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;
import static com.ojassoft.astrosage.utils.CUtils.isPopupLoginShown;
import static com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BuildConfig;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.UnlockBrihatHelperClass;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.model.BigHorscopeProductModel;
import com.ojassoft.astrosage.model.BigHorscopeServiceModel;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BrihatActivity;
import com.ojassoft.astrosage.ui.act.BrihatKundliDownload;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment for handling the free Brihat Kundli download functionality.
 * Manages the 24-hour timer, download button state, and user flow for downloading the Brihat Kundli.
 * Includes features for:
 * - 24-hour countdown timer after app installation
 * - User authentication check
 * - Product price display
 * - Navigation to email input and verification
 */
public class FreeBrihatKundliDownloadFragment extends Fragment {

    private Button downloadBtn;
    private TextView timerTextView, unlockTv, priceTextTV;
    private BeanHoroPersonalInfo beanHoroPersonalInfo;
    private String installTime;
    private long installTimeInMS;
    private boolean downloadEnabled = false;
    private ImageView backArrow;
    private BrihatKundliDownload brihatKundliDownloadActivity;
    private String priceInRs;

    /**
     * Creates and initializes the fragment's view.
     * Sets up UI components, event listeners, and initial state.
     * Handles the price display from activity arguments.
     * 
     * @param inflater The LayoutInflater object that can be used to inflate views
     * @param container The parent view that the fragment's UI should be attached to
     * @param savedInstanceState Bundle containing the fragment's previously saved state
     * @return The View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.free_brihat_kundli_download_layout, container, false);

        timerTextView = view.findViewById(R.id.timer_tv);
        downloadBtn = view.findViewById(R.id.download_btn);
        unlockTv = view.findViewById(R.id.unlock_tv);
        backArrow = view.findViewById(R.id.back_arrow);
        priceTextTV = view.findViewById(R.id.price_text);

        // Get price from arguments or activity
        if (getArguments() != null && getArguments().containsKey("priceInRs")) {
            priceInRs = getArguments().getString("priceInRs");
        } else if (brihatKundliDownloadActivity != null) {
            priceInRs = BrihatKundliDownload.priceInRs;
        }

        // Display price if available
        if (priceInRs != null && !priceInRs.isEmpty()) {
            priceTextTV.setVisibility(VISIBLE);
            priceTextTV.setText(brihatKundliDownloadActivity.getResources().getString(R.string.worth_rs599, priceInRs));
        }

        backArrow.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });


        if (CUtils.IsAppInstallTimeRecorded(brihatKundliDownloadActivity)) {
            installTime = CUtils.getAppInstallTime(brihatKundliDownloadActivity);
        } else {
            new UnlockBrihatHelperClass(brihatKundliDownloadActivity).checkServerForInstallTime(installTimeValue -> {
                installTime = installTimeValue;
            });
        }

        if (installTime != null && !installTime.isEmpty()) {
            Log.e("InstallTimeFlow", "Using installTime for flow: " + installTime);
            checkFor24Hours();
        }

        setTimerAndDownloadBtn();

        downloadBtn.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FREE_BRIHAT_KUNDLI_DOWNLOAD_BTN_CLICK_EVENT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "From_Shortcut");
            Log.e("InstallTimeFlow", "Download button clicked. installTime used: " + installTime);
            if (CUtils.isUserLogedIn(brihatKundliDownloadActivity)) {
                navigateToNextPage();
            } else {
                isPopupLoginShown = true;
                AstrosageKundliApplication.isOpenVartaPopup = true;
                Intent intent1 = new Intent(brihatKundliDownloadActivity, FlashLoginActivity.class);
                startActivity(intent1);
            }
        });

        return view;
    }

    private void navigateToNextPage() {
        beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils.getCustomObject(brihatKundliDownloadActivity);
        if (beanHoroPersonalInfo != null) {
            brihatKundliDownloadActivity.navigateToInputEmailFragment();
        } else {
            callToGetDetailsFromHomeInputModule();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPopupLoginShown){
            if (CUtils.isUserLogedIn(brihatKundliDownloadActivity)) {
                isPopupLoginShown = false;
                navigateToNextPage();
            }
        }
    }

    /**
     * Sets up the countdown timer and download button state.
     * If 24 hours have passed, enables the download button and hides the timer.
     * Otherwise, starts a countdown timer showing remaining time.
     * Includes null checks and proper UI state management.
     */
    private void setTimerAndDownloadBtn() {
        if (downloadEnabled) {
            downloadBtn.setEnabled(true);
            timerTextView.setVisibility(GONE); // Hide timer if already 24 hours
            unlockTv.setVisibility(GONE);
        } else {
            downloadBtn.setEnabled(false);
            long twentyFourHoursMillis = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
            long remainingTime = (installTimeInMS + twentyFourHoursMillis) - System.currentTimeMillis();
            if (remainingTime > 0) {
                new CountDownTimer(remainingTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long hours = millisUntilFinished / (1000 * 60 * 60);
                        long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                        long seconds = (millisUntilFinished % (1000 * 60)) / 1000;

                        timerTextView.setText(prepareTextToShowAsTimer(String.format("%02d(H) : %02d(M) : %02d(S)", hours, minutes, seconds)));
                    }

                    @Override
                    public void onFinish() {
                        downloadBtn.setEnabled(true);
                        timerTextView.setVisibility(GONE); // Hide timer if already 24 hours
                        unlockTv.setVisibility(GONE);
                    }
                }.start();
            }
        }
    }

    /**
     * Checks if 24 hours have elapsed since app installation.
     * Updates the downloadEnabled flag and triggers timer setup.
     * 
     * @throws ParseException if the install time string cannot be parsed
     */
    private void checkFor24Hours() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(installTime);
            if (date != null) {
                installTimeInMS = date.getTime();
                Log.e("InstallTimeFlow", "Parsed installTime to ms: " + installTimeInMS);
            }
            long currentTimeInMS = System.currentTimeMillis();
            long hoursDifference = (currentTimeInMS - installTimeInMS) / (1000 * 60 * 60); // Convert milliseconds to hours
            downloadEnabled = hoursDifference > 24;
            setTimerAndDownloadBtn();
        } catch (ParseException e) {
            Log.e("FreeBrihatKundliDownload", "checkFor24Hours: Exception : " + e.getMessage());
        }
    }

    /**
     * Launches the HomeInputScreen activity to collect user details.
     * Used when user details are not available in the current session.
     * Includes proper bundle handling and intent extras.
     */
    private void callToGetDetailsFromHomeInputModule() {
        try {
            if (brihatKundliDownloadActivity == null) return;
            Bundle bundle = new Bundle();
            Intent intent = new Intent(brihatKundliDownloadActivity, HomeInputScreen.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC);
            intent.putExtra(CGlobalVariables.VARTA_PROFILE_QUERY_DATA, true);
            intent.putExtras(bundle);
            startActivityForResult(intent, BRIHAT_KUNDLI_PROFILE_CODE);
        } catch (Exception e) {
            Log.e("FreeBrihatKundliDownload", "callToGetDetailsFromHomeInputModule: Exception - " + e.getMessage());
        }
    }

    /**
     * Handles results from started activities.
     * Processes user details from HomeInputScreen and navigates to email input.
     * 
     * @param requestCode The integer request code originally supplied to startActivityForResult()
     * @param resultCode The integer result code returned by the child activity
     * @param data An Intent that carries the result data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case BRIHAT_KUNDLI_PROFILE_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            beanHoroPersonalInfo = (BeanHoroPersonalInfo) bundle.getSerializable(CGlobalVariables.KEY_KUNDALI_DETAILS);
                            if(beanHoroPersonalInfo==null) return;
                            CUtils.saveDefaultKundliIfAvailable(brihatKundliDownloadActivity, beanHoroPersonalInfo);
                            if (brihatKundliDownloadActivity != null) {
                                brihatKundliDownloadActivity.navigateToInputEmailFragment();
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("FreeBrihatKundliDownload", "onActivityResult: Exception - " + e.getMessage());
        }
    }

    /**
     * Formats the timer text with proper styling.
     * Applies relative size spans to the time units (H, M, S).
     * 
     * @param time The time string in format "HH(H) : MM(M) : SS(S)"
     * @return SpannableString with formatted timer text
     */
    public SpannableString prepareTextToShowAsTimer(String time) {
//        time should be in HH:MM:SS format with identification text as 24(H): 59(M): 40(S) with space in between
        SpannableString stringToShow = new SpannableString(time);
        stringToShow.setSpan(new RelativeSizeSpan(0.4f), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringToShow.setSpan(new RelativeSizeSpan(0.4f), 10, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringToShow.setSpan(new RelativeSizeSpan(0.4f), 18, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return stringToShow;
    }

    /**
     * Called when the fragment is attached to its host activity.
     * Stores a reference to the BrihatKundliDownload activity for navigation.
     * 
     * @param context The context to which the fragment is being attached
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BrihatKundliDownload) {
            brihatKundliDownloadActivity = (BrihatKundliDownload) context;
        }
    }
}
