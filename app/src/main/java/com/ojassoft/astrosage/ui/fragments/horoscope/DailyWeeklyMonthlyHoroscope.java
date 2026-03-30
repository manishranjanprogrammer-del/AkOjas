package com.ojassoft.astrosage.ui.fragments.horoscope;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class DailyWeeklyMonthlyHoroscope extends Fragment {

    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;

    public final int DAILY_TYPE = 0;
    public final int WEEKLY_TYPE = 1;
    public final int WEEKLY_LOVE_TYPE = 2;
    public final int MONTHLY_TYPE = 3;

    int horoscopeType;
    int rashiType;
    String strRashiName = "";
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private static final String URL = CGlobalVariables.gPlusUrl;
    private TextView _tvShowMoonRashiSign, rashiIntro;
    ImageView _imageRashiWithoutName;
    private ProgressBar progressBar;
    private long MILLIs_IN_WEEK = 6 * 24 * 60 * 60 * 1000;

    String[] rashifal;
    private GetTodayRashifalASync getTodayRashifalASync;

    public static DailyWeeklyMonthlyHoroscope newInstance(int horoscopeType, int rashiType) {

        DailyWeeklyMonthlyHoroscope dailyWeeklyMonthlyHoroscopeFragment = new DailyWeeklyMonthlyHoroscope();
        Bundle args = new Bundle();
        args.putInt("horoscopeType", horoscopeType);
        args.putInt("rashiType", rashiType);
        dailyWeeklyMonthlyHoroscopeFragment.setArguments(args);

        return dailyWeeklyMonthlyHoroscopeFragment;
    }

    private void initValues() {
        typeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        initValues();
        horoscopeType = getArguments().getInt("horoscopeType", 0);
        rashiType = getArguments().getInt("rashiType", 0);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lay_daily_weekly_monthly_horoscope, container, false);
        /*mPlusClient = new PlusClient.Builder(getActivity(), DailyWeeklyMonthlyHoroscope.this, DailyWeeklyMonthlyHoroscope.this)
                .clearScopes()
                .build();*/
        Button whatsAppBtn = (Button) view.findViewById(R.id.whatsapp_standard_ann_button);
        whatsAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DetailedHoroscope) getActivity()).shareMessageWithWhatsApp(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_WEEKLY);
            }
        });
        strRashiName = getResources().getStringArray(R.array.rashiName_list)[rashiType];
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        if (horoscopeType == DAILY_TYPE) {
            // here we set moonrashi sign
            _tvShowMoonRashiSign = (TextView) view.findViewById(R.id.textViewShowMoonRashiSign);
            String heading = CUtils.returnFormattedString(LANGUAGE_CODE, getResources().getString(R.string.MoonSignDaily));
            heading = heading.replace("#", strRashiName);
            heading = heading.replace("$", getTodayDate());
            _tvShowMoonRashiSign.setText(heading);
        } else if (horoscopeType == WEEKLY_TYPE) {
            //here we set moonrashi sign
            _tvShowMoonRashiSign = (TextView) view.findViewById(R.id.textViewShowMoonRashiSign);
            String heading = CUtils.returnFormattedString(LANGUAGE_CODE, getResources().getString(R.string.moonSignWeekly));
            heading = heading.replace("#", strRashiName);
            heading = heading.replace("$", getWeeklyDate());
            _tvShowMoonRashiSign.setText(heading);
        } else if (horoscopeType == WEEKLY_LOVE_TYPE) {

            //here we set moonrashi sign
            _tvShowMoonRashiSign = (TextView) view.findViewById(R.id.textViewShowMoonRashiSign);
            String heading = CUtils.returnFormattedString(LANGUAGE_CODE, getResources().getString(R.string.weeklyLoveHeading));
            heading = heading.replace("#", strRashiName);
            heading = heading.replace("$", getWeeklyDate());
            _tvShowMoonRashiSign.setText(heading);
        } else if (horoscopeType == MONTHLY_TYPE) {
            //here we set moonrashi sign
            _tvShowMoonRashiSign = (TextView) view.findViewById(R.id.textViewShowMoonRashiSign);
            String heading = CUtils.returnFormattedString(LANGUAGE_CODE, getResources().getString(R.string.moonSignMonthly));
            heading = heading.replace("#", strRashiName);
            heading = heading.replace("$", getMonthName());
            _tvShowMoonRashiSign.setText(heading);
        }
        _tvShowMoonRashiSign.setTypeface(typeface, Typeface.BOLD);
        //here we set icon of particular sign(rashi).
        _imageRashiWithoutName = (ImageView) view.findViewById(R.id.imageViewRasi);
        _imageRashiWithoutName.setImageResource(CGlobalVariables.rashiImageWithoutName[rashiType]);
        //here we show introduction of rashi.
        rashiIntro = (TextView) view.findViewById(R.id.textViewPrediction);
        rashiIntro.setTypeface(typeface);
        rashiIntro.setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_OVERLAY);
        registerForContextMenu(rashiIntro);
        if (getTodayRashifalASync != null) {
            getTodayRashifalASync.cancel(true);
        }
        getTodayRashifalASync = new GetTodayRashifalASync();
        getTodayRashifalASync.execute();

        return view;
    }

    private String clipBoardText = "";

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        clipBoardText = ((TextView) v).getText().toString();
        menu.setHeaderTitle(getString(R.string.select_item));
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menucopy, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.copyMenu:
                CUtils.copyTextToClipBoard(clipBoardText, getActivity());
                return true;
            case R.id.shareMenu:
                CUtils.sharePrediction(getActivity(), _tvShowMoonRashiSign.getText().toString(), clipBoardText);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * This is AsyncTask to fetch today's weekly and monthly horoscope RSS feed.
     *
     * @author Hukum
     * @since 15-May-2013
     */
    private class GetTodayRashifalASync extends AsyncTask<String, Long, Void> {

        @Override
        protected Void doInBackground(String... params) {

            if (horoscopeType == DAILY_TYPE) {
                //rashifal = CUtils.getTodaysPredictinDetail(getActivity(), null, LANGUAGE_CODE, rashiType, 0);
            } else if (horoscopeType == WEEKLY_TYPE) {
                //rashifal = CUtils.getWeeklyPredictinDetail(LANGUAGE_CODE, getActivity(), rashiType);
            } else if (horoscopeType == MONTHLY_TYPE) {
                //rashifal = CUtils.getMonthlyPredictinDetail(LANGUAGE_CODE, getActivity(), rashiType);
            } else if (horoscopeType == WEEKLY_LOVE_TYPE) {
                //rashifal = CUtils.getWeeklyLovePredictinDetail(LANGUAGE_CODE, getActivity(), rashiType);
            }

            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            try {
                if (progressBar.isShown())
                    progressBar.setVisibility(View.GONE);
            } catch (Exception e) {
                // nothing
            }

            rashifal[0] = rashifal[0].replace("AstroSage.com,", "");

            rashiIntro.setTypeface(Typeface.DEFAULT); //Set Typeface to Default as this prediction comes from server in unicode and some characters are not displayed correctly if Hindi Typeface is set.
            rashiIntro.setText(Html.fromHtml(rashifal[0]));
            rashiIntro.setContentDescription(Html.fromHtml(rashifal[0]));
            if (rashifal[1].equals("NO_INTERNET")) {
                rashiIntro.setText(" ");
                rashiIntro.setContentDescription(" ");
                MyCustomToast mct2 = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), typeface);
                mct2.show(getResources().getString(R.string.internet_is_not_working));
            }

            super.onPostExecute(result);
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
    }


    private String getTodayDate() {
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ENGLISH);
        String[] dateElements = dateFormat.format(date.getTime()).split("/");
        int month = Integer.valueOf(dateElements[0]);
        String year = "20" + dateElements[2];
        String result = dateElements[1] + " " + getResources().getStringArray(R.array.MonthName)[month - 1] + getResources().getString(R.string.comma) + " " + year;
        return result;
    }

    private String getWeeklyDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int satrtDate, startMonth, endDate, endMonth;
        satrtDate = calendar.get(Calendar.DAY_OF_MONTH);
        startMonth = calendar.get(Calendar.MONTH);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + MILLIs_IN_WEEK);
        endDate = calendar.get(Calendar.DAY_OF_MONTH);
        endMonth = calendar.get(Calendar.MONTH);
        String formattedString = satrtDate + " " + getResources().getStringArray(R.array.MonthName)[startMonth] + " " + getResources().getString(R.string.str_to) + " " +
                endDate + " " + getResources().getStringArray(R.array.MonthName)[endMonth];
        return formattedString;
    }

    private String getMonthName() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String formattedString = getResources().getStringArray(R.array.MonthName)[month] + " " + year;
        return formattedString;
    }

    @Override
    public void onStart() {
        super.onStart();
        //  mPlusClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (progressBar.isShown())
            progressBar.setVisibility(View.GONE);
        // mPlusClient.disconnect();
    }

    @Override
    public void onDestroy() {
        if (getTodayRashifalASync.getStatus() == Status.RUNNING)
            getTodayRashifalASync.cancel(true);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getTodayRashifalASync.getStatus() == Status.RUNNING)
            getTodayRashifalASync.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the state of the +1 button each time we receive focus.
        //mPlusOneStandardButtonWithAnnotation.initialize(mPlusClient, URL, PLUS_ONE_REQUEST_CODE);
        //  mPlusOneStandardButtonWithAnnotation.initialize(URL, PLUS_ONE_REQUEST_CODE);//CHANGED BY DEEPAK ON 21-11-2014
    }

   /* @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionSuspended(int i) {

    }*/

   /* private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);

        return builder.build();
    }*/
}