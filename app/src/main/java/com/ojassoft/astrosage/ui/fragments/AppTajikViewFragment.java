package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IOpenInputYearCalendar;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customviews.predictions.WebViewPredictions;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.lang.reflect.Field;
import java.util.Calendar;

public class AppTajikViewFragment extends Fragment {
    View _view = null;
    int year;
    DatePickerDialog dg;
    IOpenInputYearCalendar iOpenInputYearCalendar;
    int moduleId;
    int subModuleId;
    int tajikInputYear;
    int languageIndex;
    Typeface typeface;
    String msg;
    boolean isShow = false;
    boolean isToastShouldShow = false;


    public static AppTajikViewFragment newInstance(int moduleId, int subModuleId, int tajikInputYear, boolean isToastShouldShow) {
        AppTajikViewFragment appViewFragment = new AppTajikViewFragment();

        Bundle args = new Bundle();
        args.putInt("moduleId", moduleId);
        args.putInt("subModuleId", subModuleId);
        args.putInt("tajikInputYear", tajikInputYear);
        args.putBoolean("isToastShouldShow", isToastShouldShow);
        appViewFragment.setArguments(args);

        return appViewFragment;
    }

    public AppTajikViewFragment() {
        setRetainInstance(true);
    }

    /*  @Override
      public void setMenuVisibility(boolean menuVisible) {
          super.setMenuVisibility(menuVisible);
          if (menuVisible) {
              if (isToastShouldShow) {
                  showToast();
              }
              isToastShouldShow = true;
          } else {
              isToastShouldShow = false;
          }
      }*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            showToast();
        } else {

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        moduleId = getArguments().getInt("moduleId", 0);
        subModuleId = getArguments().getInt("subModuleId", 0);
        tajikInputYear = getArguments().getInt("tajikInputYear", 0);
        isToastShouldShow = getArguments().getBoolean("isToastShouldShow", false);
        SharedPreferences sharedPreferencesForLang = getActivity().getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        languageIndex = sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, 0);

        typeface = CUtils.getRobotoFont(getActivity(), languageIndex, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.lay_tajik, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llTajikContent);
        if (_view == null) {
            try {
                //Log.e("SAN ", " ATVF Inside onCreate = " + subModuleId );
                _view = CGenerateAppViews.getViewFor(moduleId, subModuleId, getActivity(), typeface, ((OutputMasterActivity) getActivity()).chart_Style, languageIndex, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, tajikInputYear);
            } catch (Exception e) {
                //Toast.makeText(getActivity(),"Server Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
                if (isToastShouldShow)
                    this.msg = e.getMessage();

            }
        }


        if (_view != null) {
            if (_view.getTag() != null) {
                //Log.e("SAN ", " _view.getTag() = " + _view.getTag() );
                linearLayout.addView(new WebViewPredictions(getActivity(), _view.getTag().toString()));
            } else {
                //Log.e("SAN ", " _view.getTag() else _view.getId() = " + _view.getId() );
                linearLayout.addView(_view);
            }
        }
        TextView tvTajikYearTitle = (TextView) view.findViewById(R.id.tvTajikYearTitle);
        year = tajikInputYear + CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getYear();
        if(getActivity() instanceof OutputMasterActivity){
            Log.e("datecheck", "onCreateView: "+year);
            ((OutputMasterActivity)getActivity()).varshPhalSelectedYear = String.valueOf(year);
        }
        tvTajikYearTitle.setText(getYearText(year));
        tvTajikYearTitle.setTypeface(typeface);
        tvTajikYearTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAndroidDatePicker();
//                ((OutputMasterActivity) getActivity()).initMonthPicker(year);
                ((OutputMasterActivity) getActivity()).showYearPicker(year);
            }
        });


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iOpenInputYearCalendar = (IOpenInputYearCalendar) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iOpenInputYearCalendar = null;
    }

    private void fireCalenderOpenEvent() {
        iOpenInputYearCalendar.openInputYearCalendar();
    }

    private String getYearText(int year) {
        int iYear = year;
        int monthNumber = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getMonth();
        StringBuilder sb = new StringBuilder();
        String monthName = getActivity().getResources().getStringArray(R.array.month_short_name_list)[monthNumber];
        sb.append(monthName + " " + String.valueOf(iYear) + " " + getActivity().getResources().getString(R.string.desh_character) + " ");
        sb.append(monthName + " " + String.valueOf(iYear + 1));
        return sb.toString();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (_view != null) {
            ViewGroup parentViewGroup = (ViewGroup) _view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    public void showToast() {
        if (getActivity() != null && msg != null)
            Toast.makeText(getActivity(), "Server Error : " + msg, Toast.LENGTH_SHORT).show();
    }

}
