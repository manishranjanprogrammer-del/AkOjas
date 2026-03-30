package com.ojassoft.astrosage.ui.act;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopData;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.jinterface.IAskCallback;
import com.ojassoft.astrosage.jinterface.IPermissionCallback;
import com.ojassoft.astrosage.ui.fragments.FragAstroShopForeignPayment;
import com.ojassoft.astrosage.ui.fragments.FragAstroShopLogIn;
import com.ojassoft.astrosage.ui.fragments.FragAstroShopNewAddress;
import com.ojassoft.astrosage.ui.fragments.FragAstroShopPayment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.razorpay.PaymentResultListener;

//import com.google.analytics.tracking.android.EasyTracker;


/**
 * Created by ojas on ३/३/१६
 */
public class ActAstroShopShippingDetails extends BaseInputActivity implements View.OnClickListener, IPermissionCallback, IAskCallback, PaymentResultListener {


    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private AstroShopData astroShopData;
    private FrameLayout frame_layout;
    private int frameId = R.id.frame_layout;
    private FragmentTransaction ft;
    private ImageView tabLogin, tabShipping, tabPayment;
    private ProgressDialog pDialog;
    public AstroShopItemDetails itemdetails;
    public int noOfItem = 1;
    private Bundle bundle;
    private TextView txtlogin, txtshipping, txtpayment;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private final int GREYALPHA = 128;
    private final int NORMALPHA = 255;
    private ScrollView scScroll;
    private Boolean isFromCart = false;


    Typeface typeface;

    public ActAstroShopShippingDetails() {
        super(R.id.app_name);
    }

