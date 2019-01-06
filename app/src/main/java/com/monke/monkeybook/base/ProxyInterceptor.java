package com.monke.monkeybook.base;

import com.monke.monkeybook.ProxyManager;

import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ProxyInterceptor implements Interceptor {
    public ProxyInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        if (ProxyManager.hasProxy()) {  //如果是代理模式则优先请求代理服务器，失败再自行本地请求
            //获取request的创建者builder
            Request.Builder builder = oldRequest.newBuilder();
            String oldUrl = oldRequest.url().toString();
            if (!StringUtil.isBlank(oldUrl)) {
                oldUrl = URLEncoder.encode(oldUrl, "utf-8");
            }
            HttpUrl newBaseUrl = HttpUrl.parse(ProxyManager.proxyHttp).newBuilder()
                    .setQueryParameter("proxyUrl",oldUrl)
                    .setQueryParameter("proxyPackagename",ProxyManager.packageEncode)
                    .build();
            try{
                Response response = chain.proceed(builder.url(newBaseUrl).build());
                if(response.isSuccessful()) {
                    return response;
                }
            }catch (Exception e){

            }
        }
        Response oldResponse = chain.proceed(oldRequest);
        return oldResponse;
    }
}