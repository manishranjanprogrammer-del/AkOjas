package com.ojassoft.astrosage.ui.act;

import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.VolleyResponse;
import com.ojassoft.astrosage.misc.VolleyServiceHandler;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.networkcall.OnVolleyResultListener;
import com.ojassoft.astrosage.networkcall.VolleyServerRequest;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Author: Ankit Varshney
 * CreatedOn: 31/12/2019
 */
public class ActUserPlanDetails extends BaseInputActivity implements OnVolleyResultListener, VolleyResponse {

    private Button mSubmitBtn;
    private CustomProgressDialog pd;
    private TextView headingTV;
    private TextView line1TV;
    private TextView line2TV;
    Toolbar toolbar;
    private RequestQueue queue;
    TextInputLayout headingTIL, line1TIL, line2TIL;
    EditText headingET, line1ET, line2ET;
    int planId;
    GmailAccountInfo gmailAccountInfo;
    String oldPassword;

    public ActUserPlanDetails() {
        super(R.string.contact_details);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planId = CUtils.getUserPurchasedPlanFromPreference(this);
        oldPassword = CUtils.getUserPassword(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.lay_user_plan_details);
        initViews();
        getBrandingDetailFromServer();
    }

    /**
     * init views
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar_user_plan_details);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(toolbar);
        TextView title = toolbar.findViewById(R.id.tvToolBarTitle);
        title.setText(getResources().getString(R.string.contact_details));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        headingTV = (TextView) findViewById(R.id.textViewHeading);
        line1TV = (TextView) findViewById(R.id.textViewLine1);
        line2TV = (TextView) findViewById(R.id.textViewLine2);
        headingET = (EditText) findViewById(R.id.heading_edittext);
        line1ET = (EditText) findViewById(R.id.line1_edittext);
        line2ET = (EditText) findViewById(R.id.line2_edittext);
        mSubmitBtn = findViewById(R.id.saveBtn);

        headingTIL = (TextInputLayout) findViewById(R.id.inputLayHeading);
        line1TIL = (TextInputLayout) findViewById(R.id.inputLayLine1);
        line2TIL = (TextInputLayout) findViewById(R.id.inputLayLine2);

        headingTIL.setHintEnabled(false);
        line1TIL.setHintEnabled(false);
        line2TIL.setHintEnabled(false);

        if (((AstrosageKundliApplication) getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            mSubmitBtn.setText(getResources().getString(R.string.save_kundli).toUpperCase());
        } else {
            mSubmitBtn.setText(getResources().getString(R.string.save_kundli));
        }

        gmailAccountInfo = CUtils.getGmailAccountInfo(this);
        try {
            if (gmailAccountInfo != null) {
                headingET.setText(gmailAccountInfo.getHeading());
                line1ET.setText(gmailAccountInfo.getAddress1());
                line2ET.setText(gmailAccountInfo.getAddress2());
            }
        } catch (Exception ex) {
            //
        }

        setTypefaceOfViews();
        onClickEvents();
    }

    /**
     * on Click Listeners
     */
    private void onClickEvents() {
        line2TV.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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
        if (!CUtils.isConnectedWithInternet(this)) {
            showCustomMsg(getResources().getString(R.string.no_internet));
        } else {
            showProgressBar();
            //set Analytic
            CUtils.fcmAnalyticsEvents(CGlobalVariables.CONTACT_DETAIL_SUBMIT_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            // API calling
            new VolleyServerRequest(this, ActUserPlanDetails.this, CGlobalVariables.CHANGE_PASSWORD_USERNAME, getParamsNew());

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
        headingTV.setTypeface(regularTypeface);
        line1TV.setTypeface(regularTypeface);
        line2TV.setTypeface(regularTypeface);
        mSubmitBtn.setTypeface(meduimTypeface);
    }


    public HashMap<String, String> getParamsNew() {

        gmailAccountInfo = CUtils.getGmailAccountInfo(ActUserPlanDetails.this);
        HashMap<String, String> params = new HashMap<String, String>();
        try {
            if (gmailAccountInfo != null) {
                String userName = gmailAccountInfo.getUserName().trim();

                params.put("key", CUtils.getApplicationSignatureHashCode(ActUserPlanDetails.this));
                params.put(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(gmailAccountInfo.getId()));
                params.put("npw", CUtils.getUserPassword(this));
                params.put("opw", CUtils.getUserPassword(this));
                params.put("userfirstname", userName);

                params.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ActUserPlanDetails.this)));
                params.put("companyName", headingET.getText().toString().trim());
                params.put("companyLogoImgUrl", "");
                params.put("addressLine1", line1ET.getText().toString().trim());
                params.put("addressLine2", line2ET.getText().toString().trim());
                params.put("mobileNo", gmailAccountInfo.getMobileNo().trim());
                params.put("userMaritalStatus", String.valueOf(gmailAccountInfo.getMaritalStatus()));
                params.put("userOccupation", String.valueOf(gmailAccountInfo.getOccupation()));

                return params;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
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
            CUtils.hideMyKeyboard(ActUserPlanDetails.this);
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
        MyCustomToast mct = new MyCustomToast(ActUserPlanDetails.this,
                getLayoutInflater(), ActUserPlanDetails.this,
                regularTypeface);
        mct.show(msg);
    }

    /**
     * @param resultString
     * @return
     */
    private String parseResult(String resultString) {
        String result = "";
        try {
            JSONArray jsonArray = new JSONArray(resultString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            result = jsonObject.get("Result").toString();
        } catch (Exception e) {

        }
        return result;
    }

    @Override
    public void onVolleySuccess(String result, Cache cache) {
        try {
            hideProgressBar();
            String resultStr = parseResult(result);
            if (resultStr.equals("1")) {
//                if (doLogout) {
//                    showCustomMsg(getResources().getString(R.string.sign_out_success));
//                    CUtils.updateLoginDetials(false, "", "", "", ActUserPlanDetails.this);
//                } else {
                showCustomMsg(getResources().getString(R.string.porfile_updated_successfully));
                gmailAccountInfo.setHeading(headingET.getText().toString().trim());
                gmailAccountInfo.setAddress1(line1ET.getText().toString().trim());
                gmailAccountInfo.setAddress2(line2ET.getText().toString().trim());

                CUtils.saveGmailAccountInfo(ActUserPlanDetails.this, gmailAccountInfo);
                // }

                finish();
            } else if (resultStr.equals("4")) {
                showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onVolleyError(VolleyError result) {
        hideProgressBar();
    }

    private void getBrandingDetailFromServer() {
        showProgressBar();
        String url = CGlobalVariables.GET_BRANDING_DETAIL_URL;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, ActUserPlanDetails.this, true, getParamsForBrandingDetail(), 1).getMyStringRequest();
        queue.add(stringRequest);
    }

    private HashMap<String, String> getParamsForBrandingDetail() {
        gmailAccountInfo = CUtils.getGmailAccountInfo(ActUserPlanDetails.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(ActUserPlanDetails.this));
        params.put("us", CUtils.replaceEmailChar(gmailAccountInfo.getId()));
        params.put("pw", oldPassword);

        return params;
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        if (method == 1) {
            Log.i("Branding Result-", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String responsecode = jsonObject.getString("responsecode");
                if (responsecode.equals("1")) {
                    String message = jsonObject.getString("message");
                    String firstName = jsonObject.getString("firstName");
                    String occupation = jsonObject.getString("occupation");
                    String companyName = jsonObject.getString("companyName");
                    String mobile = jsonObject.getString("mobile");
                    String addressLine1 = jsonObject.getString("addressLine1");
                    String addressLine2 = jsonObject.getString("addressLine2");
                    String userId = jsonObject.getString("userId");
                    String email = jsonObject.getString("email");
                    String maritalStatus = jsonObject.getString("maritalStatus");

                    GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
                    gmailAccountInfo.setId(this.gmailAccountInfo.getId());
                    gmailAccountInfo.setUserName(firstName);
                    gmailAccountInfo.setMobileNo(mobile);
                    gmailAccountInfo.setHeading(companyName);
                    gmailAccountInfo.setAddress1(addressLine1);
                    gmailAccountInfo.setAddress2(addressLine2);
                    gmailAccountInfo.setMaritalStatus(Integer.parseInt(maritalStatus));
                    gmailAccountInfo.setOccupation(Integer.parseInt(occupation));

                    CUtils.saveGmailAccountInfo(ActUserPlanDetails.this, gmailAccountInfo);
                    headingET.setText(firstName);
                    headingET.setText(companyName);
                    line1ET.setText(addressLine1);
                    line2ET.setText(addressLine2);

                }

            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }
}
