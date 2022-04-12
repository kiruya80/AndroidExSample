package com.ulling.androidexsample.component.clieckevent

import android.view.View
import java.util.*

class OnHasTermClickListener(private val click: (view: View) -> Unit): View.OnClickListener {

    override fun onClick(v: View) {

        val current = Calendar.getInstance().timeInMillis
        val clickTerm = current - LAST_CLICK_EVENT_TIME

        if(clickTerm < MIDDLE_EVENT_TERM) {
            return
        }

        LAST_CLICK_EVENT_TIME = current
        click(v)
    }
}

fun View.setOnHasTermClickListener(block: (view: View) -> Unit) {
    setOnClickListener(OnHasTermClickListener(block))
}
