package com.libojassoft.android.misc;


import java.util.ArrayList;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.libojassoft.android.beans.LibOutPlace;

/**
 * This class is responsible for all the operation related to AstroSage Atlas.
 *
 * @author Bijendra
 * @version 1.0.0
 * @date 30 may 2013
 * @modify -
 */
public class CAstroSageAtlas {

    /**
     * Local Variables
     */
    private String ID = "id";
    private String PLACE = "place";
    private String STATE = "state";
    private String LONGITUDE = "longitude";
    private String LATITUDE = "latitude";
    private String TIMEZONE = "timezone";
    private String COUNTRY = "country";
    private String TIMEZONE_STRING = "timezonestring";

    public CAstroSageAtlas() {

    }

    /**
     * This function return the list of searched city
     *
     * @param placeName
     * @return ArrayList<LibOutPlace>
     * @throws Exception
     * @author Bijendra 31-may-13
     */
   /* public ArrayList<LibOutPlace> searchPlace(String placeName)
            throws Exception {
        ArrayList<LibOutPlace> place = null;
        String retData = null;
        try {
			*//*retData = executeHTTRequest(LibCGlobalVariables.ATRROSAGE_ATLAS_PLACE_SEARCH
					+ placeName);*//* //DISABLED BY BIJENDRA (04-04-14)

            //ADDED BY BIJENDRA (04-04-14)
            retData = executeHTTRequestToSearchCity(LibCGlobalVariables.ASTROSAGE_ATLAS_PLACE_SEARCH
                    , placeName);
            //if (retData.trim().length() > 0)
            if (retData.trim().length() > 5)
                place = getSearchedPlaceListFromJSON(retData);
            else
                throw new Exception("Unable to fatch data from astrosage");


        } catch (Exception e) {
            throw new Exception("Unable to fatch data from astrosage");
        }

        return place;

    }*/

    /**
     * This function convert string into JSON objects and return list of searched city
     *
     * @param data
     * @return ArrayList<LibOutPlace>
     * @author Bijendra 31-may-13
     */
    private ArrayList<LibOutPlace> getSearchedPlaceListFromJSON(String data) throws Exception {
        JSONArray arr = null;
        ArrayList<LibOutPlace> places = null;

        try {
            arr = new JSONArray(data);
            if (arr.length() > 0) {
                places = new ArrayList<LibOutPlace>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    LibOutPlace pl = new LibOutPlace();
                    pl.setId(obj.getString(ID));
                    pl.setName(obj.getString(PLACE));
                    pl.setState(obj.getString(STATE));
                    pl.setCountry(obj.getString(COUNTRY));
                    places.add(pl);

                }

            }
        } catch (Exception e) {
            throw e;

        }
        return places;

    }

    /**
     * This function convert string into JSON objects and return object of place
     *
     * @param data
     * @return LibOutPlace
     * @author Bijendra 31-may-13
     */
    private LibOutPlace getPlaceDetailFromJSON(String data) {

        LibOutPlace placeDetail = null;

        try {
            JSONArray array = new JSONArray(data);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                if (obj != null) {
                    placeDetail = new LibOutPlace();
                    placeDetail.setId(obj.getString(ID));
                    placeDetail.setName(obj.getString(PLACE));
                    placeDetail.setState(obj.getString(STATE));
                    placeDetail.setCountry(obj.getString(COUNTRY));
                    placeDetail.setLatitude(obj.getString(LATITUDE));
                    placeDetail.setLongitude(obj.getString(LONGITUDE));
                    placeDetail.setTimezone(obj.getString(TIMEZONE));
                    placeDetail.setTimeZoneString(obj.getString(TIMEZONE_STRING));

                }
            }
        } catch (Exception e) {

        }
        return placeDetail;

    }

    /**
     * This function return the city detail
     * @param placeId
     * @return LibOutPlace
     * @throws Exception
     * @author Bijendra 31-may-13
     */
	/*public LibOutPlace getPlaceDetail(String placeId) throws Exception {
		String retData = null;
		LibOutPlace place = null;
		try {
			retData = executeHTTRequest(LibCGlobalVariables.ASTROSAGEE_ATLAS_GET_PLACE_DETAIL
					+ placeId);
			if (retData.trim().length() >5)
				place = getPlaceDetailFromJSON(retData);
			else
				throw new Exception("Unable to fatch data from astrosage");
			
		} catch (Exception e) {
			throw new Exception("Unable to fatch data from astrosage");
			// retData=e.getMessage();
		}

		return place;

	}*/

	/*private String executeHTTRequest(String apiURL) throws Exception {
		URL url = new URL(apiURL);
		URLConnection conn = url.openConnection();
		HttpURLConnection httpConn=null ;
		int response = -1;

		String line = "";
		StringBuilder data = new StringBuilder();

		InputStream in = null;
		
		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try {
			//HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			
		
			//in = OpenHttpConnection(apiURL);
			BufferedReader rd = new BufferedReader(new InputStreamReader(in));
			while ((line = rd.readLine()) != null && !Thread.interrupted()) {
				data.append(line);
			}
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}

		finally
		{
			in.close();
			httpConn.disconnect();
			conn=null;
		}


		return data.toString();
	}*/

	/*private InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting");
		}
		return in;
	}*/

    /**
     * This function is used to search city list ,according to passed  city name
     *
     * @param apiURL
     * @param placename
     * @return String
     * @throws Exception
     */
    /*private String executeHTTRequestToSearchCity(String apiURL, String placename) throws Exception {

        if (placename.contains(" ")) {
            placename = placename.replaceAll(" ", "%20");
        }

        String _url = apiURL + placename;
        URL url = new URL(_url);
        URLConnection conn = url.openConnection();
        HttpURLConnection httpConn = null;
        int response = -1;

        String line = "";
        StringBuilder data = new StringBuilder();

        InputStream in = null;

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            //HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();


                //in = OpenHttpConnection(apiURL);
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                while ((line = rd.readLine()) != null) {
                    data.append(line);
                }
            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw e1;
        } finally {
            in.close();
            httpConn.disconnect();
            conn = null;
        }


        return data.toString();
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
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 1000 * 60;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return httpParameters;
    }


}
