package com.ulling.androidexsample;

import android.content.Context;

import com.ulling.lib.core.base.QcBaseApplication;
import com.ulling.lib.core.utils.QcPreferences;


/**
 * Class: QUllingApplication
 * Created by KILHO on 10/01/2019 11:19 PM
 *
 * @version 1.0.0
 * <p>
 * Description:
 *
 *
 **/
public class QUllingApplication extends QcBaseApplication {
    private static QUllingApplication SINGLE_U;
    public static Context qCon;

    public static synchronized QUllingApplication getInstance() {
        return SINGLE_U;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * @MethodName : init
     * @Date : 2015. 3. 15.
     * @author : KILHO
     * @Method ㄴ 초기화
     * @변경이력
     */
    private synchronized void init() {
        SINGLE_U = this;
        qCon = getApplicationContext();
        APP_NAME = qCon.getResources().getString(R.string.app_name);

        QcPreferences.getInstance().getAPP_NAME();
    }

}