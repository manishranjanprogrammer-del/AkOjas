package com.ojassoft.astrosage.ui.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.ItemOrderModel;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CardSaveModel;
import com.ojassoft.astrosage.model.CardTypeDTO;
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
import com.ojassoft.astrosage.utils.Validator;

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
 * Created by ojas on १५/३/१६.
 */
public class FragAstroShopCardDetail extends Fragment implements View.OnClickListener {
    private View view;
    private Button btn_Proceed;
    private int frameId = R.id.frame_layout;
    private FragmentTransaction ft;
    private TextView tvAmount;
    private EditText etCard, etBank, etCVV;
    private Spinner spnMont, spnYear;
    private ArrayList<String> months;
    private ArrayList<String> years;
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
    private CardTypeDTO selectedCard;
    private TextInputLayout etCardnumber_layout, etCardCvv_layout;
    private Byte cardvalue;
    ArrayList<CardSaveModel> cardList;
    private CheckBox checkSave;
    private String cardName="";
    private String subPayOpt;
    private CustomProgressDialog pd = null;
    private RequestQueue queue;
    private Boolean isFromCart=false;
    String appVerName;
    /*

 MasterCard Debit Card
Visa Debit Card
Maestro Debit Card
Amex
Amex ezeClick
JCB
MasterCard
Visa
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.lay_frag_card_deatil, container, false);
        } /*else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }*/
        appVerName = LibCUtils.getApplicationVersionToShow(getActivity());

        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        setArrayItems();
        init(view);
        setTotalPayment();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay_complete:
                if(v.isPressed())
                {
                    if(etCard.getText().toString().length()>=13)
                    {
                        cardName=getCardName(etCard.getText().toString().trim());
                        cardvalue = selectCardInfo(cardName);
                        //Log.e("Selected card name",cardName);
                        //Log.e("Selected card value",""+cardvalue);
                    }
                    //   cardvalue = selectCardInfo(selectedCard.getCardName());
                    if (etCard.getText().toString().trim().isEmpty()) {
                        etCardnumber_layout.setError(getResources().getString(R.string.enter_valid_card));
                        requestFocus(etCard);
                    } else if (etCard.getText().toString().trim().length() <13) {
                        etCardnumber_layout.setError(getResources().getString(R.string.enter_valid_card));
                        requestFocus(etCard);
                    } else if (!Validator.validate(etCard.getText().toString().trim(), cardvalue)) {
                        //     etCard.setError(getResources().getString(R.string.enter_valid_card));
                        etCardnumber_layout.setError(getResources().getString(R.string.enter_valid_card));
                        requestFocus(etCard);
                    } else if (etCVV.getText().toString().trim().isEmpty() || etCVV.getText().toString().trim().length() < 3) {
                        // etCVV.setError(getResources().getString(R.string.enter_valid_cvv));
                        etCardCvv_layout.setError(getResources().getString(R.string.enter_valid_cvv));
                        requestFocus(etCVV);
                    } /*else if (etBank.getText().toString().trim().isEmpty() ||etBank.getText().toString().trim().length()<3 ) {

                }*/ else if (spnMont.getSelectedItem().toString().equalsIgnoreCase("mm")) {
                        mct.show(getResources().getString(R.string.enter_expiry_month));
                    } else if (spnYear.getSelectedItem().toString().equalsIgnoreCase("yyyy")) {

                        mct.show(getResources().getString(R.string.enter_expiry_year));
                    } else {
                        if(subPayOpt==null)
                            subPayOpt="";
                        if(subPayOpt.equalsIgnoreCase("OPTCRDC"))
                        {
                            order_id="";
                            selectedCard=new CardTypeDTO();
                            selectedCard.setCardName(cardName);
                            selectedCard.setPayOptType(subPayOpt);
                            selectedCard.setCardType("CRDC");
                            selectedCard.setDataAcceptedAt("CCAvenue");
                        }

                        else if(selectedCard==null)
                        {
                            selectedCard=new CardTypeDTO();
                            selectedCard.setCardName(cardName);
                            selectedCard.setPayOptType(subPayOpt);
                            if(subPayOpt.equalsIgnoreCase("OPTCRDC"))
                            {
                                selectedCard.setCardType("CRDC");

                            }
                            else
                            {
                                selectedCard.setCardType("DBCRD");
                                selectedCard.setStatus("nil");
                            }
                            selectedCard.setDataAcceptedAt("CCAvenue");
                        }
                        if(checkSave.isChecked())
                        {
                            saveCards(); //Save cards to prefrence after encryption

                        }
                        if(order_id==null || order_id.isEmpty())
                        {
                            genrateOrder(orderModel,selectedCard);

                        }
                        else
                        {
                            proceedToPay();
                        }

                    }
                }