    @Override
    public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
        try {
            FragAstroShopLogIn fragment = (FragAstroShopLogIn) getSupportFragmentManager().findFragmentByTag("FragAstroShopLogIn");
            fragment.isEmailIdPermissionGranted(true);
        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }
    }


   /* @Override
    public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
        try {
            FragAstroShopLogIn fragment = (FragAstroShopLogIn) getSupportFragmentManager().findFragmentByTag("FragAstroShopLogIn");
            fragment.isEmailIdPermissionGranted(isPermissionGranted);
        }catch (Exception ex){
            //Log.e("Exception",ex.getMessage());
        }

    }*/

    //    Will be used to change for tab transection from various fragments
    public enum Status {
        LOGIN_ENABLE, SHIPPING_ENABLE, PAYMENT_ENABLE, LOGIN_DONE, SHIPPINGDONE, PAYMENTDONE
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.activity_astroshop_shipping_details);

        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        itemdetails = new AstroShopItemDetails();
        bundle = getIntent().getExtras();
        isFromCart = bundle.getBoolean("fromCart");
        if (!isFromCart) {
            itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");
            noOfItem = bundle.getInt("ItemNo");
        }

        setSupportActionBar(tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.home_astro_shop));
        tvTitle.setTypeface(((BaseInputActivity) this).regularTypeface);
        txtshipping = (TextView) findViewById(R.id.txtshipping);
        txtlogin = (TextView) findViewById(R.id.txtlogin);
        txtpayment = (TextView) findViewById(R.id.txtpayment);
        txtlogin.setTypeface(((BaseInputActivity) this).mediumTypeface);
        txtshipping.setTypeface(((BaseInputActivity) this).mediumTypeface);
        txtpayment.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLogin = (ImageView) findViewById(R.id.tablogin);
        tabShipping = (ImageView) findViewById(R.id.tabShipping);
        tabPayment = (ImageView) findViewById(R.id.tabpayment);
        scScroll = (ScrollView) findViewById(R.id.scScroll);


        if (CUtils.restoreIsUserInShipping(ActAstroShopShippingDetails.this)) {
            if (savedInstanceState == null) {
                Fragment frag_astroshop_newaddress = new FragAstroShopNewAddress();
              /*  if (bundle != null) {
                    bundle.putSerializable("key", itemdetails);
                    bundle.putInt("ItemNo", noOfItem);

                }*/
                frag_astroshop_newaddress.setArguments(bundle);
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(frameId, frag_astroshop_newaddress).commit();
            }
        } else {
            if (savedInstanceState == null) {
                Fragment frag_astroshop_login = new FragAstroShopLogIn();
              /*  if (bundle != null) {
                    bundle.putSerializable("key", itemdetails);
                    bundle.putInt("ItemNo", noOfItem);

                }*/
                frag_astroshop_login.setArguments(bundle);
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(frameId, frag_astroshop_login, "FragAstroShopLogIn").commit();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSelected(Status status) {
        switch (status) {
            case LOGIN_ENABLE:
                tabLogin.setBackgroundResource(R.drawable.circle_orange);
                tabLogin.setImageResource(R.drawable.ic_account_circle_white);
                tabShipping.setImageResource(R.drawable.ic_local_shipping_black);
                tabShipping.setAlpha(GREYALPHA);
                tabPayment.setAlpha(GREYALPHA);
                break;
            case SHIPPING_ENABLE:
                tabShipping.setBackgroundResource(R.drawable.circle_orange);
                tabShipping.setImageResource(R.drawable.ic_local_shipping_white);
                tabShipping.setAlpha(NORMALPHA);
                tabPayment.setAlpha(GREYALPHA);
                tabPayment.setImageResource(R.drawable.ic_rupees_sign_black);
                tabPayment.setBackgroundResource(R.drawable.circle_gray);
                break;
            case PAYMENT_ENABLE:
                tabPayment.setBackgroundResource(R.drawable.circle_orange);
                tabPayment.setImageResource(R.drawable.ic_rupees_sign_white);
                tabPayment.setAlpha(NORMALPHA);
                tabShipping.setAlpha(NORMALPHA);
                tabLogin.setAlpha(NORMALPHA);
                break;
            case LOGIN_DONE:
                tabLogin.setBackgroundResource(R.drawable.circle_green);
                tabLogin.setImageResource(R.drawable.ic_done_white);
                break;
            case SHIPPINGDONE:
                tabShipping.setBackgroundResource(R.drawable.circle_green);
                tabShipping.setImageResource(R.drawable.ic_done_white);
                break;
            case PAYMENTDONE:
                tabPayment.setBackgroundResource(R.drawable.circle_green);
                tabPayment.setImageResource(R.drawable.ic_done_white);
                break;
            default:
                tabLogin.setBackgroundResource(R.drawable.circle_gray);
                tabLogin.setImageResource(R.drawable.ic_account_circle_white);
                tabShipping.setBackgroundResource(R.drawable.circle_gray);
                tabShipping.setImageResource(R.drawable.ic_local_shipping_white);
                tabPayment.setBackgroundResource(R.drawable.circle_gray);
                tabPayment.setImageResource(R.drawable.ic_rupees_sign_white);
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CUtils.hideMyKeyboard(this);
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSION_CONTACTS:
                try {
                    FragAstroShopLogIn fragment = (FragAstroShopLogIn) getSupportFragmentManager().findFragmentByTag("FragAstroShopLogIn");
                    fragment.checkForPermission(false);
                } catch (Exception ex) {
                    // //Log.e("Exception",ex.getMessage());
                }
                break;
            case CGlobalVariables.REQUEST_CODE_PAYTM:{
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("FragAstroShopPayment");
                if(fragment!=null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
            }
        }

    }

    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        try {
            FragAstroShopPayment fragment1 = (FragAstroShopPayment) getSupportFragmentManager().findFragmentByTag("FragAstroShopPayment");
            if (fragment1 != null) {
                fragment1.getCallBack(result, callback, "", "");
            }

            FragAstroShopForeignPayment fragment2 = (FragAstroShopForeignPayment) getSupportFragmentManager().findFragmentByTag("FragAstroShopForeignPayment");
            if (fragment2 != null) {
                fragment2.getCallBack(result, callback, "", "");
            }

        } catch (Exception ex) {
            // //Log.e("Exception",ex.getMessage());
        }
    }

    @Override
    public void getCallBackForChat(String[] result, CUtils.callBack callback, String priceInDollor, String priceInRs) {

    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            FragAstroShopPayment fragment1 = (FragAstroShopPayment) getSupportFragmentManager().findFragmentByTag("FragAstroShopPayment");
            if (fragment1 != null) {
                fragment1.onPaymentSuccess(razorpayPaymentID);
            }

            FragAstroShopForeignPayment fragment2 = (FragAstroShopForeignPayment) getSupportFragmentManager().findFragmentByTag("FragAstroShopForeignPayment");
            if (fragment2 != null) {
                fragment2.onPaymentSuccess(razorpayPaymentID);
            }

        } catch (Exception ex) {
            // //Log.e("Exception",ex.getMessage());
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {

            FragAstroShopPayment fragment1 = (FragAstroShopPayment) getSupportFragmentManager().findFragmentByTag("FragAstroShopPayment");
            if (fragment1 != null) {
                fragment1.onPaymentError(i, s);
            }

            FragAstroShopForeignPayment fragment2 = (FragAstroShopForeignPayment) getSupportFragmentManager().findFragmentByTag("FragAstroShopForeignPayment");
            if (fragment2 != null) {
                fragment2.onPaymentError(i, s);
            }

            CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
        } catch (Exception ex) {
            // //Log.e("Exception",ex.getMessage());
        }
    }

}