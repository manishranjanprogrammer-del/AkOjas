package com.ojassoft.astrosage.misc;



import android.app.Service;
import android.content.Intent;

import android.os.IBinder;



public class ServiceToGetImageDtaFromServer extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        String imageUrl = "";
        if (intent != null && intent.getExtras() != null) {
            imageUrl = intent.getExtras().getString("imageUrl");
        }


        /*if (!imageUrl.equals("")) {
            new ImageDataFromServe().execute(imageUrl);
        } else {
            ServiceToGetImageDtaFromServer.this.stopSelf();
        }*/

        return START_REDELIVER_INTENT;
    }

   /* private class ImageDataFromServe extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String result = getImageDataFromServer(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            try {
                if (!result.equals("")) {
                    CUtils.saveStringData(ServiceToGetImageDtaFromServer.this, CGlobalVariables.imageDataForAdvertisement, result);
                } else {
                    ServiceToGetImageDtaFromServer.this.stopSelf();
                }
                super.onPostExecute(result);
            } catch (Exception ex) {

            }
        }

    }*/

    /*private String getImageDataFromServer(String requestURL) {

        HttpURLConnection httpConn = null;
        InputStream inputStream = null;
        String result = "";

        try {
            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setConnectTimeout(15000);

            httpConn.setReadTimeout(15000);

            httpConn.setUseCaches(false);

            httpConn.setDoInput(true); // true if we want to read server's response
            httpConn.setDoOutput(false);

            try {

                inputStream = httpConn.getInputStream();

				    *//*BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);//iso-8859-1
	                StringBuilder sb = new StringBuilder();
	                String line = null;
	                while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	                    // sb.append(URLDecoder.decode(line + "\n","UTF-8"));
	                }*//*
                //convert input stream into bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                inputStream.close();

                //convert bitmap into string
                result = CUtils.BitMapToString(bitmap);


            } catch (Exception ex) {
                //Log.i(ex.getMessage().toString());
            }

        } catch (Exception ex) {

        }

        return result;

    }*/

}