                break;
        }

    }

    private void init(View v) {
        bundle = this.getArguments();
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();

        if (bundle != null) {
            isFromCart=bundle.getBoolean("fromCart",false);
            if(!isFromCart)
            {
                itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");
                noOfItem = bundle.getInt("ItemNo");
            }

            currency = bundle.getString("currency");
            order_id = bundle.getString("order_Id");
            orderModel = (ItemOrderModel) bundle.getSerializable("detail");
            selectedCard = (CardTypeDTO) bundle.getSerializable("selectedcard");
            subPayOpt= bundle.getString("Payment");
            if (currency.equalsIgnoreCase("INR")) {
                Double d = Double.parseDouble(orderModel.getProductcost_inrs()) + Double.parseDouble(orderModel.getShippingcost_in_rs());
                amount = String.valueOf(d);

            } else {
                Double d = Double.parseDouble(orderModel.getProductcost()) + Double.parseDouble(orderModel.getShippingcost());
                amount = String.valueOf(d);


            }
            //Log.e("==", currency);
            //Log.e("amo##", amount);
        }


        ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.SHIPPINGDONE);
        ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.PAYMENT_ENABLE);

        mct = new MyCustomToast(getActivity(), getActivity()
                .getLayoutInflater(), getActivity(), typeface);
        txtyoupay = (TextView) v.findViewById(R.id.txtyoupay);
        txtyoupay.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        tvAmount = (TextView) v.findViewById(R.id.tvAmount);
        checkSave=(CheckBox) v.findViewById(R.id.checkSave);
        checkSave.setTypeface(typeface);
        checkSave.setChecked(true);
        etCard = (EditText) v.findViewById(R.id.etCardnumber);
        etCard.addTextChangedListener(new MyTextWatcher(etCard));
        etCVV = (EditText) v.findViewById(R.id.etCardCvv);
        etCVV.addTextChangedListener(new MyTextWatcher(etCVV));
        etCardnumber_layout = (TextInputLayout) v.findViewById(R.id.etCardnumber_layout);
        etCardCvv_layout = (TextInputLayout) v.findViewById(R.id.etCardCvv_layout);

        etBank = (EditText) v.findViewById(R.id.etIssuingBank);
        spnMont = (Spinner) v.findViewById(R.id.spnMonth);
        spnYear = (Spinner) v.findViewById(R.id.spnYear);
        btn_Proceed = (Button) v.findViewById(R.id.btn_pay_complete);
        btn_Proceed.setOnClickListener(this);
