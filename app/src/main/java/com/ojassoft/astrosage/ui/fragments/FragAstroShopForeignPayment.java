package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ojassoft.astrosage.model.ProductCategorymodal;
import com.ojassoft.astrosage.networkcall.OnVolleyResultListener;
import com.ojassoft.astrosage.ui.act.ActAstroShopShippingDetails;
import com.ojassoft.astrosage.ui.act.ActPaymentStatus;
import com.ojassoft.astrosage.ui.act.ActWebViewActivity;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.razorpay.Checkout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas on २९/३/१६.
 */
public class FragAstroShopForeignPayment extends Fragment implements View.OnClickListener, IAskCallback, OnVolleyResultListener {
    private Bundle bundle;
    private View view;
    private Spinner paySpn;
    //PaySubOption pay;
    ArrayList<CardTypeDTO> cardList;
    private Button payProceed;
    private FragmentTransaction ft;
    private CardTypeDTO selectedCard;
    public AstroShopItemDetails itemdetails;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    public int noOfItem = 1;
    TextView txtyoupaycashon, txtyoupayrscashon, tvpayMessage;
    String currency = "INR";
    String order_id = "";
    private ItemOrderModel orderModel;
    private CustomProgressDialog pd = null;
    MyCustomToast mct;
    private RequestQueue queue;
    private String title;
    private Boolean isFromCart = false;
    private String payStatus = "";
    private String status = "";
    private String totalAmount;
    String appVerName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.lay_astro_shop_foreign_payment, container, false);
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        appVerName = LibCUtils.getApplicationVersionToShow(getActivity());

        init(view);

        return view;
    }

    private void init(View v) {
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        mct = new MyCustomToast(getActivity(), getActivity()
                .getLayoutInflater(), getActivity(), typeface);
        bundle = this.getArguments();
        if (bundle != null) {
            isFromCart = bundle.getBoolean("fromCart", false);
            //Changed by Amit Sharma
            //pay = ((Payoption) bundle.getSerializable("Payoption")).getPayOptions().get(0);
            if (!isFromCart) {
                itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");
                noOfItem = bundle.getInt("ItemNo");
            }

            currency = bundle.getString("currency");
            //     order_id=bundle.getString("order_Id");
            orderModel = (ItemOrderModel) bundle.getSerializable("detail");

            //Log.e("==", currency);
            //  //Log.e("order_id",order_id);

            //   //Log.e("NOOFITEMSSSS" + noOfItem);
        }
        paySpn = (Spinner) v.findViewById(R.id.spnPay);
        payProceed = (Button) v.findViewById(R.id.btn_pay_proceed);
        payProceed.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        //payProceed.setVisibility(View.GONE);
        txtyoupayrscashon = (TextView) v.findViewById(R.id.txtyoupayrscashon);
        txtyoupaycashon = (TextView) v.findViewById(R.id.txtyoupaycashon);
        txtyoupaycashon.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        tvpayMessage = (TextView) v.findViewById(R.id.tvpayMessage);
        tvpayMessage.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        Double priceDoller = roundFunction((Double.parseDouble(orderModel.getProductcost())), 2);
        Double priceRs = roundFunction((Double.parseDouble(orderModel.getProductcost_inrs())), 2);
        Double spriceDoller = roundFunction((Double.parseDouble(orderModel.getShippingcost())), 2);
        Double spriceRs = roundFunction((Double.parseDouble(orderModel.getShippingcost_in_rs())), 2);
        txtyoupayrscashon.setText(getResources().getString(R.string.astroshop_dollar_sign) + (String.valueOf(roundFunction((priceDoller + spriceDoller), 2))) + " / " + getString(R.string.astroshop_rupees_sign) + " " + ((roundFunction((priceRs + spriceRs), 2))));
        PayOptAdapter adapter = null;
        JSONObject job = null;
        Gson gson = new Gson();
        String str = "";
        //JsonElement element = null;
        JsonArray array = null;

        //str = pay.getCardsList();
        //element = gson.fromJson(str, JsonElement.class);
        //array = element.getAsJsonArray();
   /*     cardList = (ArrayList<CardTypeDTO>) gson.fromJson(str,
                new TypeToken<ArrayList<CardTypeDTO>>() {
                }.getType());
        CardTypeDTO cardtype = new CardTypeDTO();
        cardtype.setCardName(getResources().getString(R.string.astroshop_select));
        cardList.add(0, cardtype);
        adapter = new PayOptAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item, cardList);

        paySpn.setAdapter(adapter);
        paySpn.setSelection(0);

        paySpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    selectedCard = cardList.get(position);
                    if (selectedCard == null) {
                        mct.show(getResources().getString(R.string.astroshop_payment_selection));
                    } else {
                        genrateOrder(orderModel);
                    }
                } else {
                    selectedCard = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        payProceed.setOnClickListener(this);


        ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.SHIPPINGDONE);
        ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.PAYMENT_ENABLE);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_pay_proceed:
                /*if (selectedCard == null) {
                    mct.show(getResources().getString(R.string.astroshop_payment_selection));
                } else {
                    genrateOrder(orderModel);
                }*/
                genrateOrder(orderModel);

                break;
        }

    }


    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private void genrateOrder(final ItemOrderModel localOrderModal) {
        pd = new CustomProgressDialog(getActivity(), typeface);
        pd.show();
        pd.setCancelable(false);

        String orderUrl = isFromCart ? CGlobalVariables.genrateOrderForCart : CGlobalVariables.genrateOrder;

        //CustomRequest cus=new CustomRequest();
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, orderUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  //com.google.analytics.tracking.android.//Log.e("OrderResponse  +" + response.toString());
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
                                //postDataToAvenue();
                                Double amount = Double.parseDouble(orderModel.getProductcost_inrs().trim());
                                Double deliveryAmount = Double.parseDouble(orderModel.getShippingcost_in_rs().trim());
                                Double totalAmt = amount + deliveryAmount;
                                totalAmount = String.valueOf(totalAmt);
                                Double paiseAmount = (totalAmt * 100);
                                // API calling
                                //new VolleyServerRequest(getActivity(), (OnVolleyResultListener) FragAstroShopForeignPayment.this, CGlobalVariables.RAZORPAY_ORDERID_URL, CUtils.getRazorOrderIdParams(paiseAmount,"INR",order_id));
                                postDataToRazorPay();
                                String title = "";
                                title = getItemName();

                                CUtils.googleAnalyticSendWitPlayServieForAstroshop(getActivity(),
                                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_PAYMENT,
                                        title, null);
                                String labell = CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_PAYMENT + "_" + title;
                                CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                                //com.google.analytics.tracking.android.//Log.e("GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_PAYMENT" + title);
                            } else if (result.equalsIgnoreCase("5")) {
                                mct.show(getResources().getString(R.string.out_stock));

                            }/*else if (result.equalsIgnoreCase("6")) {

                                mct.show(getResources().getString(R.string.plan_id_not_match));
                            }*/ else {
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

                headers.put(CGlobalVariables.KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(getActivity())));

                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(getActivity())));
                //headers.put("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST);
                //headers.put("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST);
                headers.put(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(localOrderModal.getEmailid()));

                headers.put("address", localOrderModal.getAddress());
                headers.put("city", localOrderModal.getCity());
                headers.put("landmark", localOrderModal.getLandmark());
                headers.put("state", localOrderModal.getState());
                headers.put("country", localOrderModal.getCountry());
                headers.put("paymode", "Razorpay");
                //   headers.put("prId", localOrderModal.getPrId());
                headers.put("pincode", localOrderModal.getPincode());
                headers.put("mobileno", localOrderModal.getMobileno());
                //   headers.put("prCatId", localOrderModal.getPrCatId());
                headers.put("productcostrs", localOrderModal.getProductcost_inrs());
                headers.put("shippingcost", localOrderModal.getShippingcost_in_rs());
                headers.put("productcost", localOrderModal.getProductcost());
                headers.put("makeitdefault", localOrderModal.getMakeitdefault());
                headers.put("shippingcostdlr", localOrderModal.getShippingcost());
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

                if (selectedCard != null) {
                    headers.put("ccAvenueType", selectedCard.getPayOptType());
                }
                headers.put("ordersource", "AK_ANDROID");
                if (!TextUtils.isEmpty(appVerName)) {
                    headers.put("appversion", appVerName);
                }
                //com.google.analytics.tracking.android.//Log.e("Request hit==" + headers.toString());
                return headers;
            }

        };
        ;
