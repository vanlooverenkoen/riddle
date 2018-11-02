package be.vanlooverenkoen.riddle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import be.vanlooverenkoen.riddle.model.RiddleLog
import be.vanlooverenkoen.riddle.util.AppHelper

/**
 * @author Koen Van Looveren
 */
class Riddle private constructor(private val context: Context) {

    private val logTypes = mutableListOf(
            RiddleLogType.VERBOSE,
            RiddleLogType.INFO,
            RiddleLogType.DEBUG,
            RiddleLogType.WARN,
            RiddleLogType.ERROR,
            RiddleLogType.ASSERT
    )

    init {
        startNewSession()
    }

    private fun d(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.DEBUG))
            sendLog(RiddleLog.d(tag, content))
    }

    private fun e(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.ERROR))
            sendLog(RiddleLog.e(tag, content))
    }

    private fun i(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.INFO))
            sendLog(RiddleLog.i(tag, content))
    }

    private fun w(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.WARN))
            sendLog(RiddleLog.w(tag, content))
    }

    private fun a(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.ASSERT))
            sendLog(RiddleLog.a(tag, content))
    }

    private fun v(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.VERBOSE))
            sendLog(RiddleLog.v(tag, content))
    }

    private fun setLogTypes(vararg logTypes: RiddleLogType) {
        this.logTypes.clear()
        this.logTypes.addAll(logTypes)
    }

    private fun startNewSession() {
        if (!AppHelper.isPackageInstalled(PACKAGE, context.packageManager)) return
        val intent = Intent().apply {
            action = Riddle.ACTION_NEW_SESSION
            putExtra(ARG_NEW_SESSION, context.packageName)
            setPackage(PACKAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    private fun sendLog(log: RiddleLog) {
        if (!AppHelper.isPackageInstalled(PACKAGE, context.packageManager)) return
        log.packageName = context.packageName
        val intent = Intent().apply {
            action = Riddle.ACTION_LOG_DATA
            putExtra(Riddle.ARG_LOG, log)
            setPackage(PACKAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak") // When using the application context there is no risk for saving a Riddle instance. (Activity context will cause memory leaks)
        private var instance: Riddle? = null

        const val ARG_LOG = "RIDDLE_ARG_LOG"
        const val ARG_NEW_SESSION = "RIDDLE_ARG_NEW_SESSION"

        private const val ACTION_LOG_DATA = "be.vanlooverenkoen.riddle.app.LOG_DATA"
        private const val ACTION_NEW_SESSION = "be.vanlooverenkoen.riddle.app.LOG_NEW_SESSION"
        private const val PACKAGE = "be.vanlooverenkoen.riddle.app"

        //region Log Functions
        fun d(tag: String?, content: String) {
            instance?.d(tag, content)
        }

        fun d(content: String) {
            d(null, content)
        }

        fun e(tag: String?, content: String) {
            instance?.e(tag, content)
        }

        fun e(content: String) {
            e(null, content)
        }

        fun i(tag: String?, content: String) {
            instance?.i(tag, content)
        }

        fun i(content: String) {
            i(null, content)
        }

        fun w(tag: String?, content: String) {
            instance?.w(tag, content)
        }

        fun w(content: String) {
            w(null, content)
        }

        fun a(tag: String?, content: String) {
            instance?.a(tag, content)
        }

        fun a(content: String) {
            a(null, content)
        }

        fun v(tag: String?, content: String) {
            instance?.v(tag, content)
        }

        fun v(content: String) {
            v(null, content)
        }
        //endregion

        //region Settings
        /**
         * with will create a new instance of Riddle. It will be stored in a static
         *
         * @param context:Context pass the Application Context in your application class.
         *                        Activity context will cause memory leaks.
         */
        fun with(context: Context): Riddle.Companion {
            instance = Riddle(context)
            return this
        }

        /**
         * setLogType will enable logging for the given RiddleLogTypes
         *
         * @param logTypes: RiddleLogTypes[] the given types will be used to enable logging
         */
        fun setLogTypes(vararg logTypes: RiddleLogType): Riddle.Companion {
            if (instance == null) {
                throw UninitializedPropertyAccessException("Call Riddle.with(applicationContext) before setting the log types.")
            }
            instance!!.setLogTypes(* logTypes)
            return this
        }
        //endregion
    }
}