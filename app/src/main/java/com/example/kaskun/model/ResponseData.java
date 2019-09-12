package com.example.kaskun.model;

import com.google.gson.annotations.SerializedName;


public class ResponseData {

    @SerializedName("success")
    String success;
    @SerializedName("message")
    String message;
    @SerializedName("username")
    String username;


    @SerializedName("nama")
    String nama;
    @SerializedName("harga")
    String harga;
    @SerializedName("foto")
    String foto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    String id;


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }






    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ResponseData(String success,  String username,String message, String nama, String foto , String harga, String id) {
        this.success = success;
        this.username = username;
        this.message = message;
        this.nama = nama;
        this.harga = harga;
        this.foto = foto;
        this.id = id;

    }


}
