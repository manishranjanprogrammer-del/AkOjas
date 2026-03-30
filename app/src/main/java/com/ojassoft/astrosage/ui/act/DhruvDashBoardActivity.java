package com.ojassoft.astrosage.ui.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.DirectoryHomeBean;
import com.ojassoft.astrosage.customadapters.DhruvDashboardAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DhruvDashBoardActivity extends BaseInputActivity implements VolleyResponse {
    RecyclerView recyclerView;
    DhruvDashboardAdapter dhruvDashboardAdapter;
    ImageView toggleIV;
    TabLayout tabLayout;
    TextView tvTitle,txtViewRemainingCount;
    public HomeNavigationDrawerFragment drawerFragment;
    private Toolbar tool_barAppModule;
    private final int GET_DIRECTORY_HOME = 4;
    private CustomProgressDialog pd;
    private DirectoryHomeBean mDirectoryHomeBean;

    public DhruvDashBoardActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dhruv_dash_board_act_layout);
        recyclerView = findViewById(R.id.recyclerview);
        toggleIV = findViewById(R.id.ivToggleImage);
        tabLayout = findViewById(R.id.tabs);
        tvTitle = findViewById(R.id.tvTitle);
        txtViewRemainingCount = findViewById(R.id.txtViewRemainingCount);

        // Fetch the remaining report count from the server and update the UI accordingly
        CUtils.getRemainingReportCountFromServer(this, new CUtils.PdfCountResponseListener() {
            @Override
            public void onPdfCountReceived(int count) {
                // Show the count TextView when the count is received
                txtViewRemainingCount.setVisibility(View.VISIBLE);
                // Set the text to display the number of premium horoscopes left to download
                txtViewRemainingCount.setText(getString(R.string.premium_horoscopes_left_to_download,String.valueOf(count)));
            }

            @Override
            public void onPdfCountError() {
                // Hide the count TextView if there is an error fetching the count
                txtViewRemainingCount.setVisibility(View.GONE);
            }
        });
        tabLayout.setVisibility(View.GONE);
        toggleIV.setVisibility(View.VISIBLE);
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        dhruvDashboardAdapter = new DhruvDashboardAdapter(this, getString(), getIcon(), getBgIcon());
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(dhruvDashboardAdapter);
        setUpDrawer();
        enableToolBar();
        tvTitle.setText(R.string.dhruv_dashboard);
    }

    private ArrayList<String> getString() {

        ArrayList<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.new_appointment));
        list.add(getResources().getString(R.string.appointment_today));
        list.add(getResources().getString(R.string.customize_footer_details));
        list.add(getResources().getString(R.string.title_join_varta));
        list.add(getResources().getString(R.string.affliated_program));
        list.add(getResources().getString(R.string.topup_recharge));
        //list.add(getResources().getString(R.string.directory_listing));

        return list;
    }

    private ArrayList<Integer> getIcon() {
        ArrayList<Integer> iconList = new ArrayList<>();
        iconList.add(R.drawable.new_appointment);
        iconList.add(R.drawable.ic_my_appointment);
        iconList.add(R.drawable.cust_footer);
        iconList.add(R.drawable.icon_varta);
        iconList.add(R.drawable.affliate_program);
        iconList.add(R.drawable.top_up_icon);
        //iconList.add(R.drawable.top_up_icon);
        return iconList;


    }

    private ArrayList<Integer> getBgIcon() {
        ArrayList<Integer> iconList = new ArrayList<>();
        iconList.add(R.drawable.bg_dotted_blue);
        iconList.add(R.drawable.bg_dotted_purple);
        iconList.add(R.drawable.bg_dotted_orange);
        iconList.add(R.drawable.bg_dotted_purple);
        iconList.add(R.drawable.bg_dotted_blue);
        iconList.add(R.drawable.bg_dotted_orange);
        iconList.add(R.drawable.bg_dotted_blue);
        return iconList;


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            drawerFragment.updateLayout(getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        } catch (Exception ex) {
        }
    }

    private void setUpDrawer() {
        drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.myDrawerFrag);
        drawerFragment.setup(R.id.myDrawerFrag, (DrawerLayout) findViewById(R.id.drawerLayout), tool_barAppModule, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    drawerFragment.usersignoutFuntionality();
                    logoutFromAstroSageCloud(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void enableToolBar() {
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        drawerFragment.updateLoginDetials(false, "", "", getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());

        if (isShowToast) {

            // create a handler to post messages to the main thread
            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MyCustomToast mct = new MyCustomToast(DhruvDashBoardActivity.this,
                            DhruvDashBoardActivity.this.getLayoutInflater(), DhruvDashBoardActivity.this,
                            regularTypeface);
                    mct.show(getResources().getString(R.string.sign_out_success));
                }
            });
        }
        if (CUtils.isConnectedWithInternet(DhruvDashBoardActivity.this)) {
            //new GetData().execute();
            // getImageAdData();
        }

        //adapter.notifyDataSetChanged();
        //added by Ankit with monika on 6/1/2020
        /*CUtils.showAdvertisement(ActAppModule.this,
                (LinearLayout) findViewById(R.id.advLayout));*/

    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        drawerFragment.updateLoginDetials(true, loginName, loginPwd, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUB_ACTIVITY_USER_LOGIN: {
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);
                }
            }
            break;

        }
    }

    List<Integer> getDrawerListItemIndex() {
        try {
            return Arrays.asList(dhruv_menu_index);
        } catch (Exception ex) {
            return null;
        }
    }

    private List<Drawable> getDrawerListItemIcon() {
        try {
            TypedArray itemsIcon2 = getResources().obtainTypedArray(R.array.dhruv_drawer_item_list_icon);
            return CUtils.convertTypedArrayToArrayList(DhruvDashBoardActivity.this, itemsIcon2, dhruv_menu_index);
        } catch (Exception ex) {
            return null;
        }
    }

    private List<String> getDrawerListItem() {
        try {
            String[] menuItems2 = getResources().getStringArray(R.array.dhruv_drawer_item_list);
            return Arrays.asList(menuItems2);
        } catch (Exception ex) {
            return null;
        }
    }

    public void newKundli(int pos) {
        int moduleType = CGlobalVariables.MODULE_BASIC;
        switch (pos) {
            case TAG_NEW_KUNDLI_DHRUV:
                moduleType = CGlobalVariables.MODULE_BASIC;
                break;
            case TAG_NEW_KUNDLI_DHRUV_MATCH_MAKING:
                moduleType = CGlobalVariables.MODULE_MATCHING;
                break;
            case TAG_NEW_KUNDLI_DHRUV_PRINT_KUNDLI:
                moduleType = CGlobalVariables.MODULE_BASIC;
                break;
            case TAG_NEW_KUNDLI_DHRUV_DASHA:
                moduleType = CGlobalVariables.MODULE_DASA;
                break;
            case TAG_NEW_KUNDLI_DHRUV_PREDICTION:
                moduleType = CGlobalVariables.MODULE_PREDICTION;
                break;
            case TAG_NEW_KUNDLI_DHRUV_NUMEROLOGY:
                moduleType = CGlobalVariables.MODULE_ASTROSAGE_NUMROLOGY;
                break;
            case TAG_NEW_KUNDLI_DHRUV_KP:
                moduleType = CGlobalVariables.MODULE_KP;
                break;
            case TAG_NEW_KUNDLI_DHRUV_SHODASHVARGA:
                moduleType = CGlobalVariables.MODULE_SHODASHVARGA;
                break;
            case TAG_NEW_KUNDLI_DHRUV_LAL_KITAB:
                moduleType = CGlobalVariables.MODULE_LALKITAB;
                break;
            case TAG_NEW_KUNDLI_DHRUV_VARSHFAL:
                moduleType = CGlobalVariables.MODULE_VARSHAPHAL;
                break;
        }
        Intent intent;
        if (moduleType == CGlobalVariables.MODULE_MATCHING) {
            intent = new Intent(DhruvDashBoardActivity.this, HomeMatchMakingInputScreen.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
        } else if (moduleType == CGlobalVariables.MODULE_ASTROSAGE_NUMROLOGY) {
            intent = new Intent(DhruvDashBoardActivity.this, NumerologyCalculatorInputActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
        } else {
            intent = new Intent(DhruvDashBoardActivity.this, HomeInputScreen.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
        }
        startActivity(intent);
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
         if(method == GET_DIRECTORY_HOME){
            if(!TextUtils.isEmpty(response)) {
                Gson gson = new Gson();
                mDirectoryHomeBean = gson.fromJson(response.toString(), DirectoryHomeBean.class);
                if (mDirectoryHomeBean != null && mDirectoryHomeBean.getStatus().equalsIgnoreCase("1")) {
                    Intent directoryHomeIntent = new Intent(this, DirectoryListingHomeActivity.class);
                    directoryHomeIntent.putExtra("directoryurl",mDirectoryHomeBean.getDirectoryurl());
                    startActivity(directoryHomeIntent);
                } else {
                    Intent listingCreationIntent = new Intent(this, ListingCreationActivity.class);
                    startActivity(listingCreationIntent);
                }
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }

    public void getDirectoryHome(){
        if (CUtils.isConnectedWithInternet(DhruvDashBoardActivity.this)) {
            showProgressBar();
            CUtils.sendDirectoryHome(DhruvDashBoardActivity.this, getDirectoryHomeParams(), GET_DIRECTORY_HOME);
        } else {
            showSnackbar(tabLayout, getResources().getString(R.string.no_internet));
        }
    }

    public Map<String, String> getDirectoryHomeParams() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userid", CUtils.getUserName(DhruvDashBoardActivity.this));
        params.put("isapi", "1");
        params.put("key", CUtils.getApplicationSignatureHashCode(DhruvDashBoardActivity.this));
        return params;
    }

    private void showProgressBar() {
        try {
            pd = new CustomProgressDialog(this, regularTypeface);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
