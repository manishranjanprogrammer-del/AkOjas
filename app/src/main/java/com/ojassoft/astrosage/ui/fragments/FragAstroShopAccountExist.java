package com.ojassoft.astrosage.ui.fragments;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on ४/३/१६.
 */
public class FragAstroShopAccountExist extends Fragment implements View.OnClickListener {
    private View view;
    private Button btn_continue;
    private int frameId = R.id.frame_layout;
    private FragmentTransaction ft;
    private TextView txt_change;
    private EditText edt_email, edt_password;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    private String[] iReturn;
    //LoginVerificationOnline loginVerificationOnline;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.frag_astroshop_account_exist, container, false);
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

        }
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity()
                .getApplication()).getLanguageCode();// ADDED BY HEVENDRA ON
        // 24-12-2014
        typeface = CUtils.getRobotoFont(getActivity(),
                LANGUAGE_CODE, CGlobalVariables.regular);
        btn_continue = (Button) view.findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        txt_change = (TextView) view.findViewById(R.id.txt_change);
        txt_change.setOnClickListener(this);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_password = (EditText) view.findViewById(R.id.edt_password);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:


                if (validateUsernamePass()) {
                    goToSignIn();
                }
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

    protected boolean validateUsernamePass() {
        boolean valid = true;
        if (!(edt_email.getText().toString().trim().length() > 0)) {
            valid = false;
            edt_email.setError("  ");
        } else if (!(edt_password.getText().toString().trim().length() > 0)) {
            valid = false;
            edt_password.setError("  ");
        }
        if (!CUtils.isConnectedWithInternet(getActivity())) {
            valid = false;
            MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                    .getLayoutInflater(), getActivity(), typeface);
            mct.show(getResources().getString(R.string.no_internet));
        }
        return valid;
    }

    public void goToSignIn() {
        String _userId = "", _pwd = "";
        _userId = edt_email.getText().toString();
        _pwd = edt_password.getText().toString();
        /*loginVerificationOnline = new LoginVerificationOnline(_userId, _pwd);
        loginVerificationOnline.execute();*/
    }


   /* class LoginVerificationOnline extends AsyncTask<String, Long, Void> {
        //ProgressDialog pd = null;
        CustomProgressDialog pd = null;
        String _userId = "", _pwd = "", _msg = "";

        public LoginVerificationOnline(String userId, String pwd) {
            _userId = userId;
            _pwd = pwd;

        }

        @Override
        protected void onPreExecute() {

            pd = new CustomProgressDialog(getActivity(), typeface);
            pd.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String key = CUtils.getApplicationSignatureHashCode(getActivity());
                iReturn = new ControllerManager()
                        .verifyLoginWithUserPurchasedPlan(_userId, _pwd, key);
            } catch (UICOnlineChartOperationException e) {
                iReturn[0] = "-1";
                _msg = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {

            }
            if (iReturn[0].equals("1")) {
                MyCustomToast mct = new MyCustomToast(getActivity(),
                        getActivity().getLayoutInflater(), getActivity(),
                        typeface);
                mct.show(getResources().getString(R.string.sign_in_success));
                goToAddressFragment();
                returnToMasterActivityAfterLogin(_userId, _pwd, iReturn[25]);
            } else {
                MyCustomToast mct = new MyCustomToast(getActivity(),
                        getActivity().getLayoutInflater(), getActivity(),
                        typeface);
                mct.show(getResources().getString(R.string.sign_in_failed));
                //    shakeMyViewOnSignFailed(usernamePassword_container);
            }

        }

    }*/

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


        int languageCode = ((AstrosageKundliApplication) getActivity()
                .getApplication()).getLanguageCode();
        String regid = CUtils.getRegistrationId(getActivity()
                .getApplicationContext());
        new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                getActivity().getApplicationContext(), regid, languageCode,
                userLogin);

    }

    private void goToAddressFragment() {
        Fragment frag_astroshop_address = new FragAstroShopNewAddress();
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
        ft.replace(frameId, frag_astroshop_address).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*if (loginVerificationOnline != null && loginVerificationOnline.getStatus() == AsyncTask.Status.RUNNING) {
            loginVerificationOnline.cancel(true);
        }*/
    }
}