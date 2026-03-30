package com.ojassoft.astrosage.ui.fragments.matching;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.jinterface.matching.IMatchingInputDetailFragment;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchingInputDetailFragment extends Fragment {

    //textViewTimePerson1,textViewTimePerson2,
    Activity activity;
    TextView textViewUserNamePerson1, textViewCalendarPerson1, textViewPlacePerson1,
            textViewUserNamePerson2, textViewCalendarPerson2, textViewPlacePerson2, textViewBoyDetail, textViewgirlDetail,
            buttonCalendarPerson1, buttonTimePerson1, buttonCalendarPerson2, buttonTimePerson2;
    Button buttonCalculateMatching;
    TextInputLayout boyInputLayout, girlInputLayout;
    EditText etUserNamePerson1, etUserNamePerson2;
    IMatchingInputDetailFragment iMatchingInputDetailFragment;
    BeanHoroPersonalInfo _beanHoroPersonalInfoPerson1, _beanHoroPersonalInfoPerson2;
    BeanDateTime beanDateTimePerson1, beanDateTimePerson2;
    BeanPlace beanPlacePerson1, beanPlacePerson2;
    CheckBox saveChart;
    boolean fragmentRestorde = false;
    int LANGUAGE_CODE;
    //public Typeface typeface = Typeface.DEFAULT;
    LinearLayout layPlaceHolderPerson1 = null, layPlaceHolderPerson2 = null;
    TextView txtPlaceNamePerson1, txtPlaceDetailPerson1, txtPlaceNamePerson2, txtPlaceDetailPerson2;
    ScrollView wholeScrren;
    CoordinatorLayout  main_container;
    private EditText etBirthDetails;
    private View viewDummy;
    private TextView btnShowInputBirthDet;
    private LinearLayout llEnterBirthDetails;
    private String prevBirthDetails;
    public MatchingInputDetailFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        iMatchingInputDetailFragment.matchingInputDetailFragmentCreated();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        iMatchingInputDetailFragment = (IMatchingInputDetailFragment) activity;
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iMatchingInputDetailFragment = null;
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        //typeface = CUtils.getUserSelectedLanguageFontType(getActivity(), LANGUAGE_CODE);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        if (fragmentRestorde) {
            updateBirthDetailPerson1(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail());
            updateBirthDetailPerson2(CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
            fragmentRestorde = false;
        }
        /*etUserNamePerson1.getBackground().setColorFilter(null);
        etUserNamePerson2.getBackground().setColorFilter(null);
        boyInputLayout.setErrorEnabled(false);
        girlInputLayout.setErrorEnabled(false);*/
        boyInputLayout.setHintEnabled(false);
        girlInputLayout.setHintEnabled(false);
        super.onResume();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onStop()
     */
    @Override
    public void onStop() {
        super.onStop();
        //when user leave the activity then it should save user input in bean.
        CGlobalMatching.getCGlobalMatching().setBoyPersonalDetail(getUserBirthDetailBeanPerson1());
        CGlobalMatching.getCGlobalMatching().setGirlPersonalDetail(getUserBirthDetailBeanPerson2());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _beanHoroPersonalInfoPerson1 = new BeanHoroPersonalInfo();
        _beanHoroPersonalInfoPerson2 = new BeanHoroPersonalInfo();
        if (savedInstanceState != null) {
            fragmentRestorde = true;
        }
        beanDateTimePerson1 = _beanHoroPersonalInfoPerson1.getDateTime();
        beanDateTimePerson2 = _beanHoroPersonalInfoPerson2.getDateTime();
        beanPlacePerson1 = CUtils.getUserDefaultCity(activity);
        beanPlacePerson2 = CUtils.getUserDefaultCity(activity);
        _beanHoroPersonalInfoPerson1.setPlace(beanPlacePerson1);
        _beanHoroPersonalInfoPerson2.setPlace(beanPlacePerson2);
        View view = inflater.inflate(R.layout.matching_detail_input, container, false);
        initValues(view);
        setTypefaceOfView();
        preventSpecialCharInName();
        etBirthDetails = view.findViewById(R.id.etBirthDetails);
        btnShowInputBirthDet = view.findViewById(R.id.btnShowInputBirthDet);
        btnShowInputBirthDet.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        RelativeLayout getFillDetails = view.findViewById(R.id.getFillDetails);
        llEnterBirthDetails = view.findViewById(R.id.llEnterBirthDetails);
        btnShowInputBirthDet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llEnterBirthDetails.getVisibility()==View.VISIBLE){
                    btnShowInputBirthDet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_birthdetailsetting_plus, 0);
                    llEnterBirthDetails.setVisibility(View.GONE);
                }else {
                    btnShowInputBirthDet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_birthdetailsetting_minus, 0);
                    llEnterBirthDetails.setVisibility(View.VISIBLE);
                }
            }
        });
        getFillDetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity)) {
                    String etBirthDetailsText = etBirthDetails.getText().toString().trim();
                    if (!TextUtils.isEmpty(etBirthDetailsText)) {
                        if (TextUtils.isEmpty(prevBirthDetails) || !prevBirthDetails.equals(etBirthDetailsText)) {
                            prevBirthDetails = etBirthDetailsText;
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.TYPE_PASTE_BIRTH_DETAILS_MATCHING, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            getBirthDetails();
                        } else {
                            CUtils.showSnackbar(main_container, getResources().getString(R.string.prev_birth_details_are_same), getContext());
                        }
                    } else {
                        CUtils.showSnackbar(main_container, getResources().getString(R.string.plz_enter_birth_details), getContext());
                    }
                } else {
                    displayLoginDialog();
                }
            }
        });
        viewDummy = view.findViewById(R.id.viewDummy);
        etBirthDetails.setOnFocusChangeListener((view1, hasFocus) -> {
            if (hasFocus) {
                // Keyboard is likely opened
                viewDummy.setVisibility(View.VISIBLE);
            } else {
                viewDummy.setVisibility(View.GONE);
                // Keyboard is likely closed
            }
        });
        //
        return view;
    }
    private void displayLoginDialog() {
        CUtils.showSnackbar(main_container, getString(R.string.to_use_this_feature_please_log_in), getContext());
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent1 = new Intent(getContext(), FlashLoginActivity.class);
                    startActivity(intent1);
                }
            }, 1000);
        } catch (Exception e) {
            //
        }
    }
    private void preventSpecialCharInName() {
//		final char[] nonAcceptedChars = new char[]{'_','^','*','-',',', '?', '&', '@', '%', '<','>','`','~','!','#','{','}','[',']','\'','\\',';','"','|','=','+'};
        final char[] nonAcceptedChars = new char[]{'?', '&', '<', '>', '\'', '%', ';'};
        InputFilter[] filterArray = new InputFilter[2];
        filterArray[0] = new InputFilter() {
            String s = new String(nonAcceptedChars);

            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    for (int index = start; index < end; index++) {
                        if (s.contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        filterArray[1] = new InputFilter.LengthFilter(50);
        etUserNamePerson1.setFilters(new InputFilter[]{filterArray[0], filterArray[1]});
        etUserNamePerson2.setFilters(new InputFilter[]{filterArray[0], filterArray[1]});
    }


    private void initValues(View view) {
        // PERSOON 1
        main_container =  view.findViewById(R.id.main_container);
        wholeScrren = (ScrollView) view.findViewById(R.id.scrollView);

        textViewBoyDetail = (TextView) view.findViewById(R.id.boy_detail_textview);
        textViewgirlDetail = (TextView) view.findViewById(R.id.girl_detail_textview);
        textViewUserNamePerson1 = (TextView) view.findViewById(R.id.textViewUserNamePerson1);
        etUserNamePerson1 = (EditText) view.findViewById(R.id.etUserNamePerson1);
        etUserNamePerson1.addTextChangedListener(new MyTextWatcher(etUserNamePerson1));

        textViewCalendarPerson1 = (TextView) view.findViewById(R.id.textViewCalendarPerson1);
        textViewPlacePerson1 = (TextView) view.findViewById(R.id.textViewPlacePerson1);
        buttonCalendarPerson1 = (TextView) view.findViewById(R.id.buttonCalendarPerson1);
        buttonTimePerson1 = (TextView) view.findViewById(R.id.buttonTimePerson1);
        layPlaceHolderPerson1 = (LinearLayout) view.findViewById(R.id.layPlaceHolderPerson1);
        txtPlaceNamePerson1 = (TextView) view.findViewById(R.id.textViewPlaceNamePerson1);
        txtPlaceDetailPerson1 = (TextView) view.findViewById(R.id.textViewPlaceDetailsPerson1);

        boyInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_boy_name);
        girlInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_girl_name);

        textViewUserNamePerson2 = (TextView) view.findViewById(R.id.textViewUserNamePerson2);
        etUserNamePerson2 = (EditText) view.findViewById(R.id.etUserNamePerson2);
        etUserNamePerson2.addTextChangedListener(new MyTextWatcher(etUserNamePerson2));

        textViewCalendarPerson2 = (TextView) view.findViewById(R.id.textViewCalendarPerson2);
        textViewPlacePerson2 = (TextView) view.findViewById(R.id.textViewPlacePerson2);
        buttonCalendarPerson2 = (TextView) view.findViewById(R.id.buttonCalendarPerson2);

        buttonTimePerson2 = (TextView) view.findViewById(R.id.buttonTimePerson2);
        layPlaceHolderPerson2 = (LinearLayout) view.findViewById(R.id.layPlaceHolderPerson2);
        txtPlaceNamePerson2 = (TextView) view.findViewById(R.id.textViewPlaceNamePerson2);
        txtPlaceDetailPerson2 = (TextView) view.findViewById(R.id.textViewPlaceDetailsPerson2);
        saveChart = (CheckBox) view.findViewById(R.id.checkBoxSaveKundli);
        buttonCalculateMatching = (Button) view.findViewById(R.id.buttonCalculateMatching);
        if (((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            buttonCalculateMatching.setText(getResources().getString(R.string.show_match).toUpperCase());
        }

        buttonCalendarPerson1.setText(getFormatedTextToShowDate(beanDateTimePerson1));
        buttonTimePerson1.setText(CUtils.getFormatedTextToShowTime(beanDateTimePerson1));
        txtPlaceNamePerson1.setText(beanPlacePerson1.getCityName().trim());
        txtPlaceDetailPerson1.setText(CUtils.getPlaceDetailInSingleString(beanPlacePerson1));

        /*set kundli date in newKundliSelectedDate*/
        if (buttonCalendarPerson1.toString().contains("-")) {
            int monthInCount = 0;
            String[] getYear = buttonCalendarPerson1.getText().toString().split("-");
            CUtils.setNewKundliSelectedDate(Integer.parseInt(getYear[2].trim()));
            String[] shortMonth = getResources().getStringArray(R.array.month_short_name_list);
            for (int i = 0; i < shortMonth.length; i++) {
                if (shortMonth[i].equalsIgnoreCase(getYear[1].trim())) {
                    monthInCount = i;
                    break;
                }
            }
            CUtils.setNewKundliSelectedMonth(monthInCount + 1);
        }


        buttonCalendarPerson2.setText(getFormatedTextToShowDate(beanDateTimePerson2));
        buttonTimePerson2.setText(CUtils.getFormatedTextToShowTime(beanDateTimePerson2));
        txtPlaceNamePerson2.setText(beanPlacePerson2.getCityName().trim());
        txtPlaceDetailPerson2.setText(CUtils.getPlaceDetailInSingleString(beanPlacePerson2));

        /*set kundli date in newKundliSelectedDate*/
        if (buttonCalendarPerson2.toString().contains("-")) {
            int monthInCount = 0;
            String[] getYear = buttonCalendarPerson2.getText().toString().split("-");
            CUtils.setNewKundliSelectedDate(Integer.parseInt(getYear[2].trim()));
            String[] shortMonth = getResources().getStringArray(R.array.month_short_name_list);
            for (int i = 0; i < shortMonth.length; i++) {
                if (shortMonth[i].equalsIgnoreCase(getYear[1].trim())) {
                    monthInCount = i;
                    break;
                }
            }
            CUtils.setNewKundliSelectedMonth(monthInCount + 1);
        }

        etUserNamePerson1.setFocusable(false);
        etUserNamePerson2.setFocusable(false);

        etUserNamePerson1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        etUserNamePerson2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        // END PERSON 2
        etUserNamePerson1.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iMatchingInputDetailFragment.setPersonVariableValue(HomeMatchMakingInputScreen.PERSON1);
                }/*else {
                    iMatchingInputDetailFragment.setPersonVariableValue(HomeMatchMakingInputScreen.PERSON2);
				}*/
            }
        });

        etUserNamePerson2.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iMatchingInputDetailFragment.setPersonVariableValue(HomeMatchMakingInputScreen.PERSON2);
                }/*else {
                    iMatchingInputDetailFragment.setPersonVariableValue(HomeMatchMakingInputScreen.PERSON1);
				}*/
            }
        });


        // EVENT FOR PERSON 1
        textViewUserNamePerson1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                iMatchingInputDetailFragment.openSavedKundli(HomeMatchMakingInputScreen.PERSON1);
            }
        });
        textViewUserNamePerson1.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Boy's Name", x + ((int) (densityConstant * 30)), y - ((int) (densityConstant * 75)));
                return true;
            }
        });
       /* textViewCalendarPerson1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                iMatchingInputDetailFragment.openCalendar(beanDateTimePerson1, HomeMatchMakingInputScreen.PERSON1);

            }
        });*/
        textViewCalendarPerson1.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Boy's Birth Date", x + ((int) (densityConstant * 30)), y - ((int) (densityConstant * 75)));
                return true;
            }
        });
        buttonCalendarPerson1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                iMatchingInputDetailFragment.openCalendar(beanDateTimePerson1, HomeMatchMakingInputScreen.PERSON1);

            }
        });
        /*textViewTimePerson1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                iMatchingInputDetailFragment.openTimePicker(beanDateTimePerson1, HomeMatchMakingInputScreen.PERSON1);

            }
        });
        textViewTimePerson1.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Boy's Birth Time", x + ((int) (densityConstant * 30)), y - ((int) (densityConstant * 75)));
                return true;
            }
        });*/

        buttonTimePerson1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                iMatchingInputDetailFragment.openTimePicker(beanDateTimePerson1, HomeMatchMakingInputScreen.PERSON1);

            }
        });

        textViewPlacePerson1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                iMatchingInputDetailFragment.openSearchPlace(_beanHoroPersonalInfoPerson1.getPlace(), HomeMatchMakingInputScreen.PERSON1);
            }
        });
        textViewPlacePerson1.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Boy's Birth Place", x + ((int) (densityConstant * 30)), y - ((int) (densityConstant * 75)));
                return true;
            }
        });
        layPlaceHolderPerson1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                iMatchingInputDetailFragment.openSearchPlace(_beanHoroPersonalInfoPerson1.getPlace(), HomeMatchMakingInputScreen.PERSON1);
            }
        });
        // END EVENT FOR PERSON 1

        // EVENT FOR PERSON 2
        textViewUserNamePerson2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                iMatchingInputDetailFragment.openSavedKundli(HomeMatchMakingInputScreen.PERSON2);
            }
        });
        textViewUserNamePerson2.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Girl's Name", x + ((int) (densityConstant * 30)), y - ((int) (densityConstant * 75)));
                return true;
            }
        });
        /*textViewCalendarPerson2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                iMatchingInputDetailFragment.openCalendar(beanDateTimePerson2, HomeMatchMakingInputScreen.PERSON2);

            }
        });*/
        textViewCalendarPerson2.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Girl's Birth Date", x + ((int) (densityConstant * 30)), y - ((int) (densityConstant * 75)));
                return true;
            }
        });
        buttonCalendarPerson2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                iMatchingInputDetailFragment.openCalendar(beanDateTimePerson2, HomeMatchMakingInputScreen.PERSON2);

            }
        });

        /*textViewTimePerson2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                iMatchingInputDetailFragment.openTimePicker(beanDateTimePerson2, HomeMatchMakingInputScreen.PERSON2);

            }
        });*/
        /*textViewTimePerson2.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Girl's Birth Time", x + ((int) (densityConstant * 30)), y - ((int) (densityConstant * 75)));
                return true;
            }
        });*/

        buttonTimePerson2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                iMatchingInputDetailFragment.openTimePicker(beanDateTimePerson2, HomeMatchMakingInputScreen.PERSON2);

            }
        });

        textViewPlacePerson2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                iMatchingInputDetailFragment.openSearchPlace(_beanHoroPersonalInfoPerson2.getPlace(), HomeMatchMakingInputScreen.PERSON2);
            }
        });
        textViewPlacePerson2.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Girl's Birth Place", x + ((int) (densityConstant * 30)), y - ((int) (densityConstant * 75)));
                return true;
            }
        });
        layPlaceHolderPerson2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                iMatchingInputDetailFragment.openSearchPlace(_beanHoroPersonalInfoPerson2.getPlace(), HomeMatchMakingInputScreen.PERSON2);
            }
        });
        // END EVENT FOR PERSON 2
        buttonCalculateMatching.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                BeanHoroPersonalInfo beanHoroPersonalInfoPerson1 = null, beanHoroPersonalInfoPerson2 = null;
                if (validateData()) {
                    beanHoroPersonalInfoPerson1 = getUserBirthDetailBeanPerson1();
                    beanHoroPersonalInfoPerson2 = getUserBirthDetailBeanPerson2();
                  /*  CUtils.saveKundliInPreference(getActivity(),beanHoroPersonalInfoPerson1);
                    CUtils.saveKundliInPreference(getActivity(),beanHoroPersonalInfoPerson2);*/

                    iMatchingInputDetailFragment.calculateMatching(beanHoroPersonalInfoPerson1, beanHoroPersonalInfoPerson2, saveChart.isChecked());

                }
            }
        });

        //setCustomFont();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etUserNamePerson1:
                    if (HomeMatchMakingInputScreen.isMenuItemClicked) {
                        //HomeMatchMakingInputScreen.isMenuItemClicked=false;
                        boyInputLayout.setErrorEnabled(false);
                        boyInputLayout.setError(null);
                        etUserNamePerson1.getBackground().setColorFilter(null);
                    } else {
                        validateName(etUserNamePerson1, boyInputLayout, getString(R.string.please_enter_boy_name_v1));
                    }
                    break;
                case R.id.etUserNamePerson2:
                    if (HomeMatchMakingInputScreen.isMenuItemClicked) {
                        HomeMatchMakingInputScreen.isMenuItemClicked = false;
                        girlInputLayout.setErrorEnabled(false);
                        girlInputLayout.setError(null);
                        etUserNamePerson2.getBackground().setColorFilter(null);
                    } else {
                        validateName(etUserNamePerson2, girlInputLayout, getString(R.string.please_enter_girl_name_v1));
                    }
                    break;

            }
        }
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        if (name.getText().toString().trim().isEmpty()) {
            inputLayout.setError(message);
            inputLayout.setErrorEnabled(true);
            requestFocus(name);
            name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            /*MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((HomeMatchMakingInputScreen) activity).regularTypeface);
            mct.show(message);*/
            wholeScrren.post(new Runnable() {
                public void run() {
                    wholeScrren.scrollTo(0, 0);
                }
            });
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
            inputLayout.setError(null);
            name.getBackground().setColorFilter(null);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    protected void showToolTipInToast(String tips, int x, int y) {
        Context context = activity;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, tips, duration);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, x, y);
        toast.show();

    }

    private String getFormatedTextToShowDate(BeanDateTime beanDateTime) {
        String strDateTime = null;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};
        strDateTime = CUtils.pad(beanDateTime.getDay()) + "-" + months[beanDateTime.getMonth()] + "-" + beanDateTime.getYear();
        return strDateTime;
    }

   /* private String getFormatedTextToShowTime(BeanDateTime beanDateTime) {
        String strDateTime = null;
        strDateTime = CUtils.pad(beanDateTime.getHour()) + ":" + CUtils.pad(beanDateTime.getMin()) + ":" + CUtils.pad(beanDateTime.getSecond()) + " (in 24 hour)";
        return strDateTime;
    }*/

    public void updateBirthPlacePerson1(BeanPlace beanPlace) {
        this.beanPlacePerson1 = beanPlace;
        _beanHoroPersonalInfoPerson1.setPlace(beanPlace);
        txtPlaceNamePerson1.setText(beanPlacePerson1.getCityName().trim());
        txtPlaceDetailPerson1.setText(CUtils.getPlaceDetailInSingleString(_beanHoroPersonalInfoPerson1.getPlace()));
    }

    //FUNCTIONS FOR PERSON 1
    public void updateBirthDetailPerson1(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        setUserBirthDetailBeanPerson1(beanHoroPersonalInfo);
        iMatchingInputDetailFragment.setPersonVariableValue(HomeMatchMakingInputScreen.PERSON2);
    }

    public void updateBirthDatePerson1(BeanDateTime beanDateTime) {
        this.beanDateTimePerson1.setYear(beanDateTime.getYear());
        this.beanDateTimePerson1.setMonth(beanDateTime.getMonth());
        this.beanDateTimePerson1.setDay(beanDateTime.getDay());
        buttonCalendarPerson1.setText(getFormatedTextToShowDate(beanDateTime));
        /*set kundli date in newKundliSelectedDate*/
        if (buttonCalendarPerson1.toString().contains("-")) {
            int monthInCount = 0;
            String[] getYear = buttonCalendarPerson1.getText().toString().split("-");
            CUtils.setNewKundliSelectedDate(Integer.parseInt(getYear[2].trim()));
            String[] shortMonth = getResources().getStringArray(R.array.month_short_name_list);
            for (int i = 0; i < shortMonth.length; i++) {
                if (shortMonth[i].equalsIgnoreCase(getYear[1].trim())) {
                    monthInCount = i;
                    break;
                }
            }
            CUtils.setNewKundliSelectedMonth(monthInCount + 1);
        }
        updateLocationTimeZonePerson1();
    }

    public void updateBirthTimePerson1(BeanDateTime beanDateTime) {
        this.beanDateTimePerson1.setHour(beanDateTime.getHour());
        this.beanDateTimePerson1.setMin(beanDateTime.getMin());
        this.beanDateTimePerson1.setSecond(beanDateTime.getSecond());
        buttonTimePerson1.setText(CUtils.getFormatedTextToShowTime(beanDateTime));
    }

    public void resetBirthDetailForm() {
        setUserBirthDetailBeanPerson1(new BeanHoroPersonalInfo());
    }

    protected void setUserBirthDetailBeanPerson1(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        this._beanHoroPersonalInfoPerson1 = beanHoroPersonalInfo;
        etUserNamePerson1.setText(beanHoroPersonalInfo.getName());
        etUserNamePerson1.setError(null);
        beanPlacePerson1 = beanHoroPersonalInfo.getPlace();
        txtPlaceNamePerson1.setText(beanPlacePerson1.getCityName().trim());
        txtPlaceDetailPerson1.setText(CUtils.getPlaceDetailInSingleString(_beanHoroPersonalInfoPerson1.getPlace()));
        beanDateTimePerson1 = beanHoroPersonalInfo.getDateTime();
        buttonCalendarPerson1.setText(getFormatedTextToShowDate(beanDateTimePerson1));
        buttonTimePerson1.setText(CUtils.getFormatedTextToShowTime(beanDateTimePerson1));

    }
    //END FUNCTIONS FOR PERSON 1

    //FUNCTIONS FOR PERSON 2
    public void updateBirthPlacePerson2(BeanPlace beanPlace) {
        this.beanPlacePerson2 = beanPlace;
        _beanHoroPersonalInfoPerson2.setPlace(beanPlace);
        txtPlaceNamePerson2.setText(beanPlacePerson2.getCityName().trim());
        txtPlaceDetailPerson2.setText(CUtils.getPlaceDetailInSingleString(_beanHoroPersonalInfoPerson2.getPlace()));
    }

    public void updateBirthDetailPerson2(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        setUserBirthDetailBeanPerson2(beanHoroPersonalInfo);
    }

    public void updateBirthDatePerson2(BeanDateTime beanDateTime) {
        this.beanDateTimePerson2.setYear(beanDateTime.getYear());
        this.beanDateTimePerson2.setMonth(beanDateTime.getMonth());
        this.beanDateTimePerson2.setDay(beanDateTime.getDay());
        buttonCalendarPerson2.setText(getFormatedTextToShowDate(beanDateTime));
        /*set kundli date in newKundliSelectedDate*/
        if (buttonCalendarPerson2.toString().contains("-")) {
            int monthInCount = 0;
            String[] getYear = buttonCalendarPerson2.getText().toString().split("-");
            CUtils.setNewKundliSelectedDate(Integer.parseInt(getYear[2].trim()));
            String[] shortMonth = getResources().getStringArray(R.array.month_short_name_list);
            for (int i = 0; i < shortMonth.length; i++) {
                if (shortMonth[i].equalsIgnoreCase(getYear[1].trim())) {
                    monthInCount = i;
                    break;
                }
            }
            CUtils.setNewKundliSelectedMonth(monthInCount + 1);
        }
        updateLocationTimeZonePerson2();
    }

    public void updateBirthTimePerson2(BeanDateTime beanDateTime) {
        this.beanDateTimePerson2.setHour(beanDateTime.getHour());
        this.beanDateTimePerson2.setMin(beanDateTime.getMin());
        this.beanDateTimePerson2.setSecond(beanDateTime.getSecond());
        this.buttonTimePerson2.setText(CUtils.getFormatedTextToShowTime(beanDateTime));
    }

    protected void setUserBirthDetailBeanPerson2(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        this._beanHoroPersonalInfoPerson2 = beanHoroPersonalInfo;
        this.etUserNamePerson2.setText(beanHoroPersonalInfo.getName());
        this.etUserNamePerson2.setError(null);
        this.etUserNamePerson2.requestFocus();
        this.beanPlacePerson2 = beanHoroPersonalInfo.getPlace();
        txtPlaceNamePerson2.setText(beanPlacePerson2.getCityName().trim());
        txtPlaceDetailPerson2.setText(CUtils.getPlaceDetailInSingleString(_beanHoroPersonalInfoPerson2.getPlace()));
        this.beanDateTimePerson2 = beanHoroPersonalInfo.getDateTime();
        this.buttonCalendarPerson2.setText(getFormatedTextToShowDate(beanDateTimePerson2));
        this.buttonTimePerson2.setText(CUtils.getFormatedTextToShowTime(beanDateTimePerson2));

    }
    //END FUNCTIONS FOR PERSON 2

    private boolean validateData() {
        boolean flag = false;
        if (validateName(etUserNamePerson1, boyInputLayout, getString(R.string.please_enter_boy_name_v1)) && validateName(etUserNamePerson2, girlInputLayout, getString(R.string.please_enter_girl_name_v1)))
            flag = true;

        return flag;
    }

    protected boolean validForm() {
        boolean valid = true;
        if (etUserNamePerson1.getText().toString().length() == 0) {
            valid = false;
            etUserNamePerson1.setError(getResources().getString(R.string.please_enter_boy_name_v1));
            etUserNamePerson1.requestFocus();
            MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((HomeMatchMakingInputScreen) activity).regularTypeface);
            mct.show(getResources().getString(R.string.please_enter_boy_name));
            wholeScrren.post(new Runnable() {
                public void run() {
                    wholeScrren.scrollTo(0, 0);
                }
            });
        }
        if (etUserNamePerson2.getText().toString().length() == 0) {
            valid = false;
            MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((HomeMatchMakingInputScreen) activity).regularTypeface);
            mct.show(getResources().getString(R.string.please_enter_girl_name));
            etUserNamePerson2.setError(getResources().getString(R.string.please_enter_girl_name_v1));
            etUserNamePerson2.requestFocus();
            wholeScrren.post(new Runnable() {
                public void run() {
                    wholeScrren.scrollTo(0, 0);
                }
            });
        }

        return valid;
    }


    protected BeanHoroPersonalInfo getUserBirthDetailBeanPerson1() {
        if (this._beanHoroPersonalInfoPerson1 == null)
            this._beanHoroPersonalInfoPerson1 = new BeanHoroPersonalInfo();

        this._beanHoroPersonalInfoPerson1.setName(etUserNamePerson1.getText().toString().trim());
        this._beanHoroPersonalInfoPerson1.setPlace(beanPlacePerson1);
        this._beanHoroPersonalInfoPerson1.setDateTime(beanDateTimePerson1);

        this._beanHoroPersonalInfoPerson1.setGender("M");
            /*else
                this.beanHoroPersonalInfo.setGender("F");*/


        return this._beanHoroPersonalInfoPerson1;
    }

    protected BeanHoroPersonalInfo getUserBirthDetailBeanPerson2() {
        if (this._beanHoroPersonalInfoPerson2 == null)
            this._beanHoroPersonalInfoPerson1 = new BeanHoroPersonalInfo();
        //BeanHoroPersonalInfo beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        this._beanHoroPersonalInfoPerson2.setName(etUserNamePerson2.getText().toString().trim());
        this._beanHoroPersonalInfoPerson2.setPlace(beanPlacePerson2);
        this._beanHoroPersonalInfoPerson2.setDateTime(beanDateTimePerson2);
        this._beanHoroPersonalInfoPerson2.setGender("F");

        return this._beanHoroPersonalInfoPerson2;
    }

    public void oneChartDeleted_IfThatIsCurrentOneThenDeleteChartIdFromIt(long chartId, boolean isOnline) {
        String deletedChartId = String.valueOf(chartId);
        if (isOnline) {
            if (this._beanHoroPersonalInfoPerson1.getOnlineChartId().trim().equalsIgnoreCase(deletedChartId)) {
                this._beanHoroPersonalInfoPerson1.setOnlineChartId("");
            }
            if (this._beanHoroPersonalInfoPerson2.getOnlineChartId().trim().equalsIgnoreCase(deletedChartId)) {
                this._beanHoroPersonalInfoPerson2.setOnlineChartId("");
            }
        } else {
            if (this._beanHoroPersonalInfoPerson1.getLocalChartId() == chartId) {
                this._beanHoroPersonalInfoPerson1.setLocalChartId(-1);
            }
            if (this._beanHoroPersonalInfoPerson2.getLocalChartId() == chartId) {
                this._beanHoroPersonalInfoPerson2.setLocalChartId(-1);
            }
        }
    }

    private void setTypefaceOfView() {
        buttonCalculateMatching.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);


        saveChart.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        textViewUserNamePerson2.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        etUserNamePerson2.setTypeface(((HomeMatchMakingInputScreen) activity).robotRegularTypeface);
        textViewCalendarPerson2.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        textViewPlacePerson2.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        buttonCalendarPerson2.setTypeface(((HomeMatchMakingInputScreen) activity).robotMediumTypeface);
        textViewgirlDetail.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        buttonTimePerson2.setTypeface(((HomeMatchMakingInputScreen) activity).robotMediumTypeface);
        txtPlaceNamePerson2.setTypeface(((HomeMatchMakingInputScreen) activity).robotMediumTypeface);
        txtPlaceDetailPerson2.setTypeface(((HomeMatchMakingInputScreen) activity).robotMediumTypeface);
        textViewgirlDetail.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        textViewBoyDetail.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        textViewgirlDetail.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        textViewBoyDetail.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        textViewUserNamePerson1.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        etUserNamePerson1.setTypeface(((HomeMatchMakingInputScreen) activity).robotRegularTypeface);
        textViewCalendarPerson1.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        textViewPlacePerson1.setTypeface(((HomeMatchMakingInputScreen) activity).mediumTypeface);
        buttonCalendarPerson1.setTypeface(((HomeMatchMakingInputScreen) activity).robotMediumTypeface);
        buttonTimePerson1.setTypeface(((HomeMatchMakingInputScreen) activity).robotMediumTypeface);
        txtPlaceNamePerson1.setTypeface(((HomeMatchMakingInputScreen) activity).robotMediumTypeface);
        txtPlaceDetailPerson1.setTypeface(((HomeMatchMakingInputScreen) activity).robotMediumTypeface);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (activity != null && isVisibleToUser) {
            CUtils.hideMyKeyboard(activity);
            View appbarAppModule = activity.findViewById(R.id.appbarAppModule);
            appbarAppModule.setVisibility(View.VISIBLE);
            activity.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void updateLocationTimeZonePerson1() {

        if (beanPlacePerson1.getCountryName().trim().equalsIgnoreCase("Nepal")) {

            if (CUtils.getNewKundliSelectedDate() <= 1985) {
                beanPlacePerson1.setTimeZoneName("GMT+5.5");
                beanPlacePerson1.setTimeZone("5.5");
                beanPlacePerson1.setTimeZoneValue(Float.parseFloat("5.5"));
            } else {
                beanPlacePerson1.setTimeZoneName("GMT+5.75");
                beanPlacePerson1.setTimeZone("5.75");
                beanPlacePerson1.setTimeZoneValue(Float.parseFloat("5.75"));
            }
        }
        if (beanPlacePerson1.getCountryName().trim().equalsIgnoreCase("Suriname")) {
            if (CUtils.getNewKundliSelectedDate() <= 1984) {
                if (CUtils.getNewKundliSelectedDate() == 1984 &&
                        CUtils.getNewKundliSelectedMonth() > 9) {
                    beanPlacePerson1.setTimeZoneName("GMT-3.0");
                    beanPlacePerson1.setTimeZone("-3.0");
                    beanPlacePerson1.setTimeZoneValue(Float.parseFloat("-3.0"));
                } else {
                    beanPlacePerson1.setTimeZoneName("GMT-3.5");
                    beanPlacePerson1.setTimeZone("-3.5");
                    beanPlacePerson1.setTimeZoneValue(Float.parseFloat("-3.5"));
                }
            } else {
                beanPlacePerson1.setTimeZoneName("GMT-3.0");
                beanPlacePerson1.setTimeZone("-3.0");
                beanPlacePerson1.setTimeZoneValue(Float.parseFloat("-3.0"));
            }
        }

        txtPlaceNamePerson1.setText(beanPlacePerson1.getCityName().trim());
        txtPlaceDetailPerson1.setText(CUtils.getPlaceDetailInSingleString(beanPlacePerson1));
    }

    private void updateLocationTimeZonePerson2() {

        if (beanPlacePerson2.getCountryName().trim().equalsIgnoreCase("Nepal")) {

            if (CUtils.getNewKundliSelectedDate() <= 1985) {
                beanPlacePerson2.setTimeZoneName("GMT+5.5");
                beanPlacePerson2.setTimeZone("5.5");
                beanPlacePerson2.setTimeZoneValue(Float.parseFloat("5.5"));
            } else {
                beanPlacePerson2.setTimeZoneName("GMT+5.75");
                beanPlacePerson2.setTimeZone("5.75");
                beanPlacePerson2.setTimeZoneValue(Float.parseFloat("5.75"));
            }
        }
        if (beanPlacePerson2.getCountryName().trim().equalsIgnoreCase("Suriname")) {
            if (CUtils.getNewKundliSelectedDate() <= 1984) {
                if (CUtils.getNewKundliSelectedDate() == 1984 &&
                        CUtils.getNewKundliSelectedMonth() > 9) {
                    beanPlacePerson2.setTimeZoneName("GMT-3.0");
                    beanPlacePerson2.setTimeZone("-3.0");
                    beanPlacePerson2.setTimeZoneValue(Float.parseFloat("-3.0"));
                } else {
                    beanPlacePerson2.setTimeZoneName("GMT-3.5");
                    beanPlacePerson2.setTimeZone("-3.5");
                    beanPlacePerson2.setTimeZoneValue(Float.parseFloat("-3.5"));
                }
            } else {
                beanPlacePerson2.setTimeZoneName("GMT-3.0");
                beanPlacePerson2.setTimeZone("-3.0");
                beanPlacePerson2.setTimeZoneValue(Float.parseFloat("-3.0"));
            }
        }

        txtPlaceNamePerson2.setText(beanPlacePerson2.getCityName().trim());
        txtPlaceDetailPerson2.setText(CUtils.getPlaceDetailInSingleString(beanPlacePerson2));
    }
    private void closeKeyBoard(){
        try {
            //clear focus in BirthDetails Input
            etBirthDetails.clearFocus();
            // Close keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(main_container.getWindowToken(), 0);
        }catch (Exception e){
            //
        }

    }
    /**
     *
     */
    private void getBirthDetails() {
        try {
            if (getActivity() instanceof HomeMatchMakingInputScreen) {
                ((HomeMatchMakingInputScreen) getActivity()).showProgressBar();
            }
        } catch (Exception e){
            //
        }
        Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getBirthDetailsMatching(getMessageParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    ((HomeMatchMakingInputScreen)getActivity()).hideProgressBar();
                } catch (Exception e){
                    //
                }
                try {
                    if (response.body() != null) {
                        String myResponse = response.body().string();
                       //Log.d("TestUIDetails", "myResponse==>>" + myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        if (jsonObject.getInt("status") == 1) {
                            llEnterBirthDetails.setVisibility(View.GONE);
                            btnShowInputBirthDet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_birthdetailsetting_plus, 0);
                            setDataInInputField(jsonObject);
                            CUtils.showSnackbar(main_container, getResources().getString(R.string.details_provided_by_you_has_been_filled), getContext());
                        } else if (jsonObject.getInt("status") == -1) {
                            setDataInInputField(jsonObject);
                            CUtils.showSnackbar(main_container, getResources().getString(R.string.details_provided_by_you_has_been_filled), getContext());
                        }else if (jsonObject.getInt("status") == -4 || jsonObject.getInt("status") == 0 || jsonObject.getInt("status") == 5) {
                            String message = jsonObject.getString("message");
                            CUtils.showSnackbar(main_container, message, getContext());

                        } else if (jsonObject.getInt("status") == 6) {
                            // String message = jsonObject.getString("message");
                            prevBirthDetails = "";
                            displayLoginDialog();
                        }
                    }
                } catch (Exception e) {
                    //Log.d("TestData", "Ex1--::" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                try{
                    ((HomeMatchMakingInputScreen)getActivity()).hideProgressBar();
                } catch (Exception e){
                    //
                }
            }
        });
    }

    private void setDataInInputField(JSONObject jsonObject) {
        try {
            if(jsonObject.has("birthDetails1_presentKeys")) {
                String str = jsonObject.getString("birthDetails1_presentKeys");
                ArrayList<String> dataList = new ArrayList<>(Arrays.asList(str.split(",")));
                HashMap<String, String> boyBirthDetails = new HashMap<String, String>();
                for (int i = 0; i < dataList.size(); i++) {
                    String singleData = dataList.get(i);
                    ArrayList<String> tempAaryList = new ArrayList<>(Arrays.asList(singleData.split(":")));
                    if (tempAaryList.size() == 2) {
                        boyBirthDetails.put(tempAaryList.get(0).trim(), tempAaryList.get(1).trim());
                    }
                }
                /**
                 *
                 */
                if (boyBirthDetails.containsKey("name")) {
                    String name = boyBirthDetails.get("name");
                    if (!TextUtils.isEmpty(name)) {
                        etUserNamePerson1.setText(name.replace("/", "_"));
                        etUserNamePerson1.setError(null);
                    }
                }
                BeanDateTime beanDateTime = new BeanDateTime();
                if(boyBirthDetails.containsKey("day")&&boyBirthDetails.containsKey("month")&&boyBirthDetails.containsKey("year")) {
                    int day = Integer.parseInt(boyBirthDetails.get("day"));
                    int month = Integer.parseInt(boyBirthDetails.get("month"));
                    int year = Integer.parseInt(boyBirthDetails.get("year"));

                    if (day != 0 && month != 0 && year != 0) {
                        beanDateTime.setYear(year);
                        beanDateTime.setMonth(month - 1);
                        beanDateTime.setDay(day);
                        beanDateTime.setHour(0);
                        beanDateTime.setMin(0);
                        beanDateTime.setSecond(0);
                        updateBirthDatePerson1(beanDateTime);
                        buttonCalendarPerson1.setText(getFormatedTextToShowDate(beanDateTime));
                    }
                }
                if(boyBirthDetails.containsKey("hour")&&boyBirthDetails.containsKey("minute")){
                    int hour = Integer.parseInt(boyBirthDetails.get("hour"));
                    int minute = Integer.parseInt(boyBirthDetails.get("minute"));
                        beanDateTime.setHour(hour);
                        beanDateTime.setMin(minute);
                        beanDateTime.setSecond(0);
                        updateBirthTimePerson1(beanDateTime);
                        buttonTimePerson1.setText(CUtils.getFormatedTextToShowTime(beanDateTime));
                }





                //txtPlaceNamePerson1.setText(beanPlacePerson1.getCityName().trim());
                //txtPlaceDetailPerson1.setText(CUtils.getPlaceDetailInSingleString(_beanHoroPersonalInfoPerson1.getPlace()));
                //buttonCalendarPerson1.setText(getFormatedTextToShowDate(beanDateTimePerson1));
                //buttonTimePerson1.setText(CUtils.getFormatedTextToShowTime(beanDateTimePerson1));
                //setUserBirthDetailBeanPerson1();
            }
            if(jsonObject.has("birthDetails2_presentKeys")) {
                String str = jsonObject.getString("birthDetails2_presentKeys");
                ArrayList<String> dataList = new ArrayList<>(Arrays.asList(str.split(",")));
                HashMap<String, String> girlBirthDetails = new HashMap<String, String>();
                for (int i = 0; i < dataList.size(); i++) {
                    String singleData = dataList.get(i);
                    ArrayList<String> tempAaryList = new ArrayList<>(Arrays.asList(singleData.split(":")));
                    if (tempAaryList.size() == 2) {
                        girlBirthDetails.put(tempAaryList.get(0).trim(), tempAaryList.get(1).trim());
                    }
                }
                /**
                 *
                 */
                if (girlBirthDetails.containsKey("name")) {
                    String name = girlBirthDetails.get("name");
                    if (!TextUtils.isEmpty(name)) {
                        etUserNamePerson2.setText(name.replace("/", "_"));
                        etUserNamePerson2.setError(null);
                    }
                }
                BeanDateTime beanDateTime = new BeanDateTime();
                if (girlBirthDetails.containsKey("day") && girlBirthDetails.containsKey("month") && girlBirthDetails.containsKey("year")) {
                    int day = Integer.parseInt(girlBirthDetails.get("day"));
                    int month = Integer.parseInt(girlBirthDetails.get("month"));
                    int year = Integer.parseInt(girlBirthDetails.get("year"));

                    if (day != 0 && month != 0 && year != 0) {
                        beanDateTime.setYear(year);
                        beanDateTime.setMonth(month - 1);
                        beanDateTime.setDay(day);
                        beanDateTime.setHour(0);
                        beanDateTime.setMin(0);
                        beanDateTime.setSecond(0);
                        updateBirthDatePerson2(beanDateTime);
                        buttonCalendarPerson2.setText(getFormatedTextToShowDate(beanDateTime));
                    }
                }
                if (girlBirthDetails.containsKey("hour") && girlBirthDetails.containsKey("minute")) {
                    int hour = Integer.parseInt(girlBirthDetails.get("hour"));
                    int minute = Integer.parseInt(girlBirthDetails.get("minute"));
                        beanDateTime.setHour(hour);
                        beanDateTime.setMin(minute);
                        beanDateTime.setSecond(0);
                        updateBirthTimePerson2(beanDateTime);
                        buttonTimePerson2.setText(CUtils.getFormatedTextToShowTime(beanDateTime));
                    
                }
            }

            if(jsonObject.has("cityDetails1")){
                String[] cityDetails = jsonObject.getString("cityDetails1").split("\\|");
                //Log.d("TestUIDetails","cityDetails.length:---"+cityDetails.length);
                if(cityDetails.length > 10){

                    beanPlacePerson1.setCityName(cityDetails[0]);
                    beanPlacePerson1.setCountryName(cityDetails[2]);
                    beanPlacePerson1.setState(cityDetails[1]);
                    beanPlacePerson1.setLatDeg(cityDetails[3]);  // Corrected parsing
                    beanPlacePerson1.setLatMin(cityDetails[4]);  // Fixed latitude minutes
                    //place.setLatNS(cityDetails[5]);
                    beanPlacePerson1.setLongDeg(cityDetails[6]);
                    beanPlacePerson1.setLongMin(cityDetails[7]);
                    //place.setLongEW(cityDetails[8]);
                    beanPlacePerson1.setTimeZone(cityDetails[9]);
                    try {
                        beanPlacePerson1.setTimeZoneValue(Float.parseFloat(cityDetails[9]));
                    } catch (Exception e){
                        //
                    }
                    updateLocationTimeZonePerson1();
                }
            }
            if(jsonObject.has("cityDetails2")){
                String[] cityDetails = jsonObject.getString("cityDetails2").split("\\|");
                //Log.d("TestUIDetails","cityDetails.length:---"+cityDetails.length);
                if(cityDetails.length > 10){

                    beanPlacePerson2.setCityName(cityDetails[0]);
                    beanPlacePerson2.setCountryName(cityDetails[2]);
                    beanPlacePerson2.setState(cityDetails[1]);
                    beanPlacePerson2.setLatDeg(cityDetails[3]);  // Corrected parsing
                    beanPlacePerson2.setLatMin(cityDetails[4]);  // Fixed latitude minutes
                    //place.setLatNS(cityDetails[5]);
                    beanPlacePerson2.setLongDeg(cityDetails[6]);
                    beanPlacePerson2.setLongMin(cityDetails[7]);
                    //place.setLongEW(cityDetails[8]);
                    beanPlacePerson2.setTimeZone(cityDetails[9]);
                    try {
                        beanPlacePerson2.setTimeZoneValue(Float.parseFloat(cityDetails[9]));
                    } catch (Exception e){
                        //
                    }
                    updateLocationTimeZonePerson2();
                }
            }

        } catch (Exception e) {
            //
        }

    }

    private Map<String, String> getMessageParams() {

        Context mContext = AstrosageKundliApplication.getAppContext();
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(mContext));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(getActivity()));
        //headers.put("message", "Paritosh (birth details)DoB 26.10.1993ToB 20:53 pmPoB: Bareilly");
        headers.put("message", etBirthDetails.getText().toString().trim());
        headers.put("userid", CUtils.getUserName(mContext));
        headers.put("pw",CUtils.getUserPassword(mContext) );
        headers.put("extradata", "");
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(mContext));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(headers);
    }

}
