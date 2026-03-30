package com.ojassoft.astrosage.ui.fragments;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.textfield.TextInputLayout;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.act.ActAstroShopShippingDetails;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

//import com.google.analytics.tracking.android.Log;

/**
 * Created by ojas on ४/३/१६.
 */
public class FragAstroShopLogIn extends Fragment implements View.OnClickListener {
    private View view;
    private Button btn_continue;
    private final int frameId = R.id.frame_layout;
    private FragmentTransaction ft;
    private RequestQueue queue;
    Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private CustomProgressDialog pd = null;
    private EditText edt_email;
    private String email_ID = "";
    private String user_Password = "";
    private String user_Name = "";
    private Bundle bundle;
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private int noOfItem = 1;
    public AstroShopItemDetails itemdetails;
    private TextView txtdetails;
    private TextInputLayout edt_email_layout;
    private String title;
    private final Boolean isFromCart = false;

    private boolean isEmailDialogueOpened;
    private String selectedEmailId;
    private Activity activity;
    private CredentialManager credentialManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.lay_astroshop_shipping_login, container, false);
        } else {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }


        }
        activity = getActivity();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getActivity().getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        btn_continue = view.findViewById(R.id.btn_continue);
        btn_continue.setTypeface(typeface);
        txtdetails = view.findViewById(R.id.txtdetails);
        txtdetails.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        edt_email = view.findViewById(R.id.edt_email);
        edt_email_layout = view.findViewById(R.id.edt_email_layout);
        edt_email.addTextChangedListener(new MyTextWatcher(edt_email));

        bundle = this.getArguments();
        if (bundle != null && !isFromCart) {
            itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");
            noOfItem = bundle.getInt("ItemNo");

        }
        credentialManager = CredentialManager.create(getActivity());

        btn_continue.setOnClickListener(this);
        ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.LOGIN_ENABLE);
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        String email_id = CUtils.getAstroshopUserEmail(getActivity());
        if (CUtils.isUserLogedIn(getActivity())) {
            user_Name = CUtils.getUserName(getActivity());
            user_Password = CUtils.getUserPassword(getActivity());
            if (user_Name != null && user_Name != "" && CUtils.isValidEmail(user_Name)) {
                edt_email.setText(user_Name);
            }
        } else if (!email_id.isEmpty()) {
            edt_email.setText(email_id);

        }
        if (TextUtils.isEmpty(edt_email.getText().toString())) {
            //edt_email.setFocusable(false);
            edt_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //requestCredentials();
                }
            });
        }
        return view;
    }


    private void setEmailIdFromUserAccount() {
        String email = UserEmailFetcher.getEmail(getActivity());
        if (email != null) {
            edt_email.setText(email);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:

                email_ID = edt_email.getText().toString().trim();
                if (edt_email.getText().toString().trim().isEmpty()) {
                    edt_email_layout.setErrorEnabled(true);
                    edt_email_layout.setError(getResources().getString(R.string.email_one_v));
                    edt_email.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    requestFocus(edt_email);


                    //   edt_email.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                } else if (!CUtils.isValidEmail(email_ID)) {
                    edt_email_layout.setErrorEnabled(true);
                    edt_email_layout.setError(getResources().getString(R.string.email_one_v_astro_service));
                    edt_email.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    requestFocus(edt_email);


                    //   edt_email.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                } else {
                    if (!CUtils.isConnectedWithInternet(getActivity())) {
                        MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                                .getLayoutInflater(), getActivity(), typeface);
                        mct.show(getResources().getString(R.string.no_internet));

                    } else {
                        checkLoginUser(email_ID, user_Password);
                    }
                }

                break;
        }

    }

    /***************************** Gmail choose dialogue start **********************/


    public void requestCredentials() {
        if (!isEmailDialogueOpened) {
            showGmailAccountPicker();
        }
    }

    public void showGmailAccountPicker() {
        if(activity == null){
            activity = getActivity();
        }
        // Configure Google ID option
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Show all accounts
                .setServerClientId(getString(R.string.default_web_client_id)) // Required
                .build();

        // Build the request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        // Launch Credential Manager UI
        credentialManager.getCredentialAsync(
                activity,
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback() {
                    @Override
                    public void onResult(Object responseObj) {
                        isEmailDialogueOpened = true;
                        setFocusOnEmail();
                        handleSignIn((GetCredentialResponse) responseObj);
                    }

                    @Override
                    public void onError(@NonNull Object o) {
                        isEmailDialogueOpened = true;
                        setFocusOnEmail();
                    }
                }
        );
    }

    private void handleSignIn(GetCredentialResponse credentialResponse) {
        try {
            Credential credential = credentialResponse.getCredential();
            if (credential instanceof CustomCredential && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                // Create Google ID Token
                Bundle credentialData = credential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
                selectedEmailId = googleIdTokenCredential.getId();
                activity.runOnUiThread(() -> {
                    try {
                        CUtils.saveAstroshopUserEmail(activity, selectedEmailId);
                        edt_email.setText(selectedEmailId);
                    } catch (Exception e) {
                        //
                    }
                });
            }
        }catch (Exception e){
            //
        }
    }

    private void setFocusOnEmail() {
        try {
            activity.runOnUiThread(() -> {
                edt_email.setFocusableInTouchMode(true);
                edt_email.setFocusable(true);
                edt_email.requestFocus();
            });
        } catch (Exception e){
            //
        }
    }

    /***************************** Gmail choose dialogue end **********************/

    private void checkLoginUser(final String email_ID, final String user_Password) {

        pd = new CustomProgressDialog(getActivity(), typeface);
        pd.show();
        pd.setCancelable(false);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShopLogInsLive,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("onResponse+", response.toString());

                        //Log.e("Convert String+" + response.toString());
                        if (response != null && !(response.isEmpty())) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                String stringresponse = obj.getString("Result");
                                JSONObject fullAdress = obj.getJSONObject("FullAddress");

                                if (stringresponse.equalsIgnoreCase("1") || stringresponse.equalsIgnoreCase("3")) {
                                    CUtils.saveAstroshopUserAddressDetailInPrefs(getActivity(), fullAdress.toString());
                                    CUtils.saveAstroshopUserEmail(getActivity(), edt_email.getText().toString().trim());
                                    //Log.e(fullAdress.toString());
                                    MyCustomToast mct = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), typeface);
                                    mct.show(getResources().getString(R.string.sign_in_success));
                                    Fragment frag_astroshop_address = new FragAstroShopNewAddress();
                                    if (bundle != null && !isFromCart) {
                                        bundle.putSerializable("key", itemdetails);
                                        bundle.putInt("ItemNo", noOfItem);

                                    }
                                    frag_astroshop_address.setArguments(bundle);
                                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                                    ft.replace(frameId, frag_astroshop_address).commit();
                                    CUtils.saveIsUserInShipping(getActivity(), true);

                                    String title = "";
                                    title = getItemName();
                                    CUtils.googleAnalyticSendWitPlayServieForAstroshop(getActivity(),
                                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_LOGIN,
                                            title, null);
                                    String labell = "astroshop_login_" + title;
                                    CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_SIGN_IN_OUT_SUCESS, "");
                                    //Log.e("GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_LOGIN" + title);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        pd.dismiss();
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onResponse+", error.toString());
                VolleyLog.d("Error: " + error.getMessage());
                //   mTextView.setText("That didn't work!");

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                pd.dismiss();
            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=raj-8";
            }


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() {
                //Log.e(email_ID + user_Password);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Key", CUtils.getApplicationSignatureHashCode(getActivity()));
                params.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(email_ID));
                params.put(KEY_PASSWORD, user_Password);
                params.put("deviceid", CUtils.getMyAndroidId(getActivity()));

                //Log.e("loginrequest", params.toString());
                return params;
            }

        };


        // Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private String getItemName() {
        if (itemdetails != null) {

            title = itemdetails.getPName();
        }
        return title;

    }

    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_email:
                    if (edt_email.getText().toString().trim().isEmpty()) {
                        edt_email_layout.setErrorEnabled(true);
                        edt_email_layout.setError(getResources().getString(R.string.email_one_v));
                        requestFocus(edt_email);
                        edt_email.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else if (!CUtils.isValidEmail(edt_email.getText().toString().trim())) {
                        edt_email_layout.setErrorEnabled(true);
                        edt_email_layout.setError(getResources().getString(R.string.email_one_v_astro_service));
                        requestFocus(edt_email);
                        edt_email.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else {
                        edt_email_layout.setError(null);
                        edt_email.getBackground().setColorFilter(null);
                        edt_email.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                    }
                    break;

            }
        }
    }

    public void checkForPermission(boolean isUSerPermissionDialogOpen) {
        boolean hasEmailIdPermission = CUtils.isContactsPermissionGranted(getActivity(), this, BaseInputActivity.PERMISSION_CONTACTS, isUSerPermissionDialogOpen);
        if (hasEmailIdPermission) {
            setEmailIdFromUserAccount();
        }
    }

    public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
        //if(isPermissionGranted){
        checkForPermission(true);
        // }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (getActivity() != null && getActivity() instanceof BaseInputActivity && grantResults != null && grantResults.length > 0) {
            if (requestCode == BaseInputActivity.PERMISSION_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setEmailIdFromUserAccount();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(getActivity(), CGlobalVariables.PERMISSION_KEY_CONTACTS, true);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}