package com.libojassoft.android.customrssfeed;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.utils.LibCUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ojas-02 on 11/11/16.
 */
public class CXmlPullFeedParserForYearly extends CBaseFeedParser implements VolleyResponse {

    private Context context;
    SendDataBackToComponent sendDataBackToComponent;

    public CXmlPullFeedParserForYearly(String feedUrl, Context context) {
        super(feedUrl);
        this.context = context;
    }

    public CXmlPullFeedParserForYearly(String feedUrl, Context context, SendDataBackToComponent sendDataBackToComponent) {
        super(feedUrl);
        this.context = context;
        this.sendDataBackToComponent = sendDataBackToComponent;
    }

    public void getDataFromServer(Context context, String feedUrl, Map<String, String> params, int method) {

       /* if (feedUrl.contains("type")||feedUrl.contains("Language")) {

            feedUrl = feedUrl + "&key=" + params.get("key");
        } else {
            feedUrl = feedUrl + "?key=" + params.get("key");
        }*/
        feedUrl = feedUrl + "&key=" + params.get("key");
        LibCUtils.vollyGetRequest(context, CXmlPullFeedParserForYearly.this, feedUrl, method);
    }

    public List<CMessage> parse() {
        List<CMessage> messages = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            // auto-detect the encoding from the stream
            //parser.setInput(this.getInputStream(context), "UTF-8");
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
                            }/* else if (name.equalsIgnoreCase(DESCRIPTION)) {
                               // currentMessage.setDescription(parser.nextText());

                            }*/ else if (name.equalsIgnoreCase(PUB_DATE)) {
                                currentMessage.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                currentMessage.setTitle(parser.nextText());
                            } else if (name.equalsIgnoreCase(GUID)) {//ADDED BY DEEPAK ON 16-12-2014
                                currentMessage.setPostId(parser.nextText());
                            } else if (name.equalsIgnoreCase(HEALTH)) {
                                currentMessage.setHealth(parser.nextText());
                            } else if (name.equalsIgnoreCase(CAREER)) {
                                currentMessage.setCareer(parser.nextText());
                            } else if (name.equalsIgnoreCase(Love_Marriage_PersonalRelations)) {
                                currentMessage.setLoveMarriagePersonalReltaion(parser.nextText());
                            } else if (name.equalsIgnoreCase(Advice)) {
                                currentMessage.setAdvice(parser.nextText());
                            } else if (name.equalsIgnoreCase(General)) {
                                currentMessage.setGeneral(parser.nextText());
                            } else if (name.equalsIgnoreCase(Finance)) {
                                currentMessage.setFinance(parser.nextText());
                            } else if (name.equalsIgnoreCase(Trade_Finance)) {
                                currentMessage.setTradeFinance(parser.nextText());
                            } else if (name.equalsIgnoreCase(Family_Friends)) {
                                currentMessage.setFamilyFriends(parser.nextText());
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
    }

    @Override
    public void onError(VolleyError error) {
        if (sendDataBackToComponent != null && error != null) {
            sendDataBackToComponent.doActionOnError(error.getMessage());
        }
    }
}
