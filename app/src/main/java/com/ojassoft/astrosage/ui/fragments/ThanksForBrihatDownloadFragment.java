package com.ojassoft.astrosage.ui.fragments;

import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.BrihatKundliDownload;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Fragment displayed after a successful Brihat Kundli download.
 * Shows options to share the app, return to home, or close the flow.
 * Handles robust navigation and user action feedback.
 */
public class ThanksForBrihatDownloadFragment extends Fragment {
    public static final String EMAIL_DELIVERED_KEY= "email_delivered_key"; //for changing msg once email delivered...assuming on second time user opens this screen email was already delivered
    private OnBackPressedCallback backPressedCallback;
    private BrihatKundliDownload brihatKundliDownloadActivity;

    LinearLayout shareBtnLayout, homeBtnLayout;
    TextView emailDeliveredMsg;
    ImageView backArrow;
    String priceInRs;

    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.FREE_BRIHAT_KUNDLI_Thanks_PAGE_OPEN_EVENT, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "From_Shortcut");
        View view = inflater.inflate(R.layout.thankyou_brihat_download_layout, container, false);
        shareBtnLayout = view.findViewById(R.id.share_app_btn_layout);
        homeBtnLayout = view.findViewById(R.id.home_btn_layout);
        backArrow = view.findViewById(R.id.back_arrow);
        emailDeliveredMsg = view.findViewById(R.id.email_deliverd_msg);
        // Get price from arguments or activity
        if (getArguments() != null && getArguments().containsKey("priceInRs")) {
            priceInRs = getArguments().getString("priceInRs");
        } else if (brihatKundliDownloadActivity != null) {
            priceInRs = BrihatKundliDownload.priceInRs;
        }

        // Display price if available
        if (priceInRs != null && !priceInRs.isEmpty()) {
            boolean alreadyDelivered = CUtils.getBooleanData(getContext(),EMAIL_DELIVERED_KEY,false);
            if(alreadyDelivered){
                emailDeliveredMsg.setText(brihatKundliDownloadActivity.getResources().getString(R.string.email_already_delivered_msg,priceInRs));
            }else{
                emailDeliveredMsg.setText(brihatKundliDownloadActivity.getResources().getString(R.string.email_will_be_sent_msg_for_brihat,priceInRs));
                CUtils.saveBooleanData(getContext(),EMAIL_DELIVERED_KEY,true);
            }
        }




        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupBtnClicks();
    }

    /**
     * Sets up click listeners for all actionable UI elements in the fragment.
     * Handles sharing, home navigation, and closing the flow with robust null and exception checks.
     */
    private void setupBtnClicks() {
        // Share button: invokes the app's share-to-friend functionality
        shareBtnLayout.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.SHARE_APP_BTN_FROM_BRIHAT_THANKS_PAGE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "From_Shortcut");
            try {
                if (getContext() != null) {
                    CUtils.shareToFriendMail(getContext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Home button: navigates to the app's main module, clearing the back stack
        homeBtnLayout.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.HOME_BTN_FROM_BRIHAT_THANKS_PAGE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "From_Shortcut");
            try {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), ActAppModule.class);
                    // These flags ensure all previous activities are cleared
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Back arrow: simply closes the current activity (should exit the flow)
        backArrow.setOnClickListener(v -> {
            try {
                if (getActivity() != null) {
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            } catch (Exception e) {
                // Log any error during activity finish
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        if(activity instanceof BrihatKundliDownload){
            brihatKundliDownloadActivity = (BrihatKundliDownload) activity;
        }
    }
}
