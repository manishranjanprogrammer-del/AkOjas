package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_GOOGLE_FACEBOOK_VISIBLE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_LOGIN_SKIP_BTN_HIDE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_SELECTION_SCRREN;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActAppSplash;
import com.ojassoft.astrosage.ui.act.ActWizardScreens;
import com.ojassoft.astrosage.ui.act.ActivityLoginAndSignin;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.adapters.LanguageAdapter;
import com.ojassoft.astrosage.varta.model.Language;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Language picker used by both onboarding and the in-app language switch entry points.
 * It persists the language choice, updates language-dependent defaults, and routes the user
 * to the next screen based on where the picker was opened from.
 */
public class WizardChooseLanguage extends Fragment {

    IWizardChooseLanguageFragmentInterface iWizardChooseLanguageFragmentInterface;
//    TextView nextButton;
    Button btnContinue;

    int oldLanguageIndex;

    public WizardChooseLanguage() {
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lay_wizard_choose_language,
                container, false);

//        nextButton = (TextView) view.findViewById(R.id.buttonNext);




        initValues();

        setLanguageListAdapter(view);

//        nextButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                goToChooseLanguage();
//            }
//        });

        return view;

    }

    /**
     * Binds the language grid and forwards the continue action to either onboarding
     * or the in-app refresh flow.
     */
    private void setLanguageListAdapter(View view) {
        TextView titleTV = view.findViewById(R.id.font_auto_lay_wizard_choose_language_1);
        TextView subtitleTV = view.findViewById(R.id.font_auto_lay_wizard_choose_language_2);
        btnContinue = (Button) view
                .findViewById(R.id.btnContinue);
        FontUtils.changeFont(requireContext(), titleTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(requireContext(), subtitleTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(requireContext(), btnContinue, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLanguage);

        List<Language> languageList = CUtils.getLanguageList(requireContext());


        LanguageAdapter adapter = new LanguageAdapter(languageList, new LanguageAdapter.OnLanguageClickListener() {
            @Override
            public void onLanguageClick(Language language) {
                // Handle item click
                //Toast.makeText(requireContext(), "Selected: " + language.getName()+" id: "+ language.getLanguageId(), Toast.LENGTH_SHORT).show();
                newLanguageIndexVar = language.getLanguageId();
            }
        }, oldLanguageIndex);

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        recyclerView.setAdapter(adapter);

        btnContinue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Added by Tushar Sharma on 30-09-2025
                // Check whether the screen is opened from Splash or directly from Home
                if (!ActWizardScreens.isFromSplash) {
                    // User opened the app from Home (not the first time install flow)
                    goToChooseLanguageHome();
                   // Log.d("ActWizardScreens", "goToChooseLanguageHome : " + ActWizardScreens.isFromSplash);

                } else {
                    // User is coming from Splash (first time app install flow)
                    goToChooseLanguage();
                   // Log.d("ActWizardScreens", "goToChooseLanguage : " + ActWizardScreens.isFromSplash);

                }
            }

        });

    }

    /**
     * This function is used to init variables and dialog values
     *
     * @date 17-dec-13
     */
    private void initValues() {
        SharedPreferences sharedPreferencesForLang = getActivity()
                .getSharedPreferences(
                        CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                        Context.MODE_PRIVATE);
        oldLanguageIndex = sharedPreferencesForLang.getInt(
                CGlobalVariables.APP_PREFS_AppLanguage, 0);
        // Preserve the saved language when the user taps Continue without selecting a different item.
        newLanguageIndexVar = oldLanguageIndex;

    }

    int newLanguageIndexVar = 0;

    /**
     * Applies the selected language during first-launch onboarding and continues to login/signup.
     * This path updates app state before the next onboarding screen so the chosen locale is used immediately.
     */
    private void goToChooseLanguage() {
        //newLanguageIndexVar = getSelectedLanguagePosition();
        final String regid = CUtils.getRegistrationId(getActivity()
                .getApplicationContext());
        //if (newLanguageIndexVar != oldLanguageIndex) {
        saveLanguageInPreference(newLanguageIndexVar);
        // Added on 11-sep-2015
        saveChartInPreference(newLanguageIndexVar);
        //
        ((AstrosageKundliApplication) getActivity().getApplication())
                .setLanguageCode(newLanguageIndexVar);

        // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION
        // INFORMATION ON SERVER

        //commented by Abhishek Raj at 19/12/2022 according to discussion with punit sir
        /*new ControllerManager()
                .saveUserGCMRegistrationInformationOnOjasServer(
                        getActivity().getApplicationContext(), regid,
                        newLanguageIndexVar, CUtils.getUserName(getActivity()
                                .getApplicationContext()));*/

        // Topic Registered and Unregistered
        CUtils.saveStringData(getActivity(), CGlobalVariables.LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, "");
        final String newLanguageTopicName = CUtils.getTopicName(newLanguageIndexVar);
        if (regid.equals("")) {
            CUtils.queueLanguageTopicForRetry(getActivity(), newLanguageIndexVar);
        }
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                //Unregistered Old Topic
                try {
                    if (!regid.equals("")) {
                        String topicName = CUtils.getTopicName(oldLanguageIndex);
                        CUtils.unSubscribeTopics(regid, topicName, getActivity());
                    }
                } catch (Exception ex) {
                    Log.e("Exception", ex.getMessage().toString());
                }

                //Registered New Topic
                try {
                    if (!regid.equals("")) {
                        CUtils.subscribeTopics(regid, newLanguageTopicName, getActivity());
                    }
                } catch (Exception ex) {
                    Log.e("Exception", ex.getMessage().toString());
                }
                return null;
            }
        }.execute(null, null, null);
        //}

        // CUtils.googleAnalyticSend(null,
        // CGlobalVariables.GOOGLE_ANALYTIC_LANGUAGE_CONFIG_SCREEN,
        // CGlobalVariables.arrLang[newLanguageIndex],null, 0l);
        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_LANGUAGE_CONFIG_SCREEN,
                CGlobalVariables.arrLang[newLanguageIndexVar], null);
        String labell = "launch_langauge_change_" + CGlobalVariables.arrLang[newLanguageIndexVar];
        CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");



            goToLoginScreen();



    }

    /**
     * Applies the selected language while the user is already inside the app.
     * Besides preference updates, it clears cached astrologer data so the home module refetches localized content.
     */
    private void goToChooseLanguageHome() {
        com.ojassoft.astrosage.varta.utils.CUtils.saveWalletRechargeData("");
        //Toast.makeText(getActivity(),"choose language OK "+newLanguageIndex+"\n buttonID "+getCheckedRadioButtonId(),Toast.LENGTH_LONG).show();
        if (newLanguageIndexVar != oldLanguageIndex) {
            saveLanguageInPreference(newLanguageIndexVar);
            //Toast.makeText(getActivity(),"language index saved "+newLanguageIndex,Toast.LENGTH_LONG).show();
            // Added on 11-sep-2015
            saveChartInPreference(newLanguageIndexVar);
            //
            ((AstrosageKundliApplication) getActivity().getApplication())
                    .setLanguageCode(newLanguageIndexVar);

            changePlanetListColor(getActivity());

            // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION
            // INFORMATION ON SERVER
            final String regid = CUtils.getRegistrationId(getActivity()
                    .getApplicationContext());
            new ControllerManager()
                    .saveUserGCMRegistrationInformationOnOjasServer(
                            getActivity().getApplicationContext(), regid,
                            newLanguageIndexVar, CUtils.getUserName(getActivity()
                                    .getApplicationContext()));

            // Topic Registered and Unregistered
            CUtils.saveStringData(getActivity(),CGlobalVariables.LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    //Unregistered Old Topic
                    try {
                        if (!regid.equals("")) {
                            String topicName = CUtils.getTopicName(oldLanguageIndex);
                            CUtils.unSubscribeTopics(regid, topicName,getActivity());
                        }
                    } catch (Exception ex) {
                        Log.e("Exception", ex.getMessage().toString());
                    }

                    //Registered New Topic
                    try {
                        if (!regid.equals("")) {
                            String topicName = CUtils.getTopicName(newLanguageIndexVar);
                            CUtils.subscribeTopics(regid, topicName,getActivity());
                        }
                    } catch (Exception ex) {
                        Log.e("Exception", ex.getMessage().toString());
                    }
                    return null;
                }
            }.execute(null, null, null);
            com.ojassoft.astrosage.varta.utils.CUtils.allAstrologersArrayList.clear();
            com.ojassoft.astrosage.varta.utils.CUtils.aiAstrologersArrayList.clear();
        }

        // CUtils.googleAnalyticSend(null,
        // CGlobalVariables.GOOGLE_ANALYTIC_LANGUAGE_CONFIG_SCREEN,
        // CGlobalVariables.arrLang[newLanguageIndex],null, 0l);
        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_LANGUAGE_CONFIG_SCREEN,
                CGlobalVariables.arrLang[newLanguageIndexVar], null);
        String labell = "language_change_" + CGlobalVariables.arrLang[newLanguageIndexVar];
        CUtils.fcmAnalyticsEvents(labell,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

        startActivity(new Intent(requireContext(), ActAppModule.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    /**
     * Rebuilds planet color keys using the latest localized resource strings after a language change.
     */
    private static void changePlanetListColor(Context context)
    {
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.La),context.getResources().getColor(R.color.colorLa));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Su),context.getResources().getColor(R.color.colorSu));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Mo),context.getResources().getColor(R.color.colorMo));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ma),context.getResources().getColor(R.color.colorMa));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Me),context.getResources().getColor(R.color.colorMe));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ju),context.getResources().getColor(R.color.colorJu));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ve),context.getResources().getColor(R.color.colorVe));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Sa),context.getResources().getColor(R.color.colorSa));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ra),context.getResources().getColor(R.color.colorRa));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ke),context.getResources().getColor(R.color.colorKe));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ur),context.getResources().getColor(R.color.colorUr));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ne),context.getResources().getColor(R.color.colorNe));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Pl),context.getResources().getColor(R.color.colorPl));
    }

    /**
     * Opens login/signup once onboarding has captured the user language.
     */
    private void goToLoginScreen() {
        // CUtils.saveWizardShownFirstTime(getActivity());
        //startActivity(new Intent(getActivity(), ActivityLoginAndSignin.class));
        Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
        intent.putExtra(IS_FROM_SCREEN, LANGUAGE_SELECTION_SCRREN);
        intent.putExtra(IS_GOOGLE_FACEBOOK_VISIBLE, true);
        startActivity(intent);
        getActivity().finish();
    }


    /**
     * Persists the selected language code used by the rest of the app when resolving locale and API langcode.
     */
    private void saveLanguageInPreference(int newLanguageIndex) {
        SharedPreferences sharedPreferencesForLang = getActivity()
                .getSharedPreferences(
                        CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferencesForLang
                .edit();
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_AppLanguage,
                newLanguageIndex);
        sharedPrefEditor.commit();
    }

    /**
     * Keeps the default chart style aligned with the selected language convention.
     * Bengali maps to East style, language id 2 maps to South style, and all others use North style.
     */
    private void saveChartInPreference(int newLanguageIndex) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        if (newLanguageIndex == CGlobalVariables.BANGALI) {//For East India Chart
            sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle, CGlobalVariables.CHART_EAST_STYLE);
        } else if (newLanguageIndex == 2) {//For South India Chart
            sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle, CGlobalVariables.CHART_SOUTH_STYLE);
        } else {
            sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle, CGlobalVariables.CHART_NORTH_STYLE);
        }
        /*int monthType = CGlobalVariables.MONTH_AMANTA;
         *//* String state = CUtils.getStringData(getActivity(), CGlobalVariables.KEY_STATE,"");
        if(state.equalsIgnoreCase(CGlobalVariables.STATE_BIHAR) || state.equalsIgnoreCase(CGlobalVariables.STATE_UTTAR_PRADESH)){
            monthType = CGlobalVariables.MONTH_PURNIMANT;
        }*//*
        if (newLanguageIndex == CGlobalVariables.HINDI) {
            monthType = CGlobalVariables.MONTH_PURNIMANT;
        }
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_MonthType, monthType);*/
        sharedPrefEditor.commit();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IWizardChooseLanguageFragmentInterface) {
            iWizardChooseLanguageFragmentInterface = (IWizardChooseLanguageFragmentInterface) activity;
        } else {
            Log.w("WizardChooseLanguage", "Host activity does not implement IWizardChooseLanguageFragmentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iWizardChooseLanguageFragmentInterface = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * Legacy host callback kept for compatibility with the existing wizard container contract.
     */
    public interface IWizardChooseLanguageFragmentInterface {
        public void clickedChooseLanguageNextButton(int languageCode);
    }


}
