package drag.mandala.com.modulea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;

@Router("LoginActivity")
public class LoginActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modulea_activity_login);

        try
        {
            Intent intent = getIntent();
//            Toast.makeText(LoginActivity.this, intent.getStringExtra("test"), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
