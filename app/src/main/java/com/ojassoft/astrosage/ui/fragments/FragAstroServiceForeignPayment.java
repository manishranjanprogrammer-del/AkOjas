package com.ojassoft.astrosage.ui.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.PayOptAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.CardTypeDTO;
import com.ojassoft.astrosage.model.PaySubOption;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.act.ActWebViewActivity;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
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

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;

/**
 * Created by ojas on २९/३/१६.
 */
public class FragAstroServiceForeignPayment extends Fragment implements View.OnClickListener {
    private Bundle bundle;
    private View view;
    private Spinner paySpn;
    PaySubOption pay;
    ArrayList<CardTypeDTO> cardList;
    private Button payProceed;
    private FragmentTransaction ft;
    private CardTypeDTO selectedCard;
    public ServicelistModal itemdetails;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    public int noOfItem = 1;
    TextView txtyoupaycashon, txtyoupayrscashon,tvpayMessage;
    String currency = "INR";
    String order_id = "";
    private AstrologerServiceInfo orderModel;
    private CustomProgressDialog pd = null;
    MyCustomToast mct;
    private RequestQueue queue;

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
            pay = (PaySubOption) bundle.getSerializable("PayOPtions");
            itemdetails = (ServicelistModal) bundle.getSerializable("Key");
            currency = bundle.getString("currency");
            orderModel = (AstrologerServiceInfo) bundle.getSerializable("detail");
            //Log.e("==", currency);
        }
        paySpn = (Spinner) v.findViewById(R.id.spnPay);
        payProceed = (Button) v.findViewById(R.id.btn_pay_proceed);
        payProceed.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        payProceed.setVisibility(View.GONE);
        txtyoupayrscashon = (TextView) v.findViewById(R.id.txtyoupayrscashon);
        txtyoupaycashon = (TextView) v.findViewById(R.id.txtyoupaycashon);
        txtyoupaycashon.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        tvpayMessage= (TextView) v.findViewById(R.id.tvpayMessage);
        tvpayMessage.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        Double priceDoller=roundFunction((Double.parseDouble(itemdetails.getPriceInDollor()) ), 2);
        Double priceRs=roundFunction((Double.parseDouble(itemdetails.getPriceInRS()) ), 2);
        txtyoupayrscashon.setText(getResources().getString(R.string.astroshop_dollar_sign) + (String.valueOf(roundFunction((priceDoller),2))) + " / " + getString(R.string.astroshop_rupees_sign) +" "+ ((roundFunction((priceRs),2))));
        PayOptAdapter adapter = null;
        JSONObject job = null;
        Gson gson = new Gson();
        String str = "";
        JsonElement element = null;
        JsonArray array = null;

        str = pay.getCardsList();
        element = gson.fromJson(str, JsonElement.class);
        array = element.getAsJsonArray();
        cardList = (ArrayList<CardTypeDTO>) gson.fromJson(str,
                new TypeToken<ArrayList<CardTypeDTO>>() {
                }.getType());
        CardTypeDTO cardtype = new CardTypeDTO();
        cardtype.setCardName(getResources().getString(R.string.astroshop_select));
        cardList.add(0, cardtype);
        adapter = new PayOptAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item, cardList);

        paySpn.setAdapter(adapter);
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
        });
        payProceed.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_pay_proceed:


                if (selectedCard == null) {
                    mct.show(getResources().getString(R.string.astroshop_payment_selection));
                } else {
                    genrateOrder(orderModel);
                }

                break;
        }

    }


    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private void genrateOrder(final AstrologerServiceInfo localOrderModal) {
        pd = new CustomProgressDialog(getActivity(), typeface);
        pd.setCancelable(false);
        pd.show();


        //CustomRequest cus=new CustomRequest();
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.genrateServiceOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //com.google.analytics.tracking.android.//Log.e("OrderResponse from server +" + response.toString());
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = array.getJSONObject(0);
                            String result = obj.getString("Result");
                            if (result.equalsIgnoreCase("1")) {
                                order_id = obj.getString("OrderId");
                                bundle.putString("order_Id", order_id);

                                         postDataToAvenue();


                                //    new GetData().execute();

                            } else if (result.equalsIgnoreCase("5")) {
                                mct.show(getResources().getString(R.string.out_stock));

                            } else {
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
            /*    key (*)
                regName (*)
                emailID (*)
                gender (*)
                dateOfBirth (*)
                monthOfBirth (*)
                yearOfBirth (*)
                minOfBirth (*)
                hourOfBirth (*)
                place (*)
                nearCity
                state (*)
                country (*)
                problem (*)
                serviceId (*)
                profileId
                price (*)
                priceRs (*)
                payMode (*)
                mobileNo
                        knowDOB
                knowTOB
                        ccAvenueType*/


                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
                headers.put("regName", localOrderModal.getRegName());
                headers.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(localOrderModal.getEmailID()));
                headers.put("gender", localOrderModal.getGender());
                headers.put("dateOfBirth", localOrderModal.getDateOfBirth());
                headers.put("monthOfBirth", localOrderModal.getMonthOfBirth());
                headers.put("yearOfBirth", localOrderModal.getYearOfBirth());
                headers.put("minOfBirth", localOrderModal.getMinOfBirth());
                headers.put("paymode", "CCAvenue");
                headers.put("hourOfBirth", localOrderModal.getHourOfBirth());
                headers.put("place", localOrderModal.getPlace());
                headers.put("nearCity", localOrderModal.getNearCity());
                headers.put("state", localOrderModal.getState());
                headers.put("country",localOrderModal.getCountry());
                headers.put("problem", localOrderModal.getProblem());
                headers.put("serviceId", localOrderModal.getServiceId());
           //     headers.put("serviceId", "3");
                headers.put("profileId", localOrderModal.getProfileId());
                headers.put("price", localOrderModal.getPrice());
                headers.put("priceRs", localOrderModal.getPriceRs());
                headers.put("mobileNo", localOrderModal.getMobileNo());
                headers.put("knowDOB", localOrderModal.getKnowDOB());
                headers.put("knowTOB", localOrderModal.getKnowTOB());
                if(selectedCard!=null)
                {
                    headers.put("ccAvenueType",selectedCard.getPayOptType());

                }
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


    private void postDataToAvenue() {
        if (selectedCard != null) {
            if (selectedCard.getDataAcceptedAt().equalsIgnoreCase("CCAvenue")) {
                Fragment frag_astroshop_card = new FragAstroServiceCardDetail();
                if (bundle != null) {
                    bundle.putSerializable("Key", itemdetails);
                    bundle.putInt("ItemNo", noOfItem);
                    bundle.putSerializable("selectedcard", selectedCard);
                    bundle.putString("currency", currency);
                    bundle.putString("order_id", order_id);
                    bundle.putSerializable("detail", orderModel);

                    frag_astroshop_card.setArguments(bundle);
                }
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                ft.replace(R.id.frame_layout, frag_astroshop_card).commit();
            } else {
                String email_id = CUtils.getAstroshopUserEmail(getActivity());
                Intent intent = new Intent(getActivity(), ActWebViewActivity.class);
                intent.putExtra("key", itemdetails);
                intent.putExtra("ItemNo", noOfItem);
                intent.putExtra(
                        AvenuesParams.ACCESS_CODE,
                        getResources().getString(R.string.access_code).trim());
                intent.putExtra(AvenuesParams.ORDER_ID, order_id.trim());
                intent.putExtra(AvenuesParams.BILLING_NAME, orderModel.getRegName().trim());
                intent.putExtra(AvenuesParams.BILLING_ADDRESS, orderModel.getBillingAddress().trim());
                intent.putExtra(AvenuesParams.BILLING_COUNTRY, orderModel.getBillingCountry().trim());
                intent.putExtra(AvenuesParams.BILLING_STATE, orderModel.getBillingState().trim());
                intent.putExtra(AvenuesParams.BILLING_CITY, orderModel.getBillingCity());
                intent.putExtra(AvenuesParams.BILLING_ZIP,orderModel.getPincode());
                intent.putExtra(AvenuesParams.BILLING_TEL, orderModel.getMobileNo());
                intent.putExtra(AvenuesParams.BILLING_EMAIL, orderModel.getEmailID());
              /*  intent.putExtra(AvenuesParams.DELIVERY_NAME, orderModel.getName().trim());
                intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, orderModel.getAddress().trim());
                intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, orderModel.getCountry().trim());
                intent.putExtra(AvenuesParams.DELIVERY_STATE, orderModel.getState().trim());
                intent.putExtra(AvenuesParams.DELIVERY_CITY, orderModel.getCity());
                intent.putExtra(AvenuesParams.DELIVERY_ZIP, orderModel.getPincode());
                intent.putExtra(AvenuesParams.DELIVERY_TEL, orderModel.getMobileno());*/
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
                Double d = Double.parseDouble(itemdetails.getPriceInDollor());
                String amount = String.valueOf(d);
                intent.putExtra(AvenuesParams.AMOUNT, amount);

                intent.putExtra("Order_Model", orderModel);
                intent.putExtra("Key", itemdetails);
                startActivityForResult(intent,1);
                //  startActivity(intent);
            }


        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("Result foreignfrag"+requestCode, "Done"+resultCode);

        if(requestCode == 1 && resultCode == 1) {
            getActivity().setResult(1);
            getActivity().finish();
        }

    }

}
