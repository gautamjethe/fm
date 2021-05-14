package com.wecode.multiplefmstations.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.wecode.multiplefmstations.R;

public class CustomProgressBar {
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Context context;

    public CustomProgressBar(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();

    }

    public void showProgressBar() {
        dialog.show(); // to show this dialog
    }

    public void hideProgressBar() {
        if (dialog != null)
            dialog.dismiss(); // to hide this dialog

    }

}
