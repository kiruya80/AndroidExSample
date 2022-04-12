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

    companion object {


        fun isReadWritePermission(
            mCtx: Context,
            requestPermissions: Array<out String>
        ): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Environment.isExternalStorageManager()
            } else {
                isAllPermissionGranted(mCtx, requestPermissions)
            }
        }

        fun isAllPermissionGranted(mCtx: Context, requestPermissions: Array<out String>): Boolean {
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


         fun showDialogToGetPermission(
            mCtx: Context,
            startForResultPermission: ActivityResultLauncher<Intent>
        ) {
            val builder = AlertDialog.Builder(mCtx)
            builder.setTitle("동의하지 않은 권한이 있습니다. ")
                .setMessage("모든 권한 동의 후 사용가능합니다.")

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