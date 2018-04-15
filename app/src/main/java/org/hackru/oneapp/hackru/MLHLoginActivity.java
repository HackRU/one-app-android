package org.hackru.oneapp.hackru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.hackru.oneapp.hackru.api.model.Login;
import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility;

public class MLHLoginActivity extends AppCompatActivity {
    String TAG = "WEBVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlhlogin);

        setTitle("MLH Login");

        final WebView myWebView = (WebView) findViewById(R.id.webview);

        String url = "https://my.mlh.io/oauth/authorize?client_id=bab4ace712bb186d8866ff4776baf96b2c4e9c64d729fb7f88e87357e4badcba&redirect_uri=https://m7cwj1fy7c.execute-api.us-west-2.amazonaws.com/mlhtest/mlhcallback&response_type=code&scope=email+education+birthday";

        //Make sure No cookies are created
//        CookieManager.getInstance().setAcceptCookie(false);
//        CookieManager.getInstance().setCookie(url, "fr='057YH5iFLod9fGROu..BazWdp...1.0.BazWdp.'");

        //TODO: Test to make sure we don't need to load the webview as the callback
        CookieManager.getInstance().removeAllCookies(null);

        //Make sure no caching is done
//        myWebView.getSettings().setCacheMode(myWebView.getSettings().LOAD_NO_CACHE);
//        myWebView.getSettings().setAppCacheEnabled(false);
//        myWebView.clearHistory();
//        myWebView.clearCache(true);

        //Enable javascript
        myWebView.getSettings().setJavaScriptEnabled(true);

        //Finally load the login page
        myWebView.loadUrl(url);

        //Webview redirect logic
        myWebView.setWebViewClient(new WebViewClient() {
            //TODO: Get this working if the stutter is an issue or there is time to implement
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon){
//                myWebView.loadUrl(
//                        "javascript:(function() { " +
//                                "var callback = function(){" +
//                                "document.querySelectorAll('[href=\"/login\"]').item(0).click();" +
//                                "};" +
//                                "if (" +
//                                "    document.readyState === \"complete\" ||" +
//                                "    (document.readyState !== \"loading\" && !document.documentElement.doScroll)" +
//                                ") {" +
//                                "  callback();" +
//                                "} else {" +
//                                "  document.addEventListener(\"DOMContentLoaded\", callback);" +
//                                "}"
//                                +
//                                "})()");
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.indexOf("hackru.org") != -1) {
                    Log.e(TAG, "FOUND: " + url);
//                    Uri uri = Uri.parse(url);
                    String test = Uri.decode(url);
                    String json = test.substring(test.indexOf("authdata=")+9);

                    Log.e(TAG, json);
//                    String token = json.substring(json.indexOf("token"), json.indexOf("\","));
//                    String email = json.substring(json.indexOf("email")+9, json.indexOf("\"}}"));
                    String token = json.substring(json.indexOf("token")+9, json.indexOf(",")-1);
                    String email = json.substring(json.indexOf("email")+9, json.indexOf("}}")-1);
                    Log.e(TAG, token);
                    Log.e(TAG, email);

                    onLoginSuccess(email, token);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                myWebView.loadUrl(
                        "javascript:(function() { " +
                                "document.querySelectorAll('[href=\"/login\"]').item(0).click();"
                                +
                                "})()");
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void onLoginSuccess(String email, String token) {
        SharedPreferencesUtility.setEmail(this, email);
        SharedPreferencesUtility.setAuthToken(this, token);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
