package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

public class CallUsDialogFragment extends DialogFragment {

    TextView callNowBtn, headingTv, subHeadingTV;
    ImageView cancelBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View rootView = inflater.inflate(R.layout.call_us_dialog, container);
        setCancelable(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        int LANGUAGE_CODE = ((AstrosageKundliApplication) requireActivity().getApplication())
                .getLanguageCode();

        //Log.d("CallUsDialogFragment", "onCreateView: LANGUAGE_CODE: " +LANGUAGE_CODE+ CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.d("CallUsDialogFragment", "context: LANGUAGE_CODE: " +requireContext().getResources().getConfiguration().locale.getLanguage());
        setAppLanguage(CUtils.getLanguageKey(LANGUAGE_CODE));
        inti(rootView);
        return rootView;
    }


    public void setAppLanguage(String languageCode) {
        // 1. Create a LocaleListCompat for the new language
        // Use the language code (e.g., "en", "hi", "es")
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(languageCode);

        // 2. Set the application locales
        AppCompatDelegate.setApplicationLocales(appLocale);
    }


    private void inti(View rootView) {
        callNowBtn = rootView.findViewById(R.id.call_now_btn);
        cancelBtn = rootView.findViewById(R.id.iv_close_view);
        headingTv = rootView.findViewById(R.id.headign_tv);
        subHeadingTV = rootView.findViewById(R.id.sub_headign_tv);
        FontUtils.changeFont(getContext(),headingTv,FONTS_OPEN_SANS_SEMIBOLD);
         // setAppLanguage(CUtils.getLanguageKey(oldLanguageIndex));

        headingTv.setText(getString(R.string.we_are_here_for_you));
        subHeadingTV.setText(getString(R.string.if_you_have_query_call_us_msg));
        headingTv.setText(getString(R.string.call_now));

        callNowBtn.setOnClickListener(v -> {
            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.CALL_NOW_BTN_FROM_DIALOG_CLICK, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getString(R.string.generate_ticket_cnumber)));
            startActivity(intent);
//            CUtils.callNow(getActivity());
            dismiss();
        });
        cancelBtn.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }
}
