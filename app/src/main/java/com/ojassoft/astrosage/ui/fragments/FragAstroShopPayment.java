package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.ItemOrderModel;
import com.ojassoft.astrosage.customadapters.PayOptAdapter;
import com.ojassoft.astrosage.jinterface.IAskCallback;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CardTypeDTO;
import com.ojassoft.astrosage.model.PaySubOption;
import com.ojassoft.astrosage.model.ProductCategorymodal;
import com.ojassoft.astrosage.networkcall.OnVolleyResultListener;
import com.ojassoft.astrosage.ui.act.ActAstroShopChequeDdDetail;
import com.ojassoft.astrosage.ui.act.ActAstroShopShippingDetails;
import com.ojassoft.astrosage.ui.act.ActAstroshopCodDetails;
import com.ojassoft.astrosage.ui.act.ActPaymentStatus;
import com.ojassoft.astrosage.ui.act.ActWebViewActivity;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;

/**
 * Created by ojas on ८/३/१६.
 */
public class FragAstroShopPayment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, IAskCallback, OnVolleyResultListener {

    public AstroShopItemDetails itemdetails;
    public int noOfItem = 1;
    ArrayList<CardTypeDTO> cardList;
    ArrayList<CardTypeDTO> topCard;
    MyCustomToast mct;
    private RadioButton radiocreditcard, radiodebitcard, radionetbanking, radiowallet, radiocheque, radiocashondelivery, radiopui;
    private View view;
    private LinearLayout layout_creditcard, layout_debitditcard, layout_net_banking, layout_net_wallet, layout_cheque_dd, layout_cash_on_delivery, layout_upi;
    private ImageView imgVisa, imgmaesrtocard, imgmastercard;
    private TextView txtvisa, txtmaesrtocard, txtmastercard;
    private Spinner spnrdrop;
    private TextView txtyopayrs, txtyoupayrscashon, txtcashondeliverylimit, txtUpicost;
    private Button btn_pay_proceed, btn_pay_place_order, btn_upi_pay_place_order, btn_online_pay_place_order;
    //private Payoption payOpt;
    private int parentId;
    private FragmentTransaction ft;
    private Bundle bundle;
    private CardTypeDTO finalSelectedCard;
    private PaySubOption subPayOpt;
    private String currency = "INR";
    private String order_id = "";
    private ItemOrderModel orderModel;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    private TextView tvpayMessage, tvOptoins;
    private CustomProgressDialog pd = null;
    private boolean imgselected = false;
    private TextView txtchequedd, txtchequeddcost, txtcod_msg;
    LinearLayout codLayout;
    private Button btn_pay_place_order_cheque_dd;
    private String payMode = "Razorpay";
    private TextView txtcreditcard, txtdebitcard, txtnetbanking, txtcheque, txtcashondelivery, txtwallet, txtOptionUpi;
    private JSONObject fullAddress;
    private RequestQueue queue;
    private String limit;
    private String pinCode;
    private LinearLayout llcodmsg;
    private final int cardFragId = R.id.flCard;
    private Boolean isFromCart = false;

    private String amount = "";
    private String payStatus = "";
    private String status = null;
    private String totalAmount;
    private String appVerName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.lay_astro_shop_payment, container, false);
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        appVerName = LibCUtils.getApplicationVersionToShow(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();

        typeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        try {

            fullAddress = new JSONObject(CUtils.getAstroshopUserAddressDetailInPrefs(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        init(view);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //txtcreditcard,txtdebitcard,txtnetbanking,txtcheque,txtcashondelivery;

            case R.id.radiocreditcard:
            case R.id.txtcreditcard:
                onCreditCardSelect();
                CardTypeDTO selectedCard = finalSelectedCard;
                break;
            case R.id.radiodebitcard:
            case R.id.txtdebitcard:
                llcodmsg.setVisibility(View.GONE);
                payMode = "Razorpay";
                setListener(R.id.incdebit);
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(true);
                radionetbanking.setChecked(false);
                radiowallet.setChecked(false);
                radiocheque.setChecked(false);
                radiocashondelivery.setChecked(false);
                radiopui.setChecked(false);
                layout_upi.setVisibility(View.GONE);
                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.GONE); //by abhishek
                layout_net_banking.setVisibility(View.GONE);
                layout_net_wallet.setVisibility(View.GONE);
                layout_cheque_dd.setVisibility(View.GONE);
                layout_cash_on_delivery.setVisibility(View.GONE);
                btn_online_pay_place_order.setVisibility(View.VISIBLE);

                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);
                populatePayOptions(R.id.radiodebitcard, R.id.incdebit);
                setTotalPayment(parentId);

                break;
            case R.id.radionetbanking:
            case R.id.txtnetbanking:
                llcodmsg.setVisibility(View.GONE);
                payMode = "Razorpay";
                setListener(R.id.incnetbanking);
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(false);
                radionetbanking.setChecked(true);
                radiowallet.setChecked(false);
                radiocheque.setChecked(false);
                radiocashondelivery.setChecked(false);
                radiopui.setChecked(false);
                layout_upi.setVisibility(View.GONE);
                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.GONE);
                layout_net_banking.setVisibility(View.GONE); //by abhishek
                layout_net_wallet.setVisibility(View.GONE);
                layout_cheque_dd.setVisibility(View.GONE);
                layout_cash_on_delivery.setVisibility(View.GONE);
                btn_online_pay_place_order.setVisibility(View.VISIBLE);

                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);
                populatePayOptions(R.id.radionetbanking, R.id.incnetbanking);
                setTotalPayment(parentId);
                break;
            case R.id.radiowallet:
            case R.id.txtWalet:
                llcodmsg.setVisibility(View.GONE);
                payMode = "Razorpay";
                setListener(R.id.incnet_wallet);
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(false);
                radionetbanking.setChecked(false);
                radiowallet.setChecked(true);
                radiocheque.setChecked(false);
                radiocashondelivery.setChecked(false);
                radiopui.setChecked(false);
                layout_upi.setVisibility(View.GONE);
                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.GONE);
                layout_net_banking.setVisibility(View.GONE);
                layout_net_wallet.setVisibility(View.GONE); //by abhishek
                layout_cheque_dd.setVisibility(View.GONE);
                layout_cash_on_delivery.setVisibility(View.GONE);
                btn_online_pay_place_order.setVisibility(View.VISIBLE);

                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);
                populatePayOptions(R.id.radiowallet, R.id.incnet_wallet);
                setTotalPayment(parentId);
                break;
            case R.id.radiocheque:
            case R.id.txtcheque:
                llcodmsg.setVisibility(View.GONE);
                payMode = "cheque";
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(false);
                radionetbanking.setChecked(false);
                radiowallet.setChecked(false);
                radiocheque.setChecked(true);
                radiocashondelivery.setChecked(false);
                radiopui.setChecked(false);
                layout_upi.setVisibility(View.GONE);
                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.GONE);
                layout_net_banking.setVisibility(View.GONE);
                layout_net_wallet.setVisibility(View.GONE);
                layout_cheque_dd.setVisibility(View.GONE); //by abhishek
                layout_cash_on_delivery.setVisibility(View.GONE);
                btn_online_pay_place_order.setVisibility(View.VISIBLE);
                setTotalPaymentForChequeDD();
                btn_pay_place_order_cheque_dd.setVisibility(View.VISIBLE);
                break;

            case R.id.txtcashondelivery:
            case R.id.radiocashondelivery:
                llcodmsg.setVisibility(View.GONE);
                payMode = "COD";
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(false);
                radionetbanking.setChecked(false);
                radiowallet.setChecked(false);
                radiocheque.setChecked(false);
                radiocashondelivery.setChecked(true);
                radiopui.setChecked(false);
                layout_upi.setVisibility(View.GONE);
                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.GONE);
                layout_net_banking.setVisibility(View.GONE);
                layout_net_wallet.setVisibility(View.GONE);
                layout_cheque_dd.setVisibility(View.GONE);
                layout_cash_on_delivery.setVisibility(View.VISIBLE);
                btn_online_pay_place_order.setVisibility(View.GONE);
                setTotalPaymentForCashOn();

                loadAstroShopPinCheck();
                break;
            case R.id.btn_pay_proceed:
            case R.id.btn_pay_place_order:
                genrateOrder(orderModel);
                CUtils.googleAnalyticSendWitPlayServie(getActivity(), CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_COD, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_COD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;

            case R.id.radiopui:
            case R.id.txtOptionUpi:
                selectedCard = new CardTypeDTO();
                selectedCard.setPayOptType("OPTUPI");
                selectedCard.setCardType("UPI");
                selectedCard.setCardName("UPI");
                selectedCard.setDataAcceptedAt("");
                finalSelectedCard = selectedCard;
                payMode = "Razorpay";

                llcodmsg.setVisibility(View.GONE);
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(false);
                radionetbanking.setChecked(false);
                radiowallet.setChecked(false);
                radiocheque.setChecked(false);
                radiocashondelivery.setChecked(false);
                radiopui.setChecked(true);
                layout_upi.setVisibility(View.GONE); //by abhishek
                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.GONE);
                layout_net_banking.setVisibility(View.GONE);
                layout_net_wallet.setVisibility(View.GONE);
                layout_cheque_dd.setVisibility(View.GONE);
                layout_cash_on_delivery.setVisibility(View.GONE);
                setTotalPaymentForUpi();
                btn_upi_pay_place_order.setVisibility(View.VISIBLE);
                btn_online_pay_place_order.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_pay_place_order_cheque_dd:
                genrateOrder(orderModel);
                CUtils.googleAnalyticSendWitPlayServie(getActivity(), CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_CHEQUEDD, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_CHEQUEDD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case R.id.btn_upi_pay_place_order:
                genrateOrder(orderModel);
                break;
            case R.id.btn_online_pay_place_order:

                boolean paytmVisibilityForProduct = CUtils.getBooleanData(getActivity(), CGlobalVariables.paytmVisibilityForProduct, false);
                boolean ccavenueVisibilityForProducts = CUtils.getBooleanData(getActivity(), CGlobalVariables.ccavenueVisibilityForProducts, false);
                boolean razorPayVisibilityForProduct = CUtils.getBooleanData(getActivity(), CGlobalVariables.razorPayVisibilityForProduct, false);
                if (paytmVisibilityForProduct) {
                    payMode = "Paytm";
                } else if (ccavenueVisibilityForProducts) {
                    payMode = "CCAvenue";
                } else if (razorPayVisibilityForProduct) {
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        payMode = "Paytm"; // for api level 16 to 18
                    } else {
                        payMode = "Razorpay";
                    }
                }
                genrateOrder(orderModel);
                break;
            case R.id.imgVisa:
                imgselected = true;
                spnrdrop.setSelection(0);
                imgVisa.setBackgroundResource(R.drawable.astroshop_border_circle);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);
                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                CardTypeDTO c = (CardTypeDTO) ((ImageView) view.findViewById(parentId).findViewById(R.id.imgVisa)).getTag();
                finalSelectedCard = c;
