package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.google.analytics.tracking.android.Log;
import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ojas on ११/३/१६.
 */
public class FragAstroShopRegister extends Fragment implements View.OnClickListener, VolleyResponse {
    private View view;
    private FragmentTransaction ft;
    private TextView txt_change;
    private EditText edt_email, edt_password, edt_mobile_number;
    private Button btn_register;
    private int frameId = R.id.frame_layout;
    private String emailId = "";
    private String password = "";
    private String mobileNo = "";
    private String[] iReturn;
    public Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    //AsyncTaskRegistrationOfUser asyncTaskRegistrationOfUser;
    //  WizardLogingRegister.IWizardLoginRegisterFragmentInterface iWizardLoginRegisterFragmentInterface;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.lay_astroshop_shipping_registeration, container, false);
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

        }
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();

        typeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        txt_change = (TextView) view.findViewById(R.id.txt_change);
        txt_change.setOnClickListener(this);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_password = (EditText) view.findViewById(R.id.edt_password);
        edt_mobile_number = (EditText) view.findViewById(R.id.edt_mobile_number);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
               /*
                Fragment frag_astroshop_address = new FragAstroShopNewAddress();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                ft.replace(frameId, frag_astroshop_address).commit();
                */
                registerUser();
                break;

            case R.id.txt_change:
                Fragment frag_astroshop_login = new FragAstroShopLogIn();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_to_right_finish, R.anim.right_to_left_finish);
                ft.replace(frameId, frag_astroshop_login).commit();
                break;
            default:
                break;

        }


    }

    private void registerUser() {

      /*  if (validateUserCredentials()) {
            asyncTaskRegistrationOfUser = new AsyncTaskRegistrationOfUser();
            asyncTaskRegistrationOfUser.execute();
        }*/

    }

    protected boolean validateUserCredentials() {
        boolean valid = true;
        emailId = edt_email.getText().toString().trim();
        password = edt_password.getText().toString().trim();
        mobileNo = edt_mobile_number.getText().toString().trim();

        if (TextUtils.isEmpty(emailId)) {
            valid = false;
            edt_email.setError(getResources().getString(R.string.email_one_v));
        } else if (TextUtils.isEmpty(password)) {
            valid = false;
            edt_password.setError(getResources().getString(R.string.enter_password));
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            valid = false;
            edt_email.setError(getResources().getString(R.string.email_three_v));
        } else if (TextUtils.isEmpty(mobileNo)) {
            valid = false;
            edt_mobile_number.setError(getResources().getString(R.string.astroshop_shipping_enter_your_mobile_no));
        }

        if (!CUtils.isConnectedWithInternet(getActivity())) {
            valid = false;
            MyCustomToast mct = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), typeface);
            mct.show(getResources().getString(R.string.no_internet));
        }
        return valid;
    }

    CustomProgressDialog pd = null;


    /*private class AsyncTaskRegistrationOfUser extends AsyncTask<String, String, String> {

        String[] status = new String[2];

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new CustomProgressDialog(getActivity(), typeface);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String key = keyToSignUp();
                status = new ControllerManager()
                        .userSignUp(emailId, password, key);
            } catch (UICOnlineChartOperationException e) {
                //Log.i(e.getMessage().toString());
                status[0] = "0";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Sign Up Successfully.
            if (status[0].equals("1")) {
                new LoginVerificationOnline().execute();
            } else {
                try {
                    if (pd != null & pd.isShowing())
                        pd.dismiss();
                } catch (Exception e) {
                    //Log.i(e.getMessage().toString());
                }

                //All Fields of sign up form are mandatory.
                if (status[0].equals("2")) {
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_all_fields));
                }
                //User Id already exist.
                else if (status[0].equals("3")) {
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_user_already_exists));
                    onBackPressedClick();
                }
                //Authentication failed.
                else if (status[0].equals("4")) {
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                } else {
                    //0 or other condition
                    //Sign Up Failed.
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_failed));
                }

            }
        }
    }*/

    /*class LoginVerificationOnline extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String key = CUtils.getApplicationSignatureHashCode(getActivity());
                iReturn = new ControllerManager()
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
                iReturn[0] = "-1";
                //Log.i(e.getMessage().toString());
            }


            showCustomMsg(getResources().getString(R.string.sign_up_success));
            goToAddressFragment();

            if (iReturn[0].equals("1")) {
                CUtils.signUpFirebaseEvent();
                returnToMasterActivityAfterLogin(emailId, password, iReturn[25]);
            } else {
                onBackPressedClick();
            }
        }

    }*/

    private void showCustomMsg(String msg) {

        MyCustomToast mct = new MyCustomToast(getActivity(),
                getActivity().getLayoutInflater(), getActivity(),
                typeface);
        mct.show(msg);
    }

    private void onBackPressedClick() {
        //this.finish();
        // iWizardLoginRegisterFragmentInterface.clickedSignUpButton(0);//0 for login
    }

    private void returnToMasterActivityAfterLogin(String userLogin, String pwd, String userName) {
        CUtils.saveLoginDetailInPrefs(getActivity(), userLogin, pwd, userName, true, false);
        // Clear old userid and password from old app
        SharedPreferences settings = getActivity().getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CGlobalVariables.PREF_USER_ID, "");
        editor.putString(CGlobalVariables.PREF_USERR_PWD, "");
        editor.commit();

        CUtils.storeUserPurchasedPlanInPreference(getActivity(), Integer.parseInt(iReturn[1]));

        CUtils.saveStringData(getActivity(), CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, iReturn[2]);//Purchase plan Date
        CUtils.saveStringData(getActivity(), CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, iReturn[3]);//Expiry plan  Date
        //   iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
        //          pwd);

        // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
        // SERVER
        int languageCode = ((AstrosageKundliApplication) getActivity()
                .getApplication()).getLanguageCode();
        String regid = CUtils.getRegistrationId(getActivity()
                .getApplicationContext());
        new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                getActivity().getApplicationContext(), regid, languageCode,
                userLogin);

    }

    private String keyToSignUp() {

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //String date = calendar.
        int year = calendar.get(Calendar.YEAR);
        int mnt = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int month = mnt + 1;

        int totalNumber = ((year * 2) + month + day) - 78;

        return String.valueOf(totalNumber);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // iWizardLoginRegisterFragmentInterface = (WizardLogingRegister.IWizardLoginRegisterFragmentInterface) activity;
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*if (asyncTaskRegistrationOfUser != null && asyncTaskRegistrationOfUser.getStatus() == AsyncTask.Status.RUNNING) {
            asyncTaskRegistrationOfUser.cancel(true);
        }*/
        activity = null;
    }

    private void goToAddressFragment() {
        Fragment frag_astroshop_address = new FragAstroShopNewAddress();
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
        ft.replace(frameId, frag_astroshop_address).commit();
    }

    @Override
    public void onResponse(String response, int method) {

    }

    @Override
    public void onError(VolleyError error) {

    }
}
