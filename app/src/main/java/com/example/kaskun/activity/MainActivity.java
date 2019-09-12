package com.example.kaskun.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaskun.Login.LoginActivity;

import com.example.kaskun.R;
import com.example.kaskun.api.ApiService;
import com.example.kaskun.api.Server;
import com.example.kaskun.model.ResponseData;
import com.example.kaskun.pref.SesionManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.kaskun.pref.SesionManager.KEY_STATUS;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 100;

    public static final int PERMISSION_REQUEST = 200;


    ApiService API;
    SesionManager sesion;
    ImageButton btn_data_1,btn_tentang,btn_petunjuk ;
    ImageButton btn_logOut;



    SesionManager session;
    ProgressDialog pDialog;

    private IntentIntegrator intentIntegrator;

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.satu, R.drawable.dua, R.drawable.tiga, R.drawable.empat};

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);

        }

        API = Server.getAPIService();

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);


        sesion = new SesionManager(this);
        HashMap user= sesion.getUserDetails();


        btn_data_1 = findViewById(R.id.btn_menu_1);
        btn_logOut = findViewById(R.id.btn_menu_4);
        btn_tentang = findViewById(R.id.btn_menu_3);
        btn_petunjuk = findViewById(R.id.btn_menu_2);

        btn_data_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.initiateScan();      //method to turn on

            }
        });

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             alert_keluar();

            }
        });

        btn_tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tentang = new Intent(MainActivity.this,tentang.class);
                startActivity(tentang);

            }
        });

        btn_petunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent petunjuk = new Intent(MainActivity.this,petunjuk.class);
                startActivity(petunjuk);


            }
        });



    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };


    void alert_keluar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Apakah yakin akan keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //logOut
                sesion.logoutUser();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{

                try{

                    String id = result.getContents();

                    cekOri(id);



                }catch (Exception e){
                    e.printStackTrace();
                    // jika format encoded tidak sesuai maka hasil
                    // ditampilkan ke toast
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }






    private void cekOri(String id) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        API.cekOriginalitas(id).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if (response.isSuccessful()){
                    ResponseData responseData = response.body();

                    if(responseData.getSuccess().equals("1")){
                        pDialog.cancel();

                        DetailActivity a = new DetailActivity() ;

                        String id = response.body().getId();
                        String nama = response.body().getNama();
                        String harga = response.body().getHarga();
                        String foto = response.body().getFoto();

                        a.s_id = id ;
                        a.s_harga = harga ;
                        a.s_nama = nama ;
                        a.s_image = "https://kaskun.000webhostapp.com/kaskun/assets/images/upload/"+foto ;

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        startActivity(intent);

                    }else {
                        pDialog.cancel();
                        Toast.makeText(MainActivity.this, "Produk Tidak Ada", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    pDialog.cancel();
                    Toast.makeText(MainActivity.this, "Cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                pDialog.cancel();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
