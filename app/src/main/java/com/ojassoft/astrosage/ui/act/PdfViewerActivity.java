package com.ojassoft.astrosage.ui.act;


import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.model.BigHorscopeProductModel;
import com.ojassoft.astrosage.model.PdfFileData;
import com.ojassoft.astrosage.ui.SuperBaseActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.adapters.PdfPageAdapter;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PdfViewerActivity extends SuperBaseActivity {


    //private LinearLayout pdfContainer;
    private Button btnDownloadList;
    private PdfRenderer pdfRenderer;
    private ParcelFileDescriptor parcelFileDescriptor;
    private String pdfPath;
    private static final String PREF_NAME = "PdfDownloads";
    private static final String KEY_PDF_LIST = "downloaded_pdfs";
    LinearLayout navView;
    ImageView  imgViewShare, imgViewWhatsapp;
    private RecyclerView pdfRecyclerView;
    View includedLayout, homeBottomNav;
    boolean isFromMatchMaking = false;
    boolean isFromReceiver = false;
    String reportType = "";
    String topTitle = "";
    String localChartId_ = "";
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        if (getIntent() != null) {
            pdfPath = getIntent().getStringExtra(CGlobalVariables.PDF_FILE);
            if (getIntent().getExtras() != null) {
                isFromMatchMaking = getIntent().getExtras().getBoolean(CGlobalVariables.IS_FROM_MATCH_MAKING);
                topTitle = getIntent().getExtras().getString(CGlobalVariables.PDF_KUNDLI_TOP_TITLE);
                localChartId_ = getIntent().getExtras().getString("local_chart_id");
                isFromReceiver = getIntent().getExtras().getBoolean("is_from_Receiver");
                reportType = getIntent().getExtras().getString("report_type");
            }
        }
        Log.d("pdfPath", pdfPath);
        //Log.d("local_chart_id",localChartId_);
        navView = findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        //pdfContainer = findViewById(R.id.pdfContainer);
        LANGUAGE_CODE = ((AstrosageKundliApplication) this.getApplication())
                .getLanguageCode();

        includedLayout = findViewById(R.id.appbarAppModule);
        LinearLayout linLayoutShare = includedLayout.findViewById(R.id.linLayoutShare);
        linLayoutShare.setVisibility(View.VISIBLE);
        homeBottomNav = findViewById(R.id.homeBottomNav);
        pdfRecyclerView = findViewById(R.id.pdfRecyclerView);
        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView ivBack = includedLayout.findViewById(R.id.ivBack);
        TextView tvTitle = includedLayout.findViewById(R.id.tvTitle);
        ImageView saveButton = includedLayout.findViewById(R.id.imgViewDownload);
        //tvTitle.setText(AstrosageKundliApplication.userKundliName+" "+getString(R.string.kundli));
        tvTitle.setText(topTitle + " " + getString(R.string.kundli));

       /* if (isFromMatchMaking){
            tvTitle.setText(CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getName().trim()+" "+getString(R.string.kundli));
        }else {
            tvTitle.setText(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().getName().trim()+"&"+CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail().getName().trim()+" "+getString(R.string.kundli));
        }*/

        imgViewWhatsapp = includedLayout.findViewById(R.id.imgViewWhatsapp);
        imgViewShare = includedLayout.findViewById(R.id.imgViewShare);

        File file = new File(pdfPath);
        if (!file.exists() || file.length() < 1024 || !file.canRead()) {
            Toast.makeText(this, getString(R.string.file_deleted_from_storage_download_again), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        saveButton.setVisibility(View.VISIBLE);
        openPdf(pdfPath);
        PdfPageAdapter adapter = new PdfPageAdapter(pdfRenderer, this);
        pdfRecyclerView.setAdapter(adapter);

        imgViewShare.setOnClickListener(v -> sharePdfDialog());
        imgViewWhatsapp.setOnClickListener(v -> {
            shareOnWhatsApp();
        });
       /* pdfContainer.setOnClickListener(view -> {
            if (includedLayout.getVisibility() == View.VISIBLE && homeBottomNav.getVisibility() == View.VISIBLE){
                includedLayout.setVisibility(View.GONE);
                homeBottomNav.setVisibility(View.GONE);
            }else {
                includedLayout.setVisibility(View.VISIBLE);
                homeBottomNav.setVisibility(View.VISIBLE);
            }
        });*/
        ivBack.setOnClickListener(v -> onBackPressed());
        saveButton.setOnClickListener(v -> {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("save_pdf_btn_click_event", AstrosageKundliApplication.currentEventType, "");

            BeanHoroPersonalInfo info = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
            String kundliName = "";
            long localChartId = 0;
            if (isFromReceiver) {
                if (isFromMatchMaking) {
                    kundliName = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().getName().trim() + " & " + CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail().getName().trim();
                    long matchMakingChartId = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().getLocalChartId() + CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail().getLocalChartId();
                    localChartId = matchMakingChartId;

                } else {
                    kundliName = (info.getName() != null) ? info.getName().trim() : "";
                    localChartId = (info != null) ? info.getLocalChartId() : 0L;
                }
            } else {
                if (!Objects.equals(localChartId_, "")) {
                    localChartId = Long.parseLong(localChartId_);
                }
            }

            String address = (info.getPlace() != null && info.getPlace().getCityName() != null)
                    ? info.getPlace().getCityName().trim() : "";
            String time = (info.getDateTime() != null && info.getDateTime().getTimeOfBirth() != null)
                    ? info.getDateTime().getTimeOfBirth().trim() : "";
            String date = (info.getDateTime() != null && info.getDateTime().getDateOfBirth() != null)
                    ? info.getDateTime().getFormattedDateOfBirth().trim() : "";
            String gender = (info.getGender() != null) ? info.getGender().trim() : "";

            saveFileData(this, kundliName, address, time, date, gender, localChartId);

        });

/*
        pdfRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 10) {
                    // Scrolling down → Hide Toolbar
                    hideToolbar();
                } else if (dy < -10) {
                    // Scrolling up → Show Toolbar
                    showToolbar();
                }
            }
        });
*/


    }

    public void wrongPdfFile() {
        finish();
    }

    boolean isAlreadySaved = false;

    private void saveFileData(Context context, String kundliName, String address, String time, String date, String gender, long localChartId) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPdfPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = prefs.getString("file_list", null);

        Type type = new TypeToken<ArrayList<PdfFileData>>() {
        }.getType();
        ArrayList<PdfFileData> fileList;

        // Load existing list or create a new one
        if (json == null) {
            fileList = new ArrayList<>();
        } else {
            fileList = gson.fromJson(json, type);
        }

        //Remove existing item if it already exists
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!fileList.isEmpty()) {
                for (int i = 0; i < fileList.size(); i++) {
                    //Log.d("pdf_view", String.valueOf(fileList.get(i).getGetLocaleChartId())+"=="+localChartId+"=="+fileList.get(i).getReportType()+"=="+reportType);
                    if (fileList.get(i).getGetLocaleChartId() != 0 && fileList.get(i).getGetLocaleChartId() == localChartId) {
                        if (TextUtils.equals(fileList.get(i).getReportType(), reportType)) {
                            isAlreadySaved = true;
                            Toast.makeText(this, getString(R.string.already_saved), Toast.LENGTH_SHORT).show();
                        }
                        //Log.d("local_chart_id1", String.valueOf(localChartId));
                    }
                }
            }
            //fileList.removeIf(file -> file.getGetLocaleChartId() ==localChartId);
        }
        if (!isAlreadySaved) {
            pdfPath = moveFileToAppStorage(pdfPath);
            // Create new file data object
            PdfFileData newFile = new PdfFileData(pdfPath, kundliName, address, date, time, gender, localChartId, isFromMatchMaking, CUtils.getUserPurchasedPlanFromPreference(context), reportType);

            // Add new item to list
            fileList.add(newFile);

            // Sort list by last modified (latest first)
            // Collections.sort(fileList, (a, b) -> Long.compare(b.getLastModified(), a.getLastModified()));

            // Save updated list to SharedPreferences
            editor.putString("file_list", gson.toJson(fileList));
            editor.apply();

            Toast.makeText(this, getString(R.string.kundli_saved), Toast.LENGTH_SHORT).show();

            loadBigHorscopeData();
        }

    }


    public void hideAndShow() {
        if (includedLayout.getVisibility() == View.VISIBLE) {
            includedLayout.setVisibility(View.GONE);
            //  homeBottomNav.setVisibility(View.GONE);
        } else {
            includedLayout.setVisibility(View.VISIBLE);
            //  homeBottomNav.setVisibility(View.VISIBLE);
        }
    }

    private void shareOnWhatsApp() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("share_pdf_on_whatsapp_btn_click_event", AstrosageKundliApplication.currentEventType, "");

        if (pdfPath == null) {
            Toast.makeText(this, "Select a PDF first!", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(pdfPath);
        if (!file.exists()) {
            Toast.makeText(this, "File not found. Download again!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent share = new Intent();
        Uri uri = Uri.fromFile(file);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, "com.ojassoft.astrosage", file);
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setPackage("com.whatsapp");


        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not installed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sharePdfDialog() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("share_pdf_on_share_btn_click_event", AstrosageKundliApplication.currentEventType, "");

        if (pdfPath == null) {
            Toast.makeText(this, "Select a PDF first!", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(pdfPath);
        if (!file.exists()) {
            Toast.makeText(this, "File not found. Download again!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent share = new Intent();
        Uri uri = Uri.fromFile(file);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, "com.ojassoft.astrosage", file);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share PDF via"));
    }

    public void customBottomNavigationFont(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    customBottomNavigationFont(child);
                }
            } else if (v instanceof TextView) {
                FontUtils.changeFont(PdfViewerActivity.this, ((TextView) v), com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                v.setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    private void setBottomNavigationText() {

        // find MenuItem you want to change
        TextView navLive = navView.findViewById(R.id.txtViewLive);
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);

        boolean isAIChatDisplayed = CUtils.isAIChatDisplayed(this);
        if (isAIChatDisplayed) {
            navChatTxt.setText(getResources().getString(R.string.text_ask));
            navCallTxt.setText(getResources().getString(R.string.ai_astrologer));
            Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_unselected).into(navCallImg);
        } else {
            navChatTxt.setText(getResources().getString(R.string.chat_now));
            navCallTxt.setText(getResources().getString(R.string.call));
            navCallImg.setImageResource(R.drawable.nav_call_icons);
        }

        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(PdfViewerActivity.this)) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
        } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);
        }
        // setting click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
        //navView.getMenu().setGroupCheckable(0,false,true);
    }

    private void saveFileToAppDirectory(String originalPath) {
        File originalFile = new File(originalPath);
        if (!originalFile.exists()) {
            Log.e("PDF", "File does not exist");
            return;
        }

        File appDir = new File(getFilesDir(), "pdfs");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        File newFile = new File(appDir, originalFile.getName());

        try (InputStream in = new FileInputStream(originalFile);
             OutputStream out = new FileOutputStream(newFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            Log.d("PDF", "File saved: " + newFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String moveFileToAppStorage(String cacheFilePath) {
        File cacheFile = new File(cacheFilePath); // File in cache directory
        if (!cacheFile.exists()) {
            return "Cache file not found: " + cacheFilePath;
        }

        // Extract file name from cache file path
        String fileName = cacheFile.getName();

        // Destination in app-specific internal storage
        File appSpecificFile = new File(getFilesDir(), fileName);

        try (FileInputStream fis = new FileInputStream(cacheFile);
             FileOutputStream fos = new FileOutputStream(appSpecificFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            // Delete original cache file after copying
            boolean deleted = cacheFile.delete();
            appSpecificFile.getAbsolutePath();
            pdfPath = appSpecificFile.getAbsolutePath();
            return appSpecificFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error moving file: " + e.getMessage();
        }
    }

    private void openPdf(String path) {

        try {
            File file = new File(path);
            parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
            //renderAllPages();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Cannot open PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (pdfRenderer != null) {
                pdfRenderer.close();
            }
            if (parcelFileDescriptor != null) {
                parcelFileDescriptor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showBrihatNotification(String price) {
        try {
            Intent resultIntent = new Intent(this, BrihatKundliActivity.class);
            resultIntent.putExtra("partnerId", CGlobalVariables.BRIHAT_FROM_KUNDLI_DOWNLOADED_NOTIFICATION_PARTNER_ID);

            int notificationId = LibCUtils.getRandomNumber();
            PendingIntent pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);

            String CHANNEL_ID = "Get Brihat Notification";

            IconCompat icon = IconCompat.createWithResource(this, com.libojassoft.android.R.drawable.ic_notification);

            Person person = new Person.Builder()
                    .setName(getString(R.string.pdf_downloaded_successfully))
                    .setIcon(icon)
                    .build();

            NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(person)
                    .setConversationTitle(getString(R.string.kundli_saved));
            if(price != null) {
                messagingStyle.addMessage(getString(R.string.get_brihat_kundli_now_text,price), System.currentTimeMillis() - 10 * 60 * 1000, person);
            }
            NotificationCompat.Builder notificationBuilder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("")
                        .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setStyle(messagingStyle)
                        .setContentIntent(pending)
                        .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(true);
            }

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "getbrihat", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }

            // using the same tag and Id causes the new notification to replace an existing one
            if (CUtils.isDisplayNotification(this)) {
                mNotificationManager.notify(String.valueOf(System.currentTimeMillis()), notificationId, notificationBuilder.build());
            }
        } catch (Exception e) {
            //
        }
    }


    /**
     * Loads BrihatKundli details from the server for collection edition.
     */
    private void loadBigHorscopeData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.brihatHorscopeWebURl,
                response -> {
                    if (response != null && !response.isEmpty()) {
                        try {
                            String str = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                            JSONArray jsonArray = new JSONArray(str);

                            if(jsonArray.length() > 0){
                                String priceInRS = jsonArray.getJSONObject(0).getString("PriceInRS");
                                showBrihatNotification(priceInRS);
                            }else{
                                showBrihatNotification(null);
                            }
                            //Log.d("Brihad kundli", "loadBigHorscopeData: " + jsonArray);
                        } catch (Exception e) {
                            showBrihatNotification(null);
                        }
                    }
                }, error -> {
            //Log.e(TAG, "Volley error: ", error); // Log error for debugging

            if (error instanceof TimeoutError) {
                VolleyLog.d("TimeoutError: " + error.getMessage());
            } else if (error instanceof NoConnectionError) {
                VolleyLog.d("NoConnectionError: " + error.getMessage());
            } else if (error instanceof AuthFailureError) {
                VolleyLog.d("AuthFailureError: " + error.getMessage());
            } else if (error instanceof ServerError) {
                VolleyLog.d("ServerError: " + error.getMessage());
            } else if (error instanceof NetworkError) {
                VolleyLog.d("NetworkError: " + error.getMessage());
            } else if (error instanceof ParseError) {
                VolleyLog.d("ParseError: " + error.getMessage());
            }
            showBrihatNotification(null);
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(PdfViewerActivity.this));
                headers.put("languagecode", "" + LANGUAGE_CODE);
                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(PdfViewerActivity.this)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(PdfViewerActivity.this)));
                return headers;
            }

        };
        int socketTimeout = 30000; // 30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        VolleySingleton.getInstance(PdfViewerActivity.this).getRequestQueue().add(stringRequest);
    }


}
