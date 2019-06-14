package drag.mandala.com.componentapp.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by hanzj on 2018/12/10.
 */

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("MyApplication","MyApplication");
        //可以理解为第三方sdk的初始化
        Log.e("LoginApplication","LoginApplication");
        Log.e("RegisterApplication","RegisterApplication");
    }

    public static HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