//        spnMont.setPrompt("MM");
//        spnYear.setPrompt("YYYY");
        if(selectedCard!=null)
        cardvalue = selectCardInfo(selectedCard.getCardName());

        ArrayAdapter monAdapter = new ArrayAdapter(getActivity(), R.layout.spin_item, months) {
            @Override
            public int getCount() {
                return (months.size() - 1); // Truncate the list
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
        };

        ArrayAdapter yearAdapter = new ArrayAdapter(getActivity(), R.layout.spin_item, years) {
            @Override
            public int getCount() {
                return (years.size() - 1); // Truncate the list
            }


            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }

        };

        spnMont.setAdapter(monAdapter);
        monAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMont.setSelection(months.size() - 1);
        spnMont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CUtils.hideMyKeyboard(getActivity());

                if (position != months.size()) {
                    //  Toast.makeText(getActivity(), months.get(position), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnYear.setAdapter(yearAdapter);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setSelection(years.size() - 1);
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(view!=null)
                view.requestFocus();
                CUtils.hideMyKeyboard(getActivity());
                if (position != years.size()) {
                    //  Toast.makeText(getActivity(), years.get(position), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // new MyAsyncTask().execute("");
    }

    private void setArrayItems() {
        months = new ArrayList<String>();
        years = new ArrayList<String>();
        for (int i = 1; i < 13; i++) {
            if (i < 10)
                months.add("0" + String.valueOf(i));
            else
                months.add(String.valueOf(i));
        }
        months.add("MM");

        for (int i = 2017; i <= 2032; i++)
            years.add(String.valueOf(i));
        years.add("YYYY");
    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void setTotalPayment() {
        Double priceDoller = roundFunction((Double.parseDouble(orderModel.getProductcost())), 2);
        Double priceRs = roundFunction((Double.parseDouble(orderModel.getProductcost_inrs())), 2);
        Double spriceDoller = roundFunction((Double.parseDouble(orderModel.getShippingcost())), 2);
        Double spriceRs = roundFunction((Double.parseDouble(orderModel.getShippingcost_in_rs())), 2);
        tvAmount.setText(getResources().getString(R.string.astroshop_dollar_sign) + (String.valueOf(roundFunction((priceDoller + spriceDoller), 2))) + " / " + getString(R.string.astroshop_rupees_sign) + " "+((roundFunction((priceRs + spriceRs),2))));

        // tvAmount.setText(getResources().getString(R.string.astroshop_dollar_sign) + ((roundFunction((Double.parseDouble(itemdetails.getPPriceInDoller()) * noOfItem), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + (roundFunction((Double.parseDouble(itemdetails.getPPriceInRs()) * noOfItem), 2))));

    }

    private void proceedToPay() {

        if (selectedCard != null) {
            String email_id = CUtils.getAstroshopUserEmail(getActivity());
            Intent intent = new Intent(getActivity(), ActWebViewActivity.class);
            intent.putExtra(
                    AvenuesParams.ACCESS_CODE,
                    getResources().getString(R.string.access_code).trim());
            intent.putExtra(AvenuesParams.ORDER_ID, (order_id.trim()));
            intent.putExtra(AvenuesParams.BILLING_NAME, orderModel.getName().trim());
            intent.putExtra(AvenuesParams.BILLING_ADDRESS, orderModel.getAddress().trim());
            intent.putExtra(AvenuesParams.BILLING_COUNTRY, orderModel.getCountry().trim());
            intent.putExtra(AvenuesParams.BILLING_STATE, orderModel.getState().trim());
            intent.putExtra(AvenuesParams.BILLING_CITY, orderModel.getCity());
            intent.putExtra(AvenuesParams.BILLING_ZIP, orderModel.getPincode());
            intent.putExtra(AvenuesParams.BILLING_TEL, orderModel.getMobileno());
            intent.putExtra(AvenuesParams.BILLING_EMAIL, email_id);

           /* intent.putExtra(AvenuesParams.DELIVERY_NAME, orderModel.getName().trim());
            intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, orderModel.getAddress().trim());
            intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, orderModel.getCountry().trim());
            intent.putExtra(AvenuesParams.DELIVERY_STATE, orderModel.getState().trim());
            intent.putExtra(AvenuesParams.DELIVERY_CITY, orderModel.getCity());
            intent.putExtra(AvenuesParams.DELIVERY_ZIP, orderModel.getPincode());
            intent.putExtra(AvenuesParams.DELIVERY_TEL, orderModel.getMobileno());*/
            intent.putExtra(AvenuesParams.CVV, etCVV.getText().toString().trim());
            intent.putExtra(AvenuesParams.REDIRECT_URL,
                    CGlobalVariables.avenueRedirectUrl);
            intent.putExtra(AvenuesParams.CANCEL_URL,
                    CGlobalVariables.avenueRedirectUrl);
            intent.putExtra(AvenuesParams.RSA_KEY_URL, CGlobalVariables.avenueRsaUrl);

            intent.putExtra(AvenuesParams.PAYMENT_OPTION, selectedCard.getPayOptType());
            intent.putExtra(AvenuesParams.CARD_NUMBER, etCard.getText().toString().trim());
            intent.putExtra(AvenuesParams.EXPIRY_YEAR, spnYear.getSelectedItem().toString().trim());
            intent.putExtra(AvenuesParams.EXPIRY_MONTH, spnMont.getSelectedItem().toString().trim());
            intent.putExtra(AvenuesParams.ISSUING_BANK, etBank.getText().toString().trim());
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
            intent.putExtra(AvenuesParams.AMOUNT, amount.toString().trim());
            intent.putExtra("Order_Model", orderModel);
            if(!isFromCart)
            {
                intent.putExtra("Key", itemdetails);

            }
            intent.putExtra("fromCart", isFromCart);
            startActivityForResult(intent, 1);

        }
    }


    private byte selectCardInfo(String str) {
        Byte cardtype = 100;
        if (str.equalsIgnoreCase("MasterCard Debit Card") || str.equalsIgnoreCase("MasterCard")) {
            cardtype = 1;
        } else if (str.equalsIgnoreCase("Visa Debit Card") || str.equalsIgnoreCase("Visa")) {
            cardtype = 0;
        } else if (str.equalsIgnoreCase("Amex ezeClick") || str.equalsIgnoreCase("Amex")) {
            cardtype = 2;
        } else if (str.equalsIgnoreCase("JCB")) {
            cardtype = 7;
        } else {
            cardtype = 8;
        }
        return cardtype;
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etCardnumber:
                    //                    if (!Validator.validate(etCard.getText().toString().trim().length()<9, cardvalue)) {
                    if (etCard.getText().toString().trim().length()<13) {
                        etCardnumber_layout.setError(getResources().getString(R.string.enter_valid_card));
                        requestFocus(etCard);
                        etCard.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        etCardnumber_layout.setError(null);;
                        etCard.getBackground().setColorFilter(null);
                        etCard.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);


                    }
                    break;
                case R.id.etCardCvv:
                    if (etCVV.getText().toString().trim().isEmpty() || etCVV.getText().toString().trim().length() < 3) {
                        etCardCvv_layout.setError(getResources().getString(R.string.enter_valid_cvv));
                        requestFocus(etCVV);
                        etCVV.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        etCardCvv_layout.setError(null);
                        etCVV.getBackground().setColorFilter(null);
                        etCVV.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);



                    }
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("Result recieve in frag"+requestCode, "Done"+resultCode);

        if(requestCode == 1 && resultCode == 1) {
            getActivity().finish();
        }
        else{}

    }


    /*
     normalTextEnc = AESHelper.encrypt(seedValue, normalText);
                               String normalTextDec = AESHelper.decrypt(seedValue, normalTextEnc);
    * */


    private void saveCards()
    {
        String cardFromPrefs=CUtils.getSavedCards(getActivity());
        if(cardFromPrefs.isEmpty())
        {

            CardSaveModel modal=getCardToSave();
            cardList=new ArrayList<>();
            cardList.add(modal);
            String cardJson=new Gson().toJson(cardList);
            //Log.e("Without encrption",cardJson);
            try
            {
                String encryptedCards= AesHelper.encryptFromAES(CUtils.getApplicationSignatureHashCode(getActivity()),cardJson);
                //Log.e("After encrption",encryptedCards);
                CUtils.setSavedCards(getActivity(),encryptedCards);

            }

            catch(Exception e)
            {
                e.printStackTrace();
            }


        }
        else
        {
            try
            {
                String decryptedCards=AesHelper.decryptFromAES(CUtils.getApplicationSignatureHashCode(getActivity()),cardFromPrefs);
                cardList = (ArrayList<CardSaveModel>) new Gson().fromJson(decryptedCards,
                        new TypeToken<ArrayList<CardSaveModel>>() {
                        }.getType());
if(cardList.size()>0)
{
    for(CardSaveModel modal:cardList)
    {
        if(modal.getCardNumber().equalsIgnoreCase(etCard.getText().toString().trim()))
        {
            mct.show(getResources().getString(R.string.astro_save_card));
            return;
        }
    }
    CardSaveModel modal1=getCardToSave();
   /* if(cardList.size()>=CGlobalVariables.MAXCARDSAVE)
    {
       // cardList.set(CGlobalVariables.MAXCARDSAVE-1,modal1);
        cardList.remove(0);
        cardList.add(modal1);

    }
    else
    {*/
        cardList.add(modal1);
       // Toast.makeText(getActivity(),"Size is"+cardList.size(),Toast.LENGTH_LONG).show();

  //  }

    String cardJson=new Gson().toJson(cardList);
    String encryptedCards= AesHelper.encryptFromAES(CUtils.getApplicationSignatureHashCode(getActivity()),cardJson);
    //Log.e("After encrption",encryptedCards);
    CUtils.setSavedCards(getActivity(),encryptedCards);

}


            }
            catch(Exception e)
            {
                //Log.e("Error",e.getMessage());
                e.printStackTrace();
            }
        }
    }

   private CardSaveModel getCardToSave()
   {
       CardSaveModel modal=new CardSaveModel();
       modal.setCardName(selectedCard.getCardName());
       modal.setCardType(selectedCard.getCardType());
       modal.setPayOptType(selectedCard.getPayOptType());
       modal.setDataAcceptedAt(selectedCard.getDataAcceptedAt());
       modal.setStatusMessage(selectedCard.getStatus());
       modal.setCardNumber(etCard.getText().toString().trim());
       modal.setExpMonth(spnMont.getSelectedItem().toString().trim());
       modal.setExpYear(spnYear.getSelectedItem().toString().trim());
       modal.setBankName(etBank.getText().toString().trim());
       modal.setEmail(orderModel.getEmailid());
       modal.setCurrency(currency);
       return modal;

   }


    private String getCardName(final String credCardNumber)
    {
        String creditCard = credCardNumber.trim();

        if (creditCard.length() >= 13 && creditCard.length() <= 16
                && creditCard.startsWith("4")) {
            return "Visa";
        }

        else if (creditCard.length() == 16) {
            int prefix = Integer.parseInt(creditCard.substring(0, 2));
            if (prefix >= 51 && prefix <= 55) {
                return "MasterCard";
            }
        }

        else   if (creditCard.length() == 15
                && (creditCard.startsWith("34") || creditCard
                .startsWith("37"))) {
            return "Amex";
        }
        else  if ((creditCard.length() == 16 && creditCard.startsWith("3"))
                || (creditCard.length() == 15 && (creditCard
                .startsWith("2131") || creditCard
                .startsWith("1800")))) {
            return "JCB";
        }
        return "";
    }






    private void genrateOrder(final ItemOrderModel localOrderModal,final CardTypeDTO card) {
        pd = new CustomProgressDialog(getActivity(), typeface);
        pd.setCancelable(false);
        pd.show();

        String orderUrl=isFromCart?CGlobalVariables.genrateOrderForCart:CGlobalVariables.genrateOrder;

        //CustomRequest cus=new CustomRequest();
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, orderUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
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
                                proceedToPay();

                                //    new GetData().execute();

                            } else if (result.equalsIgnoreCase("5")) {
                                mct.show(getResources().getString(R.string.out_stock));

                            }/*else if (result.equalsIgnoreCase("6")) {

                                mct.show(getResources().getString(R.string.plan_id_not_match));
                            }*/
                            else {
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

                String kundli=CUtils.getKundliInfo(itemdetails,LANGUAGE_CODE);
                if(kundli!=null)
                    headers.put("userbirthdetails",kundli);
                if(!isFromCart)
                {
                    headers.put("prCatId", localOrderModal.getPrCatId());
                    headers.put("prId", localOrderModal.getPrId());
                }
                else
                {
                    ArrayList<AstroShopItemDetails> list=CUtils.getCartProducts(getActivity());
                    ArrayList<ProductCategorymodal> list1=new ArrayList<ProductCategorymodal>();
                    for(AstroShopItemDetails item:list)
                    {
                        ProductCategorymodal modal=new ProductCategorymodal();
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

                headers.put("ccAvenueType",card.getPayOptType());
                headers.put("ordersource", "AK_ANDROID");
                if (!TextUtils.isEmpty(appVerName)) {
                    headers.put("appversion", appVerName);
                }
                //Log.e("Post Data",card.getPayOptType());
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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }



}
