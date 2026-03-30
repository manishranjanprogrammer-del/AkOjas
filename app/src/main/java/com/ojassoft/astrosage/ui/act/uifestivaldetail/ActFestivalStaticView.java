package com.ojassoft.astrosage.ui.act.uifestivaldetail;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.model.FestivalDetailData;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas-02 on 22/3/18.
 */

public class ActFestivalStaticView extends BaseInputActivity {
    TabLayout tabLayout;
    private ImageView img;
    Typeface typeface;
    private CustomProgressDialog pd = null;
    private String langCode = null;
    private RequestQueue queue;

    private TextView festHeading, festDesc;

    ImageLoader imageLoader;
    String festivalUrl;
    ImageView cutterImage;
    ArrayList<DetailApiModel> allDetailData;


    public ActFestivalStaticView() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.festival_static_layout);
        festivalUrl = getIntent().getStringExtra("festurl");
        allDetailData = (ArrayList<DetailApiModel>) getIntent().getSerializableExtra("detailapi");
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        imageLoader = VolleySingleton.getInstance(this).getImageLoader();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();// ADDED BY HEVENDRA ON 24-12-2014
        langCode = CUtils.getLanguageKey(LANGUAGE_CODE);

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        cutterImage = (ImageView) findViewById(R.id.backImageStatic);
        initilizeViews();

        // findColorFromBitmap(bitmap);
        setToolbarItem();

        if (!CUtils.isConnectedWithInternet(ActFestivalStaticView.this)) {
            MyCustomToast mct = new MyCustomToast(ActFestivalStaticView.this, ActFestivalStaticView.this
                    .getLayoutInflater(), ActFestivalStaticView.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            checkCachedData(festivalUrl + "?cityid=" + allDetailData.get(0).getCityId() + "?year=" + allDetailData.get(0).getYear());

        }
    }


    private void setToolbarItem() {
        Toolbar tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.fest_detail));
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

    private void initilizeViews() {
        img = (ImageView) findViewById(R.id.festival_image_layout_static);
        festHeading = (TextView) findViewById(R.id.fest_main_heading_static);
        festDesc = (TextView) findViewById(R.id.fest_description_static);
        festHeading.setTypeface(robotRegularTypeface);
        festDesc.setTypeface(robotRegularTypeface);
    }


    private void checkCachedData(String postUrl) {
        pd = new CustomProgressDialog(ActFestivalStaticView.this, typeface);
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
                MyCustomToast mct = new MyCustomToast(ActFestivalStaticView.this, ActFestivalStaticView.this
                        .getLayoutInflater(), ActFestivalStaticView.this, typeface);
                mct.show(error.getMessage());


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
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActFestivalStaticView.this));
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

    private void parseGsonData(String saveData) {
        try {
            FestivalDetailData festivalDetailData;
            Gson gson = new Gson();
            festivalDetailData = gson.fromJson(saveData, FestivalDetailData.class);

            downloadImage(img, festivalDetailData.getFestivalapidata().get(0).getFestivalImageUrl());

            setDataToView(festivalDetailData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataToView(FestivalDetailData festivalDetailData) {
        festHeading.setText(festivalDetailData.getFestivalapidata().get(0).getFestivalName());
        //festDesc.setText(Html.fromHtml(festivalDetailData.getFestivalapidata().get(0).getFestivalContant()));

        String desc = festivalDetailData.getFestivalapidata().get(0).getFestivalContant().replace("=/", "=" + CGlobalVariables.PANCHANG_BASE_URL);
        festDesc.setText(Html.fromHtml(desc));
        try {
            festDesc.setMovementMethod(new MovementCheck());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

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
    private void downloadImage(final ImageView img, String festivalImageUrl) {
        imageLoader.get(festivalImageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                    Bitmap bitmap1 = cutBimtmap(bitmap);
                    cutterImage.setImageBitmap(bitmap1);

                }


                // Bitmap croppedBitmap = Bitmap.createBitmap(response.getBitmap(), 10, 10, response.getBitmap().getWidth() - 20, response.getBitmap().getHeight() - 20)

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getCause().getMessage());

            }
        });
    }

    private Bitmap cutBimtmap(Bitmap bitmap) {
        Bitmap bmOverlay = Bitmap.createBitmap(10, bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bitmap, 0, 0, null);

        return bmOverlay;
    }
}