//
                Log.wtf("View Bound", c.getCardName());
                if (finalSelectedCard == null) {
                    mct.show(getResources().getString(R.string.astroshop_payment_selection));
                } else {
                    genrateOrder(orderModel);
                }
                break;
            case R.id.imgmaesrtocard:
                imgselected = true;
                spnrdrop.setSelection(0);
                imgmaesrtocard.setBackgroundResource(R.drawable.astroshop_border_circle);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                CardTypeDTO c1 = (CardTypeDTO) ((ImageView) view.findViewById(parentId).findViewById(R.id.imgmaesrtocard)).getTag();
                finalSelectedCard = c1;
                Log.wtf("View Bound", c1.getCardName());
                if (finalSelectedCard == null) {
                    mct.show(getResources().getString(R.string.astroshop_payment_selection));
                } else {
                    genrateOrder(orderModel);
                }
                break;

            case R.id.tvOptoins:
                Fragment fragCardSave = new FragAstroSavedCards();
                if (bundle != null) {
                    if (!isFromCart) {
                        bundle.putSerializable("Key", itemdetails);
                        bundle.putInt("ItemNo", noOfItem);
                    }
                    //bundle.putSerializable("Payoption", payOpt);
                    bundle.putString("currency", currency);
                    fragCardSave.setArguments(bundle);
                }
                ft = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                ft.replace(R.id.frame_layout, fragCardSave).addToBackStack(null).commit();
                break;
            case R.id.imgmastercard:
                imgselected = true;
                spnrdrop.setSelection(0);
                imgmastercard.setBackgroundResource(R.drawable.astroshop_border_circle);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);
                CardTypeDTO c2 = (CardTypeDTO) ((ImageView) view.findViewById(parentId).findViewById(R.id.imgmastercard)).getTag();
                finalSelectedCard = c2;

                Log.wtf("View Bound", c2.getCardName());
                if (finalSelectedCard == null) {
                    mct.show(getResources().getString(R.string.astroshop_payment_selection));
                } else {
                    genrateOrder(orderModel);
                }
                break;


            default:
                imgselected = false;
                spnrdrop.setSelection(0);
                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);


                break;
        }
    }

    private void onCreditCardSelect() {
        if (cod_available)
            llcodmsg.setVisibility(View.GONE);
        payMode = "Razorpay";
        //  setListener(R.id.inccredit);
        radiocreditcard.setChecked(true);
        radiodebitcard.setChecked(false);
        radionetbanking.setChecked(false);
        radiowallet.setChecked(false);
        radiocheque.setChecked(false);
        radiocashondelivery.setChecked(false);
        radiopui.setChecked(false);


        layout_creditcard.setVisibility(View.GONE); //by abhishek
        layout_debitditcard.setVisibility(View.GONE);
        layout_net_banking.setVisibility(View.GONE);
        layout_net_wallet.setVisibility(View.GONE);
        layout_cheque_dd.setVisibility(View.GONE);
        layout_upi.setVisibility(View.GONE);
        layout_cash_on_delivery.setVisibility(View.GONE);
        btn_online_pay_place_order.setVisibility(View.VISIBLE);


    }

    private void setListener(int parentId) {
        this.parentId = parentId;
        setListenerDisableFoeView();

        imgVisa = (ImageView) view.findViewById(parentId).findViewById(R.id.imgVisa);
        imgmaesrtocard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmaesrtocard);
        imgmastercard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmastercard);
        spnrdrop = (Spinner) view.findViewById(parentId).findViewById(R.id.spnrdrop);
        btn_pay_proceed = (Button) view.findViewById(parentId).findViewById(R.id.btn_pay_proceed);

        imgVisa.setOnClickListener(this);
        imgmaesrtocard.setOnClickListener(this);
        imgmastercard.setOnClickListener(this);
        spnrdrop.setOnItemSelectedListener(this);
        btn_pay_proceed.setOnClickListener(this);
        txtvisa = (TextView) view.findViewById(parentId).findViewById(R.id.txtvisa);
    }

    private void setListenerDisableFoeView() {

        if (imgVisa != null && imgmaesrtocard != null && imgmastercard != null) {
            imgVisa.setOnClickListener(null);
            imgmaesrtocard.setOnClickListener(null);
            imgmastercard.setOnClickListener(null);
            spnrdrop.setOnItemSelectedListener(null);
            btn_pay_proceed.setOnClickListener(null);

            txtvisa.setText("");
            txtmaesrtocard.setText("");
            txtmastercard.setText("");

        }

    }

    private void init(View v) {

        bundle = this.getArguments();
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        mct = new MyCustomToast(getActivity(), getActivity()
                .getLayoutInflater(), getActivity(), typeface);
        if (bundle != null) {
            isFromCart = bundle.getBoolean("fromCart", false);
            //payOpt = (Payoption) bundle.getSerializable("Payoption");
            currency = bundle.getString("currency");
            order_id = bundle.getString("order_Id");
            orderModel = (ItemOrderModel) bundle.getSerializable("detail");
            //Log.e("==", "" + currency);
            //Log.e("recieved", "" + order_id);
        }


        //   payOpt = ActAstroShopShippingDetails.globalPayOptions;
        ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.SHIPPINGDONE);
        ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.PAYMENT_ENABLE);


        txtcreditcard = (TextView) v.findViewById(R.id.txtcreditcard);
        txtdebitcard = (TextView) v.findViewById(R.id.txtdebitcard);
        txtnetbanking = (TextView) v.findViewById(R.id.txtnetbanking);
        txtcheque = (TextView) v.findViewById(R.id.txtcheque);
        txtcashondelivery = (TextView) v.findViewById(R.id.txtcashondelivery);
        txtcashondeliverylimit = (TextView) v.findViewById(R.id.txtcashondeliverylimit);
        llcodmsg = (LinearLayout) v.findViewById(R.id.llcodmsg);
        txtcod_msg = (TextView) v.findViewById(R.id.txtcod_msg);
        txtwallet = (TextView) v.findViewById(R.id.txtWalet);
        txtOptionUpi = (TextView) v.findViewById(R.id.txtOptionUpi);
        tvOptoins = (TextView) v.findViewById(R.id.tvOptoins);
        tvOptoins.setTypeface(typeface);

        txtUpicost = (TextView) v.findViewById(R.id.txtUpicost);

        //  txtcashondeliverylimit.setTypeface(((BaseInputActivity) getContext()).regularTypeface);
        txtcreditcard.setOnClickListener(this);
        txtdebitcard.setOnClickListener(this);
        txtnetbanking.setOnClickListener(this);
        txtcheque.setOnClickListener(this);
        tvOptoins.setOnClickListener(this);
        txtwallet.setOnClickListener(this);
        txtOptionUpi.setOnClickListener(this);
        tvpayMessage = (TextView) v.findViewById(R.id.tvpayMessage);
        tvpayMessage.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        radiocreditcard = (RadioButton) v.findViewById(R.id.radiocreditcard);
        radiocreditcard.setTypeface(typeface);
        radiodebitcard = (RadioButton) v.findViewById(R.id.radiodebitcard);
        radiodebitcard.setTypeface(typeface);
        radionetbanking = (RadioButton) v.findViewById(R.id.radionetbanking);
        radionetbanking.setTypeface(typeface);
        radiowallet = (RadioButton) v.findViewById(R.id.radiowallet);
        radiowallet.setTypeface(typeface);
        radiocheque = (RadioButton) v.findViewById(R.id.radiocheque);
        radiocheque.setTypeface(typeface);
        radiocashondelivery = (RadioButton) v.findViewById(R.id.radiocashondelivery);
        radiocashondelivery.setTypeface(typeface);
        radiopui = (RadioButton) v.findViewById(R.id.radiopui);
        radiopui.setTypeface(typeface);
        layout_creditcard = (LinearLayout) v.findViewById(R.id.layout_creditcard);
        layout_debitditcard = (LinearLayout) v.findViewById(R.id.layout_debitditcard);
        layout_net_banking = (LinearLayout) v.findViewById(R.id.layout_net_banking);
        layout_net_wallet = (LinearLayout) v.findViewById(R.id.layout_net_wallet);
        layout_cheque_dd = (LinearLayout) v.findViewById(R.id.layout_cheque_dd);
        layout_cash_on_delivery = (LinearLayout) v.findViewById(R.id.layout_cash_on_delivery);
        layout_upi = (LinearLayout) v.findViewById(R.id.layout_upi);
        codLayout = v.findViewById(R.id.cod_layout);

        txtvisa = (TextView) v.findViewById(R.id.txtvisa);
        txtmaesrtocard = (TextView) v.findViewById(R.id.txtmaesrtocard);
        txtmastercard = (TextView) v.findViewById(R.id.txtmastercard);
        spnrdrop = (Spinner) v.findViewById(R.id.spnrdrop);
        txtyopayrs = (TextView) v.findViewById(R.id.txtyopayrs);
        // txtyopayrs.setTypeface(typeface);
        txtyoupayrscashon = (TextView) v.findViewById(R.id.txtyoupayrscashon);
        btn_pay_proceed = (Button) v.findViewById(R.id.btn_pay_proceed);
        btn_pay_proceed.setTypeface(typeface);
        txtchequedd = (TextView) v.findViewById(R.id.txtchequedd);
        txtchequeddcost = (TextView) v.findViewById(R.id.txtchequeddcost);
        txtchequeddcost.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        txtchequedd.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);

        btn_pay_place_order = (Button) v.findViewById(R.id.btn_pay_place_order);
        btn_pay_place_order.setTypeface(typeface);
        btn_pay_place_order.setOnClickListener(this);
        btn_upi_pay_place_order = (Button) v.findViewById(R.id.btn_upi_pay_place_order);
        btn_upi_pay_place_order.setTypeface(typeface);
        btn_upi_pay_place_order.setOnClickListener(this);

        //by abhishek
        btn_online_pay_place_order = (Button) v.findViewById(R.id.btn_online_pay_place_order);
        btn_online_pay_place_order.setTypeface(typeface);
        btn_online_pay_place_order.setOnClickListener(this);

        btn_pay_place_order_cheque_dd = (Button) v.findViewById(R.id.btn_pay_place_order_cheque_dd);
        btn_pay_place_order_cheque_dd.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        btn_pay_place_order_cheque_dd.setOnClickListener(this);
        btn_pay_place_order.setVisibility(View.GONE);
        btn_pay_place_order_cheque_dd.setVisibility(View.GONE);
        if (bundle != null && !isFromCart) {
            itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");
        }
        try {
            limit = fullAddress.getString("COD_LimitCost");
            pinCode = orderModel.getPincode();
        } catch (Exception e) {

        }
        String cardFromPrefs = CUtils.getSavedCards(getActivity());
        if (!cardFromPrefs.isEmpty())
            tvOptoins.setVisibility(View.VISIBLE);
        else
            tvOptoins.setVisibility(View.GONE);

        txtcashondeliverylimit.setText("(" + getResources().getString(R.string.cash_on_delivery_limit).replace("@", limit) + ")");
        try {

            if (!fullAddress.getString("COD_LimitCost").isEmpty() && Integer.parseInt(fullAddress.getString("COD_LimitCost")) > roundFunction((Double.parseDouble(orderModel.getProductcost_inrs())), 2)) {

            } else {
                txtcashondelivery.setEnabled(false);
                radiocashondelivery.setEnabled(false);
            }


        } catch (Exception e) {

        }
        txtcashondelivery.setOnClickListener(this);
        radiocreditcard.setOnClickListener(this);
        radiodebitcard.setOnClickListener(this);
        radionetbanking.setOnClickListener(this);
        radiowallet.setOnClickListener(this);
        radiocheque.setOnClickListener(this);
        radiocashondelivery.setOnClickListener(this);
        radiopui.setOnClickListener(this);
        btn_pay_proceed.setOnClickListener(this);
        spnrdrop.setOnItemSelectedListener(this);

        layout_creditcard.setVisibility(View.GONE);
        layout_debitditcard.setVisibility(View.GONE);
        layout_net_banking.setVisibility(View.GONE);
        layout_net_wallet.setVisibility(View.GONE);
        layout_cheque_dd.setVisibility(View.GONE);
        layout_upi.setVisibility(View.GONE);
        layout_cash_on_delivery.setVisibility(View.GONE);
        btn_online_pay_place_order.setVisibility(View.GONE);
        radiocreditcard.setChecked(true);
        onCreditCardSelect();
    }

    private void populatePayOptions(int id, int include_id) {
        PaySubOption sunOpt = null;
        PayOptAdapter adapter = null;
        JSONObject job = null;
        Gson gson = new Gson();
        String str = "";
        JsonElement element = null;
        JsonArray array = null;
        switch (id) {
            case R.id.radiocreditcard:
                sunOpt = (PaySubOption) radiocreditcard.getTag();
                subPayOpt = sunOpt;
                str = sunOpt.getCardsList();
                element = gson.fromJson(str, JsonElement.class);
                array = element.getAsJsonArray();
                cardList = (ArrayList<CardTypeDTO>) gson.fromJson(str,
                        new TypeToken<ArrayList<CardTypeDTO>>() {
                        }.getType());

                topCard = new ArrayList<>();
                for (CardTypeDTO card : cardList) {
                    if (card.getCardName().equalsIgnoreCase("Visa") || card.getCardName().equalsIgnoreCase("Amex") || card.getCardName().equalsIgnoreCase("MasterCard")) {
                        topCard.add(card);
                    }
                }
                for (CardTypeDTO card : topCard) {
                    if (card.getCardName().equalsIgnoreCase("Visa")) {
                        imgVisa = (ImageView) view.findViewById(parentId).findViewById(R.id.imgVisa);
                        imgVisa.setTag(card);
                        imgVisa.setImageResource(R.drawable.ic_visa);
                        txtvisa.setText(R.string.visa);

                    } else if (card.getCardName().equalsIgnoreCase("Amex")) {

                        imgmaesrtocard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmaesrtocard);
                        imgmaesrtocard.setTag(card);
                        imgmaesrtocard.setImageResource(R.drawable.ic_amex);
                        txtmaesrtocard = (TextView) view.findViewById(parentId).findViewById(R.id.txtmaesrtocard);
                        txtmaesrtocard.setText(R.string.amex);
                    } else if (card.getCardName().equalsIgnoreCase("MasterCard")) {
                        imgmastercard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmastercard);
                        imgmastercard.setTag(card);
                        imgmastercard.setImageResource(R.drawable.ic_mastercard);
                        txtmastercard = (TextView) view.findViewById(parentId).findViewById(R.id.txtmastercard);
                        txtmastercard.setText(R.string.master_card);
                    }

                }
                cardList.removeAll(topCard);
                CardTypeDTO cardtype = new CardTypeDTO();
                cardtype.setCardName(getResources().getString(R.string.astroshop_select));
                cardList.add(0, cardtype);
                spnrdrop.setVisibility(View.VISIBLE);
                adapter = new PayOptAdapter(
                        getActivity(),
                        android.R.layout.simple_spinner_item, cardList);
                spnrdrop.setAdapter(adapter);
                break;
            case R.id.radiodebitcard:
                sunOpt = (PaySubOption) radiodebitcard.getTag();
                subPayOpt = sunOpt;
                str = sunOpt.getCardsList();
                element = gson.fromJson(str, JsonElement.class);
                array = element.getAsJsonArray();
                cardList = (ArrayList<CardTypeDTO>) gson.fromJson(str,
                        new TypeToken<ArrayList<CardTypeDTO>>() {
                        }.getType());

                topCard = new ArrayList<>();
                for (CardTypeDTO card : cardList) {
                    if (card.getCardName().equalsIgnoreCase("Visa Debit Card") || card.getCardName().equalsIgnoreCase("Maestro Debit Card") || card.getCardName().equalsIgnoreCase("MasterCard Debit Card")) {
                        topCard.add(card);
                    }
                }
                for (CardTypeDTO card : topCard) {
                    //Log.e("Card name",card.getCardName());
                    if (card.getCardName().equalsIgnoreCase("Visa Debit Card")) {
                        imgVisa = (ImageView) view.findViewById(parentId).findViewById(R.id.imgVisa);
                        imgVisa.setTag(card);
                        imgVisa.setImageResource(R.drawable.ic_visa);
                        txtvisa = (TextView) view.findViewById(parentId).findViewById(R.id.txtvisa);
                        txtvisa.setText(R.string.visa);


                    } else if (card.getCardName().equalsIgnoreCase("Maestro Debit Card")) {
                        imgmaesrtocard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmaesrtocard);
                        imgmaesrtocard.setTag(card);
                        imgmaesrtocard.setImageResource(R.drawable.ic_mestrocard);
                        txtmaesrtocard = (TextView) view.findViewById(parentId).findViewById(R.id.txtmaesrtocard);
                        txtmaesrtocard.setText(R.string.maestro);

                    } else if (card.getCardName().equalsIgnoreCase("MasterCard Debit Card")) {
                        imgmastercard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmastercard);
                        imgmastercard.setTag(card);
                        imgmastercard.setImageResource(R.drawable.ic_mastercard);
                        txtmastercard = (TextView) view.findViewById(parentId).findViewById(R.id.txtmastercard);
                        txtmastercard.setText(R.string.master_card);
                    }
                }
                cardList.removeAll(topCard);
                CardTypeDTO cardtypeone = new CardTypeDTO();
                cardtypeone.setCardName(getResources().getString(R.string.astroshop_select));
                cardList.add(0, cardtypeone);
                adapter = new PayOptAdapter(
                        getActivity(),
                        android.R.layout.simple_spinner_item, cardList);
                spnrdrop.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                spnrdrop.setAdapter(adapter);
                break;
            case R.id.radionetbanking:
                sunOpt = (PaySubOption) radionetbanking.getTag();
                subPayOpt = sunOpt;
                str = sunOpt.getCardsList();
                element = gson.fromJson(str, JsonElement.class);
                array = element.getAsJsonArray();
                cardList = (ArrayList<CardTypeDTO>) gson.fromJson(str,
                        new TypeToken<ArrayList<CardTypeDTO>>() {
                        }.getType());

                topCard = new ArrayList<>();
                for (CardTypeDTO card : cardList) {
                    if (card.getCardName().equalsIgnoreCase("HDFC Bank") || card.getCardName().equalsIgnoreCase("ICICI Bank") || card.getCardName().equalsIgnoreCase("State Bank of India")) {
                        topCard.add(card);
                    }
                }
                for (CardTypeDTO card : topCard) {
                    if (card.getCardName().equalsIgnoreCase("State Bank of India")) {
                        imgVisa = (ImageView) view.findViewById(parentId).findViewById(R.id.imgVisa);
                        imgVisa.setTag(card);
                        imgVisa.setImageResource(R.drawable.ic_sbi);
                        txtvisa.setText(R.string.sbi);

                    } else if (card.getCardName().equalsIgnoreCase("ICICI Bank")) {
                        imgmaesrtocard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmaesrtocard);
                        imgmaesrtocard.setTag(card);
                        imgmaesrtocard.setImageResource(R.drawable.ic_icici);
                        txtmaesrtocard = (TextView) view.findViewById(parentId).findViewById(R.id.txtmaesrtocard);
                        txtmaesrtocard.setText(R.string.icici);

                    } else if (card.getCardName().equalsIgnoreCase("HDFC Bank")) {
                        imgmastercard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmastercard);
                        imgmastercard.setTag(card);
                        imgmastercard.setImageResource(R.drawable.ic_hdfc);
                        txtmastercard = (TextView) view.findViewById(parentId).findViewById(R.id.txtmastercard);
                        txtmastercard.setText(R.string.hdfc);
                    }

                }
                cardList.removeAll(topCard);
                CardTypeDTO cardtypetwo = new CardTypeDTO();
                cardtypetwo.setCardName(getResources().getString(R.string.astroshop_select));
                cardList.add(0, cardtypetwo);
                adapter = new PayOptAdapter(
                        getActivity(),
                        android.R.layout.simple_spinner_item, cardList);
                spnrdrop.setVisibility(View.VISIBLE);

                adapter.notifyDataSetChanged();
                spnrdrop.setAdapter(adapter);

                break;

            case R.id.radiowallet:

                sunOpt = (PaySubOption) radiowallet.getTag();
                subPayOpt = sunOpt;
                str = sunOpt.getCardsList();
                element = gson.fromJson(str, JsonElement.class);
                array = element.getAsJsonArray();
                cardList = (ArrayList<CardTypeDTO>) gson.fromJson(str,
                        new TypeToken<ArrayList<CardTypeDTO>>() {
                        }.getType());

                topCard = new ArrayList<>();
                for (CardTypeDTO card : cardList) {

                    if (card.getCardName().equalsIgnoreCase("MobiKwik") || card.getCardName().equalsIgnoreCase("Paytm") || card.getCardName().equalsIgnoreCase("PayZapp")) {
                        topCard.add(card);
                    }
                }
                for (CardTypeDTO card : topCard) {
                    if (card.getCardName().equalsIgnoreCase("Paytm")) {
                        imgVisa = (ImageView) view.findViewById(parentId).findViewById(R.id.imgVisa);
                        imgVisa.setTag(card);
                        imgVisa.setImageResource(R.drawable.ic_paytm);
                        txtvisa.setText(getResources().getString(R.string.astroshop_paytm));

                    } else if (card.getCardName().equalsIgnoreCase("Mobikwik")) {
                        imgmaesrtocard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmaesrtocard);
                        imgmaesrtocard.setTag(card);
                        imgmaesrtocard.setImageResource(R.drawable.ic_mobi);
                        txtmaesrtocard = (TextView) view.findViewById(parentId).findViewById(R.id.txtmaesrtocard);
                        txtmaesrtocard.setText(getResources().getString(R.string.astroshop_mobiwik));

                    } else if (card.getCardName().equalsIgnoreCase("PayZapp")) {
                        imgmastercard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmastercard);
                        imgmastercard.setTag(card);
                        imgmastercard.setImageResource(R.drawable.ic_payzp);
                        txtmastercard = (TextView) view.findViewById(parentId).findViewById(R.id.txtmastercard);
                        txtmastercard.setText(getResources().getString(R.string.astroshop_payzapp));
                    }

                }

                spnrdrop.setVisibility(View.GONE);

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            imgselected = false;
            imgVisa.setBackgroundResource(R.drawable.circle_gray);
            imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);
            imgmastercard.setBackgroundResource(R.drawable.circle_gray);
            finalSelectedCard = cardList.get(position);
            Log.wtf("Card Selected", cardList.get(position).getCardName());
            if (finalSelectedCard == null) {
                mct.show(getResources().getString(R.string.astroshop_payment_selection));
            } else {
                genrateOrder(orderModel);
            }
        } else if (!imgselected) {
            finalSelectedCard = null;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void setTotalPayment(int id) {
        txtyopayrs = (TextView) view.findViewById(id).findViewById(R.id.txtyopayrs);
        bundle = this.getArguments();
        if (bundle != null) {
            if (!isFromCart) {
                itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");
                noOfItem = bundle.getInt("ItemNo");
            }

            //com.google.analytics.tracking.android.//Log.e("NOOFITEMSSSS" + noOfItem);
            try {
                Double priceDoller = roundFunction((Double.parseDouble(orderModel.getProductcost())), 2);
                Double priceRs = roundFunction((Double.parseDouble(orderModel.getProductcost_inrs())), 2);
                Double spriceDoller = roundFunction((Double.parseDouble(orderModel.getShippingcost())), 2);
                Double spriceRs = roundFunction((Double.parseDouble(orderModel.getShippingcost_in_rs())), 2);
                //   txtyopayrs.setText(getResources().getString(R.string.astroshop_dollar_sign) + (String.valueOf(priceDoller + spriceDoller)) + " / " + getString(R.string.astroshop_rupees_sign) + ((String.valueOf(priceRs + spriceRs))));
                txtyopayrs.setText(getResources().getString(R.string.astroshop_dollar_sign) + (roundFunction((priceDoller + spriceDoller), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + " " + ((roundFunction((priceRs + spriceRs), 2))));

            } catch (Exception e) {
                e.printStackTrace();
            }

            //  txtyopayrs.setText(getResources().getString(R.string.astroshop_dollar_sign) + ((roundFunction((Double.parseDouble(itemdetails.getPPriceInDoller()) * noOfItem), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + (roundFunction((Double.parseDouble(itemdetails.getPPriceInRs()) * noOfItem), 2))));
        }
    }

    private void setTotalPaymentForCashOn() {

        bundle = this.getArguments();
        if (bundle != null) {
            if (!isFromCart) {
                itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");
                noOfItem = bundle.getInt("ItemNo");
            }

            try {
                Double priceDoller = roundFunction((Double.parseDouble(orderModel.getProductcost())), 2);
                Double priceRs = roundFunction((Double.parseDouble(orderModel.getProductcost_inrs())), 2);
                Double spriceDoller = roundFunction((Double.parseDouble(orderModel.getShippingcost())), 2);
                Double spriceRs = roundFunction((Double.parseDouble(orderModel.getShippingcost_in_rs())), 2);
                txtyoupayrscashon.setText(getResources().getString(R.string.astroshop_dollar_sign) + (roundFunction((priceDoller + spriceDoller), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + " " + ((roundFunction((priceRs + spriceRs), 2))));
                txtchequeddcost.setText(getResources().getString(R.string.astroshop_dollar_sign) + (roundFunction((priceDoller + spriceDoller), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + " " + ((roundFunction((priceRs + spriceRs), 2))));

            } catch (Exception e) {
                e.printStackTrace();
            }
            //   txtyoupayrscashon.setText(getResources().getString(R.string.astroshop_dollar_sign) + ((roundFunction((Double.parseDouble(itemdetails.getPPriceInDoller()) * noOfItem), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + (roundFunction((Double.parseDouble(itemdetails.getPPriceInRs()) * noOfItem), 2))));
        }
    }

    private void setTotalPaymentForUpi() {

        bundle = this.getArguments();
        if (bundle != null) {
            if (!isFromCart) {
                itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");
                noOfItem = bundle.getInt("ItemNo");
            }

            try {
                Double priceDoller = roundFunction((Double.parseDouble(orderModel.getProductcost())), 2);
                Double priceRs = roundFunction((Double.parseDouble(orderModel.getProductcost_inrs())), 2);
                Double spriceDoller = roundFunction((Double.parseDouble(orderModel.getShippingcost())), 2);
                Double spriceRs = roundFunction((Double.parseDouble(orderModel.getShippingcost_in_rs())), 2);
                txtUpicost.setText(getResources().getString(R.string.astroshop_dollar_sign) + (roundFunction((priceDoller + spriceDoller), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + " " + ((roundFunction((priceRs + spriceRs), 2))));
                txtUpicost.setText(getResources().getString(R.string.astroshop_dollar_sign) + (roundFunction((priceDoller + spriceDoller), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + " " + ((roundFunction((priceRs + spriceRs), 2))));

            } catch (Exception e) {
                e.printStackTrace();
            }
            //   txtyoupayrscashon.setText(getResources().getString(R.string.astroshop_dollar_sign) + ((roundFunction((Double.parseDouble(itemdetails.getPPriceInDoller()) * noOfItem), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + (roundFunction((Double.parseDouble(itemdetails.getPPriceInRs()) * noOfItem), 2))));
        }
    }


    private void setTotalPaymentForChequeDD() {

        bundle = this.getArguments();
        if (bundle != null) {
            if (!isFromCart) {
                itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");

            }
            noOfItem = bundle.getInt("ItemNo");
            //com.google.analytics.tracking.android.//Log.e("NOOFITEMSSSS" + noOfItem);
            try {
                Double priceDoller = roundFunction((Double.parseDouble(orderModel.getProductcost())), 2);
                Double priceRs = roundFunction((Double.parseDouble(orderModel.getProductcost_inrs())), 2);
                Double spriceDoller = roundFunction((Double.parseDouble(orderModel.getShippingcost())), 2);
                Double spriceRs = roundFunction((Double.parseDouble(orderModel.getShippingcost_in_rs())), 2);
                txtchequeddcost.setText(getResources().getString(R.string.astroshop_dollar_sign) + (roundFunction((priceDoller + spriceDoller), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + " " + ((roundFunction((priceRs + spriceRs), 2))));

            } catch (Exception e) {
                e.printStackTrace();
            }

            //   txtyoupayrscashon.setText(getResources().getString(R.string.astroshop_dollar_sign) + ((roundFunction((Double.parseDouble(itemdetails.getPPriceInDoller()) * noOfItem), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + (roundFunction((Double.parseDouble(itemdetails.getPPriceInRs()) * noOfItem), 2))));
        }
    }


    private void genrateOrder(final ItemOrderModel localOrderModal) {
        pd = new CustomProgressDialog(getActivity(), typeface);
        pd.setCancelable(false);
        pd.show();

        String orderUrl = isFromCart ? CGlobalVariables.genrateOrderForCart : CGlobalVariables.genrateOrder;


        //CustomRequest cus=new CustomRequest();
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, orderUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //com.google.analytics.tracking.android.//Log.e("OrderResponse from server +" + response.toString());
                        Log.e("onResponse", response);
                        hideProgressBar();
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = array.getJSONObject(0);
                            String result = obj.getString("Result");
                            if (result.equalsIgnoreCase("1")) {
                                if (orderModel.getMakeitdefault().equalsIgnoreCase("1")) {
                                    CUtils.saveIsUserInShipping(getActivity(), false);
                                }
                                order_id = obj.getString("OrderId");
                                bundle.putString("order_Id", order_id);

                                if (payMode.equalsIgnoreCase("CCAvenue")) {
                                    postDataToAvenue();
                                } else if (payMode.equalsIgnoreCase("Paytm")) {
                                    postDataToPaytm();
                                } else if (payMode.equalsIgnoreCase("Razorpay")) {
                                    Double amount = Double.parseDouble(orderModel.getProductcost_inrs().trim());
                                    Double deliveryAmount = Double.parseDouble(orderModel.getShippingcost_in_rs().trim());
                                    Double totalAmt = amount + deliveryAmount;
                                    totalAmount = String.valueOf(totalAmt);
                                    Double paiseAmount = (totalAmt * 100);
                                    // API calling //commented by abhishek for desable autocapture
                                    //new VolleyServerRequest(getActivity(), (OnVolleyResultListener) FragAstroShopPayment.this, CGlobalVariables.RAZORPAY_ORDERID_URL, CUtils.getRazorOrderIdParams(paiseAmount,"INR",order_id));
                                    postDataToRazorPay();
                                } else if (payMode.equalsIgnoreCase("COD")) {
                                    if (isFromCart) {
                                        CUtils.setCartProduct(getActivity(), "");

                                    }
                                    Intent intent = new Intent(getActivity(), ActAstroshopCodDetails.class);
                                    startActivityForResult(intent, 1);
                                } else if (payMode.equalsIgnoreCase("cheque")) {

                                    if (isFromCart) {
                                        CUtils.setCartProduct(getActivity(), "");

                                    }
                                    Intent intent = new Intent(getActivity(), ActAstroShopChequeDdDetail.class);
                                    intent.putExtra("OrderId", order_id);
                                    intent.putExtra("Otype", "0");
                                    //Log.e("orderid", order_id);
                                    //    startActivity(intent);
                                    startActivityForResult(intent, 1);
                                }

                                //    new GetData().execute();

                            } else if (result.equalsIgnoreCase("5")) {
                                mct.show(getResources().getString(R.string.out_stock));

                            } else if (result.equalsIgnoreCase("6")) {

                                mct.show(getResources().getString(R.string.plan_id_not_match));
                            } else {
                                //Open only for payment byPass
                                /*
                                bundle.putString("order_Id", "325698774");
                                order_id= "325698774";
                                postDataToAvenue();*/

                                mct.show(getResources().getString(R.string.order_fail));

                            }
                        } catch (Exception e) {
                        }
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("VolleyError: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    error.printStackTrace();

                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                hideProgressBar();
            }
        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
//                key,name,emailid,address,city,landmark,state,country,paymode,prId,pincode
//                mobileno,prCatId,shippingcost,productcost,makeitdefault

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
                headers.put("name", localOrderModal.getName());

                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(getActivity())));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(getActivity())));

                //headers.put("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST);
                //headers.put("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST);

                headers.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(localOrderModal.getEmailid()));
                headers.put("address", localOrderModal.getAddress());
                headers.put("city", localOrderModal.getCity());
                headers.put("landmark", localOrderModal.getLandmark());
                headers.put("state", localOrderModal.getState());
                headers.put("country", localOrderModal.getCountry());
                headers.put("paymode", payMode);
                headers.put("pincode", localOrderModal.getPincode());
                headers.put("mobileno", localOrderModal.getMobileno());
                headers.put("deviceid", CUtils.getMyAndroidId(getActivity()));

                String kundli = CUtils.getKundliInfo(itemdetails, LANGUAGE_CODE);
                if (kundli != null)
                    headers.put("userbirthdetails", kundli);

                if (!isFromCart) {
                    headers.put("prCatId", localOrderModal.getPrCatId());
                    headers.put("prId", localOrderModal.getPrId());

                } else {
                    ArrayList<AstroShopItemDetails> list = CUtils.getCartProducts(getActivity());
                    ArrayList<ProductCategorymodal> list1 = new ArrayList<ProductCategorymodal>();
                    for (AstroShopItemDetails item : list) {
                        ProductCategorymodal modal = new ProductCategorymodal();
                        modal.setPId(item.getPId());
                        modal.setP_CatId(item.getP_CatId());
                        modal.setPPriceInDoller(item.getPPriceInDoller());
                        modal.setPPriceInRs(item.getPPriceInRs());
                        list1.add(modal);

                    }

                    headers.put("productList", new Gson().toJson(list1));
                }

//               headers.put("prCatId", "1");
//               headers.put("prId", "17");
                headers.put("productcostrs", localOrderModal.getProductcost_inrs());
                headers.put("shippingcost", localOrderModal.getShippingcost_in_rs());
                headers.put("productcost", localOrderModal.getProductcost());
                headers.put("makeitdefault", localOrderModal.getMakeitdefault());
                headers.put("shippingcostdlr", localOrderModal.getShippingcost());

                //    CardTypeDTO selectedCard = finalSelectedCard;
                if (finalSelectedCard != null && payMode.equalsIgnoreCase("CCAvenue")) {
                    headers.put("ccAvenueType", finalSelectedCard.getPayOptType());
                    //Log.e("Post Data",finalSelectedCard.getPayOptType());

                } else {
                    headers.put("ccAvenueType", "");

                }


                headers.put("ordersource", "AK_ANDROID");
                if (!TextUtils.isEmpty(appVerName)) {
                    headers.put("appversion", appVerName);
                }

                //Log.e("Request hit==", headers.toString());
                return headers;
            }

        };
        // Add the request to the RequestQueue.
        //com.google.analytics.tracking.android.//Log.e("API HIT HERE");
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    private void postDataToPaytm() {
        Double d = Double.parseDouble(orderModel.getProductcost_inrs()) + Double.parseDouble(orderModel.getShippingcost_in_rs());
        amount = String.valueOf(d);
        getCheckSumForProduct(CGlobalVariables.GET_CHECKSUM_FOR_PAYTM_URL);
        // CUtils.getCheckSumForProduct(getActivity(), getchecksumparams(), typeface);
    }

    private void postDataToAvenue() {

        String email_id = CUtils.getAstroshopUserEmail(getActivity());
        Intent intent = new Intent(getActivity(), ActWebViewActivity.class);
        if (!isFromCart) {
            intent.putExtra("key", itemdetails);
            intent.putExtra("ItemNo", noOfItem);
        }
        intent.putExtra(AvenuesParams.ACCESS_CODE, getResources().getString(R.string.access_code).trim());
        intent.putExtra(AvenuesParams.ORDER_ID, order_id.trim());
        intent.putExtra(AvenuesParams.BILLING_NAME, orderModel.getName().trim());
        intent.putExtra(AvenuesParams.BILLING_ADDRESS, orderModel.getAddress().trim());
        intent.putExtra(AvenuesParams.BILLING_COUNTRY, orderModel.getCountry().trim());
        intent.putExtra(AvenuesParams.BILLING_STATE, orderModel.getState().trim());
        intent.putExtra(AvenuesParams.BILLING_CITY, orderModel.getCity());
        intent.putExtra(AvenuesParams.BILLING_ZIP, orderModel.getPincode());
        intent.putExtra(AvenuesParams.BILLING_TEL, orderModel.getMobileno());
        intent.putExtra(AvenuesParams.BILLING_EMAIL, email_id);

        intent.putExtra(AvenuesParams.DELIVERY_NAME, orderModel.getName().trim());
        intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, orderModel.getAddress().trim());
        intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, orderModel.getCountry().trim());
        intent.putExtra(AvenuesParams.DELIVERY_STATE, orderModel.getState().trim());
        intent.putExtra(AvenuesParams.DELIVERY_CITY, orderModel.getCity());
        intent.putExtra(AvenuesParams.DELIVERY_ZIP, orderModel.getPincode());
        intent.putExtra(AvenuesParams.DELIVERY_TEL, orderModel.getMobileno());
        intent.putExtra(AvenuesParams.REDIRECT_URL,
                CGlobalVariables.avenueRedirectUrl);
        intent.putExtra(AvenuesParams.CANCEL_URL,
                CGlobalVariables.avenueRedirectUrl);
        intent.putExtra(AvenuesParams.RSA_KEY_URL, CGlobalVariables.avenueRsaUrl);


        intent.putExtra(
                AvenuesParams.CUSTOMER_IDENTIFIER, "".trim());
        intent.putExtra(
                AvenuesParams.CURRENCY, currency.trim());
        Double d = Double.parseDouble(orderModel.getProductcost_inrs()) + Double.parseDouble(orderModel.getShippingcost_in_rs());
        String amount = String.valueOf(d);
        intent.putExtra(AvenuesParams.AMOUNT, amount);
        intent.putExtra("Order_Model", orderModel);
        if (!isFromCart) {
            intent.putExtra("Key", itemdetails);

        }
        intent.putExtra("fromCart", isFromCart);

        //  startActivity(intent);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("Result recieve in frag" + requestCode, "Done" + resultCode);
        //Log.e("PaytmOrder", "resp data1=" + data);
        if (requestCode == 1 && resultCode == 1) {
            getActivity().finish();
        } else if (requestCode == 1 && resultCode == 2) {
            //getActivity().setResult(2);
            getActivity().finish();
        } else if (requestCode == CGlobalVariables.REQUEST_CODE_PAYTM && data != null) {
            try {
                String responseData = data.getStringExtra("response");
                //Log.e("PaytmOrder", "resp data=" + responseData);
                if (TextUtils.isEmpty(responseData)) {
                    processPaytmTransaction("TXN_FAILED");
                } else {
                    JSONObject respObj = new JSONObject(responseData);
                    String status = respObj.getString("STATUS");
                    processPaytmTransaction(status);
                }
            } catch (Exception e) {
                //
            }
        }

    }

    boolean cod_available = true;

    private void loadAstroShopPinCheck() {
        pd = new CustomProgressDialog(getActivity(), typeface);
        pd.setCancelable(false);
        pd.show();
        // Log.e("mytag", "onCreateView productId: "+orderModel.getPrId() );


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShoppincodecheckApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //com.google.analytics.tracking.android.

                        Log.e("PINSTATUS", response.toString());
                        if (response != null && !(response.isEmpty())) {
                            try {
                                JSONArray array = new JSONArray(response);
                                JSONObject obj = array.getJSONObject(0);
                                String stringresponse = obj.getString("Result");
                                if (stringresponse.equals("1")) {
                                    //   mct.show("Delivery Available");
                                    btn_pay_place_order.setVisibility(View.VISIBLE);
                                    //   genrateOrder(orderModel);
                                } else if (stringresponse.equals("0")) {
                                    //      mct.show(getResources().getString(R.string.astroshop_pin_not_available));
                                    llcodmsg.setVisibility(View.VISIBLE);
                                    btn_pay_place_order.setVisibility(View.GONE);
                                } else if (stringresponse.equals("6")) {
                                    radiocashondelivery.setChecked(false);
                                    radiocashondelivery.setEnabled(false);
                                    radiocreditcard.setChecked(true);
                                    cod_available = false;
                                    llcodmsg.setVisibility(View.VISIBLE);
                                    codLayout.setVisibility(View.GONE);
                                    btn_pay_place_order.setVisibility(View.GONE);
                                    txtcod_msg.setText(getResources().getString(R.string.cod_not_available));
                                    onCreditCardSelect();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                                    builder.setMessage(getResources().getString(R.string.cod_not_available))
                                            .setTitle("Alert!")
                                            .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());

                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                                //com.google.analytics.tracking.android.//Log.e("stringresponse  +" + stringresponse.toString());

                           /* JSONObject obj = new JSONObject(response);
                            String stringresponse = obj.getString("Sp_Price");
                            String inrs = obj.getString("Sp_RsPrice");
*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        hideProgressBar();

                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("Error: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                hideProgressBar();
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());
                }
                hideProgressBar();
            }

        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Key", CUtils.getApplicationSignatureHashCode(getActivity()));
                headers.put("paymode", payMode);
                headers.put("pincode", pinCode);
                headers.put("product_id", orderModel.getPrId());
                //com.google.analytics.tracking.android.//Log.e(headers.toString());
                return headers;
            }
        };

// Add the request to the RequestQueue.


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
        //   Log.e("urlCheck", "loadAstroShopPinCheck: "+ stringRequest.getUrl() );
    }

    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {

        Activity currentActivity = getActivity();
        if (currentActivity == null) return;

        if (callback == CUtils.callBack.GET_CHECKSUM) {
            String checksum = result;
            if (!result.isEmpty()) {
                startPaytmPayment(order_id, amount, checksum);
            } else {
                MyCustomToast mct = new MyCustomToast(currentActivity,
                        currentActivity.getLayoutInflater(),
                        currentActivity, typeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        } else if (callback == CUtils.callBack.POST_PRODUCT_RAZORPAYSTATUS) {
            payStatus = result;
            if (payStatus == null && payStatus.isEmpty()) {
                payStatus = "0";
            }
            if (payStatus.equalsIgnoreCase("1")) {
                status = "Transaction Successful!";
            } else {
                status = "Transaction Declined!";
            }
            Intent intent = new Intent(getActivity(), ActPaymentStatus.class);
            if (!isFromCart) {
                intent.putExtra("Key", itemdetails);

            }
            intent.putExtra(AvenuesParams.ORDER_ID, order_id);
            intent.putExtra("Status", status);
            intent.putExtra("Order_Model", orderModel);
            intent.putExtra("fromCart", isFromCart);
            startActivityForResult(intent, 1);

        }
    }

    @Override
    public void getCallBackForChat(String[] result, CUtils.callBack callback, String priceInDollor, String priceInRs) {

    }


    /*---------------------------------paytm payment-----------------------------*/

    public void onPurchaseCompleted(AstroShopItemDetails itemdetails, ItemOrderModel orderModel, String order_id) {

        //double price = (Double.valueOf(orderModel.getProductcost_inrs()));
        //double shipingCost = Double.valueOf(orderModel.getShippingcost_in_rs());

        String currency = orderModel.getCountry().equalsIgnoreCase("India") ? "INR" : "USD";
        if (isFromCart) {
            CUtils.trackEcommerceProduct(getActivity(), "Astro Shop", "Cart", orderModel.getProductcost_inrs(), order_id, currency, "Astro Shop", "In-App Store", orderModel.getShippingcost_in_rs(), CGlobalVariables.TOPIC_PRODUCTS);
        } else {
            CUtils.trackEcommerceProduct(getActivity(), itemdetails.getPId(), itemdetails.getPName(), orderModel.getProductcost_inrs(), order_id, currency, "Astro Shop", "In-App Store", orderModel.getShippingcost_in_rs(), CGlobalVariables.TOPIC_PRODUCTS);
        }
    }


    private void postPayStatus() {
        pd = new CustomProgressDialog(getActivity(), typeface);
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.sendPayStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = array.getJSONObject(0);
                            String result = obj.getString("Result");
                            if (result.equalsIgnoreCase("1")) {

                                Intent intent = new Intent(getActivity(), ActPaymentStatus.class);
                                if (!isFromCart) {
                                    intent.putExtra("Key", itemdetails);

                                }
                                intent.putExtra(AvenuesParams.ORDER_ID, order_id);
                                intent.putExtra("Status", status);
                                intent.putExtra("Order_Model", orderModel);
                                intent.putExtra("fromCart", isFromCart);
                                startActivityForResult(intent, 1);

                            } else {
                                mct.show(getResources().getString(R.string.order_fail));

                            }
                        } catch (Exception e) {

                        }

                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("VolleyError: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
            }

        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                /*key,amount,orderid,paycurr,status*/
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
                headers.put("amount", amount);
                headers.put("orderid", order_id);
                headers.put("paycurr", currency);
                headers.put("status", payStatus);
                //Log.e("headersPayment", headers.toString());
                return headers;
            }

        };
        // Add the request to the RequestQueue.
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }


    /**********************************************RazorPay*****************************************/
    private void postDataToRazorPay() {
        final Checkout co = new Checkout();
        co.setFullScreenDisable(true);
        try {
            JSONObject options = new JSONObject();
            options.put("name", "AstroSage");
            options.put("currency", "INR");
            Double amount = Double.parseDouble(orderModel.getProductcost_inrs().trim());
            Double deliveryAmount = Double.parseDouble(orderModel.getShippingcost_in_rs().trim());
            Double totalAmt = amount + deliveryAmount;
            totalAmount = String.valueOf(totalAmt);
            //Need to send amount to razor pay in paise
            Double paiseAmount = (totalAmt * 100);
            options.put("amount", paiseAmount);
            options.put("color", "#ff6f00");

            JSONObject preFill = new JSONObject();
            preFill.put("email", orderModel.getEmailid().trim());
            preFill.put("contact", orderModel.getMobileno().trim());
            options.put("prefill", preFill);
            //options.put("order_id", razorpayOrderId);

            JSONObject notes = new JSONObject();
            notes.put("orderId", order_id.trim());
            //added by Ankit on 17-5-2019 for Razorpay Webhook
            notes.put("chatId", "0");
            notes.put("orderType", CGlobalVariables.PAYMENT_TYPE_PRODUCT);
            notes.put("appVersion", BuildConfig.VERSION_NAME);
            notes.put("appName", BuildConfig.APPLICATION_ID);
            notes.put("firebaseinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAnalyticsAppInstanceId(getActivity()));
            notes.put("facebookinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFacebookAnalyticsAppInstanceId(getActivity()));
            options.put("notes", notes);

            co.open(getActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onPaymentSuccess(String razorpayPaymentID) {
        payStatus = "1";
        status = "Transaction Successful!";
        onPurchaseCompleted(itemdetails, orderModel, order_id);
        double dPrice = 0.0;
        String pricee = "";
        try {
            if (orderModel != null) {
                if (orderModel.getProductcost_inrs() != null && orderModel.getProductcost_inrs().length() > 0) {
                    pricee = orderModel.getProductcost_inrs();
                    dPrice = Double.valueOf(pricee);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        CUtils.googleAnalyticSendWitPlayServieForPurchased(getActivity(), CGlobalVariables.GOOGLE_ANALYTIC_PRODUCT,
                CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_SUCCESS, null, dPrice, "");
        if (isFromCart) {
            CUtils.setCartProduct(getActivity(), "");
        }

        CUtils.postProductRazorPayDetail(getActivity(), typeface, queue, order_id.trim(), payStatus, totalAmount, razorpayPaymentID, "");
    }

    public void onPaymentError(int i, String s) {
        payStatus = "0";
        status = "Transaction Declined!";
        CUtils.saveBooleanData(getContext(),CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
        CUtils.googleAnalyticSendWitPlayServie(getActivity(), CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
        CUtils.postProductRazorPayDetail(getActivity(), typeface, queue, order_id.trim(), payStatus, totalAmount, "", status);
    }

    /**
     * get CheckSum For Product
     */
    public void getCheckSumForProduct(String url) {

        showProgressBar();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hideProgressBar();
                            if (!TextUtils.isEmpty(response)) {
                                try {
                                    JSONObject Obj = new JSONObject(response);
                                    String checksum = Obj.getString("message");
                                    if (!TextUtils.isEmpty(checksum)) {
                                        getCallBack(checksum, CUtils.callBack.GET_CHECKSUM, "", "");
                                    } else {
                                        getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");
                                    }
                                } catch (Exception var7) {
                                    getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");
                                }
                            } else {
                                getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");
                            }
                        } catch (Exception e) {
                            getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");
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
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {

                String emailId = CUtils.getAstroshopUserEmail(getActivity());

                Map<String, String> params = new HashMap<>();
                params.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
                params.put("MID", "Ojasso36077880907527");
                params.put("ORDER_ID", order_id.trim());
                params.put("WEBSITE", "OjassoWAP");
                params.put("CALLBACK_URL", CGlobalVariables.CALLBACK_URL + order_id.trim());
                params.put("TXN_AMOUNT", amount);
                params.put("CUST_ID", emailId);
                String extraData = "firebaseinstanceid_"+ com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAnalyticsAppInstanceId(getActivity())+
                        "_facebookinstanceid_"+ com.ojassoft.astrosage.varta.utils.CUtils.getFacebookAnalyticsAppInstanceId(getActivity());
                params.put("MERC_UNQ_REF", extraData);

                return params;
            }

        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(getActivity(), typeface);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (getActivity() == null) return;
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            pd = null;
        }
    }

    @Override
    public void onVolleySuccess(String result, Cache cache) {
        try {
            hideProgressBar();
            //JSONObject jsonObject = new JSONObject(result);
            //postDataToRazorPay(jsonObject.optString("id")); //commented by abhishek for desable autocapture
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVolleyError(VolleyError result) {
        hideProgressBar();
    }

    /*************************************** Paytm Transaction ************************************************************/

    private void startPaytmPayment(String oId, String amount, String tnxToken) {

        String midString = CGlobalVariables.PAYTM_MID;
        String callBackUrl = CGlobalVariables.CALLBACK_URL + oId;

        PaytmOrder paytmOrder = new PaytmOrder(oId, midString, tnxToken, amount, callBackUrl);

        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {

            @Override
            public void onTransactionResponse(Bundle bundle) {
                try {
                    String status = bundle.getString("STATUS");
                    processPaytmTransaction(status);
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void networkNotAvailable() {

            }

            @Override
            public void onErrorProceed(String s) {

            }

            @Override
            public void clientAuthenticationFailed(String s) {

            }

            @Override
            public void someUIErrorOccurred(String s) {

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {

            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {

            }
        });
        //transactionManager.setAppInvokeEnabled(false);
        transactionManager.setShowPaymentUrl(CGlobalVariables.PAYTM_PAYMENT_URL);
        transactionManager.startTransaction(getActivity(), CGlobalVariables.REQUEST_CODE_PAYTM);
    }

    private void processPaytmTransaction(String statusStr) {
        if (statusStr.equals(CGlobalVariables.TXN_SUCCESS)) {
            payStatus = "1";
        } else {
            payStatus = "0";
        }
        if (payStatus.equalsIgnoreCase("1")) {
            status = "Transaction Successful!";
            onPurchaseCompleted(itemdetails, orderModel, order_id);

            double dPrice = 0.0;
            String pricee = "";
            try {
                if (orderModel != null) {
                    if (orderModel.getProductcost_inrs() != null && orderModel.getProductcost_inrs().length() > 0) {
                        pricee = orderModel.getProductcost_inrs();
                        dPrice = Double.valueOf(pricee);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            CUtils.googleAnalyticSendWitPlayServieForPurchased(getActivity(), CGlobalVariables.GOOGLE_ANALYTIC_PRODUCT,
                    CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_SUCCESS, null, dPrice, "");

            if (isFromCart) {
                CUtils.setCartProduct(getActivity(), "");
            }
        } else {
            status = "Transaction UnSuccessful!";
            CUtils.googleAnalyticSendWitPlayServie(getActivity(), CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

        }
        postPayStatus();
    }
}
