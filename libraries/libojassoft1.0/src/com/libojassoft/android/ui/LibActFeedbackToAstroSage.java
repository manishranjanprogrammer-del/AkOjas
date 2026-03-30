package com.libojassoft.android.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.libojassoft.android.R;
import com.libojassoft.android.custom.controls.MyCustomToast;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;


/**
 * This activity is used to send user feed back to AstroSage server
 *
 * @author Bijendra(2 - Nov - 13)
 */
public class LibActFeedbackToAstroSage extends Activity implements VolleyResponse {

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    /**
     * Application variables
     */
    EditText _etName, _etEmailId, _etPhone, _etMsg;
    private TextView counter;
    int FEEDBACK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.libojassoft_lay_feedback);

        this.setTitle("");

        _etName = (EditText) findViewById(R.id.etName);
        _etEmailId = (EditText) findViewById(R.id.etEmailId);
        _etPhone = (EditText) findViewById(R.id.etPhone);
        _etMsg = (EditText) findViewById(R.id.etMsg);


        _etMsg.addTextChangedListener(mTextEditorWatcher);
        counter = (TextView) findViewById(R.id.textViewCharCount);
        setTextViewFact();
        ViewGroup vg = (ViewGroup) getWindow().getDecorView();
        LibCUtils.changeAllViewsFonts(vg, LibCGlobalVariables.SHOW_FONT_TYPE);
        setEditTextFontToEnglishOnly();
    }

    private void setEditTextFontToEnglishOnly() {
        _etName.setTypeface(Typeface.DEFAULT);
        _etEmailId.setTypeface(Typeface.DEFAULT);
        _etPhone.setTypeface(Typeface.DEFAULT);
        _etMsg.setTypeface(Typeface.DEFAULT);
    }

    private void setTextViewFact() {
        getLayoutInflater().setFactory(new Factory() {
            @Override
            public View onCreateView(String name, Context context,
                                     AttributeSet attrs) {
                if (name.equalsIgnoreCase("TextView")) {
//	        		 if (name.equalsIgnoreCase("com.android.internal.view.menu.ActionMenuItemView" || name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView"))) {
//	        			 if (name.equalsIgnoreCase("Button")) {
                    try {
                        LayoutInflater li = LayoutInflater.from(context);
                        final View view = li.createView(name, null, attrs);
                        new Handler().post(new Runnable() {
                            public void run() {
                                ((TextView) view).setTextSize(18);
                                ((TextView) view).setTypeface(LibCGlobalVariables.SHOW_FONT_TYPE);
                                ((TextView) view).setTextColor(Color.BLACK);
                            }
                        });
                        return view;
                    } catch (InflateException e) {

                    } catch (ClassNotFoundException e) {

                    }
                }
                return null;
            }

        });
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            counter.setText(String.valueOf(500 - s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void goToSend(View v) {
        if (validateForm()) {
            if (LibCUtils.isConnectedWithInternet(LibActFeedbackToAstroSage.this)) {
                //new SendFeedBackAsync().execute();
                sendFeedBackAsync(getMyApplicationName(), _etName.getText().toString(), _etEmailId.getText().toString(), _etPhone.getText().toString(), _etMsg.getText().toString());
            } else {
                showCustomisedToastMessage(getResources().getString(R.string.internet_is_not_working));
            }
        }
    }

    private ProgressDialog pd;

    private void sendFeedBackAsync(String apppName, String name, String emailId, String phone, String message) {
        pd = ProgressDialog.show(LibActFeedbackToAstroSage.this, null, getResources().getString(R.string.lib_pleasewait), true, false);
        TextView tvMsg = (TextView) pd.findViewById(android.R.id.message);
        tvMsg.setTypeface(LibCGlobalVariables.SHOW_FONT_TYPE);
        tvMsg.setTextColor(Color.WHITE);
        tvMsg.setTextSize(20);
        LibCUtils.vollyPostRequest(this, this, LibCGlobalVariables.FEED_BACK, getparams(apppName, name, emailId, phone, message), FEEDBACK);
    }

    Map<String, String> getparams(String apppName, String name, String emailId, String phone, String message) {
        Map<String, String> params = new HashMap<>();
        params.put("feedbackfrom", apppName);
        params.put("feedbackfrom", apppName);

        params.put("feedbackpersonname", name);
        params.put(LibCGlobalVariables.KEY_EMAIL_ID, LibCUtils.replaceEmailChar(emailId));
        params.put("phoneno", phone);
        params.put("message", message);

        if (LibCGlobalVariables.userDetailAvailble) {
            params.put(LibCGlobalVariables.KEY_AS_USER_ID, LibCGlobalVariables.useridsession);
            params.put("name", LibCGlobalVariables.name);
            params.put("day", LibCGlobalVariables.day);
            params.put("month", LibCGlobalVariables.month);
            params.put("year", LibCGlobalVariables.year);
            params.put("hour", LibCGlobalVariables.hour);
            params.put("min", LibCGlobalVariables.min);
            params.put("sec", LibCGlobalVariables.sec);
            params.put("place", LibCGlobalVariables.place);
            params.put("timezone", LibCGlobalVariables.timezone);
            params.put("longdeg", LibCGlobalVariables.longdeg);
            params.put("longmin", LibCGlobalVariables.longmin);
            params.put("longew", LibCGlobalVariables.longew);
            params.put("latdeg", LibCGlobalVariables.latdeg);
            params.put("latmin", LibCGlobalVariables.latmin);
            params.put("latns", LibCGlobalVariables.latns);
            params.put("dst", LibCGlobalVariables.dst);
            params.put("ayanamsa", LibCGlobalVariables.ayanamsa);
        }
        LibCGlobalVariables.userDetailAvailble = false;
        return params;

    }


   /* private class SendFeedBackAsync extends AsyncTask<String, Long, Void> {

        String _exceptionMessage = "";
        boolean isSuccess = true;
        int resultCode = -1;

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                resultCode = LibCUtils.sendFeedbackToAstroSage(getMyApplicationName(), _etName.getText().toString(), _etEmailId.getText().toString(), _etPhone.getText().toString(), _etMsg.getText().toString());
            } catch (Exception e) {
                isSuccess = false;
                _exceptionMessage = e.getMessage();
            }
            return null;
        }


        protected void onPreExecute() {
            pd = ProgressDialog.show(LibActFeedbackToAstroSage.this, null, getResources().getString(R.string.lib_pleasewait), true, false);
            TextView tvMsg = (TextView) pd.findViewById(android.R.id.message);
            tvMsg.setTypeface(LibCGlobalVariables.SHOW_FONT_TYPE);
            tvMsg.setTextColor(Color.WHITE);
            tvMsg.setTextSize(20);
        }

        protected void onPostExecute(final Void unused) {
            try {
                if (pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }

                if (isSuccess) {

                    switch (resultCode) {
                        case 0:
                            showCustomisedToastMessage(getResources().getString(R.string.feedback_sent));
                            LibActFeedbackToAstroSage.this.finish();
                            break;
                        case 1:
                            showCustomisedToastMessage(getResources().getString(R.string.please_enter_name));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 2:
                            showCustomisedToastMessage(getResources().getString(R.string.name_limit));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 3:
                            showCustomisedToastMessage(getResources().getString(R.string.email_one));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 4:
                            showCustomisedToastMessage(getResources().getString(R.string.email_two));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 5:
                            showCustomisedToastMessage(getResources().getString(R.string.email_three));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 6:
                            showCustomisedToastMessage(getResources().getString(R.string.phone_validation));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 7:
                            showCustomisedToastMessage(getResources().getString(R.string.phone_should_be_numeric));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 8:
                            showCustomisedToastMessage(getResources().getString(R.string.feedback_message_one));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 9:
                            showCustomisedToastMessage(getResources().getString(R.string.feedback_message_two));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                    }
                }

                if (_exceptionMessage.trim().length() > 0) {
                    showCustomisedToastMessage(_exceptionMessage);
                }
            } catch (Exception e) {
                showCustomisedToastMessage(e.getMessage());
            }

        }
    }*/

    public void goToCancel(View v) {
        this.finish();
        ;
    }

    private void showCustomisedToastMessage(String msg) {
        MyCustomToast mct = new MyCustomToast(this, getLayoutInflater(), this);
        mct.show(msg);
    }

    /**
     * This function is used to validate the feedback form
     *
     * @return boolean
     * @author Bijendra(02 - Nov - 13)
     */
    private boolean validateForm() {
        boolean isValid = true;

        if (_etName.getText().toString().trim().length() < 1) {
            isValid = false;
            _etName.setError(getResources().getString(R.string.please_enter_name));
        } else if (_etName.getText().toString().trim().length() > 50) {
            isValid = false;
            _etName.setError(getResources().getString(R.string.name_limit));
//			_etName.setError("Name should be with in 50 characters...");
        }
        if (_etEmailId.getText().toString().trim().length() < 1) {
            isValid = false;
            _etEmailId.setError(getResources().getString(R.string.email_one));
//			_etEmailId.setError("Please Enter e-mail Id...");
        } else if (_etEmailId.getText().toString().trim().length() > 70) {
            isValid = false;
            _etEmailId.setError(getResources().getString(R.string.email_two));
            //_etEmailId.setError("e-mail should be with in 70 characters...");
        } else if (!checkEmail(_etEmailId.getText().toString().trim())) {
            _etEmailId.setError(getResources().getString(R.string.email_three));
//			_etEmailId.setError("Enter valid e-mail Id...");
            isValid = false;
        }
        if (_etMsg.getText().toString().trim().length() < 1) {
            isValid = false;
            _etMsg.setError(getResources().getString(R.string.feedback_message_one));
//			_etMsg.setError("Please write feedback message...");
        } else if (_etMsg.getText().toString().trim().length() > 500) {
            isValid = false;
            _etMsg.setError(getResources().getString(R.string.feedback_message_two));
//			_etMsg.setError("Message should be with in 500 characters...");
        }
        if (_etPhone.getText().toString().trim().length() > 1 && _etPhone.getText().toString().trim().length() < 10) {
            isValid = false;
            _etPhone.setError(getResources().getString(R.string.phone_validation));
//			_etPhone.setError("Enter 10 digit number...");
        }


        return isValid;
    }


    private boolean checkEmail(String email) {
        if (email == null) {
            return false;
        } else if (email.equals("")) {
            return true;
        } else {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
        }
    }

    /**
     * This function return the application name from which this activity is called
     *
     * @return String
     * @author Bijendra(02 - nov - 13)
     */
    private String getMyApplicationName() {
        ApplicationInfo info = getApplicationInfo();
        PackageManager p = this.getPackageManager();
        String appName = p.getApplicationLabel(info).toString();
        return appName + " ( " + getApplicationContext().getPackageName() + " )";
    }

    @Override
    public void onResponse(String response, int method) {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }

            if (!TextUtils.isEmpty(response)) {
                int resultCode = LibCUtils.parseResponse(response);
                switch (resultCode) {
                    case 0:
                        showCustomisedToastMessage(getResources().getString(R.string.feedback_sent));
                        LibActFeedbackToAstroSage.this.finish();
                        break;
                    case 1:
                        showCustomisedToastMessage(getResources().getString(R.string.please_enter_name));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 2:
                        showCustomisedToastMessage(getResources().getString(R.string.name_limit));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 3:
                        showCustomisedToastMessage(getResources().getString(R.string.email_one));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 4:
                        showCustomisedToastMessage(getResources().getString(R.string.email_two));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 5:
                        showCustomisedToastMessage(getResources().getString(R.string.email_three));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 6:
                        showCustomisedToastMessage(getResources().getString(R.string.phone_validation));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 7:
                        showCustomisedToastMessage(getResources().getString(R.string.phone_should_be_numeric));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 8:
                        showCustomisedToastMessage(getResources().getString(R.string.feedback_message_one));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 9:
                        showCustomisedToastMessage(getResources().getString(R.string.feedback_message_two));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                }
            }


        } catch (Exception e) {
            if (e.getMessage() != null) {
                showCustomisedToastMessage(e.getMessage());
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
            pd = null;
        }
        if (error != null && error.getMessage() != null) {
            showCustomisedToastMessage(error.getMessage());
        }
    }
}
