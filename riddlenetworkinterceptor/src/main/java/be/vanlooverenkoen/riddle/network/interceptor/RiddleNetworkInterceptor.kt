package be.vanlooverenkoen.riddle.network.interceptor

import android.content.Context
import android.content.Intent
import android.os.Build
import be.vanlooverenkoen.riddle.network.interceptor.model.Headers
import be.vanlooverenkoen.riddle.network.interceptor.model.NetworkRequest
import be.vanlooverenkoen.riddle.network.interceptor.model.NetworkResponse
import be.vanlooverenkoen.riddle.network.interceptor.util.AppHelper
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.util.UUID

/**
 * @author Koen Van Looveren
 */
class RiddleNetworkInterceptor(private val context: Context, private val baseUrl: String? = null) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val callId = UUID.randomUUID().toString()
        val request = chain.request()
        val url = if (baseUrl == null) request.url().toString() else request.url().toString().replaceFirst(baseUrl, "")
        val method = request.method().toString().toUpperCase()
        val packageName = context.packageName
        val networkRequest = NetworkRequest(callId,
                method,
                baseUrl,
                url,
                bodyToString(request.body()),
                packageName,
                Headers(request.headers().toMultimap()))
        sendRequest(networkRequest)
        val response = chain.proceed(request)
        val networkResponse = NetworkResponse(callId,
                method,
                baseUrl,
                url,
                response.code(),
                response.body()?.string() ?: "",
                packageName,
                Headers(request.headers().toMultimap()))
        sendResponse(networkResponse)
        return response
    }

    private fun sendRequest(request: NetworkRequest) {
        if (!AppHelper.isPackageInstalled(PACKAGE, context.packageManager)) return
        val intent = Intent().apply {
            action = ACTION_LOG_NETWORK
            putExtra(ARG_DATA, request)
        }
        startService(intent)
    }

    private fun sendResponse(response: NetworkResponse) {
        if (!AppHelper.isPackageInstalled(PACKAGE, context.packageManager)) return
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

    private fun bodyToString(body: RequestBody?): String {
        body ?: return ""
        return try {
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "could not parse requestbody"
        }

    }

    companion object {
        private const val ARG_DATA = "data"

        private const val ACTION_LOG_NETWORK = "be.vanlooverenkoen.riddle.app.LOG_NETWORK"

        private const val PACKAGE = "be.vanlooverenkoen.riddle.app"
    }
}