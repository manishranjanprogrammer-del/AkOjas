package com.libojassoft.android.customrssfeed;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class CBaseFeedParser implements IFeedParser {

    // names of the XML tags
    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String HEALTH = "Health";
    static final String CAREER = "Career";
    static final String Love_Marriage_PersonalRelations = "Love_Marriage_PersonalRelations";
    static final String Advice = "Advice";
    static final String General = "General";
    static final String Finance = "Finance";
    static final String Trade_Finance = "Trade_Finance";
    static final String Family_Friends = "Family_Friends";
    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";//
    static final String CHANNEL = "channel";//
    static final String GUID = "guid";

    final URL feedUrl;

    protected CBaseFeedParser(String feedUrl) {
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

  /*  protected InputStream getInputStream(Context context) {
        try {
            HttpsTrustManager.allowAllSSL();
            Map<String, String> params = new HashMap<>();
            params.put("key", LibCUtils.getApplicationSignatureHashCode(context));

            URL url = new URL(feedUrl.toString());
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setConnectTimeout(15000);

            httpConn.setReadTimeout(15000);

            httpConn.setUseCaches(false);

            httpConn.setDoInput(true); // true indicates the server returns response

            StringBuffer requestParams = new StringBuffer();

            if (params != null && params.size() > 0) {

                httpConn.setDoOutput(true); // true indicates POST request

                // creates the params string, encode them using URLEncoder
                Iterator<String> paramIterator = params.keySet().iterator();
                int i = 0;
                while (paramIterator.hasNext()) {
                    if (i > 0 && i < params.size()) {
                        requestParams.append("&");
                    }
                    i++;
                    String key = paramIterator.next();
                    String value = params.get(key);
                    requestParams.append(URLEncoder.encode(key, "UTF-8"));
                    requestParams.append("=").append(
                            URLEncoder.encode(value, "UTF-8"));
                }
            }

            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();
            int status = httpConn.getResponseCode();
            Log.i("", status + "");

            //return feedUrl.openConnection().getInputStream();
            return httpConn.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

   /* protected InputStream getInputStreamForArticles(Context context)
            throws Exception {

        long[] char_msg = new long[2];
        BufferedReader in = null;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        String url = feedUrl.toString();
        HttpGet hp = new HttpGet(url);

//		HttpPost hp = new HttpPost("http://192.168.1.18/astrosage-xml/savechart-xml.asp?");
        HttpResponse response = hc.execute(hp);
		*//*try {
			//hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_SaveChartOnServer(context), HTTP.UTF_8));

			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String data = "";
			while ((data = in.readLine()) != null)
				sb.append(data);

			in.close();

			String char_msg1 = sb.toString();


		} catch (Exception e) {
			throw e;
		}*//*

        return response.getEntity().getContent();

    }*/

   /* private List<NameValuePair> getNameValuePairs_SaveChartOnServer(Context context) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(23);
        nameValuePairs.add(new BasicNameValuePair("key", LibCUtils.getApplicationSignatureHashCode(context)));

        return nameValuePairs;
    }*/

  /*  private HttpParams getHttpClientTimeoutParameter() {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 1000 * 60;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 1000 * 60;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return httpParameters;
    }*/

   /* public class RssHandler extends DefaultHandler {
        private List<CMessage> messages;
        private CMessage currentMessage;
        private StringBuilder builder;
        static final String PUB_DATE = "pubDate";
        static final String DESCRIPTION = "description";
        static final String LINK = "link";
        static final String TITLE = "title";
        static final String ITEM = "item";


        public List<CMessage> getMessages() {
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
            if (this.currentMessage != null) {
                if (localName.equalsIgnoreCase(TITLE)) {
                    currentMessage.setTitle(builder.toString());
                } else if (localName.equalsIgnoreCase(LINK)) {
                    currentMessage.setLink(builder.toString());
                } else if (localName.equalsIgnoreCase(DESCRIPTION)) {
                    currentMessage.setDescription(builder.toString());
                } else if (localName.equalsIgnoreCase(PUB_DATE)) {
                    currentMessage.setDate(builder.toString());
                } else if (localName.equalsIgnoreCase(ITEM)) {
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
            if (localName.equalsIgnoreCase(ITEM)) {
                this.currentMessage = new CMessage();
            }
        }
    }*/
}
