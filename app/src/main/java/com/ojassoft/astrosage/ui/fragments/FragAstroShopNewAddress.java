package com.ojassoft.astrosage.ui.fragments;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopCountryModel;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.ItemOrderModel;
import com.ojassoft.astrosage.customadapters.AstroShopCountryListAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.Payoption;
import com.ojassoft.astrosage.model.ProductCategorymodal;
import com.ojassoft.astrosage.ui.act.ActAstroShopShippingDetails;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//import com.google.analytics.tracking.android.Log;

/**
 * Created by ojas on ७/३/१६.
 */
public class FragAstroShopNewAddress extends Fragment implements View.OnClickListener {
    private static Choice curentView;
    private static CustomProgressDialog pd = null;
    private static boolean firstTime = true;
    public Payoption globalPayOptions;
    public AstroShopItemDetails itemdetails;
    public int noOfItem = 1;
    MyCustomToast mct;
    AstroShopCountryListAdapter adapter;
    private View view;
    private Button btn_Proceed;
    private int frameId = R.id.frame_layout;
    private FragmentTransaction ft;
    private TextView tvToggleNewAddress, tvAgree, tvSavedAddress, tvTrustMsg, tvToggleSaveAddress, textDiscountPlan;
    private EditText edt_pincode, edt_mobile, edt_address, et_state, edt_landmark, edt_city, edt_Name;
    private TextView spin_country, et_shipping_cost;
    private AppCompatCheckBox chkdefault;
    private LinearLayout llsavedAddress1, llnewAddress;
    private String country_name;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Cache cache;
    private Typeface typeface;
    private RequestQueue queue, testQuesu;
    public ArrayList<AstroShopCountryModel> astroCoumtryNameList = new ArrayList<AstroShopCountryModel>();
    private String data;
    private ItemOrderModel orderModal;
    private String make_Default = "0";

    ;
    private TextInputLayout edt_Name_layout, edt_pincode_layout, edt_mobile_layout, edt_address_layout, edt_city_layout, edt_State_layout;
    private CardView card_view1;
    private String title;
    private Boolean isFromCart = false;
    private Bundle bundle;
    private String Currency = "INR";
    private JSONObject fullAddress;
    private String shipping_cost = "0";
    private String shipping_cost_in_rs = "0";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //if (view == null) {
        view = inflater.inflate(R.layout.lay_astro_shop_new_address, container, false);
       /* } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }*/
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        testQuesu = Volley.newRequestQueue(getActivity().getApplicationContext());
        //     cache = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache();
        //      Cache.Entry entry = cache.get(CGlobalVariables.astroShopcountryListLive);

        itemdetails = new AstroShopItemDetails();
        pd = new CustomProgressDialog(getActivity(), typeface);
        init(view);

        checkCachedData();
        adapter = new AstroShopCountryListAdapter(getActivity(), astroCoumtryNameList);

