package com.libojassoft.android.customrssfeed;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.utils.LibCUtils;

public class CXmlPullFeedParser extends CBaseFeedParser implements VolleyResponse {
    private Context context;
    boolean isForHoroscope;
    SendDataBackToComponent sendDataBackToComponent;


    public CXmlPullFeedParser(String feedUrl, Context context, SendDataBackToComponent sendDataBackToComponent, boolean isForHoroscope) {
        super(feedUrl);
        this.context = context;
        this.sendDataBackToComponent = sendDataBackToComponent;
        this.isForHoroscope = isForHoroscope;
    }

    public CXmlPullFeedParser(String feedUrl, Context context, boolean isForHoroscope) {
        super(feedUrl);
        this.context = context;
        this.isForHoroscope = isForHoroscope;
    }

    public void getDataFromServer(Context context, String feedUrl, Map<String, String> params, int method) {
        if (feedUrl.contains("type")||feedUrl.contains("Language")||feedUrl.contains("day")) {

            feedUrl = feedUrl + "&key=" + params.get("key");
        } else {
            feedUrl = feedUrl + "?key=" + params.get("key");
        }

        LibCUtils.vollyGetRequest(context, CXmlPullFeedParser.this, feedUrl, method);
    }


    public List<CMessage> parse() {
        List<CMessage> messages = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            // auto-detect the encoding from the stream
           /* if (isForHoroscope) {
                parser.setInput(this.getInputStream(context), "UTF-8");
            } else {
                parser.setInput(this.getInputStreamForArticles(context), "UTF-8");
            }*/
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
                            if (name.equalsIgnoreCase(LINK)) {
                                currentMessage.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                currentMessage.setDescription(parser.nextText());
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                currentMessage.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                currentMessage.setTitle(parser.nextText());
                            } else if (name.equalsIgnoreCase(GUID)) {//ADDED BY DEEPAK ON 16-12-2014
                                currentMessage.setPostId(parser.nextText());
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
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
            throw new RuntimeException(e);
        }
        return messages;
    }

    @Override
    public void onResponse(String response, int method) {
        if (sendDataBackToComponent != null) {
            sendDataBackToComponent.doActionAfterGetResult(response, method);
        }
       /* List<CMessage> messages = null;
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
                            if (name.equalsIgnoreCase(LINK)) {
                                currentMessage.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                currentMessage.setDescription(parser.nextText());
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                currentMessage.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                currentMessage.setTitle(parser.nextText());
                            } else if (name.equalsIgnoreCase(GUID)) {//ADDED BY DEEPAK ON 16-12-2014
                                currentMessage.setPostId(parser.nextText());
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
            //Log.e("Error", e.getMessage());
            throw new RuntimeException(e);
        }*/
    }

    @Override
    public void onError(VolleyError error) {
        if (sendDataBackToComponent != null && error != null) {
            sendDataBackToComponent.doActionOnError(error.getMessage());
        }
    }
}
