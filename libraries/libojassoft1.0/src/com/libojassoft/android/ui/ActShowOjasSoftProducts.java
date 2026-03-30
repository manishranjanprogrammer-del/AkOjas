package com.libojassoft.android.ui;

/*import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;*/
import com.libojassoft.android.R;
import com.libojassoft.android.utils.LibCGlobalVariables;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


public class ActShowOjasSoftProducts extends Activity {

	/** Called when the activity is first created. */
	public static final String PREFS_NAME = "PROD_ASTRO";
	// private String _uri="http://feeds.feedburner.com/vedicastrology";
	private String _uri = "https://itzhoroscope.astrosage.com/";
	final Activity activity = this;
	LinearLayout advLayout;
	private String title = "";
	private String MY_AD_ID = null;
	WebView webView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.libojassoft_astrosageproductsnotification);
		

		// ADDED BY BIJENDRA ON 08-JAN-2013
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			try {
				String value = extras.getString("BLOG_LINK_TO_SHOW");
				 title = extras.getString("TITLE_TO_SHOW");
				 MY_AD_ID = extras.getString(LibCGlobalVariables.CALLER_APP_AD_ID);
				if (value != null)
					_uri = value;
				setBlogTitle(title);
				//Log.d("BLOG_LINK_",_uri);

			} catch (Exception e) {
				//Log.d("BLOG_LINK_ID_ERROR", e.getMessage());
			}
		}
		// END
		// Logic to show web page in the activity
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setTitle("Loading...");
				activity.setProgress(progress * 100);

				if (progress == 100)
					setBlogTitle(title);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// Handle the error
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				try {
					view.loadUrl(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});

		webView.loadUrl(_uri);

		// End Logic
		
		

	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		
		if(MY_AD_ID != null){
			showAdvertisement();
		}
		super.onStart();
	}


	/**
	 * Shows advertisement on adView.
	 */
	private void showAdvertisement() {
		/*advLayout = (LinearLayout)findViewById(R.id.advtLayout);
		advLayout.removeAllViews();
		// Create the adView
		AdView adView = new AdView(this, AdSize.SMART_BANNER, MY_AD_ID);
		// Add the adView to it
		advLayout.addView(adView);
		// Initiate a generic request to load it with an ad
		AdRequest request = new AdRequest();
		adView.loadAd(request);*/
	}
	private void setBlogTitle(String title) {
		// TODO Auto-generated method stub
		this.setTitle("AstroSage.com : "+title);
	}

}