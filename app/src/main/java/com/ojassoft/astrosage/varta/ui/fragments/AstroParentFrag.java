package com.ojassoft.astrosage.varta.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

/**
 * Created by ojas on 8/2/17.
 */

public class AstroParentFrag extends DialogFragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = width - 40;
            wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(wmlp);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
