package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.utils.CGlobalVariables.PERSONALIZED_AI_NOTIFICATION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_AI_CALL_SHOW_IN_NOTIFICATION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

public class ConnectPaidChatDialog extends DialogFragment {
    private IConnectPaidChat connectPaidChat;
    private AstrologerDetailBean astrologerDetailBean;
    private String message;
    View view;

    public ConnectPaidChatDialog() {}

    public ConnectPaidChatDialog(IConnectPaidChat connectPaidChat, AstrologerDetailBean astrologerDetailBean, String message) {
        this.connectPaidChat = connectPaidChat;
        this.astrologerDetailBean = astrologerDetailBean;
        this.message = message;
    }

    public interface IConnectPaidChat {
        void connectPaidChat(String message);
        void closeChatWindow();
        void connectPaidCall();
    }

    @Override
    public void onStart() {
        super.onStart();

        if ( getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();

            // ✅ Transparent background behind the rounded corners
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // ✅ Adjust width with margins (space start & end)
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90); // 90% of screen width
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        setCancelable(false);

        view = inflater.inflate(R.layout.fragment_dialog_connect_paid_chat,container);


        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ImageView ivCPCClose = view.findViewById(R.id.ivCPCClose);
        TextView tvCPCText = view.findViewById(R.id.tvCPCText);
        TextView btnChat = view.findViewById(R.id.btnChat);
        TextView btnTalk = view.findViewById(R.id.btnTalk);
        Button btnCPCNo = view.findViewById(R.id.btnCPCNo);
        Button btnCPCYes = view.findViewById(R.id.btnCPCYes);
        RelativeLayout btnChatRL = view.findViewById(R.id.btnChatRL);
        RelativeLayout btnCallRL = view.findViewById(R.id.btnCallRL);

        FontUtils.changeFont(requireContext(), btnChat, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(requireContext(), btnTalk, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        boolean isAiCallShowInNotification = CUtils.getBooleanData(requireContext(), IS_AI_CALL_SHOW_IN_NOTIFICATION, false);



        if (astrologerDetailBean == null) {
            btnCPCNo.setVisibility(View.GONE);
            if (!message.equals(getString(R.string.already_in_chat))) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_USER_ALREADY_IN_CHAT_DIALOG_SHOWN, PERSONALIZED_AI_NOTIFICATION,"");
                message = requireActivity().getString(R.string.notification_not_available);
            }
            tvCPCText.setText(message);
            btnCPCYes.setText(getString(R.string.ok));
        } else {

            if (isAiCallShowInNotification && astrologerDetailBean.isAvailableForCallBool()){
                btnCallRL.setVisibility(View.VISIBLE);
            } else {
                btnCallRL.setVisibility(View.GONE);
            }
            String priceToShow = astrologerDetailBean.getServicePrice();
            //String text = getString(R.string.start_paid_chat_text);
            String text = getString(R.string.do_you_want_expert_advice);


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

//        btnCPCYes.setOnClickListener(v -> {
//            com.ojassoft.astrosage.utils.CUtils.createSession(getContext(), CGlobalVariables.AI_NOTIFICATION_CHAT_PARTER_ID);
//            dismiss();
//            if (astrologerDetailBean == null) {
//                if (message.equals(getString(R.string.already_in_chat))) {
//                    dismiss();
//                } else {
//                    connectPaidChat.closeChatWindow();
//                }
//            } else {
//                connectPaidChat.connectPaidChat(message);
//            }
//        });

        btnChatRL.setOnClickListener(v -> {
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

        btnCallRL.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_CONNECT_PAID_CALL_BUTTON_CLICKED, FIREBASE_EVENT_ITEM_CLICK, "");
            if(astrologerDetailBean != null){
                astrologerDetailBean.setCallSource(CGlobalVariables.AI_NOTIFICATION_POPUP_DIALOG_CALL_BTN);
            }
            connectPaidChat.connectPaidCall();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ColorDrawable bgColor = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.bg_card_view_color));
        InsetDrawable inset = new InsetDrawable(bgColor, 50);
        getDialog().getWindow().setBackgroundDrawable(inset);
    }
}
