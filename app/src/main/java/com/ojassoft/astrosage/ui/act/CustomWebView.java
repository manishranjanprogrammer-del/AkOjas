package com.ojassoft.astrosage.ui.act;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;

public class CustomWebView extends AppCompatActivity {


    private boolean javascriptInterfaceBroken = false;
    private ProgressBar progressBar;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout relativeView;
    private Button textShowStatus;
    android.webkit.WebView m_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        m_webview = (android.webkit.WebView) findViewById(R.id.webView);
        m_webview.getSettings().setJavaScriptEnabled(true);
        fixWebViewJSInterface(m_webview, new JsInterface(), "Android", "_gbjsfix:");
        initProgressBar();
        m_webview.loadUrl(CGlobalVariables.hostName);
        //http://astrology.astrosage.com
        //https://m.astrosage.com/
        //https://marriage.astrosage.com/Default.aspx?lang=2&utm_source=app&utm_medium=icon&utm_campaign=ak-home-icons
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);

        textShowStatus = new Button(this, null, android.R.attr.buttonStyle);

        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeView = new RelativeLayout(this);
        relativeView.setLayoutParams(params);
        relativeView.setGravity(Gravity.CENTER);
    }

    private void showProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
            m_webview.removeAllViews();
        } catch (Exception e) {
            //Log.e("ActAstroSageMarriage", e.getMessage());
        }
    }

    /**
     * @see http://twigstechtips.blogspot.com.au/2013/09/android-webviewaddjavascriptinterface.html
     */
    protected void fixWebViewJSInterface(android.webkit.WebView webview, Object jsInterface, String jsInterfaceName, String jsSignature) {
        // Gingerbread specific code
        if (Build.VERSION.RELEASE.startsWith("2.3")) {
            javascriptInterfaceBroken = true;
        }
        // Everything else is fine
        else {
            webview.addJavascriptInterface(jsInterface, jsInterfaceName);
        }

        webview.setWebViewClient(new GingerbreadWebViewClient(jsInterface, jsInterfaceName, jsSignature));
        webview.setWebChromeClient(new GingerbreadWebViewChrome(jsInterface, jsSignature));
    }

    private class GingerbreadWebViewClient extends WebViewClient {
        private Object jsInterface;
        private String jsInterfaceName;
        private String jsSignature;

        public GingerbreadWebViewClient(Object jsInterface, String jsInterfaceName, String jsSignature) {
            this.jsInterface = jsInterface;
            this.jsInterfaceName = jsInterfaceName;
            this.jsSignature = jsSignature;
        }


        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            super.onPageFinished(view, url);

            if (javascriptInterfaceBroken) {
                StringBuilder gbjs = new StringBuilder();

                gbjs.append("javascript: ");
                gbjs.append(generateJS());

                view.loadUrl(gbjs.toString());
            }

            // Initialise the page
            view.loadUrl("javascript: android_init();");
        }


        /**
         * What this JS wrapper function does is convert all the arguments to strings,
         * in JSON format before sending it to Android in the form of a prompt() alert.
         * <p/>
         * JSON data is returned by Android and unwrapped as the result.
         */
        public String generateJS() {
            StringBuilder gbjs = new StringBuilder();

            if (javascriptInterfaceBroken) {
                StringBuilder sb;

                gbjs.append("var ");
                gbjs.append(jsInterfaceName);
                gbjs.append(" = { " +
                        " _gbFix: function(fxname, xargs) {" +
                        " var args = new Array();" +
                        " for (var i = 0; i < xargs.length; i++) {" +
                        " args.push(xargs[i].toString());" +
                        " };" +
                        " var data = { name: fxname, len: args.length, args: args };" +
                        " var json = JSON.stringify(data);" +
                        " var res = prompt('");
                gbjs.append(jsSignature);
                gbjs.append("' + json);" +
                        " return JSON.parse(res)['result'];" +
                        " }" +
                        "};");

                // Build methods for each method in the JSInterface class.
                for (Method m : jsInterface.getClass().getMethods()) {
                    sb = new StringBuilder();

                    // Output = "Android.showToast = function() { return this._gbFix('showToast', arguments); };"
                    sb.append(jsInterfaceName);
                    sb.append(".");
                    sb.append(m.getName());
                    sb.append(" = function() { return this._gbFix('");
                    sb.append(m.getName());
                    sb.append("', arguments); };");

                    gbjs.append(sb);
                }
            }

            return gbjs.toString();
        }
    }


    private class GingerbreadWebViewChrome extends WebChromeClient {
        private Object jsInterface;
        private String jsSignature;


        public GingerbreadWebViewChrome(Object jsInterface, String jsSignature) {
            this.jsInterface = jsInterface;
            this.jsSignature = jsSignature;
        }


        @Override
        public void onProgressChanged(WebView view, int progress) {
            // TODO Auto-generated method stub
            super.onProgressChanged(view, progress);
            try {
                showProgressBar(true);

                if (progress == 100)
                    showProgressBar(false);

            } catch (Exception e) {
                //Log.e("ActAstroSageMarriage", e.getMessage());
            }
        }

        @Override
        public boolean onJsPrompt(android.webkit.WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (!javascriptInterfaceBroken || TextUtils.isEmpty(message) || !message.startsWith(jsSignature)) {
                return false;
            }

            // We've hit some code through _gbFix()
            JSONObject jsonData;
            String functionName;
            String encodedData;

            try {
                encodedData = message.substring(jsSignature.length());
                jsonData = new JSONObject(encodedData);
                encodedData = null; // no longer needed, clear memory
                functionName = jsonData.getString("name");

                for (Method m : jsInterface.getClass().getMethods()) {
                    if (m.getName().equals(functionName)) {
                        JSONArray jsonArgs = jsonData.getJSONArray("args");
                        Object[] args = new Object[jsonArgs.length()];

                        for (int i = 0; i < jsonArgs.length(); i++) {
                            args[i] = jsonArgs.get(i);
                        }

                        Object ret = m.invoke(jsInterface, args);
                        JSONObject res = new JSONObject();
                        res.put("result", ret);
                        result.confirm(res.toString());
                        return true;
                    }
                }

                // No matching method name found, should throw an exception.
                throw new RuntimeException("shouldOverrideUrlLoading: Could not find method '" + functionName + "()'.");
            } catch (IllegalArgumentException e) {
                //Log.e("GingerbreadWebViewClient", "shouldOverrideUrlLoading: Please ensure your JSInterface methods only have String as parameters.");
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }


    // javascript interface
    private class JsInterface {
        // function that will be called from assets/test.js
        // js example: android.log('my message');
        public void log(String msg) {
            Log.d("MSG FROM JAVASCRIPT", msg);
            Toast.makeText(getApplicationContext(), "JavaScript working...", 1)
                    .show();
        }
    }


}
