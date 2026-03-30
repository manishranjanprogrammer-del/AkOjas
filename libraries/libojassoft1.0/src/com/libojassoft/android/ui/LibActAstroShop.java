package com.libojassoft.android.ui;

import com.libojassoft.android.R;

import com.libojassoft.android.utils.LibCGlobalVariables;

import com.libojassoft.android.utils.LibCUtils;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class LibActAstroShop extends Activity {

	private String _uri = "";
	final Activity activity = this;
	private int pageIndex = 0;
	private TextView _tvTitle;
	private ViewGroup gv;
	private ProgressDialog pd;
	WebView webView ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LibCUtils.changeAppResourceTypeForLanguage(LibActAstroShop.this,LibCGlobalVariables.LIB_LANGUAGE_CODE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.libojassoft_lay_astroshop);
		pageIndex = getIntent().getIntExtra("ASTRO_SHOP_PAGE_INDEX", 0);
		_uri = LibCUtils.getAstroSageShopPage(pageIndex,getApplicationContext());
		showAstrShopPage();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void showAstrShopPage()
	{	
		
		 webView = (WebView) findViewById(R.id.webview);	
		
        webView.getSettings().setJavaScriptEnabled(true);
        showDialogBox();
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //activity.setTitle("Loading...");
                activity.setProgress(progress * 100);
               
 
               // if(progress == 100)
                 //   activity.setTitle(R.string.app_name);
             
            }
        });
 
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                // Handle the error
            	cancelProgessingDialogBox();
            	
            }
            
			
            @Override
            public void onPageFinished(WebView view, String url) {
            	// TODO Auto-generated method stub
            	cancelProgessingDialogBox();
            	
            	super.onPageFinished(view, url);
            	
            	
            }
        });
        
       
 
        webView.loadUrl(_uri);
        
       
	}
	
	private void cancelProgessingDialogBox()
	{
		try
    	{
    	if(pd!=null && pd.isShowing())
    		pd.dismiss();
    	}
    	catch(Exception e)
    	{
    		
    	}
	}
	
	private void showDialogBox()
	 {
			pd = null;
			pd = new ProgressDialog(LibActAstroShop.this);
			pd.setMessage(getResources().getString(R.string.lib_pleasewait));
			pd.setCancelable(false);
			pd.setButton(DialogInterface.BUTTON_NEGATIVE,
					getResources().getString(R.string.lib_cancel),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							pd.dismiss();
						}
					});

			pd.show();
			

			TextView tvMsg = (TextView) pd.findViewById(android.R.id.message);
			tvMsg.setTypeface(LibCGlobalVariables.SHOW_FONT_TYPE);
			tvMsg.setTextSize(20);

			Button button = (Button) pd.findViewById(android.R.id.button2);
			button.setTypeface(LibCGlobalVariables.SHOW_FONT_TYPE);
	
		}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(webView.canGoBack())
			{
				webView.goBack();
				return true;
			}
			else
				this.finish();
			
			break;
		
		default:
			return false;
		}

		return false;

	}
	

}
