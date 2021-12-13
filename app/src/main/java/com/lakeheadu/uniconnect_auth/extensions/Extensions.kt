package com.lakeheadu.uniconnect_auth.extensions

import android.app.Activity
import android.widget.Toast

/**
 * Utility methods for repeat tasks
 */
object Extensions {

    /**
     * Show a toast message
     * @param msg A string to show in a toast
     * @author tahmidul
     */
    fun Activity.toast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}