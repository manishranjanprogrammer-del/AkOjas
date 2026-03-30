package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.ui.fragments.ThanksForBrihatDownloadFragment.EMAIL_DELIVERED_KEY;
import static com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.KeyEvent;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.BrihatKundliDownload;
import com.ojassoft.astrosage.varta.customwidgets.CustomEditText;
import com.ojassoft.astrosage.varta.interfacefile.CustomEditTextListener;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment responsible for displaying the email verification UI to the user.
 * Inflates the layout for email verification and handles its lifecycle.
 */
public class VerifyEmailFragment extends Fragment {
    private CustomEditText otpEd1, otpEd2, otpEd3, otpEd4;
    private TextView txtViewResendOtp;
    private LinearLayout llResendOTPLayout;
    private ImageView backArrow;
    private static final long TOTAL_VERIFICATION_TIME = 60000; // 1 minute
    private static final long ONE_SECOND = 1000;
    private CountDownTimer countDownTimer;
    private String userEmail; // To be set from previous screen


    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUCCESS)) {
                callVerifyEmailAPI();

            }
            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.BRIHAT_KUNDLI_OTP_VERIFY_SCREEN_OPEN_EVENT, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "From_Shortcut");

        View view = inflater.inflate(R.layout.email_verification_layout, container, false);
        initializeViews(view);
        setupOTPInputHandling();
        initEditTextPasteListner();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startResendOtpCountdown();
    }

    private void initializeViews(View view) {
        txtViewResendOtp = view.findViewById(R.id.txtViewResendOtp);
        llResendOTPLayout = view.findViewById(R.id.llResendOTPLayout);
        backArrow = view.findViewById(R.id.back_arrow);
        TextView verifyBtn = view.findViewById(R.id.verify_btn);

        verifyBtn.setOnClickListener(v -> {
            Log.d("VerifyEmailFragment", "Verify button clicked");
            if (validateInputs()) {
                callVerifyEmailAPI();
            }
        });

        backArrow.setOnClickListener(v -> {
            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.BK_EMAIL_Verify_SCREEN_BACK_BTN_CLICK, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "From_Shortcut");
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        otpEd1 = view.findViewById(R.id.otp_edt1);
        otpEd2 = view.findViewById(R.id.otp_edt2);
        otpEd3 = view.findViewById(R.id.otp_edt3);
        otpEd4 = view.findViewById(R.id.otp_edt4);

        // Get email from arguments
        if (getArguments() != null) {
            userEmail = getArguments().getString("email");
//            Log.d("VerifyEmailFragment email = ", userEmail);
        }
    }

    private void setupOTPInputHandling() {
        Log.d("VerifyEmailFragment", "Setting up OTP input handling");
        TextWatcher otpTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    Log.d("VerifyEmailFragment", "OTP digit entered: " + s.toString());
                    if (otpEd1.isFocused()) {
                        otpEd2.requestFocus();
                    } else if (otpEd2.isFocused()) {
                        otpEd3.requestFocus();
                    } else if (otpEd3.isFocused()) {
                        otpEd4.requestFocus();
                    } else if (otpEd4.isFocused()) {
                        // Auto-submit when last digit is entered
                        if (validateInputs()) {
                            callVerifyEmailAPI();
                        }
                    }
                }
            }
        };

        // Add backspace handling
        View.OnKeyListener backspaceListener = (v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                EditText currentEditText = (EditText) v;
                if (currentEditText.getText().length() == 0) {
                    if (currentEditText == otpEd4) {
                        otpEd3.requestFocus();
                        otpEd3.setSelection(otpEd3.getText().length());
                    } else if (currentEditText == otpEd3) {
                        otpEd2.requestFocus();
                        otpEd2.setSelection(otpEd2.getText().length());
                    } else if (currentEditText == otpEd2) {
                        otpEd1.requestFocus();
                        otpEd1.setSelection(otpEd1.getText().length());
                    }
                    return true;
                }
            }
            return false;
        };

        otpEd1.addTextChangedListener(otpTextWatcher);
        otpEd2.addTextChangedListener(otpTextWatcher);
        otpEd3.addTextChangedListener(otpTextWatcher);
        otpEd4.addTextChangedListener(otpTextWatcher);

        otpEd1.setOnKeyListener(backspaceListener);
        otpEd2.setOnKeyListener(backspaceListener);
        otpEd3.setOnKeyListener(backspaceListener);
        otpEd4.setOnKeyListener(backspaceListener);

        // Set input type to number for all OTP fields
        otpEd1.setInputType(InputType.TYPE_CLASS_NUMBER);
        otpEd2.setInputType(InputType.TYPE_CLASS_NUMBER);
        otpEd3.setInputType(InputType.TYPE_CLASS_NUMBER);
        otpEd4.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * Validates the OTP input fields.
     * Shows an error message if validation fails.
     *
     * @return true if OTP is valid, false otherwise
     */
    private boolean validateInputs() {
        if (!validateOTP()) {
            showErrorMessage(getContext().getString(R.string.please_enter_valid_otp));
            return false;
        }
        return true;
    }

    /**
     * Checks if the OTP entered is 4 digits and numeric.
     *
     * @return true if OTP is valid, false otherwise
     */
    private boolean validateOTP() {
        String otp = otpEd1.getText().toString() +
                otpEd2.getText().toString() +
                otpEd3.getText().toString() +
                otpEd4.getText().toString();
        return otp.length() == 4 && otp.matches("\\d+");
    }

    /**
     * Calls the API to verify the user's email using the entered OTP.
     * Handles API response, errors, and updates the UI accordingly.
     * Includes null checks and robust exception handling.
     */
    private void callVerifyEmailAPI() {
        CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.BRIHAT_KUNDLI_OTP_VERIFY_BTN_CLICK_EVENT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "From_Shortcut");


        Context context = getContext();
        if (context == null || !isAdded()) {
            Log.e("VerifyEmailFragment", "Context is null or fragment not added");
            return;
        }
        if (userEmail == null) {
            showErrorMessage(context.getString(R.string.email_not_found));
            return;
        }
        CustomProgressDialog pd = new CustomProgressDialog(context);
        pd.setCancelable(false);
        pd.show();
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Log.d("VerifyEmailFragment", "Initiating verify email API call for: " + userEmail);
        Call<ResponseBody> call = api.verifyEmailByOtp(getOTPParams());
        call.enqueue(new Callback<ResponseBody>() {
            /**
             * Handles the API response for OTP verification.
             * Includes null checks and exception handling for safe UI updates.
             */
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Context context = getContext();
                try {
                    if (pd != null && pd.isShowing()) pd.dismiss();
                    if (context == null || !isAdded()) return;
                    if (response.isSuccessful() && response.body() != null) {
                        String myResponse = response.body().string();
                        Log.e("InputEmail", "onResponse: " + myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            // Success: OTP verified, navigate to Thank You screen
                            com.ojassoft.astrosage.utils.CUtils.saveAstroshopUserEmail(context, userEmail);
                            showSuccessMessage();
                            android.app.Activity activity = getActivity();
                            if (activity instanceof com.ojassoft.astrosage.ui.act.BrihatKundliDownload) {
                                CUtils.saveBooleanData(context, CGlobalVariables.FREE_BRIHAT_ALREADY_DOWNLOADED_KEY, true);
                                ((com.ojassoft.astrosage.ui.act.BrihatKundliDownload) activity).navigateToThankYouFragment(BrihatKundliDownload.priceInRs);
                            } else {
                                Toast.makeText(context, "Verification screen not available.", Toast.LENGTH_SHORT).show();
                            }
                        } else if (status.equalsIgnoreCase("100")) {
                            // Special status: trigger background login service
                            LocalBroadcastManager.getInstance(context).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                            try {
                                if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(context)) {
                                    Intent intent = new Intent(context, Loginservice.class);
                                    context.startService(intent);
                                }
                            } catch (Exception e) {
                                Log.e("VerifyEmailFragment", "Error starting Loginservice: " + e.getMessage());
                            }
                        } else if (status.equalsIgnoreCase("0")) {//wrong otp
                            showErrorMessage(context.getString(R.string.otp_is_wrong));
                        } else if (status.equalsIgnoreCase("2")) {//blank or incomplete otp
                            showErrorMessage(context.getString(R.string.please_enter_valid_otp));
                        }
                    } else {
                        // Failure: server returned error or empty body
                        showErrorMessage(context.getString(R.string.verification_failed_please_try_again));
                    }
                } catch (Exception e) {
                    // Handle any parsing or runtime exceptions
                    if (context != null && isAdded()) {
                        showErrorMessage("Response error: " + e.getMessage());
                    }
                    Log.e("VerifyEmailFragment", "onResponse: exception --" + e.getMessage());
                }
            }

            /**
             * Handles network failure for the OTP verification API call.
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                if (pd != null && pd.isShowing()) pd.dismiss();
                if (getContext() != null && isAdded()) {
                    showErrorMessage("Network error. Please check your connection.");
                }
                Log.e("VerifyEmailFragment", "OTP verification network error: " + throwable.getMessage());
            }
        });
    }

    /**
     * Prepares the parameters required for the OTP verification API call.
     * Includes null checks for required data.
     *
     * @return Map of parameters for the API call
     */
    private Map<String, String> getOTPParams() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("otp", getOTPString());
        headers.put("email", userEmail != null ? userEmail : "");
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID,com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(getContext()));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION,
                BuildConfig.VERSION_NAME);
        try {
            headers.put("key", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(requireActivity()));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME,
                    com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(getActivity()));
        } catch (Exception e) {
            Log.e("VerifyEmailFragment", "Error getting app signature/package name: " + e.getMessage());
        }
        return headers;
    }

    /**
     * Concatenates the OTP digits from the input fields.
     * Includes null checks for EditTexts.
     *
     * @return String representation of the entered OTP
     */
    private String getOTPString() {
        StringBuilder sb = new StringBuilder();
        if (otpEd1 != null) sb.append(otpEd1.getText().toString());
        if (otpEd2 != null) sb.append(otpEd2.getText().toString());
        if (otpEd3 != null) sb.append(otpEd3.getText().toString());
        if (otpEd4 != null) sb.append(otpEd4.getText().toString());
        return sb.toString();
    }

    private void startResendOtpCountdown() {
        if (txtViewResendOtp == null || llResendOTPLayout == null) {
            return;
        }

        llResendOTPLayout.setEnabled(false);
        llResendOTPLayout.setAlpha(0.5f);
        txtViewResendOtp.setText(getString(R.string.resend_sms_in, "01:00"));

        countDownTimer = new CountDownTimer(TOTAL_VERIFICATION_TIME, ONE_SECOND) {
            @SuppressLint("StringFormatInvalid")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSeconds = (millisUntilFinished + 999) / 1000;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                String text = String.format(Locale.US, "%02d:%02d", minutes, seconds);
                txtViewResendOtp.setText(getString(R.string.resend_otp_in, text));
            }

            @Override
            public void onFinish() {
                llResendOTPLayout.setEnabled(true);
                llResendOTPLayout.setAlpha(1f);
                // Set the text with underline formatting
                txtViewResendOtp.setText(Html.fromHtml(getResources().getString(R.string.resend_otp)));
                otpEd1.requestFocus();
            }
        }.start();

        llResendOTPLayout.setOnClickListener(v -> {
            if (llResendOTPLayout.isEnabled()) {
                // TODO: Implement resend OTP API call
                startResendOtpCountdown();
                callAPIForResend();
            }
        });
    }

    private void showErrorMessage(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessMessage() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Email verified successfully", Toast.LENGTH_SHORT).show();
        }
    }


    private void callAPIForResend() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Context context = getContext();
        Call<ResponseBody> call = api.registerEmailForFreeReport(getResendOtpParams());
        call.enqueue(new Callback<ResponseBody>() {
            /**
             * Handles the API response for email registration.
             * Includes null checks and exception handling for safe UI updates.
             */
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!isAdded() || context == null) return;
                    if (response.isSuccessful() && response.body() != null) {
                        String respString = response.body().string();
                        JSONObject jsonObject = new JSONObject(respString);
                        String status = jsonObject.getString("status");
                        android.app.Activity activity = getActivity();
                        if (status.equalsIgnoreCase("1")) {
                            //otp resend successfully
                        } else if (status.equals("0")) {//please try again
                            Toast.makeText(context, context.getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                        } else if (status.equals("2")) { //spam attempt
                            Toast.makeText(context, context.getString(R.string.spam_attemp), Toast.LENGTH_SHORT).show();
                        } else if (status.equals("3")) { //Already downloaded
                            Toast.makeText(context, context.getString(R.string.already_downloaded), Toast.LENGTH_SHORT).show();
                            if (activity instanceof com.ojassoft.astrosage.ui.act.BrihatKundliDownload) {
                                com.ojassoft.astrosage.utils.CUtils.saveBooleanData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FREE_BRIHAT_ALREADY_DOWNLOADED_KEY, true);
                                com.ojassoft.astrosage.utils.CUtils.saveBooleanData(context, EMAIL_DELIVERED_KEY, true);
                                ((com.ojassoft.astrosage.ui.act.BrihatKundliDownload) activity).navigateToThankYouFragment(BrihatKundliDownload.priceInRs);
                            }
                        } else if (status.equals("4")) { //wait for 24 hours after installing
                            Toast.makeText(context, context.getString(R.string.please_wait_for_24_hrs_after_install), Toast.LENGTH_SHORT).show();
                        } else if (status.equals("5")) { //incorrect birth details
                            Toast.makeText(context, context.getString(R.string.incorrect_birth_details), Toast.LENGTH_SHORT).show();
                        } else if (status.equals("6")) { //incorrect email
                            Toast.makeText(context, context.getString(R.string.incorrect_email), Toast.LENGTH_SHORT).show();
                        } else if (status.equals("7")) { //Too many attempts
                            Toast.makeText(context, context.getString(R.string.too_many_attempts), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Failure: server returned error or empty body
                        Toast.makeText(context, context.getString(R.string.failed_to_send_code_please_try_again), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Handle any parsing or runtime exceptions
                    if (context != null && isAdded()) {
                        Toast.makeText(context, "Response error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.e("InputEmail", "onResponse: exception --" + e.getMessage());
                }
            }

            /**
             * Handles network failure for the email registration API call.
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                if (context != null && isAdded()) {
                    // Show network error as toast
                    Toast.makeText(context, "Network error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("InputEmail", "onFailure: " + throwable.getMessage());
            }
        });

    }


    private Map<String, String> getResendOtpParams() {
        HashMap<String, String> headers = new HashMap<>();
        // Retrieve BeanHoroPersonalInfo from shared preferences
        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) com.ojassoft.astrosage.utils.CUtils.getCustomObject(requireActivity());
        if (beanHoroPersonalInfo == null) return headers;
        headers.put("email", userEmail);
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

        headers.put("isverified", "0");

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
        headers.put("phoneno", com.ojassoft.astrosage.varta.utils.CUtils.getUserID(requireActivity()));
        headers.put("countrycode", com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(requireActivity()));
        headers.put("usersex", beanHoroPersonalInfo.getGender());
        headers.put("username", beanHoroPersonalInfo.getName());
        headers.put("userid", com.ojassoft.astrosage.utils.CUtils.getUserName(requireActivity()));
        headers.put("useridas", getUserIdForBlock(requireActivity()));
//        Log.e("mytag", "inputMail: " + com.ojassoft.astrosage.BuildConfig.VERSION_NAME);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(getContext()));
        headers.put("key", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(requireActivity()));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID,com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(requireActivity()));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, com.ojassoft.astrosage.BuildConfig.VERSION_NAME);
        return headers;
    }


    private void initEditTextPasteListner() {
        otpEd1.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
        otpEd2.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
        otpEd3.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
        otpEd4.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
    }

    private void onOtpPaste() {
        try {
            String otpStr = getCopiedOtpFromClipboard();
            autoFillOtp(otpStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void autoFillOtp(String otp) {
        try {
            otpEd1.setText(String.valueOf(otp.charAt(0)));
            otpEd2.setText(String.valueOf(otp.charAt(1)));
            otpEd3.setText(String.valueOf(otp.charAt(2)));
            otpEd4.setText(String.valueOf(otp.charAt(3)));
            callVerifyEmailAPI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getCopiedOtpFromClipboard() {
        try {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null && clipboard.hasPrimaryClip()) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                String pasteOtp = item.getText().toString();
                if (!TextUtils.isEmpty(pasteOtp) && pasteOtp.length() == 4) {
                    return pasteOtp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
//}
}