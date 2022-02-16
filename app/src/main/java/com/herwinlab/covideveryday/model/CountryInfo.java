package com.herwinlab.covideveryday.model;
import com.google.gson.internal.ObjectConstructor;

public class CountryInfo {
    private final float _id;
    private final String iso2;
    private final String iso3;
    private final float lat;
    private final float longatt;
    private final String flag;

    public float get_id() {
        return _id;
    }

    public String getIso2() {
        return iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public float getLat() {
        return lat;
    }

    public float getLongatt() {
        return longatt;
    }

    public String getFlag() {
        return flag;
    }

    //Constructor
    public CountryInfo(float _id, String iso2, String iso3, float lat, float longatt, String flag) {
        this._id = _id;
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.lat = lat;
        this.longatt = longatt;
        this.flag = flag;
    }
}

