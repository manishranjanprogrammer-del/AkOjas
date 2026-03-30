package com.ojassoft.astrosage.ui.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;
import com.libojassoft.android.beans.LibOutPlace;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanUserMapping;
import com.ojassoft.astrosage.jinterface.SmsListener;
import com.ojassoft.astrosage.misc.AutoCompleteCityAdapter;
import com.ojassoft.astrosage.misc.SmsReceiver;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

;

/**
 * Created by ojas on 16/8/17.
 * This class is used as a fragment, asking for user details is Astrologer or not
 */

public class FragAskForAstrologer extends DialogFragment implements Animation.AnimationListener, AdapterView.OnItemSelectedListener {

    Activity activity;
    Typeface typeface;
    Typeface robotoMedium;
    Typeface regulerTypeface;
    // private RadioButton rbYes, rbNo;
    TextView tvTitle1;
    TextView tvTitle2;
    TextView tvTitle3;
    TextView tvContent;
    TextView tvBenefits1;
    TextView tvBenefits2;
    TextView tvBenefits3;
    TextView requiredTextView;
    TextView otpDescTextView;
    TextView descTextView1, descTextView2, descTextView3;
    TextView resendTextView;
    Button yesButton, joinButton, cancelButton1, cancelButton2, verifyButton, nextButton, editButton;
    LinearLayout layout1, layout2, layout3, layout4;
    Animation animOut, animIn;
    TextInputLayout nameTextInputLayout, phoneTextInputLayout, cityTextInputLayout, aboutTextInputLayout;
    EditText nameEditText, phoneEditText, aboutEditText, phoneNumberEditText1, otpEditText;
    AutoCompleteTextView cityEditText;
    ArrayList<LibOutPlace> places;
    ProgressBar progressBar;
    //AnimateHorizontalProgressBar determineProgressBar1;
    int clickedButtonId;
    TextView timerTextView;
    CounterClass timer;
    Thread thread;
    int counter = 60;
    String phoneNumber;
    ProgressBar progressBar1, otpProgressbar, linearProgressbar;
    String otp;
    String visibleLayout;
    String state = "", country = "";
    static int msgCounter;
    boolean isButtonClicked = false;

