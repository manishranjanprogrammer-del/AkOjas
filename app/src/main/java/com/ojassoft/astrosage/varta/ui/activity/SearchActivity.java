package com.ojassoft.astrosage.varta.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.adapters.SearchAstrologerAdapter;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends BaseActivity implements View.OnClickListener, VolleyResponse {
    private AutoCompleteTextView editTextSearchAstrologer;
    private SearchAstrologerAdapter searchAstrologerAdapter;
    private ImageView mIvImageSearch;
    private boolean isFiltered = false;
    private FrameLayout bottomLayout;
    private ArrayList<AstrologerDetailBean> searchedAtroList;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchedAtroList = new ArrayList<>();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        mIvImageSearch = findViewById(R.id.iv_search_image);
        editTextSearchAstrologer = findViewById(R.id.editTextSearchKundli);

        editTextSearchAstrologer.requestFocus();
        /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextSearchAstrologer, InputMethodManager.SHOW_IMPLICIT);*/
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        bottomLayout = findViewById(R.id.bottom_layout);

        FontUtils.changeFont(SearchActivity.this, editTextSearchAstrologer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        searchAstrologerAdapter = new SearchAstrologerAdapter(SearchActivity.this, 0, 0, searchedAtroList);
        editTextSearchAstrologer.setThreshold(1);//will start working from first character
        editTextSearchAstrologer.setAdapter(searchAstrologerAdapter);//setting the adapter data into the AutoCompleteTextView
        editTextSearchAstrologer.setTextColor(getResources().getColor(R.color.black));
        //editTextSearchAstrologer.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog));
        editTextSearchAstrologer.setDropDownBackgroundResource(R.drawable.bg_dialog);

        mIvImageSearch.setOnClickListener(this);
        bottomLayout.setOnClickListener(this);
        editTextSearchAstrologer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().isEmpty()) {
                    isFiltered = false;
                    mIvImageSearch.setImageDrawable(getResources().getDrawable(R.drawable.icon_search));
                } else {
                    isFiltered = true;
                    mIvImageSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_navigation_close));
                }

                String searchedTextStr = editable.toString().trim();
                if (searchedTextStr.length() > 3) {

                    if (!searchedAtroList.isEmpty()) {
                        searchAstrologerAdapter.getFilter().filter(searchedTextStr);
                    } else {
                        getSearchResultFromApi(searchedTextStr);
                    }

                } else if (searchedTextStr.length() == 3) {
                    getSearchResultFromApi(searchedTextStr);
                }
            }
        });

        /*editTextSearchAstrologer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("COUNT click ",""+parent.getCount());
                AstrologerDetailBean astrologerDetailBean = (AstrologerDetailBean) parent.getItemAtPosition(position);
                Toast.makeText(SearchActivity.this, "NAME "+ astrologerDetailBean.getName(),Toast.LENGTH_LONG).show();
            }
        });*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        //CUtils.hideMyKeyboard(this);
        hideKeyboard(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_search_image:
                editTextSearchAstrologer.setText("");
                mIvImageSearch.setImageDrawable(getResources().getDrawable(R.drawable.icon_search));
                break;

            case R.id.bottom_layout:
                finish();
                break;
        }
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void getSearchResultFromApi(String text) {
        if (CUtils.isConnectedWithInternet(SearchActivity.this)) {
            //showProgressBar();
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_SEARCHED_ASTRO_URL,
                    SearchActivity.this, false, getParams(text), 1).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public Map<String, String> getParams(String text) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(SearchActivity.this));
        headers.put("q", text);
        //Log.d("SearchAstro", "Params=>" + headers);
        return headers;
    }

    @Override
    public void onResponse(String response, int method) {
        Log.d("SearchAstro", "response=>" + response);
        //hideProgressBar();
        if (method == 1) {
            try {
                parseJsonDataAndUpdateUi(response);
            } catch (Exception e) {
                clearList();
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        Log.d("SearchAstro ", "error=>" + error.toString());
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong_error), Toast.LENGTH_LONG).show();
        clearList();
        //hideProgressBar();
    }

    private void parseJsonDataAndUpdateUi(String response) {
        boolean isClearList = true;
        if (response != null && !response.isEmpty()) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status = jsonObj.getString(CGlobalVariables.STATUS);
                if (status.equals("1")) {
                    JSONArray jsonArray = jsonObj.getJSONArray("astrologers");
                    if (jsonArray.length() > 0) {
                        if (searchedAtroList == null) {
                            searchedAtroList = new ArrayList();
                        } else {
                            searchedAtroList.clear();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
                            astrologerDetailBean.setAstroWalletId(object.getString("wi"));
                            astrologerDetailBean.setUrlText(object.getString("urlText"));
                            astrologerDetailBean.setName(object.getString("n"));
                            astrologerDetailBean.setImageFile(object.getString("if"));
                            searchedAtroList.add(astrologerDetailBean);
                        }
                    }
                    searchAstrologerAdapter.getFilter().filter(editTextSearchAstrologer.getText().toString());
                    isClearList = false;
                } else if (status.equals("2")) {
                   // Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_astrologer_found), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong_error) + " (" + status + ")", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong_error), Toast.LENGTH_LONG).show();
            }
        }

        if(isClearList){
            clearList();
        }
    }

    private void clearList() {
        try {
            if (searchedAtroList != null) {
                searchedAtroList.clear();
            }
            searchAstrologerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }
}