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
        Response response = request(chain,request);
        return response;
    }

    private Response request(Chain chain,Request request) throws IOException{
        Response response = null;
        if (ProxyManager.hasProxy()) {  //如果是代理模式则优先请求代理服务器，失败再自行本地请求
            Request requestProxy = null;
            String url = request.url().toString();
            if (!StringUtil.isBlank(url)) {
                url = URLEncoder.encode(url, "utf-8");
            }
            Request.Builder requestProxyBuilder = new Request.Builder()
                    .url(ProxyManager.proxyHttp);
            requestProxyBuilder.headers(request.headers());
            requestProxyBuilder.header("proxyUrl", url)
                    .header("proxyPackagename",ProxyManager.packAgeEncode);
            if(request.method().equalsIgnoreCase("get")){
                requestProxyBuilder.get();
            }else{
                requestProxyBuilder.post(request.body());
            }
            requestProxy = requestProxyBuilder.build();
            try{
                response = chain.proceed(requestProxy);
                if(response.isSuccessful()){
                    return response;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(response!=null){
                    response.close();
                    response = null;
                }
            }
        }
        response = chain.proceed(request);
        return response;
    }
}