package com.ojassoft.astrosage.ui.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.PayOptAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.CardTypeDTO;
import com.ojassoft.astrosage.model.PaySubOption;
import com.ojassoft.astrosage.model.Payoption;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.act.ActAstroShopChequeDdDetail;
import com.ojassoft.astrosage.ui.act.ActPaymentWebView;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;

/**
 * Created by ojas on ८/३/१६.
 */
public class FragAstroServicePayment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private RadioButton radiocreditcard, radiodebitcard, radionetbanking,radiocheque;
    private View view;
    private LinearLayout layout_creditcard, layout_debitditcard, layout_net_banking, layout_cheque_dd;
    private ImageView imgVisa, imgmaesrtocard, imgmastercard;
    private TextView txtvisa, txtmaesrtocard, txtmastercard;
    private Spinner spnrdrop;
    private TextView txtyopayrs, txtyoupayrscashon, txtcashondeliverylimit;
    private Button btn_pay_proceed;
    private Payoption payOpt;
    ArrayList<CardTypeDTO> cardList;
    ArrayList<CardTypeDTO> topCard;
    private int parentId;
    private FragmentTransaction ft;
    private Bundle bundle;
    public ServicelistModal itemdetails;
    public int noOfItem = 1;
    private CardTypeDTO finalSelectedCard;
    private PaySubOption subPayOpt;
    private String currency = "INR";
    private String order_id = "";
    private AstrologerServiceInfo orderModel;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    private TextView tvpayMessage;
    private CustomProgressDialog pd = null;
    private boolean imgselected = false;
    MyCustomToast mct;
    private TextView txtchequedd, txtchequeddcost;
    private Button btn_pay_place_order_cheque_dd;
    private String payMode = "CCAvenue";
    private TextView txtcreditcard, txtdebitcard, txtnetbanking, txtcheque;
    private JSONObject fullAddress;
    private RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.lay_frag_service_payment, container, false);
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

        }
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        try {

            fullAddress = new JSONObject(CUtils.getAstroshopUserAddressDetailInPrefs(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListener(R.id.inccredit);

        init(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //txtcreditcard,txtdebitcard,txtnetbanking,txtcheque,txtcashondelivery;

            case R.id.radiocreditcard:
            case R.id.txtcreditcard:
                payMode = "CCAvenue";
                setListener(R.id.inccredit);
                radiocreditcard.setChecked(true);
                radiodebitcard.setChecked(false);
                radionetbanking.setChecked(false);
                radiocheque.setChecked(false);

                layout_creditcard.setVisibility(View.VISIBLE);
                layout_debitditcard.setVisibility(View.GONE);
                layout_net_banking.setVisibility(View.GONE);
                layout_cheque_dd.setVisibility(View.GONE);
                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);

                populatePayOptions(R.id.radiocreditcard, R.id.inccredit);
                setTotalPayment(parentId);
                break;
            case R.id.radiodebitcard:
            case R.id.txtdebitcard:
                payMode = "CCAvenue";
                setListener(R.id.incdebit);
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(true);
                radionetbanking.setChecked(false);
                radiocheque.setChecked(false);

                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.VISIBLE);
                layout_net_banking.setVisibility(View.GONE);
                layout_cheque_dd.setVisibility(View.GONE);


                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);
                populatePayOptions(R.id.radiodebitcard, R.id.incdebit);
                setTotalPayment(parentId);

                break;
            case R.id.radionetbanking:
            case R.id.txtnetbanking:
                payMode = "CCAvenue";
                setListener(R.id.incnetbanking);
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(false);
                radionetbanking.setChecked(true);
                radiocheque.setChecked(false);
                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.GONE);
                layout_net_banking.setVisibility(View.VISIBLE);

                layout_cheque_dd.setVisibility(View.GONE);

                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);
                populatePayOptions(R.id.radionetbanking, R.id.incnetbanking);
                setTotalPayment(parentId);
                break;
            case R.id.radiowallet:
                setListener(R.id.incnet_wallet);
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(false);
                radionetbanking.setChecked(false);
                radiocheque.setChecked(false);

                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.GONE);
                layout_net_banking.setVisibility(View.GONE);
                layout_cheque_dd.setVisibility(View.GONE);
                imgmastercard.setBackgroundResource(R.drawable.circle_gray);
                imgVisa.setBackgroundResource(R.drawable.circle_gray);
                imgmaesrtocard.setBackgroundResource(R.drawable.circle_gray);
                break;
            case R.id.radiocheque:
            case R.id.txtcheque:
                payMode = "cheque";
                radiocreditcard.setChecked(false);
                radiodebitcard.setChecked(false);
                radionetbanking.setChecked(false);
                radiocheque.setChecked(true);

                layout_creditcard.setVisibility(View.GONE);
                layout_debitditcard.setVisibility(View.GONE);
                layout_net_banking.setVisibility(View.GONE);
                layout_cheque_dd.setVisibility(View.VISIBLE);
                setTotalPaymentForChequeDD();
                btn_pay_place_order_cheque_dd.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_pay_proceed:
            case R.id.btn_pay_place_order:
              /*  if (finalSelectedCard == null) {
                    mct.show(getResources().getString(R.string.astroshop_payment_selection));
                } else {
                    genrateOrder(orderModel);
                }
*/
               genrateOrder(orderModel);
                break;
            case R.id.btn_pay_place_order_cheque_dd:
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
            payOpt = (Payoption) bundle.getSerializable("Payoption");
            itemdetails = (ServicelistModal) bundle.getSerializable("Key");
            currency = bundle.getString("currency");
            orderModel = (AstrologerServiceInfo) bundle.getSerializable("detail");
            //Log.e("==", "" + currency);
            //Log.e("recieved", "" + order_id);

        }


        txtcreditcard = (TextView) v.findViewById(R.id.txtcreditcard);
        txtdebitcard = (TextView) v.findViewById(R.id.txtdebitcard);
        txtnetbanking = (TextView) v.findViewById(R.id.txtnetbanking);
        txtcheque = (TextView) v.findViewById(R.id.txtcheque);

        //  txtcashondeliverylimit.setTypeface(((BaseInputActivity) getContext()).regularTypeface);
        txtcreditcard.setOnClickListener(this);
        txtdebitcard.setOnClickListener(this);
        txtnetbanking.setOnClickListener(this);
        txtcheque.setOnClickListener(this);


        tvpayMessage = (TextView) v.findViewById(R.id.tvpayMessage);
        tvpayMessage.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        radiocreditcard = (RadioButton) v.findViewById(R.id.radiocreditcard);
        radiocreditcard.setTypeface(typeface);
        radiodebitcard = (RadioButton) v.findViewById(R.id.radiodebitcard);
        radiodebitcard.setTypeface(typeface);
        radionetbanking = (RadioButton) v.findViewById(R.id.radionetbanking);
        radionetbanking.setTypeface(typeface);
        radiocheque = (RadioButton) v.findViewById(R.id.radiocheque);
        radiocheque.setTypeface(typeface);
        layout_creditcard = (LinearLayout) v.findViewById(R.id.layout_creditcard);
        layout_debitditcard = (LinearLayout) v.findViewById(R.id.layout_debitditcard);
        layout_net_banking = (LinearLayout) v.findViewById(R.id.layout_net_banking);
        layout_cheque_dd = (LinearLayout) v.findViewById(R.id.layout_cheque_dd);


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


        btn_pay_place_order_cheque_dd = (Button) v.findViewById(R.id.btn_pay_place_order_cheque_dd);
        btn_pay_place_order_cheque_dd.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        btn_pay_place_order_cheque_dd.setOnClickListener(this);
        btn_pay_place_order_cheque_dd.setVisibility(View.GONE);



        radiocreditcard.setOnClickListener(this);
        radiodebitcard.setOnClickListener(this);
        radionetbanking.setOnClickListener(this);
        radiocheque.setOnClickListener(this);
        btn_pay_proceed.setOnClickListener(this);
        spnrdrop.setOnItemSelectedListener(this);

        layout_creditcard.setVisibility(View.GONE);
        layout_debitditcard.setVisibility(View.GONE);
        layout_net_banking.setVisibility(View.GONE);
        layout_cheque_dd.setVisibility(View.GONE);
        List<PaySubOption> payOptionslist = payOpt.getPayOptions();
        for (PaySubOption pay : payOptionslist) {
            if (pay.getPayOpt().equalsIgnoreCase("OPTCRDC")) {
                radiocreditcard.setTag(pay);
            }
            if (pay.getPayOpt().equalsIgnoreCase("OPTDBCRD")) {
                radiodebitcard.setTag(pay);

            }
            if (pay.getPayOpt().equalsIgnoreCase("OPTNBK")) {
                radionetbanking.setTag(pay);

            }
        }


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
                    if (card.getCardName().equalsIgnoreCase("Visa Debit Card")) {
                        imgVisa = (ImageView) view.findViewById(parentId).findViewById(R.id.imgVisa);
                        imgVisa.setTag(card);
                        imgVisa.setImageResource(R.drawable.ic_visa);
                        txtvisa.setText(R.string.visa);


                    } else if (card.getCardName().equalsIgnoreCase("Maestro Debit Card")) {
                        imgmaesrtocard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmaesrtocard);
                        imgmaesrtocard.setTag(card);
                        imgmaesrtocard.setImageResource(R.drawable.ic_mestrocard);
                        txtmaesrtocard.setText(R.string.maestro);

                    } else if (card.getCardName().equalsIgnoreCase("MasterCard Debit Card")) {
                        imgmastercard = (ImageView) view.findViewById(parentId).findViewById(R.id.imgmastercard);
                        imgmastercard.setTag(card);
                        imgmastercard.setImageResource(R.drawable.ic_mastercard);
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
                adapter.notifyDataSetChanged();
                spnrdrop.setAdapter(adapter);
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
 {
                Double priceDoller = roundFunction((Double.parseDouble(itemdetails.getPriceInDollor()) ), 2);
                Double priceRs = roundFunction((Double.parseDouble(itemdetails.getPriceInRS()) ), 2);
                txtyopayrs.setText(getResources().getString(R.string.astroshop_dollar_sign) + (roundFunction((priceDoller), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + " "+((roundFunction((priceRs), 2))));

            //  txtyopayrs.setText(getResources().getString(R.string.astroshop_dollar_sign) + ((roundFunction((Double.parseDouble(itemdetails.getPPriceInDoller()) * noOfItem), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + (roundFunction((Double.parseDouble(itemdetails.getPPriceInRs()) * noOfItem), 2))));
        }
    }


    private void setTotalPaymentForChequeDD() {


                Double priceDoller = roundFunction((Double.parseDouble(itemdetails.getPriceInDollor()) ), 2);
                Double priceRs = roundFunction((Double.parseDouble(itemdetails.getPriceInRS()) ), 2);
                txtchequeddcost.setText(getResources().getString(R.string.astroshop_dollar_sign) + (roundFunction((priceDoller), 2)) + " / " + getString(R.string.astroshop_rupees_sign) +" "+ ((roundFunction((priceRs), 2))));



            //   txtyoupayrscashon.setText(getResources().getString(R.string.astroshop_dollar_sign) + ((roundFunction((Double.parseDouble(itemdetails.getPPriceInDoller()) * noOfItem), 2)) + " / " + getString(R.string.astroshop_rupees_sign) + (roundFunction((Double.parseDouble(itemdetails.getPPriceInRs()) * noOfItem), 2))));

    }

    private void showHidePaymentOptions(String country) {
        if (country != null && !country.isEmpty()) {
            if (country.equalsIgnoreCase("india")) {
                radionetbanking.setVisibility(View.VISIBLE);
                radiocheque.setVisibility(View.VISIBLE);
            } else {

                radionetbanking.setVisibility(View.GONE);
                radiocheque.setVisibility(View.GONE);
            }

        }
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
                        //Log.e("Paymode",payMode);
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = array.getJSONObject(0);
                            String result = obj.getString("Result");
                            if (result.equalsIgnoreCase("1")) {
                                order_id = obj.getString("OrderId");
                                bundle.putString("order_Id", order_id);
                                if (payMode.equalsIgnoreCase("CCAvenue")) {
                                    postDataToAvenue();
                                }  else if (payMode.equalsIgnoreCase("cheque")) {
                                    Intent intent = new Intent(getActivity(), ActAstroShopChequeDdDetail.class);
                                    intent.putExtra("OrderId", order_id);
                                    intent.putExtra("Otype","1");
                                    //Log.e("orderid", order_id);
                                    //    startActivity(intent);
                                    startActivityForResult(intent, 1);
                                }

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

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
                headers.put("regName", localOrderModal.getRegName());
                headers.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(localOrderModal.getEmailID()));
                headers.put("gender", localOrderModal.getGender());
                headers.put("dateOfBirth", localOrderModal.getDateOfBirth());
                headers.put("monthOfBirth", localOrderModal.getMonthOfBirth());
                headers.put("yearOfBirth", localOrderModal.getYearOfBirth());
                headers.put("minOfBirth", localOrderModal.getMinOfBirth());
             //   headers.put("paymode", "cheque");
                headers.put("paymode", payMode);
                headers.put("hourOfBirth", localOrderModal.getHourOfBirth());
                headers.put("place", localOrderModal.getPlace());
                headers.put("nearCity", localOrderModal.getNearCity());
                headers.put("state", localOrderModal.getState());
                headers.put("country",localOrderModal.getCountry());
                headers.put("problem", localOrderModal.getProblem());
                headers.put("serviceId", localOrderModal.getServiceId());
               // headers.put("serviceId", "3");
                headers.put("profileId", localOrderModal.getProfileId());
                headers.put("price", localOrderModal.getPrice());
                headers.put("priceRs", localOrderModal.getPriceRs());
                headers.put("mobileNo", localOrderModal.getMobileNo());
                headers.put("knowDOB", localOrderModal.getKnowDOB());
                headers.put("knowTOB", localOrderModal.getKnowTOB());
                if(finalSelectedCard!=null)
                {
                    headers.put("ccAvenueType", finalSelectedCard.getPayOptType());

                }
                else
                {
                    headers.put("ccAvenueType", "");

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
        CardTypeDTO selectedCard = finalSelectedCard;
        if (selectedCard.getDataAcceptedAt().equalsIgnoreCase("CCAvenue") &&!selectedCard.getCardType().equalsIgnoreCase("NBK")) {
            Fragment frag_astroshop_card = new FragAstroServiceCardDetail();
            if (bundle != null) {
                bundle.putSerializable("Key", itemdetails);
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
            if (selectedCard != null) {
                String email_id = CUtils.getAstroshopUserEmail(getActivity());
                Intent intent = new Intent(getActivity(), ActPaymentWebView.class);
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
//
//                intent.putExtra(AvenuesParams.DELIVERY_NAME, orderModel.getName().trim());
//                intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, orderModel.getAddress().trim());
//                intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, orderModel.getCountry().trim());
//                intent.putExtra(AvenuesParams.DELIVERY_STATE, orderModel.getState().trim());
//                //Log.e("State", orderModel.getState().trim());
//                //Log.e("City", orderModel.getCity().trim());
//
//                intent.putExtra(AvenuesParams.DELIVERY_CITY, orderModel.getCity());
//                intent.putExtra(AvenuesParams.DELIVERY_ZIP, orderModel.getPincode());
//                intent.putExtra(AvenuesParams.DELIVERY_TEL, orderModel.getMobileno());
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
                Double d = Double.parseDouble(itemdetails.getPriceInRS());
                String amount = String.valueOf(d);
                intent.putExtra(AvenuesParams.AMOUNT, amount);
                intent.putExtra("Order_Model", orderModel);
                intent.putExtra("Key", itemdetails);

                //  startActivity(intent);
                startActivityForResult(intent, 1);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("Result recieve in frag" + requestCode, "Done" + resultCode);

        if (requestCode == 1 && resultCode == 1) {
            getActivity().setResult(1);
            getActivity().finish();
        }

    }



}
