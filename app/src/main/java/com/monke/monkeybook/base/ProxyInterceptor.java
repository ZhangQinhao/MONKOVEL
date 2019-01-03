package com.monke.monkeybook.base;

import com.monke.monkeybook.ProxyManager;

import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ProxyInterceptor implements Interceptor {
    public ProxyInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (ProxyManager.hasProxy()) {  //如果是代理模式则优先请求代理服务器，失败再自行本地请求
            String url = request.url().toString();
            if (!StringUtil.isBlank(url)) {
                url = URLEncoder.encode("url", "utf-8");
            }
            Request requestProxy = new Request.Builder()
                    .url(ProxyManager.proxyHttp)
                    .header("url", url)
                    .header("packagename",ProxyManager.packAgeEncode)
                    .get()
                    .build();
            Response responseProxy = chain.proceed(request);
            if(responseProxy.isSuccessful()){
                return responseProxy;
            }
        }
        Response response = chain.proceed(request);
        return response;
    }
}
