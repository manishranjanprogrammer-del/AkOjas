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
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.CardTypeDTO;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.act.ActPaymentWebView;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by ojas on १५/३/१६.
 */
public class FragAstroServiceCardDetail extends Fragment implements View.OnClickListener {
    private View view;
    private Button btn_Proceed;
    private int frameId = R.id.frame_layout;
    private FragmentTransaction ft;
    private TextView tvAmount;
    private EditText etCard, etBank, etCVV;
    private Spinner spnMont, spnYear;
    private ArrayList<String> months;
    private ArrayList<String> years;
    public ServicelistModal itemdetails;
    private Bundle bundle;
    private String currency = "INR";
    private String amount = "0";
    private String order_id = "";
    private AstrologerServiceInfo orderModel;
    MyCustomToast mct;
    private Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private TextView txtyoupay;
    private CardTypeDTO selectedCard = null;
    private TextInputLayout etCardnumber_layout, etCardCvv_layout;
    private Byte cardvalue;
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
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
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
                //   cardvalue = selectCardInfo(selectedCard.getCardName());
               /* if (etCard.getText().toString().trim().isEmpty()) {
                    etCard.setError(getResources().getString(R.string.astro_shop_User_card_no));
                } else if (etCard.getText().toString().trim().length() != 16) {
                    etCard.setError(getResources().getString(R.string.enter_valid_card));
                } else*/
                if (!Validator.validate(etCard.getText().toString().trim(), cardvalue)) {
                    //     etCard.setError(getResources().getString(R.string.enter_valid_card));
                    etCardnumber_layout.setError(getResources().getString(R.string.enter_valid_card));
                    requestFocus(etCard);
                } else if (etCVV.getText().toString().trim().isEmpty() || etCVV.getText().toString().trim().length() < 3) {
                    //  etCVV.setError(getResources().getString(R.string.enter_valid_cvv));
                    etCardCvv_layout.setError(getResources().getString(R.string.enter_valid_cvv));
                    requestFocus(etCVV);
                } /*else if (etBank.getText().toString().trim().isEmpty() ||etBank.getText().toString().trim().length()<3 ) {

                }*/ else if (spnMont.getSelectedItem().toString().equalsIgnoreCase("mm")) {
                    mct.show(getResources().getString(R.string.enter_expiry_month));
                } else if (spnYear.getSelectedItem().toString().equalsIgnoreCase("yyyy")) {

                    mct.show(getResources().getString(R.string.enter_expiry_year));
                } else {
                    proceedToPay();
                }
                break;
        }

    }

    private void init(View v) {
        bundle = this.getArguments();
        if (bundle != null) {
            itemdetails = (ServicelistModal) bundle.getSerializable("Key");
            currency = bundle.getString("currency");
            order_id = bundle.getString("order_id");
            orderModel = (AstrologerServiceInfo) bundle.getSerializable("detail");
            selectedCard = (CardTypeDTO) bundle.getSerializable("selectedcard");
            if (currency.equalsIgnoreCase("USD")) {
                Double d = Double.parseDouble(itemdetails.getPriceInDollor());
                amount = String.valueOf(d);

            } else {
                Double d = Double.parseDouble(itemdetails.getPriceInRS());
                amount = String.valueOf(d);;

            }
            //Log.e("==", currency);
            //Log.e("amo##", amount);
        }
        mct = new MyCustomToast(getActivity(), getActivity()
                .getLayoutInflater(), getActivity(), typeface);
        txtyoupay = (TextView) v.findViewById(R.id.txtyoupay);
        txtyoupay.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        tvAmount = (TextView) v.findViewById(R.id.tvAmount);
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
        for (int i = 1; i < 12; i++) {
            if (i < 10)
                months.add("0" + String.valueOf(i));
            else
                months.add(String.valueOf(i));
        }
        months.add("MM");

        for (int i = 2016; i <= 2032; i++)
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
        Double priceDoller = roundFunction((Double.parseDouble(itemdetails.getPriceInDollor())), 2);
        Double priceRs = roundFunction((Double.parseDouble(itemdetails.getPriceInRS())), 2);
        tvAmount.setText(getResources().getString(R.string.astroshop_dollar_sign) + (String.valueOf(roundFunction((priceDoller ), 2))) + " / " + getString(R.string.astroshop_rupees_sign) + " "+((roundFunction((priceRs),2))));

        // tvAmount.setText(getResources().getString(R.string.astroshop_dollar_sign) + ((roundFunction((Double.parseDouble(itemdetails.getPPriceInDoller()) * noOfItem), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + (roundFunction((Double.parseDouble(itemdetails.getPPriceInRs()) * noOfItem), 2))));

    }

    private void proceedToPay() {

        if (selectedCard != null) {
            String email_id = CUtils.getAstroshopUserEmail(getActivity());
            Intent intent = new Intent(getActivity(), ActPaymentWebView.class);
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

         /*   intent.putExtra(AvenuesParams.DELIVERY_NAME, orderModel.getName().trim());
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
            intent.putExtra("Key", itemdetails);
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
                    if (!Validator.validate(etCard.getText().toString().trim(), cardvalue)) {
                        etCardnumber_layout.setError(getResources().getString(R.string.enter_valid_card));
                        requestFocus(etCard);
                        etCard.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        etCardnumber_layout.setErrorEnabled(false);
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
                        etCardCvv_layout.setErrorEnabled(false);
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

    }



}
