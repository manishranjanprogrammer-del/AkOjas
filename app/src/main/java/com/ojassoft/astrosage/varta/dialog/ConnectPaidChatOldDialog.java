package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.utils.CGlobalVariables.PERSONALIZED_AI_NOTIFICATION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

public class ConnectPaidChatOldDialog extends DialogFragment {
    private ConnectPaidChatDialog.IConnectPaidChat connectPaidChat;
    private AstrologerDetailBean astrologerDetailBean;
    private String message;
    View view;

    public ConnectPaidChatOldDialog() {}

    public ConnectPaidChatOldDialog(ConnectPaidChatDialog.IConnectPaidChat connectPaidChat, AstrologerDetailBean astrologerDetailBean, String message) {
        this.connectPaidChat = connectPaidChat;
        this.astrologerDetailBean = astrologerDetailBean;
        this.message = message;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        setCancelable(false);

        view = inflater.inflate(R.layout.connect_paid_chat_old_dialog,container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView ivCPCClose = view.findViewById(R.id.ivCPCClose);
        TextView tvCPCText = view.findViewById(R.id.tvCPCText);
        Button btnCPCNo = view.findViewById(R.id.btnCPCNo);
        Button btnCPCYes = view.findViewById(R.id.btnCPCYes);

        if (astrologerDetailBean == null) {
            btnCPCNo.setVisibility(View.GONE);
            if (!message.equals(getString(R.string.already_in_chat))) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_USER_ALREADY_IN_CHAT_DIALOG_SHOWN, PERSONALIZED_AI_NOTIFICATION,"");
                message = requireActivity().getString(R.string.notification_not_available);
            }
            tvCPCText.setText(message);
            btnCPCYes.setText(getString(R.string.ok));
        } else {

            String priceToShow = astrologerDetailBean.getServicePrice();
            String text = getString(R.string.start_paid_chat_text);

            String currentOfferType = CUtils.getUserIntroOfferType(requireContext());

            if (astrologerDetailBean.getUseIntroOffer()) {
                if (currentOfferType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    text = getString(R.string.continue_this_chat_with_expert_insights_free, astrologerDetailBean.getName());
                }
            }

            text = text.replace("~~", priceToShow);
            text = text.replace("@@", astrologerDetailBean.getName());
            tvCPCText.setText(text);
        }

        ivCPCClose.setOnClickListener(v -> dismiss());
        btnCPCNo.setOnClickListener(v -> dismiss());

        btnCPCYes.setOnClickListener(v -> {
            com.ojassoft.astrosage.utils.CUtils.createSession(getContext(), CGlobalVariables.AI_NOTIFICATION_CHAT_PARTER_ID);
            dismiss();
            if (astrologerDetailBean == null) {
                if (message.equals(getString(R.string.already_in_chat))) {
                    dismiss();
                } else {
                    connectPaidChat.closeChatWindow();
                }
            } else {
                connectPaidChat.connectPaidChat(message);
            }
        });

        return view;
    }
}
