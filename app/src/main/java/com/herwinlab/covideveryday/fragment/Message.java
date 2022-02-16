package com.herwinlab.covideveryday.fragment;

import android.content.Context;
import androidx.core.app.ActivityCompat;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Message {
    private SweetAlertDialog sw;
    private Context c;
    public Message(Context c){
        this.c = c;
    }


    public void error(String title, String msg){
        sw = new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(msg);
        sw.show();
    }

    public void hide(){
        sw.hide();
    }
}
