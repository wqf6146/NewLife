package com.vise.xsnow.net.core;

import android.content.Context;

import com.vise.xsnow.net.mode.CookiesStore;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @Description: Cookie
 */
public class ApiCookie implements CookieJar {

    private final Context mContext;
    private CookiesStore cookieStore;

    public ApiCookie(Context context) {
        mContext = context;
        if (cookieStore == null) {
            cookieStore = new CookiesStore(mContext);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

}
