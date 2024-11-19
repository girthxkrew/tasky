package com.rkm.tasky.util.intent

import android.content.Intent
import android.os.Build
import android.os.Parcelable

inline fun <reified T: Parcelable> Intent.getCompatParcelableExtra(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
       getParcelableExtra(name, T::class.java)
    } else {
        getParcelableExtra<T>(name)
    }

}