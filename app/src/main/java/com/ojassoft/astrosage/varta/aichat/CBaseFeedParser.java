package com.ojassoft.astrosage.varta.aichat;

import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.customrssfeed.IFeedParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class CBaseFeedParser implements IFeedParser {

    // names of the XML tags
    static final String PUB_DATE = "pubDate";
    static final  String DESCRIPTION = "description";
    static final  String LINK = "link";
    static final  String TITLE = "title";
    static final  String ITEM = "item";//
    static final  String CHANNEL = "channel";//
    static final String GUID = "guid";
    
    final URL feedUrl;

    protected CBaseFeedParser(String feedUrl){
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    protected InputStream getInputStream() {
        try {
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public class RssHandler extends DefaultHandler {
	    private List<CMessage> messages;
	    private CMessage currentMessage;
	    private StringBuilder builder;
	    static final String PUB_DATE = "pubDate";
	    static final  String DESCRIPTION = "description";
	    static final  String LINK = "link";
	    static final  String TITLE = "title";
	    static final  String ITEM = "item";
	    

	    
	    public List<CMessage> getMessages(){
	        return this.messages;
	    }
	    @Override
	    public void characters(char[] ch, int start, int length)
	            throws SAXException {
	        super.characters(ch, start, length);
	        builder.append(ch, start, length);
	    }

	    @Override
	    public void endElement(String uri, String localName, String name)
	            throws SAXException {
	        super.endElement(uri, localName, name);
	        if (this.currentMessage != null){
	            if (localName.equalsIgnoreCase(TITLE)){
	                currentMessage.setTitle(builder.toString());
	            } else if (localName.equalsIgnoreCase(LINK)){
	                currentMessage.setLink(builder.toString());
	            } else if (localName.equalsIgnoreCase(DESCRIPTION)){
	                currentMessage.setDescription(builder.toString());
	            } else if (localName.equalsIgnoreCase(PUB_DATE)){
	                currentMessage.setDate(builder.toString());
	            } else if (localName.equalsIgnoreCase(ITEM)){
	                messages.add(currentMessage);
	            }
	            builder.setLength(0);    
	        }
	    }

	    @Override
	    public void startDocument() throws SAXException {
	        super.startDocument();
	        messages = new ArrayList<CMessage>();
	        builder = new StringBuilder();
	    }

	    @Override
	    public void startElement(String uri, String localName, String name,
	            Attributes attributes) throws SAXException {
	        super.startElement(uri, localName, name, attributes);
	        if (localName.equalsIgnoreCase(ITEM)){
	            this.currentMessage = new CMessage();
	        }
	    }
	}
}