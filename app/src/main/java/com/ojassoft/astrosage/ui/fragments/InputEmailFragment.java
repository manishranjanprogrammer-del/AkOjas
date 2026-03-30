package com.ojassoft.astrosage.ui.fragments;

import static android.app.Activity.RESULT_OK;
import static com.ojassoft.astrosage.ui.act.BaseInputActivity.BRIHAT_KUNDLI_PROFILE_CODE;
import static com.ojassoft.astrosage.ui.fragments.ThanksForBrihatDownloadFragment.EMAIL_DELIVERED_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_PREFS_User_Id;
import static com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.BrihatKundliDownload;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment for handling email input and verification in the Brihat Kundli download flow.
 * This fragment manages:
 * - Email input validation
 * - Email verification status
 * - API communication for email registration
 * - Navigation to verification or thank you screens
 * - Integration with BrihatKundliDownload activity
 * 
 * The fragment follows a flow:
 * 1. User enters email
 * 2. Email is validated
 * 3. API call is made to register/verify email
 * 4. Based on response, user is navigated to appropriate screen
 */
public class InputEmailFragment extends Fragment {
    EditText editTextEmail;
    TextView sendCodeBtn;
    boolean isVerifiedMail = false;
    String verifiedMailId;
    ImageView backArrow;

    BrihatKundliDownload brihatKundliDownloadActivity;

    /**
     * Creates and initializes the fragment's view.
     * Sets up UI components and click listeners.
     * Handles pre-verified email population if available.
     * 
     * @param inflater LayoutInflater for inflating the view
     * @param container Parent view group for the fragment
     * @param savedInstanceState Bundle containing saved state
     * @return The initialized view for the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.BRIHAT_KUNDLI_EMAIL_INPUT_SCREEN_EVENT, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "From_Shortcut");

        View view = inflater.inflate(R.layout.input_email_fragment, container, false);
        sendCodeBtn = view.findViewById(R.id.send_code_btn);
        editTextEmail = view.findViewById(R.id.edit_text_email);
        backArrow = view.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.BK_EMAIL_INPUT_SCREEN_BACK_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "From_Shortcut");
                if (getActivity() != null) {
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        handleVerifiedEmail();

        handleSendBtnClick();
        return view;
    }

    /**
     * Handles pre-verified email population and status.
     * Checks for existing verified email in preferences and populates the input field.
     * Updates verification status based on stored data.
     */
    private void handleVerifiedEmail() {

        verifiedMailId = CUtils.getAstroshopUserEmail(brihatKundliDownloadActivity);

        if (!TextUtils.isEmpty(verifiedMailId)) {
            editTextEmail.setText(verifiedMailId);
        }
        isVerifiedMail = CUtils.getBooleanData(brihatKundliDownloadActivity, CGlobalVariables.EMAIL_IS_VERIFIED, false);

    }

