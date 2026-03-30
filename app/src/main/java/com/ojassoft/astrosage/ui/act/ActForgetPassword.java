package com.ojassoft.astrosage.ui.act;

import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.google.analytics.tracking.android.EasyTracker;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.PasswordSentConfirmationDialog;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/*import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;*/
import org.json.JSONArray;
import org.json.JSONException;

/*import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;*/
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amit Rautela on 19/4/16.
 */
public class ActForgetPassword extends BaseInputActivity implements VolleyResponse {

    TextView tvRequestPasswordHeading, tvRequestPasswordDesc;
    TextInputLayout userTextInputLay;
    EditText etUserName;
    Button btnSendPass;
    Toolbar toolBar_InputKundli;
    TextView tvTitle;
    //AsyncSendPassword asyncSendPassword;
    CustomProgressDialog pd = null;

    public ActForgetPassword() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lay_activity_forget_pass);

        setLayRef();

    }

    private void setLayRef() {

        toolBar_InputKundli = (Toolbar) findViewById(R.id.tool_barAppModule);
        toolBar_InputKundli.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(toolBar_InputKundli);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);

        tvRequestPasswordHeading = (TextView) findViewById(R.id.tvRequestPasswordHeading);
        tvRequestPasswordHeading.setTypeface(regularTypeface);
        tvRequestPasswordDesc = (TextView) findViewById(R.id.tvRequestPasswordDesc);
        tvRequestPasswordDesc.setTypeface(regularTypeface);
        userTextInputLay = (TextInputLayout) findViewById(R.id.userTextInputLay);
        etUserName = (EditText) findViewById(R.id.etUserName);
        btnSendPass = (Button) findViewById(R.id.btnSendPass);
        btnSendPass.setTypeface(regularTypeface);
        btnSendPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForPassword();
            }
        });

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setTypeface(regularTypeface);
        tvTitle.setText(getResources().getString(R.string.forget_pass_tag));

    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    private void requestForPassword() {

        if (validateForm()) {
            /*Map<String, String> params = new HashMap<String, String>();
            params.put("userid", etUserName.getText().toString().trim());
            params.put("key", CUtils.getApplicationSignatureHashCode(ActForgetPassword.this));*/

           /* try {
                if (asyncSendPassword != null) {

                    if (asyncSendPassword.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncSendPassword.cancel(true);
                    }
                }

                asyncSendPassword = new AsyncSendPassword();
                asyncSendPassword.execute();
            } catch (Exception ex) {
                //
            }*/
            sendPassword();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateForm() {

        if (etUserName.getText().toString().trim().length() == 0) {

            userTextInputLay.setError(getString(R.string.email_login));
            userTextInputLay.setErrorEnabled(true);
            //requestFocus(etUserName);
            etUserName.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

            return false;
        } else if (!CUtils.isConnectedWithInternet(ActForgetPassword.this)) {

            MyCustomToast mct = new MyCustomToast(ActForgetPassword.this,
                    ActForgetPassword.this.getLayoutInflater(),
                    ActForgetPassword.this, regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));

            return false;
        } else {

            userTextInputLay.setErrorEnabled(false);
            userTextInputLay.setError(null);
            etUserName.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);


            return true;
        }
    }


   /* private class AsyncSendPassword extends AsyncTask<Map<String, String>, String, String> {

        String userid = "";
        String key = "";
        CustomProgressDialog pd = null;

        @Override
        protected void onPreExecute() {

            userid = etUserName.getText().toString().trim();
            key = CUtils.getApplicationSignatureHashCode(ActForgetPassword.this);

            pd = new CustomProgressDialog(ActForgetPassword.this, regularTypeface);
            pd.show();
        }

        @Override
        protected String doInBackground(Map<String, String>... params) {

            String result = "-1";
            try {
                //JSONArray jsonResponse = HttpUtility.sendPostRequest(CGlobalVariables.forgetPasswordUrl, params[0],ActForgetPassword.this);
                JSONArray jsonResponse = executeHTTRequestForgetPasswordUrl(userid, key);

                if (jsonResponse != null) {
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        try {

                            result = jsonResponse.getJSONObject(i).getString("result");

                        } catch (JSONException e) {
                            //Log.i("error", e.getMessage().toString());
                        }
                    }
                }

            } catch (Exception ex) {
                //Log.i("error", ex.getMessage().toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                try {
                    if (pd != null & pd.isShowing())
                        pd.dismiss();
                } catch (Exception e) {
                    //Log.i("Exception",e.getMessage().toString());
                }

                MyCustomToast mct = new MyCustomToast(ActForgetPassword.this,
                        ActForgetPassword.this.getLayoutInflater(),
                        ActForgetPassword.this, regularTypeface);

                if (result.equals("0")) {
                    mct.show(getResources().getString(R.string.user_id_not_exists));
                } else if (result.equals("1")) {
                    //mct.show(getResources().getString(R.string.email_succecfully_send));
                    showDialogPopup(getResources().getString(R.string.email_succecfully_send), getResources().getString(R.string.ok));
                } else if (result.equals("2")) {
                    mct.show(getResources().getString(R.string.forgot_pass_validation_all_fields));
                } else if (result.equals("")) {
                    mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                }
            } catch (Exception ex) {
                // Log.e("Ex",ex.getMessage());
            }
        }
    }*/

    /*private JSONArray executeHTTRequestForgetPasswordUrl(String userid, String key)
            throws Exception {
        String strReturn = "";
        BufferedReader in = null;
        JSONArray jArray = null;
        HttpClient hc = CUtils.getNewHttpClient();
        HttpPost hp = new HttpPost(CGlobalVariables.forgetPasswordUrl);
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_ForgetPassword(
                    userid, key), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            strReturn = sb.toString();

        } catch (Exception e) {
            //Log.i("Tag","error -> "+e.getMessage().toString());
        }

        try {
            jArray = new JSONArray(strReturn);
        } catch (JSONException e) {
            //Log.i("Tag","error -> "+e.getMessage().toString());
        }

        return jArray;

    }

    private List<NameValuePair> getNameValuePairs_ForgetPassword(String userid,
                                                                 String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userid)));
        nameValuePairs.add(new BasicNameValuePair("key", key));

        return nameValuePairs;

    }*/

    private void showDialogPopup(final String msg, final String btnText) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager fm = getSupportFragmentManager();
            Fragment prev = fm.findFragmentByTag("ACT_FORGET_PASS");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            PasswordSentConfirmationDialog passwordSentConfirmationDialog = PasswordSentConfirmationDialog.getInstance(msg, btnText);
            passwordSentConfirmationDialog.show(fm, "ACT_FORGET_PASS");
            ft.commit();
        } catch (Exception ex) {
            //Log.e("Ex",ex.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    public void closeActivity() {
      /*  if (asyncSendPassword != null && asyncSendPassword.getStatus() == AsyncTask.Status.RUNNING) {
            asyncSendPassword.cancel(true);
        }*/
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeActivity();
    }

    private void sendPassword() {
        showProgressBar();
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = CGlobalVariables.forgetPasswordUrl;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsNew(), 0).getMyStringRequest();
        queue.add(stringRequest);
    }

    public Map<String, String> getParamsNew() {

        String userid = etUserName.getText().toString().trim();
        String key = CUtils.getApplicationSignatureHashCode(ActForgetPassword.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userid));
        params.put("key", key);
        return params;
    }

    @Override
    public void onResponse(String response, int method) {
        try {
            hideProgressBar();
            String result = "-1";
            JSONArray jArray = new JSONArray(response);
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    result = jArray.getJSONObject(i).getString("result");
                }
            }
            MyCustomToast mct = new MyCustomToast(ActForgetPassword.this,
                    ActForgetPassword.this.getLayoutInflater(),
                    ActForgetPassword.this, regularTypeface);

            if (result.equals("0")) {
                mct.show(getResources().getString(R.string.user_id_not_exists));
            } else if (result.equals("1")) {
                showDialogPopup(getResources().getString(R.string.email_succecfully_send), getResources().getString(R.string.ok));
            } else if (result.equals("2")) {
                mct.show(getResources().getString(R.string.forgot_pass_validation_all_fields));
            } else if (result.equals("")) {
                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
            }
        } catch (JSONException e) {
        }
    }

    @Override
    public void onError(VolleyError error) {

    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(ActForgetPassword.this, regularTypeface);
        }
        pd.setCanceledOnTouchOutside(false);
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
}
