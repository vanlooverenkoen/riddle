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
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException
import java.util.UUID

/**
 * @author Koen Van Looveren
 */
class RiddleNetworkInterceptor(private val context: Context, private val baseUrl: String? = null) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val callId = UUID.randomUUID().toString()

        //Request
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder().build()
        val url = if (baseUrl == null) originalRequest.url().toString() else originalRequest.url().toString().replaceFirst(baseUrl, "")
        val method = originalRequest.method().toString().toUpperCase()
        val packageName = context.packageName
        val networkRequest = NetworkRequest(callId,
                method,
                baseUrl,
                url,
                bodyToString(originalRequest.body()),
                packageName,
                Headers(originalRequest.headers().toMultimap()))
        sendRequest(networkRequest)

        //Response
        val response = chain.proceed(newRequest)
        val newResponse = response.newBuilder().build()
        val networkResponse = NetworkResponse(callId,
                method,
                baseUrl,
                url,
                response.code(),
                bodyToString(response.body()),
                packageName,
                Headers(response.headers().toMultimap()))
        sendResponse(networkResponse)
        return newResponse
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

    private fun bodyToString(body: ResponseBody?): String {
        body ?: return ""
        return try {
            val source = body.source()
            source.request(Long.MAX_VALUE)
            String(source.buffer().clone().readByteArray())
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