    /**
     * Sets up the send button click listener.
     * Triggers email validation and server verification process.
     * Includes analytics tracking for button click.
     */
    private void handleSendBtnClick() {
        sendCodeBtn.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.BRIHAT_KUNDLI_EMAIL_SUBMIT_BTN_CLICK_EVENT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "From_Shortcut");
            sendEmailToServerForVerify();});
    }

    /**
     * Initiates the email verification process with the server.
     * Performs validation and makes API call to register/verify email.
     * Handles various response scenarios:
     * - Success: Navigates to verification or thank you screen
     * - Already downloaded: Shows appropriate message
     * - Invalid details: Prompts for correction
     * - Rate limiting: Shows wait message
     * 
     * Includes comprehensive error handling and user feedback.
     */
    private void sendEmailToServerForVerify() {
        try {
            String email = editTextEmail.getText().toString().trim();
            // Validate email: not empty and correct format
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (brihatKundliDownloadActivity != null && isAdded()) {
                    Toast.makeText(brihatKundliDownloadActivity, brihatKundliDownloadActivity.getString(R.string.please_enter_valid_email), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // Prepare params with entered email
            Map<String, String> params = getBrihatKundliParams();
            params.put("email", email);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            // Show loading indicator
            CustomProgressDialog pd = new CustomProgressDialog(brihatKundliDownloadActivity);
            pd.setCancelable(false);
            pd.show();
            Call<ResponseBody> call = api.registerEmailForFreeReport(params);
            call.enqueue(new Callback<ResponseBody>() {
                /**
                 * Handles the API response for email registration.
                 * Includes null checks and exception handling for safe UI updates.
                 */
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Dismiss the progress dialog safely
                    if (pd != null && pd.isShowing()) pd.dismiss();
                    // Only update UI if fragment is attached and brihatKundliDownloadActivity is valid
                    if (!isAdded() || brihatKundliDownloadActivity == null) return;

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String respString = response.body().string();
                            Log.e("responseCheck", "onResponse: "+respString );
                            JSONObject jsonObject = new JSONObject(respString);
                            String status = jsonObject.getString("status");
                            android.app.Activity activity = getActivity();
                            if (status.equalsIgnoreCase("1")) {
                                // Success: navigate to Thank You or Verify Email screens
                                if (activity instanceof com.ojassoft.astrosage.ui.act.BrihatKundliDownload) {
                                    if (isVerifiedMail) {
                                        ((com.ojassoft.astrosage.ui.act.BrihatKundliDownload) activity).navigateToThankYouFragment(BrihatKundliDownload.priceInRs);
                                    } else {
                                        ((com.ojassoft.astrosage.ui.act.BrihatKundliDownload) activity).navigateToVerifyEmailFragment(email);
                                    }
                                }
//                                    else {
//                                        Toast.makeText(brihatKundliDownloadActivity, "Verification screen not available.", Toast.LENGTH_SHORT).show();
//                                    }
                            } else if (status.equals("0")) {//please try again
                                Toast.makeText(brihatKundliDownloadActivity, brihatKundliDownloadActivity.getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            } else if (status.equals("2")) { //spam attempt
                                Toast.makeText(brihatKundliDownloadActivity, brihatKundliDownloadActivity.getString(R.string.spam_attemp), Toast.LENGTH_SHORT).show();
                            } else if (status.equals("3")) { //Already downloaded
                                Toast.makeText(brihatKundliDownloadActivity, brihatKundliDownloadActivity.getString(R.string.already_downloaded), Toast.LENGTH_SHORT).show();
                                if (activity instanceof com.ojassoft.astrosage.ui.act.BrihatKundliDownload) {
                                    CUtils.saveBooleanData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FREE_BRIHAT_ALREADY_DOWNLOADED_KEY, true);
                                    CUtils.saveBooleanData(brihatKundliDownloadActivity, EMAIL_DELIVERED_KEY, true);
                                    ((com.ojassoft.astrosage.ui.act.BrihatKundliDownload) activity).navigateToThankYouFragment(BrihatKundliDownload.priceInRs);
                                }
                            } else if (status.equals("4")) { //wait for 24 hours after installing
                                Toast.makeText(brihatKundliDownloadActivity, brihatKundliDownloadActivity.getString(R.string.please_wait_for_24_hrs_after_install), Toast.LENGTH_SHORT).show();
                            } else if (status.equals("5")) { //incorrect birth details
                                Toast.makeText(brihatKundliDownloadActivity, brihatKundliDownloadActivity.getString(R.string.incorrect_birth_details), Toast.LENGTH_SHORT).show();
                                callToGetDetailsFromHomeInputModule();
                            } else if (status.equals("6")) { //incorrect email
                                Toast.makeText(brihatKundliDownloadActivity, brihatKundliDownloadActivity.getString(R.string.incorrect_email), Toast.LENGTH_SHORT).show();
                            } else if (status.equals("7")) { //Too many attempts
                                Toast.makeText(brihatKundliDownloadActivity, brihatKundliDownloadActivity.getString(R.string.too_many_attempts), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Failure: server returned error or empty body
                            Toast.makeText(brihatKundliDownloadActivity, brihatKundliDownloadActivity.getString(R.string.failed_to_send_code_please_try_again), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        // Handle any parsing or runtime exceptions
                        if (brihatKundliDownloadActivity != null && isAdded()) {
                            Toast.makeText(brihatKundliDownloadActivity, "Response error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.e("InputEmail", "onResponse: exception --" + e.getMessage());
                    }
                }

                /**
                 * Handles network failure for the email registration API call.
                 */
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    if (pd != null && pd.isShowing()) pd.dismiss();
                    if (brihatKundliDownloadActivity != null && isAdded()) {
                        // Show network error as toast
                        Toast.makeText(brihatKundliDownloadActivity, "Network error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.e("InputEmail", "onFailure: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            // Handle any unexpected error in the click handler
            if (brihatKundliDownloadActivity != null && isAdded()) {
                Toast.makeText(brihatKundliDownloadActivity, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Log.e("InputEmail", "API call exception: " + e.getMessage());
        }

    }

    /**
     * Builds parameters map for Brihat Kundli API request.
     * Collects user details from preferences and current input:
     * - Birth details
     * - Location information
     * - User preferences
     * - Device information
     * - Verification status
     * 
     * @return Map containing all required parameters for the API request
     */
    private Map<String, String> getBrihatKundliParams() {
        HashMap<String, String> headers = new HashMap<>();
        // Retrieve BeanHoroPersonalInfo from shared preferences
        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) com.ojassoft.astrosage.utils.CUtils.getCustomObject(requireActivity());
        if (beanHoroPersonalInfo == null) return headers;

        headers.put("charttype", "" + com.ojassoft.astrosage.utils.CUtils.getChartStyleFromPreference(requireActivity()));
        headers.put("useryearbirth", String.valueOf(beanHoroPersonalInfo.getDateTime().getYear()));

        headers.put("usermonthbirth", String.valueOf(beanHoroPersonalInfo.getDateTime().getMonth()+1));
        headers.put("userdaybirth", String.valueOf(beanHoroPersonalInfo.getDateTime().getDay()));
        headers.put("userhourbirth", String.valueOf(beanHoroPersonalInfo.getDateTime().getHour()));
        headers.put("userminbirth", String.valueOf(beanHoroPersonalInfo.getDateTime().getMin()));
        headers.put("usereastwest", String.valueOf(beanHoroPersonalInfo.getPlace().getLongDir()));
        headers.put("usernorthsouth", String.valueOf(beanHoroPersonalInfo.getPlace().getLatDir()));
        headers.put("userlangcode", "" + com.ojassoft.astrosage.utils.CUtils.getLanguageCode(requireActivity()));
        headers.put("userdeglat", String.valueOf(beanHoroPersonalInfo.getPlace().getLatDeg()));
        headers.put("userminlat", String.valueOf(beanHoroPersonalInfo.getPlace().getLatMin()));
        headers.put("userseclat", String.valueOf(beanHoroPersonalInfo.getPlace().getLatSec()));
        headers.put("userdeglong", String.valueOf(beanHoroPersonalInfo.getPlace().getLongDeg()));
        headers.put("userminlong", String.valueOf(beanHoroPersonalInfo.getPlace().getLongMin()));

        if (isVerifiedMail) {//if already verified email then only check if user change the prefilled mail or not
            isVerifiedMail = verifiedMailId.equals(editTextEmail.getText().toString());
        }
        headers.put("isverified", isVerifiedMail ? "1" : "0");
        // 'email' will be overwritten by EditText value

        String timeZone;
        if (beanHoroPersonalInfo.getPlace().getTimeZone() != null && !beanHoroPersonalInfo.getPlace().getTimeZone().isEmpty()) {
            timeZone = String.valueOf(beanHoroPersonalInfo.getPlace().getTimeZone());
        } else {
            timeZone = "5.5";
        }
        headers.put("usertimezone", timeZone);
        headers.put("dst", "0");
        headers.put("userseclong", String.valueOf(beanHoroPersonalInfo.getPlace().getLongSec()));
        headers.put("userplace", String.valueOf(beanHoroPersonalInfo.getPlace().getCityName()));
        headers.put("phoneno", com.ojassoft.astrosage.varta.utils.CUtils.getUserID(brihatKundliDownloadActivity));
        headers.put("countrycode", com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(brihatKundliDownloadActivity));
        headers.put("usersex", beanHoroPersonalInfo.getGender());
        headers.put("username", beanHoroPersonalInfo.getName());
        headers.put("userid", com.ojassoft.astrosage.utils.CUtils.getUserName(requireActivity()));
        headers.put("useridas", getUserIdForBlock(requireActivity()));

//        Log.e("mytag", "inputMail: "+ com.ojassoft.astrosage.BuildConfig.VERSION_NAME );
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(brihatKundliDownloadActivity));
        headers.put("key", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(brihatKundliDownloadActivity));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID,com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(brihatKundliDownloadActivity));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION,com.ojassoft.astrosage.BuildConfig.VERSION_NAME);
        return headers;
    }

    /**
     * Initiates navigation to HomeInputScreen for updating user details.
     * Used when birth details need correction.
     * Includes error handling and activity result handling.
     */
    private void callToGetDetailsFromHomeInputModule() {
        try {
            if (getActivity() == null) return;
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
     * Handles activity results from HomeInputScreen.
     * Processes updated user details and reinitiates email verification.
     * 
     * @param requestCode The request code used to start the activity
     * @param resultCode The result code returned by the activity
     * @param data The intent data containing the result
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
                             BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) bundle.getSerializable(CGlobalVariables.KEY_KUNDALI_DETAILS);
                            if(beanHoroPersonalInfo==null) return;
                            CUtils.saveDefaultKundliIfAvailable(brihatKundliDownloadActivity, beanHoroPersonalInfo);
                            if (brihatKundliDownloadActivity != null) {
                              sendEmailToServerForVerify();
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("FreeBrihatKundliDownload", "onActivityResult: Exception - " + e.getMessage());
        }
    }



    // Constant for package name
    private static final String ASTROSAGE_AI_PACKAGE_NAME = "com.ojassoft.astrosage";


    /**
     * Attaches the fragment to its context.
     * Initializes reference to BrihatKundliDownload activity.
     * 
     * @param context The context to attach to
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BrihatKundliDownload) {
            brihatKundliDownloadActivity = (BrihatKundliDownload) context;
        }
    }

}
