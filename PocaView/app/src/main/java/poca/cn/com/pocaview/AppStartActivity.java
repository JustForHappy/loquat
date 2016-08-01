package poca.cn.com.pocaview;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;

public class AppStartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);

        SystemClock.sleep(300 * 1000);
    }
}