        return view;
    }

    private boolean validateData() {
        boolean flag = false;
        if (curentView == Choice.SAVE_ADDRESS) {
            flag = true;
        } else if (validateName(edt_Name, edt_Name_layout, getString(R.string.astro_shop_User_name))
                && validateName(edt_pincode, edt_pincode_layout, getString(R.string.astro_shop_User_pincode))
                && validateName(edt_mobile, edt_mobile_layout, getString(R.string.astro_shop_User_mob_no))
                && validateName(edt_address, edt_address_layout, getString(R.string.astro_shop_User_shipping_address))
                && validateName(edt_city, edt_city_layout, getString(R.string.astro_shop_User_city))
                && validateName(et_state, edt_State_layout, getString(R.string.astro_shop_User_state))
                && validateNameForCountry(spin_country, getString(R.string.astro_shop_User_country))
        )
            flag = true;

        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay_proceed:
                //Log.e("Clicked button");
                try {
                    CUtils.hideMyKeyboard(getActivity());

                } catch (Exception e) {
                    // TODO: handle exception
                }
                orderModal.setCartList(CUtils.getCartProducts(getActivity()));
                if (!isFromCart) {
                    orderModal.setPrCatId(itemdetails.getP_CatId());
                    orderModal.setPrId(itemdetails.getPId());
                    orderModal.setProductcost_inrs(itemdetails.getPPriceInRs());
                    orderModal.setProductcost(itemdetails.getPPriceInDoller());

                } else {
                    Double priceDoller = 0d;
                    ;
                    Double priceRs = 0d;

                    orderModal.setPrCatId("");
                    orderModal.setPrId("");
                    ArrayList<AstroShopItemDetails> cartList = CUtils.getCartProducts(getActivity());

                    for (AstroShopItemDetails item : cartList) {
                        priceDoller = priceDoller + roundFunction((Double.parseDouble(item.getPPriceInDoller())), 2);
                        priceRs = priceRs + roundFunction((Double.parseDouble(item.getPPriceInRs())), 2);
                    }
                    orderModal.setProductcost_inrs(String.valueOf(priceRs));
                    orderModal.setProductcost(String.valueOf(priceDoller));
                }

                if (curentView == Choice.NEW_ADDRESS) {
                    if (validateData()) {
                        orderModal.setAddress(edt_address.getText().toString());
                        orderModal.setCity(edt_city.getText().toString().trim());
                        orderModal.setCountry(country_name);
                        orderModal.setEmailid(CUtils.getAstroshopUserEmail(getActivity()));
                        orderModal.setLandmark(edt_landmark.getText().toString().trim());
                        orderModal.setMobileno(edt_mobile.getText().toString().trim());
                        orderModal.setName(edt_Name.getText().toString().trim());
                        orderModal.setCity(edt_city.getText().toString().trim());
                        orderModal.setPaymode("CCAVENUE");
                        orderModal.setPincode(edt_pincode.getText().toString().trim());
                        //    orderModal.setShippingcost(shipping_cost);
                        orderModal.setState(et_state.getText().toString().trim());

                        if (chkdefault.isChecked())
                            make_Default = "1";
                        else
                            make_Default = "0";

                        orderModal.setMakeitdefault(make_Default);
                        if (orderModal.getMakeitdefault().equalsIgnoreCase("1")) {
                            //  new GetData().execute();
                            saveAddressOnServer();
                        } else {

                            //Log.e("Get Data from new address");
                            /*if (!CUtils.isConnectedWithInternet(getActivity())) {
                                MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                                        .getLayoutInflater(), getActivity(), typeface);
                                mct.show(getResources().getString(R.string.no_internet));
                            } else {*/

                            //getCCavenuePayOptions();
                            //      new GetData().execute();
                            //}
                            switchToFrag();
                        }


                        //     genrateOrder(orderModal);
                    }
                } else {
                    try {
                        country_name = fullAddress.getString("Country");
                        //                        orderModal.setAddress(new String(fullAddress.getString("Address").getBytes("ISO-8859-1"), "UTF-8"));

                        orderModal.setAddress(fullAddress.getString("Address"));
                        orderModal.setCity(fullAddress.getString("City"));
                        //Log.e("City" + new String(fullAddress.getString("City").getBytes("ISO-8859-1"), "UTF-8"));
                        orderModal.setState(fullAddress.getString("State"));
                        orderModal.setCountry(country_name);
                        orderModal.setEmailid(CUtils.getAstroshopUserEmail(getActivity()));
                        orderModal.setLandmark(fullAddress.getString("LandMark"));
                        orderModal.setMobileno(fullAddress.getString("MobileNo"));
                        orderModal.setName(fullAddress.getString("Name"));
                        orderModal.setPaymode("CCAVENUE");
                        //  orderModal.setPrCatId(itemdetails.getP_CatId());
                        // orderModal.setPrId(itemdetails.getPId());
                        orderModal.setPincode(fullAddress.getString("Pincode"));
                        //    orderModal.setProductcost(itemdetails.getPPriceInDoller());
                        //  orderModal.setProductcost_inrs(itemdetails.getPPriceInRs());

                        //         orderModal.setShippingcost(shipping_cost);
                        orderModal.setMakeitdefault("0");
                        //    genrateOrder(orderModal);
                        //Log.e("Get Data from filled address");


                       /* if (!CUtils.isConnectedWithInternet(getActivity())) {
                            MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                                    .getLayoutInflater(), getActivity(), typeface);
                            mct.show(getResources().getString(R.string.no_internet));
                        } else {*/

                        //getCCavenuePayOptions();
                        //      new GetData().execute();
                        //}
                        switchToFrag();
                    } catch (Exception e) {
                    }
                }


              /*  if (!CUtils.isConnectedWithInternet(getActivity())) {
                    MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                            .getLayoutInflater(), getActivity(), typeface);
                    mct.show(getResources().getString(R.string.no_internet));
                } else {


                    //      new GetData().execute();
                }
*/
                break;

            case R.id.tvAddress:
            case R.id.tvAddress1:

                if (curentView == Choice.SAVE_ADDRESS) {/*"Name").getBytes("ISO-8859-1"), "UTF-8");
            String strAddress = new String(fullAddress.getString("Address").getBytes("ISO-8859-1"), "UTF-8");
            String strCity = new String(fullAddress.getString("City").getBytes("ISO-8859-1"), "UTF-8");
            String strState = new String(fullAddress.getString("State").getBytes("ISO-8859-1"), "UTF-8");
            String strCountry = new String(fullAddress.getString("Country").getBytes("ISO-8859-1"), "UTF-8");
            String strPincode = new String(fullAddress.getString("Pincode").*/
                    showSuitableView(false);
                } else {

                    try {
                        if (!fullAddress.getString("Name").isEmpty() && !fullAddress.getString("Address").isEmpty() && !fullAddress.getString("Pincode").isEmpty() && !fullAddress.getString("Country").isEmpty() && !fullAddress.getString("State").isEmpty() && !fullAddress.getString("City").isEmpty())
                            showSuitableView(true);
                        else
                            mct.show(getResources().getString(R.string.default_address));
                    } catch (Exception e) {
                    }
                }
                break;
            case R.id.spinCountry:
                checkCachedData();
                adapter = new AstroShopCountryListAdapter(getActivity(), astroCoumtryNameList);
                showDialog();
                break;
        }
    }

    private void init(View view) {
        firstTime = true;
        orderModal = new ItemOrderModel();
        mct = new MyCustomToast(getActivity(), getActivity()
                .getLayoutInflater(), getActivity(), typeface);
        bundle = this.getArguments();

        //                bundle.putBoolean("fromCart", true);

        isFromCart = bundle.getBoolean("fromCart", false);
        if (bundle != null && !isFromCart) {
            itemdetails = (AstroShopItemDetails) bundle.getSerializable("key");
            noOfItem = bundle.getInt("ItemNo");
            //Log.e("NOOFITEMSSSS" + noOfItem);
        }
        try {
            fullAddress = new JSONObject(CUtils.getAstroshopUserAddressDetailInPrefs(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.LOGIN_DONE);
        ((ActAstroShopShippingDetails) getActivity()).setSelected(ActAstroShopShippingDetails.Status.SHIPPING_ENABLE);
        btn_Proceed = (Button) view.findViewById(R.id.btn_pay_proceed);
        btn_Proceed.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        btn_Proceed.setOnClickListener(this);
        tvToggleNewAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvToggleNewAddress.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        tvToggleNewAddress.setOnClickListener(this);
        tvSavedAddress = (TextView) view.findViewById(R.id.tvSavedAddress);
        tvToggleSaveAddress = (TextView) view.findViewById(R.id.tvAddress1);
        tvToggleSaveAddress.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        tvToggleSaveAddress.setOnClickListener(this);
        card_view1 = (CardView) view.findViewById(R.id.card_view1);
        //   tvSavedAddress.setTypeface(typeface);
        tvTrustMsg = (TextView) view.findViewById(R.id.tvTrustMsg);
        // tvTrustMsg.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        edt_pincode = (EditText) view.findViewById(R.id.edt_pincode);
        edt_mobile = (EditText) view.findViewById(R.id.edt_mobile);
        edt_address = (EditText) view.findViewById(R.id.edt_address);
        et_state = (EditText) view.findViewById(R.id.edt_State);
        edt_city = (EditText) view.findViewById(R.id.edt_city);
        edt_Name = (EditText) view.findViewById(R.id.edt_Name);
        edt_Name_layout = (TextInputLayout) view.findViewById(R.id.edt_Name_layout);
        edt_Name.addTextChangedListener(new MyTextWatcher(edt_Name));
        edt_pincode_layout = (TextInputLayout) view.findViewById(R.id.edt_pincode_layout);
        edt_pincode.addTextChangedListener(new MyTextWatcher(edt_pincode));
        edt_address_layout = (TextInputLayout) view.findViewById(R.id.edt_address_layout);
        edt_address.addTextChangedListener(new MyTextWatcher(edt_address));
        edt_mobile_layout = (TextInputLayout) view.findViewById(R.id.edt_mobile_layout);
        edt_mobile.addTextChangedListener(new MyTextWatcher(edt_mobile));

        edt_city_layout = (TextInputLayout) view.findViewById(R.id.edt_city_layout);
        edt_city.addTextChangedListener(new MyTextWatcher(edt_city));
        edt_State_layout = (TextInputLayout) view.findViewById(R.id.edt_State_layout);
        et_state.addTextChangedListener(new MyTextWatcher(et_state));
        et_shipping_cost = (TextView) view.findViewById(R.id.edt_ShippingCost);
        edt_landmark = (EditText) view.findViewById(R.id.edt_landmark);
        edt_landmark.addTextChangedListener(new MyTextWatcher(edt_landmark));

        textDiscountPlan = (TextView) view.findViewById(R.id.text_Discount_plan);

        spin_country = (TextView) view.findViewById(R.id.spinCountry);
        spin_country.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        chkdefault = (AppCompatCheckBox) view.findViewById(R.id.chkdefault);
        chkdefault.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        llnewAddress = (LinearLayout) view.findViewById(R.id.llnewAddress);
        //llsavedAddress = (LinearLayout) view.findViewById(R.id.llsavedAddress);
        spin_country.setOnClickListener(this);
        chkdefault.setChecked(true);
        try {
            if (!fullAddress.getString("Name").isEmpty() && !fullAddress.getString("Address").isEmpty() && !fullAddress.getString("Pincode").isEmpty() && !fullAddress.getString("Country").isEmpty() && !fullAddress.getString("State").isEmpty() && !fullAddress.getString("City").isEmpty()) {
                tvToggleNewAddress.setVisibility(View.VISIBLE);
                showSuitableView(true);
            } else {
                tvToggleNewAddress.setVisibility(View.GONE);
                card_view1.setVisibility(View.GONE);
                showSuitableView(false);
            }
        } catch (Exception e) {
            Log.i("Exception>>", e.getMessage());
        }
    }

    private void showSuitableView(boolean showSaved) {
        if (showSaved) {
            curentView = Choice.SAVE_ADDRESS;
            //llsavedAddress.setVisibility(View.VISIBLE);
            tvSavedAddress.setVisibility(View.VISIBLE);
            llnewAddress.setVisibility(View.GONE);
            //  tvToggleNewAddress.setText(getResources().getString(R.string.add_new_address));
            tvToggleNewAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_black, 0);
            tvToggleSaveAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_remove_black, 0);
            setAddress();
            //  CUtils.getAstroshopUserAddressDetailInPrefs(getActivity());
            //  new Gson().toJson(CUtils.getAstroshopUserAddressDetailInPrefs(getActivity()));
            try {
                //   new Gson().toJson(CUtils.getAstroshopUserAddressDetailInPrefs(getActivity()));
                //      //Log.e(fullAddress.toString());
                //    //Log.e(CUtils.getAstroshopUserAddressDetailInPrefs(getActivity()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            et_shipping_cost.setText("");
            if (!spin_country.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.select_state))) {
                country_name = spin_country.getText().toString().trim();
                //   loadAstroShopShippingCost(true);

                if (isFromCart) {
                    loadAstroShopCartShippingCost(true);
                } else {
                    loadAstroShopShippingCost(true);

                }
            }
            curentView = Choice.NEW_ADDRESS;
            //llsavedAddress.setVisibility(View.GONE);
            tvSavedAddress.setVisibility(View.GONE);
            llnewAddress.setVisibility(View.VISIBLE);
            //  tvToggleNewAddress.setText(getResources().getString(R.string.use_default_address));
            tvToggleNewAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_remove_black, 0);
            tvToggleSaveAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_black, 0);

        }
    }//ic_keyboard_arrow_up_black

    private void loadAstroShopCountryList() {
        //pd.show();
        //pd.setCancelable(false);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShopcountryListLive,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (pd != null && pd.isShowing()) {
                            //Log.e("Showing");
                            pd.dismiss();
                        }
                        /*AstroShopCountryModel model=new AstroShopCountryModel();
                        //Log.e("CountryListResponse  +" + response.toString());
                        try {
                            JSONArray array=new JSONArray(response);
                            for(int i=0;i<array.length();i++){
                               JSONObject obj=array.getJSONObject(i);
                                model.setCn_Name(obj.getString("Cn_Name"));
                                model.setCn_SPrice(obj.getString("Cn_SPrice"));
                                astroCoumtryNameList.add(model);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                        Gson gson = new Gson();
                        JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                        //Log.e("Element" + element.toString());
                        astroCoumtryNameList = gson.fromJson(response, new TypeToken<ArrayList<AstroShopCountryModel>>() {
                        }.getType());

                        //Log.e("Size returned" + astroCoumtryNameList.size());
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("Error: " + error.getMessage());
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
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Key", CUtils.getApplicationSignatureHashCode(getActivity()));

                //headers.put("asuserid", CUtils.getUserName(getActivity()));
                // headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(getActivity())));

                return headers;
            }

        };


        ;
// Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    private void showDialog() {
        String strFilter;
        final EditText inputSearch;
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);

        View view = getActivity().getLayoutInflater().inflate(R.layout.lay_astroshop_city_custompopup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);

        inputSearch = (EditText) view.findViewById(R.id.edtcountry);
        strFilter = inputSearch.getText().toString();
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("COUNTRY ",""+astroCoumtryNameList.get(position).getCn_Name());
                Log.e("COUNTRY SIZE ",""+astroCoumtryNameList.size());

                spin_country.setText(astroCoumtryNameList.get(position).getCn_Name());
                country_name = astroCoumtryNameList.get(position).getCn_Name();
                if (astroCoumtryNameList.get(position).getCn_Name().equalsIgnoreCase("India")) {
                    Currency = "INR";
                } else {
                    Currency = "USD";
                }
                if (!country_name.trim().equalsIgnoreCase(getResources().getString(R.string.select_state))) {

                    if (isFromCart) {
                        loadAstroShopCartShippingCost(false);
                    } else {
                        loadAstroShopShippingCost(false);

                    }
                    //  loadAstroShopShippingCost(false);
                }
                spin_country.setTypeface((null));
                dialog.dismiss();

                try {
                    if(adapter != null) {
                        astroCoumtryNameList = (ArrayList<AstroShopCountryModel>) adapter.filter("");
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text


                //  new AstroShopCountryListAdapter(getActivity(), astroCoumtryNameList).getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                astroCoumtryNameList = (ArrayList<AstroShopCountryModel>) adapter.filter(text);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private void parseGsonData() {
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(data.toString(), JsonElement.class);
        astroCoumtryNameList = gson.fromJson(data, new TypeToken<ArrayList<AstroShopCountryModel>>() {
        }.getType());

      /*   AstroShopCountryModel model=new AstroShopCountryModel();
        //Log.e("CountryListResponse  +" + data.toString());
        try {
            JSONArray array=new JSONArray(data);
            for(int i=0;i<array.length();i++){
                JSONObject obj=array.getJSONObject(i);
                model.setCn_Name(obj.getString("Cn_Name"));
                model.setCn_SPrice(obj.getString("Cn_SPrice"));
                astroCoumtryNameList.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    private void loadAstroShopShippingCost(final boolean isDeafult) {
        if (firstTime) {
            //Log.e("Called...");
            pd.show();
            pd.setCancelable(false);
        } else if (!isDeafult) {
            pd.show();
            pd.setCancelable(false);
        }

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShopcountryListLive,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("ShippingAdress  +" + response.toString());
                        if (response != null && !(response.isEmpty())) {
                            try {
                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                //android.util.
                                Log.e("wiConvert+", str);

                               /* byte ptext[] = response.getBytes();
                                String value = new String(ptext, "UTF-8");
                                response=value;*/
                                response = str;


                                JSONArray array = new JSONArray(response);
                                JSONObject obj1 = array.getJSONObject(0);
                                String result = obj1.getString("Result");

                                if (result.equals("1")) {

                                    JSONObject obj = array.getJSONObject(1);
                                    String stringresponse = obj.getString("Sp_Price");
                                    String inrs = obj.getString("Sp_RsPrice");
                                    shipping_cost = stringresponse;

                                    orderModal.setShippingcost(stringresponse);
                                    orderModal.setShippingcost_in_rs(inrs);

                                    String rs_shpng1 = obj.getString("Sp_ShippingMsgRs");
                                    String dl_shpng1 = obj.getString("Sp_ShippingMsg");

                                    String rs_shpng2 = obj.getString("Sp_ShippingMsgRs1");
                                    String dl_shpng2 = obj.getString("Sp_ShippingMsg1");


                                    if (Currency.equalsIgnoreCase("INR")) {

                                        if (inrs.trim().equalsIgnoreCase(CGlobalVariables.FREE_SHIPPING)) {
                                            textDiscountPlan.setVisibility(View.VISIBLE);
                                            tvTrustMsg.setVisibility(View.GONE);
                                            CUtils.showServiceProductDiscountedText(getActivity(), textDiscountPlan,
                                                    rs_shpng1, rs_shpng2,
                                                    CGlobalVariables.FROM_SERVICE_TEXT);
                                        } else {
                                            textDiscountPlan.setVisibility(View.GONE);
                                            tvTrustMsg.setVisibility(View.VISIBLE);
                                            tvTrustMsg.setText((Html.fromHtml(rs_shpng1)));
                                        }

                                    } else {

                                        if (inrs.trim().equalsIgnoreCase(CGlobalVariables.FREE_SHIPPING)) {
                                            textDiscountPlan.setVisibility(View.VISIBLE);
                                            tvTrustMsg.setVisibility(View.GONE);
                                            CUtils.showServiceProductDiscountedText(getActivity(), textDiscountPlan,
                                                    dl_shpng1, dl_shpng2,
                                                    CGlobalVariables.FROM_SERVICE_TEXT);
                                        } else {
                                            textDiscountPlan.setVisibility(View.GONE);
                                            tvTrustMsg.setVisibility(View.VISIBLE);
                                            tvTrustMsg.setText((Html.fromHtml(dl_shpng1)));
                                        }
                                    }

                                    et_shipping_cost.setText(getResources().getString(R.string.shp_cost) + ":" + getResources().getString(R.string.astroshop_dollar_sign).trim() + stringresponse.trim() + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + inrs);
                                } else if (result.equals("2")) {

                                    mct.show(getResources().getString(R.string.plan_id_not_match));
                                } else if (result.equals("4")) {

                                    mct.show(getResources().getString(R.string.plan_id_not_match));
                                } else if (result.equals("5")) {

                                    mct.show(getResources().getString(R.string.plan_id_not_match));
                                } else {
                                    mct.show(getResources().getString(R.string.plan_id_not_match));
                                }
                                //Log.e("Cost rs:" + inrs + "In doler:" + stringresponse);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        if (firstTime) {
                            //Log.e("Called...");
                            pd.dismiss();
                            firstTime = false;
                        } else if (!isDeafult) {
                            pd.dismiss();
                            firstTime = false;
                        }
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("Error: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                pd.dismiss();
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
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Key", CUtils.getApplicationSignatureHashCode(getActivity()));

                headers.put(CGlobalVariables.KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(getActivity())));

                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(getActivity())));

                // headers.put("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST);
                // headers.put("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST);


                headers.put("countryname", country_name);
                headers.put("productId", itemdetails.getPId());
                headers.put("languagecode", "" + LANGUAGE_CODE);

                //Log.e(headers.toString());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void loadAstroShopCartShippingCost(final boolean isDeafult) {
        if (firstTime) {
            //Log.e("Called...");
            pd.show();
            pd.setCancelable(false);
        } else if (!isDeafult) {
            pd.show();
            pd.setCancelable(false);
        }

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.ASTROSHOP_CARTSHIPPING_COST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("ShippingAdress  +" + response.toString());
                        if (response != null && !(response.isEmpty())) {
                            try {

                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                //android.util.
                                Log.e("wiConvert+", str);

                               /* byte ptext[] = response.getBytes();
                                String value = new String(ptext, "UTF-8");
                                response=value;*/
                                response = str;


                                JSONArray array = new JSONArray(response);
                                JSONObject obj1 = array.getJSONObject(0);
                                String result = obj1.getString("Result");

                                if (result.equals("1")) {

                                    JSONObject obj = array.getJSONObject(1);
                                    String stringresponse = obj.getString("Sp_Price");
                                    String inrs = obj.getString("Sp_RsPrice");

                                    String rs_shpng1 = obj.getString("Sp_ShippingMsgRs");
                                    String dl_shpng1 = obj.getString("Sp_ShippingMsg");

                                    String rs_shpng2 = obj.getString("Sp_ShippingMsgRs1");
                                    String dl_shpng2 = obj.getString("Sp_ShippingMsg1");

                                    shipping_cost = stringresponse;
                                    orderModal.setShippingcost(stringresponse);
                                    orderModal.setShippingcost_in_rs(inrs);
                                    orderModal.setPr_rs_Shippingmsg(rs_shpng1);
                                    orderModal.setPr_dl_Shippingmsg_rs(dl_shpng1);

                                    /*tvTrustMsg.setVisibility(View.VISIBLE);
                                    if (Currency.equalsIgnoreCase("INR")) {
                                        tvTrustMsg.setText((Html.fromHtml(rs_shpng)));
                                    } else {
                                        tvTrustMsg.setText((Html.fromHtml(dl_shpng)));
                                    }*/

                                    if (Currency.equalsIgnoreCase("INR")) {

                                        if (inrs.trim().equalsIgnoreCase(CGlobalVariables.FREE_SHIPPING)) {
                                            textDiscountPlan.setVisibility(View.VISIBLE);
                                            tvTrustMsg.setVisibility(View.GONE);
                                            CUtils.showServiceProductDiscountedText(getActivity(), textDiscountPlan,
                                                    rs_shpng1, rs_shpng2,
                                                    CGlobalVariables.FROM_SERVICE_TEXT);
                                        } else {
                                            textDiscountPlan.setVisibility(View.GONE);
                                            tvTrustMsg.setVisibility(View.VISIBLE);
                                            tvTrustMsg.setText((Html.fromHtml(rs_shpng1)));
                                        }

                                    } else {

                                        if (inrs.trim().equalsIgnoreCase(CGlobalVariables.FREE_SHIPPING)) {
                                            textDiscountPlan.setVisibility(View.VISIBLE);
                                            tvTrustMsg.setVisibility(View.GONE);
                                            CUtils.showServiceProductDiscountedText(getActivity(), textDiscountPlan,
                                                    dl_shpng1, dl_shpng2,
                                                    CGlobalVariables.FROM_SERVICE_TEXT);
                                        } else {
                                            textDiscountPlan.setVisibility(View.GONE);
                                            tvTrustMsg.setVisibility(View.VISIBLE);
                                            tvTrustMsg.setText((Html.fromHtml(dl_shpng1)));
                                        }
                                    }

                                    //      et_shipping_cost.setText(stringresponse);
                                    et_shipping_cost.setText(getResources().getString(R.string.shp_cost) + ":" + getResources().getString(R.string.astroshop_dollar_sign).trim() + stringresponse.trim() + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + inrs);
                                } else if (result.equals("2")) {

                                    mct.show(getResources().getString(R.string.plan_id_not_match));
                                } else if (result.equals("4")) {

                                    mct.show(getResources().getString(R.string.plan_id_not_match));
                                } else if (result.equals("5")) {

                                    mct.show(getResources().getString(R.string.plan_id_not_match));
                                } else {
                                    mct.show(getResources().getString(R.string.plan_id_not_match));
                                }
                                //Log.e("Cost rs:" + inrs + "In doler:" + stringresponse);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        if (firstTime) {
                            //Log.e("Called...");
                            pd.dismiss();
                            firstTime = false;
                        } else if (!isDeafult) {
                            pd.dismiss();
                            firstTime = false;
                        }
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("Error: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                pd.dismiss();
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
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() {
                ArrayList<AstroShopItemDetails> list = CUtils.getCartProducts(getActivity());
                ArrayList<ProductCategorymodal> list1 = new ArrayList<ProductCategorymodal>();
                for (AstroShopItemDetails item : list) {
                    ProductCategorymodal modal = new ProductCategorymodal();
                    modal.setPId(item.getPId());
                    modal.setP_CatId(item.getP_CatId());
                    list1.add(modal);

                }
                HashMap<String, String> headers = new HashMap<String, String>();
             /*   JSONObject json=new JSONObject();
                try {
                    json.accumulate("Key", CUtils.getApplicationSignatureHashCode(this));
                    json.accumulate("countryname", country_name);
                   // String listData = new Gson().toJson(list1);
                   // json.accumulate("productList",listData);
                    //Log.e("values"+json);

                }
                catch(Exception e)
                {
                      e.printStackTrace();
                }*/

              /*  headers.put("jsonInput",json.toString());
                headers.put("jsonInputOther",new Gson().toJson(list1));

                //Log.e("re"+headers.toString());*/

                headers.put(CGlobalVariables.KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(getActivity())));

                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(getActivity())));

                //headers.put("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST);
                //headers.put("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST);

                headers.put("Key", CUtils.getApplicationSignatureHashCode(getActivity()));
                headers.put("countryname", country_name);
                headers.put("languagecode", "" + LANGUAGE_CODE);

                headers.put("productList", new Gson().toJson(list1));
                //Log.e("" + new Gson().toJson(list1));
                //Log.e(headers.toString());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    @Override
    public void onResume() {

        //Log.e("Onresume called");
        super.onResume();

    }

    private void checkCachedData() {
        cache = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(CGlobalVariables.astroShopcountryListLive);
        if (entry != null) {
            try {
                //  isCached = true;
                data = new String(entry.data, "UTF-8");
                //Log.e("Volley Cached Data" + data.toString());
                parseGsonData();
                //Log.e("Size returned" + astroCoumtryNameList.size());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            //Log.e("Volley Not Cached Data");
            if (!CUtils.isConnectedWithInternet(getActivity())) {
                MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                        .getLayoutInflater(), getActivity(), typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                loadAstroShopCountryList();
            }

        }
    }

    private void setAddress() {
        try {
            String name = new String(fullAddress.getString("Name").getBytes("ISO-8859-1"), "UTF-8");
            String strAddress = new String(fullAddress.getString("Address").getBytes("ISO-8859-1"), "UTF-8");
            String strCity = new String(fullAddress.getString("City").getBytes("ISO-8859-1"), "UTF-8");
            String strState = new String(fullAddress.getString("State").getBytes("ISO-8859-1"), "UTF-8");
            String strCountry = new String(fullAddress.getString("Country").getBytes("ISO-8859-1"), "UTF-8");
            String strPincode = new String(fullAddress.getString("Pincode").getBytes("ISO-8859-1"), "UTF-8");
            country_name = strCountry;
            if (country_name.equalsIgnoreCase("India")) {
                Currency = "INR";
            } else {
                Currency = "USD";
            }
            if (!country_name.trim().equalsIgnoreCase(getResources().getString(R.string.select_state))) {

                if (isFromCart) {
                    loadAstroShopCartShippingCost(true);
                } else {
                    loadAstroShopShippingCost(true);

                }
                //    loadAstroShopShippingCost(true);

            }
            tvSavedAddress.setText(name.trim() + "\n" + strAddress.trim() + "\n" + strCity.trim() + "\n" + strState.trim() + "\n" + strPincode.trim() + "\n" + strCountry.trim());
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /*
        private void setView()
        {
            try {
                String full_name=fullAddress.getString("Name");
                if(full_name!=null && !full_name.isEmpty())
                {
                    curentView=Choice.SAVE_ADDRESS;
                    showSuitableView(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        boolean value = true;
        if (curentView == Choice.SAVE_ADDRESS) {
            return true;
        }
        if (name == edt_Name) {
            if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;

            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (name == edt_pincode) {
            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }


        }

        if (name == edt_address) {
            if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }

        }

        if (name == edt_mobile) {
            if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 5) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }


        }
        if (name == et_state) {
            if (et_state.getText().toString().trim().isEmpty() || et_state.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }

        }

        if (name == edt_city) {
            if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }

        }

        if (name == edt_city) {
            if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }

        }

        if (name == spin_country) {
            if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 1) {
                mct.show(getString(R.string.astro_shop_User_country));
            } else {
            }
        }


        return value;
    }

    private void enterAlpha(final EditText edt_Text, final boolean numeric) {

        edt_Text.setFilters(new InputFilter[]{
                new InputFilter() {


                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        } else if (cs.toString().matches("[a-zA-Z 0-9]+")) {
                            return cs;
                        } else if (cs.toString().matches("[-/_,]")) {
                            return cs;
                        }

                        return "";
                    }
                }
        });
    }

    private boolean validateNameForCountry(TextView name, String message) {
        boolean value = true;
        if (name == spin_country) {

            if (name.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.select_state))) {
                mct.show(getString(R.string.astro_shop_User_country));
                value = false;
            } else {
            }
        }


        return value;
    }

    private String getItemName() {
        if (itemdetails != null) {

            title = itemdetails.getPName();
        }
        return title;

    }

    private void saveAddressOnServer() {
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShopSaveAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("Simple+" + response.toString());

                        //Log.e("Convert String+" + response.toString());
                        if (response != null && !(response.isEmpty())) {
                            try {

                                JSONObject obj = new JSONObject(response);
                                String stringresponse = obj.getString("Result");
                                JSONObject fullAdress = obj.getJSONObject("FullAddress");
                                if (stringresponse.equalsIgnoreCase("1") || stringresponse.equalsIgnoreCase("3")) {
                                    CUtils.saveAstroshopUserAddressDetailInPrefs(getActivity(), fullAdress.toString());
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    //getCCavenuePayOptions();
                                    switchToFrag();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("Error: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                pd.dismiss();
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
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() {
                /*
                 * key,name,emailid,address,city,landmark,state,country,pincode,mobileno*/
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
                headers.put(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(orderModal.getEmailid()));
                headers.put("address", orderModal.getAddress());
                headers.put("city", orderModal.getCity());
                headers.put("landmark", orderModal.getLandmark());
                headers.put("state", orderModal.getState());
                headers.put("country", orderModal.getCountry());
                headers.put("pincode", orderModal.getPincode());
                headers.put("mobileno", orderModal.getMobileno());
                headers.put("makeitdefault", orderModal.getMakeitdefault());
                headers.put("name", orderModal.getName());
                //Log.e("Body" + headers.toString());
                return headers;
            }
        };

// Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public enum Choice {SAVE_ADDRESS, NEW_ADDRESS}

    /*private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pd = new CustomProgressDialog(getActivity(), typeface);
            pd.show();
            pd.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            // List<Pair<String, String>> vParams = new ArrayList<NameValuePair>();
            List<NameValuePair> vParams = new ArrayList<NameValuePair>();
            vParams.add(new BasicNameValuePair(AvenuesParams.COMMAND,
                    "getJsonDataVault"));
            vParams.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, "ZFKU3FWDWSYYST33".toString().trim()));
            vParams.add(new BasicNameValuePair(AvenuesParams.CURRENCY, Currency.toString().trim()));
            //Log.e("Post currency%%%%%%" + Currency);
            vParams.add(new BasicNameValuePair(AvenuesParams.AMOUNT, "1".toString().trim()));
//            vParams.add(new BasicNameValuePair(
//                    AvenuesParams.CUSTOMER_IDENTIFIER, "Swat".toString().trim()));
            //Log.e("Body post is" + vParams.toString());
            String vJsonStr = sh.makeServiceCall(Constants.JSON_URL,
                    ServiceHandler.POST, vParams);
            //android.util.//Log.e("payResponse: ", "> " + vJsonStr);
            if (!vJsonStr.isEmpty() && !vJsonStr.contains("An error occurred while processing")) {
                try {
                    final Payoption payOption = new Gson().fromJson(vJsonStr, Payoption.class);
                    globalPayOptions = payOption;
                    //android.util.//Log.e("Conversion from tag", " Size= " + payOption.getPayOptions().size());

                } catch (Exception e) {
                    e.printStackTrace();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            getCCavenuePayOptions();

                        }
                    });
                }

            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getCCavenuePayOptions();
                        //   mct.show(getResources().getString(R.string.unable_to_show_due_to_internal_error));

                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            bundle.putSerializable("detail", orderModal);
            pd.dismiss();

            String cardFromPrefs = CUtils.getSavedCards(getActivity());
            if (!cardFromPrefs.isEmpty()) {
                Fragment fragCardSave = new FragAstroSavedCards();

                if (bundle != null && globalPayOptions != null && globalPayOptions.getPayOptions().get(0) != null) {
                    if (!isFromCart) {
                        bundle.putSerializable("Key", itemdetails);
                        bundle.putInt("ItemNo", noOfItem);

                    }
                    bundle.putSerializable("Payoption", globalPayOptions);
                    bundle.putString("currency", Currency);
                    fragCardSave.setArguments(bundle);
                }
                ft = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                ft.replace(frameId, fragCardSave).addToBackStack(null).commit();
            } else if (Currency.equalsIgnoreCase("INR")) {
                Fragment frag_astroshop_pay = new FragAstroShopPayment();
                if (bundle != null && globalPayOptions != null && globalPayOptions.getPayOptions().get(0) != null) {
                    if (!isFromCart) {
                        bundle.putSerializable("Key", itemdetails);
                        bundle.putInt("ItemNo", noOfItem);

                    }
                    bundle.putSerializable("Payoption", globalPayOptions);
                    bundle.putString("currency", Currency);
                    frag_astroshop_pay.setArguments(bundle);
                }
                ft = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                ft.replace(frameId, frag_astroshop_pay, "FragAstroShopPayment").addToBackStack(null).commit();

                //    ft.replace(frameId, frag_astroshop_pay).addToBackStack(null).commit();

                String title = "";
                title = getItemName();
                CUtils.googleAnalyticSendWitPlayServieForAstroshop(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING,
                        title, null);
                //Log.e("GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING" + title);
            } else {
                Fragment frag_astroshop = new FragAstroShopForeignPayment();
                if (bundle != null && globalPayOptions != null && globalPayOptions.getPayOptions().get(0) != null) {
                    if (!isFromCart) {
                        bundle.putSerializable("Key", itemdetails);
                        bundle.putInt("ItemNo", noOfItem);

                    }
                    bundle.putSerializable("PayOPtions", globalPayOptions.getPayOptions().get(0));
                    bundle.putString("currency", Currency);
                    frag_astroshop.setArguments(bundle);
                    String title = "";
                    title = getItemName();
                    CUtils.googleAnalyticSendWitPlayServieForAstroshop(getActivity(),
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING,
                            title, null);
                    //Log.e("GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING" + title);
                }
                //getHostFragmentManager()
                //     ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                ft.replace(frameId, frag_astroshop, "FragAstroShopForeignPayment").addToBackStack(null).commit();
            }


        }

    }*/

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence.toString().length() == 1) {
                if (!charSequence.toString().matches("[a-zA-Z ]+") && !charSequence.toString().matches("[0-9]") && !charSequence.toString().matches("'+'")) {
                    //   mct.show(getResources().getString(R.string.astro_shop_valid_text));
                }
            }
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_Name:
                    validateName(edt_Name, edt_Name_layout, getString(R.string.astro_shop_User_name));

                    String charSequence = edt_Name.getText().toString().trim();
                    if (charSequence.toString().length() == 1) {
                        if (!charSequence.toString().matches("[a-zA-Z ]+") && !charSequence.toString().matches("[0-9]")) {
                            if (curentView == Choice.NEW_ADDRESS) {
                                mct.show(getResources().getString(R.string.astro_shop_valid_text));
                                edt_Name.setText("");
                            }

                        }
                    } else {
                        enterAlpha(edt_Name, false);
                    }
                     /*
                    if (edt_Name.getText().toString().trim().isEmpty() || edt_Name.getText().toString().trim().length() < 1) {
                        //    edt_Name.setError(getString(R.string.astro_shop_User_name));
                        edt_Name_layout.setError(getString(R.string.astro_shop_User_name));
                        requestFocus(edt_Name);
                        edt_Name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        edt_Name_layout.setErrorEnabled(false);
                        edt_Name.getBackground().setColorFilter(null);
                        edt_Name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);

                    }*/
                    break;
                case R.id.edt_pincode:
                    validateName(edt_pincode, edt_pincode_layout, getString(R.string.astro_shop_User_pincode));

                    String charSequence1 = edt_pincode.getText().toString().trim();
                    if (charSequence1.toString().length() == 1) {
                        if (!charSequence1.toString().matches("[a-zA-Z ]+") && !charSequence1.toString().matches("[0-9]")) {
//                            mct.show(getResources().getString(R.string.astro_shop_valid_text));
//                            edt_pincode.setText("");
                        }
                    }
                    //    enterAlpha(edt_pincode, false);
