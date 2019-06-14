package drag.mandala.com.modulea.debug;

import android.app.Application;
import android.util.Log;

/**
 * Created by hanzj on 2018/12/10.
 */

public class LoginApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("LoginApplication","LoginApplication");
    }
}
