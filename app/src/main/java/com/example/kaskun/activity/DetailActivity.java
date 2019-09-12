package com.example.kaskun.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaskun.R;

public class DetailActivity extends AppCompatActivity {

    TextView nama,id, harga ;

    public  static String s_id="",s_nama="",s_harga="",s_image="" ;
    ImageView img ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        id = findViewById(R.id.id);
        nama = findViewById(R.id.nama);
        harga = findViewById(R.id.harga);
        img = findViewById(R.id.imagePrev);

        id.setText(s_id);
        nama.setText(s_nama);
        harga.setText(s_harga);


        Glide.with(this).load(s_image).into(img);







    }
}
