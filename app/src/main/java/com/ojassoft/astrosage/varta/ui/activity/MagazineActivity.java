package com.ojassoft.astrosage.varta.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.adapters.MagazineAdapter;
import com.ojassoft.astrosage.varta.model.CMessage;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MagazineActivity extends BaseActivity implements VolleyResponse {

    static final String CHANNEL = "channel";//
    static final String ITEM = "item";//

    static final String TITLE = "title";
    static final String LINK = "link";
    static final String PUB_DATE = "pubDate";
    static final String CREATOR = "creator";
    static final String CATEGORY = "category";
    static final String GUID = "guid";
    static final String DESCRIPTION = "description";
    static final String ENCODED = "encoded";
    static final String CONTENT = "content";


    private CustomProgressDialog pd;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magazine_layout);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vollyGetRequest();
    }

    public void vollyGetRequest() {
        showProgressBar();
        String url = "https://feeds.feedburner.com/astrosageblog?format=xml";
        HashMap<String, String> params = new HashMap<String, String>();
        RequestQueue queue = VolleySingleton.getInstance(MagazineActivity.this).getRequestQueue();
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.GET, url, MagazineActivity.this, true, params, 1).getMyStringRequest();
        queue.add(stringRequest);
    }

    public List<CMessage> parseXML(String response) {
        List<CMessage> messages = null;
        XmlPullParser parser = Xml.newPullParser();
        try {

            // auto-detect the encoding from the stream
            parser.setInput(new StringReader(response));

            int eventType = parser.getEventType();
            CMessage currentMessage = null;
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        messages = new ArrayList<CMessage>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            currentMessage = new CMessage();
                        } else if (currentMessage != null) {
                            if (name.equalsIgnoreCase(TITLE)) {
                                currentMessage.setTitle(parser.nextText());
                            } else if (name.equalsIgnoreCase(LINK)) {
                                currentMessage.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                currentMessage.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(CREATOR)) {
                                currentMessage.setCreator(parser.nextText());
                            } else if (name.equalsIgnoreCase(CATEGORY)) {
                                currentMessage.setCategory(parser.nextText());
                            } else if (name.equalsIgnoreCase(GUID)) {//ADDED BY DEEPAK ON 16-12-2014
                                currentMessage.setPostId(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                currentMessage.setDescription(parser.nextText());
                            } else if (name.equalsIgnoreCase(ENCODED)) {
                                currentMessage.setEncoded(parser.nextText());
                            } else if (name.equalsIgnoreCase(CONTENT)) {
                                String imgUrl = parser.getAttributeValue(null, "url");
                                currentMessage.setImageUrl(imgUrl);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM) &&
                                currentMessage != null) {
                            messages.add(currentMessage);
                        } else if (name.equalsIgnoreCase(CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
            Log.i("size==", messages.size() + "");
        } catch (Exception e) {
            messages = null;
        }
        return messages;
    }

    @Override
    public void onResponse(String response, int method) {
        try {
            hideProgressBar();
            List<CMessage> list = parseXML(response);
            MagazineAdapter magazineAdapter = new MagazineAdapter(MagazineActivity.this, list);
            recyclerView.setAdapter(magazineAdapter);
        } catch (Exception e) {

        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }

    private void showProgressBar() {

        pd = new CustomProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
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
