package com.example.novelrepo.util

import android.content.Context

object ResHelper {
    fun drawableId(context: Context, name: String?, fallbackRes: Int): Int {
        if (name.isNullOrBlank()) return fallbackRes
        val pkg = context.packageName
        val normalized = name.trim().lowercase().replace(' ', '_').replace('-', '_')
        val id = context.resources.getIdentifier(normalized, "drawable", pkg)
        return if (id != 0) id else fallbackRes
    }
}