    public FragAskForAstrologer(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //beanUserMapping = CUtils.getUserMappingData(activity);

        String currentDate = currentDate();
        String savedDate = CUtils.getStringData(activity, "savedDate", "");
        if (!currentDate.equals(savedDate)) {
            msgCounter = 0;
        } else {
            msgCounter = CUtils.getIntData(activity, "msgCounter", 0);
        }

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (clickedButtonId == R.id.btnyes) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);
            CUtils.saveIntData(activity, visibleLayout, 0);
        } else if (clickedButtonId == R.id.btn_join_now) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.GONE);

            descTextView2.setText("+91 " + phoneNumber);
            CUtils.saveIntData(activity, visibleLayout, 1);
        } else if (clickedButtonId == R.id.btnnext) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.VISIBLE);
            CUtils.saveIntData(activity, visibleLayout, 1);
            Runnable myRunnableThread = new CountDownRunner();
            if (thread == null) {
                thread = new Thread(myRunnableThread);
            }
            if (!thread.isAlive()) {
                thread.start();
            }
            // timer.start();
        } else if (clickedButtonId == R.id.btnverify) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.VISIBLE);
            // timer.start();
        } else if (clickedButtonId == R.id.btnedit) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);
            // timer.start();
        } else if (clickedButtonId == R.id.otp_desc_text) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        typeface = ((BaseInputActivity) activity).robotRegularTypeface;
        robotoMedium = ((BaseInputActivity) activity).robotMediumTypeface;
        regulerTypeface = ((BaseInputActivity) activity).mediumTypeface;
        CUtils.saveBooleanData(activity, CGlobalVariables.isShowAstrologerDirectoryInMainMenu, true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View view = inflater.inflate(R.layout.lay_ask_for_astrologer, container);
        timer = new CounterClass(60000, 1000);
        animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right);
        animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left);
        animOut.setAnimationListener(this);
        animIn.setAnimationListener(this);
        setLayRef(view);
        setFeildValues();
        setClickListners();
        cityEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                LibOutPlace libOutPlace = (LibOutPlace) parent.getItemAtPosition(position);
                state = libOutPlace.getState();
                country = libOutPlace.getCountry();
            }
        });
        int visibleNumber = CUtils.getIntData(activity, visibleLayout, 0);
        if (visibleNumber == 0) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);
        } else if (visibleNumber == 1) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.GONE);
        }
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text", messageText);
                otp = messageText;
                otpEditText.setText(messageText);
            }
        });

        return view;
    }

    /**
     * This method is used to set the layout references
     *
     * @param view
     */
    private void setLayRef(View view) {
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressbar1);
        linearProgressbar = (ProgressBar) view.findViewById(R.id.linear_progressbar);
        otpProgressbar = (ProgressBar) view.findViewById(R.id.otp_progressbar);
        tvTitle1 = (TextView) view.findViewById(R.id.tvTitle1);
        tvTitle2 = (TextView) view.findViewById(R.id.tvTitle2);
        tvTitle3 = (TextView) view.findViewById(R.id.tvTitle3);
        tvContent = (TextView) view.findViewById(R.id.tvcontent);
        tvBenefits1 = (TextView) view.findViewById(R.id.benefit1);
        tvBenefits2 = (TextView) view.findViewById(R.id.benefit2);
        tvBenefits3 = (TextView) view.findViewById(R.id.benefit3);
        descTextView1 = (TextView) view.findViewById(R.id.desc_textview1);
        descTextView2 = (TextView) view.findViewById(R.id.desc_textview2);
        descTextView3 = (TextView) view.findViewById(R.id.desc_textview3);
        requiredTextView = (TextView) view.findViewById(R.id.required_field);
        otpDescTextView = (TextView) view.findViewById(R.id.otp_desc_text);
        yesButton = (Button) view.findViewById(R.id.btnyes);
        joinButton = (Button) view.findViewById(R.id.btn_join_now);
        nextButton = (Button) view.findViewById(R.id.btnnext);
        editButton = (Button) view.findViewById(R.id.btnedit);
        cancelButton1 = (Button) view.findViewById(R.id.btncacel);
        cancelButton2 = (Button) view.findViewById(R.id.btncacel1);
        verifyButton = (Button) view.findViewById(R.id.btnverify);
        layout1 = (LinearLayout) view.findViewById(R.id.lay1);
        layout2 = (LinearLayout) view.findViewById(R.id.lay2);
        layout3 = (LinearLayout) view.findViewById(R.id.lay3);
        layout4 = (LinearLayout) view.findViewById(R.id.lay4);


        nameTextInputLayout = (TextInputLayout) view.findViewById(R.id.name_inputLayabout);
        phoneTextInputLayout = (TextInputLayout) view.findViewById(R.id.phone_inputLayabout);
        cityTextInputLayout = (TextInputLayout) view.findViewById(R.id.city_inputLayabout);
        aboutTextInputLayout = (TextInputLayout) view.findViewById(R.id.about_inputLayabout);
        nameEditText = (EditText) view.findViewById(R.id.name_edittext);
        phoneEditText = (EditText) view.findViewById(R.id.phone_edittext);
        cityEditText = (AutoCompleteTextView) view.findViewById(R.id.city_edittext);
        aboutEditText = (EditText) view.findViewById(R.id.about_edittext);
        //  phoneNumberEditText = (EditText) view.findViewById(R.id.phone_number_edittext);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        // determineProgressBar = (AnimateHorizontalProgressBar) view.findViewById(R.id.progressBar);
        otpEditText = (EditText) view.findViewById(R.id.otp_edittext);
        timerTextView = (TextView) view.findViewById(R.id.time_textview);
        resendTextView = (TextView) view.findViewById(R.id.resend_textview);
       /* determineProgressBar.setProgress(50);
        determineProgressBar.*/
        //setProgressValue(10);
        if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.HINDI) {
            cancelButton1.setText(getResources().getString(R.string.hindi_cancel));
            cancelButton2.setText(getResources().getString(R.string.hindi_cancel));
        } else {
            cancelButton1.setText(getResources().getString(R.string.cancel));
            cancelButton2.setText(getResources().getString(R.string.cancel));
        }


        nameEditText.addTextChangedListener(new MyTextWatcher(nameEditText));
        phoneEditText.addTextChangedListener(new MyTextWatcher(phoneEditText));
        cityEditText.addTextChangedListener(new MyTextWatcher(cityEditText));
        aboutEditText.addTextChangedListener(new MyTextWatcher(aboutEditText));
        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (cityEditText.getText().length() == 3 && (places == null || places.size() == 0)) {
                    searchCityOnline();
                }
                if (cityEditText.getText().length() == 0) {
                    if (places != null) {
                        places.clear();
                    }

                }
            }
        });
        otpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpEditText.getText().length() == 4) {
                    otp = otpEditText.getText().toString();
                    if (CUtils.isConnectedWithInternet(activity)) {
                        new SendOTPOnServer(otp).execute();
                    } else {
                        Toast.makeText(activity, getResources().getString(R.string.internet_is_not_working), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        //setAutoCompleteTextAdapter();
        setTypefaceOfViews();
       /* determineProgressBar.setMax(100);
        determineProgressBar.setProgress(1);

        determineProgressBar.setAnimateProgressListener(new AnimateHorizontalProgressBar.AnimateProgressListener() {
            @Override
            public void onAnimationStart(int progress, int max) {
                // do nothing
            }

            @Override
            public void onAnimationEnd(int progress, int max) {
               *//* mTxtProgress.setText(getString(R.string.progress, progress));
                mTxtMax.setText(getString(R.string.max, max));*//*
            }
        });*/


    }

    /**
     * This method is used to set the click listeners
     */


    private void setClickListners() {

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setData(1);
                clickedButtonId = R.id.btnyes;
                layout1.startAnimation(animOut);
                //layout2.startAnimation(animIn);
                // setViewAnimation();
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {

                    //setData(0);
                    if (CUtils.isConnectedWithInternet(activity)) {
                        if (!isButtonClicked) {
                            isButtonClicked = true;
                            sendProfileInfoOnGcmServer();

                        } else {
                            //Toast.makeText(activity, "not able to click", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        showToast(getResources().getString(R.string.internet_is_not_working));
                    }

                }

            }
        });
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setProgressValue(1);


                /*ObjectAnimator progressAnimator = ObjectAnimator.ofFloat(determineProgressBar, "progress", 100.0, 0.0);
                progressAnimator.setDuration(30000);
                progressAnimator.setInterpolator(new LinearInterpolator());
                progressAnimator.start();*/
                //determineProgressBar.setProgressWithAnim(100);
                //clickedButtonId = R.id.btnnext;
                //timer.start();


            }

        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call a function
                clickedButtonId = R.id.btnnext;
                if (msgCounter < 3) {
                    verifyMobileNumber();
                } else {
                    showToast("Your today limit is over");
                }


            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call a function
                clickedButtonId = R.id.btnedit;
                layout3.startAnimation(animOut);


            }
        });
        cancelButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // FragAskForAstrologer.this.dismiss();
                doActionOnCancelButton();

            }
        });
        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // FragAskForAstrologer.this.dismiss();
                doActionOnCancelButton();

            }
        });
        resendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButtonId = R.id.resend_textview;
                if (msgCounter < 3 && counter == -1) {
                    if (CUtils.isConnectedWithInternet(activity)) {
                        verifyMobileNumber();
                    } else {
                        showToast(getResources().getString(R.string.internet_is_not_working));
                    }

                    Runnable myRunnableThread = new CountDownRunner();
                    if (thread == null) {
                        thread = new Thread(myRunnableThread);
                    }

                    if (!thread.isAlive()) {
                        counter = 60;
                        thread.start();
                    }
                } else {
                    if (msgCounter == 3) {
                        showToast("Your today limit is over");
                    } else {
                        showToast("Try after one minute");
                    }

                }


            }
        });


    }

   /* private void setData(int val) {
        try {
            BeanUserMapping beanUserMapping = CUtils.getUserMappingData(activity);
            if (beanUserMapping == null) {
                beanUserMapping = new BeanUserMapping();
            }
            beanUserMapping.setIsAstrologer(val);
            beanUserMapping.setStatus(1);//Data need to sent to server
            CUtils.saveUserMappingData(activity, beanUserMapping);
            sendUSerToProfileScreen();

            this.dismiss();

        } catch (Exception ex) {
            Log.e("FragAskForAstrologer", ex.getMessage());
        }
    }*/

   /* public static FragAskForAstrologer getInstance(){
        FragAskForAstrologer askForAstrologer = new FragAskForAstrologer();
        return askForAstrologer;
    }*/

    /*private void sendUSerToProfileScreen() {
        String className = activity.getLocalClassName();
        Intent intent = new Intent(activity, EditProfileActivity.class);
        intent.putExtra("password", CUtils.getUserPassword(getActivity()));
        intent.putExtra("activity", className);
        intent.putExtra("dologout", false);
        startActivityForResult(intent, 111);
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    private void setTypefaceOfViews() {
        tvTitle1.setTypeface(robotoMedium);
        tvTitle2.setTypeface(robotoMedium);
        tvContent.setTypeface(typeface);
        tvBenefits1.setTypeface(typeface);
        tvBenefits2.setTypeface(typeface);
        tvBenefits3.setTypeface(typeface);
        yesButton.setTypeface(typeface);
        joinButton.setTypeface(typeface);
        requiredTextView.setTypeface(typeface);
        descTextView1.setTypeface(typeface);
        descTextView2.setTypeface(robotoMedium);
        descTextView3.setTypeface(typeface);
        tvTitle3.setTypeface(robotoMedium);
        //phoneNumberEditText.setTypeface(typeface);
        nextButton.setTypeface(typeface);
        editButton.setTypeface(typeface);
        cancelButton1.setTypeface(typeface);
        cancelButton2.setTypeface(typeface);
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //int height = metrics.heightPixels;
        final float scale = metrics.density;
        int height = (int) (500 * scale);
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = width - 40;
            //wmlp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            wmlp.height = height;
            //wmlp.height = height - 250;
            dialog.getWindow().setAttributes(wmlp);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            //counter.setText(getResources().getString(R.string.startbracket) + String.valueOf(500 - s.length()) + " " + getResources().getString(R.string.charater) + getResources().getString(R.string.endbracket));

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.name_edittext:
                    validateName(nameEditText, nameTextInputLayout, getString(R.string.please_enter_name_v));
                    break;
                case R.id.phone_edittext:
                    validateName(phoneEditText, phoneTextInputLayout, getString(R.string.email_one_v));
                    break;
                case R.id.city_edittext:
                    validateName(cityEditText, cityTextInputLayout, getString(R.string.mandatory_fields));
                    break;
                case R.id.about_edittext:
                    validateName(aboutEditText, aboutTextInputLayout, getString(R.string.feedback_message_one_v));
                    break;

            }
        }
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        boolean value = true;
        if (name == nameEditText) {
            if (name.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (name.getText().toString().trim().length() > 50) {
                inputLayout.setError(getResources().getString(R.string.name_limit_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (name == cityEditText) {
            if (name.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (name.getText().toString().trim().length() > 70) {
                inputLayout.setError(getResources().getString(R.string.email_two_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (name == aboutEditText) {
            if (name.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (name.getText().toString().trim().length() > 500) {
                inputLayout.setError(getResources().getString(R.string.feedback_message_two_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (name == phoneEditText) {
            if (name.getText().toString().trim().length() > 0 && name.getText().toString().trim().length() < 10) {
                inputLayout.setError(getResources().getString(R.string.phone_validation_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    private boolean validateData() {
        boolean flag = false;
        if (validateName(nameEditText, nameTextInputLayout, getString(R.string.please_enter_name_v))
                && validateName(phoneEditText, phoneTextInputLayout, getString(R.string.email_one_v))
                && validateName(cityEditText, cityTextInputLayout, "Please enter your current city.")
                && validateName(aboutEditText, aboutTextInputLayout, "Please write about yourself")) {
            flag = true;
        }

        return flag;
    }


    private void sendProfileInfoOnGcmServer() {

        BeanUserMapping beanUserMapping = CUtils.getUserMappingData(activity);
        if (beanUserMapping == null) {
            beanUserMapping = new BeanUserMapping();
        }
        beanUserMapping.setStatus(1);
        beanUserMapping.setName(nameEditText.getText().toString());
        beanUserMapping.setPhoneNo(phoneEditText.getText().toString());
        beanUserMapping.setCity(cityEditText.getText().toString());
        if (!state.equals("")) {
            beanUserMapping.setState(state);
        }
        if (!country.equals("")) {
            beanUserMapping.setCountry(country);

        }
        beanUserMapping.setAbout(aboutEditText.getText().toString());
        beanUserMapping.setIsAstrologer(1);
        beanUserMapping.setDeviceId(CUtils.getMyAndroidId(activity));
        new SendAstrologerInfoOnserver(beanUserMapping).execute();

    }


    private void setAutoCompleteTextAdapter() {
// Get the string array
        ArrayList<LibOutPlace> cityList = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            cityList.add(places.get(i));
        }
// Create the adapter and set it to the AutoCompleteTextView
        AutoCompleteCityAdapter adapter = new AutoCompleteCityAdapter(activity, R.layout.lay_ask_for_astrologer, cityList);
        cityEditText.setAdapter(adapter);

    }

    private void searchCityOnline() {
        MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((BaseInputActivity) activity).regularTypeface);
        if (CUtils.isConnectedWithInternet(getActivity())) {
            SearchCityFromAstroSageAtlas searchCityFromAstroSageAtlas = new SearchCityFromAstroSageAtlas(cityEditText.getText().toString().trim());
            searchCityFromAstroSageAtlas.execute();
        } else {
            mct.show(getResources().getString(R.string.no_internet));
        }

    }

    private class SearchCityFromAstroSageAtlas extends
            AsyncTask<String, Long, Void> {


        String searchText;

        // CustomProgressDialog pd = null;

        public SearchCityFromAstroSageAtlas(String searchText) {
            this.searchText = searchText;
        }


        @Override
        protected void onPreExecute() {
            // pd = new CustomProgressDialog(getActivity(), typeface);
            // pd.show();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {

               // places = new CAstroSageAtlas().searchPlace(searchText);

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                try {
                    if (progressBar != null)
                        progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    Log.i("Exception", e.getMessage());
                }

                if (places != null && places.size() > 0) {
                    // isCityHasBeenSearched = true;
                    setAutoCompleteTextAdapter();
                    cityEditText.showDropDown();
                    cityEditText.setThreshold(1);
                    //((AutoCompleteCityAdapter) cityEditText.getAdapter()).refreshList(cityEditText.getText().toString());
                } else {
                    MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((ActPlaceSearch) activity).regularTypeface);
                    mct.show(getResources().getString(R.string.city_not_found));
                }
            } catch (Exception ex) {
                Log.i("Exception", ex.getMessage() + "");
            }
        }

    }

    /*private void setProgressValue(final int progress) {

        // set the progress
        //determineProgressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(progress + 1);
            }
        });
        thread.start();
    }
*/
    public void setOTP(String msg) {
        otpEditText.setText(msg);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer {
        long millis;

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            timerTextView.setText("Completed.");
        }

        @SuppressLint("NewApi")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onTick(long millisUntilFinished) {

            millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println(hms);

            timerTextView.setText(hms);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    public void doWork() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (counter <= 0) {
                        thread.interrupt();
                        thread = null;
                        timerTextView.setVisibility(View.INVISIBLE);
                        resendTextView.setTextColor(Color.BLUE);
                    } else {
                        timerTextView.setVisibility(View.VISIBLE);
                        resendTextView.setTextColor(Color.GRAY);

                    }
                    if (counter >= 10) {
                        if (counter == 60) {
                            timerTextView.setText("1:00");
                        } else {
                            timerTextView.setText("00:" + counter);
                        }

                    } else {
                        timerTextView.setText("00:0" + counter);
                    }

                    counter--;
                } catch (Exception e) {
                }
            }
        });
    }


    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    private void verifyMobileNumber() {
        if (CUtils.isConnectedWithInternet(activity)) {
            new VerifyMobileNumberFromServer().execute();
        } else {
            showToast(getResources().getString(R.string.internet_is_not_working));
        }

    }




  /*  private List<NameValuePair> getNameValuePairs_CustomAdds(String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("mobileno", "9015469060"));
        nameValuePairs.add(new BasicNameValuePair("key", );

        return nameValuePairs;

    }*/

    private void doActionOnOkButton() {
        layout3.startAnimation(animOut);
        String text = getResources().getString(R.string.otp_desc_text) + " " + "+91" + " 9015469060" + " " + "worng number?";

        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                clickedButtonId = R.id.otp_desc_text;
                layout4.startAnimation(animOut);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 64, 76, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        otpDescTextView.setText(ss);
        otpDescTextView.setMovementMethod(LinkMovementMethod.getInstance());
        otpDescTextView.setHighlightColor(Color.TRANSPARENT);
    }

    class SendAstrologerInfoOnserver extends AsyncTask<String, Void, Void> {
        BeanUserMapping beanUserMapping;
        //String key;
        String resultStr;

        SendAstrologerInfoOnserver(BeanUserMapping beanUserMapping) {
            this.beanUserMapping = beanUserMapping;
            CUtils.saveUserMappingData(activity, beanUserMapping);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linearProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
           /* try {
                //Thread.sleep(5000);
            } catch (Exception e) {

            }*/

            resultStr = sendDataToServer(beanUserMapping);
            //resultStr = "[{'Result':1}]";
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                isButtonClicked = false;
                linearProgressbar.setVisibility(View.GONE);
                if (resultStr != null && !resultStr.equals("")) {
                    JSONArray jArray = new JSONArray(resultStr);
                    String result = jArray.getJSONObject(jArray.length() - 1).getString("Result");
                    beanUserMapping.setStatus(2);// data sent to server
                    CUtils.saveUserMappingData(activity, beanUserMapping);
                    //If Insert Successfully on Server
                    if (result.equals("0")) {
                        showToast("All fields required.");
                    } else if (result.equals("1")) {

                        clickedButtonId = R.id.btn_join_now;
                        layout2.startAnimation(animOut);
                        phoneNumber = phoneEditText.getText().toString();
                        //descTextView1.setText(getResources().getString(R.string.verifing_number) + phoneNumber);
                    } else if (result.equals("2")) {

                    } else if (result.equals("3")) {
                        showToast("Authentication failed");
                    } else if (result.equals("4")) {
                        showToast("Enter valid data field");
                    } else if (result.equals("5")) {
                        showToast("Enter valid Mobile number.");
                    }
                }

            } catch (Exception e) {
                Log.i("Error", "" + e.getMessage());
            }
        }
    }

    private String sendDataToServer(BeanUserMapping beanUserMapping) {

        String resultStr = "";
        if (CUtils.isConnectedWithInternet(activity)) {
            //Collecting information in Map
            Map<String, String> params = new HashMap<>();
            params.put("name", beanUserMapping.getName());
            params.put("isastrologer", String.valueOf(beanUserMapping.getIsAstrologer()));
            params.put("mobileno", beanUserMapping.getPhoneNo());
            params.put("city", beanUserMapping.getCity());
            params.put("aboutuser", beanUserMapping.getAbout());
            params.put("astrocampid", beanUserMapping.getAstrocampId());
            params.put("deviceid", beanUserMapping.getDeviceId());

            try {
               // resultStr = httpExcuteMethod(CGlobalVariables.updateUserInfo, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultStr;
        //checkForUpdateData(beanUserMapping,data);
    }


    private class VerifyMobileNumberFromServer extends AsyncTask<Void, Void, String> {
        //CustomProgressDialog pd;
        String resultStr = "";
        BeanUserMapping beanUserMapping = CUtils.getUserMappingData(activity);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar1.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //String url = CGlobalVariables.sendOtpUrl;
                String url = "";
                Map<String, String> param = new HashMap<>();
                param.put("mobileno", beanUserMapping.getPhoneNo());
                param.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
                resultStr = httpExcuteMethod(url, param);
                return resultStr;
            } catch (Exception ex) {
                android.util.Log.i("Tag", "1 - " + ex.getMessage());
            }

            return resultStr;
        }

        @Override
        protected void onPostExecute(String result1) {
            super.onPostExecute(result1);

            progressBar1.setVisibility(View.GONE);


            if (resultStr != null && !resultStr.equals("")) {
                try {
                    JSONArray jArray = new JSONArray(resultStr);
                    String result = jArray.getJSONObject(jArray.length() - 1).getString("result");
                    if (result.equals("0")) {
                        showToast("All parameters are required.");
                    } else if (result.equals("1")) {
                        msgCounter++;
                        CUtils.saveIntData(activity, "msgCounter", msgCounter);
                        CUtils.saveStringData(activity, "savedDate", currentDate());
                        if (clickedButtonId != R.id.resend_textview) {
                            doActionOnOkButton();
                        }
                    } else if (result.equals("2")) {
                        showToast("SMS send fail.");
                    } else if (result.equals("3")) {
                        showToast("Authentication failed.");
                    } else if (result.equals("4")) {
                        showToast("Mobile number is not correct.");
                    } else if (result.equals("5")) {
                        showToast("Mobile number is not correct.");
                    } else if (result.equals("6")) {
                        showToast("SMS send fail.");
                    } else if (result.equals("7")) {
                        showToast("SMS send fail.");
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage() + "");
                }

            }


        }

    }

    class SendOTPOnServer extends AsyncTask<Void, Void, Void> {
        String resultStr;
        String otp;
        BeanUserMapping beanUserMapping = CUtils.getUserMappingData(activity);

        SendOTPOnServer(String otp) {
            this.otp = otp;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            otpProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                //Thread.sleep(5000);
                //String url = CGlobalVariables.smsVerification;
                String url ="";
                Map<String, String> param = new HashMap<>();
                param.put("mobileno", beanUserMapping.getPhoneNo());
                param.put("otp", otp);
                param.put("deviceid", beanUserMapping.getDeviceId());
                resultStr = httpExcuteMethod(url, param);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            otpProgressbar.setVisibility(View.GONE);
            if (resultStr != null && !resultStr.equals("")) {
                try {
                    JSONArray jArray = new JSONArray(resultStr);
                    String result = jArray.getJSONObject(jArray.length() - 1).getString("result");

                    if (result.equals("0")) {
                        showToast("All parameters are required.");
                    } else if (result.equals("1")) {
                        showToast("Mobile number verified sucessfully.");
                        FragAskForAstrologer.this.dismiss();
                        CUtils.saveBooleanData(activity, CGlobalVariables.isShowAstrologerDirectoryInMainMenu, false);
                    } else if (result.equals("2")) {
                        showToast("Invalid otp number");
                    } else if (result.equals("3")) {
                        showToast("Authentication failed.");
                    }
                } catch (Exception e) {

                }

            }

        }

    }

    private String httpExcuteMethod(String url, Map<String, String> params) {
        String resultStr = "";
        String key = CUtils.getApplicationSignatureHashCode(getActivity());
        if (CUtils.isConnectedWithInternet(activity)) {
            params.put("key", key);
            try {
                //new HttpUtility(activity).sendPostRequest(url, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultStr;
    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    private void doActionOnCancelButton() {
        try {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("AstrologerInfoCancellationFrag");
            if (prev != null) {
                ft.remove(prev);
            }
            AstrologerInfoCancellationFrag astrologerInfoCancellationFrag = new AstrologerInfoCancellationFrag();
            ft.addToBackStack(null);
            astrologerInfoCancellationFrag.show(fm, "AstrologerInfoCancellationFrag");
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.e("Error>>", "" + e.getMessage());
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.hideMyKeyboard(activity);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        nameEditText.setFocusable(false);
        phoneEditText.setFocusable(false);
        cityEditText.setFocusable(false);
        aboutEditText.setFocusable(false);
        otpEditText.setFocusable(false);
        CUtils.hideMyKeyboard(activity);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(activity, "Position" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setFeildValues() {
        BeanUserMapping beanUserMapping = CUtils.getUserMappingData(activity);
        if (beanUserMapping != null) {
            nameEditText.setText(beanUserMapping.getName());
            phoneEditText.setText(beanUserMapping.getPhoneNo());
            cityEditText.setText(beanUserMapping.getCity());
            aboutEditText.setText(beanUserMapping.getAbout());
            descTextView2.setText("+91 " + beanUserMapping.getPhoneNo());
        }
    }

    private String currentDate() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        return day + "-" + month + "-" + year;
    }
}
