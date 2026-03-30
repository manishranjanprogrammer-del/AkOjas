package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class ReportAbuseDialog extends DialogFragment implements View.OnClickListener, VolleyResponse {
    private Activity activity;
    private ImageView imageView;
    private Button button;
    String userRemarks = "";
    String userRemarksType = "";
    FrameLayout frInputConcern1, frInputConcern2, frInputConcern3, frInputConcern5, frInputConcern4;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton5, radioButton4;
    EditText etConcern1, etConcern2, etConcern3, etConcern5, etConcern4;
    private RequestQueue queue;
    private static final int REPORT_ABUSE = 101;
    private String channelName = "", astrologerId = "";

    public ReportAbuseDialog() {

	}

    public ReportAbuseDialog(Activity activity, String astrologerId, String channelName) {
        this.activity = activity;
        this.astrologerId = astrologerId;
        this.channelName = channelName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        View view = inflater.inflate(R.layout.layout_report_abuse, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setRadioInput(view);
        imageView = view.findViewById(R.id.closeAbuse);
        button = view.findViewById(R.id.submit_btn);
        button.setOnClickListener(this);
        imageView.setOnClickListener(this);

        if (queue == null)
            queue = VolleySingleton.getInstance(activity).getRequestQueue();
        return view;
    }

    private void setRadioInput(View view) {
        frInputConcern1 = view.findViewById(R.id.fr_input_concern_1);
        frInputConcern2 = view.findViewById(R.id.fr_input_concern_2);
        frInputConcern3 = view.findViewById(R.id.fr_input_concern_3);
        frInputConcern5 = view.findViewById(R.id.fr_input_concern_5);
        frInputConcern4 = view.findViewById(R.id.fr_input_concern_4);
        radioButton1 = view.findViewById(R.id.radio_button_1);
        radioButton2 = view.findViewById(R.id.radio_button_2);
        radioButton3 = view.findViewById(R.id.radio_button_3);
        radioButton5 = view.findViewById(R.id.radio_button_5);
        radioButton4 = view.findViewById(R.id.radio_button_4);
        etConcern1 = view.findViewById(R.id.etConcern_1);
        etConcern2 = view.findViewById(R.id.etConcern_2);
        etConcern3 = view.findViewById(R.id.etConcern_3);
        etConcern5 = view.findViewById(R.id.etConcern_5);
        etConcern4 = view.findViewById(R.id.etConcern_4);

        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    radioButton1.setChecked(true);
                    radioButton2.setChecked(false);
                    radioButton3.setChecked(false);
                    radioButton5.setChecked(false);
                    radioButton4.setChecked(false);
                    frInputConcern1.setVisibility(View.VISIBLE);
                    frInputConcern2.setVisibility(View.GONE);
                    frInputConcern3.setVisibility(View.GONE);
                    frInputConcern5.setVisibility(View.GONE);
                    frInputConcern4.setVisibility(View.GONE);
                    etConcern1.setText("");
                    etConcern2.setText("");
                    etConcern3.setText("");
                    etConcern5.setText("");
                    etConcern4.setText("");
                    userRemarks = "";
                    userRemarksType = "1";
                }
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(true);
                    radioButton3.setChecked(false);
                    radioButton5.setChecked(false);
                    radioButton4.setChecked(false);
                    frInputConcern1.setVisibility(View.GONE);
                    frInputConcern2.setVisibility(View.VISIBLE);
                    frInputConcern3.setVisibility(View.GONE);
                    frInputConcern5.setVisibility(View.GONE);
                    frInputConcern4.setVisibility(View.GONE);
                    etConcern1.setText("");
                    etConcern2.setText("");
                    etConcern3.setText("");
                    etConcern5.setText("");
                    etConcern4.setText("");
                    userRemarks = "";
                    userRemarksType = "2";
                }
            }
        });
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(false);
                    radioButton3.setChecked(true);
                    radioButton5.setChecked(false);
                    radioButton4.setChecked(false);
                    frInputConcern1.setVisibility(View.GONE);
                    frInputConcern2.setVisibility(View.GONE);
                    frInputConcern3.setVisibility(View.VISIBLE);
                    frInputConcern5.setVisibility(View.GONE);
                    frInputConcern4.setVisibility(View.GONE);
                    etConcern1.setText("");
                    etConcern2.setText("");
                    etConcern3.setText("");
                    etConcern5.setText("");
                    etConcern4.setText("");
                    userRemarks = "";
                    userRemarksType = "3";
                }
            }
        });

        radioButton5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(false);
                    radioButton3.setChecked(false);
                    radioButton5.setChecked(true);
                    radioButton4.setChecked(false);
                    frInputConcern1.setVisibility(View.GONE);
                    frInputConcern2.setVisibility(View.GONE);
                    frInputConcern3.setVisibility(View.GONE);
                    frInputConcern5.setVisibility(View.VISIBLE);
                    frInputConcern4.setVisibility(View.GONE);
                    etConcern1.setText("");
                    etConcern2.setText("");
                    etConcern3.setText("");
                    etConcern5.setText("");
                    etConcern4.setText("");
                    userRemarks = "";
                    userRemarksType = "5";
                }
            }
        });

        radioButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(false);
                    radioButton3.setChecked(false);
                    radioButton5.setChecked(false);
                    radioButton4.setChecked(true);
                    frInputConcern1.setVisibility(View.GONE);
                    frInputConcern2.setVisibility(View.GONE);
                    frInputConcern3.setVisibility(View.GONE);
                    frInputConcern5.setVisibility(View.GONE);
                    frInputConcern4.setVisibility(View.VISIBLE);
                    etConcern1.setText("");
                    etConcern2.setText("");
                    etConcern3.setText("");
                    etConcern5.setText("");
                    etConcern4.setText("");
                    userRemarks = "";
                    userRemarksType = "4";
                }
            }
        });

        etConcern1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userRemarks = charSequence.toString();
                if (charSequence.length() > 0) {
                    button.setBackgroundResource(R.drawable.bg_button_orange);
                    button.setTextColor(getResources().getColor(R.color.white));
                } else {
                    button.setBackgroundResource(R.drawable.bg_button_gray);
                    button.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etConcern2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userRemarks = charSequence.toString();
                if (charSequence.length() > 0) {
                    button.setBackgroundResource(R.drawable.bg_button_orange);
                    button.setTextColor(getResources().getColor(R.color.white));
                } else {
                    button.setBackgroundResource(R.drawable.bg_button_gray);
                    button.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etConcern3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userRemarks = charSequence.toString();
                if (charSequence.length() > 0) {
                    button.setBackgroundResource(R.drawable.bg_button_orange);
                    button.setTextColor(getResources().getColor(R.color.white));
                } else {
                    button.setBackgroundResource(R.drawable.bg_button_gray);
                    button.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etConcern5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userRemarks = charSequence.toString();
                if (charSequence.length() > 0) {
                    button.setBackgroundResource(R.drawable.bg_button_orange);
                    button.setTextColor(getResources().getColor(R.color.white));
                } else {
                    button.setBackgroundResource(R.drawable.bg_button_gray);
                    button.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etConcern4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userRemarks = charSequence.toString();
                if (charSequence.length() > 0) {
                    button.setBackgroundResource(R.drawable.bg_button_orange);
                    button.setTextColor(getResources().getColor(R.color.white));
                } else {
                    button.setBackgroundResource(R.drawable.bg_button_gray);
                    button.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeAbuse:
                dismiss();
                break;
            case R.id.submit_btn:
                if (validForInput()) {
                    addAbuseReportApiCall();
                }
                break;
        }
    }

    private boolean validForInput() {
        if (userRemarks.equals("")) {
            return false;
        }
        if (userRemarksType.equals("")) {
            return false;
        }
        return true;
    }

    @Override
    public void onResponse(String response, int method) {
        if (method == REPORT_ABUSE) {
            if(activity!=null){
                SuccessMsgDialog successMsgDialog = new SuccessMsgDialog(activity, AstrosageKundliApplication.getAppContext().getResources().getString(R.string.concern_submit_text));
                successMsgDialog.show(getActivity().getSupportFragmentManager(), "Dialog");
                dismiss();
            }

        }
    }

    @Override
    public void onError(VolleyError error) {

    }

    private void addAbuseReportApiCall() {
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.REPORT_ABUSE_FEEDBACK_URL,
                ReportAbuseDialog.this, false, getAbuseReportParams(), REPORT_ABUSE).getMyStringRequest();
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    public Map<String, String> getAbuseReportParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.REMARK_TYPE, userRemarksType + "");
        headers.put(CGlobalVariables.REMARK, userRemarks);
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserID(activity));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(activity));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(activity));
        headers.put(CGlobalVariables.CHANNEL_NAME, channelName);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        return headers;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        CUtils.fcmAnalyticsEvents("report_abuse_dialog_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }

}
