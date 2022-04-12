package com.ulling.androidexsample.common;

import com.ulling.androidexsample.BuildConfig;
import com.ulling.lib.core.common.QcDefine;

/**
 * Class: Define
 * Version 1.0.0
 * Created by P139090 on 2018-12-28. 오후 3:35
 * <p>
 * Description:
 */
public class Define extends QcDefine {
    /**
     * 상용/ 테스트용
     */
    public static final boolean DEBUG_FLAG = BuildConfig.DEBUG;

    public static final int PAGE_SIZE = 20;

    public static final int HTTP_READ_TIMEOUT = 5000;
    public static final int HTTP_CONNECT_TIMEOUT = 4000;
    /**
     * TimeOut
     */
    public static final int INTRO_TIMEOUT = 1500;

    /**
     * time format
     */
    public static final String dateFormatFrom_sss = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String dateFormatFrom_ss = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String displayDateFormatFrom = "MM.dd aa hh:mm:ss";
    public static final String yyyyMMddFormatFrom = "yyyy MM.dd";


}
