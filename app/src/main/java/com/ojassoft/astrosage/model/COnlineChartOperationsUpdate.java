package com.ojassoft.astrosage.model;
/*
import java.io.BufferedReader;
import java.io.InputStreamReader;*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

//import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
/*import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;*/
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
//import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.jinterface.IOnlineChartOperations;
import com.ojassoft.astrosage.jinterface.IOnlineChartOperationsUpdate;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CXMLOperations;


public class COnlineChartOperationsUpdate implements IOnlineChartOperationsUpdate {
    /**
     * These are the  variables  used to contain user id,password and chart id.
     */
    //private String uid="", pwd="", chartid;
    /**
     * This variables is used for login verification value.
     */
    private String verifyingLoginValue = "-1";
    private String userPurchasedPlanId = "1";

    /**
     * This is a string array to contain online chart list .
     */
    private Map<String, String> chartListArray;

    /**
     * This variable is used to contain Chart detail.
     */
    private BeanHoroPersonalInfo _horoPersonalInfo;
    private String updatedOnlineChartId = null;

    /**
     * This function is used to check/verify user login
     *
     * @param _uid
     * @param _pwd
     * @return integer value
     * @throws Exception
     */

	/*public int verifyLogin(String _uid, String _pwd) throws Exception {
        int iStatus = -1;
		try {			
			    executeHTTRequestVerifyingLogin(_uid, _pwd);
			    iStatus = Integer.valueOf(getVerifyingLoginValue());
			    ////Log.e("UserPlan", this.userPurchasedPlanId);
		} catch (Exception e) {
			throw e;
		}
		return iStatus;

	}*/
   /* public int[] verifyLoginWithUserPurchasedPlan(String _uid, String _pwd) throws Exception {
        int[] iStatus = new int[]{-1, 1};

        try {
            executeHTTRequestVerifyingLogin(_uid, _pwd);
            iStatus[0] = Integer.valueOf(getVerifyingLoginValue());
            iStatus[1] = Integer.valueOf(this.userPurchasedPlanId);
            ////Log.e("UserPlan", this.userPurchasedPlanId);
        } catch (Exception e) {
            throw e;
        }
        return iStatus;

    }*/

    /**
     * This function is used to execute online request to verify user login.
     *
     * @param userid
     * @param pwd
     * @throws Exception
     */
    /*private void executeHTTRequestVerifyingLogin(String userid, String pwd)
            throws Exception {
        String strReturn = "";
        BufferedReader in = null;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        HttpPost hp = new HttpPost(CGlobalVariables.VERIFYING_LOGIN);
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Login(
                    userid, pwd), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            setVerifyingLoginValue(sb.toString());

        } catch (Exception e) {

            throw e;
        }

    }*/

    /**
     * This function prepare and return time out parameter for
     * online connection.
     *
     * @return HttpParams
     */
    private HttpParams getHttpClientTimeoutParameter() {
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
    }

    /**
     * This function return list of name value pairs to  verify login
     *
     * @param userid
     * @param pwd
     * @return List<NameValuePair>
     */

