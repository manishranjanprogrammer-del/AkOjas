package com.ojassoft.astrosage.ui.act;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.DownloadPdfReceiver;
import com.ojassoft.astrosage.misc.DownloadPdfService;
import com.ojassoft.astrosage.model.PrinCategory;
import com.ojassoft.astrosage.model.PrintModuleData;
import com.ojassoft.astrosage.model.PrintSubCategory;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.KundliCategoryDescFrag;
import com.ojassoft.astrosage.ui.fragments.KundliCategoryFrag;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Build.VERSION.SDK_INT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_PRINT_DHRUV_PDF;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_CHART_STYLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RECEIVER;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_URL;

public class ActPrintKundliCategory extends BaseInputActivity implements KundliCategoryFrag.RefreshList, VolleyResponse {
    public KundliCategoryDescFrag kundliCategoryDescFrag;
    public KundliCategoryFrag kundliCategoryFrag;
    //public boolean selectAllCategory = false;
    public CheckBox checkBox;
    public PrinCategory allDataList;
    public ArrayList<String> categoryList;
    CustomProgressDialog pd = null;
    String url;
    public int startYear;
    public int numberOfYear = 5;
    private int chart_Style;
    private boolean isPdfShare;
    ArrayList<PrintModuleData> moduleDataArrayList;


