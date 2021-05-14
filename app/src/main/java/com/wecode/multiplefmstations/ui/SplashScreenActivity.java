package com.wecode.multiplefmstations.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.wecode.multiplefmstations.MainActivity;
import com.wecode.multiplefmstations.R;

public class SplashScreenActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            String version = pInfo.versionName;
            TextView textView = findViewById(R.id.versionTextView);
            textView.setText("V "+version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        handler=new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        },3000);

    }
}