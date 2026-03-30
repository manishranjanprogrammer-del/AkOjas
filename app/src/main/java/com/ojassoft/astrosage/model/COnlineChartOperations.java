package com.ojassoft.astrosage.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.jinterface.IOnlineChartOperations;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.MychartActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CXMLOperations;


import org.apache.http.NameValuePair;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class COnlineChartOperations implements IOnlineChartOperations {
    /**
     * These are the  variables  used to contain user id,password and chart id.
     */
    //private String uid="", pwd="", chartid;
    /**
     * This variables is used for login verification value.
     */
    private String verifyingLoginValue = "-1";
    private String userPurchasedPlanId = "1";
    private String userPlanExpiryDate = "0";
    private String userPlanPurchasedDate = "0";
    private String userFirstName = "";
    // Added by Amit Sharma
    private String chartid;
    private String pname;
    private String gender;
    private String dayofbirth;
    private String monthofbirth;
    private String yearofbirth;
    private String hourofbirth;
    private String minuteofbirth;
    private String secondofbirth;
    private String dst;
    private String placeofbirth;
    private String longdeg;
    private String longmin;
    private String longew;
    private String latdeg;
    private String latmin;
    private String latns;
    private String timezone;
    private String ayanamsa;
    private String charttype;
    private String kphn;

    /**
     * This is a string array to contain online chart list .
     */
    private Map<String, String> chartListArray;
    ArrayList<BeanHoroPersonalInfo> onLineSavedChart;

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
    /*public String[] verifyLoginWithUserPurchasedPlan(String _uid, String _pwd, String key) throws Exception {
        String[] iStatus = new String[]{"-1", "1", "0", "0", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", ""};

        try {
            //If user not login at the time of purchase plan, then sign in user with device id only once.
            if (CUtils.getBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false)) {
                // commented by abhishek raj -- it will be set to false while plan activated successfylly.
                //CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(),CGlobalVariables.needToSendDeviceIdForLogin,false);
                executeHTTRequestVerifyingLoginFirstTimeAfterPurchasePlan(_uid, _pwd, key);
            } else {
                executeHTTRequestVerifyingLogin(_uid, _pwd, key);
            }
            iStatus[0] = getVerifyingLoginValue();
            iStatus[1] = this.userPurchasedPlanId;
            iStatus[2] = this.userPlanPurchasedDate;
            iStatus[3] = this.userPlanExpiryDate;
            ////
            iStatus[4] = this.chartid;
            iStatus[5] = this.pname;
            iStatus[6] = this.gender;
            iStatus[7] = this.dayofbirth;
            iStatus[8] = this.monthofbirth;
            iStatus[9] = this.yearofbirth;
            iStatus[10] = this.hourofbirth;
            iStatus[11] = this.minuteofbirth;
            iStatus[12] = this.secondofbirth;
            iStatus[13] = this.dst;
            iStatus[14] = this.placeofbirth;
            iStatus[15] = this.longdeg;
            iStatus[16] = this.longmin;
            iStatus[17] = this.longew;
            iStatus[18] = this.latdeg;
            iStatus[19] = this.latmin;
            iStatus[20] = this.latns;
            iStatus[21] = this.timezone;
            iStatus[22] = this.ayanamsa;
            iStatus[23] = this.charttype;
            iStatus[24] = this.kphn;
            iStatus[25] = this.userFirstName;
            ////Log.e("UserPlan", this.userPurchasedPlanId);
        } catch (Exception e) {
            throw e;
        }
        return iStatus;

    }*/

    /**
     * this function is used to register user using emailid only
     *
     * @param _uid
     * @param key
     * @return
     * @throws Exception
     */
   /* public String[] userSignUp(String _uid, String key) throws Exception {
        String[] iStatus = new String[]{"0", "0"};
        try {
            iStatus = executeHTTRequestForSignUpUsingEmailOnly(_uid, key);
        } catch (Exception ex) {
            throw ex;
        }
        return iStatus;
    }*/

    /**
     * This function is used to register the user on servers
     *
     * @param _uid,_pwd 18-Jan-2016
     */
   /* public String[] userSignUp(String _uid, String _pwd, String key) throws Exception {
        String[] iStatus = new String[]{"0", "0"};
        try {
            if (CUtils.getBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false)) {
                // commented by abhishek raj -- it will be set to false while plan activated successfylly.
                //CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
                iStatus = executeHTTRequestForSignUpFirstTimeAfterPurchasePlan(_uid, _pwd, key);
            } else {
                iStatus = executeHTTRequestForSignUp(_uid, _pwd, key);
            }
        } catch (Exception ex) {
            throw ex;
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
   /* private void executeHTTRequestVerifyingLogin(String userid, String pwd, String key)
            throws Exception {
        String strReturn = "";
        BufferedReader in = null;
        HttpClient hc = CUtils.getNewHttpClient();
        //HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        HttpPost hp = new HttpPost(CGlobalVariables.VERIFYING_LOGIN); //signupabhishek
        //HttpPost hp = new HttpPost("http://192.168.1.93/astrosage-xml/userlogincheckv3.asp");
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Login(
                    userid, pwd, key), HTTP.UTF_8));
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
     * This function is used to execute online request to verify user login after purchase plan.
     *
     * @param userid
     * @param pwd
     * @throws Exception
     */
    /*private void executeHTTRequestVerifyingLoginFirstTimeAfterPurchasePlan(String userid, String pwd, String key)
            throws Exception {
        String strReturn = "";
        BufferedReader in = null;
        HttpClient hc = CUtils.getNewHttpClient();
        //HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        HttpPost hp = new HttpPost(CGlobalVariables.VERIFYING_LOGIN_FIRST_TIME_AFTER_PURCHASE_PLAN);//signupabhishek
        //HttpPost hp = new HttpPost("http://192.168.1.93/astrosage-xml/userlogincheckv3.asp");
        try {
            String device_id = CUtils.getMyAndroidId(AstrosageKundliApplication.getAppContext());
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_LoginFirstTimeAfterPurchasePlan(
                    userid, pwd, key, device_id), HTTP.UTF_8));
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

   /* private String[] executeHTTRequestForSignUpUsingEmailOnly(String userid, String key)
            throws Exception {
        String[] iStatus = new String[]{"0", "0"};
        String strReturn = "";
        BufferedReader in = null;
        HttpClient hc = CUtils.getNewHttpClient();
        HttpPost hp = new HttpPost(CGlobalVariables.REGISTRATION_USER_WITHOUT_PASSWORD);//signupabhishek
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Signup(
                    userid, key), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            iStatus = parseSignupResult(sb.toString());

        } catch (Exception e) {

            throw e;
        }

        return iStatus;

    }*/

    /*private String[] parseSignupResult(String jsonString) {
        String[] resultArray = new String[2];
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String resultString = jsonObject.get("Result").toString();
            resultArray[0] = resultString;
            if (resultString.equals("-1") || resultString.equals("0") || resultString.equals("4") || resultString.equals("5")) {
                resultArray[1] = "";
            } else if (resultString.equals("1")) {
                resultArray[1] = jsonObject.get("userpassword").toString();
            }
        } catch (Exception e) {
            resultArray[0] = "1001"; //Incorrect Json Format Error
        }
        return resultArray;
    }*/

    /*private String[] executeHTTRequestForSignUp(String userid, String pwd, String key)
            throws Exception {
        String[] iStatus = new String[]{"0", "0"};
        String strReturn = "";
        BufferedReader in = null;
        HttpClient hc = CUtils.getNewHttpClient();
        HttpPost hp = new HttpPost(CGlobalVariables.REGISTRATION_USER);//signupabhishek
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Signup(
                    userid, pwd, key), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            iStatus = setSignUpDetails(sb.toString());

        } catch (Exception e) {

            throw e;
        }

        return iStatus;

    }*/

   /* private String[] executeHTTRequestForSignUpFirstTimeAfterPurchasePlan(String userid, String pwd, String key)
            throws Exception {
        String[] iStatus = new String[]{"0", "0"};
        String strReturn = "";
        BufferedReader in = null;
        HttpClient hc = CUtils.getNewHttpClient();
        HttpPost hp = new HttpPost(CGlobalVariables.REGISTRATION_USER_FIRST_TIME_AFTER_PURCHASE_PLAN);//signupabhishek
        try {
            String device_id = CUtils.getMyAndroidId(AstrosageKundliApplication.getAppContext());
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_SignupFirstTimeAfterPurchasePlan(
                    userid, pwd, key, device_id), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            iStatus = setSignUpDetails(sb.toString());

            //if plan activated successfully(response code 8)
            if (iStatus[0].equals("8")) {
                CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
            }

        } catch (Exception e) {

            throw e;
        }

        return iStatus;

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
                                                        String pwd, String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userid)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("key", key));

        Context context = AstrosageKundliApplication.getAppContext();
        String latitude = CUtils.getStringData(context, CGlobalVariables.KEY_LATITUDE, "");
        String longitude = CUtils.getStringData(context, CGlobalVariables.KEY_LONGITUDE, "");
        String city = CUtils.getStringData(context, CGlobalVariables.KEY_CITY, "");
        nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
        nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
        nameValuePairs.add(new BasicNameValuePair("city", city));

        return nameValuePairs;

    }


    /**
     * This function return list of name value pairs to  verify login for first time after purchase plan
     *
     * @param userid
     * @param pwd
     * @return List<NameValuePair>
     */

    private List<NameValuePair> getNameValuePairs_LoginFirstTimeAfterPurchasePlan(String userid,
                                                                                  String pwd, String key, String device_id) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userid)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("key", key));
        nameValuePairs.add(new BasicNameValuePair("device_id", device_id));

        Context context = AstrosageKundliApplication.getAppContext();
        String latitude = CUtils.getStringData(context, CGlobalVariables.KEY_LATITUDE, "");
        String longitude = CUtils.getStringData(context, CGlobalVariables.KEY_LONGITUDE, "");
        String city = CUtils.getStringData(context, CGlobalVariables.KEY_CITY, "");
        nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
        nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
        nameValuePairs.add(new BasicNameValuePair("city", city));

        return nameValuePairs;

    }

    /**
     * This function return list of name value pairs for  sign up
     *
     * @param userid
     * @param pwd
     * @return List<NameValuePair>
     */

    private List<NameValuePair> getNameValuePairs_Signup(String userid,
                                                         String pwd, String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(userid)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("key", key));

        Context context = AstrosageKundliApplication.getAppContext();
        String latitude = CUtils.getStringData(context, CGlobalVariables.KEY_LATITUDE, "");
        String longitude = CUtils.getStringData(context, CGlobalVariables.KEY_LONGITUDE, "");
        String city = CUtils.getStringData(context, CGlobalVariables.KEY_CITY, "");
        nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
        nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
        nameValuePairs.add(new BasicNameValuePair("city", city));

        return nameValuePairs;

    }

    /**
     * This function return list of name value pairs for  sign up
     *
     * @param userid
     * @param pwd
     * @return List<NameValuePair>
     */

   /* private List<NameValuePair> getNameValuePairs_SignupFirstTimeAfterPurchasePlan(String userid,
                                                                                   String pwd, String key, String device_id) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(userid)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("key", key));
        nameValuePairs.add(new BasicNameValuePair("device_id", device_id));

        Context context = AstrosageKundliApplication.getAppContext();
        String latitude = CUtils.getStringData(context, CGlobalVariables.KEY_LATITUDE, "");
        String longitude = CUtils.getStringData(context, CGlobalVariables.KEY_LONGITUDE, "");
        String city = CUtils.getStringData(context, CGlobalVariables.KEY_CITY, "");
        nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
        nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
        nameValuePairs.add(new BasicNameValuePair("city", city));

        return nameValuePairs;

    }*/

    /*private List<NameValuePair> getNameValuePairs_Signup(String userid,
                                                         String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(userid)));
        nameValuePairs.add(new BasicNameValuePair("key", key));

        Context context = AstrosageKundliApplication.getAppContext();
        String latitude = CUtils.getStringData(context, CGlobalVariables.KEY_LATITUDE, "");
        String longitude = CUtils.getStringData(context, CGlobalVariables.KEY_LONGITUDE, "");
        String city = CUtils.getStringData(context, CGlobalVariables.KEY_CITY, "");
        nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
        nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
        nameValuePairs.add(new BasicNameValuePair("city", city));

        return nameValuePairs;

    }*/

    /**
     * This function is used to extract login status value
     * returned from server after executing the login verification request.
     */
    public void setVerifyingLoginValue(String loginValue) {
        Document doc = CXMLOperations.XMLfromString(loginValue);
        int numResults = doc.getChildNodes().getLength();
        if ((numResults <= 0))
            this.verifyingLoginValue = "-1";
        else {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                try {

                    // LOGIN
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    String expression = "/product/msgcode"; // first book
                    Node logValue = (Node) xpath.evaluate(expression, doc,
                            XPathConstants.NODE);
                    if (logValue != null)
                        this.verifyingLoginValue = logValue.getTextContent();
                    // USER PURCHASED PLAN ID
                    String expressionPlanId = "/product/userplanid"; // first
                    // book
                    Node logValuePlanId = (Node) xpath.evaluate(
                            expressionPlanId, doc, XPathConstants.NODE);
                    if (logValuePlanId != null)
                        this.userPurchasedPlanId = logValuePlanId
                                .getTextContent();

                    // 24 Dec 2015 User Plan Purchase Date
                    String expressionPlanPurchaseDate = "/product/userplanpurchasedate"; // first
                    // book
                    Node logValuePlanPurchaseDate = (Node) xpath.evaluate(
                            expressionPlanPurchaseDate, doc,
                            XPathConstants.NODE);
                    if (logValuePlanPurchaseDate != null)
                        this.userPlanPurchasedDate = logValuePlanPurchaseDate
                                .getTextContent();

                    // 24 Dec 2015 User Plan Expiry Date
                    String expressionPlanExpiryDate = "/product/userplanexpirydate"; // first
                    // book
                    Node logValuePlanExpiryDate = (Node) xpath.evaluate(
                            expressionPlanExpiryDate, doc, XPathConstants.NODE);
                    if (logValuePlanExpiryDate != null)
                        this.userPlanExpiryDate = logValuePlanExpiryDate
                                .getTextContent();
                    String userFirstName = "/product/userfirstname"; // first
                    // book
                    Node logValueUserFirstName = (Node) xpath.evaluate(
                            userFirstName, doc, XPathConstants.NODE);
                    if (logValueUserFirstName != null)
                        this.userFirstName = logValueUserFirstName
                                .getTextContent();

                    saveDefaultKundliData(doc, xpath);

                } catch (Exception e) {

                }
                    /*try {

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
							this.userPurchasedPlanId=logValuePlanId.getTextContent();
						
						//24 Dec 2015 User Plan Purchase Date
						String expressionPlanPurchaseDate = "/product/userplanpurchasedate"; // first book
						Node logValuePlanPurchaseDate = (Node) xpath.evaluate(expressionPlanPurchaseDate, doc, XPathConstants.NODE);
						if (logValuePlanPurchaseDate != null)
							this.userPlanPurchasedDate=logValuePlanPurchaseDate.getTextContent();
						
						//24 Dec 2015 User Plan Expiry Date
						String expressionPlanExpiryDate = "/product/userplanexpirydate"; // first book
						Node logValuePlanExpiryDate = (Node) xpath.evaluate(expressionPlanExpiryDate, doc, XPathConstants.NODE);
						if (logValuePlanExpiryDate != null)
							this.userPlanExpiryDate=logValuePlanExpiryDate.getTextContent();
								
	
					} catch (Exception e) {
	
					}*/
            } else {
                    /*NodeList nodes = doc.getElementsByTagName("msgcode");
                    for (int i = 0; i < nodes.getLength(); i++) {
						Element e = (Element) nodes.item(i);
						this.verifyingLoginValue = CXMLOperations.getValue(e,"msgcode");
					}
					// USER PURCHASED PLAN ID	
					NodeList nodesPlanId = doc.getElementsByTagName("userplanid");
					for (int i = 0; i < nodesPlanId.getLength(); i++) {
						Element e = (Element) nodesPlanId.item(i);
						this.userPurchasedPlanId = CXMLOperations.getValue(e,"userplanid");
					}
					
					//24 Dec 2015 User Plan Purchase Date			
					NodeList nodesPlanPurchaseDate = doc.getElementsByTagName("userplanpurchasedate");
					for (int i = 0; i < nodesPlanPurchaseDate.getLength(); i++) {
						Element e = (Element) nodesPlanPurchaseDate.item(i);
						this.userPlanPurchasedDate = CXMLOperations.getValue(e,"userplanpurchasedate");
					}
					
					//24 Dec 2015 User Plan Expiry Date			
					NodeList nodesPlanExpiryDate = doc.getElementsByTagName("userplanexpirydate");
					for (int i = 0; i < nodesPlanExpiryDate.getLength(); i++) {
						Element e = (Element) nodesPlanExpiryDate.item(i);
						this.userPlanExpiryDate = CXMLOperations.getValue(e,"userplanexpirydate");
					}*/
                NodeList nodes = doc.getElementsByTagName("msgcode");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element e = (Element) nodes.item(i);
                    this.verifyingLoginValue = CXMLOperations.getValue(e,
                            "msgcode");
                }
                // USER PURCHASED PLAN ID
                NodeList nodesPlanId = doc.getElementsByTagName("userplanid");
                for (int i = 0; i < nodesPlanId.getLength(); i++) {
                    Element e = (Element) nodesPlanId.item(i);
                    this.userPurchasedPlanId = CXMLOperations.getValue(e,
                            "userplanid");
                }

                // 24 Dec 2015 User Plan Purchase Date
                NodeList nodesPlanPurchaseDate = doc
                        .getElementsByTagName("userplanpurchasedate");
                for (int i = 0; i < nodesPlanPurchaseDate.getLength(); i++) {
                    Element e = (Element) nodesPlanPurchaseDate.item(i);
                    this.userPlanPurchasedDate = CXMLOperations.getValue(e,
                            "userplanpurchasedate");
                }

                // 24 Dec 2015 User Plan Expiry Date
                NodeList nodesPlanExpiryDate = doc
                        .getElementsByTagName("userplanexpirydate");
                for (int i = 0; i < nodesPlanExpiryDate.getLength(); i++) {
                    Element e = (Element) nodesPlanExpiryDate.item(i);
                    this.userPlanExpiryDate = CXMLOperations.getValue(e,
                            "userplanexpirydate");
                }
                NodeList nodesUserFirstName = doc
                        .getElementsByTagName("userfirstname");
                for (int i = 0; i < nodesUserFirstName.getLength(); i++) {
                    Element e = (Element) nodesUserFirstName.item(i);
                    this.userFirstName = CXMLOperations.getValue(e,
                            "userfirstname");
                }
                saveDefaultKundliData(doc);
            }

        }

        //if plan activated successfully
        if (this.verifyingLoginValue != null && this.verifyingLoginValue.equals("2")) {
            CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
        }


    }

    /*
     * save defult kundali data
     */
    private void saveDefaultKundliData(Document doc) {
        String[] nodeval;
        // BeanHoroPersonalInfo beanHoroPersonalInfo = new
        // BeanHoroPersonalInfo();
        // BeanDateTime beanDateTime = new BeanDateTime();
        // BeanPlace beanPlace = new BeanPlace();
        NodeList chartid = doc.getElementsByTagName("chartid");
        nodeval = getValFromNodeList(chartid, "chartid");
        this.chartid = nodeval[0];
        // beanHoroPersonalInfo.setOnlineChartId(nodeval[0]);
        NodeList pname = doc.getElementsByTagName("pname");
        nodeval = getValFromNodeList(pname, "pname");
        this.pname = nodeval[0];
        // beanHoroPersonalInfo.setName(nodeval[0]);
        NodeList gender = doc.getElementsByTagName("gender");
        nodeval = getValFromNodeList(gender, "gender");
        this.gender = nodeval[0];
        // beanHoroPersonalInfo.setGender(nodeval[0]);
        NodeList dayofbirth = doc.getElementsByTagName("dayofbirth");
        nodeval = getValFromNodeList(dayofbirth, "dayofbirth");
        this.dayofbirth = nodeval[0];
        // beanDateTime.setDay(Integer.parseInt(nodeval[0]));
        NodeList monthofbirth = doc.getElementsByTagName("monthofbirth");
        nodeval = getValFromNodeList(monthofbirth, "monthofbirth");
        this.monthofbirth = nodeval[0];
        // beanDateTime.setMonth(Integer.parseInt(nodeval[0]));
        NodeList yearofbirth = doc.getElementsByTagName("yearofbirth");
        nodeval = getValFromNodeList(yearofbirth, "yearofbirth");
        this.yearofbirth = nodeval[0];
        // beanDateTime.setYear(Integer.parseInt(nodeval[0]));
        NodeList hourofbirth = doc.getElementsByTagName("hourofbirth");
        nodeval = getValFromNodeList(hourofbirth, "hourofbirth");
        this.hourofbirth = nodeval[0];
        // beanDateTime.setHour(Integer.parseInt(nodeval[0]));
        NodeList minuteofbirth = doc.getElementsByTagName("minuteofbirth");
        nodeval = getValFromNodeList(minuteofbirth, "minuteofbirth");
        this.minuteofbirth = nodeval[0];
        // beanDateTime.setMin(Integer.parseInt(nodeval[0]));
        NodeList secondofbirth = doc.getElementsByTagName("secondofbirth");
        nodeval = getValFromNodeList(secondofbirth, "secondofbirth");
        this.secondofbirth = nodeval[0];
        // beanDateTime.setSecond(Integer.parseInt(nodeval[0]));
        NodeList dst = doc.getElementsByTagName("dst");
        nodeval = getValFromNodeList(dst, "dst");
        this.dst = nodeval[0];
        // beanHoroPersonalInfo.setDST(Integer.parseInt(nodeval[0]));
        NodeList placeofbirth = doc.getElementsByTagName("placeofbirth");
        nodeval = getValFromNodeList(placeofbirth, "placeofbirth");
        this.placeofbirth = nodeval[0];
        // beanPlace.setCityName(nodeval[0]);
        NodeList longdeg = doc.getElementsByTagName("longdeg");
        nodeval = getValFromNodeList(longdeg, "longdeg");
        this.longdeg = nodeval[0];
        // beanPlace.setLongDeg(nodeval[0]);
        NodeList longmin = doc.getElementsByTagName("longmin");
        nodeval = getValFromNodeList(longmin, "longmin");
        this.longmin = nodeval[0];
        // beanPlace.setLongMin(nodeval[0]);
        NodeList longew = doc.getElementsByTagName("longew");
        nodeval = getValFromNodeList(longew, "longew ");
        this.longew = nodeval[0];
        // beanPlace.setLongDir(nodeval[0]);
        NodeList latdeg = doc.getElementsByTagName("latdeg");
        nodeval = getValFromNodeList(latdeg, "latdeg");
        this.latdeg = nodeval[0];
        // beanPlace.setLatDeg(nodeval[0]);
        NodeList latmin = doc.getElementsByTagName("latmin");
        nodeval = getValFromNodeList(latmin, "latmin ");
        this.latmin = nodeval[0];
        // beanPlace.setLatMin(nodeval[0]);
        NodeList latns = doc.getElementsByTagName("latns");
        nodeval = getValFromNodeList(latns, "latns");
        this.latns = nodeval[0];
        // beanPlace.setLatDir(nodeval[0]);
        NodeList timezone = doc.getElementsByTagName("timezone");
        nodeval = getValFromNodeList(timezone, "timezone");
        this.timezone = nodeval[0];
        // beanPlace.setTimeZone(nodeval[0]);
        NodeList ayanamsa = doc.getElementsByTagName("ayanamsa");
        nodeval = getValFromNodeList(ayanamsa, "ayanamsa");
        this.ayanamsa = nodeval[0];
        // beanHoroPersonalInfo.setAyan(nodeval[0]);
        NodeList charttype = doc.getElementsByTagName("charttype");
        nodeval = getValFromNodeList(charttype, "charttype");
        this.charttype = nodeval[0];
        // beanHoroPersonalInfo.setC;
        NodeList kphn = doc.getElementsByTagName("kphn");
        nodeval = getValFromNodeList(kphn, "kphn");
        this.kphn = nodeval[0];
        /*
         * beanHoroPersonalInfo.setHoraryNumber(Integer.parseInt(nodeval[0]));
         * beanHoroPersonalInfo.setDateTime(beanDateTime);
         * beanHoroPersonalInfo.setPlace(beanPlace);
         */
    }

    /*
     * get value from nodelist
     */
    private String[] getValFromNodeList(NodeList nodeList, String nodeName) {
        String[] nodeval = new String[10];
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element e = (Element) nodeList.item(i);
            nodeval[i] = CXMLOperations.getValue(e, nodeName);
        }
        return nodeval;
    }

    /*
     * save defult kundali data
     */
    private void saveDefaultKundliData(Document doc, XPath xPath) {
        try {
            /*
             * String nodeval; BeanHoroPersonalInfo beanHoroPersonalInfo = new
             * BeanHoroPersonalInfo(); BeanDateTime beanDateTime = new
             * BeanDateTime(); BeanPlace beanPlace = new BeanPlace();
             */
            Node chartid = (Node) xPath.evaluate("/product/chartid", doc,
                    XPathConstants.NODE);
            this.chartid = getValFromNode(chartid);
            // beanHoroPersonalInfo.setOnlineChartId(nodeval);
            Node pname = (Node) xPath.evaluate("/product/pname", doc,
                    XPathConstants.NODE);
            this.pname = getValFromNode(pname);
            // beanHoroPersonalInfo.setName(nodeval);
            Node gender = (Node) xPath.evaluate("/product/gender", doc,
                    XPathConstants.NODE);
            this.gender = getValFromNode(gender);
            // beanHoroPersonalInfo.setGender(nodeval);
            Node dayofbirth = (Node) xPath.evaluate("/product/dayofbirth", doc,
                    XPathConstants.NODE);
            this.dayofbirth = getValFromNode(dayofbirth);
            // beanDateTime.setDay(Integer.parseInt(nodeval));
            Node monthofbirth = (Node) xPath.evaluate("/product/monthofbirth",
                    doc, XPathConstants.NODE);
            this.monthofbirth = getValFromNode(monthofbirth);
            // beanDateTime.setMonth(Integer.parseInt(nodeval));
            Node yearofbirth = (Node) xPath.evaluate("/product/yearofbirth",
                    doc, XPathConstants.NODE);
            this.yearofbirth = getValFromNode(yearofbirth);
            // beanDateTime.setYear(Integer.parseInt(nodeval));
            Node hourofbirth = (Node) xPath.evaluate("/product/hourofbirth",
                    doc, XPathConstants.NODE);
            this.hourofbirth = getValFromNode(hourofbirth);
            // beanDateTime.setHour(Integer.parseInt(nodeval));
            Node minuteofbirth = (Node) xPath.evaluate(
                    "/product/minuteofbirth", doc, XPathConstants.NODE);
            this.minuteofbirth = getValFromNode(minuteofbirth);
            // beanDateTime.setMin(Integer.parseInt(nodeval));
            Node secondofbirth = (Node) xPath.evaluate(
                    "/product/secondofbirth", doc, XPathConstants.NODE);
            this.secondofbirth = getValFromNode(secondofbirth);
            // beanDateTime.setSecond(Integer.parseInt(nodeval));
            Node dst = (Node) xPath.evaluate("/product/dst", doc,
                    XPathConstants.NODE);
            this.dst = getValFromNode(dst);
            // beanHoroPersonalInfo.setDST(Integer.parseInt(nodeval));
            Node placeofbirth = (Node) xPath.evaluate("/product/placeofbirth",
                    doc, XPathConstants.NODE);
            this.placeofbirth = getValFromNode(placeofbirth);
            // beanPlace.setCityName(nodeval);
            Node longdeg = (Node) xPath.evaluate("/product/longdeg", doc,
                    XPathConstants.NODE);
            this.longdeg = getValFromNode(longdeg);
            // beanPlace.setLongDeg(nodeval);
            Node longmin = (Node) xPath.evaluate("/product/longmin", doc,
                    XPathConstants.NODE);
            this.longmin = getValFromNode(longmin);
            // beanPlace.setLongMin(nodeval);
            Node longew = (Node) xPath.evaluate("/product/longew ", doc,
                    XPathConstants.NODE);
            this.longew = getValFromNode(longew);
            // beanPlace.setLongDir(nodeval);
            Node latdeg = (Node) xPath.evaluate("/product/latdeg", doc,
                    XPathConstants.NODE);
            this.latdeg = getValFromNode(latdeg);
            // beanPlace.setLatDeg(nodeval);
            Node latmin = (Node) xPath.evaluate("/product/latmin", doc,
                    XPathConstants.NODE);
            this.latmin = getValFromNode(latmin);
            // beanPlace.setLatMin(nodeval);
            Node latns = (Node) xPath.evaluate("/product/latns", doc,
                    XPathConstants.NODE);
            this.latns = getValFromNode(latns);
            // beanPlace.setLatDir(nodeval);
            Node timezone = (Node) xPath.evaluate("/product/timezone", doc,
                    XPathConstants.NODE);
            this.timezone = getValFromNode(timezone);
            // beanPlace.setTimeZone(nodeval);
            Node ayanamsa = (Node) xPath.evaluate("/product/ayanamsa", doc,
                    XPathConstants.NODE);
            this.ayanamsa = getValFromNode(ayanamsa);
            // beanHoroPersonalInfo.setAyan(nodeval);
            Node charttype = (Node) xPath.evaluate("/product/charttype", doc,
                    XPathConstants.NODE);
            this.charttype = getValFromNode(charttype);
            // beanHoroPersonalInfo.setC;
            Node kphn = (Node) xPath.evaluate("/product/kphn", doc,
                    XPathConstants.NODE);
            this.kphn = getValFromNode(kphn);
            /*
             * beanHoroPersonalInfo.setHoraryNumber(Integer.parseInt(nodeval));
             * beanHoroPersonalInfo.setDateTime(beanDateTime);
             * beanHoroPersonalInfo.setPlace(beanPlace);
             */

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /*
     * get Value from node
     */
    private String getValFromNode(Node node) {
        String nodeVal = "";
        if (node != null) {
            nodeVal = node.getTextContent();
        }
        return nodeVal;
    }

    /**
     * This function is used to extract sign up status value
     * returned from server after executing the sign up request.
     *
     * @param signUpValue
     */
    public String[] setSignUpDetails(String signUpValue) {
        String[] iStatus = new String[]{"0", "0"};
        Document doc = CXMLOperations.XMLfromString(signUpValue);
        int numResults = doc.getChildNodes().getLength();
        if ((numResults <= 0)) {
            //
        } else {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                try {

                    XPath xpath = XPathFactory.newInstance().newXPath();
                    String expression = "/product/msgcode"; // first book
                    Node logValue = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
                    if (logValue != null)
                        iStatus[0] = logValue.getTextContent();

                    String expressionmsgId = "/product/msg"; // first book
                    Node msgId = (Node) xpath.evaluate(expressionmsgId, doc, XPathConstants.NODE);
                    if (msgId != null)
                        iStatus[1] = msgId.getTextContent();

                } catch (Exception e) {

                }
            } else {
                NodeList nodes = doc.getElementsByTagName("msgcode");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element e = (Element) nodes.item(i);
                    iStatus[0] = CXMLOperations.getValue(e, "msgcode");
                }

                NodeList nodesMsgId = doc.getElementsByTagName("msg");
                for (int i = 0; i < nodesMsgId.getLength(); i++) {
                    Element e = (Element) nodesMsgId.item(i);
                    iStatus[1] = CXMLOperations.getValue(e, "msg");
                }
            }

        }

        return iStatus;

    }

    /**
     * This getter is used to get login verification value
     *
     * @return
     */
    public String getVerifyingLoginValue() {
        return verifyingLoginValue;
    }

    @Override
    public ArrayList<BeanHoroPersonalInfo> getOnlineChartList(Context context, String kundliName, String uid, String pwd, String key, String isapi) throws Exception {
        ArrayList<BeanHoroPersonalInfo> SavedChartList;
        try {
            getOnlineSavedKundli(context, kundliName, uid, pwd, key, isapi);
            //executeHTTRequestChartList(kundliName, uid, pwd);

        } catch (Exception e) {
            throw e;
        }
        return onLineSavedChart;
    }

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

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID,CUtils.replaceEmailChar(uid) ));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("ChartName", searchText));

        return nameValuePairs;

    }


    private ArrayList<BeanHoroPersonalInfo> setChartList(String _chartList) {

        ArrayList<BeanHoroPersonalInfo> saveChartlist = new ArrayList<BeanHoroPersonalInfo>();

        _chartList = _chartList.replace("<![CDATA[", "");
        _chartList = _chartList.replace("]]>", "");
        _chartList = _chartList.replace("&", "");
        Document doc = CXMLOperations.XMLfromString(_chartList);


        chartListArray = new HashMap<String, String>();
        try {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                BeanHoroPersonalInfo beanHoroPersonalInfo;
                BeanDateTime beanDateTime;
                BeanPlace beanPlace;
                XPath xpath = XPathFactory.newInstance().newXPath();
                String chartIdExp = "/product/chartid";
                String chartNameExp = "/product/chartname";
                String genderExp = "/product/gender";
                String dayOfBirthExp = "/product/dayofbirth";
                String monthOfBirthExp = "/product/monthofbirth";
                String yearOfBirthExp = "/product/yearofbirth";
                String hourOfBirthExp = "/product/hourofbirth";
                String minuteOfBirthExp = "/product/minuteofbirth";
                String secondOfBirthExp = "/product/secondofbirth";
                String placeOfBirthExp = "/product/place_of_birth";
                String longdegExp = "/product/longdeg";
                String longminExp = "/product/longmin";
                String longEWExp = "/product/longEW";
                String latdegExp = "/product/latdeg";
                String latminExp = "/product/latmin";
                String latNSExp = "/product/latNS";
                String timezoneExp = "/product/timezone";
                String ayanamsaExp = "/product/ayanamsa";
                String kphnExp = "/product/kphn";
                String DSTExp = "/product/DST";

                NodeList chartIdNodes = (NodeList) xpath.evaluate(chartIdExp, doc, XPathConstants.NODESET);
                NodeList chartNameNodes = (NodeList) xpath.evaluate(chartNameExp, doc, XPathConstants.NODESET);
                NodeList genderNodes = (NodeList) xpath.evaluate(genderExp, doc, XPathConstants.NODESET);
                NodeList dayOfBirthNodes = (NodeList) xpath.evaluate(dayOfBirthExp, doc, XPathConstants.NODESET);
                NodeList monthOfBirthNodes = (NodeList) xpath.evaluate(monthOfBirthExp, doc, XPathConstants.NODESET);
                NodeList yearOfBirthNodes = (NodeList) xpath.evaluate(yearOfBirthExp, doc, XPathConstants.NODESET);
                NodeList hourOfBirthNodes = (NodeList) xpath.evaluate(hourOfBirthExp, doc, XPathConstants.NODESET);
                NodeList minuteOfBirthNodes = (NodeList) xpath.evaluate(minuteOfBirthExp, doc, XPathConstants.NODESET);
                NodeList secondOfBirthNodes = (NodeList) xpath.evaluate(secondOfBirthExp, doc, XPathConstants.NODESET);
                NodeList placeOfBirthNodes = (NodeList) xpath.evaluate(placeOfBirthExp, doc, XPathConstants.NODESET);
                NodeList longdegNodes = (NodeList) xpath.evaluate(longdegExp, doc, XPathConstants.NODESET);
                NodeList longminNodes = (NodeList) xpath.evaluate(longminExp, doc, XPathConstants.NODESET);
                NodeList longEWNodes = (NodeList) xpath.evaluate(longEWExp, doc, XPathConstants.NODESET);
                NodeList latdegNodes = (NodeList) xpath.evaluate(latdegExp, doc, XPathConstants.NODESET);
                NodeList latminNodes = (NodeList) xpath.evaluate(latminExp, doc, XPathConstants.NODESET);
                NodeList latNSNodes = (NodeList) xpath.evaluate(latNSExp, doc, XPathConstants.NODESET);
                NodeList timezoneNodes = (NodeList) xpath.evaluate(timezoneExp, doc, XPathConstants.NODESET);
                NodeList ayanamsaNodes = (NodeList) xpath.evaluate(ayanamsaExp, doc, XPathConstants.NODESET);
                NodeList kphnNodes = (NodeList) xpath.evaluate(kphnExp, doc, XPathConstants.NODESET);
                NodeList DSTNodes = (NodeList) xpath.evaluate(DSTExp, doc, XPathConstants.NODESET);

                for (int i = 0; i < chartIdNodes.getLength(); i++) {
                    beanHoroPersonalInfo = new BeanHoroPersonalInfo();
                    beanDateTime = new BeanDateTime();
                    beanPlace = new BeanPlace();
                    beanHoroPersonalInfo.setName(chartNameNodes.item(i).getTextContent());
                    beanHoroPersonalInfo.setOnlineChartId(chartIdNodes.item(i).getTextContent());
                    beanHoroPersonalInfo.setGender(genderNodes.item(i).getTextContent());
                    beanDateTime.setDay(Integer.parseInt(dayOfBirthNodes.item(i).getTextContent()));
                    beanDateTime.setMonth(Integer.parseInt(monthOfBirthNodes.item(i).getTextContent()) - 1);
                    beanDateTime.setYear(Integer.parseInt(yearOfBirthNodes.item(i).getTextContent()));
                    beanDateTime.setHour(Integer.parseInt(hourOfBirthNodes.item(i).getTextContent()));
                    beanDateTime.setMin(Integer.parseInt(minuteOfBirthNodes.item(i).getTextContent()));
                    beanDateTime.setSecond(Integer.parseInt(secondOfBirthNodes.item(i).getTextContent()));
                    beanHoroPersonalInfo.setDateTime(beanDateTime);
                    beanPlace.setCityName(placeOfBirthNodes.item(i).getTextContent());
                    beanPlace.setLongDeg(longdegNodes.item(i).getTextContent());
                    beanPlace.setLongMin(longminNodes.item(i).getTextContent());
                    beanPlace.setLongDir(longEWNodes.item(i).getTextContent());
                    beanPlace.setLatDeg(latdegNodes.item(i).getTextContent());
                    beanPlace.setLatMin(latminNodes.item(i).getTextContent());
                    beanPlace.setLatDir(latNSNodes.item(i).getTextContent());

                    //because $ comes from server on behalf of - sign.
                    String timeZone = timezoneNodes.item(i).getTextContent();
                    if (timeZone.contains("$")) {
                        timeZone = timeZone.replace("$", "-");
                        float timezoneFloat = Float.parseFloat(timeZone);

                        beanPlace.setTimeZone(timeZone);
                        beanPlace.setTimeZoneValue(timezoneFloat);

                    } else {
                        beanPlace.setTimeZone(timeZone);
                        float timezoneFloat = Float.parseFloat(timeZone);
                        beanPlace.setTimeZoneValue(timezoneFloat);
                    }

                    // beanPlace.setTimeZone(timezoneNodes.item(i).getTextContent());
                    beanHoroPersonalInfo.setPlace(beanPlace);
                    beanHoroPersonalInfo.setAyan(ayanamsaNodes.item(i).getTextContent());
                    beanHoroPersonalInfo.setAyanIndex(Integer.parseInt(ayanamsaNodes.item(i).getTextContent()));
                    beanHoroPersonalInfo.setHoraryNumber(Integer.parseInt(kphnNodes.item(i).getTextContent()));
                    beanHoroPersonalInfo.setDST(Integer.parseInt(DSTNodes.item(i).getTextContent()));
                    saveChartlist.add(beanHoroPersonalInfo);
                }
            } else {
                BeanHoroPersonalInfo beanHoroPersonalInfo;
                BeanDateTime beanDateTime;
                BeanPlace beanPlace;
                NodeList chartIdNode = doc.getElementsByTagName("chartid");
                NodeList chartNameNode = doc.getElementsByTagName("chartname");
                NodeList genderNode = doc.getElementsByTagName("gender");
                NodeList dayOfBirthNode = doc.getElementsByTagName("dayofbirth");
                NodeList monthOfBirthNode = doc.getElementsByTagName("monthofbirth");
                NodeList yearOfBirthNode = doc.getElementsByTagName("yearofbirth");
                NodeList hourOfBirthNode = doc.getElementsByTagName("hourofbirth");
                NodeList minuteOfBirthNode = doc.getElementsByTagName("minuteofbirth");
                NodeList secondOfBirthNode = doc.getElementsByTagName("secondofbirth");
                NodeList placeOfBirthNode = doc.getElementsByTagName("place_of_birth");
                NodeList longdegNode = doc.getElementsByTagName("longdeg");
                NodeList longminNode = doc.getElementsByTagName("longmin");
                NodeList longEWNode = doc.getElementsByTagName("longEW");
                NodeList latdegNode = doc.getElementsByTagName("latdeg");
                NodeList latminNode = doc.getElementsByTagName("latmin");
                NodeList latNSNode = doc.getElementsByTagName("latNS");
                NodeList timezoneNode = doc.getElementsByTagName("timezone");
                NodeList ayanamsaNode = doc.getElementsByTagName("ayanamsa");
                NodeList kphnNode = doc.getElementsByTagName("kphn");
                NodeList DSTNode = doc.getElementsByTagName("DST");

                for (int i = 0; i < chartIdNode.getLength(); i++) {
                    beanHoroPersonalInfo = new BeanHoroPersonalInfo();
                    beanDateTime = new BeanDateTime();
                    beanPlace = new BeanPlace();
                    beanHoroPersonalInfo.setName(CXMLOperations.getValue((Element) chartIdNode.item(i), "chartid"));
                    beanHoroPersonalInfo.setOnlineChartId(CXMLOperations.getValue((Element) chartNameNode.item(i), "chartname"));
                    beanHoroPersonalInfo.setGender(CXMLOperations.getValue((Element) genderNode.item(i), "gender"));
                    beanDateTime.setDay(Integer.parseInt(CXMLOperations.getValue((Element) dayOfBirthNode.item(i), "dayofbirth")));
                    beanDateTime.setMonth(Integer.parseInt(CXMLOperations.getValue((Element) monthOfBirthNode.item(i), "monthofbirth")));
                    beanDateTime.setYear(Integer.parseInt(CXMLOperations.getValue((Element) yearOfBirthNode.item(i), "yearofbirth")));
                    beanDateTime.setHour(Integer.parseInt(CXMLOperations.getValue((Element) hourOfBirthNode.item(i), "hourofbirth")));
                    beanDateTime.setMin(Integer.parseInt(CXMLOperations.getValue((Element) minuteOfBirthNode.item(i), "minuteofbirth")));
                    beanDateTime.setSecond(Integer.parseInt(CXMLOperations.getValue((Element) secondOfBirthNode.item(i), "secondofbirth")));
                    beanHoroPersonalInfo.setDateTime(beanDateTime);
                    beanPlace.setCityName(CXMLOperations.getValue((Element) placeOfBirthNode.item(i), "place_of_birth"));
                    beanPlace.setLongDeg(CXMLOperations.getValue((Element) longdegNode.item(i), "longdeg"));
                    beanPlace.setLongMin(CXMLOperations.getValue((Element) longminNode.item(i), "longmin"));
                    beanPlace.setLongDir(CXMLOperations.getValue((Element) longEWNode.item(i), "longEW"));
                    beanPlace.setLatDeg(CXMLOperations.getValue((Element) latdegNode.item(i), "latdeg"));
                    beanPlace.setLatMin(CXMLOperations.getValue((Element) latminNode.item(i), "latmin"));
                    beanPlace.setLatDir(CXMLOperations.getValue((Element) latNSNode.item(i), "latNS"));

                    //because $ comes from server on behalf of - sign.
                    String timeZone = CXMLOperations.getValue((Element) timezoneNode.item(i), "timezone");
                    if (timeZone.contains("$")) {
                        timeZone = timeZone.replace("$", "-");
                        float timezoneFloat = Float.parseFloat(timeZone);

                        beanPlace.setTimeZone(timeZone);
                        beanPlace.setTimeZoneValue(timezoneFloat);

                    } else {
                        beanPlace.setTimeZone(timeZone);
                        float timezoneFloat = Float.parseFloat(timeZone);
                        beanPlace.setTimeZoneValue(timezoneFloat);
                    }

                    beanHoroPersonalInfo.setPlace(beanPlace);
                    beanHoroPersonalInfo.setAyan(CXMLOperations.getValue((Element) ayanamsaNode.item(i), "ayanamsa"));
                    beanHoroPersonalInfo.setAyanIndex(Integer.parseInt(CXMLOperations.getValue((Element) ayanamsaNode.item(i), "ayanamsa")));
                    beanHoroPersonalInfo.setHoraryNumber(Integer.parseInt(CXMLOperations.getValue((Element) kphnNode.item(i), "kphn")));
                    beanHoroPersonalInfo.setDST(Integer.parseInt(CXMLOperations.getValue((Element) DSTNode.item(i), "DST")));
                    saveChartlist.add(beanHoroPersonalInfo);
                }
            }
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
        }
        return saveChartlist;
    }

    /*@Override
    public int deleteOnlineKundli(long kundliId, String uid, String pwd) throws Exception {
        // TODO Auto-generated method stub
        return executeHTTRequestOnlineKundliDelete(kundliId, uid, pwd);
    }*/

    /**
     * This function is used to delete chart on astrosage server
     * according to chart id.
     *
     * @param _chartId
     * @return int value(success/fail)
     * @throws Exception
     */
   /* private int executeHTTRequestOnlineKundliDelete(long kundliId, String uid, String pwd) throws Exception {

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
     *
     * @param _chartId
     * @return List<NameValuePair>
     */
    /*private List<NameValuePair> getNameValuePairsOnlineKundliDelete(long kundliId, String uid, String pwd) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(uid)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("chartId", String.valueOf(kundliId)));

        return nameValuePairs;

    }*/

    /**
     * This function is used to extract the status after deleting
     * chart on AstroSage server.
     *
     * @param _status
     * @return int(success / fail)
     */
    /*private int getChartDeleteStatus(String _status) {
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

    }*/

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
    /*@Override
    public BeanHoroPersonalInfo getOnlineKundliDetail(SQLiteDatabase sqLiteDatabase, long kundliId, String uid,
                                                      String pwd) throws Exception {
        // TODO Auto-generated method stub
        try {
            executeHTTRequestOnlineKundliDetail(sqLiteDatabase, kundliId, uid, pwd);
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
    /*private void executeHTTRequestOnlineKundliDetail(SQLiteDatabase sqLiteDatabase, long kundliId, String uid, String pwd) throws Exception {
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
            setChartDetail(sqLiteDatabase, sb.toString());
            //Log.d("InOnline_3", sb.toString());

        } catch (Exception e) {
            //Log.d("InOnline_3", e.getMessage());
            throw e;
        }
    }*/

    /**
     * This function return list of name value pairs to get chart detail
     * from server.
     *
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
    private void setChartDetail(SQLiteDatabase sqLiteDatabase, String _chartDetail) {

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
            setChartDetailBeforeGINGERBREAD(sqLiteDatabase, nodes);

        }
    }

    private void setChartDetailBeforeGINGERBREAD(SQLiteDatabase sqLiteDatabase, NodeList nodes) {
        _horoPersonalInfo = new BeanHoroPersonalInfo();
        try {


            Element e = (Element) nodes.item(0);
            String onlineChartId = CXMLOperations.getValue(e, "chartid").trim();
            long localChartId = new CDataOperations().getLocalChartIdWithRespactiveOnline(sqLiteDatabase, onlineChartId);

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
            objPlace.setCountryName("not define ");
            /****************************************************************/
            objPlace.setCityName(CXMLOperations.getValue(e, "place_of_birth").trim());
            objPlace.setLongDeg(CXMLOperations.getValue(e, "longdeg").trim());
            objPlace.setLongMin(CXMLOperations.getValue(e, "longmin").trim());
            objPlace.setLatDeg(CXMLOperations.getValue(e, "latdeg").trim());
            objPlace.setLatMin(CXMLOperations.getValue(e, "latmin").trim());
            objPlace.setLongDir(CXMLOperations.getValue(e, "longEW").trim());
            objPlace.setLatDir(CXMLOperations.getValue(e, "latNS").trim());

            _horoPersonalInfo.setDST(Integer.parseInt(CXMLOperations.getValue(e, "DST").trim()));

            BeanPlace objTz = new CDataOperations().getNewTimZoneByName(sqLiteDatabase, CXMLOperations.getValue(e,
                    "timezone").trim());
            if (objTz != null) {
                objPlace.setTimeZoneName(objTz.getTimeZoneName().trim());
                objPlace.setTimeZoneValue(objTz.getTimeZoneValue());
                objPlace.setTimeZoneId(objTz.getTimeZoneId());

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
   /* public long[] saveChartOnServer(BeanHoroPersonalInfo beanHoroPersonalInfo, String userId, String pwd) throws Exception {

        return executeHTTRequestSaveChartOnServer(beanHoroPersonalInfo, userId, pwd);
    }*/

    /**
     * This function return list of name value pairs to save chart in online database.
     *
     * @param _birthDetail
     * @return List<NameValuePair>
     */

    private List<NameValuePair> getNameValuePairs_SaveChartOnServer(
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
        /*nameValuePairs.add(new BasicNameValuePair("Place", _birthDetail
                .getCityName().trim()));*/
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
    }

    /**
     * This function is used to save chart on astrisage server
     *
     * @param _birthDetail
     * @return
     * @throws Exception
     */
    /*private long[] executeHTTRequestSaveChartOnServer(BeanHoroPersonalInfo _birthDetail, String userId, String pwd)
            throws Exception {
        if (_birthDetail.getOnlineChartId().equalsIgnoreCase("0")) {
            _birthDetail.setOnlineChartId("");
        }
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

            char_msg = getSaveOnlineChartId(sb.toString(), null);


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
    public long[] getSaveOnlineChartId(String chartSavedId, String updatedOnlineChartId) throws Exception {
        if (updatedOnlineChartId == null) {
            updatedOnlineChartId = this.updatedOnlineChartId;
        }
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
                        XPath xpathChartId = XPathFactory.newInstance()
                                .newXPath();
                        String chartIdExp = "/product/chartid"; // chart id
                        NodeList chartIdNodes = (NodeList) xpathChartId
                                .evaluate(chartIdExp, doc,
                                        XPathConstants.NODESET);
                        _chartId = Long.valueOf(chartIdNodes.item(0)
                                .getTextContent());

                        String msgcodeExp = "/product/msgcode"; // msgcode
                        NodeList msgcodeNodes = (NodeList) xpathChartId
                                .evaluate(msgcodeExp, doc,
                                        XPathConstants.NODESET);
                        _msgcode = Long.valueOf(msgcodeNodes.item(0)
                                .getTextContent());

                        char_msg[0] = _chartId;
                        char_msg[1] = _msgcode;
                    } catch (Exception e) {
                        // THIS CODE IS ADDED BY BIJENDRA ON 28-NOV-2012
                        // DESCRIPTION:This code is run when the chart saving
                        // account is full on server
                        // this code is written in catch because xml values
                        // changed so it throw exception
                        XPath xpathMsgId = XPathFactory.newInstance()
                                .newXPath();
                        String msgCodeExp = "/product/msgcode"; // message code
                        NodeList msgcodeNodes = (NodeList) xpathMsgId.evaluate(
                                msgCodeExp, doc, XPathConstants.NODESET);
                        _msgcode = Long.valueOf(msgcodeNodes.item(0)
                                .getTextContent());
                        if ((updatedOnlineChartId != null) && (_msgcode == 3)
                                || (_msgcode == 4) || (_msgcode == 5)) {
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
                            Element e1 = (Element) nodes.item(i);
                            _chartId = Long.valueOf(CXMLOperations.getValue(e1,
                                    "chartid"));
                        }
                        for (int j = 0; j < nodes.getLength(); j++) {
                            Element e2 = (Element) nodes.item(j);
                            _msgcode = Long.valueOf(CXMLOperations.getValue(e2,
                                    "msgcode"));
                        }
                        char_msg[0] = _chartId;
                        char_msg[1] = _msgcode;
                    } catch (Exception e) {
                        // THIS CODE IS ADDED BY BIJENDRA ON 28-NOV-2012
                        // DESCRIPTION:This code is run when the chart saving
                        // account is full on server
                        // this code is written in catch because xml values
                        // changed so it throw exception
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element eMsg = (Element) nodes.item(i);
                            _msgcode = Long.valueOf(CXMLOperations.getValue(
                                    eMsg, "msgcode"));
                        }
                        if ((updatedOnlineChartId != null) && (_msgcode == 3)
                                || (_msgcode == 4) || (_msgcode == 5)) {
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
     * @param userId
     * @param name
     * @return String[]
     * @throws Exception
     */

   /* public String[] shareKundliNameOnline(String userId, String pwd, String name, String chartId)
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

   /* private String[] getShareStatusResult(String strData) {
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
    }*/
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

    /*public String[] checkAvailableKundliNameToShare(String kundliName)
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
    private List<NameValuePair> getNameValuePairs_Check_Available_Name_To_Share(String kundliName) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("publicchartname", kundliName));

        return nameValuePairs;
    }

    private String[] getAvaliableNameToShare(String strData) {
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
    }

    /**
     * this function is used to update Default Kundli On server
     *
     * @return
     */
    /*public String UpdateDefaultKundliOnServer(String kundliId, String userId,
                                              String password) {
        String msgCode = "";
        try {
            msgCode = executeHTTRequestUpdateDefaultKundliOnServer(kundliId,
                    userId, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return msgCode;
    }*/

    /*public String changePasswordAndProfile(String emailId, String newPassword, String oldPassword, String userName, String key) {
        String restultStr = "";
        BufferedReader in = null;
        HttpClient hc = CUtils.getNewHttpClient();
        HttpPost hp = new HttpPost(CGlobalVariables.CHANGE_PASSWORD_USERNAME);
        try {
            hp.setEntity(new UrlEncodedFormEntity(
                    getNameValuePairsToChangePasswordAndProfile(emailId, newPassword, oldPassword, userName, key), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            restultStr = sb.toString();

        } catch (Exception e) {
            //Log.e("Exception ", e.getMessage());
        }

        return restultStr;

       *//* String[] iStatus = new String[]{"0", "0"};
        String strReturn = "";
        BufferedReader in = null;
        HttpClient hc = CUtils.getNewHttpClient();
        HttpPost hp = new HttpPost(CGlobalVariables.REGISTRATION_USER_WITHOUT_PASSWORD);
        try {
            hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Signup(
                    userid, key), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            iStatus = parseSignupResult(sb.toString());

        } catch (Exception e) {

            throw e;
        }

        return iStatus;*//*
    }*/

    /*private String executeHTTRequestUpdateDefaultKundliOnServer(
            String kundliId, String userId, String password) throws Exception {
        String restultStr = "";
        BufferedReader in = null;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        //CGlobalVariables.UPDATE_DEFAULT_CHART_URL;
        HttpPost hp = new HttpPost();
        try {
            hp.setEntity(new UrlEncodedFormEntity(
                    getNameValuePairsToUpdateDefaultKundliOnServer(kundliId,
                            userId, password), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);

            in.close();

            restultStr = sb.toString();

        } catch (Exception e) {
            throw e;
        }

        return restultStr;

    }*/
    private List<NameValuePair> getNameValuePairsToUpdateDefaultKundliOnServer(
            String kundliId, String uid, String pwd) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

        nameValuePairs.add(new BasicNameValuePair("seconduserparam", uid));
        nameValuePairs.add(new BasicNameValuePair("thirdpassparam", pwd));
        nameValuePairs.add(new BasicNameValuePair("firstchartparam", kundliId));

        return nameValuePairs;

    }

    private List<NameValuePair> getNameValuePairsToChangePasswordAndProfile(String emailId, String newPassword, String oldPassword, String userName, String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(emailId)));
        nameValuePairs.add(new BasicNameValuePair("npw", newPassword));
        nameValuePairs.add(new BasicNameValuePair("opw", oldPassword));
        nameValuePairs.add(new BasicNameValuePair("userfirstname", userName));
        nameValuePairs.add(new BasicNameValuePair("key", key));

        return nameValuePairs;

    }

    private void getOnlineSavedKundli(final Context context, final String kundliName, final String uid,
                                      final String pwd, final String key, final String isapi) {

        RequestQueue queue = Volley.newRequestQueue(context);

        String url= CGlobalVariables.OPEN_CHART_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onLineSavedChart = setChartList(response.toString());
                        if (context instanceof HomeMatchMakingInputScreen) {
                            ((HomeMatchMakingInputScreen) context).searchBirthDetailsFragment.showOnlineSearchedKundli(onLineSavedChart);
                        } else if (context instanceof HomeInputScreen) {
                            ((HomeInputScreen) context).searchBirthDetailsFragment.showOnlineSearchedKundli(onLineSavedChart);
                        } else if (context instanceof MychartActivity) {
                            ((MychartActivity) context).searchBirthDetailsFragment.showOnlineSearchedKundli(onLineSavedChart);
                        }

                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
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

                // pd.dismiss();
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
                headers.put("key", key);
                headers.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(uid));
                headers.put(CGlobalVariables.KEY_PASSWORD, pwd);
                headers.put("isapi", isapi);
                headers.put("ChartName", kundliName);
                //Log.e("searchbirth", "LL" + headers);
                return headers;
            }

        };
        int socketTimeout = 10000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

   /* @Override
    public String syncChartOnServer(Context context, String userName, String password, String jsonString) {
        return syncChart(context, userName, password, jsonString);
    }*/

   /* private String syncChart(Context context, String userName, String password, String jsonString) {
        String restultStr = "";
        BufferedReader in = null;
        HttpClient hc = CUtils.getNewHttpClient();
        String url = CGlobalVariables.SYNCCHARTURL;
        //String url= "http://192.168.1.70/as/chart-synch.asp?";
        HttpPost hp = new HttpPost(url);
        try {
            hp.setEntity(new UrlEncodedFormEntity(
                    getNameValuePairForSyncCharts(context, userName, password, jsonString), HTTP.UTF_8));
            HttpResponse response = hc.execute(hp);

            Log.i("Result>>", "");
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);
            in.close();
            restultStr = sb.toString();
            Log.i("Result>>", restultStr);
        } catch (Exception e) {
            Log.e("Exception ", e.getMessage());
            restultStr = "";
        }
        Log.e("Result ", restultStr);
        return restultStr;
    }*/

    /*private List<NameValuePair> getNameValuePairForSyncCharts(Context context, String userId, String pwd, String chartDetail) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        String key = CUtils.getApplicationSignatureHashCode(context);
        nameValuePairs.add(new BasicNameValuePair("key", key));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userId)));
        nameValuePairs.add(new BasicNameValuePair(CGlobalVariables.KEY_PASSWORD, pwd));
        nameValuePairs.add(new BasicNameValuePair("jsonInput", chartDetail));
        return nameValuePairs;
    }*/
}
