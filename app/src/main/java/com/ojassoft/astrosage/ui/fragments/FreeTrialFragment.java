package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity.PLATINUM_PLAN_MONTHLY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;

import android.content.Context;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.core.content.ContextCompat;

import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IPurchasePlan;
import com.ojassoft.astrosage.ui.act.PurchaseDhruvPlanActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UPIAppChecker;
import com.ojassoft.astrosage.varta.adapters.UPIAppAdapter;
import com.ojassoft.astrosage.varta.model.UPIAppModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A Fragment that displays a Free Trial offer to the user.
 * <p>
 * This fragment handles the UI for a 7-day free trial (or variable period),
 * showing the amount to be paid after the trial ends and the date when the
 * billing will commence.
 */
public class FreeTrialFragment extends DialogFragment {

    // Bundle argument keys
    private static final String ARG_FREE_TRIAL_PERIOD = "free_trial_period";
    private static final String ARG_AMOUNT_TO_PAY = "amount_to_pay";
    private static final String ARG_ORIGINAL_PRICE = "original_price";

    // Data variables
    private int freeTrialPeriod;
    private int amountToPay;
    private String originalPrice;

    // Interface callback for plan purchase events
    private IPurchasePlan iPurchasePlan;

    // UI Components
    private RelativeLayout subscribeForBtn;
    private Button btnStartSubsPayment;
    private TextView free_trial_tv;
    private TextView tvRepaymentDate;
    private TextView subsBeginTV;
    private TextView tv_btn_txt;

    private TextView tvTrialByPaying,tvSelectedAppName;
    private ImageView ivCloseBtn,ivSelectedAppIcon;
    private Spinner spinnerUpiApps;
    private boolean userSelect = false;
    UPIAppModel selectedUPIAppModel;
    private List<UPIAppModel> masterList;


    private ImageView  imgHeading;
    private TextView tvHeading;
    private ConstraintLayout phonePePaymentLayout;
    Context context;
    private boolean isPhonePeSubscriptionEnabled;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;


    /**
     * Required empty public constructor.
     */
    public FreeTrialFragment() {
        // Fragments must have an empty constructor
    }

    /**
     * Sets the listener for purchase plan actions.
     * <p>
     * Note: This must be called after creating the fragment instance as interfaces
     * cannot be passed via Bundle arguments.
     *
     * @param iPurchasePlan The implementation of IPurchasePlan interface.
     */
    public void setPurchasePlanListener(IPurchasePlan iPurchasePlan) {
        this.iPurchasePlan = iPurchasePlan;
    }

    /**
     * Factory method to create a new instance of this fragment.
     *
     * @param freeTrialPeriod The duration of the trial in days (e.g., 7).
     * @param amountToPay     The amount that will be charged after the trial.
     * @return A new instance of FreeTrialFragment.
     */
    public static FreeTrialFragment newInstance(int freeTrialPeriod, int amountToPay,String originalPrice) {
        FreeTrialFragment fragment = new FreeTrialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FREE_TRIAL_PERIOD, freeTrialPeriod);
        args.putInt(ARG_AMOUNT_TO_PAY, amountToPay);
        args.putString(ARG_ORIGINAL_PRICE, originalPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("DebugTrial", "FreeTrialFragment onCreate");
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.transparentdialog);
        LANGUAGE_CODE = CUtils.getLanguageCode(context);

