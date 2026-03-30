package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import androidx.fragment.app.DialogFragment;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

/**
 * Created by ojas on 4/5/16.
 */
public class AstroSageParentDialogFragment extends DialogFragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = (int) width - 40;
            wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(wmlp);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
