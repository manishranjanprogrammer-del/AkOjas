package com.ojassoft.astrosage.ui.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.ItemOrderModel;
import com.ojassoft.astrosage.customadapters.SaveCardAdapter;
import com.ojassoft.astrosage.jinterface.IHandleSavedCards;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CardSaveModel;
import com.ojassoft.astrosage.model.Payoption;
import com.ojassoft.astrosage.model.ProductCategorymodal;
import com.ojassoft.astrosage.ui.act.ActAstroShopShippingDetails;
import com.ojassoft.astrosage.ui.act.ActWebViewActivity;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.AesHelper;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;

/**
 * Created by ojas on ३०/६/१६.
 */
public class FragAstroSavedCards extends Fragment implements IHandleSavedCards {

    private View view;
    private int frameId = R.id.frame_layout;
    private FragmentTransaction ft;
    private TextView tvAmount, tvOptoins, txtMsg;
    public AstroShopItemDetails itemdetails;
    public int noOfItem = 1;
    private Bundle bundle;
    private String currency = "INR";
    private String amount = "0";
    private String order_id = "";
    private ItemOrderModel orderModel;
    MyCustomToast mct;
    private Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private TextView txtyoupay;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CardSaveModel> cardList;
    private RecyclerView my_recycler_view;
    public RecyclerView.Adapter mAdapter;
    public Payoption globalPayOptions;
    private CustomProgressDialog pd = null;
    private RequestQueue queue;
    private Boolean isFromCart = false;
    String appVerName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.frag_showsave_cards, container, false);
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        appVerName = LibCUtils.getApplicationVersionToShow(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();

        typeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();

        //   setArrayItems();
        init(view);

        getSaveCards();
        setVisible(cardList);
        setTotalPayment();

        return view;
    }


    private void init(View v) {

        my_recycler_view = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        my_recycler_view.setVisibility(View.VISIBLE);
        my_recycler_view.setHasFixedSize(true);
        tvOptoins = (TextView) v.findViewById(R.id.tvOptoins);
        tvOptoins.setTypeface(typeface);
        mLayoutManager = new LinearLayoutManager(getActivity());
        bundle = this.getArguments();
        if (bundle != null) {
            isFromCart = bundle.getBoolean("fromCart", false);
            if (!isFromCart) {
                itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");

            }
            orderModel = (ItemOrderModel) bundle.getSerializable("detail");
            noOfItem = bundle.getInt("ItemNo");
            currency = bundle.getString("currency");
            order_id = bundle.getString("order_Id");
            globalPayOptions = (Payoption) bundle.getSerializable("Payoption");
            //     selectedCard = (CardTypeDTO) bundle.getSerializable("selectedcard");
            if (currency.equalsIgnoreCase("INR")) {
                Double d = Double.parseDouble(orderModel.getProductcost_inrs()) + Double.parseDouble(orderModel.getShippingcost_in_rs());
                amount = String.valueOf(d);

            } else {
                Double d = Double.parseDouble(orderModel.getProductcost()) + Double.parseDouble(orderModel.getShippingcost());
                amount = String.valueOf(d);
            }

            mct = new MyCustomToast(getActivity(), getActivity()
                    .getLayoutInflater(), getActivity(), typeface);
            txtyoupay = (TextView) v.findViewById(R.id.txtyoupay);
            txtyoupay.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
            tvAmount = (TextView) v.findViewById(R.id.tvAmount);
            txtMsg = (TextView) v.findViewById(R.id.txtMsg);
            txtMsg.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);

            //Log.e("==", currency);
            //Log.e("amo##", amount);

            ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.SHIPPINGDONE);
            ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.PAYMENT_ENABLE);

        }

        tvOptoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currency.equalsIgnoreCase("INR")) {
                    Fragment frag_astroshop_pay = new FragAstroShopPayment();
                    if (bundle != null && globalPayOptions != null && globalPayOptions.getPayOptions().get(0) != null) {
                        if (!isFromCart) {
                            bundle.putSerializable("Key", itemdetails);
                            bundle.putInt("ItemNo", noOfItem);

                        }
                        bundle.putSerializable("Payoption", globalPayOptions);
                        bundle.putString("currency", currency);
                        frag_astroshop_pay.setArguments(bundle);
                    }
                    ft = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                    ft.replace(frameId, frag_astroshop_pay, "FragAstroShopPayment").commit();

                    String title = "";
                    CUtils.googleAnalyticSendWitPlayServieForAstroshop(getActivity(),
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING,
                            title, null);
                    //com.google.analytics.tracking.android.//Log.e("GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING" + title);
                } else {
                    Fragment frag_astroshop = new FragAstroShopForeignPayment();
                    if (bundle != null && globalPayOptions != null && globalPayOptions.getPayOptions().get(0) != null) {
                        if (!isFromCart) {
                            bundle.putSerializable("Key", itemdetails);
                            bundle.putInt("ItemNo", noOfItem);

                        }
                        bundle.putSerializable("PayOPtions", globalPayOptions.getPayOptions().get(0));
                        bundle.putString("currency", currency);
                        frag_astroshop.setArguments(bundle);
                        String title = "";
                        CUtils.googleAnalyticSendWitPlayServieForAstroshop(getActivity(),
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING,
                                title, null);
                        //com.google.analytics.tracking.android.//Log.e("GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING" + title);
                    }
                    //getHostFragmentManager()
                    //     ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                    ft.replace(frameId, frag_astroshop, "FragAstroShopForeignPayment").addToBackStack(null).commit();
                }

            }
        });
    }


    private void proceedToPay(CardSaveModel card, String cvv) {

        if (orderModel != null && card != null) {
            String email_id = orderModel.getEmailid();
            Intent intent = new Intent(getActivity(), ActWebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, getResources().getString(R.string.access_code).trim());
            intent.putExtra(AvenuesParams.ORDER_ID, (order_id.trim()));
            intent.putExtra(AvenuesParams.BILLING_NAME, orderModel.getName().trim());
            intent.putExtra(AvenuesParams.BILLING_ADDRESS, orderModel.getAddress().trim());
            intent.putExtra(AvenuesParams.BILLING_COUNTRY, orderModel.getCountry().trim());
            intent.putExtra(AvenuesParams.BILLING_STATE, orderModel.getState().trim());
            intent.putExtra(AvenuesParams.BILLING_CITY, orderModel.getCity());
            intent.putExtra(AvenuesParams.BILLING_ZIP, orderModel.getPincode());
            intent.putExtra(AvenuesParams.BILLING_TEL, orderModel.getMobileno());
            intent.putExtra(AvenuesParams.BILLING_EMAIL, email_id);

        /*    intent.putExtra(AvenuesParams.DELIVERY_NAME, orderModel.getName().trim());
            intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, orderModel.getAddress().trim());
            intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, orderModel.getCountry().trim());
            intent.putExtra(AvenuesParams.DELIVERY_STATE, orderModel.getState().trim());
            intent.putExtra(AvenuesParams.DELIVERY_CITY, orderModel.getCity());
            intent.putExtra(AvenuesParams.DELIVERY_ZIP, orderModel.getPincode());
            intent.putExtra(AvenuesParams.DELIVERY_TEL, orderModel.getMobileno());*/
            intent.putExtra(AvenuesParams.CVV, cvv.trim());
            intent.putExtra(AvenuesParams.REDIRECT_URL,
                    CGlobalVariables.avenueRedirectUrl);
            intent.putExtra(AvenuesParams.CANCEL_URL,
                    CGlobalVariables.avenueRedirectUrl);
            intent.putExtra(AvenuesParams.RSA_KEY_URL, CGlobalVariables.avenueRsaUrl);
            intent.putExtra(AvenuesParams.PAYMENT_OPTION, card.getPayOptType());
            intent.putExtra(AvenuesParams.CARD_NUMBER, card.getCardNumber().trim());
            intent.putExtra(AvenuesParams.EXPIRY_YEAR, card.getExpYear().trim());
            intent.putExtra(AvenuesParams.EXPIRY_MONTH, card.getExpMonth().trim());
            intent.putExtra(AvenuesParams.ISSUING_BANK, card.getBankName().trim());
            intent.putExtra(AvenuesParams.CARD_TYPE,
                    card.getCardType());
            intent.putExtra(AvenuesParams.CARD_NAME,
                    card.getCardName());
            intent.putExtra(
                    AvenuesParams.DATA_ACCEPTED_AT,
                    card.getDataAcceptedAt() != null ? (card
                            .getDataAcceptedAt().equals("CCAvenue") ? "Y"
                            : "N") : null);
            intent.putExtra(
                    AvenuesParams.CUSTOMER_IDENTIFIER, "".trim());
            intent.putExtra(
                    AvenuesParams.CURRENCY, currency.trim());
            intent.putExtra(AvenuesParams.AMOUNT, amount.toString().trim());
            intent.putExtra("Order_Model", orderModel);
            if (!isFromCart) {
                intent.putExtra("Key", itemdetails);

            }
            intent.putExtra("fromCart", isFromCart);
            startActivityForResult(intent, 1);

        }
    }


    private void setTotalPayment() {
        Double priceDoller = roundFunction((Double.parseDouble(orderModel.getProductcost())), 2);
        Double priceRs = roundFunction((Double.parseDouble(orderModel.getProductcost_inrs())), 2);
        Double spriceDoller = roundFunction((Double.parseDouble(orderModel.getShippingcost())), 2);
        Double spriceRs = roundFunction((Double.parseDouble(orderModel.getShippingcost_in_rs())), 2);
        tvAmount.setText(getResources().getString(R.string.astroshop_dollar_sign) + (String.valueOf(roundFunction((priceDoller + spriceDoller), 2))) + " / " + getString(R.string.astroshop_rupees_sign) + ((roundFunction((priceRs + spriceRs), 2))));

        // tvAmount.setText(getResources().getString(R.string.astroshop_dollar_sign) + ((roundFunction((Double.parseDouble(itemdetails.getPPriceInDoller()) * noOfItem), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + (roundFunction((Double.parseDouble(itemdetails.getPPriceInRs()) * noOfItem), 2))));

    }


    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void getSaveCards() {
        String cardFromPrefs = CUtils.getSavedCards(getActivity());
        String decryptedCards = AesHelper.decryptFromAES(CUtils.getApplicationSignatureHashCode(getActivity()), cardFromPrefs);
        cardList = (ArrayList<CardSaveModel>) new Gson().fromJson(decryptedCards,
                new TypeToken<ArrayList<CardSaveModel>>() {
                }.getType());
        if (cardList != null) {
            mAdapter = new SaveCardAdapter(getActivity(), cardList, typeface, this, 0);
            my_recycler_view.setAdapter(mAdapter);
            my_recycler_view.setLayoutManager(mLayoutManager);
        }


    }

    @Override
    public void deleteCard(int position) {
        cardList.remove(position);
        mAdapter.notifyDataSetChanged();
        saveData(cardList);
        setVisible(cardList);
    }

    @Override
    public void ProceedPay(String cvv, int position) {

        //Log.e("event", "recieved");

        genrateOrder(orderModel, cardList.get(position), cvv);

    }

    private void saveData(ArrayList<CardSaveModel> list) {
        if (list.size() > 0) {
            try {
                String cardJson = new Gson().toJson(cardList);
                String encryptedCards = AesHelper.encryptFromAES(CUtils.getApplicationSignatureHashCode(getActivity()), cardJson);
                //Log.e("After encrption", encryptedCards);
                CUtils.setSavedCards(getActivity(), encryptedCards);
            } catch (Exception e) {

            }

        } else {
            CUtils.setSavedCards(getActivity(), "");

        }
    }


    private void genrateOrder(final ItemOrderModel localOrderModal, final CardSaveModel card, final String cvv) {
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
                                proceedToPay(card, cvv);


                                //    new GetData().execute();

                            } else if (result.equalsIgnoreCase("5")) {
                                mct.show(getResources().getString(R.string.out_stock));

                            }/*else if (result.equalsIgnoreCase("6")) {

                                mct.show(getResources().getString(R.string.plan_id_not_match));
                            }*/ else {
                                mct.show(getResources().getString(R.string.order_fail));

                            }
                        } catch (Exception e) {
                        }
                        pd.dismiss();

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
                pd.dismiss();
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
                headers.put("paymode", "CCAvenue");
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
                headers.put("productcostrs", localOrderModal.getProductcost_inrs());
                headers.put("shippingcost", localOrderModal.getShippingcost_in_rs());
                headers.put("productcost", localOrderModal.getProductcost());
                headers.put("makeitdefault", localOrderModal.getMakeitdefault());
                headers.put("shippingcostdlr", localOrderModal.getShippingcost());

                //    CardTypeDTO selectedCard = finalSelectedCard;

                headers.put("ccAvenueType", card.getPayOptType());
                headers.put("ordersource", "AK_ANDROID");
                if (!TextUtils.isEmpty(appVerName)) {
                    headers.put("appversion", appVerName);
                }
                //Log.e("Post Data", card.getPayOptType());


                //com.google.analytics.tracking.android.//Log.e("Request hit==" + headers.toString());
                return headers;
            }

        };
        ;
// Add the request to the RequestQueue.
        //com.google.analytics.tracking.android.//Log.e("API HIT HERE");
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    private void setVisible(ArrayList<CardSaveModel> cards) {
        if (cardList != null && cards.size() == 0) {
            txtMsg.setVisibility(View.VISIBLE);

        } else {
            txtMsg.setVisibility(View.GONE);

        }
    }


}
