package me.edujtm.todoexample.domain.utils

import android.app.Activity
import android.content.Intent


inline fun <reified T : Activity> Activity.startActivity(
    noinline intentAddons: ((Intent) -> Unit)? = null
) {
    val intent = Intent(this, T::class.java)
    intentAddons?.let { intent.apply(it) }
    startActivity(intent)
}