package com.ojassoft.astrosage.varta.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.adapters.NotificationAdapter;
import com.ojassoft.astrosage.varta.dao.NotificationDBManager;
import com.ojassoft.astrosage.varta.interfacefile.NotificationCenterCallback;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.NotificationModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_NOTIFICATION_COUNT;

public class NotificationCenterActivity extends BaseActivity implements View.OnClickListener {
    private static NotificationCenterCallback actAppModuleCtx;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private Activity currentActivity;
    private ImageView ivBack;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextView noNotificationTV;
    private RecyclerView recyclerView;
    private NotificationDBManager dbManager;
    private List<NotificationModel> notificationModels;
    private NotificationAdapter notificationAdapter;
    private String playUrl = "https://play.google.com";
    private LinearLayoutManager mLayoutManager;
    private boolean loading = true;

    public static void setNotificationCenterCallback(NotificationCenterCallback context) {
        NotificationCenterActivity.actAppModuleCtx = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);
        initContext();
        initViews();
        initListners();
    }

    protected void initViews() {
        CUtils.saveIntData(this, KEY_NOTIFICATION_COUNT, 0);
        mToolbar = findViewById(R.id.tool_barAppModule);
        mToolbarTitle = mToolbar.findViewById(R.id.tvTitle);
        ivBack = mToolbar.findViewById(R.id.ivBack);
        TextView notificationHeadingTV = findViewById(R.id.font_auto_activity_notification_center_1);
        noNotificationTV = findViewById(R.id.noNotificationTV);
        recyclerView = findViewById(R.id.notification_list);

        dbManager = new NotificationDBManager(currentActivity);
        notificationModels = dbManager.getNotificationList(0, 10);
        if (notificationModels == null) {
            notificationModels = new ArrayList<>();
        }
        if (notificationModels.isEmpty()) {
            noNotificationTV.setVisibility(View.VISIBLE);
        } else {
            noNotificationTV.setVisibility(View.GONE);
        }
        notificationAdapter = new NotificationAdapter(currentActivity, notificationModels);
        mLayoutManager = new LinearLayoutManager(currentActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notificationAdapter);

        FontUtils.changeFont(currentActivity, mToolbarTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(currentActivity, notificationHeadingTV, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
    }

    protected void initContext() {
        currentActivity = NotificationCenterActivity.this;
    }

    protected void initListners() {
        ivBack.setOnClickListener(this);

        notificationAdapter.setOnClickListner(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                if (notificationModels != null && notificationModels.size() > position) {
                    NotificationModel notificationModel = notificationModels.get(position);
                    redirectToActivity(notificationModel);
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            notificationModels.addAll(dbManager.getNotificationList(notificationModels.size(), 10));
                            notificationAdapter.notifyDataSetChanged();
                            //Toast.makeText(ActNotificationCenter.this, "loading", Toast.LENGTH_LONG).show();
                            loading = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: {
                finish();
                break;
            }
        }
    }

    // deep linking
    private void redirectToActivity(NotificationModel notificationModel) {
        if (notificationModel == null) {
            return;
        }
        try {
            String link = notificationModel.getLink();
            String title = notificationModel.getTitle();
            if (TextUtils.isEmpty(link) || TextUtils.isEmpty(title)) {
                goToDashBoard();
                return;
            }
            if (title.equals(getResources().getString(R.string.call_completed))) {
                Uri linkData = Uri.parse(link);
                if (linkData != null) {
                    String articleIdLastPathSegment = linkData.getLastPathSegment();
                    Intent intent1 = new Intent(NotificationCenterActivity.this, AstrologerDescriptionActivity.class);
                    intent1.putExtra("phoneNumber", CUtils.getUserID(NotificationCenterActivity.this));
                    intent1.putExtra("urlText", articleIdLastPathSegment);
                    intent1.putExtra("msg", notificationModel.getMessage());
                    intent1.putExtra("title", title);
                    intent1.putExtra("from_notification_center", true);
                    startActivity(intent1);
                    finish();
                }
            } else {
                Uri linkData = Uri.parse(link);
                String articleIdLastPathSegment = linkData.getLastPathSegment();
                if (articleIdLastPathSegment == null) articleIdLastPathSegment = "";
                String articleId = linkData.toString();
                if (articleId.contains(CGlobalVariables.varta_astrosage_urls) || articleId.contains(CGlobalVariables.varta_astrosage_url)) {
                    boolean isLogin = CUtils.getUserLoginStatus(NotificationCenterActivity.this);
                    if (articleIdLastPathSegment.equals(CGlobalVariables.consultationHistory) && isLogin) {
                        openWalletScreen();
                    } else {
                        goToDashBoard();
                    }

                } else {
                    CUtils.openWebBrowser(this, linkData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            goToDashBoard();
        }
    }

    private void goToDashBoard() {
        Intent intent = new Intent(NotificationCenterActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    public void openWalletScreen() {
        Intent intent = new Intent(NotificationCenterActivity.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        startActivity(intent);
    }
}
