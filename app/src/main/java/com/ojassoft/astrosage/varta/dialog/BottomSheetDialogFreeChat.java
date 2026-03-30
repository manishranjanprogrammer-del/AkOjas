package com.ojassoft.astrosage.varta.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.fragments.HomeFragment1;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

public class BottomSheetDialogFreeChat extends BottomSheetDialogFragment {

    public BottomSheetDialogFreeChat(){
        //
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
//        LANGUAGE_CODE = VartaUserApplication.getAppContext().getLanguageCode();
//        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View v = inflater.inflate(R.layout.fragment_bottom_sheet_free_pop_up,
                container, false);
        inti(v);
        return v;
    }

    private void inti(View v) {
        Button openNow = v.findViewById(R.id.open_now);
        TextView mMessageTv = v.findViewById(R.id.message_tv);
        TextView idFree = v.findViewById(R.id.id_free);
        TextView idChat = v.findViewById(R.id.id_chat);
        TextView idFirst = v.findViewById(R.id.id_first);
        FontUtils.changeFont(getContext(), openNow, "fonts/Roboto-Bold.ttf");
        FontUtils.changeFont(getContext(), mMessageTv, "fonts/Roboto-Bold.ttf");
        FontUtils.changeFont(getContext(), idFree, "fonts/Roboto-Black.ttf");
        FontUtils.changeFont(getContext(), idChat, "fonts/Roboto-Black.ttf");
        FontUtils.changeFont(getContext(), idFirst, "fonts/Roboto-Black.ttf");

        ImageView crossButton = v.findViewById(R.id.cross_btn);
        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        openNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof DashBoardActivity) {
                    AstrosageKundliApplication.apiCallingSource = CGlobalVariables.POP_UNDER_FREE_CHAT;
                    ((DashBoardActivity) getContext()).popUnderClicked();
                    AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                    CUtils.fcmAnalyticsEvents("pop_under_free_chat_now", AstrosageKundliApplication.currentEventType, "");

                }
                dismiss();
            }
        });
        CUtils.setPrefNextOffer(getActivity(), CUtils.getCurrentDateInString());
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("pop_under_free_chat_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }
}
