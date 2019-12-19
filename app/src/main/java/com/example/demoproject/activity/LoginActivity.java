package com.example.demoproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.demoproject.R;
import com.example.demoproject.api.ApiClient;
import com.example.demoproject.init.SessionManager;
import com.example.demoproject.methods.method;
import com.example.demoproject.model.RequestModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public EditText ed_username, ed_password;
    public Button bt_login;
    public SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        bt_login = findViewById(R.id.bt_login);

        bt_login.setOnClickListener(v -> {
            if (!ed_username.getText().toString().isEmpty()) {
                operatorsLogin();
            }
        });
    }

    private void operatorsLogin() {
        RequestModel request = new RequestModel();
        request.username = "Admin";
        request.password = "Test@123";

        try {
            ApiClient.getSingletonApiClient().operatorsLogin(request, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String responseString = response.body();
                        method.savePreferences(LoginActivity.this, "Authorization", "Bearer "+responseString);
                        session.createLoginSession(responseString, "true");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("Login Request", "krishna Failed request : " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("TAG", "krishna Error Notifications :" + e.getMessage());
        }
    }
}
