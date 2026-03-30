package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

/**
 * SubscriptionPaymentFailDialog
 * <p>
 * Requirements:
 * - This dialog is shown when a user's subscription payment fails or is cancelled.
 * - The dialog informs the user about the payment failure and suggests an alternative payment method.
 * - If the payment was attempted through Razorpay, the dialog suggests trying Google Payment.
 * - If the payment was attempted through Google Payment, the dialog suggests trying Razorpay.
 * - The paymentTypeIs parameter must be passed to indicate which payment method failed.
 * - Supported payment types: CGlobalVariables.RAZORPAY and CGlobalVariables.GOOGLE
 * - The dialog provides visual feedback with appropriate error messages.
 * - Analytics events are logged to track user interaction with the dialog.
 * <p>
 * Usage:
 * // Create the dialog with the payment method that failed
 * SubscriptionPaymentFailDialog dialog = new SubscriptionPaymentFailDialog(CGlobalVariables.RAZORPAY);
 * 
 * // Optional: Set action listener to handle callbacks
 * dialog.setOnDialogActionListener(new SubscriptionPaymentFailDialog.OnDialogActionListener() {
 *     @Override
 *     public void onDialogAction() {
 *         // Handle dialog action (e.g., attempt payment with alternative method)
 *     }
 * });
 * 
 * // Show the dialog
 * dialog.show(fragmentManager, "payment_fail_dialog");
 */
public class SubscriptionPaymentFailDialog extends DialogFragment implements View.OnClickListener {

    // Stores which payment method failed (Razorpay or Google)
    private String paymentTypeIs = "";

    // Reference to the host activity
    private Activity activity;

    // UI elements for the dialog
    private TextView oopsTV, rechargeUnsuccessfulTV, text1, text2, descTV;
    private LinearLayout try_again_button;

    /**
     * Constructor for the dialog
     *
     * @param paymentTypeIs The payment method that failed (use CGlobalVariables.RAZORPAY or CGlobalVariables.GOOGLE)
     */
    public SubscriptionPaymentFailDialog(String paymentTypeIs) {
        this.paymentTypeIs = paymentTypeIs;
    }

    public SubscriptionPaymentFailDialog() {
    }

    /**
     * Interface to handle dialog actions
     * <p>
     * This interface allows the host activity or fragment to receive callbacks
     * when the user interacts with the dialog, specifically when they choose
     * to try an alternative payment method.
     */
    public interface OnDialogActionListener {
        /**
         * Called when the user clicks on the "Try Again" button or related text
         * The host activity can implement this method to handle the retry action,
         * typically by initiating payment with the alternative gateway
         */
        void onDialogAction(); // You can pass parameters if needed
    }

    private OnDialogActionListener listener;

    /**
     * Sets the listener for dialog action events
     *
     * @param listener The listener implementation that will receive callbacks when
     *                 the user interacts with the dialog
     */
    public void setOnDialogActionListener(OnDialogActionListener listener) {
        this.listener = listener;
    }

    /**
     * Creates and returns the view hierarchy associated with the dialog
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Set language code for localization
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        // Inflate the dialog layout
        View view = inflater.inflate(R.layout.sub_failed_payment_dialog, container);

        // Configure dialog window (with null checks for safety)
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Remove the title bar
            Log.d("TestDialogC, ", "onCreateView: ");
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Initialize all UI elements
        oopsTV = view.findViewById(R.id.heading);
        rechargeUnsuccessfulTV = view.findViewById(R.id.recharge_unsucessful);

        descTV = view.findViewById(R.id.recharge_unsucessful_content);
        try_again_button = view.findViewById(R.id.try_again_button);
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text2.setText(Html.fromHtml(getResources().getString(R.string.with_other_payment_gateway)));
        ImageView cancelBtn = view.findViewById(R.id.iv_close_view);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // Apply custom fonts to all text elements
        FontUtils.changeFont(getActivity(), oopsTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), rechargeUnsuccessfulTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), descTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), text2, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), text1, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        // Set click listener for the done button
        try_again_button.setOnClickListener(this);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);

        return view;
    }

    /**
     * Called when the dialog is cancelled
     * Logs the cancellation event to Firebase analytics
     */
    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("payment_failed_dialog_cancelled", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
    }

    /**
     * Called when the dialog is starting
     * Sets the dialog dimensions and background
     */
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            // Get the screen width
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            // Set dialog width to 85% of screen width
            int dialogWidth = (int) (screenWidth * 0.90);

            // Set the dialog width to 85% of screen width and height to wrap content
            dialog.getWindow().setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    /**
     * Called when the fragment is attached to its context
     * Stores a reference to the activity for later use
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
    }

    /**
     * Called when the fragment is detached from its context
     * Cleans up the activity reference to prevent memory leaks
     */
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    /**
     * Handles button click events in the dialog
     * <p>
     * This method handles user interactions with the dialog buttons:
     * - Try Again button: Allows the user to attempt payment with a different method
     * - Text1/Text2: Alternative options for retrying payment
     * <p>
     * Logs the appropriate analytics event based on which payment method failed,
     * notifies the listener if registered, and dismisses the dialog.
     *
     * @param v The view that was clicked
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.try_again_button || v.getId() == R.id.text1 || v.getId() == R.id.text2) {
            // Log different events based on which payment method failed
            if (paymentTypeIs.equals(CGlobalVariables.RAZORPAY)) {
                CUtils.fcmAnalyticsEvents("payment_failed_dialog_razorpay", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            } else {
                CUtils.fcmAnalyticsEvents("payment_failed_dialog_google", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            }
            if (listener != null) {
                listener.onDialogAction();
            }
            // Close the dialog
            dismiss();
        }
    }
}