    private List<NameValuePair> getNameValuePairs_Login(String userid,
                                                        String pwd) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userid)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));

        return nameValuePairs;

    }

    /**
     * This function is used to extract login status value
     * returned from server after executing the login verification request.
     *
     */
    public void setVerifyingLoginValue(String loginValue) {
        Document doc = CXMLOperations.XMLfromString(loginValue);
        int numResults = doc.getChildNodes().getLength();
        if ((numResults <= 0))
            this.verifyingLoginValue = "-1";
        else {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                try {

                    //LOGIN
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    String expression = "/product/msgcode"; // first book
                    Node logValue = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
                    if (logValue != null)
                        this.verifyingLoginValue = logValue.getTextContent();
                    // USER PURCHASED PLAN ID
                    String expressionPlanId = "/product/userplanid"; // first book
                    Node logValuePlanId = (Node) xpath.evaluate(expressionPlanId, doc, XPathConstants.NODE);
                    if (logValuePlanId != null)
                        this.userPurchasedPlanId = logValuePlanId.getTextContent();


                } catch (Exception e) {

                }
            } else {
                NodeList nodes = doc.getElementsByTagName("msgcode");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element e = (Element) nodes.item(i);
                    this.verifyingLoginValue = CXMLOperations.getValue(e, "msgcode");
                }
                // USER PURCHASED PLAN ID
                NodeList nodesPlanId = doc.getElementsByTagName("userplanid");
                for (int i = 0; i < nodesPlanId.getLength(); i++) {
                    Element e = (Element) nodesPlanId.item(i);
                    this.userPurchasedPlanId = CXMLOperations.getValue(e, "userplanid");
                }

            }

        }


    }

    /**
     * This getter is used to get login verification value
     *
     * @return
     */
   /* public String getVerifyingLoginValue() {
        return verifyingLoginValue;
    }*/

    /*@Override
    public Map<String, String> getOnlineChartList(String kundliName, String uid, String pwd) throws Exception {
        try {
            executeHTTRequestChartList(kundliName, uid, pwd);

        } catch (Exception e) {
            throw e;
        }

        return chartListArray;
    }*/

    /**
     * This function is used to get chart list from astrosage server
     * according to uid ,pwd and chart name.
     *
     * @param uid
     * @param pwd
     * @throws Exception
     */
    /*private void executeHTTRequestChartList(String kundliName, String uid, String pwd) throws Exception {
        BufferedReader in = null;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        //CGlobalVariables.ONLINE_CHART_LIST_URL
        HttpPost hp = new HttpPost();
//		HttpPost hp = new HttpPost("http://192.168.1.18/astrosage-xml/chartnamebyuserid-xml.asp?");
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_ChartList(
                    uid, pwd, kundliName), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();
            // Toast.makeText(_context, sb.toString(),
            // Toast.LENGTH_LONG).show();

            setChartList(sb.toString());

        } catch (Exception e) {
            throw e;
        }
    }*/

    /**
     * This function return list of name value pairs to get chart list
     * from server according to uid,pwd and chart name.
     *
     * @param uid
     * @param pwd
     * @param searchText
     * @return List<NameValuePair>
     */
    private List<NameValuePair> getNameValuePairs_ChartList(String uid,
                                                            String pwd, String searchText) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(uid)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("ChartName", searchText));

        return nameValuePairs;

    }


    private void setChartList(String _chartList) {
//		String data = "";
//		String _id = "", _name = "";

        _chartList = _chartList.replace("<![CDATA[", "");
        _chartList = _chartList.replace("]]>", "");
        _chartList = _chartList.replace("&", "");
        Document doc = CXMLOperations.XMLfromString(_chartList);


        chartListArray = new HashMap<String, String>();
        try {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {

                //FOR CHART ID
                XPath xpath = XPathFactory.newInstance().newXPath();
                String chartIdExp = "/product/chartId"; // chart id
                NodeList chartIdNodes = (NodeList) xpath.evaluate(chartIdExp, doc, XPathConstants.NODESET);
                //FOR CHART NAME
                XPath xpathName = XPathFactory.newInstance().newXPath();
                String chartNameExp = "/product/ChartName"; // chart id
                NodeList chartNameNodes = (NodeList) xpathName.evaluate(chartNameExp, doc, XPathConstants.NODESET);

                for (int i = 0; i < chartIdNodes.getLength(); i++) {
                    chartListArray.put(chartIdNodes.item(i).getTextContent(), chartNameNodes.item(i).getTextContent());
                }
            } else {
                NodeList nodes = doc.getElementsByTagName("chartId");
                NodeList nodes2 = doc.getElementsByTagName("ChartName");

                for (int i = 0; i < nodes.getLength(); i++) {
                    Element e = (Element) nodes.item(i);
                    Element e2 = (Element) nodes2.item(i);
                    chartListArray.put(CXMLOperations.getValue(e, "chartId"), CXMLOperations.getValue(e2, "ChartName"));
                }
            }
        } catch (Exception e) {

        }

    }

   /* @Override
    public int deleteOnlineKundli(long kundliId, String uid, String pwd) throws Exception {
        // TODO Auto-generated method stub
        return executeHTTRequestOnlineKundliDelete(kundliId, uid, pwd);
    }*/

    /**
     * This function is used to delete chart on astrosage server
     * according to chart id.
     *
     * @return int value(success/fail)
     * @throws Exception
     */
    /*private int executeHTTRequestOnlineKundliDelete(long kundliId, String uid, String pwd) throws Exception {

        int _result = -1;
        BufferedReader in = null;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        HttpPost hp = new HttpPost(CGlobalVariables.ONLINE_CHART_DELETE_URL);
        // HttpPost hp=new HttpPost(CGlobalVariables.CHART_DELETE);
        // HttpPost hp=new HttpPost(_url);
        try {
            // Toast.makeText(_context,
            // String.valueOf(_chartId),Toast.LENGTH_LONG).show();
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairsOnlineKundliDelete(kundliId, uid, pwd), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();
            //Toast.makeText(_context, sb.toString(),
            // Toast.LENGTH_LONG).show();
            _result = getChartDeleteStatus(sb.toString());
        } catch (Exception e) {
            throw e;
        }
        return _result;
    }*/

    /**
     * This function return list of name value pairs to  delete  online chart
     * according to chart id passed .
     * @return List<NameValuePair>
     */
    private List<NameValuePair> getNameValuePairsOnlineKundliDelete(long kundliId, String uid, String pwd) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(uid)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("chartId", String.valueOf(kundliId)));

        return nameValuePairs;

    }

    /**
     * This function is used to extract the status after deleting
     * chart on AstroSage server.
     *
     * @param _status
     * @return int(success / fail)
     */
    private int getChartDeleteStatus(String _status) {
        int _result = -1;
        Document doc = CXMLOperations.XMLfromString(_status);
        int numResults = doc.getChildNodes().getLength();
        // Toast.makeText(_context,
        // String.valueOf(_status),Toast.LENGTH_LONG).show();
        NodeList nodes = doc.getElementsByTagName("product");
        if (numResults > 0) {
            try {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                    XPath xpathDeleteStatus = XPathFactory.newInstance().newXPath();
                    String msgCodeExp = "/product/msgcode"; // chart id
                    NodeList msgCodeNodes;
                    try {
                        msgCodeNodes = (NodeList) xpathDeleteStatus.evaluate(msgCodeExp, doc, XPathConstants.NODESET);
                        _result = Integer.valueOf(msgCodeNodes.item(0).getTextContent());
                    } catch (XPathExpressionException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element e = (Element) nodes.item(i);
                        _result = Integer.valueOf(CXMLOperations.getValue(e, "msgcode"));
                    }
                }

            } catch (Exception e) {

            }
        }
        return _result;

    }

    //OPEN ONLINE KUNDLI DETAIL

    /**
     * This function is used to to return online  kundli detail from AstroSage server
     *
     * @param kundliId
     * @param uid
     * @param pwd
     * @return BeanHoroPersonalInfo
     * @throws Exception
     */
   /* @Override
    public BeanHoroPersonalInfo getOnlineKundliDetail(Context context, long kundliId, String uid,
                                                      String pwd) throws Exception {
        // TODO Auto-generated method stub
        //SQLiteDatabase sqLiteDatabase=new CDatabaseHelperOperations(context).getReadableDatabase();
        try {
            executeHTTRequestOnlineKundliDetail(context, kundliId, uid, pwd);
        } catch (Exception e) {
            throw e;
        }

        return _horoPersonalInfo;
    }*/

    /**
     * This function is used to get online chart detail from  astrosage
     * server.
     * @throws Exception
     */
    /*private void executeHTTRequestOnlineKundliDetail(Context context, long kundliId, String uid, String pwd) throws Exception {
        BufferedReader in = null;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        //CGlobalVariables.ONLINE_CHART_DETAIL_URL
        HttpPost hp = new HttpPost();
        try {
            //Log.d("InOnline_1", "1");
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairsKundliDetail(kundliId, uid, pwd), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();
            // Toast.makeText(_context, sb.toString(),
            // Toast.LENGTH_LONG).show();
            //Log.d("InOnline_2", "2");
            setChartDetail(context, sb.toString());
            //Log.d("InOnline_3", sb.toString());

        } catch (Exception e) {
            //Log.d("InOnline_3", e.getMessage());
            throw e;
        }
    }*/


    /**
     * This function return list of name value pairs to get chart detail
     * from server.
     * @return List<NameValuePair>
     */

    private List<NameValuePair> getNameValuePairsKundliDetail(long kundliId, String uid, String pwd) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(uid)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("ChartId", String.valueOf(kundliId)));
        nameValuePairs.add(new BasicNameValuePair("Isapi", "1"));

        return nameValuePairs;

    }

    /**
     * This function is called after fetching chart detail from server
     * and  extract chart detail from  returned XML and filled in object
     *
     * @param _chartDetail
     */
    private void setChartDetail(Context context, String _chartDetail) {

        _chartDetail = _chartDetail.replace("<![CDATA[", "");
        _chartDetail = _chartDetail.replace("]]>", "");
        _chartDetail = _chartDetail.replace("&", "");
        Document doc = CXMLOperations.XMLfromString(_chartDetail);

        NodeList nodes = doc.getElementsByTagName("product");

        if (nodes.getLength() > 0) {
			/*if(Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD)
				setChartDetailFromGINGERBREAD(nodes,doc);
			else
				setChartDetailBeforeGINGERBREAD(nodes);*/
            setChartDetailBeforeGINGERBREAD(context, nodes);

        }
    }

    private void setChartDetailBeforeGINGERBREAD(Context context, NodeList nodes) {
        _horoPersonalInfo = new BeanHoroPersonalInfo();
        try {


            Element e = (Element) nodes.item(0);
            String onlineChartId = CXMLOperations.getValue(e, "chartid").trim();
            //long localChartId = new CDataOperations().getLocalChartIdWithRespactiveOnline(sqLiteDatabase, onlineChartId);
            long localChartId = new CDatabaseHelperOperations(context).
                    getLocalChartIdWithRespactiveOnlineOperation(onlineChartId);

            _horoPersonalInfo.setLocalChartId(localChartId);
            _horoPersonalInfo.setOnlineChartId(onlineChartId);

            //END
            _horoPersonalInfo.setName(CXMLOperations.getValue(e,
                    "chartname").trim());

            _horoPersonalInfo.setGender(CXMLOperations
                    .getValue(e, "gender").trim());

            /****************************************************************/
            String strDOB = CXMLOperations.getValue(e, "date_of_birth").trim();
            String strTOB = CXMLOperations.getValue(e, "time_of_birth").trim();
            strDOB = strDOB.replace("$", "/");
            strTOB = strTOB.replace("$", "/");


            String[] arrDOB = new String[3];
            String[] arrTOB = new String[3];
            arrDOB = strDOB.split("/");
            arrTOB = strTOB.split("/");

            BeanDateTime dTemp = new BeanDateTime();

            dTemp.setDay(Integer.valueOf(arrDOB[0].trim()));
            dTemp.setMonth(Integer.valueOf(arrDOB[1].trim()) - 1);
            dTemp.setYear(Integer.valueOf(arrDOB[2].trim()));

            dTemp.setHour(Integer.valueOf(arrTOB[0].trim()));
            dTemp.setMin(Integer.valueOf(arrTOB[1].trim()));
            dTemp.setSecond(Integer.valueOf(arrTOB[2].trim()));
            _horoPersonalInfo.setDateTime(dTemp);

            /****************************************************************/
            _horoPersonalInfo.setAyanIndex(Integer.valueOf(CXMLOperations
                    .getValue(e, "ayanamsa").trim()));

            BeanPlace objPlace = new BeanPlace();
            _horoPersonalInfo.setCityID(-1);
            objPlace.setCountryId(-1);
            objPlace.setCountryName("not define");
            /****************************************************************/
            objPlace.setCityName(CXMLOperations.getValue(e, "place_of_birth").trim());
            objPlace.setLongDeg(CXMLOperations.getValue(e, "longdeg").trim());
            objPlace.setLongMin(CXMLOperations.getValue(e, "longmin").trim());
            objPlace.setLatDeg(CXMLOperations.getValue(e, "latdeg").trim());
            objPlace.setLatMin(CXMLOperations.getValue(e, "latmin").trim());
            objPlace.setLongDir(CXMLOperations.getValue(e, "longEW").trim());
            objPlace.setLatDir(CXMLOperations.getValue(e, "latNS").trim());

            _horoPersonalInfo.setDST(Integer.parseInt(CXMLOperations.getValue(e, "DST").trim()));

			/*BeanPlace objTz = new CDataOperations().getNewTimZoneByName(sqLiteDatabase,CXMLOperations.getValue(e,
							"timezone").trim());*/

            //updated on 11 feb 2016
            BeanPlace objTz = new CDatabaseHelperOperations(context).getNewTimZoneByNameOperation(
                    CXMLOperations.getValue(e,
                            "timezone").trim());
            if (objTz != null) {
                objPlace.setTimeZoneName(objTz.getTimeZoneName().trim());
                objPlace.setTimeZoneValue(objTz.getTimeZoneValue());
                objPlace.setTimeZoneId(objTz.getTimeZoneId());

            }
            try {
                String timezonevalue = CXMLOperations.getValue(e, "timezone").trim();
                if (timezonevalue.contains("$")) {
                    timezonevalue = timezonevalue.replace("$", "-");
                }
                objPlace.setTimeZoneValue(Float.parseFloat(timezonevalue));
            } catch (Exception ex) {
                //Log.i("error", ex.getMessage().toString());
            }

            _horoPersonalInfo.setPlace(objPlace);


        } catch (Exception e) {

            _horoPersonalInfo = null;
        }
    }

    //ONLINE SACE CHART

    /**
     * This function is used to save  kundli detail on AstroSage cloud
     *
     * @param beanHoroPersonalInfo
     * @throws Exception
     * @returnint[][online kundli idmessage id]
     */
    /*public long[] saveChartOnServer(BeanHoroPersonalInfo beanHoroPersonalInfo, String userId, String pwd) throws Exception {

        return executeHTTRequestSaveChartOnServer(beanHoroPersonalInfo, userId, pwd);
    }*/

    /**
     * This function return list of name value pairs to save chart in online database.
     *
     * @param _birthDetail
     * @return List<NameValuePair>
     */

    /*private List<NameValuePair> getNameValuePairs_SaveChartOnServer(
            BeanHoroPersonalInfo _birthDetail, String userId, String pwd) {
        String _name = _birthDetail.getName().trim();
        String _city = _birthDetail.getPlace().getCityName().trim();
        //_city=_city.replaceAll("_","");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(23);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userId)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));


        nameValuePairs.add(new BasicNameValuePair("Name", _name));

        nameValuePairs.add(new BasicNameValuePair("Sex", _birthDetail.getGender().trim()));

        nameValuePairs.add(new BasicNameValuePair("Day", String.valueOf(
                _birthDetail.getDateTime().getDay()).trim()));
        nameValuePairs.add(new BasicNameValuePair("Month", String.valueOf(
                _birthDetail.getDateTime().getMonth() + 1).trim()));
        nameValuePairs.add(new BasicNameValuePair("Year", String.valueOf(
                _birthDetail.getDateTime().getYear()).trim()));
        nameValuePairs.add(new BasicNameValuePair("Hrs", String.valueOf(
                _birthDetail.getDateTime().getHour()).trim()));
        nameValuePairs.add(new BasicNameValuePair("Min", String.valueOf(
                _birthDetail.getDateTime().getMin()).trim()));
        nameValuePairs.add(new BasicNameValuePair("Sec", String.valueOf(
                _birthDetail.getDateTime().getSecond()).trim()));
		*//*nameValuePairs.add(new BasicNameValuePair("Place", _birthDetail
				.getCityName().trim()));*//*
        nameValuePairs.add(new BasicNameValuePair("Place", _city));
        nameValuePairs.add(new BasicNameValuePair("LongDeg", _birthDetail.getPlace().getLongDeg().trim()));
        nameValuePairs.add(new BasicNameValuePair("LongMin", _birthDetail.getPlace().getLongMin().trim()));
        nameValuePairs.add(new BasicNameValuePair("LongEW", _birthDetail.getPlace().getLongDir().trim()));
        nameValuePairs.add(new BasicNameValuePair("LatDeg", _birthDetail.getPlace().getLatDeg().trim()));
        nameValuePairs.add(new BasicNameValuePair("LatMin", _birthDetail.getPlace().getLatMin().trim()));
        nameValuePairs.add(new BasicNameValuePair("LatNS", _birthDetail.getPlace().getLatDir().trim()));

        nameValuePairs.add(new BasicNameValuePair("Ayanamsa", String.valueOf(
                _birthDetail.getAyanIndex()).trim()));
        nameValuePairs.add(new BasicNameValuePair("timezone", String.valueOf(_birthDetail.getPlace().getTimeZoneValue()).trim()));
        nameValuePairs.add(new BasicNameValuePair("DST", String.valueOf(
                _birthDetail.getDST()).trim()));
        nameValuePairs.add(new BasicNameValuePair("Isapi", "1"));
        if (_birthDetail.getOnlineChartId().trim().equals("-1")) {
            nameValuePairs.add(new BasicNameValuePair("ChartId", ""));
        } else {
            nameValuePairs.add(new BasicNameValuePair("ChartId", _birthDetail.getOnlineChartId().trim()));
        }
        nameValuePairs.add(new BasicNameValuePair("kphn", String.valueOf(_birthDetail.getHoraryNumber())));
        if ((_birthDetail.getOnlineChartId() != null) && (_birthDetail.getOnlineChartId().trim().length() > 0)) {
            updatedOnlineChartId = _birthDetail.getOnlineChartId().trim();
        }
        return nameValuePairs;
    }*/

    /**
     * This function is used to save chart on astrisage server
     *
     * @param _birthDetail
     * @return
     * @throws Exception
     */
    /*private long[] executeHTTRequestSaveChartOnServer(BeanHoroPersonalInfo _birthDetail, String userId, String pwd)
            throws Exception {
        long[] char_msg = new long[2];
        BufferedReader in = null;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        HttpPost hp = new HttpPost(CGlobalVariables.ONLINE_CHART_SAVE_URL);
//		HttpPost hp = new HttpPost("http://192.168.1.18/astrosage-xml/savechart-xml.asp?");
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_SaveChartOnServer(_birthDetail, userId, pwd), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            char_msg = getSaveOnlineChartId(sb.toString());


        } catch (Exception e) {
            throw e;
        }

        return char_msg;

    }*/

    /**
     * This function is used to extract chart id after saving on
     * chart on astrosage server
     *
     * @param chartSavedId
     * @return chart id
     */
    private long[] getSaveOnlineChartId(String chartSavedId) throws Exception {
        long _chartId = -1;
        long _msgcode = -1;
        long[] char_msg = new long[2];
        try {
            Document doc = CXMLOperations.XMLfromString(chartSavedId);
            int numResults = doc.getChildNodes().getLength();

            if ((numResults <= 0)) {
                _chartId = -1;
                char_msg[0] = _chartId;
                char_msg[1] = _msgcode;


            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                    try {
                        XPath xpathChartId = XPathFactory.newInstance().newXPath();
                        String chartIdExp = "/product/chartid"; // chart id
                        NodeList chartIdNodes = (NodeList) xpathChartId.evaluate(chartIdExp, doc, XPathConstants.NODESET);
                        _chartId = Long.valueOf(chartIdNodes.item(0).getTextContent());
                        char_msg[0] = _chartId;
                        char_msg[1] = _msgcode;
                    } catch (Exception e) {
                        //THIS CODE IS ADDED BY BIJENDRA ON 28-NOV-2012
                        //DESCRIPTION:This code is run when the chart saving account is full on server
                        //this code is written in catch because xml  values changed so it throw exception
                        XPath xpathMsgId = XPathFactory.newInstance().newXPath();
                        String msgCodeExp = "/product/msgcode"; // message code
                        NodeList msgcodeNodes = (NodeList) xpathMsgId.evaluate(msgCodeExp, doc, XPathConstants.NODESET);
                        _msgcode = Long.valueOf(msgcodeNodes.item(0).getTextContent());
                        if ((updatedOnlineChartId != null) && (_msgcode == 3) || (_msgcode == 4) || (_msgcode == 5)) {
                            char_msg[0] = Long.valueOf(updatedOnlineChartId);
                        } else if ((_msgcode == 2) || (_msgcode == 0)) {
                            char_msg[0] = _chartId;
                        }
                        char_msg[1] = _msgcode;

                        // END
                    }

                } else {
                    NodeList nodes = doc.getElementsByTagName("product");

                    try {
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element e = (Element) nodes.item(i);
                            _chartId = Long.valueOf(CXMLOperations.getValue(e, "chartid"));

                        }
                        char_msg[0] = _chartId;
                        char_msg[1] = _msgcode;
                    } catch (Exception e) {
                        //THIS CODE IS ADDED BY BIJENDRA ON 28-NOV-2012
                        //DESCRIPTION:This code is run when the chart saving account is full on server
                        //this code is written in catch because xml  values changed so it throw exception
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element eMsg = (Element) nodes.item(i);
                            _msgcode = Long.valueOf(CXMLOperations.getValue(eMsg, "msgcode"));
                        }
                        if ((updatedOnlineChartId != null) && (_msgcode == 3) || (_msgcode == 4) || (_msgcode == 5)) {
                            char_msg[0] = Long.valueOf(updatedOnlineChartId);
                        } else if ((_msgcode == 2) || (_msgcode == 0)) {
                            char_msg[0] = _chartId;
                        }
                        char_msg[1] = _msgcode;
                        // END
                    }

                }
            }

        } catch (Exception e) {
            throw e;
        }

        return char_msg;

    }


    /**
     * This function share kundli name online
     *
     * @return String[]
     * @throws Exception
     */

    /*public String[] shareKundliNameOnline(String userId, String pwd, String name, String chartId)
            throws Exception {
        String[] _resultString = new String[2];

        try {
            _resultString = executeHTTRequestToshareKundliNameOnline(userId, pwd, name, chartId);

        } catch (Exception e) {
            throw e;
        }
        return _resultString;
    }*/

    /*private String[] executeHTTRequestToshareKundliNameOnline(String userId,
                                                              String pwd2, String name, String chartId) throws Exception {
        String[] results = null;
        BufferedReader in = null;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        //CGlobalVariables.SHARE_CHART
        HttpPost hp = new HttpPost();
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Share_chart_onlile(userId, pwd2, name, chartId), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();


            ////Log.e("SHARE_RETURN", sb.toString());
            results = getShareStatusResult(sb.toString());

        } catch (Exception e) {

            throw e;
        }

        return results;
    }*/
    private String[] getShareStatusResult(String strData) {
        String[] _resultString = new String[2];
        Document doc = CXMLOperations.XMLfromString(strData);
        int numResults = doc.getChildNodes().getLength();

        NodeList nodes = doc.getElementsByTagName("product");
        if (numResults > 0) {
            try {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                    //FOR CHART ID
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    String chartIdExp = "/product/msgcode";
                    NodeList chartIdNodes = (NodeList) xpath.evaluate(chartIdExp, doc, XPathConstants.NODESET);
                    for (int i = 0; i < chartIdNodes.getLength(); i++)
                        _resultString[0] = chartIdNodes.item(i).getTextContent();

                    if (chartIdNodes.item(0).getTextContent().equalsIgnoreCase("1")) {
                        //FOR Share URL
                        XPath xpathURL = XPathFactory.newInstance().newXPath();
                        String chartURLExp = "/product/sharedurl";
                        NodeList chartURLNodes = (NodeList) xpathURL.evaluate(chartURLExp, doc, XPathConstants.NODESET);
                        for (int i = 0; i < chartURLNodes.getLength(); i++)
                            _resultString[1] = chartURLNodes.item(i).getTextContent();
                    } else {
                        //FOR Other Message Code
                        XPath xpathMsg = XPathFactory.newInstance().newXPath();
                        String chartMsgExp = "/product/msg";
                        NodeList chartMsgNodes = (NodeList) xpathMsg.evaluate(chartMsgExp, doc, XPathConstants.NODESET);
                        for (int i = 0; i < chartMsgNodes.getLength(); i++)
                            _resultString[1] = chartMsgNodes.item(i).getTextContent();
                    }
                } else {
                    String msgCode = null;
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element e = (Element) nodes.item(i);
                        msgCode = CXMLOperations.getValue(e, "msgcode");
                        _resultString[0] = msgCode;
                    }
                    if (msgCode.equalsIgnoreCase("1")) {
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element e = (Element) nodes.item(i);
                            _resultString[1] = CXMLOperations.getValue(e, "sharedurl");
                        }
                    } else {
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element e = (Element) nodes.item(i);
                            _resultString[1] = CXMLOperations.getValue(e, "msg");
                        }
                    }

                }

            } catch (Exception e) {

            }
        }
        return _resultString;
    }

    private List<NameValuePair> getNameValuePairs_Share_chart_onlile(
            String userId, String pwd2, String name, String chartId2) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userId)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd2));
        nameValuePairs.add(new BasicNameValuePair("publicchartname", name));
        nameValuePairs.add(new BasicNameValuePair("chartid", chartId2));
		/*//Log.e("userid", userId);
		//Log.e("password", pwd2);
		//Log.e("publicchartname", name);
		//Log.e("chartid", chartId2);*/
        return nameValuePairs;
    }

    /**
     * This function is used to check the available name to share kundli
     *
     * @param kundliName
     * @return String[]
     * @throws Exception
     */

   /* public String[] checkAvailableKundliNameToShare(String kundliName)
            throws Exception {
        // TODO Auto-generated method stub
        String[] _result = null;

        try {
            _result = executeHTTRequestToCheckAvailableKundliNameToShare(kundliName);

        } catch (Exception e) {
            throw e;
        }
        return _result;

    }*/

    /*private String[] executeHTTRequestToCheckAvailableKundliNameToShare(String kundliName)
            throws Exception {
        String[] arrayAvailable = null;

        BufferedReader in = null;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        //CGlobalVariables.AVAILABLE_NAME_TO_SHARE
        HttpPost hp = new HttpPost();
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Check_Available_Name_To_Share(
                    kundliName), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            arrayAvailable = getAvaliableNameToShare(sb.toString());

        } catch (Exception e) {

            throw e;
        }
        return arrayAvailable;

    }*/

   /* private List<NameValuePair> getNameValuePairs_Check_Available_Name_To_Share(String kundliName) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("publicchartname", kundliName));

        return nameValuePairs;
    }*/

    /*private String[] getAvaliableNameToShare(String strData) {
        String[] _resultString = new String[2];
        Document doc = CXMLOperations.XMLfromString(strData);
        int numResults = doc.getChildNodes().getLength();
        // Toast.makeText(_context,
        // String.valueOf(_status),Toast.LENGTH_LONG).show();
        NodeList nodes = doc.getElementsByTagName("product");
        if (numResults > 0) {
            try {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                    //FOR CHART ID
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    String chartIdExp = "/product/msgcode"; // chart id
                    NodeList chartIdNodes = (NodeList) xpath.evaluate(chartIdExp, doc, XPathConstants.NODESET);
                    for (int i = 0; i < chartIdNodes.getLength(); i++)
                        _resultString[0] = chartIdNodes.item(i).getTextContent();


                    //FOR CHART NAME
                    XPath xpathName = XPathFactory.newInstance().newXPath();
                    String chartNameExp = "/product/msg"; // chart id
                    NodeList chartNameNodes = (NodeList) xpathName.evaluate(chartNameExp, doc, XPathConstants.NODESET);
                    for (int i = 0; i < chartNameNodes.getLength(); i++)
                        _resultString[1] = chartNameNodes.item(i).getTextContent();
                } else {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element e = (Element) nodes.item(i);
                        _resultString[0] = CXMLOperations.getValue(e, "msgcode");
                    }
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element e = (Element) nodes.item(i);
                        _resultString[1] = CXMLOperations.getValue(e, "msg");
                    }
                }

            } catch (Exception e) {

            }
        }
        // Log.d("Online_Delete_Result", String.valueOf(_result));
        return _resultString;
    }*/


}