/*
                    if (edt_pincode.getText().toString().trim().isEmpty()) {
                        //  edt_pincode.setError(getString(R.string.astro_shop_User_pincode));
                        edt_pincode_layout.setError(getString(R.string.astro_shop_User_pincode));
                        requestFocus(edt_pincode);
                        edt_pincode.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else {
                        edt_pincode_layout.setErrorEnabled(false);
                        edt_pincode.getBackground().setColorFilter(null);
                        edt_pincode.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);

                    }*/
                    break;

                case R.id.edt_address:
                    validateName(edt_address, edt_address_layout, getString(R.string.astro_shop_User_shipping_address));


                    String charSequence2 = edt_address.getText().toString().trim();
                    if (charSequence2.toString().length() == 1) {
//                        if (!charSequence2.toString().matches("[a-zA-Z ]+") && !charSequence2.toString().matches("[0-9]")) {
//                            mct.show(getResources().getString(R.string.astro_shop_valid_text));
//                            edt_address.setText("");
//                        }
                    }
                    //     enterAlpha(edt_address, true);
/*
                    if (edt_address.getText().toString().trim().isEmpty() || edt_address.getText().toString().trim().length() < 1) {
                        //   edt_address.setError(getString(R.string.astro_shop_User_shipping_address));
                        edt_address_layout.setError(getString(R.string.astro_shop_User_shipping_address));
                        requestFocus(edt_address);
                        edt_address.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else {
                        edt_address_layout.setErrorEnabled(false);
                        edt_address.getBackground().setColorFilter(null);
                        edt_address.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);

                    }*/
                    break;
                case R.id.edt_mobile:
                    validateName(edt_mobile, edt_mobile_layout, getString(R.string.astro_shop_User_mob_no));

