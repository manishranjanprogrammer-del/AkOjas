package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.google.analytics.tracking.android.Log;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UICOnlineChartOperationException;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

/*
 * @date : 11 jan 2016
 * @description : This class is used to register the user in server
 */
public class ActSignUp extends Activity {

    EditText edtMailId, edtPassword;
    Button btnSignUp, btnBackPressed;
    TextView tvEmailId, tvPassword, tvHeading, tvNotes;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;

    String emailId = "";
    String password = "";
    String[] iReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_wizard_signup);

        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();

        typeface = CUtils.getRobotoFont(ActSignUp.this, LANGUAGE_CODE, CGlobalVariables.regular);

        setLayRef();
    }

    /*
     * @date : 11 jan 2016
     * @description : This method is used to look up the XMl fields
     */
    private void setLayRef() {
        edtMailId = (EditText) findViewById(R.id.edtMailId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnBackPressed = (Button) findViewById(R.id.btnBackPressed);
        TextView signUpTitleTV = (TextView) findViewById(R.id.font_auto_lay_wizard_signup_1);
        tvEmailId = (TextView) findViewById(R.id.tvEmailId);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        //tvNotes = (TextView)findViewById(R.id.tvNotes);

        //set type face of all fields
        tvEmailId.setTypeface(typeface);
        tvPassword.setTypeface(typeface);
        FontUtils.changeFont(this, signUpTitleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        tvHeading.setTypeface(typeface);
        btnSignUp.setTypeface(typeface);
        btnBackPressed.setTypeface(typeface);
        /*tvNotes.setTypeface(typeface);
        Spanned note = Html.fromHtml(getString(R.string.login_note));
		tvNotes.setText(note);*/

        //Set the click listener of fields
        btnSignUp.setOnClickListener(new ButtonClickListener());
        btnBackPressed.setOnClickListener(new ButtonClickListener());
    }

    /*
     * @date : 11 jan 2016
     * @description : This class is used to set the Click listener.
     */
    private class ButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {

                case R.id.btnSignUp:
                    //registerUser();
                    break;

                case R.id.btnBackPressed:
                    onBackPressedClick();
                    break;

                default:
                    break;
            }
        }

    }

    /*
     * @date : 11 jan 2016
     * @description : This method is used to register the user into server
     */
   /* private void registerUser() {

        if (validateUserCredentials()) {
            new AsyncTaskRegistrationOfUser().execute();
        }

    }*/

    /*
     * @date : 11 jan 2016
     * @description : This method is used to validate user credentials
     */
    protected boolean validateUserCredentials() {
        boolean valid = true;
        emailId = edtMailId.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailId)) {
            valid = false;
            edtMailId.setError(getResources().getString(R.string.email_one_v));
        } else if (TextUtils.isEmpty(password)) {
            valid = false;
            edtPassword.setError(getResources().getString(R.string.enter_password));
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            valid = false;
            edtMailId.setError(getResources().getString(R.string.email_three_v));
        }

        if (!CUtils.isConnectedWithInternet(ActSignUp.this)) {
            valid = false;
            MyCustomToast mct = new MyCustomToast(ActSignUp.this, ActSignUp.this.getLayoutInflater(), ActSignUp.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        }
        return valid;
    }

    /*
     * @date : 11 jan 2016
     * @description : This method is used to finish the current activity
     */
    private void onBackPressedClick() {
        this.finish();
    }

    /*
     * @date : 11 jan 2016
     * @description : This Async task class is used to register the user into server
     */
    CustomProgressDialog pd = null;

    /*private class AsyncTaskRegistrationOfUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new CustomProgressDialog(ActSignUp.this, typeface);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result.equals("1")) {
                new LoginVerificationOnline().execute();
            } else {
                try {
                    if (pd != null & pd.isShowing())
                        pd.dismiss();
                } catch (Exception e) {
                    //Log.i(e.getMessage().toString());
                }
            }
        }
    }*/

   /* class LoginVerificationOnline extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String key = CUtils.getApplicationSignatureHashCode(ActSignUp.this);
                iReturn = new com.ojassoft.astrosage.controller.ControllerManager()
                        .verifyLoginWithUserPurchasedPlan(emailId, password, key);
            } catch (UICOnlineChartOperationException e) {
                //Log.i(e.getMessage().toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                //Log.i(e.getMessage().toString());
            }

            if (iReturn[0].equals("1")) {
                MyCustomToast mct = new MyCustomToast(ActSignUp.this,
                        ActSignUp.this.getLayoutInflater(), ActSignUp.this,
                        typeface);
                mct.show(getResources().getString(R.string.sign_in_success));
                returnToMasterActivityAfterLogin(emailId, password, iReturn[25]);
            }
        }

    }*/

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        typeface = CUtils.getRobotoFont(ActSignUp.this, LANGUAGE_CODE, CGlobalVariables.regular);
    }

   /* private void returnToMasterActivityAfterLogin(String userLogin, String pwd, String userName) {
        CUtils.saveLoginDetailInPrefs(ActSignUp.this, userLogin, pwd, userName, true, false);
        // Clear old userid and password from old app
        SharedPreferences settings = ActSignUp.this.getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CGlobalVariables.PREF_USER_ID, "");
        editor.putString(CGlobalVariables.PREF_USERR_PWD, "");
        editor.commit();

        CUtils.storeUserPurchasedPlanInPreference(ActSignUp.this, Integer.parseInt(iReturn[1]));

        CUtils.saveStringData(ActSignUp.this, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, iReturn[2]);//Purchase plan Date
        CUtils.saveStringData(ActSignUp.this, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, iReturn[3]);//Expiry plan  Date
        *//*iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
                pwd);*//*

        // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
        // SERVER
        int languageCode = ((AstrosageKundliApplication) ActSignUp.this
                .getApplication()).getLanguageCode();
        String regid = CUtils.getRegistrationId(ActSignUp.this
                .getApplicationContext());
        new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                ActSignUp.this.getApplicationContext(), regid, languageCode,
                userLogin);

    }*/


}
