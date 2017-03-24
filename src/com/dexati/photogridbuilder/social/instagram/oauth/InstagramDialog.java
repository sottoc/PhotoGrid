package com.dexati.photogridbuilder.social.instagram.oauth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.webkit.*;

// Referenced classes of package com.dexati.photogridbuilder.social.instagram.oauth:
//            InstagramApp

public class InstagramDialog extends Dialog
{
    public static interface OAuthDialogListener
    {

        public abstract void onComplete(String s);

        public abstract void onError(String s);
    }

    private class OAuthWebViewClient extends WebViewClient
    {
        public void onPageFinished(WebView webview, String s)
        {
            if(mSpinner != null)
            {
                mSpinner.dismiss();
            }
            super.onPageFinished(webview, s);
        }

        public void onPageStarted(WebView webview, String s, Bitmap bitmap)
        {
            Log.d("Instagram-WebView", (new StringBuilder("Loading URL: ")).append(s).toString());
            mSpinner.show();
            super.onPageStarted(webview, s, bitmap);
        }

        public void onReceivedError(WebView webview, int i, String s, String s1)
        {
            Log.d("Instagram-WebView", (new StringBuilder("Page error: ")).append(s).toString());
            super.onReceivedError(webview, i, s, s1);
            mListener.onError(s);
            dismiss();
        }

        public boolean shouldOverrideUrlLoading(WebView webview, String s)
        {
            Log.d("Instagram-WebView", (new StringBuilder("Redirecting URL ")).append(s).toString());
            if(s.startsWith(InstagramApp.mCallbackUrl))
            {
                String as[] = s.split("=");
                mListener.onComplete(as[1]);
                dismiss();
                return true;
            } else
            {
                return false;
            }
        }

        private OAuthWebViewClient()
        {
            super();
        }

        OAuthWebViewClient(OAuthWebViewClient oauthwebviewclient)
        {
            this();
        }
    }


    static final float DIMENSIONS_LANDSCAPE[] = {
        460F, 260F
    };
    static final float DIMENSIONS_PORTRAIT[] = {
        350F, 450F
    };
    static final int MARGIN = 4;
    static final int PADDING = 2;
    private static final String TAG = "Instagram-WebView";
    private Context mContext;
    private OAuthDialogListener mListener;
    private ProgressDialog mSpinner;
    private String mUrl;
    private WebView mWebView;
    private View view;

    public InstagramDialog(Context context, String s, OAuthDialogListener oauthdialoglistener)
    {
        super(context);
        mContext = context;
        mUrl = s;
        mListener = oauthdialoglistener;
    }

    private void setUpWebView()
    {
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new OAuthWebViewClient(null));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
    }

    public void onBackPressed()
    {
        if(mSpinner != null)
        {
            mSpinner.dismiss();
        }
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(1);
        mSpinner.setMessage(mContext.getString(0x7f060054));
        mSpinner.setCancelable(false);
        mSpinner.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialoginterface)
            {
                mWebView.stopLoading();
            }
        });
        view = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(0x7f030021, null);
        mWebView = (WebView)view.findViewById(0x7f0900a4);
        setUpWebView();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        android.widget.LinearLayout.LayoutParams layoutparams = new android.widget.LinearLayout.LayoutParams((int)((double)display.getWidth() - 0.050000000000000003D * (double)display.getWidth()), (int)((double)display.getHeight() - 0.20000000000000001D * (double)display.getWidth()));
        layoutparams.gravity = 17;
        addContentView(view, layoutparams);
        CookieSyncManager.createInstance(getContext());
        CookieManager.getInstance().removeAllCookie();
    }

    protected void onStop()
    {
        mWebView.stopLoading();
        if(mSpinner != null)
        {
            mSpinner.dismiss();
        }
        super.onStop();
    }




}
