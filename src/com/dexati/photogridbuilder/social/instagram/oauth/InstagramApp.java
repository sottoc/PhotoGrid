package com.dexati.photogridbuilder.social.instagram.oauth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONTokener;

// Referenced classes of package com.dexati.photogridbuilder.social.instagram.oauth:
//            InstagramSession, InstagramDialog

public class InstagramApp
{
    public static interface OAuthAuthenticationListener
    {

        public abstract void onFail(String s);

        public abstract void onSuccess();
    }


    private static final String API_URL = "https://api.instagram.com/v1";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TAG = "InstagramAPI";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;
    private static int WHAT_FINALIZE = 0;
    public static String mCallbackUrl = "";
    private String mAccessToken;
    private String mAuthUrl;
    private String mClientId;
    private String mClientSecret;
    private Context mCtx;
    private InstagramDialog mDialog;
    private Handler mHandler;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;
    private InstagramSession mSession;
    private String mTokenUrl;

    public InstagramApp(Context context, String s, String s1, String s2)
    {
        mHandler = new Handler() {
            public void handleMessage(Message message)
            {
                if(message.what != InstagramApp.WHAT_ERROR)
                {
                	if(message.what == InstagramApp.WHAT_FETCH_INFO)
                    {
                        mProgress.dismiss();
                        mListener.onSuccess();
                        return;
                    } else
                    {
                        mProgress.dismiss();
                        mListener.onSuccess();
                        return;
                    }
                }
                mProgress.dismiss();
                if(message.arg1 != 1){
                	if(message.arg1 != 2)
                    	return;
                    mListener.onFail("Failed to get user information");
                    return;
                }
                mListener.onFail("Failed to get access token");
                return;
            }
        };
        mClientId = s;
        mClientSecret = s1;
        mCtx = context;
        mSession = new InstagramSession(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = s2;
        mTokenUrl = (new StringBuilder("https://api.instagram.com/oauth/access_token?client_id=")).append(s).append("&client_secret=").append(s1).append("&redirect_uri=").append(mCallbackUrl).append("&grant_type=authorization_code").toString();
        mAuthUrl = (new StringBuilder("https://api.instagram.com/oauth/authorize/?client_id=")).append(s).append("&redirect_uri=").append(mCallbackUrl).append("&response_type=code&display=touch&scope=likes+comments+relationships").toString();
        InstagramDialog.OAuthDialogListener oauthdialoglistener = new InstagramDialog.OAuthDialogListener() {
            public void onComplete(String s3)
            {
                getAccessToken(s3);
            }

            public void onError(String s3)
            {
                mListener.onFail("Authorization failed");
            }
        };
        mDialog = new InstagramDialog(context, mAuthUrl, oauthdialoglistener);
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    private void fetchUserName()
    {
        mProgress.setMessage("Finalizing ...");
        (new Thread() {
            public void run()
            {
                Log.i("InstagramAPI", "Fetching user info");
                int i = InstagramApp.WHAT_FINALIZE;
                try
                {
                    URL url = new URL((new StringBuilder("https://api.instagram.com/v1/users/")).append(mSession.getId()).append("/?access_token=").append(mAccessToken).toString());
                    Log.d("InstagramAPI", (new StringBuilder("Opening URL ")).append(url.toString()).toString());
                    HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
                    httpurlconnection.setRequestMethod("GET");
                    httpurlconnection.setDoInput(true);
                    httpurlconnection.setDoOutput(true);
                    httpurlconnection.connect();
                    String s = streamToString(httpurlconnection.getInputStream());
                    System.out.println(s);
                    JSONObject jsonobject = (JSONObject)(new JSONTokener(s)).nextValue();
                    String s1 = jsonobject.getJSONObject("data").getString("full_name");
                    String s2 = jsonobject.getJSONObject("data").getString("bio");
                    Log.i("InstagramAPI", (new StringBuilder("Got name: ")).append(s1).append(", bio [").append(s2).append("]").toString());
                }
                catch(Exception exception)
                {
                    i = InstagramApp.WHAT_ERROR;
                    exception.printStackTrace();
                }
                mHandler.sendMessage(mHandler.obtainMessage(i, 2, 0));
            }
        }).start();
    }

    private void getAccessToken(final String code)
    {
        mProgress.setMessage("Getting access token ...");
        mProgress.show();
        (new Thread() {
            public void run()
            {
                Log.i("InstagramAPI", "Getting access token");
                int i = InstagramApp.WHAT_FETCH_INFO;
                try
                {
                    URL url = new URL("https://api.instagram.com/oauth/access_token");
                    Log.i("InstagramAPI", (new StringBuilder("Opening Token URL ")).append(url.toString()).toString());
                    HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
                    httpurlconnection.setRequestMethod("POST");
                    httpurlconnection.setDoInput(true);
                    httpurlconnection.setDoOutput(true);
                    OutputStreamWriter outputstreamwriter = new OutputStreamWriter(httpurlconnection.getOutputStream());
                    outputstreamwriter.write((new StringBuilder("client_id=")).append(mClientId).append("&client_secret=").append(mClientSecret).append("&grant_type=authorization_code").append("&redirect_uri=").append(InstagramApp.mCallbackUrl).append("&code=").append(code).toString());
                    outputstreamwriter.flush();
                    String s = streamToString(httpurlconnection.getInputStream());
                    Log.i("InstagramAPI", (new StringBuilder("response ")).append(s).toString());
                    JSONObject jsonobject = (JSONObject)(new JSONTokener(s)).nextValue();
                    mAccessToken = jsonobject.getString("access_token");
                    Log.i("InstagramAPI", (new StringBuilder("Got access token: ")).append(mAccessToken).toString());
                    String s1 = jsonobject.getJSONObject("user").getString("id");
                    String s2 = jsonobject.getJSONObject("user").getString("username");
                    String s3 = jsonobject.getJSONObject("user").getString("full_name");
                    String s4 = jsonobject.getJSONObject("user").getString("profile_picture");
                    mSession.storeAccessToken(mAccessToken, s1, s2, s3, s4);
                }
                catch(Exception exception)
                {
                    i = InstagramApp.WHAT_ERROR;
                    exception.printStackTrace();
                }
                mHandler.sendMessage(mHandler.obtainMessage(i, 1, 0));
            }
        }).start();
    }

    private String streamToString(InputStream inputstream)
        throws IOException
    {
        String s = "";
        try{
	        if(inputstream == null)
	        	 return s;
	        StringBuilder stringbuilder = new StringBuilder();
	        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
	        while(true){
		        String s1 = bufferedreader.readLine();
		        if(s1 != null)
		        {
		        	stringbuilder.append(s1);
		        	continue;
		        }
		        bufferedreader.close();
		        inputstream.close();
		        s = stringbuilder.toString();
		        return s;
	        }
        }catch(Exception e){
        	return s;
        }
    }

    public void authorize()
    {
        mDialog.show();
    }

    public String getAccessToken()
    {
        return mSession.getAccessToken();
    }

    public String getId()
    {
        return mSession.getId();
    }

    public String getName()
    {
        return mSession.getName();
    }

    public String getUserName()
    {
        return mSession.getUsername();
    }

    public boolean hasAccessToken()
    {
        return mAccessToken != null;
    }

    public void resetAccessToken()
    {
        if(mAccessToken != null)
        {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public void setListener(OAuthAuthenticationListener oauthauthenticationlistener)
    {
        mListener = oauthauthenticationlistener;
    }














}
