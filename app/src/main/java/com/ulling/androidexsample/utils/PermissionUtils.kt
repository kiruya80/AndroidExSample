package com.ulling.androidexsample.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ulling.androidexsample.common.permissionListRW

class PermissionUtils {

    enum class PermissionStatus {
        GRANTED, // 허용
        DENIED, // 거부
        SETTING // 설정으로
    }

    companion object {


        fun isReadWritePermission(
            mCtx: Context,
            requestPermissions: Array<out String>
        ): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Environment.isExternalStorageManager()
            } else {
                isAllCheckSelfPermission(mCtx, requestPermissions)
            }
        }

        fun isAllCheckSelfPermission(
            mCtx: Context,
            requestPermissions: Array<out String>
        ): Boolean {
            for (permission in requestPermissions) {
                // checkSelfPermission
                // 특정 권한이 PackageManager.PERMISSION_DENIED 인지, PackageManager.PERMISSION_GRANTED 인지 반환 한다.
                if (ContextCompat.checkSelfPermission(
                        mCtx,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        /**
         * android R (11, 30) 이상인 경우, 모든 파일에 접근 허용 필요
         */
        fun requestReadWritePermission(
            mCtx: Context,
            startForResultReadWrite: ActivityResultLauncher<Intent>,
            permissionMultiLauncher: ActivityResultLauncher<Array<String>>,
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    startForResultReadWrite.launch(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        addCategory("android.intent.category.DEFAULT")
                        data = Uri.parse(String.format("package:%s", mCtx.packageName))
                    })
                } catch (e: Exception) {
                    startForResultReadWrite.launch(
                        Intent().apply {
                            action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                        },
                    )
                }
            } else {
                //below android 11
                permissionMultiLauncher.launch(permissionListRW);
            }
        }

        fun isCheckSelfPermission(mCtx: Context, permission: String): Boolean {
            // checkSelfPermission
            // 특정 권한이 PackageManager.PERMISSION_DENIED 인지, PackageManager.PERMISSION_GRANTED 인지 반환 한다.
            if (ContextCompat.checkSelfPermission(
                    mCtx,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            return true
        }

        // 카메라 안함 / 오디오 이번만 허용/ 사진 미디어 엑서스 안함

        fun showDialogToGetPermission(
            mCtx: Context,
            msg: String,
            startForResultPermission: ActivityResultLauncher<Intent>
        ) {
            val builder = AlertDialog.Builder(mCtx)
            builder.setTitle("동의하지 않은 권한이 있습니다. ")
                .setMessage(msg + "\n\n모든 권한 동의 후 사용가능합니다.")

            builder.setPositiveButton("설정") { dialogInterface, i ->
                startForResultPermission.launch(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        data = Uri.parse(String.format("package:%s", mCtx.packageName))
                    },
                )
            }
            builder.setNegativeButton("종료") { dialogInterface, i ->

            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}