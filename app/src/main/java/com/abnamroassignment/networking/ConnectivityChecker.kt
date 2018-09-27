package com.abnamroassignment.networking

import android.content.Context
import android.net.ConnectivityManager

interface ConnectivityChecker {
    fun isNetworkConnected():Boolean
}

class ConnectivityCheckerImpl(context: Context):ConnectivityChecker {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isNetworkConnected() = connectivityManager.activeNetworkInfo?.isConnected == true
}