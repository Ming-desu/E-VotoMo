package com.qqdota.evotomo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.qqdota.evotomo.R;

public class MyDialog {
    private static MyDialog instance;

    public static MyDialog getInstance() {
        if (instance == null)
            instance = new MyDialog();
        return instance;
    }

    public Dialog createDialog(Context context, String title, String message) {
        // Create the dialog using the alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set to false so that the user cannot close the dialog
        builder.setCancelable(false);
        // Set the title and message for the dialog
        builder.setTitle(title);
        builder.setMessage(message);
        // Set the positive button to dismiss the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    public AlertDialog createLoading(Context context, String title, String message) {
        // Create the dialog using the alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set to false so that the user cannot close the dialog
        builder.setCancelable(false);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.uc_loading, null);

        // Set the title and message for the dialog
        TextView textViewTitle = view.findViewById(R.id.text_view_title);
        TextView textViewMessage = view.findViewById(R.id.text_view_message);

        textViewTitle.setText(title);
        textViewMessage.setText(message);

        // Set the custom UI layout for the dialog
        builder.setView(view);

        return builder.create();
    }
}
