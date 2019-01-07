package com.monke.monkeybook;

import android.content.SharedPreferences;

import org.jsoup.helper.StringUtil;

import java.util.regex.Pattern;

public class ProxyManager {
    public static final String SP_KEY_PROXY_HTTP = "proxy_http";
    public static final String SP_KEY_PROXY_STATE = "proxy_state";
    public static final String PROXY_HTTP_DEFAULT = "";
    public static final boolean PROXY_STATE_DEFAULT = false;

    public static boolean proxyState;
    public static String proxyHttp;
    private static final String PROXY_HTTP_MATCH = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";//http正则表达式
    public static final String PROXY_PACKAGENAME_ENCODE = "代理包名加密key";   //代理包名加密key
    public static final String PROXY_PACKAGENAME_SPILT = "*"; //加密分隔符
    public static String packageName; //加密后的包名

    public static void saveProxyState(boolean state) {
        proxyState = state;
        SharedPreferences.Editor editor = MApplication.getInstance().getSharedPreferences("CONFIG", 0).edit();
        editor.putBoolean(SP_KEY_PROXY_STATE, proxyState);
        editor.commit();
    }

    private static void initProxyState() {
        try {
            packageName = MApplication.getInstance().getPackageManager().getPackageInfo(MApplication.getInstance().getPackageName(), 0).packageName;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=================包名获取失败，可能会影响代理请求功能=================");
        }
        proxyState = MApplication.getInstance().getSharedPreferences("CONFIG", 0).getBoolean(SP_KEY_PROXY_STATE, PROXY_STATE_DEFAULT);
    }

    public static void saveProxyHttp(String http) {
        proxyHttp = http;
        SharedPreferences.Editor editor = MApplication.getInstance().getSharedPreferences("CONFIG", 0).edit();
        editor.putString(SP_KEY_PROXY_HTTP, proxyHttp);
        editor.commit();
    }

    private static void initProxyHttp() {
        proxyHttp = MApplication.getInstance().getSharedPreferences("CONFIG", 0).getString(SP_KEY_PROXY_HTTP, PROXY_HTTP_DEFAULT);
    }

    public static void initProxy() {
        initProxyHttp();
        initProxyState();
        hasProxy();
    }

    public static boolean hasProxy() {
        if (!proxyState) {
            return false;
        }
        Pattern pattern = Pattern.compile(PROXY_HTTP_MATCH);
        if (!StringUtil.isBlank(proxyHttp) && pattern.matcher(proxyHttp).matches()) {
            return true;
        } else {
            saveProxyState(false);
            return false;
        }
    }
}
