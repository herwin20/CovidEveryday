package com.herwinlab.covideveryday.model;

public class DataRumahSakitModel {
    private String id,nama;
    public DataRumahSakitModel(String id, String nama){
        //ini provinsi
        this.id = id;
        this.nama = nama;
    }

    public String getId(){
        return this.id;
    }
    public String getNama(){
        return this.nama;
    }
}

