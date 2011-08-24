package com.webviewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SiteViewer extends Activity
{
	WebView siteview;
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.siteviewer);
    	 if (savedInstanceState != null)
    	      ((WebView)findViewById(R.id.siteviewer)).restoreState(savedInstanceState);
    	Bundle extras=getIntent().getExtras();
    	String site=extras.getString("site");
    	siteview=(WebView) findViewById(R.id.siteviewer);
    	WebSettings siteviewSettings = siteview.getSettings();
    	siteviewSettings.setJavaScriptEnabled(true);
        siteview.setWebViewClient(new WebViewClient()
        {
        	public boolean shouldOverrideUrlLoading(WebView view, String url)
        	{
                view.loadUrl(url);
                return true;
            }
        	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        	{
        		Toast webError=Toast.makeText(SiteViewer.this, "Oh no! " + description, Toast.LENGTH_SHORT);
        		webError.show();
        	}
        	
        });

    	siteview.loadUrl("http://" + site);
    }
    
    protected void onSaveInstanceState(Bundle outState) {
        siteview.saveState(outState);
     }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && siteview.canGoBack()) {
            siteview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
