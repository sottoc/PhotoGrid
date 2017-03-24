package com.dexati.photogridbuilder.social.instagram.servercall;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpVersion;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpUtils
{
    public static class MySSLSocketFactory extends SSLSocketFactory
    {

        SSLContext sslContext;

        public Socket createSocket()
            throws IOException
        {
            return sslContext.getSocketFactory().createSocket();
        }

        public Socket createSocket(Socket socket, String s, int i, boolean flag)
            throws IOException, UnknownHostException
        {
            return sslContext.getSocketFactory().createSocket(socket, s, i, flag);
        }

        public MySSLSocketFactory(KeyStore keystore)
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
        {
            super(keystore);
            sslContext = SSLContext.getInstance("TLS");
            X509TrustManager x509trustmanager = new X509TrustManager(){
            	public void checkClientTrusted(X509Certificate ax509certificate[], String s)
                        throws CertificateException
                    {
                    }

                    public void checkServerTrusted(X509Certificate ax509certificate[], String s)
                        throws CertificateException
                    {
                    }

                    public X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }
            };
            sslContext.init(null, new TrustManager[] {
                x509trustmanager
            }, null);
        }
    }


    public HttpUtils()
    {
    }

    public static DefaultHttpClient getNewHttpClient()
    {
        DefaultHttpClient defaulthttpclient;
        try
        {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            MySSLSocketFactory mysslsocketfactory = new MySSLSocketFactory(keystore);
            mysslsocketfactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            BasicHttpParams basichttpparams = new BasicHttpParams();
            HttpProtocolParams.setVersion(basichttpparams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(basichttpparams, "UTF-8");
            SchemeRegistry schemeregistry = new SchemeRegistry();
            schemeregistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeregistry.register(new Scheme("https", mysslsocketfactory, 443));
            defaulthttpclient = new DefaultHttpClient(new ThreadSafeClientConnManager(basichttpparams, schemeregistry), basichttpparams);
        }
        catch(Exception exception)
        {
            return new DefaultHttpClient();
        }
        return defaulthttpclient;
    }

    public static boolean isNetworkAvail(Context context)
    {
        NetworkInfo networkinfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isConnected();
    }
}
