package com.example.hamrobookmark.receivers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.Button;

import com.example.hamrobookmark.misc.Constants;

public class NetworkChangeReceiver extends BroadcastReceiver {
    boolean isConnected;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean isNotConnected = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            isConnected = !isNotConnected;
            if (isNotConnected) {
                showAlert(context);
            }
        }
    }

    public void showAlert(final Context context){
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setPositiveButton("Retry", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isConnected){
                            dialog.dismiss();
                            Intent intent = new Intent(Constants.ACTIVITY_REFRESH_ACTION);
                            context.sendBroadcast(intent);
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }
}
