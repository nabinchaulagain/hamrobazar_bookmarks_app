package com.example.tracker.misc;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.tracker.R;

public class ProgressDialog {
    Context context;
    String loadingMsg;
    AlertDialog dialog;

    public ProgressDialog(Context context, String loadingMsg) {
        this.context = context;
        this.loadingMsg = loadingMsg;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_progress,null);
        TextView loadingTextView = dialogView.findViewById(R.id.loadingTextView);
        loadingTextView.setText(loadingMsg);
        dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create();
    }

    public void show(){
        dialog.show();
    }
    public void hide(){
        dialog.hide();
    }
    public void dismiss(){
        dialog.dismiss();
    }
}
