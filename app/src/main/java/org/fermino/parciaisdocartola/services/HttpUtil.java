package org.fermino.parciaisdocartola.services;

import com.loopj.android.http.*;

/**
 * Created by guihgf on 02/07/2017.
 */

public class HttpUtil {
    private static final String BASE_URL = "http://192.34.58.115/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        config();
        if(url.equals("http://192.168.1.148/cartola/")){
            client.get(url, params, responseHandler);
        }else{
            client.get(getAbsoluteUrl(url), params, responseHandler);
        }

    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        config();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void config(){
        client.setMaxRetriesAndTimeout(3, 30000);
        client.setTimeout(3000000);
        client.setUserAgent("android-async-http-1.4.9");
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