// Add the request to the RequestQueue.
        //com.google.analytics.tracking.android.//Log.e("API HIT HERE");
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }
    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if(getActivity() == null) return;
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
    private String getItemName() {
        if (itemdetails != null) {

            title = itemdetails.getPName();
        }

        return title;

    }


    private void postDataToAvenue() {
        if (selectedCard != null) {
            if (selectedCard.getDataAcceptedAt().equalsIgnoreCase("CCAvenue")) {
                Fragment frag_astroshop_card = new FragAstroShopCardDetail();
                if (bundle != null) {
                    if (!isFromCart) {
                        bundle.putSerializable("Key", itemdetails);

                    }
                    bundle.putInt("ItemNo", noOfItem);
                    bundle.putSerializable("selectedcard", selectedCard);
                    bundle.putString("currency", currency);
                    bundle.putString("order_id", order_id);
                    bundle.putSerializable("detail", orderModel);
                    bundle.putString("Payment", "OPTCRDC");

                    frag_astroshop_card.setArguments(bundle);
                }
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                paySpn.setSelection(0);

                ft.replace(R.id.frame_layout, frag_astroshop_card).addToBackStack(null).commit();
            } else {
                String email_id = CUtils.getAstroshopUserEmail(getActivity());
                Intent intent = new Intent(getActivity(), ActWebViewActivity.class);
                if (!isFromCart) {
                    intent.putExtra("key", itemdetails);
                    intent.putExtra("ItemNo", noOfItem);
                }

                intent.putExtra(
                        AvenuesParams.ACCESS_CODE,
                        getResources().getString(R.string.access_code).trim());
                intent.putExtra(AvenuesParams.ORDER_ID, order_id);
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
                intent.putExtra(AvenuesParams.CVV, "");
                intent.putExtra(AvenuesParams.REDIRECT_URL,
                        CGlobalVariables.avenueRedirectUrl);
                intent.putExtra(AvenuesParams.CANCEL_URL,
                        CGlobalVariables.avenueRedirectUrl);
                intent.putExtra(AvenuesParams.RSA_KEY_URL, CGlobalVariables.avenueRsaUrl);
                intent.putExtra(AvenuesParams.PAYMENT_OPTION, selectedCard.getPayOptType());
                intent.putExtra(AvenuesParams.CARD_NUMBER, "".trim());
                intent.putExtra(AvenuesParams.EXPIRY_YEAR, "".trim());
                intent.putExtra(AvenuesParams.EXPIRY_MONTH, "".trim());
                intent.putExtra(AvenuesParams.ISSUING_BANK, "".trim());
                intent.putExtra(AvenuesParams.CARD_TYPE,
                        selectedCard.getCardType());
                intent.putExtra(AvenuesParams.CARD_NAME,
                        selectedCard.getCardName());
                intent.putExtra(
                        AvenuesParams.DATA_ACCEPTED_AT,
                        selectedCard.getDataAcceptedAt() != null ? (selectedCard
                                .getDataAcceptedAt().equals("CCAvenue") ? "Y"
                                : "N") : null);
                intent.putExtra(
                        AvenuesParams.CUSTOMER_IDENTIFIER, "".trim());
                intent.putExtra(
                        AvenuesParams.CURRENCY, currency.trim());
                Double d = Double.parseDouble(orderModel.getProductcost()) + Double.parseDouble(orderModel.getShippingcost());
                String amount = String.valueOf(d);
                intent.putExtra(AvenuesParams.AMOUNT, amount);
                //     //Log.e("Item" + itemdetails.getPPriceInDoller(), "Shipping" + orderModel.getShippingcost());
                intent.putExtra("Order_Model", orderModel);
                if (!isFromCart) {
                    intent.putExtra("Key", itemdetails);

                }
                intent.putExtra("fromCart", isFromCart);

                startActivityForResult(intent, 1);
                //  startActivity(intent);
            }


        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("Result foreignfrag" + requestCode, "Done" + resultCode);

        if (requestCode == 1 && resultCode == 1) {
            getActivity().finish();
        }

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
            //added by Ankit on 17-5-2019 for Razorpay Webhook
            notes.put("orderId", order_id.trim());
            notes.put("chatId", "");
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

    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {

        Activity currentActivity = getActivity();
        if (currentActivity == null) return;

        if (callback == CUtils.callBack.POST_PRODUCT_RAZORPAYSTATUS) {

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

    public void onPaymentSuccess(String razorpayPaymentID) {
        payStatus = "1";
        status = "Transaction Successful!";
        onPurchaseCompleted(itemdetails, orderModel, order_id);

        double dPrice = 0.0;
        try {
            if(totalAmount != null && totalAmount.length()>0)
            {
                dPrice = Double.valueOf(totalAmount);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        CUtils.googleAnalyticSendWitPlayServieForPurchased(getActivity(), CGlobalVariables.GOOGLE_ANALYTIC_PRODUCT,
                CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_SUCCESS, null,dPrice,"");
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

    public void onPurchaseCompleted(AstroShopItemDetails itemdetails, ItemOrderModel orderModel, String order_id) {

        //double price = (Double.valueOf(orderModel.getProductcost_inrs()));
        //double shipingCost = Double.valueOf(orderModel.getShippingcost_in_rs());

        if (isFromCart) {
            CUtils.trackEcommerceProduct(getActivity(), "Astro Shop", "Cart", orderModel.getProductcost_inrs(), order_id, "INR", "Astro Shop", "In-App Store", orderModel.getShippingcost_in_rs(), CGlobalVariables.TOPIC_PRODUCTS);
        } else {
            CUtils.trackEcommerceProduct(getActivity(), itemdetails.getPId(), itemdetails.getPName(), orderModel.getProductcost_inrs(), order_id, "INR", "Astro Shop", "In-App Store", orderModel.getShippingcost_in_rs(), CGlobalVariables.TOPIC_PRODUCTS);
        }
    }

    @Override
    public void onVolleySuccess(String result, Cache cache) {
        try {
            hideProgressBar();
            //JSONObject jsonObject = new JSONObject(result);
            //postDataToRazorPay(jsonObject.optString("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVolleyError(VolleyError result) {
        hideProgressBar();
    }
}