    public ActPrintKundliCategory() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_screen_layout);
        startYear = Calendar.getInstance().get(Calendar.YEAR);
        setStatusBarColor();
        categoryList = new ArrayList<>();
        //allDataList = getData();
        checkBox = (CheckBox) findViewById(R.id.all_cat_check_box);
        TextView filterHeading = (TextView) findViewById(R.id.filter_heading);
        Button printBtn = (Button) findViewById(R.id.print_text);
        Button closeBtn = (Button) findViewById(R.id.close_text);
        LinearLayout shareLL = findViewById(R.id.shareLL);
        printBtn.setTypeface(mediumTypeface);
        closeBtn.setTypeface(mediumTypeface);
        filterHeading.setTypeface(regularTypeface);
        checkBox.setTypeface(regularTypeface);
        if (getIntent() != null)
            chart_Style = getIntent().getIntExtra("ChartStyle", 0);
        url = CGlobalVariables.printCategoryUrl + "Language=" + LANGUAGE_CODE + "," + "UserPlan=" + String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ActPrintKundliCategory.this));
        checkCachedData();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateCatData(b);
            }
        });
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.googleAnalyticSendWitPlayServie(ActPrintKundliCategory.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        GOOGLE_ANALYTIC_KUNDLI_PRINT_DHRUV_PDF, null);

                //CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_PRINT_DHRUV_PDF, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, ""); //now trigger from addFacebookAndFirebaseEvent
                com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF,"ActPrintKundliCategory");

                isPdfShare = false;
                printPDF();
            }
        });
        shareLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.googleAnalyticSendWitPlayServie(ActPrintKundliCategory.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_SHARE_DHRUV_PDF, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_SHARE_DHRUV_PDF, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                isPdfShare = true;
                printPDF();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.googleAnalyticSendWitPlayServie(ActPrintKundliCategory.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_CLOSE_DHRUV_PDF, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_CLOSE_DHRUV_PDF, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                finish();
            }
        });
    }

    public void updateView() {
        if (isAllCatSelected(allDataList.getSubCategories())) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    private void setStatusBarColor() {
        //Used for setting status
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.ColorPrimary));
        }
    }

    @Override
    public void changeDataOnClick(int position, String title) {
        if (kundliCategoryDescFrag == null || allDataList == null) return;
        if (allDataList.getSubCategories() != null && allDataList.getSubCategories().size() > position) {
            PrintSubCategory printSubCategory = allDataList.getSubCategories().get(position);
            if (printSubCategory != null) {
                kundliCategoryDescFrag.refreshData(printSubCategory, title, printSubCategory.isAllSubCatSelected(), position);
            }
        }
    }

    /**
     * Get Data from server
     *
     * @param isShowProgressbar
     */
    private void getCategory(boolean isShowProgressbar) {
        if (isShowProgressbar) {
            showProgressBar();
        }
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsNew(), 0).getMyStringRequest();
        queue.add(stringRequest);
    }

    public Map<String, String> getParamsNew() {

        String key = CUtils.getApplicationSignatureHashCode(ActPrintKundliCategory.this);
        HashMap<String, String> params = new HashMap<>();
        params.put(CGlobalVariables.KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ActPrintKundliCategory.this)));
        params.put("key", key);
        params.put("asplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ActPrintKundliCategory.this)));
        params.put("language", "" + LANGUAGE_CODE);
        return params;
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        try {
            if (allDataList == null) {
                //response = getRsponseString();
                //response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                allDataList = parseData(response);

                setData();

            }
        } catch (Exception ignored) {

        }
    }


    /**
     * Set fragment after get result
     */
    private void setData() {
        if (allDataList != null) {
            kundliCategoryDescFrag = KundliCategoryDescFrag.newInstance(allDataList.getSubCategories().get(0));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.category_desc_list, kundliCategoryDescFrag).commit();

            kundliCategoryFrag = KundliCategoryFrag.newInstance(categoryList);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.category_list, kundliCategoryFrag).commit();
        } else {
            Toast.makeText(this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(VolleyError error) {
        try {
            hideProgressBar();
            if (error != null) {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //
        }

    }

    public void updateCatData(boolean val) {
        try {
            if (allDataList == null) return;
            allDataList.setSelectedAll(val);
            if (val || isAllCatSelected(allDataList.getSubCategories())) {
                ArrayList<PrintSubCategory> arrayList = allDataList.getSubCategories();
                for (int i = 0; i < arrayList.size(); i++) {
                    updateSubCatData(i, val);
                }
            }
            kundliCategoryDescFrag.selectAllCB.setChecked(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSubCatData(int position, boolean val) {
        if (allDataList != null && allDataList.getSubCategories() != null && allDataList.getSubCategories().size() > 0) {
            allDataList.getSubCategories().get(position).setAllSubCatSelected(val);
            if (val || isAllItemSelected(allDataList.getSubCategories().get(position).getSubCatDetails())) {
                ArrayList<PrintSubCategory.SubCatDetail> arrayList = allDataList.getSubCategories().get(position).getSubCatDetails();
                for (int i = 0; i < arrayList.size(); i++) {
                    arrayList.get(i).setCatSelected(val);
                }
            }
        }

    }

    public void doActionOnModuleSelect(int selectedItem) {
        if (moduleDataArrayList != null) {
            updateModuleData(false);
        }
        switch (selectedItem) {
            case 1:
                moduleDataArrayList = getBasicModuleData();
                break;
            case 2:
                moduleDataArrayList = getEasyModuleData();
                break;
            case 3:
                moduleDataArrayList = getVarshfalModuleData();
                break;
            case 4:
                moduleDataArrayList = getKpModuleData();
                break;
            case 5:
                moduleDataArrayList = getLalKitabModuleData();
                break;
            case 6:
                moduleDataArrayList = getJaminiModuleData();
                break;
            case 7:
                moduleDataArrayList = getSpecialModuleData();
                break;
           /* case 8:
                moduleDataArrayList = getDhruvModuleData();
                break;*/
        }
        updateModuleData(true);
        kundliCategoryDescFrag.kundliCategoryAdapter.notifyOnDataChanged(false);
    }

    public void updateModuleData(boolean val) {

        Log.i("", allDataList.toString());
        if (allDataList != null) {
            PrintModuleData printModuleData;
            if (moduleDataArrayList != null) {
                for (int i = 0; i < moduleDataArrayList.size(); i++) {
                    printModuleData = moduleDataArrayList.get(i);
                    allDataList.getSubCategories().get(printModuleData.getIndexOfCategory())
                            .getSubCatDetails().get(printModuleData.getIndexOfSubcategory()).setCatSelected(val);
                }
                ArrayList<PrintSubCategory> printSubCategoriesList = allDataList.getSubCategories();
                PrintSubCategory printSubCategory;
                for (int i = 0; i < printSubCategoriesList.size(); i++) {
                    printSubCategory = printSubCategoriesList.get(i);
                    if (isAllItemSelected(printSubCategory.getSubCatDetails())) {
                        printSubCategory.setAllSubCatSelected(true);
                    } else {
                        printSubCategory.setAllSubCatSelected(false);
                    }
                }


            }
        }
    }

    public boolean isAllItemSelected(ArrayList<PrintSubCategory.SubCatDetail> arrayList) {
        boolean isSelected = true;
        for (int i = 0; i < arrayList.size(); i++) {
            if (!arrayList.get(i).isCatSelected()) {
                isSelected = false;
                break;
            }
        }
        return isSelected;
    }

    public boolean isAllCatSelected(ArrayList<PrintSubCategory> arrayList) {
        boolean isSelected = true;
        for (int i = 0; i < arrayList.size(); i++) {
            if (!arrayList.get(i).isAllSubCatSelected()) {
                isSelected = false;
                break;
            }
        }
        return isSelected;
    }


    private PrinCategory parseData(String response) {
        PrinCategory prinCategory;
        try {
            prinCategory = new PrinCategory();
            prinCategory.setSelectedAll(false);
            ArrayList<PrintSubCategory> arrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject;
            PrintSubCategory printSubCategory;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                printSubCategory = new PrintSubCategory();
                printSubCategory.setCatName(jsonObject.getString("Category"));
                categoryList.add(jsonObject.getString("Category"));
                printSubCategory.setAllSubCatSelected(false);
                ArrayList<PrintSubCategory.SubCatDetail> subCatDetailList = new ArrayList<>();
                PrintSubCategory.SubCatDetail subCatDetail;
                JSONArray innerArray = jsonObject.getJSONArray("SubCategory");
                JSONObject innerObject;
                for (int j = 0; j < innerArray.length(); j++) {
                    innerObject = innerArray.getJSONObject(j);
                    subCatDetail = printSubCategory.new SubCatDetail();
                    subCatDetail.setSuCatName(innerObject.getString("TextToShow"));
                    subCatDetail.setIndexOfModule(innerObject.getString("IndexOfModule"));
                    subCatDetail.setValueOfModule(innerObject.getString("ValueOfModule"));
                    subCatDetail.setCatSelected(false);
                    subCatDetailList.add(subCatDetail);
                }
                printSubCategory.setSubCatDetails(subCatDetailList);
                arrayList.add(printSubCategory);
            }
            prinCategory.setSubCategories(arrayList);
        } catch (Exception e) {
            prinCategory = null;
        }

        return prinCategory;
    }



    private void requestExternalStoragePermission(int requestCode) {

        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            CUtils.requestForExternalStorageNew(this, this, requestCode);
        } else {
            CUtils.requestForExternalStorage(this, this, requestCode);
        }
    }

    private void printPDF() {
        if (allDataList != null) {
            boolean isPageSelected = false;
            String[] pages = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
            ArrayList<PrintSubCategory> subCategories = allDataList.getSubCategories();
            ArrayList<PrintSubCategory.SubCatDetail> subCategoryDetails;

            for (int i = 0; i < subCategories.size(); i++) {
                subCategoryDetails = allDataList.getSubCategories().get(i).getSubCatDetails();
                PrintSubCategory.SubCatDetail subCatDetail;
                for (int j = 0; j < subCategoryDetails.size(); j++) {
                    subCatDetail = subCategoryDetails.get(j);
                    int index = Integer.parseInt(subCatDetail.getIndexOfModule());
                    if (index < pages.length) {
                        if (subCatDetail.isCatSelected()) {
                            pages[index] = subCatDetail.getValueOfModule();
                            if (!pages[index].equals("0")) {
                                isPageSelected = true;
                            }
                        }
                    }
                }
            }
            if (allDataList.isSelectedAll()) {
                pages[20] = "17";
            }
            String url = CGlobalVariables.PDF_BASE_URL + "HindipdfNew.aspx?TypePdf=";
            for (int i = 0; i < pages.length; i++) {
                if (i < pages.length - 1) {
                    url = url + pages[i].trim() + ",";
                } else {
                    url = url + pages[i].trim();
                }

            }
            /*boolean IS_NORTH_CHART = false;

            if (chart_Style == 0 || chart_Style == 2)
                IS_NORTH_CHART = true;*/

            //sharePDF(false);
            url = getPdfUrl(ActPrintKundliCategory.this, chart_Style, LANGUAGE_CODE, url) + "&time=" + (new Date().getTime());
            if (CUtils.isConnectedWithInternet(this)) {
                if (isPageSelected) {
                    downloadPDF(url);
                    isPageSelected = false;
                } else {
                    if (isPdfShare) {
                        Toast.makeText(this, getResources().getString(R.string.please_select_at_laeast_page_share), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.please_select_at_laeast_page), Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                hideProgressBar();
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, regularTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            }
        }
    }

    public String getPdfUrl(Context context, int chartStyle, int languageCode, String url) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
          /*  if (iS_NORTH_CHART) {
                sb.append("&ChartType=" + 1);
            } else {
                sb.append("&ChartType=" + 2);
            }*/
            sb.append("&ChartType=" + (chartStyle + 1)); // 1 for north, 2 for south, 3 for east
            sb.append("&DashaBhog=" + "");

            String encodedUserName = URLEncoder.encode(CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getName().trim(), "UTF-8");

            sb.append("&UserName="
                    + encodedUserName);
            sb.append("&UserPlace="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getCityName().trim());

            float timeZoneWithDST = CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getTimeZoneValue() + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDST();

            sb.append("&UserTimeZone=" + timeZoneWithDST);
        /*sb.append("&UserTimeZone="
                + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                .getPlace().getTimeZoneValue());*/
            sb.append("&UserSex="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getGender().trim());
            sb.append("&UserSecondOfBirth="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getSecond());
            sb.append("&UserMinuteOfBirth="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getMin());
            sb.append("&UserHourOfBirth="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getHour());
            sb.append("&UserDayOfBirth="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getDay());
            sb.append("&UserMonthOfBirth="
                    + (CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getMonth() + 1));
            sb.append("&UserYearOfBirth="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getYear());
            sb.append("&UserDegreeOfLattitude="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getLatDeg().trim());
            sb.append("&UserDegreeOfLongitude="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getLongDeg().trim());
            sb.append("&UserSecondOfLattitude="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getLatSec().trim());
            sb.append("&UserSecondOfLongitude="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getLongSec().trim());
            sb.append("&UserMinuteOfLongitude="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getLongMin().trim());
            sb.append("&UserMinuteOfLattitude="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getLatMin().trim());
            sb.append("&UserEastWest="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getLongDir().trim());
            sb.append("&UserNorthSouth="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getPlace().getLatDir().trim());
            sb.append("&UserDST="
                    + 0);
            sb.append("&UserAyanamsaType="
                    + CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject().getAyanIndex());
            sb.append("&userChartId="
                    + CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject().getOnlineChartId());
            // END
            sb.append("&UserKPHorarySeed="
                    + CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getHoraryNumber());
            sb.append("&LanguageCode=" + languageCode);

            try {
                sb.append("&" + CGlobalVariables.KEY_USER_ID + "=" + CUtils.replaceEmailChar(CUtils.getUserName(context)));
                sb.append("&" + CGlobalVariables.KEY_PLAN_ID + "=" + String.valueOf(CUtils.getUserPurchasedPlanFromPreference(context)));
                sb.append("&" + CGlobalVariables.VARSHFAL_START_DATE + "=" + startYear);
                sb.append("&" + CGlobalVariables.VARSHFAL_YEAR_NO + "=" + numberOfYear);

                //added by Ankit on 13-04-2020 to notify server for app version
                sb.append("&AppVersion=" + BuildConfig.VERSION_CODE);
                //added by Ankit on 28-07-2020 to notify server for app package
                sb.append("&AppPackage=" + BuildConfig.APPLICATION_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void downloadPDF(String url) {
        showProgressBar();
        Intent intent = new Intent(this, DownloadPdfService.class);
        intent.putExtra(KEY_PDF_SHARE, isPdfShare);
        intent.putExtra(KEY_CHART_STYLE, chart_Style);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_RECEIVER, new DownloadPdfReceiver(ActPrintKundliCategory.this, new Handler()));
        startService(intent);
    }


    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(ActPrintKundliCategory.this, regularTypeface);
        }
        pd.setCancelable(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE:
                /*if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        printPDF();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                    }
                }*/
                printPDF();
                break;
            default:
                break;

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == PERMISSION_EXTERNAL_STORAGE) {
                boolean isPermissionGranted = false;
                if(grantResults.length == 1){
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE) {
                        isPermissionGranted = true;
                    }
                }else if(grantResults.length == 2){
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE){
                        isPermissionGranted = true;
                    }
                }

                if (isPermissionGranted) {
                    printPDF();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Check and load cache data
     */
    private void checkCachedData() {
        boolean isShowProgressbar = true;
        Cache cache = VolleySingleton.getInstance(ActPrintKundliCategory.this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1-" + url);
        // cache data
        try {
            if (entry != null) {
                String saveData = new String(entry.data, "UTF-8");
                if (!TextUtils.isEmpty(saveData)) {
                    allDataList = parseData(saveData);
                    if (allDataList != null) {
                        setData();
                        isShowProgressbar = false;
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Cached response doesn't exists. Make network call here
        if (!CUtils.isConnectedWithInternet(ActPrintKundliCategory.this)) {
            MyCustomToast mct = new MyCustomToast(ActPrintKundliCategory.this, ActPrintKundliCategory.this
                    .getLayoutInflater(), ActPrintKundliCategory.this, regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            //getLearnAstrologyData(url, isShowProgressbar);
            getCategory(isShowProgressbar);
        }
    }

    private ArrayList<PrintModuleData> getBasicModuleData() {
        ArrayList<PrintModuleData> basicModuleList = null;
        if (allDataList != null) {
            basicModuleList = new ArrayList<>();
            PrintModuleData basicModuleItem1 = new PrintModuleData(0, 0);
            PrintModuleData basicModuleItem2 = new PrintModuleData(0, 1);
            PrintModuleData basicModuleItem3 = new PrintModuleData(0, 2);
            PrintModuleData basicModuleItem4 = new PrintModuleData(0, 3);
            PrintModuleData basicModuleItem5 = new PrintModuleData(0, 4);
            PrintModuleData basicModuleItem6 = new PrintModuleData(0, 6);
            basicModuleList.add(basicModuleItem1);
            basicModuleList.add(basicModuleItem2);
            basicModuleList.add(basicModuleItem3);
            basicModuleList.add(basicModuleItem4);
            basicModuleList.add(basicModuleItem5);
            basicModuleList.add(basicModuleItem6);
        }

        return basicModuleList;
    }

    private ArrayList<PrintModuleData> getEasyModuleData() {
        ArrayList<PrintModuleData> easyModuleList = null;
        if (allDataList != null) {
            easyModuleList = new ArrayList<>();
            PrintModuleData easyModuleItem1 = new PrintModuleData(1, 0);
            PrintModuleData easyModuleItem2 = new PrintModuleData(1, 1);
            PrintModuleData easyModuleItem3 = new PrintModuleData(1, 2);
            PrintModuleData easyModuleItem4 = new PrintModuleData(1, 3);
            PrintModuleData easyModuleItem5 = new PrintModuleData(1, 7);
            easyModuleList.addAll(getBasicModuleData());
            easyModuleList.add(easyModuleItem1);
            easyModuleList.add(easyModuleItem2);
            easyModuleList.add(easyModuleItem3);
            easyModuleList.add(easyModuleItem4);
            easyModuleList.add(easyModuleItem5);
        }

        return easyModuleList;
    }

    private ArrayList<PrintModuleData> getVarshfalModuleData() {
        ArrayList<PrintModuleData> varshfalModuleList = null;
        if (allDataList != null) {
            varshfalModuleList = new ArrayList<>();
            PrintModuleData varshfalModuleItem = new PrintModuleData(4, 0);
            varshfalModuleList.addAll(getBasicModuleData());
            varshfalModuleList.add(varshfalModuleItem);
        }

        return varshfalModuleList;
    }

    private ArrayList<PrintModuleData> getKpModuleData() {
        ArrayList<PrintModuleData> kpModuleList = null;
        if (allDataList != null) {
            kpModuleList = new ArrayList<>();

            PrintModuleData kpModuleItem1 = new PrintModuleData(7, 0);
            PrintModuleData kpModuleItem2 = new PrintModuleData(7, 1);
            PrintModuleData kpModuleItem3 = new PrintModuleData(7, 2);
            PrintModuleData kpModuleItem4 = new PrintModuleData(7, 3);
            PrintModuleData kpModuleItem5 = new PrintModuleData(7, 4);
            PrintModuleData kpModuleItem6 = new PrintModuleData(7, 5);
            PrintModuleData kpModuleItem7 = new PrintModuleData(7, 6);
            PrintModuleData kpModuleItem8 = new PrintModuleData(7, 7);

            kpModuleList.addAll(getBasicModuleData());
            kpModuleList.add(kpModuleItem1);
            kpModuleList.add(kpModuleItem2);
            kpModuleList.add(kpModuleItem3);
            kpModuleList.add(kpModuleItem4);
            kpModuleList.add(kpModuleItem5);
            kpModuleList.add(kpModuleItem6);
            kpModuleList.add(kpModuleItem7);
            kpModuleList.add(kpModuleItem8);
        }
        return kpModuleList;
    }

    private ArrayList<PrintModuleData> getLalKitabModuleData() {
        ArrayList<PrintModuleData> lalKitabModuleList = null;
        if (allDataList != null) {
            lalKitabModuleList = new ArrayList<>();

            PrintModuleData lalKitabModuleItem1 = new PrintModuleData(5, 0);
            PrintModuleData lalKitabModuleItem2 = new PrintModuleData(5, 1);
            PrintModuleData lalKitabModuleItem3 = new PrintModuleData(5, 2);
            PrintModuleData lalKitabModuleItem4 = new PrintModuleData(5, 3);
            PrintModuleData lalKitabModuleItem5 = new PrintModuleData(5, 4);

            lalKitabModuleList.add(lalKitabModuleItem1);
            lalKitabModuleList.add(lalKitabModuleItem2);
            lalKitabModuleList.add(lalKitabModuleItem3);
            lalKitabModuleList.add(lalKitabModuleItem4);
            lalKitabModuleList.add(lalKitabModuleItem5);
        }
        return lalKitabModuleList;
    }

    private ArrayList<PrintModuleData> getJaminiModuleData() {
        ArrayList<PrintModuleData> jaminiModuleList = null;
        if (allDataList != null) {
            jaminiModuleList = new ArrayList<>();
            PrintModuleData jaminiModuleItem1 = new PrintModuleData(8, 0);
            PrintModuleData jaminiModuleItem2 = new PrintModuleData(8, 1);
            PrintModuleData jaminiModuleItem3 = new PrintModuleData(8, 2);
            jaminiModuleList.addAll(getBasicModuleData());
            jaminiModuleList.add(jaminiModuleItem1);
            jaminiModuleList.add(jaminiModuleItem2);
            jaminiModuleList.add(jaminiModuleItem3);
        }
        return jaminiModuleList;
    }

    private ArrayList<PrintModuleData> getSpecialModuleData() {
        ArrayList<PrintModuleData> specialModuleList = null;
        if (allDataList != null) {
            specialModuleList = new ArrayList<>();
            PrintModuleData specialModuleItem1 = new PrintModuleData(0, 5);
            PrintModuleData specialModuleItem2 = new PrintModuleData(0, 7);
            PrintModuleData specialModuleItem3 = new PrintModuleData(1, 4);
            PrintModuleData specialModuleItem4 = new PrintModuleData(1, 5);
            PrintModuleData specialModuleItem5 = new PrintModuleData(1, 6);
            PrintModuleData specialModuleItem6 = new PrintModuleData(1, 8);
            PrintModuleData specialModuleItem7 = new PrintModuleData(1, 9);


            specialModuleList.addAll(getBasicModuleData());
            specialModuleList.addAll(getEasyModuleData());
            specialModuleList.addAll(getVarshfalModuleData());
            specialModuleList.addAll(getKpModuleData());
            specialModuleList.addAll(getLalKitabModuleData());
            specialModuleList.addAll(getJaminiModuleData());
            specialModuleList.add(specialModuleItem1);
            specialModuleList.add(specialModuleItem2);
            specialModuleList.add(specialModuleItem3);
            specialModuleList.add(specialModuleItem4);
            specialModuleList.add(specialModuleItem5);
            specialModuleList.add(specialModuleItem6);
            specialModuleList.add(specialModuleItem7);
        }
        return specialModuleList;
    }

   /* private ArrayList<PrintModuleData> getDhruvModuleData() {
        ArrayList<PrintModuleData> dhruvModuleList = null;
        if (allDataList != null) {
            dhruvModuleList = new ArrayList<>();
            PrintModuleData dhruvModuleItem1 = new PrintModuleData(2, 0);
            PrintModuleData dhruvModuleItem2 = new PrintModuleData(2, 1);
            PrintModuleData dhruvModuleItem3 = new PrintModuleData(2, 2);
            PrintModuleData dhruvModuleItem4 = new PrintModuleData(2, 3);
            PrintModuleData dhruvModuleItem5 = new PrintModuleData(3, 0);
            PrintModuleData dhruvModuleItem6 = new PrintModuleData(3, 1);
            PrintModuleData dhruvModuleItem7 = new PrintModuleData(3, 2);
            PrintModuleData dhruvModuleItem8 = new PrintModuleData(3, 3);
            PrintModuleData dhruvModuleItem9 = new PrintModuleData(6, 0);
            PrintModuleData dhruvModuleItem10 = new PrintModuleData(6, 1);
            PrintModuleData dhruvModuleItem11 = new PrintModuleData(6, 2);
            PrintModuleData dhruvModuleItem12 = new PrintModuleData(6, 3);
            PrintModuleData dhruvModuleItem13 = new PrintModuleData(6, 4);
            PrintModuleData dhruvModuleItem14 = new PrintModuleData(6, 5);
            PrintModuleData dhruvModuleItem15 = new PrintModuleData(1, 10);
            PrintModuleData dhruvModuleItem16 = new PrintModuleData(0, 8);
            PrintModuleData dhruvModuleItem17 = new PrintModuleData(0, 9);


            dhruvModuleList.addAll(getSpecialModuleData());
            dhruvModuleList.add(dhruvModuleItem1);
            dhruvModuleList.add(dhruvModuleItem2);
            dhruvModuleList.add(dhruvModuleItem3);
            dhruvModuleList.add(dhruvModuleItem4);
            dhruvModuleList.add(dhruvModuleItem5);
            dhruvModuleList.add(dhruvModuleItem6);
            dhruvModuleList.add(dhruvModuleItem7);
            dhruvModuleList.add(dhruvModuleItem8);
            dhruvModuleList.add(dhruvModuleItem9);
            dhruvModuleList.add(dhruvModuleItem10);
            dhruvModuleList.add(dhruvModuleItem11);
            dhruvModuleList.add(dhruvModuleItem12);
            dhruvModuleList.add(dhruvModuleItem13);
            dhruvModuleList.add(dhruvModuleItem14);
            dhruvModuleList.add(dhruvModuleItem15);
            dhruvModuleList.add(dhruvModuleItem16);
            dhruvModuleList.add(dhruvModuleItem17);
        }
        return dhruvModuleList;
    }*/
}
