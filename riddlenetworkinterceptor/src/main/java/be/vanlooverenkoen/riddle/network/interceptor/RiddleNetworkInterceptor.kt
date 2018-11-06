package be.vanlooverenkoen.riddle.network.interceptor

import android.app.Application
import android.content.Intent
import android.os.Build
import be.vanlooverenkoen.riddle.network.interceptor.model.NetworkRequest
import be.vanlooverenkoen.riddle.network.interceptor.model.NetworkResponse
import be.vanlooverenkoen.riddle.network.interceptor.util.AppHelper
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID

/**
 * @author Koen Van Looveren
 */
class RiddleNetworkInterceptor(application: Application) : Interceptor {
    private val context = application.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        val callId = UUID.randomUUID().toString()
        val request = chain.request()
        val networkRequest = NetworkRequest(callId,
                request.url().toString(),
                request.body()?.toString() ?: "")
        sendRequest(networkRequest)
        val response = chain.proceed(request)
        val networkResponse = NetworkResponse(callId,
                response.request().url().toString(),
                response.code(),
                response.body()?.toString() ?: "")
        sendResponse(networkResponse)
        return response
    }

    private fun sendRequest(request: NetworkRequest) {
        if (!AppHelper.isPackageInstalled(PACKAGE, context.packageManager)) return
        request.packageName = context.packageName
        val intent = Intent().apply {
            action = ACTION_LOG_NETWORK
            putExtra(ARG_DATA, request)
        }
        startService(intent)
    }

    private fun sendResponse(response: NetworkResponse) {
        if (!AppHelper.isPackageInstalled(PACKAGE, context.packageManager)) return
        response.packageName = context.packageName
        val intent = Intent().apply {
            action = ACTION_LOG_NETWORK
            putExtra(ARG_DATA, response)
        }
        startService(intent)
    }

    private fun startService(intent: Intent) {
        intent.setPackage(PACKAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    companion object {
        private const val ARG_DATA = "data"

        private const val ACTION_LOG_NETWORK = "be.vanlooverenkoen.riddle.app.LOG_NETWORK"

        private const val PACKAGE = "be.vanlooverenkoen.riddle.app"
    }
}