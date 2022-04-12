package com.ulling.androidexsample.utils

import android.content.Intent
import com.ulling.lib.core.utils.QcLog

class Utils {
    companion object {

        fun printAllIntentParams(intent: Intent) {
            val _bundle = intent.extras

            if (_bundle == null) {
                QcLog.d("intent bundle is empty!!")
                return
            }

            var _value: String = "\n\nBundle {"
            for (key in _bundle.keySet()) {
                _value += "\n    " + key + " => " + _bundle.get(key) + ";"
            }
            _value += "\n}"
            QcLog.e(_value)
        }
    }
}