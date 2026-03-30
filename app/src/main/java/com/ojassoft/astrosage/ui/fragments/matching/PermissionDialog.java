package com.ojassoft.astrosage.ui.fragments.matching;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IPermissionCallback;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on १२/९/१७.
 */

public class PermissionDialog extends DialogFragment {

    private TextView tvContent;
    private Button btnDeny,btnAllow;
    private IPermissionCallback iPermissionCallback;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iPermissionCallback = (IPermissionCallback)context;
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        View view = inflater.inflate(R.layout.lay_permission_dialog, container);

        setLayRef(view);

        setListener();

        return view;
    }

    private void setLayRef(View view){
        tvContent = (TextView)view.findViewById(R.id.tvContent);
        btnDeny = (Button)view.findViewById(R.id.btnDeny);
        btnAllow = (Button)view.findViewById(R.id.btnAllow);
    }

    private void setListener(){
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack(false);
            }
        });

        btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.saveBooleanData(context, CGlobalVariables.EmailId_Permission,true);
                callBack(true);
            }
        });
    }

    private void callBack(boolean isPermissionGranted){
        iPermissionCallback.isEmailIdPermissionGranted(isPermissionGranted);
        dismiss();
    }
}