        // Retrieve arguments passed during instance creation
        if (getArguments() != null) {
            freeTrialPeriod = getArguments().getInt(ARG_FREE_TRIAL_PERIOD);
            amountToPay = getArguments().getInt(ARG_AMOUNT_TO_PAY);
            originalPrice = getArguments().getString(ARG_ORIGINAL_PRICE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.free_trial_kundli_ai_plus_layout, container, false);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FREE_TRAIL_INFO_SCREEN_SHOWN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
        isPhonePeSubscriptionEnabled = CUtils.getBooleanData(getActivity(), com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISPHONEPESUBSCRIPTIONENABLED, true);
        if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(getActivity()).equalsIgnoreCase(COUNTRY_CODE_IND)) {
            isPhonePeSubscriptionEnabled = false;
        }
            // Initialize UI components and set data
            initUI(view);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            // Setup click listeners
            initListeners(view);
            return view;
        }

    /**
     * Initializes click listeners for interactive elements.
     */
    private void initListeners(View view) {
        // Handle Subscribe Button click
        LinearLayout llSelectedAppName = view.findViewById(R.id.llSelectedAppName);
        spinnerUpiApps = view.findViewById(R.id.spinnerApps);
        ivSelectedAppIcon = view.findViewById(R.id.ivSelectedAppIcon);
        tvSelectedAppName = view.findViewById(R.id.tvSelectedAppName);

        masterList = Arrays.asList(
                new UPIAppModel(getActivity().getResources().getString(R.string.phonepe), R.drawable.ic_phonpe_icon, UPIAppChecker.PACKAGE_PHONEPE,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PHONE),
                new UPIAppModel(getActivity().getResources().getString(R.string.gpay), R.drawable.ic_gpay_icon, UPIAppChecker.PACKAGE_GOOGLE_PAY,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_GPAY),
                new UPIAppModel(getActivity().getResources().getString(R.string.paytm), R.drawable.ic_paytm_icon, UPIAppChecker.PACKAGE_PAYTM,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PAYTM),
                new UPIAppModel(getActivity().getResources().getString(R.string.bhim), R.drawable.ic_bhim_icon, UPIAppChecker.PACKAGE_BHIM,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_BHIM),
                //new UPIAppModel(getActivity().getResources().getString(R.string.amazon_pay), R.drawable.ic_amazon_icon, UPIAppChecker.PACKAGE_AMAZON,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_AMAZON),
                new UPIAppModel(getActivity().getResources().getString(R.string.cred), R.drawable.ic_cred_icon, UPIAppChecker.PACKAGE_CRED,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_CRED));

        // 2) Filter based on installed packages on device
        List<UPIAppModel> installedApps = UPIAppChecker.getInstalledAndReadyUpiApps(getActivity(),masterList);
        // 3) Create adapter and set to spinner
        installedApps.add(new UPIAppModel(getActivity().getResources().getString(R.string.razorpay), R.drawable.ic_razorpay_icon, "com.razorpay",com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY));
        UPIAppAdapter adapter = new UPIAppAdapter(
                getActivity(),
                R.layout.row_upi_app,
                R.layout.row_upi_app,
                installedApps
        );
        spinnerUpiApps.setAdapter(adapter);
        // 4 Listen for selection
        spinnerUpiApps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true; // if you want to ignore initial automatic selection callback
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // if (first) { first = false; return; } // optional
                selectedUPIAppModel = (UPIAppModel) parent.getItemAtPosition(position);
                onUpiAppSelected(selectedUPIAppModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });

        llSelectedAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Optionally set flag so performClick counts as user action
                userSelect = true;
                spinnerUpiApps.performClick();
