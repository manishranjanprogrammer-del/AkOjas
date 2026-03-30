package com.ojassoft.astrosage.ui.act;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;

//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.NoInternetException;
import com.ojassoft.astrosage.customexceptions.UICOnlineChartOperationException;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

//import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ActCalculateKundli extends BaseInputActivity {
    Typeface typeface;
    int ModuleId, SubModuleId;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface regularTypeface;

    public ActCalculateKundli() {
        super(R.string.astrosage_name);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_calculate_kundli);

        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();
        regularTypeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        ModuleId = getIntent().getIntExtra("ModuleId", 0);
        SubModuleId = getIntent().getIntExtra("SubModuleId", 0);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
      //  new CCalculateOnlineDataSync(CGlobal.getCGlobalObject().getHoroPersonalInfoObject()).execute();
   // }
        CalculateKundli kundli = new CalculateKundli(CGlobal.getCGlobalObject().getHoroPersonalInfoObject(), CUtils.isConnectedWithInternet(getApplicationContext()), ActCalculateKundli.this, regularTypeface, ModuleId, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SubModuleId);
        kundli.calculate();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        typeface = CUtils.getRobotoFont(getApplicationContext(), CUtils.getLanguageCodeFromPreference(getApplicationContext()), CGlobalVariables.regular);

    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    private class CCalculateOnlineDataSync extends AsyncTask<String, Long, Void> {
        CustomProgressDialog processDialog = null;
        BeanHoroPersonalInfo beanHoroPersonalInfo;
        boolean isSuccessCalculation = true;
        ;
        String msg = "";

        public CCalculateOnlineDataSync(BeanHoroPersonalInfo beanHoroPersonalInfo) {
            this.beanHoroPersonalInfo = beanHoroPersonalInfo;

        }

        @Override
        protected Void doInBackground(String... arg0) {

            ControllerManager _controllerManager = new ControllerManager();
            try {

                _controllerManager.calculateKundliData(beanHoroPersonalInfo, true, CUtils.isConnectedWithInternet(getApplicationContext()));
            } catch (UICOnlineChartOperationException e) {
                e.printStackTrace();
                msg = e.getMessage();
                isSuccessCalculation = false;
            } catch (NoInternetException e) {
                isSuccessCalculation = false;
                e.printStackTrace();
            }

            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {

            try {
                if (processDialog != null & processDialog.isShowing())
                    processDialog.dismiss();

            } catch (Exception e) {

            }
            if (isSuccessCalculation) {
                Intent intent = new Intent(ActCalculateKundli.this, OutputMasterActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences sharedPreferencesForLang = getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
                intent.putExtra(CGlobalVariables.LANGUAGE_CODE, sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, 0));
                intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, ModuleId);
                intent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, SubModuleId);
                intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN, sharedPreferences.getInt(CGlobalVariables.APP_PREFS_ChartStyle, CGlobalVariables.CHART_NORTH_STYLE));
                startActivity(intent);
                ActCalculateKundli.this.finish();
            } else {
                if (msg.length() > 0) {
                    MyCustomToast mct = new MyCustomToast(ActCalculateKundli.this, ActCalculateKundli.this.getLayoutInflater(), ActCalculateKundli.this, Typeface.DEFAULT);
                    mct.show(msg);
                } else {
                    MyCustomToast mct2 = new MyCustomToast(ActCalculateKundli.this, ActCalculateKundli.this.getLayoutInflater(), ActCalculateKundli.this, typeface);
                    mct2.show(getResources().getString(R.string.no_internet));
                }
            }
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
        /*	processDialog = ProgressDialog.show(ActCalculateKundli.this, null, getResources().getString(R.string.msg_please_wait), true, false);
			TextView tvMsg = (TextView) processDialog.findViewById(android.R.id.message);
			tvMsg.setTypeface(CUtils.getUserSelectedLanguageFontType(getApplicationContext(), CUtils.getLanguageCodeFromPreference(getApplicationContext())));
			tvMsg.setTextSize(20);*/

            processDialog = new CustomProgressDialog(ActCalculateKundli.this, typeface);
            processDialog.show();
        }
    }
}
