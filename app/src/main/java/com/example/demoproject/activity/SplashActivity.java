package com.example.demoproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.example.demoproject.R;
import com.example.demoproject.init.SessionManager;
import com.example.demoproject.methods.AppConstants;
import com.example.demoproject.methods.method;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.example.demoproject.init.ApplicationAppContext.getAppContext;

public class SplashActivity extends AppCompatActivity {

    public Handler handler;
    public SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionManager(getApplicationContext());
        handler = new Handler();

        AppConstants.CONTEXT = SplashActivity.this;
        AppConstants.TOKEN = method.getPreferences(getAppContext(), "Authorization");
        getKeyHash();

        handler.postDelayed(() -> {
            if (session.isLoggedIn()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }

    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
