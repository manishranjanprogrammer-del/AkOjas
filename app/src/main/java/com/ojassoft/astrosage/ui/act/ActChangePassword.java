package com.ojassoft.astrosage.ui.act;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.VolleyError;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.networkcall.OnVolleyResultListener;
import com.ojassoft.astrosage.networkcall.VolleyServerRequest;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Author: Ankit Varshney
 * CreatedOn: 22/7/2019
 */
public class ActChangePassword extends BaseInputActivity implements OnVolleyResultListener {

    private Button mSubmitBtn;
    private EditText mOldPasswordET;
    private TextInputLayout mOldPasswordTIL;
    private EditText mNewPasswordET;
    private TextInputLayout mNewPasswordTIL;
    private CustomProgressDialog pd;
    private final String PASSWORD_EMPTY = "PASSWORD_EMPTY";
    private final String PASSWORD_LENGTH = "PASSWORD_LENGTH";
    private final String PASSWORD_MISMATCH = "PASSWORD_MISMATCH";

    public ActChangePassword() {
        super(R.string.change_password);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_change_password);
        initViews();
    }

    /**
     * init views
     */
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar_magazine);
        setSupportActionBar(toolbar);
        TextView title = toolbar.findViewById(R.id.tvToolBarTitle);
        title.setText(getResources().getString(R.string.change_password));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mOldPasswordET = findViewById(R.id.etOldPassword);
        mOldPasswordTIL = findViewById(R.id.oldPasswordTextInputLay);
        mNewPasswordET = findViewById(R.id.etNewPassword);
        mNewPasswordTIL = findViewById(R.id.newPasswordTextInputLay);
        mSubmitBtn = findViewById(R.id.btn_submit);
        setTypefaceOfViews();
        onClickEvents();
    }

    /**
     * on Click Listeners
     */
    private void onClickEvents() {
        mNewPasswordET.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    performClick();
                }
                return false;
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClick();
            }
        });
    }

    /**
     * perform Click
     */
    private void performClick() {
        //set Analytic
        CUtils.fcmAnalyticsEvents(CGlobalVariables.CHANGE_PASSWORD_SUBMIT_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        String oldPassword = mOldPasswordET.getText().toString().trim();
        String newPassword = mNewPasswordET.getText().toString().trim();
        if (matchPassword()) {
            submitData(oldPassword, newPassword);
        }
    }

    /**
     * set Typeface Of Views
     */
    private void setTypefaceOfViews() {
        int LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        //Typeface robotoMediumTypeface = CUtils.getRobotoMedium(ActChangePassword.this);
        Typeface meduimTypeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.medium);

        regularTypeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        //Typeface robotMediumTypeface = CUtils.getRobotoMedium(getApplicationContext());
        //Typeface robotRegularTypeface = CUtils.getRobotoRegular(getApplicationContext());
        mOldPasswordET.setTypeface(regularTypeface);
        mNewPasswordET.setTypeface(regularTypeface);
        mSubmitBtn.setTypeface(meduimTypeface);
    }

    /**
     * match Password
     *
     * @return boolean
     */
    private Boolean matchPassword() {
        return (validateAllFields()
                && validateField(mNewPasswordET, mNewPasswordTIL, getResources().getString(R.string.password_length_error_msg), PASSWORD_LENGTH));
    }

    /**
     * check for empty field.
     *
     * @return boolean
     */
    private Boolean validateAllFields() {
        return (validateField(mOldPasswordET, mOldPasswordTIL, getString(R.string.enter_password), PASSWORD_EMPTY) &&
                validateField(mOldPasswordET, mOldPasswordTIL, getString(R.string.enter_correct_password), PASSWORD_MISMATCH)
                && validateField(mNewPasswordET, mNewPasswordTIL, getString(R.string.enter_password), PASSWORD_EMPTY));
    }

    /**
     * @param editText    fields
     * @param inputLayout fields
     * @param message     message to print
     * @return boolean
     */
    private boolean validateField(EditText editText, TextInputLayout inputLayout, String message, String validationCase) {
        final int MINIMUM_PASSWORD_LENGTH = 3;
        switch (validationCase) {
            case PASSWORD_EMPTY:
                if (editText.getText().toString().trim().isEmpty()) {
                    setErrorMessage(editText, inputLayout, message);
                    return false;
                } else {
                    removeErrorMessage(editText, inputLayout);
                }
                break;
            case PASSWORD_LENGTH:
                if (editText.length() > MINIMUM_PASSWORD_LENGTH) {
                    removeErrorMessage(editText, inputLayout);
                } else {
                    setErrorMessage(editText, inputLayout, message);
                    return false;
                }
                break;
            case PASSWORD_MISMATCH:
                if (!editText.getText().toString().equals(CUtils.getUserPassword(ActChangePassword.this))) {
                    setErrorMessage(editText, inputLayout, message);
                    return false;
                } else {
                    removeErrorMessage(editText, inputLayout);
                }
                break;
        }
        return true;
    }

    /**
     * set Error Message
     */
    private void setErrorMessage(EditText editText, TextInputLayout inputLayout, String message) {
        inputLayout.setErrorEnabled(true);
        requestFocus(editText);
        inputLayout.setError(message);
        editText.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

    }

    /**
     * remove Error Message
     */
    private void removeErrorMessage(EditText editText, TextInputLayout inputLayout) {
        inputLayout.setErrorEnabled(false);
        inputLayout.setError(null);
        editText.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * @param view hide state
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * submit Data
     */
    private void submitData(String oldPassword, String newPassword) {
        if (!CUtils.isConnectedWithInternet(this)) {
            showCustomMsg(getResources().getString(R.string.no_internet));
        } else {
            //String url = "http://192.168.1.117/as/reset-password-v3.asp";
            showProgressBar();
            String userId = "";
            GmailAccountInfo gmailAccountInfo = CUtils.getGmailAccountInfo(this);
            if (gmailAccountInfo != null && gmailAccountInfo.getId() != null && !gmailAccountInfo.getId().isEmpty()) {
                userId = gmailAccountInfo.getId().trim();
            }
            // API calling
            new VolleyServerRequest(this, ActChangePassword.this, CGlobalVariables.CHANGE_PASSWORD_URL, getParams(userId, oldPassword, newPassword));
        }
    }

    @Override
    public void onVolleySuccess(String result, Cache cache) {
        int resultCode = -1;
        hideProgressBar();
        if (result != null && !result.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jObj = jsonArray.getJSONObject(0);
                resultCode = Integer.parseInt(jObj.optString("Result"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (resultCode) {
                case 0:   //0 for all fields required
                    showCustomMsg(getResources().getString(R.string.password_not_updated));
                    break;
                case 1:   //1 for Sucessfully update
                    showCustomMsg(getResources().getString(R.string.password_reset_successfully));
                    CUtils.hideMyKeyboard(ActChangePassword.this);
                    finish();
                    break;
                case 2:   //2 for result failed to update / no record update
                    showCustomMsg(getResources().getString(R.string.password_not_updated));
                    break;
                case 3:   //3 for authentication failed
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                    break;
            }
        }
    }

    @Override
    public void onVolleyError(VolleyError result) {
        hideProgressBar();
    }

    /**
     * @param userId      id
     * @param oldPassword password
     * @param newPassword password
     * @return hash map
     */
    private HashMap<String, String> getParams(String userId, String oldPassword, String newPassword) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("key", CUtils.getApplicationSignatureHashCode(ActChangePassword.this));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userId));
        headers.put("opw", oldPassword);
        headers.put("npw", newPassword);
        return headers;
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(this, regularTypeface);
            pd.setCanceledOnTouchOutside(false);
        }
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && android.R.id.home == item.getItemId()) {
            CUtils.hideMyKeyboard(ActChangePassword.this);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * show Custom Msg
     *
     * @param msg to show
     */
    private void showCustomMsg(String msg) {
        MyCustomToast mct = new MyCustomToast(ActChangePassword.this,
                getLayoutInflater(), ActChangePassword.this,
                regularTypeface);
        mct.show(msg);
    }
}
