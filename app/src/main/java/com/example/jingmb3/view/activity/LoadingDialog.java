package com.example.jingmb3.view.activity;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.jingmb3.R;

public class LoadingDialog {
    public static LoadingDialog Instance;
    public static LoadingDialog getInstance(){
        if(Instance==null) return Instance=new LoadingDialog();
        return Instance;
    }
    ProgressDialog progressDialog;

    public void StartDialog(Context context){
        progressDialog=new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void StopDialog(){
        progressDialog.dismiss();
    }
}
