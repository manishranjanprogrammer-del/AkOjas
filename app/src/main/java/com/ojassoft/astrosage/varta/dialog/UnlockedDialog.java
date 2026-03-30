package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
public class UnlockedDialog {
    private Context mContext;
    private Dialog dialog;
    public UnlockedDialog(){
        //
    }
    public UnlockedDialog(Context context){
        this.mContext = context;
    }

    public Dialog showDialog(){
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(mContext, LANGUAGE_CODE, CGlobalVariables.regular);

        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.unlocked_dialog_layout);

        TextView tvOffer = dialog.findViewById(R.id.tvFreeChatDialogOffer);
        TextView tvOfferDesc = dialog.findViewById(R.id.tvFreeChatDialogOfferDesc);
        TextView tvYouGot = dialog.findViewById(R.id.tvFreeChatDialogYouGot);
        Button button = dialog.findViewById(R.id.btnFreeChatDialogButton);
        ImageView ivUnlockedDialogClose = dialog.findViewById(R.id.ivUnlockedDialogClose);

        FontUtils.changeFont(mContext, tvOffer, CGlobalVariables.FONTS_COMIC_SANS_REGULAR);
        FontUtils.changeFont(mContext, tvOfferDesc, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(mContext, tvYouGot, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        button.setOnClickListener(v->{
            if (mContext instanceof DashBoardActivity) {
                AstrosageKundliApplication.apiCallingSource = CGlobalVariables.UNLOCK_FREE_DIALOG_FREE_CHAT;
                ((DashBoardActivity) mContext).popUnderClicked();
                AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                CUtils.fcmAnalyticsEvents("unlock_dialog_free_chat_now", AstrosageKundliApplication.currentEventType, "");

            }
            dialog.dismiss();
        });

        ivUnlockedDialogClose.setOnClickListener(v -> dialog.dismiss());

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                CUtils.fcmAnalyticsEvents("unlock_dialog_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
            }
        });

        return dialog;
    }
}
