package poca.cn.com.ndkjnidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import poca.cn.com.ndkjnidemo.jniUtils.StringUtils;

public class AppStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(StringUtils.getStringFromC());
    }
}