/*
                    if (edt_mobile.getText().toString().trim().isEmpty() || edt_mobile.getText().toString().trim().length() < 10) {
                        // edt_mobile.setError(getString(R.string.astro_shop_User_mob_no));
                        edt_mobile_layout.setError(getString(R.string.astro_shop_User_mob_no));
                        requestFocus(edt_mobile);
                        edt_mobile.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else {
                        edt_mobile_layout.setErrorEnabled(false);
                        edt_mobile.getBackground().setColorFilter(null);
                        edt_mobile.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);

                    }*/
                    break;
                case R.id.edt_State:
                    validateName(et_state, edt_State_layout, getString(R.string.astro_shop_User_state));
                    String charSequence3 = et_state.getText().toString().trim();
                    if (charSequence3.toString().length() == 1) {
                        if (!charSequence3.toString().matches("[a-zA-Z ]+") && !charSequence3.toString().matches("[0-9]")) {
                            if (curentView == Choice.NEW_ADDRESS) {
                                mct.show(getResources().getString(R.string.astro_shop_valid_text));
                                et_state.setText("");
                            }
                        }
                    } else {
                        enterAlpha(et_state, false);
                    }
                    /*
                    if (et_state.getText().toString().trim().isEmpty() || et_state.getText().toString().trim().length() < 1) {
                        //    et_state.setError(getString(R.string.astro_shop_User_state));
                        edt_State_layout.setError(getString(R.string.astro_shop_User_state));
                        requestFocus(et_state);
                        et_state.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else {
                        edt_State_layout.setErrorEnabled(false);
                        et_state.getBackground().setColorFilter(null);
                        et_state.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);

                    }*/
                    break;
                case R.id.edt_city:
                    validateName(edt_city, edt_city_layout, getString(R.string.astro_shop_User_city));

                    String charSequence4 = edt_city.getText().toString().trim();
                    if (charSequence4.toString().length() == 1) {
                        if (!charSequence4.toString().matches("[a-zA-Z ]+") && !charSequence4.toString().matches("[0-9]")) {
                            if (curentView == Choice.NEW_ADDRESS) {
                                mct.show(getResources().getString(R.string.astro_shop_valid_text));
                                edt_city.setText("");
                            }
                        }
                    } else {
                        enterAlpha(edt_city, false);
                    }
                    /*
                    if (edt_city.getText().toString().trim().isEmpty() || edt_city.getText().toString().trim().length() < 1) {
                        // edt_city.setError(getString(R.string.astro_shop_User_city));
                        edt_city_layout.setError(getString(R.string.astro_shop_User_city));
                        requestFocus(edt_city);
                        edt_city.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else {
                        edt_city_layout.setErrorEnabled(false);
                        edt_city.getBackground().setColorFilter(null);
                        edt_city.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);

                    }*/

                case R.id.edt_landmark:
                    String charSequence5 = edt_landmark.getText().toString().trim();
                    if (charSequence5.toString().length() == 1) {
                        if (!charSequence5.toString().matches("[a-zA-Z ]+") && !charSequence5.toString().matches("[0-9]")) {
//                            mct.show(getResources().getString(R.string.astro_shop_valid_text));
//                            edt_landmark.setText("");
                        }
                        //    enterAlpha(edt_landmark, true);

                    }
                    break;
            }
        }
    }

    /**
     * get ccavenue pay options
     * Added by Amit Sharma
     */
    private void getCCavenuePayOptions() {
        if (!CUtils.isConnectedWithInternet(getActivity())) {
            MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                    .getLayoutInflater(), getActivity(), typeface);
            mct.show(getResources().getString(R.string.no_internet));
            hideProgressBar();
        } else {
            getData();
        }
        //new GetData().execute();
    }

    /**
     * get custom Ads
     */
    public void getData() {

        showProgressBar();

        Log.e("TestProduct","Constants.JSON_URL="+Constants.JSON_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TestProduct","response="+response);
                        if (!TextUtils.isEmpty(response) && !response.contains("An error occurred while processing")) {
                            try {
                                final Payoption payOption = new Gson().fromJson(response, Payoption.class);
                                globalPayOptions = payOption;
                                switchToFrag();
                                hideProgressBar();
                            } catch (Exception e) {
                                getCCavenuePayOptions();
                            }
                        } else {
                            getCCavenuePayOptions();
                        }

                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TestProduct","error="+error);
                hideProgressBar();
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


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put(AvenuesParams.COMMAND, "getJsonDataVault");
                headers.put(AvenuesParams.ACCESS_CODE, "ZFKU3FWDWSYYST33");
                headers.put(AvenuesParams.CURRENCY, Currency.trim());
                headers.put(AvenuesParams.AMOUNT, "1");
                return headers;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("User-agent", "Mozilla/5.0 (Linux; U; Android-4.0.3; en-us; Galaxy Nexus Build/IML74K) AppleWebKit/535.7 (KHTML, like Gecko) CrMo/16.0.912.75 Mobile Safari/535.7");
                return headers;
            }

        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    /**
     * To switch fragment
     */
    private void switchToFrag() {
        bundle.putSerializable("detail", orderModal);
        if (bundle != null && globalPayOptions != null && globalPayOptions.getPayOptions() != null && globalPayOptions.getPayOptions().get(0) != null) {
            if (!isFromCart) {
                bundle.putSerializable("Key", itemdetails);
                bundle.putInt("ItemNo", noOfItem);
            }
            bundle.putSerializable("Payoption", globalPayOptions);
            bundle.putString("currency", Currency);
        }
        String cardFromPrefs = CUtils.getSavedCards(getActivity());
        String title = "";
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
        Fragment fragment;
        String tag;
        if (!cardFromPrefs.isEmpty()) {
            tag = "FragAstroSavedCards";
            fragment = new FragAstroSavedCards();
        } else if (Currency.equalsIgnoreCase("INR")) {
            tag = "FragAstroShopPayment";
            fragment = new FragAstroShopPayment();
            title = getItemName();
        } else {
            tag = "FragAstroShopForeignPayment";
            fragment = new FragAstroShopForeignPayment();
            title = getItemName();
        }
        fragment.setArguments(bundle);
        ft.replace(frameId, fragment, tag).addToBackStack(null).commit();
        CUtils.googleAnalyticSendWitPlayServieForAstroshop(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING,
                title, null);
    }

    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(getActivity(), typeface);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }


    private void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
