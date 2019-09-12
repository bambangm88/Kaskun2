package com.example.kaskun.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kaskun.activity.MainActivity;
import com.example.kaskun.R;
import com.example.kaskun.api.ApiService;
import com.example.kaskun.api.Server;
import com.example.kaskun.model.ResponseData;
import com.example.kaskun.pref.SesionManager;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {



    @BindView(R.id.edtUser)
    EditText edtusername;
    @BindView(R.id.edtPass)
    EditText password;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    ApiService API;
    SesionManager session;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(LoginActivity.this);

        API = Server.getAPIService();

        session = new SesionManager(getApplicationContext());

//        session.checkLogin();

        if (session.isLoggedIn()) {

            Intent i = new Intent(LoginActivity.this, MainActivity.class);

            startActivity(i);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = edtusername.getText().toString();
                String pass = password.getText().toString();

                if (validateLogin(user, pass)){ //validasi
                    //login
                    doLogin(user, pass); //login

                }
            }
        });
    }



    //validasi login
    private boolean validateLogin(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Masukkan Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Masukkan Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void doLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        API.login(username, password).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if (response.isSuccessful()){
                    ResponseData responseData = response.body();

                    if(responseData.getSuccess().equals("1")){
                        pDialog.cancel();

                        String username = response.body().getUsername();

                       session.createLoginSession(username);


                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                    }else {
                        pDialog.cancel();
                        Toast.makeText(LoginActivity.this, "Username atau pass salah", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    pDialog.cancel();
                    Toast.makeText(LoginActivity.this, "Cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