//                Intent intent = new Intent(PurchaseAiConsultationPlan.this, SubscriptionPaymentActivity.class);
//                intent.putExtra("service_model", servicelistModal);
//                intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_PARAMS, source);
//                startActivity(intent);
            }
        });
        subscribeForBtn.setOnClickListener(v -> {
            if (iPurchasePlan != null) {
                // Trigger the monthly platinum plan selection via the interface
                iPurchasePlan.selectedPlan(PLATINUM_PLAN_MONTHLY,selectedUPIAppModel);
            }
        });
        btnStartSubsPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iPurchasePlan != null) {
                    // Trigger the monthly platinum plan selection via the interface
                    iPurchasePlan.selectedPlan(PLATINUM_PLAN_MONTHLY,selectedUPIAppModel);
                }
            }
        });
        // Handle Close/Dismiss Button click
        ivCloseBtn.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Remove this fragment from the transaction manager
                getActivity().getSupportFragmentManager().beginTransaction().remove(FreeTrialFragment.this).commit();
            }
        });
    }
    private void onUpiAppSelected(UPIAppModel app) {
        // Called when user selects an app. Do whatever you need (save selection, start payment intent, etc.)
        //Toast.makeText(getActivity(), "Selected: " + app.getName() + " (" + app.getPackageName() + ")", Toast.LENGTH_SHORT).show();
        tvSelectedAppName.setText(app.getName());
        ivSelectedAppIcon.setImageResource(app.getIconResId());

    }
    /**
     * Finds views by ID and populates them with dynamic text.
     *
     * @param view The root view of the fragment.
     */
    private void initUI(View view) {
        // Bind UI elements
        free_trial_tv = view.findViewById(R.id.free_trial_tv);
        tvRepaymentDate = view.findViewById(R.id.tvRepaymentDate);
        tv_btn_txt = view.findViewById(R.id.tv_btn_txt);
        tvTrialByPaying = view.findViewById(R.id.tvTrialByPaying);
        subscribeForBtn = view.findViewById(R.id.subscribeForBtn);
        btnStartSubsPayment = view.findViewById(R.id.btnStartSubsPayment);
        phonePePaymentLayout = view.findViewById(R.id.phonePePaymentLayout);
        ivCloseBtn = view.findViewById(R.id.ivClosePage);
        imgHeading = view.findViewById(R.id.platinumImageview);
        tvHeading = view.findViewById(R.id.tv_heading);
        subsBeginTV = view.findViewById(R.id.auto_pay_time_tv);


        if (LANGUAGE_CODE != 0) {
            imgHeading.setVisibility(View.GONE);
            tvHeading.setVisibility(View.VISIBLE);
        } else {
            imgHeading.setVisibility(View.VISIBLE);
            tvHeading.setVisibility(View.GONE);
        }

        // Set dynamic text values using string resources
        tvTrialByPaying.setText(getString(R.string.pay_deducted_refunded_immediately, String.valueOf(amountToPay)));
        //btnStartSubsPayment.setText(getString(R.string.button_text_with_price, String.valueOf(amountToPay)));
        free_trial_tv.setText(PurchaseDhruvPlanActivity.getFormattedFreeTrialText(context,freeTrialPeriod));
        if(CUtils.is3MonthSubsEnabled(context)){//enabled 3 months subscription
            subsBeginTV.setText(getResources().getString(R.string.subscription_starts_on_date,getAutoPayDate(freeTrialPeriod)));
            tvRepaymentDate.setText(getFormattedAutoPayFor3MonthString(context,originalPrice));
        }else{
            subsBeginTV.setText(getString(R.string.subscription_begins));
            tvRepaymentDate.setText(getResources().getString(R.string.subscription_begins_on_date_cancel_anytime_before_no_charges_no_penalties,  getAutoPayDate(freeTrialPeriod),originalPrice));
        }
            if(isPhonePeSubscriptionEnabled){
                subscribeForBtn.setVisibility(View.GONE);
                phonePePaymentLayout.setVisibility(View.VISIBLE);
            }else {
                subscribeForBtn.setVisibility(View.VISIBLE);
                phonePePaymentLayout.setVisibility(View.GONE);
            }
    }

    /**
     * Calculates the date 7 days from the current date.
     *
     * @return A formatted string representation of the date (dd-MM-yyyy).
     */
    public static String getAutoPayDate(int autoPayDays) {
        // Get current system time
        Calendar calendar = Calendar.getInstance();

        // Add autoPayDays  to the current date
        calendar.add(Calendar.DAY_OF_MONTH, autoPayDays);

        // Format the date to "dd-MM-yyyy" (e.g., 25-12-2023)
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Set the width and height to MATCH_PARENT
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }
    }


    /**
     * Formats the autopay string with styled and replaced price values.
     *
     * @param context         The context to resolve resources (colors and strings).
     * @param autoPayPrice    The integer price for the autopay offer (e.g., 499).
     * @return A SpannableStringBuilder with formatted text, ready to be set on a TextView.
     */
    public static SpannableStringBuilder getFormattedAutoPayFor3MonthString(Context context, String autoPayPrice) {

        // 1. Get the string template from strings.xml
        String baseString = context.getString(R.string.autopay_at_date);
        String durationText = "/3 months";

        // 2. Prepare the replacement strings
        String rupeeSymbol = "₹";
        String autopayPriceText = rupeeSymbol + autoPayPrice + durationText;   // e.g., "₹499/3 months"
        String originalPriceText = rupeeSymbol + "597" + durationText;  // e.g., "₹597/3 months" assuming 1 month price = 199/

        // 3. Find the placeholder start indices
        int placeholder1_start = baseString.indexOf("#");
        int placeholder2_start = baseString.indexOf("$");

        // 4. Create a SpannableStringBuilder to build the final styled string
        SpannableStringBuilder ssb = new SpannableStringBuilder(baseString);

        // 5. Replace the placeholders and apply spans
        // IMPORTANT: Replace the second placeholder ($) first, because replacing the first (#)
        // will change the string length and invalidate the index of the second placeholder.

        // --- Replace '$' with the strikethrough original price ---
        ssb.replace(placeholder2_start, placeholder2_start + 1, originalPriceText);
        // Apply the strikethrough span to the text we just inserted
        ssb.setSpan(
                new StrikethroughSpan(),
                placeholder2_start,
                placeholder2_start + originalPriceText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // --- Replace '#' with the colored autopay price ---
        // Note: We need to find the index of '#' again in the modified builder.
        placeholder1_start = ssb.toString().indexOf("#");
        ssb.replace(placeholder1_start, placeholder1_start + 1, autopayPriceText);

        // Get your primary color from colors.xml
        int primaryColor = ContextCompat.getColor(context, R.color.colorPrimary_day_night);

        // Apply the color span to the text we just inserted
        ssb.setSpan(
                new ForegroundColorSpan(primaryColor),
                placeholder1_start,
                placeholder1_start + autopayPriceText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return ssb;
    }
}
