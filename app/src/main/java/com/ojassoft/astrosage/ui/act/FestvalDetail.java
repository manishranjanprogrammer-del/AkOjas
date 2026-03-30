package com.ojassoft.astrosage.ui.act;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.customadapters.FestivalDetailAdapter;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.model.FestivalDetailData;
import com.ojassoft.astrosage.model.FestivalMuhurat;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ojas on २३/१/१८.
 */

public class FestvalDetail extends BaseInputActivity {
    TabLayout tabLayout;
    private ImageView img;
    LinearLayout festivalDetail;
    Typeface typeface;
    Boolean isvrat = false;
    private CustomProgressDialog pd = null;
    private String langCode = null;
    private RequestQueue queue;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView festHeading, festDesc;
    private TextView festDayMonth, festDay, festTithi, festMonthAmanta, festHinduPaksh, festSamvat;
    private AajKaPanchangCalulation calculation;
    private AajKaPanchangModel model;
    private BeanPlace beanPlace;
    ImageLoader imageLoader;
    String festivalUrl;
    ImageView cutterImage;
    BeanHoroPersonalInfo beanHoroPersonalInfo;
    LinearLayout linearLayout;

    ArrayList<DetailApiModel> allDetailData;

    public FestvalDetail() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.festival_detail_layout);
        festivalUrl = getIntent().getStringExtra("festurl");
        isvrat = getIntent().getBooleanExtra("isvrat", false);
        allDetailData = (ArrayList<DetailApiModel>) getIntent().getSerializableExtra("detailapi");
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        imageLoader = VolleySingleton.getInstance(this).getImageLoader();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();// ADDED BY HEVENDRA ON 24-12-2014
        langCode = CUtils.getLanguageKey(LANGUAGE_CODE);

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        cutterImage = (ImageView) findViewById(R.id.backImage);


        initilizeViews();
        addRecyclerView();
        beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        if (CUtils.getBeanHoroPersonalInfo(this) != null || CUtils.getBeanPalce(FestvalDetail.this) != null) {
            beanHoroPersonalInfo = CUtils.getBeanHoroPersonalInfo(this);
            beanPlace = CUtils.getBeanPalce(FestvalDetail.this);
        } else {
            beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        }
        // findColorFromBitmap(bitmap);
        setToolbarItem();

        if (!CUtils.isConnectedWithInternet(FestvalDetail.this)) {
            MyCustomToast mct = new MyCustomToast(FestvalDetail.this, FestvalDetail.this
                    .getLayoutInflater(), FestvalDetail.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            checkCachedData(festivalUrl + "?cityid=" + allDetailData.get(0).getCityId() + "?year=" + allDetailData.get(0).getYear());
        }


    }

    private void initilizeViews() {
        linearLayout = (LinearLayout) findViewById(R.id.backgroundLayout);
        img = (ImageView) findViewById(R.id.festival_image_layout);
        festivalDetail = (LinearLayout) findViewById(R.id.festival_Detail_Layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.muhuratRecyclerView);
        festHeading = (TextView) findViewById(R.id.fest_main_heading);
        festDesc = (TextView) findViewById(R.id.fest_description);
        festDayMonth = (TextView) findViewById(R.id.fest_D_DayMonth);
        festDay = (TextView) findViewById(R.id.fest_D_Day);
        festTithi = (TextView) findViewById(R.id.fest_tithi_value);
        festMonthAmanta = (TextView) findViewById(R.id.fest_month_value);
        festHinduPaksh = (TextView) findViewById(R.id.fest_paksh_value);
        festSamvat = (TextView) findViewById(R.id.fest_samvat_value);
        festHeading.setTypeface(robotRegularTypeface);
        festDesc.setTypeface(robotRegularTypeface);
        festDayMonth.setTypeface(typeface);
        festDay.setTypeface(typeface);
        festTithi.setTypeface(typeface);
        festMonthAmanta.setTypeface(typeface);
        festHinduPaksh.setTypeface(typeface);
        festSamvat.setTypeface(typeface);

    }

    private void addRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    // method is used to check if data in cache
    private void checkCachedData(String postUrl) {
        Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(postUrl);
        if (entry != null) {
            try {
                String saveData = new String(entry.data, "UTF-8");
                parseGsonData(saveData);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {

            if (!CUtils.isConnectedWithInternet(this)) {
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                getDetailFromServer(postUrl);
            }
        }
    }

    // end checkCacheData
// methos is used for network call using volley without using cache
    private void getDetailFromServer(String postUrl) {
        pd = new CustomProgressDialog(FestvalDetail.this, typeface);
        pd.show();
        pd.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //        //Log.e("Simple+" + response.toString());
                        if (response != null && !response.isEmpty()) {
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                            //Log.e("Element" + element.toString());
                            parseGsonData(response);

                        }
                        pd.dismiss();
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                try {
                    MyCustomToast mct = new MyCustomToast(FestvalDetail.this, FestvalDetail.this
                            .getLayoutInflater(), FestvalDetail.this, typeface);
                    mct.show(error.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                    //      loadAstroShopData();
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
                headers.put("key", CUtils.getApplicationSignatureHashCode(FestvalDetail.this));
                headers.put("isapi", "1");
                String cityIdToSend = null;
                if (allDetailData.get(0).getCityId() != null) {
                    cityIdToSend = allDetailData.get(0).getCityId();
                } else {
                    cityIdToSend = "";
                }
                headers.put("date", allDetailData.get(0).getYear());
                headers.put("lid", cityIdToSend);
                headers.put("language", allDetailData.get(0).getLangCode());
                return headers;
            }

        };


        ;
// Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    // end getDetailFromServer


    // method is used to parse response from server using google gson lib
    private void parseGsonData(String saveData) {
        try {
            FestivalDetailData festivalDetailData;
            Gson gson = new Gson();
            festivalDetailData = gson.fromJson(saveData, FestivalDetailData.class);

            List<FestivalMuhurat> festivalMuhurats = festivalDetailData.getFestivalapidata().get(0).getFestivalMuhurat();
            downloadImage(img, festivalDetailData.getFestivalapidata().get(0).getFestivalImageUrl());

            setDataToView(festivalDetailData);
            setAdapterForMuhurat(festivalMuhurats);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // end parseGsonData

    // metod is used to set data to their respective view
    private void setDataToView(FestivalDetailData festivalDetailData) {
        festHeading.setText(festivalDetailData.getFestivalapidata().get(0).getFestivalName());
        //  Linkify.addLinks(festHeading,Linkify.ALL);
        //festDesc.setText(festivalDetailData.getFestivalapidata().get(0).getFestivalContant());
        String desc = festivalDetailData.getFestivalapidata().get(0).getFestivalContant().replace("=/", "=" + CGlobalVariables.PANCHANG_BASE_URL);
        festDesc.setText(Html.fromHtml(desc));
        try {
            festDesc.setMovementMethod(new MovementCheck());
        } catch (Exception e) {
            e.printStackTrace();
        }


        //img.setImageUrl(festivalDetailData.getFestivalapidata().get(0).getFestivalImageUrl(), imageLoader);
        String dateOfFest = festivalDetailData.getFestivalapidata().get(0).getFestivalDate();
        String[] separated = dateOfFest.split("/");
        ArrayList<Integer> dayandMonth = CUtils.returDayandMonth(dateOfFest);

        String[] dayName = getResources().getStringArray(R.array.week_day_sunday_to_saturday_list);
        String[] monthName = getResources().getStringArray(R.array.MonthName);
        if (LANGUAGE_CODE == 1) {
            String dayHoleName = dayName[(dayandMonth.get(0)) - 1];
            festDay.setText("(" + dayHoleName + ")");

        } else {
            String dayHoleName = "(" + dayName[(dayandMonth.get(0)) - 1] + ")";
            festDay.setText(dayHoleName);

        }

        if (LANGUAGE_CODE == 1) {
            String monthHoleName = separated[0] + " " + monthName[dayandMonth.get(1)] + ", " + separated[2];
            festDayMonth.setText(monthHoleName);

        } else if (LANGUAGE_CODE == 0) {

            String monthHoleName = separated[0] + " " + monthName[dayandMonth.get(1)] + ", " + separated[2];
            festDayMonth.setText(monthHoleName);

        } else {
            String monthHoleName = separated[0] + " " + monthName[dayandMonth.get(1)] + ", " + separated[2];
            festDayMonth.setText(monthHoleName);
        }


        // set titi and other stuff

        // setTithiandOther(dateOfFest);


    }

    // end setDataToView

    // this class is used to check if any bad url in link
    private class MovementCheck extends LinkMovementMethod {

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            try {
                return super.onTouchEvent(widget, buffer, event);
            } catch (Exception ex) {
                // Toast.makeText( FestvalDetail.this, "Bad Url", Toast.LENGTH_LONG ).show();
                return true;
            }
        }

    }
    // end movement cahek class

    //this method is used to download image using volley image loader
    private void downloadImage(final ImageView img, String festivalImageUrl) {
        imageLoader.get(festivalImageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                  /*  BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                    Bitmap bitmap2 = drawable.getBitmap();*/
                    Bitmap bitmap1 = cutBimtmap(bitmap);
                    cutterImage.setImageBitmap(bitmap1);


     /*               BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap1);
                    bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    linearLayout.setBackgroundDrawable(bitmapDrawable);
*/

                }


                // Bitmap croppedBitmap = Bitmap.createBitmap(response.getBitmap(), 10, 10, response.getBitmap().getWidth() - 20, response.getBitmap().getHeight() - 20)

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    // end volley image loader
//this method is used to cut a pixel of image
    private Bitmap cutBimtmap(Bitmap bitmap) {
        Bitmap bmOverlay = Bitmap.createBitmap(10, bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmap, 0, 0, null);

        return bmOverlay;
    }

    //end cut a pixelof image

/*    private void setTithiandOther(String dateOfFest) {
        Date date = new Date(dateOfFest);
        try {

            String lat;
            String lng;
            String timeZone;
            String timeZoneString;
            String cityId = String.valueOf(beanPlace.getCityId());
            if (langCode.equals("")) {
                langCode = "en";
            }

            if (cityId.equals("-1")) {
                cityId = "1261481";
            }

            lat = "0";
            lng = "0";
            timeZone = "0";
            timeZoneString = "";

            if (beanPlace != null) {
                lat = beanPlace.getLatitude();
                lng = beanPlace.getLongitude();
                timeZone = beanPlace.getTimeZone();
                timeZoneString = beanPlace.getTimeZoneString();
            }


            AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(date, cityId, langCode, lat, lng, timeZone, timeZoneString);
            AajKaPanchangModel model = calculation.getPanchang();

            //festTithi.setText(model.getTithiValue());
           // festMonthAmanta.setText(model.getMonthAmanta());
           // festHinduPaksh.setText(model.getPakshaName());
           // festSamvat.setText(model.getVikramSamvat());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    //method is used to display muhurat in festival
    private void setAdapterForMuhurat(List<FestivalMuhurat> festivalMuhurats) {
        if (festivalMuhurats.size() > 0) {
            mAdapter = new FestivalDetailAdapter(this, festivalMuhurats, LANGUAGE_CODE, robotRegularTypeface);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

//end to display muhurat in festival

    private void setToolbarItem() {
        Toolbar tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (isvrat) {
            tvTitle.setText(R.string.vratdetail);

        } else {
            tvTitle.setText(getResources().getString(R.string.fest_detail));
        }
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
