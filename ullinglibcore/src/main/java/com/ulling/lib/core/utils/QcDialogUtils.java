package com.ulling.lib.core.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

/**
 *
 * 
 * Class: QcDialogUtils
 * Created by 최길호 on 2019-01-14 오전 10:09
 * @version 1.0.0
 *
 * Description:
 *
 **/
public class QcDialogUtils {
    private  static   QcDialogUtils instance = new QcDialogUtils();

    public static QcDialogUtils getInstance() {
        if (instance == null)
            instance = new QcDialogUtils();
        return instance;
    } 

    public QcDialogUtils() {
    }
    
    public ProgressDialog showProgressDialog(Context mCtx, String msg) {
        ProgressDialog asyncDialog = new ProgressDialog(mCtx);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage(msg);
        asyncDialog.setCancelable(false);

        return asyncDialog;
    }

    public interface DialogListener {
        void onClickListener(Boolean positive);
    }
    
    public void showDefaultDialog(Context mCtx,
                                  String title,
                                  String msg,
                                  boolean isOneBtn,
                                  String msgPositive,
                                  String msgNegative,
                                  final DialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(msgPositive,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null)
                            listener.onClickListener(true);
                    }
                });
        if (!isOneBtn)
            builder.setNegativeButton(msgNegative,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (listener != null)
                                listener.onClickListener(false);
                        }
                    });
        builder.show();
    }

}
